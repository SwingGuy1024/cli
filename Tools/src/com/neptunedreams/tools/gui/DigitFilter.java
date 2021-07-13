package com.neptunedreams.tools.gui;

import java.lang.reflect.Field;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>Experiments to filter out non-numeric characters from a String</p>
 * <p>Conclusion:<br>
 * 1. Regex is slowest<br>
 * 2. Precompiled regex is significantly faster<br>
 * 3. Single-char regex (/D vs. /D*) is faster still<br>
 * 4. Imperative is fastest.<br>
 * 5. Integer Collector is the fastest functional solution<br>
 * 6. Functional solutions are faster with non-western digits than western digits. (This makes no sense.)<br>
 * 7. Regex fails with non-western digits.</p>
 * <p>
 * Winners:<br>
 * 1 0.088 탎 Imperative<br>
 * 2 0.263 탎 Integer Collector<br>
 * 3 0.296 탎 Single-char Pre-compiled Regex: "\\D"<br>
 * 4 0.416 탎 Character Collector 2<br>
 * 5 0.451 탎 String Collector 2<br>
 * 6 0.452 탎 Character Collector 1<br>
 * 7 0.496 탎 String Collector 1<br>
 * 8 0.594 탎 Pre-Compiled Regex: "\\D*"<br>
 * 9 0.717 탎 String.replaceAll() (Regex): "\\D*"<br>
 * </p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/13/21
 * <p>Time: 4:41 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum DigitFilter {
  ;
  private static final List<String> testCases = Arrays.asList(
      "0123456789", // sRgx
      "abc123def456G7890", // sRgx
      "(323) 225-7285", // sRgx
      "323.225.7285", // sRgx
      "323/225-2785", // sRgx
      "313/425-7075",
      "313-425-7075",
      "313.425.7075",
      "(313) 425-7075",
      "[323] 225 - 7285" // sRgx

      // non-ascii digits
//      "\u0660\u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669", // sRgx
//      "\u0660\u0661\u0662-\u0663\u0664\u0665-\u0666\u0667\u0668\u0669", // Reg2
//      "\u09E9\u09EA\u09EB-\u09EC\u09ED\u09EE\u09EF-\u0A66\u0A67\u0A68", // Reg2
//      "\u09E9\u09EB-\u09EC\u09ED\u09EE\u09EF-\u0A66\u0A67\u0A68\u0A69", // Reg2
//      "(\u0660\u0661\u0662)\u0663\u0664\u0665-\u0666\u0667\u0668\u0669", // sRgx
//      "\u0660\u0661\u0662.\u0663\u0664\u0665.\u0666\u0667\u0668\u0669", // sRgx
//      "\u0660\u0661\u0662/\u0663\u0664\u0665-\u0666\u0667\u0668\u0669", // sRgx
//      "[\u0660\u0661\u0662] \u0663\u0664\u0665 - \u0666\u0667\u0668\u0669", // sRgx
//      "\u0660.\u0661.\u0662.\u0663.\u0664\u0665\u0666\u0667\u0668\u0669", // sRgx
//      "\u0660\u0661\u0662\u0663.\u0664.\u0665\u0666.\u0667\u0668\u0669" // sRgx
  );
  
  private static final Map<Byte, String> dirMap = createDirMap();
  
  private static Map<Byte, String> createDirMap() {
    Map<Byte, String> map = new HashMap<>();
    Field[] fields = Character.class.getDeclaredFields();
    try {
      final String lead = "DIRECTIONALITY_";
      for (Field f: fields) {
        if (f.getName().startsWith(lead)) {
          String name = f.getName().substring(lead.length());
          map.put(f.getByte(null), name);
        }
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return map;
  }

  public static void main(String[] args) {
    System.out.printf("%17s    %16s    %16s    %16s    %16s    %16s    %16s    %16s    %16s    %16s    %16s    %16s    %16s%n", 
        "Original", 
        "Regex", "Regex-Precompile", "Regix-Single-char", "Rgx, 1 chr PrCmp", "Coll. String", "Coll(***) String", "Coll(***) Int", "Coll. Char", "Coll(***) Char", "Imperative", "Imp, Char Iter", "Imp, Cheat"); // NON-NLS
    for (String s: testCases) {
      System.out.printf("%17s 1: %16s 2: %16s 3: %16s 4: %16s 5: %16s 6: %16s 7: %16s 8: %16s 9: %16s A: %16s B: %16s C: %16s%n", 
          s, 
          regex(s), 
          rxPre(s),
          regexSingle(s),
          regexSngPre(s),
          stringCollector(s),
          stringCollector2(s),
          intCollector(s),
          charCollector(s),
          charCollector2(s),
          imperative(s),
          imperativeCharItr(s),
          impIf(s)
      ); // NON-NLS
    }

    System.out.println("---");
    System.out.println("All Results in 탎");
    System.out.printf("%12s\t%12s\t%12s\t%12s\t%12s\t%12s\t%12s\t%12s\t%12s\t%12s\t%12s\t%12s%n", "Regex", "Pre-Regex", "Sng-Regex", "Sng-PreRx", "String Clctr", "String Clctr 2", "Int Clctr", "Char Clctr", "Char Clctr 2", "Imperative", "Imp-ChrItr", "Imp-Cheat"); // NON-NLS
    Timer tRegex = new Timer(DigitFilter::regex);
    Timer tRegx2 = new Timer(DigitFilter::rxPre);
    Timer tSglRx = new Timer(DigitFilter::regexSingle);
    Timer txSPre = new Timer(DigitFilter::regexSngPre);
    Timer tStCol = new Timer(DigitFilter::stringCollector);
    Timer tStCl2 = new Timer(DigitFilter::stringCollector2);
    Timer tInCol = new Timer(DigitFilter::intCollector);
    Timer tChCol = new Timer(DigitFilter::charCollector);
    Timer tChCl2 = new Timer(DigitFilter::charCollector2);
    Timer tImper = new Timer(DigitFilter::imperative);
    Timer tImpr2 = new Timer(DigitFilter::imperativeCharItr);
    Timer tImpr3 = new Timer(DigitFilter::impIf);

    for (int i=0; i<500; ++i) {
      System.out.printf(
          "%12.6f\t%12.6f\t%12.6f\t%12.6f\t%12.6f\t%12.6f\t%12.6f\t%12.6f\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n",
          tRegex.time(),
          tRegx2.time(),
          tSglRx.time(),
          txSPre.time(),
          tStCol.time(),
          tStCl2.time(),
          tInCol.time(),
          tChCol.time(),
          tChCl2.time(),
          tImper.time(),
          tImpr2.time(),
          tImpr3.time()
      ); // NON-NLS
    }
    
    // I manually sorted these by speed, although the order does sometimes change. They go fastest to slowest.
    System.out.printf("Median: tImper =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tImper.median(), tImper.qMedian(), tImper.start(), tImper.high()); // NON-NLS
    System.out.printf("Median: tImItr =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tImpr2.median(), tImpr2.qMedian(), tImpr2.start(), tImpr2.high()); // NON-NLS
    System.out.printf("Median: tImCht =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tImpr3.median(), tImpr3.qMedian(), tImpr3.start(), tImpr3.high()); // NON-NLS
    System.out.printf("Median: tInCol =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tInCol.median(), tInCol.qMedian(), tInCol.start(), tInCol.high()); // NON-NLS
    System.out.printf("Median: tSxPre =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", txSPre.median(), txSPre.qMedian(), txSPre.start(), txSPre.high()); // NON-NLS
    System.out.printf("Median: tSglRx =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tSglRx.median(), tSglRx.qMedian(), tSglRx.start(), tSglRx.high()); // NON-NLS
    System.out.printf("Median: tChCl2 =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tChCl2.median(), tChCl2.qMedian(), tChCl2.start(), tChCl2.high()); // NON-NLS
    System.out.printf("Median: tStCl2 =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tStCl2.median(), tStCl2.qMedian(), tStCl2.start(), tStCl2.high()); // NON-NLS
    System.out.printf("Median: tChCol =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tChCol.median(), tChCol.qMedian(), tChCol.start(), tChCol.high()); // NON-NLS
    System.out.printf("Median: tStCol =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tStCol.median(), tStCol.qMedian(), tStCol.start(), tStCol.high()); // NON-NLS
    System.out.printf("Median: tRxPre =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tRegx2.median(), tRegx2.qMedian(), tRegx2.start(), tRegx2.high()); // NON-NLS
    System.out.printf("Median: tRegex =\t%12.6f\t%12.6f\t%12.6f\t%12.6f%n", tRegex.median(), tRegex.qMedian(), tRegex.start(), tRegex.high()); // NON-NLS

//    for (char c=27; c<0xFF; ++c) {
//      System.out.printf("%1c = 0x%04x: %s%n", c, (int)c, getDirectionality(c)); // NON-NLS
//    }
//    for (char c=0x0600; c<0x0700; ++c) {
//      if (Character.isDefined(c)) {
//        System.out.printf("%1c = 0x%04x: %s%n", c, (int) c, getDirectionality(c)); // NON-NLS
//      }
//    }
//    System.out.println("\n---\n");
//    System.out.printf("%s%n", "\u06a8\u06ad\u0660\u0661\u0662\u0677\u0678\u06f1\u06f2\u06f3\u06fa\u06fd"); // NON-NLS
  }

//  private static String getDirectionality(char c) {
//    byte dir = Character.getDirectionality(c);
//    return dirMap.get(dir);
//  }

  private static class Timer {
    private final LinkedList<Double> results = new LinkedList<>();
    private final UnaryOperator<String> function;

    Timer(UnaryOperator<String> function) {
      this.function = function;
    }

    public double time() {
      double nanos = 0;
      long caseCount = 100;
      for (int i = 0; i < caseCount; ++i) {
        for (String tCase : testCases) {
          long start = System.nanoTime();
          function.apply(tCase);
          long end = System.nanoTime() - start;
          nanos += end;
        }
      }
      final double time = ((nanos) / (double) (caseCount * 1000L))/testCases.size();
      results.add(time);
      return time;
    }
    
    public double median() {
      Collections.sort(results);
      return results.get(results.size()/2);
    }
    
    public double qMedian() {
      Collections.sort(results);
      return results.get(results.size()/4);
    }
    
    public double start() {
      Collections.sort(results);
      return results.get(0);
    }
    
    public double high() {
      Collections.sort(results);
      return results.getLast();
    }
  }

  private static final Pattern NON_DIGIT_SERIES = Pattern.compile("\\D*");
  private static final Pattern SINGLE_NON_DIGIT = Pattern.compile("\\D");

  private static String regex(String s) {
    // Same as s.replaceAll("\\D*", "");
    return Pattern.compile("\\D*").matcher(s).replaceAll("");
  }

  private static String rxPre(String s) {
    return NON_DIGIT_SERIES.matcher(s).replaceAll("");
  }
  
  private static String regexSngPre(String s) {
    return SINGLE_NON_DIGIT.matcher(s).replaceAll("");
  }

  private static String regexSingle(String s) {
    return Pattern.compile("\\D").matcher(s).replaceAll("");
  }

  /**
   * Filter out all non-digit characters from a String. I could say s.replaceAll("\\D*", ""), but that only works with
   * Locales that use western numerals. This works with all Locales.
   * @param s The string to strip.
   * @return A string consisting of only numeric digits, which means characters for which Character::isDigit returns true.
   */
  public static String intCollector(String s) {
    return s.chars()
        .filter(Character::isDigit)
        // This stream doesn't have a collect() method that takes a Collector!
        .collect(
            StringBuilder::new,                                  // Supplier<StringDigit>
            (stringBuilder, i) -> stringBuilder.append((char)i), // ObjectIntConsumer<StringBuilder>
//            (b, b2) -> b.append(b2.toString())                   // BiConsumer<StringBuilder, StringBuilder>
            // The third parameter is only used with spliterators, but it can't be null.
            prohibitSpliterator()
        ).toString();
  }

  /**
   * This is the easiest to write, but the slowest of the functional methods. 
   * @param s the String
   * @return the String, stripped of non-numeric characters.
   */
  private static String stringCollector(String s) {
    return s.chars()
        .filter(Character::isDigit)
        .mapToObj(c -> String.valueOf((char) c))
        .collect(Collectors.joining());
  }

  private static String stringCollector2(String s) {
    return s.chars()
        .filter(Character::isDigit)
        .mapToObj(c -> String.valueOf((char) c))
        .collect(
            StringBuilder::new,                  // Supplier
            StringBuilder::append,               // Accumulator
//            (b1, b2) -> b1.append(b2.toString()) // Combiner, only used by spliterators
            prohibitSpliterator()
        )
        .toString();
  }

  private static String charCollector(String s) {
    Collector<Character, StringBuilder, String> collector = new Collector<>() {
      @Override public Supplier<StringBuilder> supplier() { return StringBuilder::new; }
      @Override public BiConsumer<StringBuilder, Character> accumulator() { return StringBuilder::append; }
      @Override public Function<StringBuilder, String> finisher() { return StringBuilder::toString; }
      @Override public Set<Characteristics> characteristics() { return EnumSet.noneOf(Collector.Characteristics.class); }
      @Override public BinaryOperator<StringBuilder> combiner() {
//        throw new AssertionError("Combiner is necessary");
        return (stringBuilder, stringBuilder2) -> {
//          stringBuilder.append(stringBuilder2.toString());
          prohibitSpliterator().accept(stringBuilder, stringBuilder2);
          return stringBuilder;
        };
//        final BiConsumer<Object, Object> objectObjectBiConsumer = prohibitSpliterator();
//        return objectObjectBiConsumer;
      }
    };
    return s.chars()
        .filter(Character::isDigit)
        .mapToObj(c -> (char) c)
        .collect(collector);
  }

  private static String charCollector2(String s) {
    return s.chars()
        .filter(Character::isDigit)
        .mapToObj(c -> (char)c)
        .collect(
            StringBuilder::new,                 // Supplier
            (StringBuilder::append),            // Accumulator
            prohibitSpliterator()
//            (b, b2) -> b.append(b2.toString())  // Combiner, only used by spliterators
        ).toString();
  }

  private static String imperative(String s) {
    StringBuilder builder = new StringBuilder(s.length());
    for (int i=0; i<s.length(); ++i) {
      char digit = s.charAt(i);
      if (Character.isDigit(digit)) {
        builder.append(digit);
      }
    }
    return builder.toString();
  }
  
  private static String imperativeCharItr(String s) {
    StringBuilder builder = new StringBuilder(s.length());
    CharacterIterator characterIterator = new StringCharacterIterator(s);
    char c = characterIterator.first();
    while (c != CharacterIterator.DONE) {
      if (Character.isDigit(c)) {
        builder.append(c);
      }
      c = characterIterator.next();
    }
    return builder.toString();
  }

  private static String impIf(String s) {
    StringBuilder builder = new StringBuilder(s.length());
    for (int i = 0; i < s.length(); ++i) {
      char digit = s.charAt(i);
      if ((digit >= '0') && (digit <= '9')) {
        builder.append(digit);
      }
    }
    return builder.toString();
  }

  // Not sure if this is ever actually necessary. If it is, it's a good way to prevent an operator from getting
  // used in a spliterator.
  @SuppressWarnings("unchecked")
  private static <T> BiConsumer<T, T> prohibitSpliterator() { return (BiConsumer<T, T>) PROHIBIT_SPLITERATOR; }
  private static final BiConsumer<?, ?> PROHIBIT_SPLITERATOR = (a, b) -> {
    throw new IllegalStateException("Spliterator use illegal with this accumulator");
  };
}
