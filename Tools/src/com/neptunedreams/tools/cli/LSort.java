package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 5/1/24</p>
 * <p>Time: 1:00?AM</p>
 * <p>@author Miguel Mu�oz</p>
 */
public enum LSort {
  ;
  private static final StringBuilderCollector instance = new StringBuilderCollector();

  public static void main(String[] args) {
    String requiredLetters = "";
    int wordCount = args.length;
    if (args[0].startsWith("-")) {
      requiredLetters = args[0].substring(1).toUpperCase();
      wordCount--;
      args = Arrays.copyOfRange(args, 1, args.length);
    }
    final int finalWordCount = wordCount;

    StringBuilder builder = String
        .join("", args)
        .toUpperCase()
        .chars()
        .filter(Character::isLetter)
        .boxed()
        .sorted()
        .collect(instance);
//    StringBuilder builder = new StringBuilder(cList.size());
//    cList.forEach(i -> builder.append((char)(i & 0xFFFF)));
    
    // Strip out required letters
    requiredLetters
        .chars()
        .forEach(i -> {
          int lSpot = builder.indexOf(String.valueOf((char)i));
          if (lSpot >= 0) {
            builder.delete(lSpot, lSpot+finalWordCount);
          }
        });

    int i = builder.length();
    char previous = builder.charAt(--i);
    while (i > 0) {
      char currentChar = builder.charAt(--i);
      if (currentChar != previous) {
        builder.insert(i+1, ' ');
      }
      previous = currentChar;
    }
    final String result = builder.toString();
    StringSelection stringSelection = new StringSelection(result);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
    System.out.println(result);
  }

  static class StringBuilderCollector implements Collector<Integer, StringBuilder, StringBuilder> {
    private final Supplier<StringBuilder> supplier = StringBuilder::new;
    private final BiConsumer<StringBuilder, Integer> accumulator = (sb, i) -> sb.append((char) i.intValue());
    private final BinaryOperator<StringBuilder> combiner = (t, u) -> t.append(u.toString());
    private final Function<StringBuilder, StringBuilder> finisher = s -> s;
    private final Set<Characteristics> characteristics = Collections.emptySet();


    @Override
    public Supplier<StringBuilder> supplier() {
      return supplier;
    }

    @Override
    public BiConsumer<StringBuilder, Integer> accumulator() {
      return accumulator;
    }

    @Override
    public BinaryOperator<StringBuilder> combiner() {
      return combiner;
    }

    @Override
    public Function<StringBuilder, StringBuilder> finisher() {
      return finisher;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return characteristics;
    }

    public StringBuilderCollector instance() {
      return instance;
    }
  }
}
