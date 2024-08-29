package com.neptunedreams.tools.cli;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>I wrote this class to investigate numbers of the form 999001, or 10<sup>n</sup>(10<sup>n</sup>-1) + 1, to
 * determine which ones are prime, and to look at the prime factors of he ones that aren't. I did this because
 * I noticed that many members of this family have factors that are also members of this family. Those facters
 * are usually prime factors. When they're not, they're 91.</p>
 * <h1>Summary:</h1>
 * <h2>1. Definitions:</h2>
 * <p>P(n) means a number with n nines, followed by n-1 zeroes, followed by one.</p>
 * <p>Examples:</p>
 * <br>P(1) = 91 = 7 * 13
 * <br>P(2) = 9901 (Prime)
 * <br>P(3) = 999001
 * <br>P(4) = 99990001 (Prime)
 * <br>P(5) = 9999900001
 * <br>P(6) = 999999000001 (Prime)
 * <br>P(7) = 99999990000001
 * <br>P(8) = 9999999900000001 (Prime)
 * <br>P(9) = 999999999000000001
 * <p>These numbers may be expressed this way: P(n) = a<sup>2</sup> + a<sup>1</sup> + a<sup>0</sup> = 
 * a<sup>2</sup> + a + 1, where a = 10<sup>n</sup> - 1</p>
 * <p>They may be simplified to this: P(n) = 100<sup>n</sup> - 10<sup>n</sup> + 1</p>
 * <h2>Discoveries:</h2>
 * <p>1. Beyond the ones marked Prime in the examples above, no others appear to be primes. I have looked as far as p(4000),
 * and haven't found any that are prime.</p>
 * <p>2. Nearly every number in this family is divisible by a smaller member of the family.</p>
 * <p>3. For any P(n) that isn't divisible by a smaller member, n will be a compound number with prime factors
 * that are all twos and threes.
 * So, when n = (2<sup>a</sup>)(3<sup>b</sup>), then P(n) will not be divisible by a smaller family member.</p>
 * <p>I have explored as far as p(11764) and found no deviation from this rule.</p>
 * <p>4. For every member of the family that is divisible by smaller family members, there's a clear pattern to the
 * smallest divisor as n increases. The pattern isn't fully apparent until about P(36), but then it becomes very
 * clear. There are well defined exceptions to the pattern (described below), which are very common for low
 * values of n, but become rare very quickly. The pattern goes for twelve numbers before repeating itself. In the
 * following description of the pattern, I follow four conventions:
 * <br>a. x represents some integer i >= 1, which may be different each time.
 * <br>b. The second column gives the smallest member of the family that evenly divides the first number.
 * <br>c. Q represents some multiple of 12.
 * <br>d. M refers to any number of the from (2<sup>a</sup>)(3<sup>b</sup>), including the number one.</p>
 * <p>Here's the pattern:</p>
 * <pre>
 *   <b>P(n)    Divisible by</b>
 *   P(Q)    P(12M)
 *   P(Q+1)  P(1)
 *   P(Q+2)  P(2)
 *   P(Q+3)  P(3<sup>x</sup>)
 *   P(Q+4)  P(2<sup>x+1</sup>) [The minimum value is P(4)]
 *   P(Q+5)  P(1)
 *   P(Q+6)  P(2×3<sup>x</sup>)
 *   P(Q+7)  P(1)
 *   P(Q+8)  P(2<sup>x+1</sup>) [The minimum value is P(4)]
 *   P(Q+9)  P(3<sup>x</sup>) [x>0]
 *   P(Q+10) P(2)
 *   P(Q+11) P(1)
 * </pre>
 * <p>The exceptions to this rule occur when the prime factors of n are all twos and threes. In these cases,
 * as noted above, there will be no divisors in the family.</p>
 * <p>Several of the numbers in the second column can have many values. If we look at the most common member for
 * each part of the pattern, which is also the lowest member, the table looks like this:</p>
 * <pre>
 *   <b>P(n)    Divisible by</b>
 *   P(Q)    P(12)
 *   P(Q+1)  P(1)*
 *   P(Q+2)  P(2)*
 *   P(Q+3)  P(3)
 *   P(Q+4)  P(4)
 *   P(Q+5)  P(1)*
 *   P(Q+6)  P(6)
 *   P(Q+7)  P(1)*
 *   P(Q+8)  P(4)
 *   P(Q+9)  P(3)
 *   P(Q+10) P(2)*
 *   P(Q+11) P(1)*
 * </pre>
 * <p>The numbers with asterisks never change.</p>
 * <p>As the table shows, for the numbers p(Q), where Q is a multiple of 12, The smallest family divisor is P(12M). 
 * I have determined the value of M from the value of Q. M can be calculated as follows: First find the prime factors
 * of Q/12. Then remove all factors that aren't 2<sup>a</sup> or 3<sup>b</sup>. The
 * resulting product of (2<sup>a</sup>)(3<sup>b</sup>) will be the value of M.</p>
 * <p></p>So, for example, if Q is 8064, Q/12 is 672. The prime factorization of 672 is 2<sup>5</sup> × 3 × 7. We remove the
 * 7 and get 2<sup>5</sup> × 3, which is 96. So M is 96, which means the smallest family member that divides P(8064)
 * is P(12 × 96), or P(1152). </p>
 * <h1>References</h1>
 * <a href="https://oeis.org/A168624"> Online Encyclopedia of Integer Sequences, A168624</a><br>
 * <a href="https://oeis.org/A187868"> Online Encyclopedia of Integer Sequences, A187868</a><br>
 * <a href="https://oeis.org/A147554"> Online Encyclopedia of Integer Sequences, A147554</a><br>
 * <a href="https://t5k.org/curios/page.php?short=9901">Prime Curio for 9901</a><br>
 * <a href="https://t5k.org/curios/page.php?short=99990001">Prime Curio for 99990001</a><br>
 * <a href="https://t5k.org/curios/page.php?short=9999900001">Prime Curio for 9999900001</a><br>
 * <a href="https://t5k.org/curios/page.php?short=9999999900000001">Prime Curio for 9999999900000001</a><br>
 * <a href="https://math.stackexchange.com/questions/4775425/prime-numbers-p-for-which-p2-divides-p-3-what-does-p-3-mean-h">Stack Exchange Question</a><br>
 * <a href="https://t5k.org/notes/proofs/">Selected Theorems</a><br>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 9/12/23
 * <p>Time: 1:25 PM
 *
 * @author Miguel Muñoz
 */
@SuppressWarnings({"SizeReplaceableByIsEmpty", "UnnecessaryUnicodeEscape"})
public enum NineDivisors {
  ;
  public static void main(String[] args) {
    if (args.length > 0) {
      int seed = Integer.parseInt(args[0]);
      System.out.println(getCandidate(seed));
      return;
    }
    List<Set<Integer>> pattern = new ArrayList<>(12);
    for (int i=0; i<12; ++i) {
      pattern.add(new TreeSet<>());
    }
    int max = 8192;
//    int max = 2048;
//    int max = 1024;
//    int max = 128;
    BigInteger[] factors = new BigInteger[max+1];
    List<Integer> noneFound = new LinkedList<>();
    int breakCountAlpha = 0;
    int breakCountBravo = 0;
    for (int i = 1; i<=max; ++i) {
      BigInteger ninePrime = getCandidate(i);
      factors[i] = ninePrime;
//      System.out.println(ninePrime.toString() + ':');
      System.out.printf("[%d, %d] p(%5d): ", breakCountAlpha, breakCountBravo, i);
      boolean found = false;
      for (int j=1; j<i; ++j) {
        if (isDivisible(ninePrime, factors[j])) {
          System.out.printf(" %4d", j); // NON-NLS
          if (!found) { // Do this only for the first one found.
            int index = i % 12;
            pattern.get(index).add(j);
          }
          found = true;
        }
      }
      boolean limited = limitedFactors(i);
      if (found) {
        if (limited) {
          System.out.printf("### Break for i = %d", i);
          breakCountBravo++;
        }
      } else {
        noneFound.add(i);
        if (!limited) {
          System.out.printf("*** Break for i = %d", i); // NON-NLS
          breakCountAlpha++;
        }
      }
      System.out.println();
    }
    System.out.printf("%nNone found for these integers:%n"); // NON-NLS
    for (Integer i: noneFound) {
      System.out.printf("%d = %s%n", i, findPrimes(i));
    }
    System.out.printf("Breaks:%n%d%n%d%n", breakCountAlpha, breakCountBravo);

    System.out.println("\nPattern:");
    for (int i=0; i<12; ++i) {
      final Set<Integer> setForI = pattern.get(i);
      System.out.print(setForI);
      switch(i) {
        case 0:
          System.out.print(" Dividing each by 12 gives <");
          for (int j: setForI) {
            System.out.printf("%d, ", j/12);
          }
          System.out.print('>');
          break;
        case 6:
          System.out.print(" Dividing each by 6 gives <");
          for (int j: setForI) {
            System.out.printf("%d, ", j/6);
          }
          System.out.print('>');
          break;
        default:
      }
          System.out.println();
    }
  }
  
  private static boolean isDivisible(BigInteger number, BigInteger factor) {
    return number.mod(factor).equals(BigInteger.ZERO);
  }

  public static final BigInteger NINE = new BigInteger("9");

  private static BigInteger getCandidate(int k) {
//    int k = 2*n;
    BigInteger product = BigInteger.ONE;
    BigInteger sum = BigInteger.ZERO;
    for (int i = 0; i < k; ++i) {
      sum = sum.add(product);
      product = product.multiply(BigInteger.TEN);
    }
    sum = sum
        .multiply(NINE)
        .multiply(product)
        .add(BigInteger.ONE);
    return sum;
  }
  
  //---------------------------------

  /**
   * Returns true if the prime factors only include 2 and 3, false otherwise.
   * @param i The number to factor
   * @return true if prime factors only include 2 or 3.
   */
  private static boolean limitedFactors(long i) {
    i = stripFactors(i, 2L);
    i = stripFactors(i, 3L);
    return i == 1;
  }
  
  private static long stripFactors(long value, long factor) {
    while ((value % factor) == 0) {
      value /= factor;
    }
    return value;
  }

  private static String findPrimes(long arg) {
    int exp = 0;
    while ((arg % 2L) == 0) {
      exp++;
      arg /= 2L;
    }
    StringBuilder builder = new StringBuilder();
    printExp(builder, 2, exp);

    long odd = 3;
    while (odd <= arg) {
      exp = 0;
      while ((arg % odd) == 0) {
        exp++;
        arg /= odd;
      }
      printExp(builder, odd, exp);
      odd += 2;
    }
    if ((builder.length() > 0) && (builder.charAt(builder.length() - 2) == '\u00d7')) {
      builder.deleteCharAt(builder.length() - 1);
      builder.deleteCharAt(builder.length() - 1);
    }
    return builder.toString();
  }

  private static void printExp(StringBuilder builder, long base, int exp) {
    switch (exp) {
      case 0:
        break;
      case 1:
        builder.append(base);
        builder.append(" \u00d7 ");
        break;
      default:
        builder.append(base)
            .append('^')
            .append(exp); // NON-NLS
        builder.append(" \u00d7 ");
        break;
    }
  }

}

/*
For max=8192, this is what I get:

/Library/Java/JavaVirtualMachines/jdk-16.0.2.jdk/Contents/Home/bin/java -ea -Dfile.encoding=UTF-8 -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=54556:/Applications/IntelliJ IDEA.app/Contents/bin -classpath /Users/miguelmunoz/Documents/DevDocs/Tools/cli/Tools/target/production/Tools:/Users/miguelmunoz/Documents/DevDocs/Tools/cli/Tools/lib/gson-2.8.5.jar:/Users/miguelmunoz/Documents/DevDocs/Tools/lib/kotlin-stdlib.jar:/Users/miguelmunoz/Documents/DevDocs/Tools/lib/kotlin-reflect.jar:/Users/miguelmunoz/Documents/DevDocs/Tools/lib/kotlin-test.jar:/Users/miguelmunoz/Documents/DevDocs/Tools/lib/kotlin-stdlib-jdk7.jar:/Users/miguelmunoz/Documents/DevDocs/Tools/lib/kotlin-stdlib-jdk8.jar:/Users/miguelmunoz/.m2/repository/org/jetbrains/annotations/21.0.1/annotations-21.0.1.jar com.neptunedreams.tools.cli.NineDivisors
[0, 0] p(    1): 
[0, 0] p(    2): 
[0, 0] p(    3): 
[0, 0] p(    4): 
[0, 0] p(    5):     1
[0, 0] p(    6): 
[0, 0] p(    7):     1
[0, 0] p(    8): 
[0, 0] p(    9): 
[0, 0] p(   10):     2
[0, 0] p(   11):     1
[0, 0] p(   12): 
[0, 0] p(   13):     1
[0, 0] p(   14):     2
[0, 0] p(   15):     3
[0, 0] p(   16): 
[0, 0] p(   17):     1
[0, 0] p(   18): 
[0, 0] p(   19):     1
[0, 0] p(   20):     4
[0, 0] p(   21):     3
[0, 0] p(   22):     2
[0, 0] p(   23):     1
[0, 0] p(   24): 
[0, 0] p(   25):     1    5
[0, 0] p(   26):     2
[0, 0] p(   27): 
[0, 0] p(   28):     4
[0, 0] p(   29):     1
[0, 0] p(   30):     6
[0, 0] p(   31):     1
[0, 0] p(   32): 
[0, 0] p(   33):     3
[0, 0] p(   34):     2
[0, 0] p(   35):     1    5    7
[0, 0] p(   36): 
[0, 0] p(   37):     1
[0, 0] p(   38):     2
[0, 0] p(   39):     3
[0, 0] p(   40):     8
[0, 0] p(   41):     1
[0, 0] p(   42):     6
[0, 0] p(   43):     1
[0, 0] p(   44):     4
[0, 0] p(   45):     9
[0, 0] p(   46):     2
[0, 0] p(   47):     1
[0, 0] p(   48): 
[0, 0] p(   49):     1    7
[0, 0] p(   50):     2   10
[0, 0] p(   51):     3
[0, 0] p(   52):     4
[0, 0] p(   53):     1
[0, 0] p(   54): 
[0, 0] p(   55):     1    5   11
[0, 0] p(   56):     8
[0, 0] p(   57):     3
[0, 0] p(   58):     2
[0, 0] p(   59):     1
[0, 0] p(   60):    12
[0, 0] p(   61):     1
[0, 0] p(   62):     2
[0, 0] p(   63):     9
[0, 0] p(   64): 
[0, 0] p(   65):     1    5   13
[0, 0] p(   66):     6
[0, 0] p(   67):     1
[0, 0] p(   68):     4
[0, 0] p(   69):     3
[0, 0] p(   70):     2   10   14
[0, 0] p(   71):     1
[0, 0] p(   72): 
[0, 0] p(   73):     1
[0, 0] p(   74):     2
[0, 0] p(   75):     3   15
[0, 0] p(   76):     4
[0, 0] p(   77):     1    7   11
[0, 0] p(   78):     6
[0, 0] p(   79):     1
[0, 0] p(   80):    16
[0, 0] p(   81): 
[0, 0] p(   82):     2
[0, 0] p(   83):     1
[0, 0] p(   84):    12
[0, 0] p(   85):     1    5   17
[0, 0] p(   86):     2
[0, 0] p(   87):     3
[0, 0] p(   88):     8
[0, 0] p(   89):     1
[0, 0] p(   90):    18
[0, 0] p(   91):     1    7   13
[0, 0] p(   92):     4
[0, 0] p(   93):     3
[0, 0] p(   94):     2
[0, 0] p(   95):     1    5   19
[0, 0] p(   96): 
[0, 0] p(   97):     1
[0, 0] p(   98):     2   14
[0, 0] p(   99):     9
[0, 0] p(  100):     4   20
[0, 0] p(  101):     1
[0, 0] p(  102):     6
[0, 0] p(  103):     1
[0, 0] p(  104):     8
[0, 0] p(  105):     3   15   21
[0, 0] p(  106):     2
[0, 0] p(  107):     1
[0, 0] p(  108): 
[0, 0] p(  109):     1
[0, 0] p(  110):     2   10   22
[0, 0] p(  111):     3
[0, 0] p(  112):    16
[0, 0] p(  113):     1
[0, 0] p(  114):     6
[0, 0] p(  115):     1    5   23
[0, 0] p(  116):     4
[0, 0] p(  117):     9
[0, 0] p(  118):     2
[0, 0] p(  119):     1    7   17
[0, 0] p(  120):    24
[0, 0] p(  121):     1   11
[0, 0] p(  122):     2
[0, 0] p(  123):     3
[0, 0] p(  124):     4
[0, 0] p(  125):     1    5   25
[0, 0] p(  126):    18
[0, 0] p(  127):     1
[0, 0] p(  128): 
[0, 0] p(  129):     3
[0, 0] p(  130):     2   10   26
[0, 0] p(  131):     1
[0, 0] p(  132):    12
[0, 0] p(  133):     1    7   19
[0, 0] p(  134):     2
[0, 0] p(  135):    27
[0, 0] p(  136):     8
[0, 0] p(  137):     1
[0, 0] p(  138):     6
[0, 0] p(  139):     1
[0, 0] p(  140):     4   20   28
[0, 0] p(  141):     3
[0, 0] p(  142):     2
[0, 0] p(  143):     1   11   13
[0, 0] p(  144): 
[0, 0] p(  145):     1    5   29
[0, 0] p(  146):     2
[0, 0] p(  147):     3   21
[0, 0] p(  148):     4
[0, 0] p(  149):     1
[0, 0] p(  150):     6   30
[0, 0] p(  151):     1
[0, 0] p(  152):     8
[0, 0] p(  153):     9
[0, 0] p(  154):     2   14   22
[0, 0] p(  155):     1    5   31
[0, 0] p(  156):    12
[0, 0] p(  157):     1
[0, 0] p(  158):     2
[0, 0] p(  159):     3
[0, 0] p(  160):    32
[0, 0] p(  161):     1    7   23
[0, 0] p(  162): 
[0, 0] p(  163):     1
[0, 0] p(  164):     4
[0, 0] p(  165):     3   15   33
[0, 0] p(  166):     2
[0, 0] p(  167):     1
[0, 0] p(  168):    24
[0, 0] p(  169):     1   13
[0, 0] p(  170):     2   10   34
[0, 0] p(  171):     9
[0, 0] p(  172):     4
[0, 0] p(  173):     1
[0, 0] p(  174):     6
[0, 0] p(  175):     1    5    7   25   35
[0, 0] p(  176):    16
[0, 0] p(  177):     3
[0, 0] p(  178):     2
[0, 0] p(  179):     1
[0, 0] p(  180):    36
[0, 0] p(  181):     1
[0, 0] p(  182):     2   14   26
[0, 0] p(  183):     3
[0, 0] p(  184):     8
[0, 0] p(  185):     1    5   37
[0, 0] p(  186):     6
[0, 0] p(  187):     1   11   17
[0, 0] p(  188):     4
[0, 0] p(  189):    27
[0, 0] p(  190):     2   10   38
[0, 0] p(  191):     1
[0, 0] p(  192): 
[0, 0] p(  193):     1
[0, 0] p(  194):     2
[0, 0] p(  195):     3   15   39
[0, 0] p(  196):     4   28
[0, 0] p(  197):     1
[0, 0] p(  198):    18
[0, 0] p(  199):     1
[0, 0] p(  200):     8   40
[0, 0] p(  201):     3
[0, 0] p(  202):     2
[0, 0] p(  203):     1    7   29
[0, 0] p(  204):    12
[0, 0] p(  205):     1    5   41
[0, 0] p(  206):     2
[0, 0] p(  207):     9
[0, 0] p(  208):    16
[0, 0] p(  209):     1   11   19
[0, 0] p(  210):     6   30   42
[0, 0] p(  211):     1
[0, 0] p(  212):     4
[0, 0] p(  213):     3
[0, 0] p(  214):     2
[0, 0] p(  215):     1    5   43
[0, 0] p(  216): 
[0, 0] p(  217):     1    7   31
[0, 0] p(  218):     2
[0, 0] p(  219):     3
[0, 0] p(  220):     4   20   44
[0, 0] p(  221):     1   13   17
[0, 0] p(  222):     6
[0, 0] p(  223):     1
[0, 0] p(  224):    32
[0, 0] p(  225):     9   45
[0, 0] p(  226):     2
[0, 0] p(  227):     1
[0, 0] p(  228):    12
[0, 0] p(  229):     1
[0, 0] p(  230):     2   10   46
[0, 0] p(  231):     3   21   33
[0, 0] p(  232):     8
[0, 0] p(  233):     1
[0, 0] p(  234):    18
[0, 0] p(  235):     1    5   47
[0, 0] p(  236):     4
[0, 0] p(  237):     3
[0, 0] p(  238):     2   14   34
[0, 0] p(  239):     1
[0, 0] p(  240):    48
[0, 0] p(  241):     1
[0, 0] p(  242):     2   22
[0, 0] p(  243): 
[0, 0] p(  244):     4
[0, 0] p(  245):     1    5    7   35   49
[0, 0] p(  246):     6
[0, 0] p(  247):     1   13   19
[0, 0] p(  248):     8
[0, 0] p(  249):     3
[0, 0] p(  250):     2   10   50
[0, 0] p(  251):     1
[0, 0] p(  252):    36
[0, 0] p(  253):     1   11   23
[0, 0] p(  254):     2
[0, 0] p(  255):     3   15   51
[0, 0] p(  256): 
[0, 0] p(  257):     1
[0, 0] p(  258):     6
[0, 0] p(  259):     1    7   37
[0, 0] p(  260):     4   20   52
[0, 0] p(  261):     9
[0, 0] p(  262):     2
[0, 0] p(  263):     1
[0, 0] p(  264):    24
[0, 0] p(  265):     1    5   53
[0, 0] p(  266):     2   14   38
[0, 0] p(  267):     3
[0, 0] p(  268):     4
[0, 0] p(  269):     1
[0, 0] p(  270):    54
[0, 0] p(  271):     1
[0, 0] p(  272):    16
[0, 0] p(  273):     3   21   39
[0, 0] p(  274):     2
[0, 0] p(  275):     1    5   11   25   55
[0, 0] p(  276):    12
[0, 0] p(  277):     1
[0, 0] p(  278):     2
[0, 0] p(  279):     9
[0, 0] p(  280):     8   40   56
[0, 0] p(  281):     1
[0, 0] p(  282):     6
[0, 0] p(  283):     1
[0, 0] p(  284):     4
[0, 0] p(  285):     3   15   57
[0, 0] p(  286):     2   22   26
[0, 0] p(  287):     1    7   41
[0, 0] p(  288): 
[0, 0] p(  289):     1   17
[0, 0] p(  290):     2   10   58
[0, 0] p(  291):     3
[0, 0] p(  292):     4
[0, 0] p(  293):     1
[0, 0] p(  294):     6   42
[0, 0] p(  295):     1    5   59
[0, 0] p(  296):     8
[0, 0] p(  297):    27
[0, 0] p(  298):     2
[0, 0] p(  299):     1   13   23
[0, 0] p(  300):    12   60
[0, 0] p(  301):     1    7   43
[0, 0] p(  302):     2
[0, 0] p(  303):     3
[0, 0] p(  304):    16
[0, 0] p(  305):     1    5   61
[0, 0] p(  306):    18
[0, 0] p(  307):     1
[0, 0] p(  308):     4   28   44
[0, 0] p(  309):     3
[0, 0] p(  310):     2   10   62
[0, 0] p(  311):     1
[0, 0] p(  312):    24
[0, 0] p(  313):     1
[0, 0] p(  314):     2
[0, 0] p(  315):     9   45   63
[0, 0] p(  316):     4
[0, 0] p(  317):     1
[0, 0] p(  318):     6
[0, 0] p(  319):     1   11   29
[0, 0] p(  320):    64
[0, 0] p(  321):     3
[0, 0] p(  322):     2   14   46
[0, 0] p(  323):     1   17   19
[0, 0] p(  324): 
[0, 0] p(  325):     1    5   13   25   65
[0, 0] p(  326):     2
[0, 0] p(  327):     3
[0, 0] p(  328):     8
[0, 0] p(  329):     1    7   47
[0, 0] p(  330):     6   30   66
[0, 0] p(  331):     1
[0, 0] p(  332):     4
[0, 0] p(  333):     9
[0, 0] p(  334):     2
[0, 0] p(  335):     1    5   67
[0, 0] p(  336):    48
[0, 0] p(  337):     1
[0, 0] p(  338):     2   26
[0, 0] p(  339):     3
[0, 0] p(  340):     4   20   68
[0, 0] p(  341):     1   11   31
[0, 0] p(  342):    18
[0, 0] p(  343):     1    7   49
[0, 0] p(  344):     8
[0, 0] p(  345):     3   15   69
[0, 0] p(  346):     2
[0, 0] p(  347):     1
[0, 0] p(  348):    12
[0, 0] p(  349):     1
[0, 0] p(  350):     2   10   14   50   70
[0, 0] p(  351):    27
[0, 0] p(  352):    32
[0, 0] p(  353):     1
[0, 0] p(  354):     6
[0, 0] p(  355):     1    5   71
[0, 0] p(  356):     4
[0, 0] p(  357):     3   21   51
[0, 0] p(  358):     2
[0, 0] p(  359):     1
[0, 0] p(  360):    72
[0, 0] p(  361):     1   19
[0, 0] p(  362):     2
[0, 0] p(  363):     3   33
[0, 0] p(  364):     4   28   52
[0, 0] p(  365):     1    5   73
[0, 0] p(  366):     6
[0, 0] p(  367):     1
[0, 0] p(  368):    16
[0, 0] p(  369):     9
[0, 0] p(  370):     2   10   74
[0, 0] p(  371):     1    7   53
[0, 0] p(  372):    12
[0, 0] p(  373):     1
[0, 0] p(  374):     2   22   34
[0, 0] p(  375):     3   15   75
[0, 0] p(  376):     8
[0, 0] p(  377):     1   13   29
[0, 0] p(  378):    54
[0, 0] p(  379):     1
[0, 0] p(  380):     4   20   76
[0, 0] p(  381):     3
[0, 0] p(  382):     2
[0, 0] p(  383):     1
[0, 0] p(  384): 
[0, 0] p(  385):     1    5    7   11   35   55   77
[0, 0] p(  386):     2
[0, 0] p(  387):     9
[0, 0] p(  388):     4
[0, 0] p(  389):     1
[0, 0] p(  390):     6   30   78
[0, 0] p(  391):     1   17   23
[0, 0] p(  392):     8   56
[0, 0] p(  393):     3
[0, 0] p(  394):     2
[0, 0] p(  395):     1    5   79
[0, 0] p(  396):    36
[0, 0] p(  397):     1
[0, 0] p(  398):     2
[0, 0] p(  399):     3   21   57
[0, 0] p(  400):    16   80
[0, 0] p(  401):     1
[0, 0] p(  402):     6
[0, 0] p(  403):     1   13   31
[0, 0] p(  404):     4
[0, 0] p(  405):    81
[0, 0] p(  406):     2   14   58
[0, 0] p(  407):     1   11   37
[0, 0] p(  408):    24
[0, 0] p(  409):     1
[0, 0] p(  410):     2   10   82
[0, 0] p(  411):     3
[0, 0] p(  412):     4
[0, 0] p(  413):     1    7   59
[0, 0] p(  414):    18
[0, 0] p(  415):     1    5   83
[0, 0] p(  416):    32
[0, 0] p(  417):     3
[0, 0] p(  418):     2   22   38
[0, 0] p(  419):     1
[0, 0] p(  420):    12   60   84
[0, 0] p(  421):     1
[0, 0] p(  422):     2
[0, 0] p(  423):     9
[0, 0] p(  424):     8
[0, 0] p(  425):     1    5   17   25   85
[0, 0] p(  426):     6
[0, 0] p(  427):     1    7   61
[0, 0] p(  428):     4
[0, 0] p(  429):     3   33   39
[0, 0] p(  430):     2   10   86
[0, 0] p(  431):     1
[0, 0] p(  432): 
[0, 0] p(  433):     1
[0, 0] p(  434):     2   14   62
[0, 0] p(  435):     3   15   87
[0, 0] p(  436):     4
[0, 0] p(  437):     1   19   23
[0, 0] p(  438):     6
[0, 0] p(  439):     1
[0, 0] p(  440):     8   40   88
[0, 0] p(  441):     9   63
[0, 0] p(  442):     2   26   34
[0, 0] p(  443):     1
[0, 0] p(  444):    12
[0, 0] p(  445):     1    5   89
[0, 0] p(  446):     2
[0, 0] p(  447):     3
[0, 0] p(  448):    64
[0, 0] p(  449):     1
[0, 0] p(  450):    18   90
[0, 0] p(  451):     1   11   41
[0, 0] p(  452):     4
[0, 0] p(  453):     3
[0, 0] p(  454):     2
[0, 0] p(  455):     1    5    7   13   35   65   91
[0, 0] p(  456):    24
[0, 0] p(  457):     1
[0, 0] p(  458):     2
[0, 0] p(  459):    27
[0, 0] p(  460):     4   20   92
[0, 0] p(  461):     1
[0, 0] p(  462):     6   42   66
[0, 0] p(  463):     1
[0, 0] p(  464):    16
[0, 0] p(  465):     3   15   93
[0, 0] p(  466):     2
[0, 0] p(  467):     1
[0, 0] p(  468):    36
[0, 0] p(  469):     1    7   67
[0, 0] p(  470):     2   10   94
[0, 0] p(  471):     3
[0, 0] p(  472):     8
[0, 0] p(  473):     1   11   43
[0, 0] p(  474):     6
[0, 0] p(  475):     1    5   19   25   95
[0, 0] p(  476):     4   28   68
[0, 0] p(  477):     9
[0, 0] p(  478):     2
[0, 0] p(  479):     1
[0, 0] p(  480):    96
[0, 0] p(  481):     1   13   37
[0, 0] p(  482):     2
[0, 0] p(  483):     3   21   69
[0, 0] p(  484):     4   44
[0, 0] p(  485):     1    5   97
[0, 0] p(  486): 
[0, 0] p(  487):     1
[0, 0] p(  488):     8
[0, 0] p(  489):     3
[0, 0] p(  490):     2   10   14   70   98
[0, 0] p(  491):     1
[0, 0] p(  492):    12
[0, 0] p(  493):     1   17   29
[0, 0] p(  494):     2   26   38
[0, 0] p(  495):     9   45   99
[0, 0] p(  496):    16
[0, 0] p(  497):     1    7   71
[0, 0] p(  498):     6
[0, 0] p(  499):     1
[0, 0] p(  500):     4   20  100
[0, 0] p(  501):     3
[0, 0] p(  502):     2
[0, 0] p(  503):     1
[0, 0] p(  504):    72
[0, 0] p(  505):     1    5  101
[0, 0] p(  506):     2   22   46
[0, 0] p(  507):     3   39
[0, 0] p(  508):     4
[0, 0] p(  509):     1
[0, 0] p(  510):     6   30  102
[0, 0] p(  511):     1    7   73
[0, 0] p(  512): 
[0, 0] p(  513):    27
[0, 0] p(  514):     2
[0, 0] p(  515):     1    5  103
[0, 0] p(  516):    12
[0, 0] p(  517):     1   11   47
[0, 0] p(  518):     2   14   74
[0, 0] p(  519):     3
[0, 0] p(  520):     8   40  104
[0, 0] p(  521):     1
[0, 0] p(  522):    18
[0, 0] p(  523):     1
[0, 0] p(  524):     4
[0, 0] p(  525):     3   15   21   75  105
[0, 0] p(  526):     2
[0, 0] p(  527):     1   17   31
[0, 0] p(  528):    48
[0, 0] p(  529):     1   23
[0, 0] p(  530):     2   10  106
[0, 0] p(  531):     9
[0, 0] p(  532):     4   28   76
[0, 0] p(  533):     1   13   41
[0, 0] p(  534):     6
[0, 0] p(  535):     1    5  107
[0, 0] p(  536):     8
[0, 0] p(  537):     3
[0, 0] p(  538):     2
[0, 0] p(  539):     1    7   11   49   77
[0, 0] p(  540):   108
[0, 0] p(  541):     1
[0, 0] p(  542):     2
[0, 0] p(  543):     3
[0, 0] p(  544):    32
[0, 0] p(  545):     1    5  109
[0, 0] p(  546):     6   42   78
[0, 0] p(  547):     1
[0, 0] p(  548):     4
[0, 0] p(  549):     9
[0, 0] p(  550):     2   10   22   50  110
[0, 0] p(  551):     1   19   29
[0, 0] p(  552):    24
[0, 0] p(  553):     1    7   79
[0, 0] p(  554):     2
[0, 0] p(  555):     3   15  111
[0, 0] p(  556):     4
[0, 0] p(  557):     1
[0, 0] p(  558):    18
[0, 0] p(  559):     1   13   43
[0, 0] p(  560):    16   80  112
[0, 0] p(  561):     3   33   51
[0, 0] p(  562):     2
[0, 0] p(  563):     1
[0, 0] p(  564):    12
[0, 0] p(  565):     1    5  113
[0, 0] p(  566):     2
[0, 0] p(  567):    81
[0, 0] p(  568):     8
[0, 0] p(  569):     1
[0, 0] p(  570):     6   30  114
[0, 0] p(  571):     1
[0, 0] p(  572):     4   44   52
[0, 0] p(  573):     3
[0, 0] p(  574):     2   14   82
[0, 0] p(  575):     1    5   23   25  115
[0, 0] p(  576): 
[0, 0] p(  577):     1
[0, 0] p(  578):     2   34
[0, 0] p(  579):     3
[0, 0] p(  580):     4   20  116
[0, 0] p(  581):     1    7   83
[0, 0] p(  582):     6
[0, 0] p(  583):     1   11   53
[0, 0] p(  584):     8
[0, 0] p(  585):     9   45  117
[0, 0] p(  586):     2
[0, 0] p(  587):     1
[0, 0] p(  588):    12   84
[0, 0] p(  589):     1   19   31
[0, 0] p(  590):     2   10  118
[0, 0] p(  591):     3
[0, 0] p(  592):    16
[0, 0] p(  593):     1
[0, 0] p(  594):    54
[0, 0] p(  595):     1    5    7   17   35   85  119
[0, 0] p(  596):     4
[0, 0] p(  597):     3
[0, 0] p(  598):     2   26   46
[0, 0] p(  599):     1
[0, 0] p(  600):    24  120
[0, 0] p(  601):     1
[0, 0] p(  602):     2   14   86
[0, 0] p(  603):     9
[0, 0] p(  604):     4
[0, 0] p(  605):     1    5   11   55  121
[0, 0] p(  606):     6
[0, 0] p(  607):     1
[0, 0] p(  608):    32
[0, 0] p(  609):     3   21   87
[0, 0] p(  610):     2   10  122
[0, 0] p(  611):     1   13   47
[0, 0] p(  612):    36
[0, 0] p(  613):     1
[0, 0] p(  614):     2
[0, 0] p(  615):     3   15  123
[0, 0] p(  616):     8   56   88
[0, 0] p(  617):     1
[0, 0] p(  618):     6
[0, 0] p(  619):     1
[0, 0] p(  620):     4   20  124
[0, 0] p(  621):    27
[0, 0] p(  622):     2
[0, 0] p(  623):     1    7   89
[0, 0] p(  624):    48
[0, 0] p(  625):     1    5   25  125
[0, 0] p(  626):     2
[0, 0] p(  627):     3   33   57
[0, 0] p(  628):     4
[0, 0] p(  629):     1   17   37
[0, 0] p(  630):    18   90  126
[0, 0] p(  631):     1
[0, 0] p(  632):     8
[0, 0] p(  633):     3
[0, 0] p(  634):     2
[0, 0] p(  635):     1    5  127
[0, 0] p(  636):    12
[0, 0] p(  637):     1    7   13   49   91
[0, 0] p(  638):     2   22   58
[0, 0] p(  639):     9
[0, 0] p(  640):   128
[0, 0] p(  641):     1
[0, 0] p(  642):     6
[0, 0] p(  643):     1
[0, 0] p(  644):     4   28   92
[0, 0] p(  645):     3   15  129
[0, 0] p(  646):     2   34   38
[0, 0] p(  647):     1
[0, 0] p(  648): 
[0, 0] p(  649):     1   11   59
[0, 0] p(  650):     2   10   26   50  130
[0, 0] p(  651):     3   21   93
[0, 0] p(  652):     4
[0, 0] p(  653):     1
[0, 0] p(  654):     6
[0, 0] p(  655):     1    5  131
[0, 0] p(  656):    16
[0, 0] p(  657):     9
[0, 0] p(  658):     2   14   94
[0, 0] p(  659):     1
[0, 0] p(  660):    12   60  132
[0, 0] p(  661):     1
[0, 0] p(  662):     2
[0, 0] p(  663):     3   39   51
[0, 0] p(  664):     8
[0, 0] p(  665):     1    5    7   19   35   95  133
[0, 0] p(  666):    18
[0, 0] p(  667):     1   23   29
[0, 0] p(  668):     4
[0, 0] p(  669):     3
[0, 0] p(  670):     2   10  134
[0, 0] p(  671):     1   11   61
[0, 0] p(  672):    96
[0, 0] p(  673):     1
[0, 0] p(  674):     2
[0, 0] p(  675):    27  135
[0, 0] p(  676):     4   52
[0, 0] p(  677):     1
[0, 0] p(  678):     6
[0, 0] p(  679):     1    7   97
[0, 0] p(  680):     8   40  136
[0, 0] p(  681):     3
[0, 0] p(  682):     2   22   62
[0, 0] p(  683):     1
[0, 0] p(  684):    36
[0, 0] p(  685):     1    5  137
[0, 0] p(  686):     2   14   98
[0, 0] p(  687):     3
[0, 0] p(  688):    16
[0, 0] p(  689):     1   13   53
[0, 0] p(  690):     6   30  138
[0, 0] p(  691):     1
[0, 0] p(  692):     4
[0, 0] p(  693):     9   63   99
[0, 0] p(  694):     2
[0, 0] p(  695):     1    5  139
[0, 0] p(  696):    24
[0, 0] p(  697):     1   17   41
[0, 0] p(  698):     2
[0, 0] p(  699):     3
[0, 0] p(  700):     4   20   28  100  140
[0, 0] p(  701):     1
[0, 0] p(  702):    54
[0, 0] p(  703):     1   19   37
[0, 0] p(  704):    64
[0, 0] p(  705):     3   15  141
[0, 0] p(  706):     2
[0, 0] p(  707):     1    7  101
[0, 0] p(  708):    12
[0, 0] p(  709):     1
[0, 0] p(  710):     2   10  142
[0, 0] p(  711):     9
[0, 0] p(  712):     8
[0, 0] p(  713):     1   23   31
[0, 0] p(  714):     6   42  102
[0, 0] p(  715):     1    5   11   13   55   65  143
[0, 0] p(  716):     4
[0, 0] p(  717):     3
[0, 0] p(  718):     2
[0, 0] p(  719):     1
[0, 0] p(  720):   144
[0, 0] p(  721):     1    7  103
[0, 0] p(  722):     2   38
[0, 0] p(  723):     3
[0, 0] p(  724):     4
[0, 0] p(  725):     1    5   25   29  145
[0, 0] p(  726):     6   66
[0, 0] p(  727):     1
[0, 0] p(  728):     8   56  104
[0, 0] p(  729): 
[0, 0] p(  730):     2   10  146
[0, 0] p(  731):     1   17   43
[0, 0] p(  732):    12
[0, 0] p(  733):     1
[0, 0] p(  734):     2
[0, 0] p(  735):     3   15   21  105  147
[0, 0] p(  736):    32
[0, 0] p(  737):     1   11   67
[0, 0] p(  738):    18
[0, 0] p(  739):     1
[0, 0] p(  740):     4   20  148
[0, 0] p(  741):     3   39   57
[0, 0] p(  742):     2   14  106
[0, 0] p(  743):     1
[0, 0] p(  744):    24
[0, 0] p(  745):     1    5  149
[0, 0] p(  746):     2
[0, 0] p(  747):     9
[0, 0] p(  748):     4   44   68
[0, 0] p(  749):     1    7  107
[0, 0] p(  750):     6   30  150
[0, 0] p(  751):     1
[0, 0] p(  752):    16
[0, 0] p(  753):     3
[0, 0] p(  754):     2   26   58
[0, 0] p(  755):     1    5  151
[0, 0] p(  756):   108
[0, 0] p(  757):     1
[0, 0] p(  758):     2
[0, 0] p(  759):     3   33   69
[0, 0] p(  760):     8   40  152
[0, 0] p(  761):     1
[0, 0] p(  762):     6
[0, 0] p(  763):     1    7  109
[0, 0] p(  764):     4
[0, 0] p(  765):     9   45  153
[0, 0] p(  766):     2
[0, 0] p(  767):     1   13   59
[0, 0] p(  768): 
[0, 0] p(  769):     1
[0, 0] p(  770):     2   10   14   22   70  110  154
[0, 0] p(  771):     3
[0, 0] p(  772):     4
[0, 0] p(  773):     1
[0, 0] p(  774):    18
[0, 0] p(  775):     1    5   25   31  155
[0, 0] p(  776):     8
[0, 0] p(  777):     3   21  111
[0, 0] p(  778):     2
[0, 0] p(  779):     1   19   41
[0, 0] p(  780):    12   60  156
[0, 0] p(  781):     1   11   71
[0, 0] p(  782):     2   34   46
[0, 0] p(  783):    27
[0, 0] p(  784):    16  112
[0, 0] p(  785):     1    5  157
[0, 0] p(  786):     6
[0, 0] p(  787):     1
[0, 0] p(  788):     4
[0, 0] p(  789):     3
[0, 0] p(  790):     2   10  158
[0, 0] p(  791):     1    7  113
[0, 0] p(  792):    72
[0, 0] p(  793):     1   13   61
[0, 0] p(  794):     2
[0, 0] p(  795):     3   15  159
[0, 0] p(  796):     4
[0, 0] p(  797):     1
[0, 0] p(  798):     6   42  114
[0, 0] p(  799):     1   17   47
[0, 0] p(  800):    32  160
[0, 0] p(  801):     9
[0, 0] p(  802):     2
[0, 0] p(  803):     1   11   73
[0, 0] p(  804):    12
[0, 0] p(  805):     1    5    7   23   35  115  161
[0, 0] p(  806):     2   26   62
[0, 0] p(  807):     3
[0, 0] p(  808):     8
[0, 0] p(  809):     1
[0, 0] p(  810):   162
[0, 0] p(  811):     1
[0, 0] p(  812):     4   28  116
[0, 0] p(  813):     3
[0, 0] p(  814):     2   22   74
[0, 0] p(  815):     1    5  163
[0, 0] p(  816):    48
[0, 0] p(  817):     1   19   43
[0, 0] p(  818):     2
[0, 0] p(  819):     9   63  117
[0, 0] p(  820):     4   20  164
[0, 0] p(  821):     1
[0, 0] p(  822):     6
[0, 0] p(  823):     1
[0, 0] p(  824):     8
[0, 0] p(  825):     3   15   33   75  165
[0, 0] p(  826):     2   14  118
[0, 0] p(  827):     1
[0, 0] p(  828):    36
[0, 0] p(  829):     1
[0, 0] p(  830):     2   10  166
[0, 0] p(  831):     3
[0, 0] p(  832):    64
[0, 0] p(  833):     1    7   17   49  119
[0, 0] p(  834):     6
[0, 0] p(  835):     1    5  167
[0, 0] p(  836):     4   44   76
[0, 0] p(  837):    27
[0, 0] p(  838):     2
[0, 0] p(  839):     1
[0, 0] p(  840):    24  120  168
[0, 0] p(  841):     1   29
[0, 0] p(  842):     2
[0, 0] p(  843):     3
[0, 0] p(  844):     4
[0, 0] p(  845):     1    5   13   65  169
[0, 0] p(  846):    18
[0, 0] p(  847):     1    7   11   77  121
[0, 0] p(  848):    16
[0, 0] p(  849):     3
[0, 0] p(  850):     2   10   34   50  170
[0, 0] p(  851):     1   23   37
[0, 0] p(  852):    12
[0, 0] p(  853):     1
[0, 0] p(  854):     2   14  122
[0, 0] p(  855):     9   45  171
[0, 0] p(  856):     8
[0, 0] p(  857):     1
[0, 0] p(  858):     6   66   78
[0, 0] p(  859):     1
[0, 0] p(  860):     4   20  172
[0, 0] p(  861):     3   21  123
[0, 0] p(  862):     2
[0, 0] p(  863):     1
[0, 0] p(  864): 
[0, 0] p(  865):     1    5  173
[0, 0] p(  866):     2
[0, 0] p(  867):     3   51
[0, 0] p(  868):     4   28  124
[0, 0] p(  869):     1   11   79
[0, 0] p(  870):     6   30  174
[0, 0] p(  871):     1   13   67
[0, 0] p(  872):     8
[0, 0] p(  873):     9
[0, 0] p(  874):     2   38   46
[0, 0] p(  875):     1    5    7   25   35  125  175
[0, 0] p(  876):    12
[0, 0] p(  877):     1
[0, 0] p(  878):     2
[0, 0] p(  879):     3
[0, 0] p(  880):    16   80  176
[0, 0] p(  881):     1
[0, 0] p(  882):    18  126
[0, 0] p(  883):     1
[0, 0] p(  884):     4   52   68
[0, 0] p(  885):     3   15  177
[0, 0] p(  886):     2
[0, 0] p(  887):     1
[0, 0] p(  888):    24
[0, 0] p(  889):     1    7  127
[0, 0] p(  890):     2   10  178
[0, 0] p(  891):    81
[0, 0] p(  892):     4
[0, 0] p(  893):     1   19   47
[0, 0] p(  894):     6
[0, 0] p(  895):     1    5  179
[0, 0] p(  896):   128
[0, 0] p(  897):     3   39   69
[0, 0] p(  898):     2
[0, 0] p(  899):     1   29   31
[0, 0] p(  900):    36  180
[0, 0] p(  901):     1   17   53
[0, 0] p(  902):     2   22   82
[0, 0] p(  903):     3   21  129
[0, 0] p(  904):     8
[0, 0] p(  905):     1    5  181
[0, 0] p(  906):     6
[0, 0] p(  907):     1
[0, 0] p(  908):     4
[0, 0] p(  909):     9
[0, 0] p(  910):     2   10   14   26   70  130  182
[0, 0] p(  911):     1
[0, 0] p(  912):    48
[0, 0] p(  913):     1   11   83
[0, 0] p(  914):     2
[0, 0] p(  915):     3   15  183
[0, 0] p(  916):     4
[0, 0] p(  917):     1    7  131
[0, 0] p(  918):    54
[0, 0] p(  919):     1
[0, 0] p(  920):     8   40  184
[0, 0] p(  921):     3
[0, 0] p(  922):     2
[0, 0] p(  923):     1   13   71
[0, 0] p(  924):    12   84  132
[0, 0] p(  925):     1    5   25   37  185
[0, 0] p(  926):     2
[0, 0] p(  927):     9
[0, 0] p(  928):    32
[0, 0] p(  929):     1
[0, 0] p(  930):     6   30  186
[0, 0] p(  931):     1    7   19   49  133
[0, 0] p(  932):     4
[0, 0] p(  933):     3
[0, 0] p(  934):     2
[0, 0] p(  935):     1    5   11   17   55   85  187
[0, 0] p(  936):    72
[0, 0] p(  937):     1
[0, 0] p(  938):     2   14  134
[0, 0] p(  939):     3
[0, 0] p(  940):     4   20  188
[0, 0] p(  941):     1
[0, 0] p(  942):     6
[0, 0] p(  943):     1   23   41
[0, 0] p(  944):    16
[0, 0] p(  945):    27  135  189
[0, 0] p(  946):     2   22   86
[0, 0] p(  947):     1
[0, 0] p(  948):    12
[0, 0] p(  949):     1   13   73
[0, 0] p(  950):     2   10   38   50  190
[0, 0] p(  951):     3
[0, 0] p(  952):     8   56  136
[0, 0] p(  953):     1
[0, 0] p(  954):    18
[0, 0] p(  955):     1    5  191
[0, 0] p(  956):     4
[0, 0] p(  957):     3   33   87
[0, 0] p(  958):     2
[0, 0] p(  959):     1    7  137
[0, 0] p(  960):   192
[0, 0] p(  961):     1   31
[0, 0] p(  962):     2   26   74
[0, 0] p(  963):     9
[0, 0] p(  964):     4
[0, 0] p(  965):     1    5  193
[0, 0] p(  966):     6   42  138
[0, 0] p(  967):     1
[0, 0] p(  968):     8   88
[0, 0] p(  969):     3   51   57
[0, 0] p(  970):     2   10  194
[0, 0] p(  971):     1
[0, 0] p(  972): 
[0, 0] p(  973):     1    7  139
[0, 0] p(  974):     2
[0, 0] p(  975):     3   15   39   75  195
[0, 0] p(  976):    16
[0, 0] p(  977):     1
[0, 0] p(  978):     6
[0, 0] p(  979):     1   11   89
[0, 0] p(  980):     4   20   28  140  196
[0, 0] p(  981):     9
[0, 0] p(  982):     2
[0, 0] p(  983):     1
[0, 0] p(  984):    24
[0, 0] p(  985):     1    5  197
[0, 0] p(  986):     2   34   58
[0, 0] p(  987):     3   21  141
[0, 0] p(  988):     4   52   76
[0, 0] p(  989):     1   23   43
[0, 0] p(  990):    18   90  198
[0, 0] p(  991):     1
[0, 0] p(  992):    32
[0, 0] p(  993):     3
[0, 0] p(  994):     2   14  142
[0, 0] p(  995):     1    5  199
[0, 0] p(  996):    12
[0, 0] p(  997):     1
[0, 0] p(  998):     2
[0, 0] p(  999):    27
[0, 0] p( 1000):     8   40  200
[0, 0] p( 1001):     1    7   11   13   77   91  143
[0, 0] p( 1002):     6
[0, 0] p( 1003):     1   17   59
[0, 0] p( 1004):     4
[0, 0] p( 1005):     3   15  201
[0, 0] p( 1006):     2
[0, 0] p( 1007):     1   19   53
[0, 0] p( 1008):   144
[0, 0] p( 1009):     1
[0, 0] p( 1010):     2   10  202
[0, 0] p( 1011):     3
[0, 0] p( 1012):     4   44   92
[0, 0] p( 1013):     1
[0, 0] p( 1014):     6   78
[0, 0] p( 1015):     1    5    7   29   35  145  203
[0, 0] p( 1016):     8
[0, 0] p( 1017):     9
[0, 0] p( 1018):     2
[0, 0] p( 1019):     1
[0, 0] p( 1020):    12   60  204
[0, 0] p( 1021):     1
[0, 0] p( 1022):     2   14  146
[0, 0] p( 1023):     3   33   93
[0, 0] p( 1024): 
[0, 0] p( 1025):     1    5   25   41  205
[0, 0] p( 1026):    54
[0, 0] p( 1027):     1   13   79
[0, 0] p( 1028):     4
[0, 0] p( 1029):     3   21  147
[0, 0] p( 1030):     2   10  206
[0, 0] p( 1031):     1
[0, 0] p( 1032):    24
[0, 0] p( 1033):     1
[0, 0] p( 1034):     2   22   94
[0, 0] p( 1035):     9   45  207
[0, 0] p( 1036):     4   28  148
[0, 0] p( 1037):     1   17   61
[0, 0] p( 1038):     6
[0, 0] p( 1039):     1
[0, 0] p( 1040):    16   80  208
[0, 0] p( 1041):     3
[0, 0] p( 1042):     2
[0, 0] p( 1043):     1    7  149
[0, 0] p( 1044):    36
[0, 0] p( 1045):     1    5   11   19   55   95  209
[0, 0] p( 1046):     2
[0, 0] p( 1047):     3
[0, 0] p( 1048):     8
[0, 0] p( 1049):     1
[0, 0] p( 1050):     6   30   42  150  210
[0, 0] p( 1051):     1
[0, 0] p( 1052):     4
[0, 0] p( 1053):    81
[0, 0] p( 1054):     2   34   62
[0, 0] p( 1055):     1    5  211
[0, 0] p( 1056):    96
[0, 0] p( 1057):     1    7  151
[0, 0] p( 1058):     2   46
[0, 0] p( 1059):     3
[0, 0] p( 1060):     4   20  212
[0, 0] p( 1061):     1
[0, 0] p( 1062):    18
[0, 0] p( 1063):     1
[0, 0] p( 1064):     8   56  152
[0, 0] p( 1065):     3   15  213
[0, 0] p( 1066):     2   26   82
[0, 0] p( 1067):     1   11   97
[0, 0] p( 1068):    12
[0, 0] p( 1069):     1
[0, 0] p( 1070):     2   10  214
[0, 0] p( 1071):     9   63  153
[0, 0] p( 1072):    16
[0, 0] p( 1073):     1   29   37
[0, 0] p( 1074):     6
[0, 0] p( 1075):     1    5   25   43  215
[0, 0] p( 1076):     4
[0, 0] p( 1077):     3
[0, 0] p( 1078):     2   14   22   98  154
[0, 0] p( 1079):     1   13   83
[0, 0] p( 1080):   216
[0, 0] p( 1081):     1   23   47
[0, 0] p( 1082):     2
[0, 0] p( 1083):     3   57
[0, 0] p( 1084):     4
[0, 0] p( 1085):     1    5    7   31   35  155  217
[0, 0] p( 1086):     6
[0, 0] p( 1087):     1
[0, 0] p( 1088):    64
[0, 0] p( 1089):     9   99
[0, 0] p( 1090):     2   10  218
[0, 0] p( 1091):     1
[0, 0] p( 1092):    12   84  156
[0, 0] p( 1093):     1
[0, 0] p( 1094):     2
[0, 0] p( 1095):     3   15  219
[0, 0] p( 1096):     8
[0, 0] p( 1097):     1
[0, 0] p( 1098):    18
[0, 0] p( 1099):     1    7  157
[0, 0] p( 1100):     4   20   44  100  220
[0, 0] p( 1101):     3
[0, 0] p( 1102):     2   38   58
[0, 0] p( 1103):     1
[0, 0] p( 1104):    48
[0, 0] p( 1105):     1    5   13   17   65   85  221
[0, 0] p( 1106):     2   14  158
[0, 0] p( 1107):    27
[0, 0] p( 1108):     4
[0, 0] p( 1109):     1
[0, 0] p( 1110):     6   30  222
[0, 0] p( 1111):     1   11  101
[0, 0] p( 1112):     8
[0, 0] p( 1113):     3   21  159
[0, 0] p( 1114):     2
[0, 0] p( 1115):     1    5  223
[0, 0] p( 1116):    36
[0, 0] p( 1117):     1
[0, 0] p( 1118):     2   26   86
[0, 0] p( 1119):     3
[0, 0] p( 1120):    32  160  224
[0, 0] p( 1121):     1   19   59
[0, 0] p( 1122):     6   66  102
[0, 0] p( 1123):     1
[0, 0] p( 1124):     4
[0, 0] p( 1125):     9   45  225
[0, 0] p( 1126):     2
[0, 0] p( 1127):     1    7   23   49  161
[0, 0] p( 1128):    24
[0, 0] p( 1129):     1
[0, 0] p( 1130):     2   10  226
[0, 0] p( 1131):     3   39   87
[0, 0] p( 1132):     4
[0, 0] p( 1133):     1   11  103
[0, 0] p( 1134):   162
[0, 0] p( 1135):     1    5  227
[0, 0] p( 1136):    16
[0, 0] p( 1137):     3
[0, 0] p( 1138):     2
[0, 0] p( 1139):     1   17   67
[0, 0] p( 1140):    12   60  228
[0, 0] p( 1141):     1    7  163
[0, 0] p( 1142):     2
[0, 0] p( 1143):     9
[0, 0] p( 1144):     8   88  104
[0, 0] p( 1145):     1    5  229
[0, 0] p( 1146):     6
[0, 0] p( 1147):     1   31   37
[0, 0] p( 1148):     4   28  164
[0, 0] p( 1149):     3
[0, 0] p( 1150):     2   10   46   50  230
[0, 0] p( 1151):     1
[0, 0] p( 1152): 
[0, 0] p( 1153):     1
[0, 0] p( 1154):     2
[0, 0] p( 1155):     3   15   21   33  105  165  231
[0, 0] p( 1156):     4   68
[0, 0] p( 1157):     1   13   89
[0, 0] p( 1158):     6
[0, 0] p( 1159):     1   19   61
[0, 0] p( 1160):     8   40  232
[0, 0] p( 1161):    27
[0, 0] p( 1162):     2   14  166
[0, 0] p( 1163):     1
[0, 0] p( 1164):    12
[0, 0] p( 1165):     1    5  233
[0, 0] p( 1166):     2   22  106
[0, 0] p( 1167):     3
[0, 0] p( 1168):    16
[0, 0] p( 1169):     1    7  167
[0, 0] p( 1170):    18   90  234
[0, 0] p( 1171):     1
[0, 0] p( 1172):     4
[0, 0] p( 1173):     3   51   69
[0, 0] p( 1174):     2
[0, 0] p( 1175):     1    5   25   47  235
[0, 0] p( 1176):    24  168
[0, 0] p( 1177):     1   11  107
[0, 0] p( 1178):     2   38   62
[0, 0] p( 1179):     9
[0, 0] p( 1180):     4   20  236
[0, 0] p( 1181):     1
[0, 0] p( 1182):     6
[0, 0] p( 1183):     1    7   13   91  169
[0, 0] p( 1184):    32
[0, 0] p( 1185):     3   15  237
[0, 0] p( 1186):     2
[0, 0] p( 1187):     1
[0, 0] p( 1188):   108
[0, 0] p( 1189):     1   29   41
[0, 0] p( 1190):     2   10   14   34   70  170  238
[0, 0] p( 1191):     3
[0, 0] p( 1192):     8
[0, 0] p( 1193):     1
[0, 0] p( 1194):     6
[0, 0] p( 1195):     1    5  239
[0, 0] p( 1196):     4   52   92
[0, 0] p( 1197):     9   63  171
[0, 0] p( 1198):     2
[0, 0] p( 1199):     1   11  109
[0, 0] p( 1200):    48  240
[0, 0] p( 1201):     1
[0, 0] p( 1202):     2
[0, 0] p( 1203):     3
[0, 0] p( 1204):     4   28  172
[0, 0] p( 1205):     1    5  241
[0, 0] p( 1206):    18
[0, 0] p( 1207):     1   17   71
[0, 0] p( 1208):     8
[0, 0] p( 1209):     3   39   93
[0, 0] p( 1210):     2   10   22  110  242
[0, 0] p( 1211):     1    7  173
[0, 0] p( 1212):    12
[0, 0] p( 1213):     1
[0, 0] p( 1214):     2
[0, 0] p( 1215):   243
[0, 0] p( 1216):    64
[0, 0] p( 1217):     1
[0, 0] p( 1218):     6   42  174
[0, 0] p( 1219):     1   23   53
[0, 0] p( 1220):     4   20  244
[0, 0] p( 1221):     3   33  111
[0, 0] p( 1222):     2   26   94
[0, 0] p( 1223):     1
[0, 0] p( 1224):    72
[0, 0] p( 1225):     1    5    7   25   35   49  175  245
[0, 0] p( 1226):     2
[0, 0] p( 1227):     3
[0, 0] p( 1228):     4
[0, 0] p( 1229):     1
[0, 0] p( 1230):     6   30  246
[0, 0] p( 1231):     1
[0, 0] p( 1232):    16  112  176
[0, 0] p( 1233):     9
[0, 0] p( 1234):     2
[0, 0] p( 1235):     1    5   13   19   65   95  247
[0, 0] p( 1236):    12
[0, 0] p( 1237):     1
[0, 0] p( 1238):     2
[0, 0] p( 1239):     3   21  177
[0, 0] p( 1240):     8   40  248
[0, 0] p( 1241):     1   17   73
[0, 0] p( 1242):    54
[0, 0] p( 1243):     1   11  113
[0, 0] p( 1244):     4
[0, 0] p( 1245):     3   15  249
[0, 0] p( 1246):     2   14  178
[0, 0] p( 1247):     1   29   43
[0, 0] p( 1248):    96
[0, 0] p( 1249):     1
[0, 0] p( 1250):     2   10   50  250
[0, 0] p( 1251):     9
[0, 0] p( 1252):     4
[0, 0] p( 1253):     1    7  179
[0, 0] p( 1254):     6   66  114
[0, 0] p( 1255):     1    5  251
[0, 0] p( 1256):     8
[0, 0] p( 1257):     3
[0, 0] p( 1258):     2   34   74
[0, 0] p( 1259):     1
[0, 0] p( 1260):    36  180  252
[0, 0] p( 1261):     1   13   97
[0, 0] p( 1262):     2
[0, 0] p( 1263):     3
[0, 0] p( 1264):    16
[0, 0] p( 1265):     1    5   11   23   55  115  253
[0, 0] p( 1266):     6
[0, 0] p( 1267):     1    7  181
[0, 0] p( 1268):     4
[0, 0] p( 1269):    27
[0, 0] p( 1270):     2   10  254
[0, 0] p( 1271):     1   31   41
[0, 0] p( 1272):    24
[0, 0] p( 1273):     1   19   67
[0, 0] p( 1274):     2   14   26   98  182
[0, 0] p( 1275):     3   15   51   75  255
[0, 0] p( 1276):     4   44  116
[0, 0] p( 1277):     1
[0, 0] p( 1278):    18
[0, 0] p( 1279):     1
[0, 0] p( 1280):   256
[0, 0] p( 1281):     3   21  183
[0, 0] p( 1282):     2
[0, 0] p( 1283):     1
[0, 0] p( 1284):    12
[0, 0] p( 1285):     1    5  257
[0, 0] p( 1286):     2
[0, 0] p( 1287):     9   99  117
[0, 0] p( 1288):     8   56  184
[0, 0] p( 1289):     1
[0, 0] p( 1290):     6   30  258
[0, 0] p( 1291):     1
[0, 0] p( 1292):     4   68   76
[0, 0] p( 1293):     3
[0, 0] p( 1294):     2
[0, 0] p( 1295):     1    5    7   35   37  185  259
[0, 0] p( 1296): 
[0, 0] p( 1297):     1
[0, 0] p( 1298):     2   22  118
[0, 0] p( 1299):     3
[0, 0] p( 1300):     4   20   52  100  260
[0, 0] p( 1301):     1
[0, 0] p( 1302):     6   42  186
[0, 0] p( 1303):     1
[0, 0] p( 1304):     8
[0, 0] p( 1305):     9   45  261
[0, 0] p( 1306):     2
[0, 0] p( 1307):     1
[0, 0] p( 1308):    12
[0, 0] p( 1309):     1    7   11   17   77  119  187
[0, 0] p( 1310):     2   10  262
[0, 0] p( 1311):     3   57   69
[0, 0] p( 1312):    32
[0, 0] p( 1313):     1   13  101
[0, 0] p( 1314):    18
[0, 0] p( 1315):     1    5  263
[0, 0] p( 1316):     4   28  188
[0, 0] p( 1317):     3
[0, 0] p( 1318):     2
[0, 0] p( 1319):     1
[0, 0] p( 1320):    24  120  264
[0, 0] p( 1321):     1
[0, 0] p( 1322):     2
[0, 0] p( 1323):    27  189
[0, 0] p( 1324):     4
[0, 0] p( 1325):     1    5   25   53  265
[0, 0] p( 1326):     6   78  102
[0, 0] p( 1327):     1
[0, 0] p( 1328):    16
[0, 0] p( 1329):     3
[0, 0] p( 1330):     2   10   14   38   70  190  266
[0, 0] p( 1331):     1   11  121
[0, 0] p( 1332):    36
[0, 0] p( 1333):     1   31   43
[0, 0] p( 1334):     2   46   58
[0, 0] p( 1335):     3   15  267
[0, 0] p( 1336):     8
[0, 0] p( 1337):     1    7  191
[0, 0] p( 1338):     6
[0, 0] p( 1339):     1   13  103
[0, 0] p( 1340):     4   20  268
[0, 0] p( 1341):     9
[0, 0] p( 1342):     2   22  122
[0, 0] p( 1343):     1   17   79
[0, 0] p( 1344):   192
[0, 0] p( 1345):     1    5  269
[0, 0] p( 1346):     2
[0, 0] p( 1347):     3
[0, 0] p( 1348):     4
[0, 0] p( 1349):     1   19   71
[0, 0] p( 1350):    54  270
[0, 0] p( 1351):     1    7  193
[0, 0] p( 1352):     8  104
[0, 0] p( 1353):     3   33  123
[0, 0] p( 1354):     2
[0, 0] p( 1355):     1    5  271
[0, 0] p( 1356):    12
[0, 0] p( 1357):     1   23   59
[0, 0] p( 1358):     2   14  194
[0, 0] p( 1359):     9
[0, 0] p( 1360):    16   80  272
[0, 0] p( 1361):     1
[0, 0] p( 1362):     6
[0, 0] p( 1363):     1   29   47
[0, 0] p( 1364):     4   44  124
[0, 0] p( 1365):     3   15   21   39  105  195  273
[0, 0] p( 1366):     2
[0, 0] p( 1367):     1
[0, 0] p( 1368):    72
[0, 0] p( 1369):     1   37
[0, 0] p( 1370):     2   10  274
[0, 0] p( 1371):     3
[0, 0] p( 1372):     4   28  196
[0, 0] p( 1373):     1
[0, 0] p( 1374):     6
[0, 0] p( 1375):     1    5   11   25   55  125  275
[0, 0] p( 1376):    32
[0, 0] p( 1377):    81
[0, 0] p( 1378):     2   26  106
[0, 0] p( 1379):     1    7  197
[0, 0] p( 1380):    12   60  276
[0, 0] p( 1381):     1
[0, 0] p( 1382):     2
[0, 0] p( 1383):     3
[0, 0] p( 1384):     8
[0, 0] p( 1385):     1    5  277
[0, 0] p( 1386):    18  126  198
[0, 0] p( 1387):     1   19   73
[0, 0] p( 1388):     4
[0, 0] p( 1389):     3
[0, 0] p( 1390):     2   10  278
[0, 0] p( 1391):     1   13  107
[0, 0] p( 1392):    48
[0, 0] p( 1393):     1    7  199
[0, 0] p( 1394):     2   34   82
[0, 0] p( 1395):     9   45  279
[0, 0] p( 1396):     4
[0, 0] p( 1397):     1   11  127
[0, 0] p( 1398):     6
[0, 0] p( 1399):     1
[0, 0] p( 1400):     8   40   56  200  280
[0, 0] p( 1401):     3
[0, 0] p( 1402):     2
[0, 0] p( 1403):     1   23   61
[0, 0] p( 1404):   108
[0, 0] p( 1405):     1    5  281
[0, 0] p( 1406):     2   38   74
[0, 0] p( 1407):     3   21  201
[0, 0] p( 1408):   128
[0, 0] p( 1409):     1
[0, 0] p( 1410):     6   30  282
[0, 0] p( 1411):     1   17   83
[0, 0] p( 1412):     4
[0, 0] p( 1413):     9
[0, 0] p( 1414):     2   14  202
[0, 0] p( 1415):     1    5  283
[0, 0] p( 1416):    24
[0, 0] p( 1417):     1   13  109
[0, 0] p( 1418):     2
[0, 0] p( 1419):     3   33  129
[0, 0] p( 1420):     4   20  284
[0, 0] p( 1421):     1    7   29   49  203
[0, 0] p( 1422):    18
[0, 0] p( 1423):     1
[0, 0] p( 1424):    16
[0, 0] p( 1425):     3   15   57   75  285
[0, 0] p( 1426):     2   46   62
[0, 0] p( 1427):     1
[0, 0] p( 1428):    12   84  204
[0, 0] p( 1429):     1
[0, 0] p( 1430):     2   10   22   26  110  130  286
[0, 0] p( 1431):    27
[0, 0] p( 1432):     8
[0, 0] p( 1433):     1
[0, 0] p( 1434):     6
[0, 0] p( 1435):     1    5    7   35   41  205  287
[0, 0] p( 1436):     4
[0, 0] p( 1437):     3
[0, 0] p( 1438):     2
[0, 0] p( 1439):     1
[0, 0] p( 1440):   288
[0, 0] p( 1441):     1   11  131
[0, 0] p( 1442):     2   14  206
[0, 0] p( 1443):     3   39  111
[0, 0] p( 1444):     4   76
[0, 0] p( 1445):     1    5   17   85  289
[0, 0] p( 1446):     6
[0, 0] p( 1447):     1
[0, 0] p( 1448):     8
[0, 0] p( 1449):     9   63  207
[0, 0] p( 1450):     2   10   50   58  290
[0, 0] p( 1451):     1
[0, 0] p( 1452):    12  132
[0, 0] p( 1453):     1
[0, 0] p( 1454):     2
[0, 0] p( 1455):     3   15  291
[0, 0] p( 1456):    16  112  208
[0, 0] p( 1457):     1   31   47
[0, 0] p( 1458): 
[0, 0] p( 1459):     1
[0, 0] p( 1460):     4   20  292
[0, 0] p( 1461):     3
[0, 0] p( 1462):     2   34   86
[0, 0] p( 1463):     1    7   11   19   77  133  209
[0, 0] p( 1464):    24
[0, 0] p( 1465):     1    5  293
[0, 0] p( 1466):     2
[0, 0] p( 1467):     9
[0, 0] p( 1468):     4
[0, 0] p( 1469):     1   13  113
[0, 0] p( 1470):     6   30   42  210  294
[0, 0] p( 1471):     1
[0, 0] p( 1472):    64
[0, 0] p( 1473):     3
[0, 0] p( 1474):     2   22  134
[0, 0] p( 1475):     1    5   25   59  295
[0, 0] p( 1476):    36
[0, 0] p( 1477):     1    7  211
[0, 0] p( 1478):     2
[0, 0] p( 1479):     3   51   87
[0, 0] p( 1480):     8   40  296
[0, 0] p( 1481):     1
[0, 0] p( 1482):     6   78  114
[0, 0] p( 1483):     1
[0, 0] p( 1484):     4   28  212
[0, 0] p( 1485):    27  135  297
[0, 0] p( 1486):     2
[0, 0] p( 1487):     1
[0, 0] p( 1488):    48
[0, 0] p( 1489):     1
[0, 0] p( 1490):     2   10  298
[0, 0] p( 1491):     3   21  213
[0, 0] p( 1492):     4
[0, 0] p( 1493):     1
[0, 0] p( 1494):    18
[0, 0] p( 1495):     1    5   13   23   65  115  299
[0, 0] p( 1496):     8   88  136
[0, 0] p( 1497):     3
[0, 0] p( 1498):     2   14  214
[0, 0] p( 1499):     1
[0, 0] p( 1500):    12   60  300
[0, 0] p( 1501):     1   19   79
[0, 0] p( 1502):     2
[0, 0] p( 1503):     9
[0, 0] p( 1504):    32
[0, 0] p( 1505):     1    5    7   35   43  215  301
[0, 0] p( 1506):     6
[0, 0] p( 1507):     1   11  137
[0, 0] p( 1508):     4   52  116
[0, 0] p( 1509):     3
[0, 0] p( 1510):     2   10  302
[0, 0] p( 1511):     1
[0, 0] p( 1512):   216
[0, 0] p( 1513):     1   17   89
[0, 0] p( 1514):     2
[0, 0] p( 1515):     3   15  303
[0, 0] p( 1516):     4
[0, 0] p( 1517):     1   37   41
[0, 0] p( 1518):     6   66  138
[0, 0] p( 1519):     1    7   31   49  217
[0, 0] p( 1520):    16   80  304
[0, 0] p( 1521):     9  117
[0, 0] p( 1522):     2
[0, 0] p( 1523):     1
[0, 0] p( 1524):    12
[0, 0] p( 1525):     1    5   25   61  305
[0, 0] p( 1526):     2   14  218
[0, 0] p( 1527):     3
[0, 0] p( 1528):     8
[0, 0] p( 1529):     1   11  139
[0, 0] p( 1530):    18   90  306
[0, 0] p( 1531):     1
[0, 0] p( 1532):     4
[0, 0] p( 1533):     3   21  219
[0, 0] p( 1534):     2   26  118
[0, 0] p( 1535):     1    5  307
[0, 0] p( 1536): 
[0, 0] p( 1537):     1   29   53
[0, 0] p( 1538):     2
[0, 0] p( 1539):    81
[0, 0] p( 1540):     4   20   28   44  140  220  308
[0, 0] p( 1541):     1   23   67
[0, 0] p( 1542):     6
[0, 0] p( 1543):     1
[0, 0] p( 1544):     8
[0, 0] p( 1545):     3   15  309
[0, 0] p( 1546):     2
[0, 0] p( 1547):     1    7   13   17   91  119  221
[0, 0] p( 1548):    36
[0, 0] p( 1549):     1
[0, 0] p( 1550):     2   10   50   62  310
[0, 0] p( 1551):     3   33  141
[0, 0] p( 1552):    16
[0, 0] p( 1553):     1
[0, 0] p( 1554):     6   42  222
[0, 0] p( 1555):     1    5  311
[0, 0] p( 1556):     4
[0, 0] p( 1557):     9
[0, 0] p( 1558):     2   38   82
[0, 0] p( 1559):     1
[0, 0] p( 1560):    24  120  312
[0, 0] p( 1561):     1    7  223
[0, 0] p( 1562):     2   22  142
[0, 0] p( 1563):     3
[0, 0] p( 1564):     4   68   92
[0, 0] p( 1565):     1    5  313
[0, 0] p( 1566):    54
[0, 0] p( 1567):     1
[0, 0] p( 1568):    32  224
[0, 0] p( 1569):     3
[0, 0] p( 1570):     2   10  314
[0, 0] p( 1571):     1
[0, 0] p( 1572):    12
[0, 0] p( 1573):     1   11   13  121  143
[0, 0] p( 1574):     2
[0, 0] p( 1575):     9   45   63  225  315
[0, 0] p( 1576):     8
[0, 0] p( 1577):     1   19   83
[0, 0] p( 1578):     6
[0, 0] p( 1579):     1
[0, 0] p( 1580):     4   20  316
[0, 0] p( 1581):     3   51   93
[0, 0] p( 1582):     2   14  226
[0, 0] p( 1583):     1
[0, 0] p( 1584):   144
[0, 0] p( 1585):     1    5  317
[0, 0] p( 1586):     2   26  122
[0, 0] p( 1587):     3   69
[0, 0] p( 1588):     4
[0, 0] p( 1589):     1    7  227
[0, 0] p( 1590):     6   30  318
[0, 0] p( 1591):     1   37   43
[0, 0] p( 1592):     8
[0, 0] p( 1593):    27
[0, 0] p( 1594):     2
[0, 0] p( 1595):     1    5   11   29   55  145  319
[0, 0] p( 1596):    12   84  228
[0, 0] p( 1597):     1
[0, 0] p( 1598):     2   34   94
[0, 0] p( 1599):     3   39  123
[0, 0] p( 1600):    64  320
[0, 0] p( 1601):     1
[0, 0] p( 1602):    18
[0, 0] p( 1603):     1    7  229
[0, 0] p( 1604):     4
[0, 0] p( 1605):     3   15  321
[0, 0] p( 1606):     2   22  146
[0, 0] p( 1607):     1
[0, 0] p( 1608):    24
[0, 0] p( 1609):     1
[0, 0] p( 1610):     2   10   14   46   70  230  322
[0, 0] p( 1611):     9
[0, 0] p( 1612):     4   52  124
[0, 0] p( 1613):     1
[0, 0] p( 1614):     6
[0, 0] p( 1615):     1    5   17   19   85   95  323
[0, 0] p( 1616):    16
[0, 0] p( 1617):     3   21   33  147  231
[0, 0] p( 1618):     2
[0, 0] p( 1619):     1
[0, 0] p( 1620):   324
[0, 0] p( 1621):     1
[0, 0] p( 1622):     2
[0, 0] p( 1623):     3
[0, 0] p( 1624):     8   56  232
[0, 0] p( 1625):     1    5   13   25   65  125  325
[0, 0] p( 1626):     6
[0, 0] p( 1627):     1
[0, 0] p( 1628):     4   44  148
[0, 0] p( 1629):     9
[0, 0] p( 1630):     2   10  326
[0, 0] p( 1631):     1    7  233
[0, 0] p( 1632):    96
[0, 0] p( 1633):     1   23   71
[0, 0] p( 1634):     2   38   86
[0, 0] p( 1635):     3   15  327
[0, 0] p( 1636):     4
[0, 0] p( 1637):     1
[0, 0] p( 1638):    18  126  234
[0, 0] p( 1639):     1   11  149
[0, 0] p( 1640):     8   40  328
[0, 0] p( 1641):     3
[0, 0] p( 1642):     2
[0, 0] p( 1643):     1   31   53
[0, 0] p( 1644):    12
[0, 0] p( 1645):     1    5    7   35   47  235  329
[0, 0] p( 1646):     2
[0, 0] p( 1647):    27
[0, 0] p( 1648):    16
[0, 0] p( 1649):     1   17   97
[0, 0] p( 1650):     6   30   66  150  330
[0, 0] p( 1651):     1   13  127
[0, 0] p( 1652):     4   28  236
[0, 0] p( 1653):     3   57   87
[0, 0] p( 1654):     2
[0, 0] p( 1655):     1    5  331
[0, 0] p( 1656):    72
[0, 0] p( 1657):     1
[0, 0] p( 1658):     2
[0, 0] p( 1659):     3   21  237
[0, 0] p( 1660):     4   20  332
[0, 0] p( 1661):     1   11  151
[0, 0] p( 1662):     6
[0, 0] p( 1663):     1
[0, 0] p( 1664):   128
[0, 0] p( 1665):     9   45  333
[0, 0] p( 1666):     2   14   34   98  238
[0, 0] p( 1667):     1
[0, 0] p( 1668):    12
[0, 0] p( 1669):     1
[0, 0] p( 1670):     2   10  334
[0, 0] p( 1671):     3
[0, 0] p( 1672):     8   88  152
[0, 0] p( 1673):     1    7  239
[0, 0] p( 1674):    54
[0, 0] p( 1675):     1    5   25   67  335
[0, 0] p( 1676):     4
[0, 0] p( 1677):     3   39  129
[0, 0] p( 1678):     2
[0, 0] p( 1679):     1   23   73
[0, 0] p( 1680):    48  240  336
[0, 0] p( 1681):     1   41
[0, 0] p( 1682):     2   58
[0, 0] p( 1683):     9   99  153
[0, 0] p( 1684):     4
[0, 0] p( 1685):     1    5  337
[0, 0] p( 1686):     6
[0, 0] p( 1687):     1    7  241
[0, 0] p( 1688):     8
[0, 0] p( 1689):     3
[0, 0] p( 1690):     2   10   26  130  338
[0, 0] p( 1691):     1   19   89
[0, 0] p( 1692):    36
[0, 0] p( 1693):     1
[0, 0] p( 1694):     2   14   22  154  242
[0, 0] p( 1695):     3   15  339
[0, 0] p( 1696):    32
[0, 0] p( 1697):     1
[0, 0] p( 1698):     6
[0, 0] p( 1699):     1
[0, 0] p( 1700):     4   20   68  100  340
[0, 0] p( 1701):   243
[0, 0] p( 1702):     2   46   74
[0, 0] p( 1703):     1   13  131
[0, 0] p( 1704):    24
[0, 0] p( 1705):     1    5   11   31   55  155  341
[0, 0] p( 1706):     2
[0, 0] p( 1707):     3
[0, 0] p( 1708):     4   28  244
[0, 0] p( 1709):     1
[0, 0] p( 1710):    18   90  342
[0, 0] p( 1711):     1   29   59
[0, 0] p( 1712):    16
[0, 0] p( 1713):     3
[0, 0] p( 1714):     2
[0, 0] p( 1715):     1    5    7   35   49  245  343
[0, 0] p( 1716):    12  132  156
[0, 0] p( 1717):     1   17  101
[0, 0] p( 1718):     2
[0, 0] p( 1719):     9
[0, 0] p( 1720):     8   40  344
[0, 0] p( 1721):     1
[0, 0] p( 1722):     6   42  246
[0, 0] p( 1723):     1
[0, 0] p( 1724):     4
[0, 0] p( 1725):     3   15   69   75  345
[0, 0] p( 1726):     2
[0, 0] p( 1727):     1   11  157
[0, 0] p( 1728): 
[0, 0] p( 1729):     1    7   13   19   91  133  247
[0, 0] p( 1730):     2   10  346
[0, 0] p( 1731):     3
[0, 0] p( 1732):     4
[0, 0] p( 1733):     1
[0, 0] p( 1734):     6  102
[0, 0] p( 1735):     1    5  347
[0, 0] p( 1736):     8   56  248
[0, 0] p( 1737):     9
[0, 0] p( 1738):     2   22  158
[0, 0] p( 1739):     1   37   47
[0, 0] p( 1740):    12   60  348
[0, 0] p( 1741):     1
[0, 0] p( 1742):     2   26  134
[0, 0] p( 1743):     3   21  249
[0, 0] p( 1744):    16
[0, 0] p( 1745):     1    5  349
[0, 0] p( 1746):    18
[0, 0] p( 1747):     1
[0, 0] p( 1748):     4   76   92
[0, 0] p( 1749):     3   33  159
[0, 0] p( 1750):     2   10   14   50   70  250  350
[0, 0] p( 1751):     1   17  103
[0, 0] p( 1752):    24
[0, 0] p( 1753):     1
[0, 0] p( 1754):     2
[0, 0] p( 1755):    27  135  351
[0, 0] p( 1756):     4
[0, 0] p( 1757):     1    7  251
[0, 0] p( 1758):     6
[0, 0] p( 1759):     1
[0, 0] p( 1760):    32  160  352
[0, 0] p( 1761):     3
[0, 0] p( 1762):     2
[0, 0] p( 1763):     1   41   43
[0, 0] p( 1764):    36  252
[0, 0] p( 1765):     1    5  353
[0, 0] p( 1766):     2
[0, 0] p( 1767):     3   57   93
[0, 0] p( 1768):     8  104  136
[0, 0] p( 1769):     1   29   61
[0, 0] p( 1770):     6   30  354
[0, 0] p( 1771):     1    7   11   23   77  161  253
[0, 0] p( 1772):     4
[0, 0] p( 1773):     9
[0, 0] p( 1774):     2
[0, 0] p( 1775):     1    5   25   71  355
[0, 0] p( 1776):    48
[0, 0] p( 1777):     1
[0, 0] p( 1778):     2   14  254
[0, 0] p( 1779):     3
[0, 0] p( 1780):     4   20  356
[0, 0] p( 1781):     1   13  137
[0, 0] p( 1782):   162
[0, 0] p( 1783):     1
[0, 0] p( 1784):     8
[0, 0] p( 1785):     3   15   21   51  105  255  357
[0, 0] p( 1786):     2   38   94
[0, 0] p( 1787):     1
[0, 0] p( 1788):    12
[0, 0] p( 1789):     1
[0, 0] p( 1790):     2   10  358
[0, 0] p( 1791):     9
[0, 0] p( 1792):   256
[0, 0] p( 1793):     1   11  163
[0, 0] p( 1794):     6   78  138
[0, 0] p( 1795):     1    5  359
[0, 0] p( 1796):     4
[0, 0] p( 1797):     3
[0, 0] p( 1798):     2   58   62
[0, 0] p( 1799):     1    7  257
[0, 0] p( 1800):    72  360
[0, 0] p( 1801):     1
[0, 0] p( 1802):     2   34  106
[0, 0] p( 1803):     3
[0, 0] p( 1804):     4   44  164
[0, 0] p( 1805):     1    5   19   95  361
[0, 0] p( 1806):     6   42  258
[0, 0] p( 1807):     1   13  139
[0, 0] p( 1808):    16
[0, 0] p( 1809):    27
[0, 0] p( 1810):     2   10  362
[0, 0] p( 1811):     1
[0, 0] p( 1812):    12
[0, 0] p( 1813):     1    7   37   49  259
[0, 0] p( 1814):     2
[0, 0] p( 1815):     3   15   33  165  363
[0, 0] p( 1816):     8
[0, 0] p( 1817):     1   23   79
[0, 0] p( 1818):    18
[0, 0] p( 1819):     1   17  107
[0, 0] p( 1820):     4   20   28   52  140  260  364
[0, 0] p( 1821):     3
[0, 0] p( 1822):     2
[0, 0] p( 1823):     1
[0, 0] p( 1824):    96
[0, 0] p( 1825):     1    5   25   73  365
[0, 0] p( 1826):     2   22  166
[0, 0] p( 1827):     9   63  261
[0, 0] p( 1828):     4
[0, 0] p( 1829):     1   31   59
[0, 0] p( 1830):     6   30  366
[0, 0] p( 1831):     1
[0, 0] p( 1832):     8
[0, 0] p( 1833):     3   39  141
[0, 0] p( 1834):     2   14  262
[0, 0] p( 1835):     1    5  367
[0, 0] p( 1836):   108
[0, 0] p( 1837):     1   11  167
[0, 0] p( 1838):     2
[0, 0] p( 1839):     3
[0, 0] p( 1840):    16   80  368
[0, 0] p( 1841):     1    7  263
[0, 0] p( 1842):     6
[0, 0] p( 1843):     1   19   97
[0, 0] p( 1844):     4
[0, 0] p( 1845):     9   45  369
[0, 0] p( 1846):     2   26  142
[0, 0] p( 1847):     1
[0, 0] p( 1848):    24  168  264
[0, 0] p( 1849):     1   43
[0, 0] p( 1850):     2   10   50   74  370
[0, 0] p( 1851):     3
[0, 0] p( 1852):     4
[0, 0] p( 1853):     1   17  109
[0, 0] p( 1854):    18
[0, 0] p( 1855):     1    5    7   35   53  265  371
[0, 0] p( 1856):    64
[0, 0] p( 1857):     3
[0, 0] p( 1858):     2
[0, 0] p( 1859):     1   11   13  143  169
[0, 0] p( 1860):    12   60  372
[0, 0] p( 1861):     1
[0, 0] p( 1862):     2   14   38   98  266
[0, 0] p( 1863):    81
[0, 0] p( 1864):     8
[0, 0] p( 1865):     1    5  373
[0, 0] p( 1866):     6
[0, 0] p( 1867):     1
[0, 0] p( 1868):     4
[0, 0] p( 1869):     3   21  267
[0, 0] p( 1870):     2   10   22   34  110  170  374
[0, 0] p( 1871):     1
[0, 0] p( 1872):   144
[0, 0] p( 1873):     1
[0, 0] p( 1874):     2
[0, 0] p( 1875):     3   15   75  375
[0, 0] p( 1876):     4   28  268
[0, 0] p( 1877):     1
[0, 0] p( 1878):     6
[0, 0] p( 1879):     1
[0, 0] p( 1880):     8   40  376
[0, 0] p( 1881):     9   99  171
[0, 0] p( 1882):     2
[0, 0] p( 1883):     1    7  269
[0, 0] p( 1884):    12
[0, 0] p( 1885):     1    5   13   29   65  145  377
[0, 0] p( 1886):     2   46   82
[0, 0] p( 1887):     3   51  111
[0, 0] p( 1888):    32
[0, 0] p( 1889):     1
[0, 0] p( 1890):    54  270  378
[0, 0] p( 1891):     1   31   61
[0, 0] p( 1892):     4   44  172
[0, 0] p( 1893):     3
[0, 0] p( 1894):     2
[0, 0] p( 1895):     1    5  379
[0, 0] p( 1896):    24
[0, 0] p( 1897):     1    7  271
[0, 0] p( 1898):     2   26  146
[0, 0] p( 1899):     9
[0, 0] p( 1900):     4   20   76  100  380
[0, 0] p( 1901):     1
[0, 0] p( 1902):     6
[0, 0] p( 1903):     1   11  173
[0, 0] p( 1904):    16  112  272
[0, 0] p( 1905):     3   15  381
[0, 0] p( 1906):     2
[0, 0] p( 1907):     1
[0, 0] p( 1908):    36
[0, 0] p( 1909):     1   23   83
[0, 0] p( 1910):     2   10  382
[0, 0] p( 1911):     3   21   39  147  273
[0, 0] p( 1912):     8
[0, 0] p( 1913):     1
[0, 0] p( 1914):     6   66  174
[0, 0] p( 1915):     1    5  383
[0, 0] p( 1916):     4
[0, 0] p( 1917):    27
[0, 0] p( 1918):     2   14  274
[0, 0] p( 1919):     1   19  101
[0, 0] p( 1920):   384
[0, 0] p( 1921):     1   17  113
[0, 0] p( 1922):     2   62
[0, 0] p( 1923):     3
[0, 0] p( 1924):     4   52  148
[0, 0] p( 1925):     1    5    7   11   25   35   55   77  175  275  385
[0, 0] p( 1926):    18
[0, 0] p( 1927):     1   41   47
[0, 0] p( 1928):     8
[0, 0] p( 1929):     3
[0, 0] p( 1930):     2   10  386
[0, 0] p( 1931):     1
[0, 0] p( 1932):    12   84  276
[0, 0] p( 1933):     1
[0, 0] p( 1934):     2
[0, 0] p( 1935):     9   45  387
[0, 0] p( 1936):    16  176
[0, 0] p( 1937):     1   13  149
[0, 0] p( 1938):     6  102  114
[0, 0] p( 1939):     1    7  277
[0, 0] p( 1940):     4   20  388
[0, 0] p( 1941):     3
[0, 0] p( 1942):     2
[0, 0] p( 1943):     1   29   67
[0, 0] p( 1944): 
[0, 0] p( 1945):     1    5  389
[0, 0] p( 1946):     2   14  278
[0, 0] p( 1947):     3   33  177
[0, 0] p( 1948):     4
[0, 0] p( 1949):     1
[0, 0] p( 1950):     6   30   78  150  390
[0, 0] p( 1951):     1
[0, 0] p( 1952):    32
[0, 0] p( 1953):     9   63  279
[0, 0] p( 1954):     2
[0, 0] p( 1955):     1    5   17   23   85  115  391
[0, 0] p( 1956):    12
[0, 0] p( 1957):     1   19  103
[0, 0] p( 1958):     2   22  178
[0, 0] p( 1959):     3
[0, 0] p( 1960):     8   40   56  280  392
[0, 0] p( 1961):     1   37   53
[0, 0] p( 1962):    18
[0, 0] p( 1963):     1   13  151
[0, 0] p( 1964):     4
[0, 0] p( 1965):     3   15  393
[0, 0] p( 1966):     2
[0, 0] p( 1967):     1    7  281
[0, 0] p( 1968):    48
[0, 0] p( 1969):     1   11  179
[0, 0] p( 1970):     2   10  394
[0, 0] p( 1971):    27
[0, 0] p( 1972):     4   68  116
[0, 0] p( 1973):     1
[0, 0] p( 1974):     6   42  282
[0, 0] p( 1975):     1    5   25   79  395
[0, 0] p( 1976):     8  104  152
[0, 0] p( 1977):     3
[0, 0] p( 1978):     2   46   86
[0, 0] p( 1979):     1
[0, 0] p( 1980):    36  180  396
[0, 0] p( 1981):     1    7  283
[0, 0] p( 1982):     2
[0, 0] p( 1983):     3
[0, 0] p( 1984):    64
[0, 0] p( 1985):     1    5  397
[0, 0] p( 1986):     6
[0, 0] p( 1987):     1
[0, 0] p( 1988):     4   28  284
[0, 0] p( 1989):     9  117  153
[0, 0] p( 1990):     2   10  398
[0, 0] p( 1991):     1   11  181
[0, 0] p( 1992):    24
[0, 0] p( 1993):     1
[0, 0] p( 1994):     2
[0, 0] p( 1995):     3   15   21   57  105  285  399
[0, 0] p( 1996):     4
[0, 0] p( 1997):     1
[0, 0] p( 1998):    54
[0, 0] p( 1999):     1
[0, 0] p( 2000):    16   80  400
[0, 0] p( 2001):     3   69   87
[0, 0] p( 2002):     2   14   22   26  154  182  286
[0, 0] p( 2003):     1
[0, 0] p( 2004):    12
[0, 0] p( 2005):     1    5  401
[0, 0] p( 2006):     2   34  118
[0, 0] p( 2007):     9
[0, 0] p( 2008):     8
[0, 0] p( 2009):     1    7   41   49  287
[0, 0] p( 2010):     6   30  402
[0, 0] p( 2011):     1
[0, 0] p( 2012):     4
[0, 0] p( 2013):     3   33  183
[0, 0] p( 2014):     2   38  106
[0, 0] p( 2015):     1    5   13   31   65  155  403
[0, 0] p( 2016):   288
[0, 0] p( 2017):     1
[0, 0] p( 2018):     2
[0, 0] p( 2019):     3
[0, 0] p( 2020):     4   20  404
[0, 0] p( 2021):     1   43   47
[0, 0] p( 2022):     6
[0, 0] p( 2023):     1    7   17  119  289
[0, 0] p( 2024):     8   88  184
[0, 0] p( 2025):    81  405
[0, 0] p( 2026):     2
[0, 0] p( 2027):     1
[0, 0] p( 2028):    12  156
[0, 0] p( 2029):     1
[0, 0] p( 2030):     2   10   14   58   70  290  406
[0, 0] p( 2031):     3
[0, 0] p( 2032):    16
[0, 0] p( 2033):     1   19  107
[0, 0] p( 2034):    18
[0, 0] p( 2035):     1    5   11   37   55  185  407
[0, 0] p( 2036):     4
[0, 0] p( 2037):     3   21  291
[0, 0] p( 2038):     2
[0, 0] p( 2039):     1
[0, 0] p( 2040):    24  120  408
[0, 0] p( 2041):     1   13  157
[0, 0] p( 2042):     2
[0, 0] p( 2043):     9
[0, 0] p( 2044):     4   28  292
[0, 0] p( 2045):     1    5  409
[0, 0] p( 2046):     6   66  186
[0, 0] p( 2047):     1   23   89
[0, 0] p( 2048): 
[0, 0] p( 2049):     3
[0, 0] p( 2050):     2   10   50   82  410
[0, 0] p( 2051):     1    7  293
[0, 0] p( 2052):   108
[0, 0] p( 2053):     1
[0, 0] p( 2054):     2   26  158
[0, 0] p( 2055):     3   15  411
[0, 0] p( 2056):     8
[0, 0] p( 2057):     1   11   17  121  187
[0, 0] p( 2058):     6   42  294
[0, 0] p( 2059):     1   29   71
[0, 0] p( 2060):     4   20  412
[0, 0] p( 2061):     9
[0, 0] p( 2062):     2
[0, 0] p( 2063):     1
[0, 0] p( 2064):    48
[0, 0] p( 2065):     1    5    7   35   59  295  413
[0, 0] p( 2066):     2
[0, 0] p( 2067):     3   39  159
[0, 0] p( 2068):     4   44  188
[0, 0] p( 2069):     1
[0, 0] p( 2070):    18   90  414
[0, 0] p( 2071):     1   19  109
[0, 0] p( 2072):     8   56  296
[0, 0] p( 2073):     3
[0, 0] p( 2074):     2   34  122
[0, 0] p( 2075):     1    5   25   83  415
[0, 0] p( 2076):    12
[0, 0] p( 2077):     1   31   67
[0, 0] p( 2078):     2
[0, 0] p( 2079):    27  189  297
[0, 0] p( 2080):    32  160  416
[0, 0] p( 2081):     1
[0, 0] p( 2082):     6
[0, 0] p( 2083):     1
[0, 0] p( 2084):     4
[0, 0] p( 2085):     3   15  417
[0, 0] p( 2086):     2   14  298
[0, 0] p( 2087):     1
[0, 0] p( 2088):    72
[0, 0] p( 2089):     1
[0, 0] p( 2090):     2   10   22   38  110  190  418
[0, 0] p( 2091):     3   51  123
[0, 0] p( 2092):     4
[0, 0] p( 2093):     1    7   13   23   91  161  299
[0, 0] p( 2094):     6
[0, 0] p( 2095):     1    5  419
[0, 0] p( 2096):    16
[0, 0] p( 2097):     9
[0, 0] p( 2098):     2
[0, 0] p( 2099):     1
[0, 0] p( 2100):    12   60   84  300  420
[0, 0] p( 2101):     1   11  191
[0, 0] p( 2102):     2
[0, 0] p( 2103):     3
[0, 0] p( 2104):     8
[0, 0] p( 2105):     1    5  421
[0, 0] p( 2106):   162
[0, 0] p( 2107):     1    7   43   49  301
[0, 0] p( 2108):     4   68  124
[0, 0] p( 2109):     3   57  111
[0, 0] p( 2110):     2   10  422
[0, 0] p( 2111):     1
[0, 0] p( 2112):   192
[0, 0] p( 2113):     1
[0, 0] p( 2114):     2   14  302
[0, 0] p( 2115):     9   45  423
[0, 0] p( 2116):     4   92
[0, 0] p( 2117):     1   29   73
[0, 0] p( 2118):     6
[0, 0] p( 2119):     1   13  163
[0, 0] p( 2120):     8   40  424
[0, 0] p( 2121):     3   21  303
[0, 0] p( 2122):     2
[0, 0] p( 2123):     1   11  193
[0, 0] p( 2124):    36
[0, 0] p( 2125):     1    5   17   25   85  125  425
[0, 0] p( 2126):     2
[0, 0] p( 2127):     3
[0, 0] p( 2128):    16  112  304
[0, 0] p( 2129):     1
[0, 0] p( 2130):     6   30  426
[0, 0] p( 2131):     1
[0, 0] p( 2132):     4   52  164
[0, 0] p( 2133):    27
[0, 0] p( 2134):     2   22  194
[0, 0] p( 2135):     1    5    7   35   61  305  427
[0, 0] p( 2136):    24
[0, 0] p( 2137):     1
[0, 0] p( 2138):     2
[0, 0] p( 2139):     3   69   93
[0, 0] p( 2140):     4   20  428
[0, 0] p( 2141):     1
[0, 0] p( 2142):    18  126  306
[0, 0] p( 2143):     1
[0, 0] p( 2144):    32
[0, 0] p( 2145):     3   15   33   39  165  195  429
[0, 0] p( 2146):     2   58   74
[0, 0] p( 2147):     1   19  113
[0, 0] p( 2148):    12
[0, 0] p( 2149):     1    7  307
[0, 0] p( 2150):     2   10   50   86  430
[0, 0] p( 2151):     9
[0, 0] p( 2152):     8
[0, 0] p( 2153):     1
[0, 0] p( 2154):     6
[0, 0] p( 2155):     1    5  431
[0, 0] p( 2156):     4   28   44  196  308
[0, 0] p( 2157):     3
[0, 0] p( 2158):     2   26  166
[0, 0] p( 2159):     1   17  127
[0, 0] p( 2160):   432
[0, 0] p( 2161):     1
[0, 0] p( 2162):     2   46   94
[0, 0] p( 2163):     3   21  309
[0, 0] p( 2164):     4
[0, 0] p( 2165):     1    5  433
[0, 0] p( 2166):     6  114
[0, 0] p( 2167):     1   11  197
[0, 0] p( 2168):     8
[0, 0] p( 2169):     9
[0, 0] p( 2170):     2   10   14   62   70  310  434
[0, 0] p( 2171):     1   13  167
[0, 0] p( 2172):    12
[0, 0] p( 2173):     1   41   53
[0, 0] p( 2174):     2
[0, 0] p( 2175):     3   15   75   87  435
[0, 0] p( 2176):   128
[0, 0] p( 2177):     1    7  311
[0, 0] p( 2178):    18  198
[0, 0] p( 2179):     1
[0, 0] p( 2180):     4   20  436
[0, 0] p( 2181):     3
[0, 0] p( 2182):     2
[0, 0] p( 2183):     1   37   59
[0, 0] p( 2184):    24  168  312
[0, 0] p( 2185):     1    5   19   23   95  115  437
[0, 0] p( 2186):     2
[0, 0] p( 2187): 
[0, 0] p( 2188):     4
[0, 0] p( 2189):     1   11  199
[0, 0] p( 2190):     6   30  438
[0, 0] p( 2191):     1    7  313
[0, 0] p( 2192):    16
[0, 0] p( 2193):     3   51  129
[0, 0] p( 2194):     2
[0, 0] p( 2195):     1    5  439
[0, 0] p( 2196):    36
[0, 0] p( 2197):     1   13  169
[0, 0] p( 2198):     2   14  314
[0, 0] p( 2199):     3
[0, 0] p( 2200):     8   40   88  200  440
[0, 0] p( 2201):     1   31   71
[0, 0] p( 2202):     6
[0, 0] p( 2203):     1
[0, 0] p( 2204):     4   76  116
[0, 0] p( 2205):     9   45   63  315  441
[0, 0] p( 2206):     2
[0, 0] p( 2207):     1
[0, 0] p( 2208):    96
[0, 0] p( 2209):     1   47
[0, 0] p( 2210):     2   10   26   34  130  170  442
[0, 0] p( 2211):     3   33  201
[0, 0] p( 2212):     4   28  316
[0, 0] p( 2213):     1
[0, 0] p( 2214):    54
[0, 0] p( 2215):     1    5  443
[0, 0] p( 2216):     8
[0, 0] p( 2217):     3
[0, 0] p( 2218):     2
[0, 0] p( 2219):     1    7  317
[0, 0] p( 2220):    12   60  444
[0, 0] p( 2221):     1
[0, 0] p( 2222):     2   22  202
[0, 0] p( 2223):     9  117  171
[0, 0] p( 2224):    16
[0, 0] p( 2225):     1    5   25   89  445
[0, 0] p( 2226):     6   42  318
[0, 0] p( 2227):     1   17  131
[0, 0] p( 2228):     4
[0, 0] p( 2229):     3
[0, 0] p( 2230):     2   10  446
[0, 0] p( 2231):     1   23   97
[0, 0] p( 2232):    72
[0, 0] p( 2233):     1    7   11   29   77  203  319
[0, 0] p( 2234):     2
[0, 0] p( 2235):     3   15  447
[0, 0] p( 2236):     4   52  172
[0, 0] p( 2237):     1
[0, 0] p( 2238):     6
[0, 0] p( 2239):     1
[0, 0] p( 2240):    64  320  448
[0, 0] p( 2241):    27
[0, 0] p( 2242):     2   38  118
[0, 0] p( 2243):     1
[0, 0] p( 2244):    12  132  204
[0, 0] p( 2245):     1    5  449
[0, 0] p( 2246):     2
[0, 0] p( 2247):     3   21  321
[0, 0] p( 2248):     8
[0, 0] p( 2249):     1   13  173
[0, 0] p( 2250):    18   90  450
[0, 0] p( 2251):     1
[0, 0] p( 2252):     4
[0, 0] p( 2253):     3
[0, 0] p( 2254):     2   14   46   98  322
[0, 0] p( 2255):     1    5   11   41   55  205  451
[0, 0] p( 2256):    48
[0, 0] p( 2257):     1   37   61
[0, 0] p( 2258):     2
[0, 0] p( 2259):     9
[0, 0] p( 2260):     4   20  452
[0, 0] p( 2261):     1    7   17   19  119  133  323
[0, 0] p( 2262):     6   78  174
[0, 0] p( 2263):     1   31   73
[0, 0] p( 2264):     8
[0, 0] p( 2265):     3   15  453
[0, 0] p( 2266):     2   22  206
[0, 0] p( 2267):     1
[0, 0] p( 2268):   324
[0, 0] p( 2269):     1
[0, 0] p( 2270):     2   10  454
[0, 0] p( 2271):     3
[0, 0] p( 2272):    32
[0, 0] p( 2273):     1
[0, 0] p( 2274):     6
[0, 0] p( 2275):     1    5    7   13   25   35   65   91  175  325  455
[0, 0] p( 2276):     4
[0, 0] p( 2277):     9   99  207
[0, 0] p( 2278):     2   34  134
[0, 0] p( 2279):     1   43   53
[0, 0] p( 2280):    24  120  456
[0, 0] p( 2281):     1
[0, 0] p( 2282):     2   14  326
[0, 0] p( 2283):     3
[0, 0] p( 2284):     4
[0, 0] p( 2285):     1    5  457
[0, 0] p( 2286):    18
[0, 0] p( 2287):     1
[0, 0] p( 2288):    16  176  208
[0, 0] p( 2289):     3   21  327
[0, 0] p( 2290):     2   10  458
[0, 0] p( 2291):     1   29   79
[0, 0] p( 2292):    12
[0, 0] p( 2293):     1
[0, 0] p( 2294):     2   62   74
[0, 0] p( 2295):    27  135  459
[0, 0] p( 2296):     8   56  328
[0, 0] p( 2297):     1
[0, 0] p( 2298):     6
[0, 0] p( 2299):     1   11   19  121  209
[0, 0] p( 2300):     4   20   92  100  460
[0, 0] p( 2301):     3   39  177
[0, 0] p( 2302):     2
[0, 0] p( 2303):     1    7   47   49  329
[0, 0] p( 2304): 
[0, 0] p( 2305):     1    5  461
[0, 0] p( 2306):     2
[0, 0] p( 2307):     3
[0, 0] p( 2308):     4
[0, 0] p( 2309):     1
[0, 0] p( 2310):     6   30   42   66  210  330  462
[0, 0] p( 2311):     1
[0, 0] p( 2312):     8  136
[0, 0] p( 2313):     9
[0, 0] p( 2314):     2   26  178
[0, 0] p( 2315):     1    5  463
[0, 0] p( 2316):    12
[0, 0] p( 2317):     1    7  331
[0, 0] p( 2318):     2   38  122
[0, 0] p( 2319):     3
[0, 0] p( 2320):    16   80  464
[0, 0] p( 2321):     1   11  211
[0, 0] p( 2322):    54
[0, 0] p( 2323):     1   23  101
[0, 0] p( 2324):     4   28  332
[0, 0] p( 2325):     3   15   75   93  465
[0, 0] p( 2326):     2
[0, 0] p( 2327):     1   13  179
[0, 0] p( 2328):    24
[0, 0] p( 2329):     1   17  137
[0, 0] p( 2330):     2   10  466
[0, 0] p( 2331):     9   63  333
[0, 0] p( 2332):     4   44  212
[0, 0] p( 2333):     1
[0, 0] p( 2334):     6
[0, 0] p( 2335):     1    5  467
[0, 0] p( 2336):    32
[0, 0] p( 2337):     3   57  123
[0, 0] p( 2338):     2   14  334
[0, 0] p( 2339):     1
[0, 0] p( 2340):    36  180  468
[0, 0] p( 2341):     1
[0, 0] p( 2342):     2
[0, 0] p( 2343):     3   33  213
[0, 0] p( 2344):     8
[0, 0] p( 2345):     1    5    7   35   67  335  469
[0, 0] p( 2346):     6  102  138
[0, 0] p( 2347):     1
[0, 0] p( 2348):     4
[0, 0] p( 2349):    81
[0, 0] p( 2350):     2   10   50   94  470
[0, 0] p( 2351):     1
[0, 0] p( 2352):    48  336
[0, 0] p( 2353):     1   13  181
[0, 0] p( 2354):     2   22  214
[0, 0] p( 2355):     3   15  471
[0, 0] p( 2356):     4   76  124
[0, 0] p( 2357):     1
[0, 0] p( 2358):    18
[0, 0] p( 2359):     1    7  337
[0, 0] p( 2360):     8   40  472
[0, 0] p( 2361):     3
[0, 0] p( 2362):     2
[0, 0] p( 2363):     1   17  139
[0, 0] p( 2364):    12
[0, 0] p( 2365):     1    5   11   43   55  215  473
[0, 0] p( 2366):     2   14   26  182  338
[0, 0] p( 2367):     9
[0, 0] p( 2368):    64
[0, 0] p( 2369):     1   23  103
[0, 0] p( 2370):     6   30  474
[0, 0] p( 2371):     1
[0, 0] p( 2372):     4
[0, 0] p( 2373):     3   21  339
[0, 0] p( 2374):     2
[0, 0] p( 2375):     1    5   19   25   95  125  475
[0, 0] p( 2376):   216
[0, 0] p( 2377):     1
[0, 0] p( 2378):     2   58   82
[0, 0] p( 2379):     3   39  183
[0, 0] p( 2380):     4   20   28   68  140  340  476
[0, 0] p( 2381):     1
[0, 0] p( 2382):     6
[0, 0] p( 2383):     1
[0, 0] p( 2384):    16
[0, 0] p( 2385):     9   45  477
[0, 0] p( 2386):     2
[0, 0] p( 2387):     1    7   11   31   77  217  341
[0, 0] p( 2388):    12
[0, 0] p( 2389):     1
[0, 0] p( 2390):     2   10  478
[0, 0] p( 2391):     3
[0, 0] p( 2392):     8  104  184
[0, 0] p( 2393):     1
[0, 0] p( 2394):    18  126  342
[0, 0] p( 2395):     1    5  479
[0, 0] p( 2396):     4
[0, 0] p( 2397):     3   51  141
[0, 0] p( 2398):     2   22  218
[0, 0] p( 2399):     1
[0, 0] p( 2400):    96  480
[0, 0] p( 2401):     1    7   49  343
[0, 0] p( 2402):     2
[0, 0] p( 2403):    27
[0, 0] p( 2404):     4
[0, 0] p( 2405):     1    5   13   37   65  185  481
[0, 0] p( 2406):     6
[0, 0] p( 2407):     1   29   83
[0, 0] p( 2408):     8   56  344
[0, 0] p( 2409):     3   33  219
[0, 0] p( 2410):     2   10  482
[0, 0] p( 2411):     1
[0, 0] p( 2412):    36
[0, 0] p( 2413):     1   19  127
[0, 0] p( 2414):     2   34  142
[0, 0] p( 2415):     3   15   21   69  105  345  483
[0, 0] p( 2416):    16
[0, 0] p( 2417):     1
[0, 0] p( 2418):     6   78  186
[0, 0] p( 2419):     1   41   59
[0, 0] p( 2420):     4   20   44  220  484
[0, 0] p( 2421):     9
[0, 0] p( 2422):     2   14  346
[0, 0] p( 2423):     1
[0, 0] p( 2424):    24
[0, 0] p( 2425):     1    5   25   97  485
[0, 0] p( 2426):     2
[0, 0] p( 2427):     3
[0, 0] p( 2428):     4
[0, 0] p( 2429):     1    7  347
[0, 0] p( 2430):   486
[0, 0] p( 2431):     1   11   13   17  143  187  221
[0, 0] p( 2432):   128
[0, 0] p( 2433):     3
[0, 0] p( 2434):     2
[0, 0] p( 2435):     1    5  487
[0, 0] p( 2436):    12   84  348
[0, 0] p( 2437):     1
[0, 0] p( 2438):     2   46  106
[0, 0] p( 2439):     9
[0, 0] p( 2440):     8   40  488
[0, 0] p( 2441):     1
[0, 0] p( 2442):     6   66  222
[0, 0] p( 2443):     1    7  349
[0, 0] p( 2444):     4   52  188
[0, 0] p( 2445):     3   15  489
[0, 0] p( 2446):     2
[0, 0] p( 2447):     1
[0, 0] p( 2448):   144
[0, 0] p( 2449):     1   31   79
[0, 0] p( 2450):     2   10   14   50   70   98  350  490
[0, 0] p( 2451):     3   57  129
[0, 0] p( 2452):     4
[0, 0] p( 2453):     1   11  223
[0, 0] p( 2454):     6
[0, 0] p( 2455):     1    5  491
[0, 0] p( 2456):     8
[0, 0] p( 2457):    27  189  351
[0, 0] p( 2458):     2
[0, 0] p( 2459):     1
[0, 0] p( 2460):    12   60  492
[0, 0] p( 2461):     1   23  107
[0, 0] p( 2462):     2
[0, 0] p( 2463):     3
[0, 0] p( 2464):    32  224  352
[0, 0] p( 2465):     1    5   17   29   85  145  493
[0, 0] p( 2466):    18
[0, 0] p( 2467):     1
[0, 0] p( 2468):     4
[0, 0] p( 2469):     3
[0, 0] p( 2470):     2   10   26   38  130  190  494
[0, 0] p( 2471):     1    7  353
[0, 0] p( 2472):    24
[0, 0] p( 2473):     1
[0, 0] p( 2474):     2
[0, 0] p( 2475):     9   45   99  225  495
[0, 0] p( 2476):     4
[0, 0] p( 2477):     1
[0, 0] p( 2478):     6   42  354
[0, 0] p( 2479):     1   37   67
[0, 0] p( 2480):    16   80  496
[0, 0] p( 2481):     3
[0, 0] p( 2482):     2   34  146
[0, 0] p( 2483):     1   13  191
[0, 0] p( 2484):   108
[0, 0] p( 2485):     1    5    7   35   71  355  497
[0, 0] p( 2486):     2   22  226
[0, 0] p( 2487):     3
[0, 0] p( 2488):     8
[0, 0] p( 2489):     1   19  131
[0, 0] p( 2490):     6   30  498
[0, 0] p( 2491):     1   47   53
[0, 0] p( 2492):     4   28  356
[0, 0] p( 2493):     9
[0, 0] p( 2494):     2   58   86
[0, 0] p( 2495):     1    5  499
[0, 0] p( 2496):   192
[0, 0] p( 2497):     1   11  227
[0, 0] p( 2498):     2
[0, 0] p( 2499):     3   21   51  147  357
[0, 0] p( 2500):     4   20  100  500
[0, 0] p( 2501):     1   41   61
[0, 0] p( 2502):    18
[0, 0] p( 2503):     1
[0, 0] p( 2504):     8
[0, 0] p( 2505):     3   15  501
[0, 0] p( 2506):     2   14  358
[0, 0] p( 2507):     1   23  109
[0, 0] p( 2508):    12  132  228
[0, 0] p( 2509):     1   13  193
[0, 0] p( 2510):     2   10  502
[0, 0] p( 2511):    81
[0, 0] p( 2512):    16
[0, 0] p( 2513):     1    7  359
[0, 0] p( 2514):     6
[0, 0] p( 2515):     1    5  503
[0, 0] p( 2516):     4   68  148
[0, 0] p( 2517):     3
[0, 0] p( 2518):     2
[0, 0] p( 2519):     1   11  229
[0, 0] p( 2520):    72  360  504
[0, 0] p( 2521):     1
[0, 0] p( 2522):     2   26  194
[0, 0] p( 2523):     3   87
[0, 0] p( 2524):     4
[0, 0] p( 2525):     1    5   25  101  505
[0, 0] p( 2526):     6
[0, 0] p( 2527):     1    7   19  133  361
[0, 0] p( 2528):    32
[0, 0] p( 2529):     9
[0, 0] p( 2530):     2   10   22   46  110  230  506
[0, 0] p( 2531):     1
[0, 0] p( 2532):    12
[0, 0] p( 2533):     1   17  149
[0, 0] p( 2534):     2   14  362
[0, 0] p( 2535):     3   15   39  195  507
[0, 0] p( 2536):     8
[0, 0] p( 2537):     1   43   59
[0, 0] p( 2538):    54
[0, 0] p( 2539):     1
[0, 0] p( 2540):     4   20  508
[0, 0] p( 2541):     3   21   33  231  363
[0, 0] p( 2542):     2   62   82
[0, 0] p( 2543):     1
[0, 0] p( 2544):    48
[0, 0] p( 2545):     1    5  509
[0, 0] p( 2546):     2   38  134
[0, 0] p( 2547):     9
[0, 0] p( 2548):     4   28   52  196  364
[0, 0] p( 2549):     1
[0, 0] p( 2550):     6   30  102  150  510
[0, 0] p( 2551):     1
[0, 0] p( 2552):     8   88  232
[0, 0] p( 2553):     3   69  111
[0, 0] p( 2554):     2
[0, 0] p( 2555):     1    5    7   35   73  365  511
[0, 0] p( 2556):    36
[0, 0] p( 2557):     1
[0, 0] p( 2558):     2
[0, 0] p( 2559):     3
[0, 0] p( 2560):   512
[0, 0] p( 2561):     1   13  197
[0, 0] p( 2562):     6   42  366
[0, 0] p( 2563):     1   11  233
[0, 0] p( 2564):     4
[0, 0] p( 2565):    27  135  513
[0, 0] p( 2566):     2
[0, 0] p( 2567):     1   17  151
[0, 0] p( 2568):    24
[0, 0] p( 2569):     1    7  367
[0, 0] p( 2570):     2   10  514
[0, 0] p( 2571):     3
[0, 0] p( 2572):     4
[0, 0] p( 2573):     1   31   83
[0, 0] p( 2574):    18  198  234
[0, 0] p( 2575):     1    5   25  103  515
[0, 0] p( 2576):    16  112  368
[0, 0] p( 2577):     3
[0, 0] p( 2578):     2
[0, 0] p( 2579):     1
[0, 0] p( 2580):    12   60  516
[0, 0] p( 2581):     1   29   89
[0, 0] p( 2582):     2
[0, 0] p( 2583):     9   63  369
[0, 0] p( 2584):     8  136  152
[0, 0] p( 2585):     1    5   11   47   55  235  517
[0, 0] p( 2586):     6
[0, 0] p( 2587):     1   13  199
[0, 0] p( 2588):     4
[0, 0] p( 2589):     3
[0, 0] p( 2590):     2   10   14   70   74  370  518
[0, 0] p( 2591):     1
[0, 0] p( 2592): 
[0, 0] p( 2593):     1
[0, 0] p( 2594):     2
[0, 0] p( 2595):     3   15  519
[0, 0] p( 2596):     4   44  236
[0, 0] p( 2597):     1    7   49   53  371
[0, 0] p( 2598):     6
[0, 0] p( 2599):     1   23  113
[0, 0] p( 2600):     8   40  104  200  520
[0, 0] p( 2601):     9  153
[0, 0] p( 2602):     2
[0, 0] p( 2603):     1   19  137
[0, 0] p( 2604):    12   84  372
[0, 0] p( 2605):     1    5  521
[0, 0] p( 2606):     2
[0, 0] p( 2607):     3   33  237
[0, 0] p( 2608):    16
[0, 0] p( 2609):     1
[0, 0] p( 2610):    18   90  522
[0, 0] p( 2611):     1    7  373
[0, 0] p( 2612):     4
[0, 0] p( 2613):     3   39  201
[0, 0] p( 2614):     2
[0, 0] p( 2615):     1    5  523
[0, 0] p( 2616):    24
[0, 0] p( 2617):     1
[0, 0] p( 2618):     2   14   22   34  154  238  374
[0, 0] p( 2619):    27
[0, 0] p( 2620):     4   20  524
[0, 0] p( 2621):     1
[0, 0] p( 2622):     6  114  138
[0, 0] p( 2623):     1   43   61
[0, 0] p( 2624):    64
[0, 0] p( 2625):     3   15   21   75  105  375  525
[0, 0] p( 2626):     2   26  202
[0, 0] p( 2627):     1   37   71
[0, 0] p( 2628):    36
[0, 0] p( 2629):     1   11  239
[0, 0] p( 2630):     2   10  526
[0, 0] p( 2631):     3
[0, 0] p( 2632):     8   56  376
[0, 0] p( 2633):     1
[0, 0] p( 2634):     6
[0, 0] p( 2635):     1    5   17   31   85  155  527
[0, 0] p( 2636):     4
[0, 0] p( 2637):     9
[0, 0] p( 2638):     2
[0, 0] p( 2639):     1    7   13   29   91  203  377
[0, 0] p( 2640):    48  240  528
[0, 0] p( 2641):     1   19  139
[0, 0] p( 2642):     2
[0, 0] p( 2643):     3
[0, 0] p( 2644):     4
[0, 0] p( 2645):     1    5   23  115  529
[0, 0] p( 2646):    54  378
[0, 0] p( 2647):     1
[0, 0] p( 2648):     8
[0, 0] p( 2649):     3
[0, 0] p( 2650):     2   10   50  106  530
[0, 0] p( 2651):     1   11  241
[0, 0] p( 2652):    12  156  204
[0, 0] p( 2653):     1    7  379
[0, 0] p( 2654):     2
[0, 0] p( 2655):     9   45  531
[0, 0] p( 2656):    32
[0, 0] p( 2657):     1
[0, 0] p( 2658):     6
[0, 0] p( 2659):     1
[0, 0] p( 2660):     4   20   28   76  140  380  532
[0, 0] p( 2661):     3
[0, 0] p( 2662):     2   22  242
[0, 0] p( 2663):     1
[0, 0] p( 2664):    72
[0, 0] p( 2665):     1    5   13   41   65  205  533
[0, 0] p( 2666):     2   62   86
[0, 0] p( 2667):     3   21  381
[0, 0] p( 2668):     4   92  116
[0, 0] p( 2669):     1   17  157
[0, 0] p( 2670):     6   30  534
[0, 0] p( 2671):     1
[0, 0] p( 2672):    16
[0, 0] p( 2673):   243
[0, 0] p( 2674):     2   14  382
[0, 0] p( 2675):     1    5   25  107  535
[0, 0] p( 2676):    12
[0, 0] p( 2677):     1
[0, 0] p( 2678):     2   26  206
[0, 0] p( 2679):     3   57  141
[0, 0] p( 2680):     8   40  536
[0, 0] p( 2681):     1    7  383
[0, 0] p( 2682):    18
[0, 0] p( 2683):     1
[0, 0] p( 2684):     4   44  244
[0, 0] p( 2685):     3   15  537
[0, 0] p( 2686):     2   34  158
[0, 0] p( 2687):     1
[0, 0] p( 2688):   384
[0, 0] p( 2689):     1
[0, 0] p( 2690):     2   10  538
[0, 0] p( 2691):     9  117  207
[0, 0] p( 2692):     4
[0, 0] p( 2693):     1
[0, 0] p( 2694):     6
[0, 0] p( 2695):     1    5    7   11   35   49   55   77  245  385  539
[0, 0] p( 2696):     8
[0, 0] p( 2697):     3   87   93
[0, 0] p( 2698):     2   38  142
[0, 0] p( 2699):     1
[0, 0] p( 2700):   108  540
[0, 0] p( 2701):     1   37   73
[0, 0] p( 2702):     2   14  386
[0, 0] p( 2703):     3   51  159
[0, 0] p( 2704):    16  208
[0, 0] p( 2705):     1    5  541
[0, 0] p( 2706):     6   66  246
[0, 0] p( 2707):     1
[0, 0] p( 2708):     4
[0, 0] p( 2709):     9   63  387
[0, 0] p( 2710):     2   10  542
[0, 0] p( 2711):     1
[0, 0] p( 2712):    24
[0, 0] p( 2713):     1
[0, 0] p( 2714):     2   46  118
[0, 0] p( 2715):     3   15  543
[0, 0] p( 2716):     4   28  388
[0, 0] p( 2717):     1   11   13   19  143  209  247
[0, 0] p( 2718):    18
[0, 0] p( 2719):     1
[0, 0] p( 2720):    32  160  544
[0, 0] p( 2721):     3
[0, 0] p( 2722):     2
[0, 0] p( 2723):     1    7  389
[0, 0] p( 2724):    12
[0, 0] p( 2725):     1    5   25  109  545
[0, 0] p( 2726):     2   58   94
[0, 0] p( 2727):    27
[0, 0] p( 2728):     8   88  248
[0, 0] p( 2729):     1
[0, 0] p( 2730):     6   30   42   78  210  390  546
[0, 0] p( 2731):     1
[0, 0] p( 2732):     4
[0, 0] p( 2733):     3
[0, 0] p( 2734):     2
[0, 0] p( 2735):     1    5  547
[0, 0] p( 2736):   144
[0, 0] p( 2737):     1    7   17   23  119  161  391
[0, 0] p( 2738):     2   74
[0, 0] p( 2739):     3   33  249
[0, 0] p( 2740):     4   20  548
[0, 0] p( 2741):     1
[0, 0] p( 2742):     6
[0, 0] p( 2743):     1   13  211
[0, 0] p( 2744):     8   56  392
[0, 0] p( 2745):     9   45  549
[0, 0] p( 2746):     2
[0, 0] p( 2747):     1   41   67
[0, 0] p( 2748):    12
[0, 0] p( 2749):     1
[0, 0] p( 2750):     2   10   22   50  110  250  550
[0, 0] p( 2751):     3   21  393
[0, 0] p( 2752):    64
[0, 0] p( 2753):     1
[0, 0] p( 2754):   162
[0, 0] p( 2755):     1    5   19   29   95  145  551
[0, 0] p( 2756):     4   52  212
[0, 0] p( 2757):     3
[0, 0] p( 2758):     2   14  394
[0, 0] p( 2759):     1   31   89
[0, 0] p( 2760):    24  120  552
[0, 0] p( 2761):     1   11  251
[0, 0] p( 2762):     2
[0, 0] p( 2763):     9
[0, 0] p( 2764):     4
[0, 0] p( 2765):     1    5    7   35   79  395  553
[0, 0] p( 2766):     6
[0, 0] p( 2767):     1
[0, 0] p( 2768):    16
[0, 0] p( 2769):     3   39  213
[0, 0] p( 2770):     2   10  554
[0, 0] p( 2771):     1   17  163
[0, 0] p( 2772):    36  252  396
[0, 0] p( 2773):     1   47   59
[0, 0] p( 2774):     2   38  146
[0, 0] p( 2775):     3   15   75  111  555
[0, 0] p( 2776):     8
[0, 0] p( 2777):     1
[0, 0] p( 2778):     6
[0, 0] p( 2779):     1    7  397
[0, 0] p( 2780):     4   20  556
[0, 0] p( 2781):    27
[0, 0] p( 2782):     2   26  214
[0, 0] p( 2783):     1   11   23  121  253
[0, 0] p( 2784):    96
[0, 0] p( 2785):     1    5  557
[0, 0] p( 2786):     2   14  398
[0, 0] p( 2787):     3
[0, 0] p( 2788):     4   68  164
[0, 0] p( 2789):     1
[0, 0] p( 2790):    18   90  558
[0, 0] p( 2791):     1
[0, 0] p( 2792):     8
[0, 0] p( 2793):     3   21   57  147  399
[0, 0] p( 2794):     2   22  254
[0, 0] p( 2795):     1    5   13   43   65  215  559
[0, 0] p( 2796):    12
[0, 0] p( 2797):     1
[0, 0] p( 2798):     2
[0, 0] p( 2799):     9
[0, 0] p( 2800):    16   80  112  400  560
[0, 0] p( 2801):     1
[0, 0] p( 2802):     6
[0, 0] p( 2803):     1
[0, 0] p( 2804):     4
[0, 0] p( 2805):     3   15   33   51  165  255  561
[0, 0] p( 2806):     2   46  122
[0, 0] p( 2807):     1    7  401
[0, 0] p( 2808):   216
[0, 0] p( 2809):     1   53
[0, 0] p( 2810):     2   10  562
[0, 0] p( 2811):     3
[0, 0] p( 2812):     4   76  148
[0, 0] p( 2813):     1   29   97
[0, 0] p( 2814):     6   42  402
[0, 0] p( 2815):     1    5  563
[0, 0] p( 2816):   256
[0, 0] p( 2817):     9
[0, 0] p( 2818):     2
[0, 0] p( 2819):     1
[0, 0] p( 2820):    12   60  564
[0, 0] p( 2821):     1    7   13   31   91  217  403
[0, 0] p( 2822):     2   34  166
[0, 0] p( 2823):     3
[0, 0] p( 2824):     8
[0, 0] p( 2825):     1    5   25  113  565
[0, 0] p( 2826):    18
[0, 0] p( 2827):     1   11  257
[0, 0] p( 2828):     4   28  404
[0, 0] p( 2829):     3   69  123
[0, 0] p( 2830):     2   10  566
[0, 0] p( 2831):     1   19  149
[0, 0] p( 2832):    48
[0, 0] p( 2833):     1
[0, 0] p( 2834):     2   26  218
[0, 0] p( 2835):    81  405  567
[0, 0] p( 2836):     4
[0, 0] p( 2837):     1
[0, 0] p( 2838):     6   66  258
[0, 0] p( 2839):     1   17  167
[0, 0] p( 2840):     8   40  568
[0, 0] p( 2841):     3
[0, 0] p( 2842):     2   14   58   98  406
[0, 0] p( 2843):     1
[0, 0] p( 2844):    36
[0, 0] p( 2845):     1    5  569
[0, 0] p( 2846):     2
[0, 0] p( 2847):     3   39  219
[0, 0] p( 2848):    32
[0, 0] p( 2849):     1    7   11   37   77  259  407
[0, 0] p( 2850):     6   30  114  150  570
[0, 0] p( 2851):     1
[0, 0] p( 2852):     4   92  124
[0, 0] p( 2853):     9
[0, 0] p( 2854):     2
[0, 0] p( 2855):     1    5  571
[0, 0] p( 2856):    24  168  408
[0, 0] p( 2857):     1
[0, 0] p( 2858):     2
[0, 0] p( 2859):     3
[0, 0] p( 2860):     4   20   44   52  220  260  572
[0, 0] p( 2861):     1
[0, 0] p( 2862):    54
[0, 0] p( 2863):     1    7  409
[0, 0] p( 2864):    16
[0, 0] p( 2865):     3   15  573
[0, 0] p( 2866):     2
[0, 0] p( 2867):     1   47   61
[0, 0] p( 2868):    12
[0, 0] p( 2869):     1   19  151
[0, 0] p( 2870):     2   10   14   70   82  410  574
[0, 0] p( 2871):     9   99  261
[0, 0] p( 2872):     8
[0, 0] p( 2873):     1   13   17  169  221
[0, 0] p( 2874):     6
[0, 0] p( 2875):     1    5   23   25  115  125  575
[0, 0] p( 2876):     4
[0, 0] p( 2877):     3   21  411
[0, 0] p( 2878):     2
[0, 0] p( 2879):     1
[0, 0] p( 2880):   576
[0, 0] p( 2881):     1   43   67
[0, 0] p( 2882):     2   22  262
[0, 0] p( 2883):     3   93
[0, 0] p( 2884):     4   28  412
[0, 0] p( 2885):     1    5  577
[0, 0] p( 2886):     6   78  222
[0, 0] p( 2887):     1
[0, 0] p( 2888):     8  152
[0, 0] p( 2889):    27
[0, 0] p( 2890):     2   10   34  170  578
[0, 0] p( 2891):     1    7   49   59  413
[0, 0] p( 2892):    12
[0, 0] p( 2893):     1   11  263
[0, 0] p( 2894):     2
[0, 0] p( 2895):     3   15  579
[0, 0] p( 2896):    16
[0, 0] p( 2897):     1
[0, 0] p( 2898):    18  126  414
[0, 0] p( 2899):     1   13  223
[0, 0] p( 2900):     4   20  100  116  580
[0, 0] p( 2901):     3
[0, 0] p( 2902):     2
[0, 0] p( 2903):     1
[0, 0] p( 2904):    24  264
[0, 0] p( 2905):     1    5    7   35   83  415  581
[0, 0] p( 2906):     2
[0, 0] p( 2907):     9  153  171
[0, 0] p( 2908):     4
[0, 0] p( 2909):     1
[0, 0] p( 2910):     6   30  582
[0, 0] p( 2911):     1   41   71
[0, 0] p( 2912):    32  224  416
[0, 0] p( 2913):     3
[0, 0] p( 2914):     2   62   94
[0, 0] p( 2915):     1    5   11   53   55  265  583
[0, 0] p( 2916): 
[0, 0] p( 2917):     1
[0, 0] p( 2918):     2
[0, 0] p( 2919):     3   21  417
[0, 0] p( 2920):     8   40  584
[0, 0] p( 2921):     1   23  127
[0, 0] p( 2922):     6
[0, 0] p( 2923):     1   37   79
[0, 0] p( 2924):     4   68  172
[0, 0] p( 2925):     9   45  117  225  585
[0, 0] p( 2926):     2   14   22   38  154  266  418
[0, 0] p( 2927):     1
[0, 0] p( 2928):    48
[0, 0] p( 2929):     1   29  101
[0, 0] p( 2930):     2   10  586
[0, 0] p( 2931):     3
[0, 0] p( 2932):     4
[0, 0] p( 2933):     1    7  419
[0, 0] p( 2934):    18
[0, 0] p( 2935):     1    5  587
[0, 0] p( 2936):     8
[0, 0] p( 2937):     3   33  267
[0, 0] p( 2938):     2   26  226
[0, 0] p( 2939):     1
[0, 0] p( 2940):    12   60   84  420  588
[0, 0] p( 2941):     1   17  173
[0, 0] p( 2942):     2
[0, 0] p( 2943):    27
[0, 0] p( 2944):   128
[0, 0] p( 2945):     1    5   19   31   95  155  589
[0, 0] p( 2946):     6
[0, 0] p( 2947):     1    7  421
[0, 0] p( 2948):     4   44  268
[0, 0] p( 2949):     3
[0, 0] p( 2950):     2   10   50  118  590
[0, 0] p( 2951):     1   13  227
[0, 0] p( 2952):    72
[0, 0] p( 2953):     1
[0, 0] p( 2954):     2   14  422
[0, 0] p( 2955):     3   15  591
[0, 0] p( 2956):     4
[0, 0] p( 2957):     1
[0, 0] p( 2958):     6  102  174
[0, 0] p( 2959):     1   11  269
[0, 0] p( 2960):    16   80  592
[0, 0] p( 2961):     9   63  423
[0, 0] p( 2962):     2
[0, 0] p( 2963):     1
[0, 0] p( 2964):    12  156  228
[0, 0] p( 2965):     1    5  593
[0, 0] p( 2966):     2
[0, 0] p( 2967):     3   69  129
[0, 0] p( 2968):     8   56  424
[0, 0] p( 2969):     1
[0, 0] p( 2970):    54  270  594
[0, 0] p( 2971):     1
[0, 0] p( 2972):     4
[0, 0] p( 2973):     3
[0, 0] p( 2974):     2
[0, 0] p( 2975):     1    5    7   17   25   35   85  119  175  425  595
[0, 0] p( 2976):    96
[0, 0] p( 2977):     1   13  229
[0, 0] p( 2978):     2
[0, 0] p( 2979):     9
[0, 0] p( 2980):     4   20  596
[0, 0] p( 2981):     1   11  271
[0, 0] p( 2982):     6   42  426
[0, 0] p( 2983):     1   19  157
[0, 0] p( 2984):     8
[0, 0] p( 2985):     3   15  597
[0, 0] p( 2986):     2
[0, 0] p( 2987):     1   29  103
[0, 0] p( 2988):    36
[0, 0] p( 2989):     1    7   49   61  427
[0, 0] p( 2990):     2   10   26   46  130  230  598
[0, 0] p( 2991):     3
[0, 0] p( 2992):    16  176  272
[0, 0] p( 2993):     1   41   73
[0, 0] p( 2994):     6
[0, 0] p( 2995):     1    5  599
[0, 0] p( 2996):     4   28  428
[0, 0] p( 2997):    81
[0, 0] p( 2998):     2
[0, 0] p( 2999):     1
[0, 0] p( 3000):    24  120  600
[0, 0] p( 3001):     1
[0, 0] p( 3002):     2   38  158
[0, 0] p( 3003):     3   21   33   39  231  273  429
[0, 0] p( 3004):     4
[0, 0] p( 3005):     1    5  601
[0, 0] p( 3006):    18
[0, 0] p( 3007):     1   31   97
[0, 0] p( 3008):    64
[0, 0] p( 3009):     3   51  177
[0, 0] p( 3010):     2   10   14   70   86  430  602
[0, 0] p( 3011):     1
[0, 0] p( 3012):    12
[0, 0] p( 3013):     1   23  131
[0, 0] p( 3014):     2   22  274
[0, 0] p( 3015):     9   45  603
[0, 0] p( 3016):     8  104  232
[0, 0] p( 3017):     1    7  431
[0, 0] p( 3018):     6
[0, 0] p( 3019):     1
[0, 0] p( 3020):     4   20  604
[0, 0] p( 3021):     3   57  159
[0, 0] p( 3022):     2
[0, 0] p( 3023):     1
[0, 0] p( 3024):   432
[0, 0] p( 3025):     1    5   11   25   55  121  275  605
[0, 0] p( 3026):     2   34  178
[0, 0] p( 3027):     3
[0, 0] p( 3028):     4
[0, 0] p( 3029):     1   13  233
[0, 0] p( 3030):     6   30  606
[0, 0] p( 3031):     1    7  433
[0, 0] p( 3032):     8
[0, 0] p( 3033):     9
[0, 0] p( 3034):     2   74   82
[0, 0] p( 3035):     1    5  607
[0, 0] p( 3036):    12  132  276
[0, 0] p( 3037):     1
[0, 0] p( 3038):     2   14   62   98  434
[0, 0] p( 3039):     3
[0, 0] p( 3040):    32  160  608
[0, 0] p( 3041):     1
[0, 0] p( 3042):    18  234
[0, 0] p( 3043):     1   17  179
[0, 0] p( 3044):     4
[0, 0] p( 3045):     3   15   21   87  105  435  609
[0, 0] p( 3046):     2
[0, 0] p( 3047):     1   11  277
[0, 0] p( 3048):    24
[0, 0] p( 3049):     1
[0, 0] p( 3050):     2   10   50  122  610
[0, 0] p( 3051):    27
[0, 0] p( 3052):     4   28  436
[0, 0] p( 3053):     1   43   71
[0, 0] p( 3054):     6
[0, 0] p( 3055):     1    5   13   47   65  235  611
[0, 0] p( 3056):    16
[0, 0] p( 3057):     3
[0, 0] p( 3058):     2   22  278
[0, 0] p( 3059):     1    7   19   23  133  161  437
[0, 0] p( 3060):    36  180  612
[0, 0] p( 3061):     1
[0, 0] p( 3062):     2
[0, 0] p( 3063):     3
[0, 0] p( 3064):     8
[0, 0] p( 3065):     1    5  613
[0, 0] p( 3066):     6   42  438
[0, 0] p( 3067):     1
[0, 0] p( 3068):     4   52  236
[0, 0] p( 3069):     9   99  279
[0, 0] p( 3070):     2   10  614
[0, 0] p( 3071):     1   37   83
[0, 0] p( 3072): 
[0, 0] p( 3073):     1    7  439
[0, 0] p( 3074):     2   58  106
[0, 0] p( 3075):     3   15   75  123  615
[0, 0] p( 3076):     4
[0, 0] p( 3077):     1   17  181
[0, 0] p( 3078):   162
[0, 0] p( 3079):     1
[0, 0] p( 3080):     8   40   56   88  280  440  616
[0, 0] p( 3081):     3   39  237
[0, 0] p( 3082):     2   46  134
[0, 0] p( 3083):     1
[0, 0] p( 3084):    12
[0, 0] p( 3085):     1    5  617
[0, 0] p( 3086):     2
[0, 0] p( 3087):     9   63  441
[0, 0] p( 3088):    16
[0, 0] p( 3089):     1
[0, 0] p( 3090):     6   30  618
[0, 0] p( 3091):     1   11  281
[0, 0] p( 3092):     4
[0, 0] p( 3093):     3
[0, 0] p( 3094):     2   14   26   34  182  238  442
[0, 0] p( 3095):     1    5  619
[0, 0] p( 3096):    72
[0, 0] p( 3097):     1   19  163
[0, 0] p( 3098):     2
[0, 0] p( 3099):     3
[0, 0] p( 3100):     4   20  100  124  620
[0, 0] p( 3101):     1    7  443
[0, 0] p( 3102):     6   66  282
[0, 0] p( 3103):     1   29  107
[0, 0] p( 3104):    32
[0, 0] p( 3105):    27  135  621
[0, 0] p( 3106):     2
[0, 0] p( 3107):     1   13  239
[0, 0] p( 3108):    12   84  444
[0, 0] p( 3109):     1
[0, 0] p( 3110):     2   10  622
[0, 0] p( 3111):     3   51  183
[0, 0] p( 3112):     8
[0, 0] p( 3113):     1   11  283
[0, 0] p( 3114):    18
[0, 0] p( 3115):     1    5    7   35   89  445  623
[0, 0] p( 3116):     4   76  164
[0, 0] p( 3117):     3
[0, 0] p( 3118):     2
[0, 0] p( 3119):     1
[0, 0] p( 3120):    48  240  624
[0, 0] p( 3121):     1
[0, 0] p( 3122):     2   14  446
[0, 0] p( 3123):     9
[0, 0] p( 3124):     4   44  284
[0, 0] p( 3125):     1    5   25  125  625
[0, 0] p( 3126):     6
[0, 0] p( 3127):     1   53   59
[0, 0] p( 3128):     8  136  184
[0, 0] p( 3129):     3   21  447
[0, 0] p( 3130):     2   10  626
[0, 0] p( 3131):     1   31  101
[0, 0] p( 3132):   108
[0, 0] p( 3133):     1   13  241
[0, 0] p( 3134):     2
[0, 0] p( 3135):     3   15   33   57  165  285  627
[0, 0] p( 3136):    64  448
[0, 0] p( 3137):     1
[0, 0] p( 3138):     6
[0, 0] p( 3139):     1   43   73
[0, 0] p( 3140):     4   20  628
[0, 0] p( 3141):     9
[0, 0] p( 3142):     2
[0, 0] p( 3143):     1    7  449
[0, 0] p( 3144):    24
[0, 0] p( 3145):     1    5   17   37   85  185  629
[0, 0] p( 3146):     2   22   26  242  286
[0, 0] p( 3147):     3
[0, 0] p( 3148):     4
[0, 0] p( 3149):     1   47   67
[0, 0] p( 3150):    18   90  126  450  630
[0, 0] p( 3151):     1   23  137
[0, 0] p( 3152):    16
[0, 0] p( 3153):     3
[0, 0] p( 3154):     2   38  166
[0, 0] p( 3155):     1    5  631
[0, 0] p( 3156):    12
[0, 0] p( 3157):     1    7   11   41   77  287  451
[0, 0] p( 3158):     2
[0, 0] p( 3159):   243
[0, 0] p( 3160):     8   40  632
[0, 0] p( 3161):     1   29  109
[0, 0] p( 3162):     6  102  186
[0, 0] p( 3163):     1
[0, 0] p( 3164):     4   28  452
[0, 0] p( 3165):     3   15  633
[0, 0] p( 3166):     2
[0, 0] p( 3167):     1
[0, 0] p( 3168):   288
[0, 0] p( 3169):     1
[0, 0] p( 3170):     2   10  634
[0, 0] p( 3171):     3   21  453
[0, 0] p( 3172):     4   52  244
[0, 0] p( 3173):     1   19  167
[0, 0] p( 3174):     6  138
[0, 0] p( 3175):     1    5   25  127  635
[0, 0] p( 3176):     8
[0, 0] p( 3177):     9
[0, 0] p( 3178):     2   14  454
[0, 0] p( 3179):     1   11   17  187  289
[0, 0] p( 3180):    12   60  636
[0, 0] p( 3181):     1
[0, 0] p( 3182):     2   74   86
[0, 0] p( 3183):     3
[0, 0] p( 3184):    16
[0, 0] p( 3185):     1    5    7   13   35   49   65   91  245  455  637
[0, 0] p( 3186):    54
[0, 0] p( 3187):     1
[0, 0] p( 3188):     4
[0, 0] p( 3189):     3
[0, 0] p( 3190):     2   10   22   58  110  290  638
[0, 0] p( 3191):     1
[0, 0] p( 3192):    24  168  456
[0, 0] p( 3193):     1   31  103
[0, 0] p( 3194):     2
[0, 0] p( 3195):     9   45  639
[0, 0] p( 3196):     4   68  188
[0, 0] p( 3197):     1   23  139
[0, 0] p( 3198):     6   78  246
[0, 0] p( 3199):     1    7  457
[0, 0] p( 3200):   128  640
[0, 0] p( 3201):     3   33  291
[0, 0] p( 3202):     2
[0, 0] p( 3203):     1
[0, 0] p( 3204):    36
[0, 0] p( 3205):     1    5  641
[0, 0] p( 3206):     2   14  458
[0, 0] p( 3207):     3
[0, 0] p( 3208):     8
[0, 0] p( 3209):     1
[0, 0] p( 3210):     6   30  642
[0, 0] p( 3211):     1   13   19  169  247
[0, 0] p( 3212):     4   44  292
[0, 0] p( 3213):    27  189  459
[0, 0] p( 3214):     2
[0, 0] p( 3215):     1    5  643
[0, 0] p( 3216):    48
[0, 0] p( 3217):     1
[0, 0] p( 3218):     2
[0, 0] p( 3219):     3   87  111
[0, 0] p( 3220):     4   20   28   92  140  460  644
[0, 0] p( 3221):     1
[0, 0] p( 3222):    18
[0, 0] p( 3223):     1   11  293
[0, 0] p( 3224):     8  104  248
[0, 0] p( 3225):     3   15   75  129  645
[0, 0] p( 3226):     2
[0, 0] p( 3227):     1    7  461
[0, 0] p( 3228):    12
[0, 0] p( 3229):     1
[0, 0] p( 3230):     2   10   34   38  170  190  646
[0, 0] p( 3231):     9
[0, 0] p( 3232):    32
[0, 0] p( 3233):     1   53   61
[0, 0] p( 3234):     6   42   66  294  462
[0, 0] p( 3235):     1    5  647
[0, 0] p( 3236):     4
[0, 0] p( 3237):     3   39  249
[0, 0] p( 3238):     2
[0, 0] p( 3239):     1   41   79
[0, 0] p( 3240):   648
[0, 0] p( 3241):     1    7  463
[0, 0] p( 3242):     2
[0, 0] p( 3243):     3   69  141
[0, 0] p( 3244):     4
[0, 0] p( 3245):     1    5   11   55   59  295  649
[0, 0] p( 3246):     6
[0, 0] p( 3247):     1   17  191
[0, 0] p( 3248):    16  112  464
[0, 0] p( 3249):     9  171
[0, 0] p( 3250):     2   10   26   50  130  250  650
[0, 0] p( 3251):     1
[0, 0] p( 3252):    12
[0, 0] p( 3253):     1
[0, 0] p( 3254):     2
[0, 0] p( 3255):     3   15   21   93  105  465  651
[0, 0] p( 3256):     8   88  296
[0, 0] p( 3257):     1
[0, 0] p( 3258):    18
[0, 0] p( 3259):     1
[0, 0] p( 3260):     4   20  652
[0, 0] p( 3261):     3
[0, 0] p( 3262):     2   14  466
[0, 0] p( 3263):     1   13  251
[0, 0] p( 3264):   192
[0, 0] p( 3265):     1    5  653
[0, 0] p( 3266):     2   46  142
[0, 0] p( 3267):    27  297
[0, 0] p( 3268):     4   76  172
[0, 0] p( 3269):     1    7  467
[0, 0] p( 3270):     6   30  654
[0, 0] p( 3271):     1
[0, 0] p( 3272):     8
[0, 0] p( 3273):     3
[0, 0] p( 3274):     2
[0, 0] p( 3275):     1    5   25  131  655
[0, 0] p( 3276):    36  252  468
[0, 0] p( 3277):     1   29  113
[0, 0] p( 3278):     2   22  298
[0, 0] p( 3279):     3
[0, 0] p( 3280):    16   80  656
[0, 0] p( 3281):     1   17  193
[0, 0] p( 3282):     6
[0, 0] p( 3283):     1    7   49   67  469
[0, 0] p( 3284):     4
[0, 0] p( 3285):     9   45  657
[0, 0] p( 3286):     2   62  106
[0, 0] p( 3287):     1   19  173
[0, 0] p( 3288):    24
[0, 0] p( 3289):     1   11   13   23  143  253  299
[0, 0] p( 3290):     2   10   14   70   94  470  658
[0, 0] p( 3291):     3
[0, 0] p( 3292):     4
[0, 0] p( 3293):     1   37   89
[0, 0] p( 3294):    54
[0, 0] p( 3295):     1    5  659
[0, 0] p( 3296):    32
[0, 0] p( 3297):     3   21  471
[0, 0] p( 3298):     2   34  194
[0, 0] p( 3299):     1
[0, 0] p( 3300):    12   60  132  300  660
[0, 0] p( 3301):     1
[0, 0] p( 3302):     2   26  254
[0, 0] p( 3303):     9
[0, 0] p( 3304):     8   56  472
[0, 0] p( 3305):     1    5  661
[0, 0] p( 3306):     6  114  174
[0, 0] p( 3307):     1
[0, 0] p( 3308):     4
[0, 0] p( 3309):     3
[0, 0] p( 3310):     2   10  662
[0, 0] p( 3311):     1    7   11   43   77  301  473
[0, 0] p( 3312):   144
[0, 0] p( 3313):     1
[0, 0] p( 3314):     2
[0, 0] p( 3315):     3   15   39   51  195  255  663
[0, 0] p( 3316):     4
[0, 0] p( 3317):     1   31  107
[0, 0] p( 3318):     6   42  474
[0, 0] p( 3319):     1
[0, 0] p( 3320):     8   40  664
[0, 0] p( 3321):    81
[0, 0] p( 3322):     2   22  302
[0, 0] p( 3323):     1
[0, 0] p( 3324):    12
[0, 0] p( 3325):     1    5    7   19   25   35   95  133  175  475  665
[0, 0] p( 3326):     2
[0, 0] p( 3327):     3
[0, 0] p( 3328):   256
[0, 0] p( 3329):     1
[0, 0] p( 3330):    18   90  666
[0, 0] p( 3331):     1
[0, 0] p( 3332):     4   28   68  196  476
[0, 0] p( 3333):     3   33  303
[0, 0] p( 3334):     2
[0, 0] p( 3335):     1    5   23   29  115  145  667
[0, 0] p( 3336):    24
[0, 0] p( 3337):     1   47   71
[0, 0] p( 3338):     2
[0, 0] p( 3339):     9   63  477
[0, 0] p( 3340):     4   20  668
[0, 0] p( 3341):     1   13  257
[0, 0] p( 3342):     6
[0, 0] p( 3343):     1
[0, 0] p( 3344):    16  176  304
[0, 0] p( 3345):     3   15  669
[0, 0] p( 3346):     2   14  478
[0, 0] p( 3347):     1
[0, 0] p( 3348):   108
[0, 0] p( 3349):     1   17  197
[0, 0] p( 3350):     2   10   50  134  670
[0, 0] p( 3351):     3
[0, 0] p( 3352):     8
[0, 0] p( 3353):     1    7  479
[0, 0] p( 3354):     6   78  258
[0, 0] p( 3355):     1    5   11   55   61  305  671
[0, 0] p( 3356):     4
[0, 0] p( 3357):     9
[0, 0] p( 3358):     2   46  146
[0, 0] p( 3359):     1
[0, 0] p( 3360):    96  480  672
[0, 0] p( 3361):     1
[0, 0] p( 3362):     2   82
[0, 0] p( 3363):     3   57  177
[0, 0] p( 3364):     4  116
[0, 0] p( 3365):     1    5  673
[0, 0] p( 3366):    18  198  306
[0, 0] p( 3367):     1    7   13   37   91  259  481
[0, 0] p( 3368):     8
[0, 0] p( 3369):     3
[0, 0] p( 3370):     2   10  674
[0, 0] p( 3371):     1
[0, 0] p( 3372):    12
[0, 0] p( 3373):     1
[0, 0] p( 3374):     2   14  482
[0, 0] p( 3375):    27  135  675
[0, 0] p( 3376):    16
[0, 0] p( 3377):     1   11  307
[0, 0] p( 3378):     6
[0, 0] p( 3379):     1   31  109
[0, 0] p( 3380):     4   20   52  260  676
[0, 0] p( 3381):     3   21   69  147  483
[0, 0] p( 3382):     2   38  178
[0, 0] p( 3383):     1   17  199
[0, 0] p( 3384):    72
[0, 0] p( 3385):     1    5  677
[0, 0] p( 3386):     2
[0, 0] p( 3387):     3
[0, 0] p( 3388):     4   28   44  308  484
[0, 0] p( 3389):     1
[0, 0] p( 3390):     6   30  678
[0, 0] p( 3391):     1
[0, 0] p( 3392):    64
[0, 0] p( 3393):     9  117  261
[0, 0] p( 3394):     2
[0, 0] p( 3395):     1    5    7   35   97  485  679
[0, 0] p( 3396):    12
[0, 0] p( 3397):     1   43   79
[0, 0] p( 3398):     2
[0, 0] p( 3399):     3   33  309
[0, 0] p( 3400):     8   40  136  200  680
[0, 0] p( 3401):     1   19  179
[0, 0] p( 3402):   486
[0, 0] p( 3403):     1   41   83
[0, 0] p( 3404):     4   92  148
[0, 0] p( 3405):     3   15  681
[0, 0] p( 3406):     2   26  262
[0, 0] p( 3407):     1
[0, 0] p( 3408):    48
[0, 0] p( 3409):     1    7  487
[0, 0] p( 3410):     2   10   22   62  110  310  682
[0, 0] p( 3411):     9
[0, 0] p( 3412):     4
[0, 0] p( 3413):     1
[0, 0] p( 3414):     6
[0, 0] p( 3415):     1    5  683
[0, 0] p( 3416):     8   56  488
[0, 0] p( 3417):     3   51  201
[0, 0] p( 3418):     2
[0, 0] p( 3419):     1   13  263
[0, 0] p( 3420):    36  180  684
[0, 0] p( 3421):     1   11  311
[0, 0] p( 3422):     2   58  118
[0, 0] p( 3423):     3   21  489
[0, 0] p( 3424):    32
[0, 0] p( 3425):     1    5   25  137  685
[0, 0] p( 3426):     6
[0, 0] p( 3427):     1   23  149
[0, 0] p( 3428):     4
[0, 0] p( 3429):    27
[0, 0] p( 3430):     2   10   14   70   98  490  686
[0, 0] p( 3431):     1   47   73
[0, 0] p( 3432):    24  264  312
[0, 0] p( 3433):     1
[0, 0] p( 3434):     2   34  202
[0, 0] p( 3435):     3   15  687
[0, 0] p( 3436):     4
[0, 0] p( 3437):     1    7  491
[0, 0] p( 3438):    18
[0, 0] p( 3439):     1   19  181
[0, 0] p( 3440):    16   80  688
[0, 0] p( 3441):     3   93  111
[0, 0] p( 3442):     2
[0, 0] p( 3443):     1   11  313
[0, 0] p( 3444):    12   84  492
[0, 0] p( 3445):     1    5   13   53   65  265  689
[0, 0] p( 3446):     2
[0, 0] p( 3447):     9
[0, 0] p( 3448):     8
[0, 0] p( 3449):     1
[0, 0] p( 3450):     6   30  138  150  690
[0, 0] p( 3451):     1    7   17   29  119  203  493
[0, 0] p( 3452):     4
[0, 0] p( 3453):     3
[0, 0] p( 3454):     2   22  314
[0, 0] p( 3455):     1    5  691
[0, 0] p( 3456): 
[0, 0] p( 3457):     1
[0, 0] p( 3458):     2   14   26   38  182  266  494
[0, 0] p( 3459):     3
[0, 0] p( 3460):     4   20  692
[0, 0] p( 3461):     1
[0, 0] p( 3462):     6
[0, 0] p( 3463):     1
[0, 0] p( 3464):     8
[0, 0] p( 3465):     9   45   63   99  315  495  693
[0, 0] p( 3466):     2
[0, 0] p( 3467):     1
[0, 0] p( 3468):    12  204
[0, 0] p( 3469):     1
[0, 0] p( 3470):     2   10  694
[0, 0] p( 3471):     3   39  267
[0, 0] p( 3472):    16  112  496
[0, 0] p( 3473):     1   23  151
[0, 0] p( 3474):    18
[0, 0] p( 3475):     1    5   25  139  695
[0, 0] p( 3476):     4   44  316
[0, 0] p( 3477):     3   57  183
[0, 0] p( 3478):     2   74   94
[0, 0] p( 3479):     1    7   49   71  497
[0, 0] p( 3480):    24  120  696
[0, 0] p( 3481):     1   59
[0, 0] p( 3482):     2
[0, 0] p( 3483):    81
[0, 0] p( 3484):     4   52  268
[0, 0] p( 3485):     1    5   17   41   85  205  697
[0, 0] p( 3486):     6   42  498
[0, 0] p( 3487):     1   11  317
[0, 0] p( 3488):    32
[0, 0] p( 3489):     3
[0, 0] p( 3490):     2   10  698
[0, 0] p( 3491):     1
[0, 0] p( 3492):    36
[0, 0] p( 3493):     1    7  499
[0, 0] p( 3494):     2
[0, 0] p( 3495):     3   15  699
[0, 0] p( 3496):     8  152  184
[0, 0] p( 3497):     1   13  269
[0, 0] p( 3498):     6   66  318
[0, 0] p( 3499):     1
[0, 0] p( 3500):     4   20   28  100  140  500  700
[0, 0] p( 3501):     9
[0, 0] p( 3502):     2   34  206
[0, 0] p( 3503):     1   31  113
[0, 0] p( 3504):    48
[0, 0] p( 3505):     1    5  701
[0, 0] p( 3506):     2
[0, 0] p( 3507):     3   21  501
[0, 0] p( 3508):     4
[0, 0] p( 3509):     1   11   29  121  319
[0, 0] p( 3510):    54  270  702
[0, 0] p( 3511):     1
[0, 0] p( 3512):     8
[0, 0] p( 3513):     3
[0, 0] p( 3514):     2   14  502
[0, 0] p( 3515):     1    5   19   37   95  185  703
[0, 0] p( 3516):    12
[0, 0] p( 3517):     1
[0, 0] p( 3518):     2
[0, 0] p( 3519):     9  153  207
[0, 0] p( 3520):    64  320  704
[0, 0] p( 3521):     1    7  503
[0, 0] p( 3522):     6
[0, 0] p( 3523):     1   13  271
[0, 0] p( 3524):     4
[0, 0] p( 3525):     3   15   75  141  705
[0, 0] p( 3526):     2   82   86
[0, 0] p( 3527):     1
[0, 0] p( 3528):    72  504
[0, 0] p( 3529):     1
[0, 0] p( 3530):     2   10  706
[0, 0] p( 3531):     3   33  321
[0, 0] p( 3532):     4
[0, 0] p( 3533):     1
[0, 0] p( 3534):     6  114  186
[0, 0] p( 3535):     1    5    7   35  101  505  707
[0, 0] p( 3536):    16  208  272
[0, 0] p( 3537):    27
[0, 0] p( 3538):     2   58  122
[0, 0] p( 3539):     1
[0, 0] p( 3540):    12   60  708
[0, 0] p( 3541):     1
[0, 0] p( 3542):     2   14   22   46  154  322  506
[0, 0] p( 3543):     3
[0, 0] p( 3544):     8
[0, 0] p( 3545):     1    5  709
[0, 0] p( 3546):    18
[0, 0] p( 3547):     1
[0, 0] p( 3548):     4
[0, 0] p( 3549):     3   21   39  273  507
[0, 0] p( 3550):     2   10   50  142  710
[0, 0] p( 3551):     1   53   67
[0, 0] p( 3552):    96
[0, 0] p( 3553):     1   11   17   19  187  209  323
[0, 0] p( 3554):     2
[0, 0] p( 3555):     9   45  711
[0, 0] p( 3556):     4   28  508
[0, 0] p( 3557):     1
[0, 0] p( 3558):     6
[0, 0] p( 3559):     1
[0, 0] p( 3560):     8   40  712
[0, 0] p( 3561):     3
[0, 0] p( 3562):     2   26  274
[0, 0] p( 3563):     1    7  509
[0, 0] p( 3564):   324
[0, 0] p( 3565):     1    5   23   31  115  155  713
[0, 0] p( 3566):     2
[0, 0] p( 3567):     3   87  123
[0, 0] p( 3568):    16
[0, 0] p( 3569):     1   43   83
[0, 0] p( 3570):     6   30   42  102  210  510  714
[0, 0] p( 3571):     1
[0, 0] p( 3572):     4   76  188
[0, 0] p( 3573):     9
[0, 0] p( 3574):     2
[0, 0] p( 3575):     1    5   11   13   25   55   65  143  275  325  715
[0, 0] p( 3576):    24
[0, 0] p( 3577):     1    7   49   73  511
[0, 0] p( 3578):     2
[0, 0] p( 3579):     3
[0, 0] p( 3580):     4   20  716
[0, 0] p( 3581):     1
[0, 0] p( 3582):    18
[0, 0] p( 3583):     1
[0, 0] p( 3584):   512
[0, 0] p( 3585):     3   15  717
[0, 0] p( 3586):     2   22  326
[0, 0] p( 3587):     1   17  211
[0, 0] p( 3588):    12  156  276
[0, 0] p( 3589):     1   37   97
[0, 0] p( 3590):     2   10  718
[0, 0] p( 3591):    27  189  513
[0, 0] p( 3592):     8
[0, 0] p( 3593):     1
[0, 0] p( 3594):     6
[0, 0] p( 3595):     1    5  719
[0, 0] p( 3596):     4  116  124
[0, 0] p( 3597):     3   33  327
[0, 0] p( 3598):     2   14  514
[0, 0] p( 3599):     1   59   61
[0, 0] p( 3600):   144  720
[0, 0] p( 3601):     1   13  277
[0, 0] p( 3602):     2
[0, 0] p( 3603):     3
[0, 0] p( 3604):     4   68  212
[0, 0] p( 3605):     1    5    7   35  103  515  721
[0, 0] p( 3606):     6
[0, 0] p( 3607):     1
[0, 0] p( 3608):     8   88  328
[0, 0] p( 3609):     9
[0, 0] p( 3610):     2   10   38  190  722
[0, 0] p( 3611):     1   23  157
[0, 0] p( 3612):    12   84  516
[0, 0] p( 3613):     1
[0, 0] p( 3614):     2   26  278
[0, 0] p( 3615):     3   15  723
[0, 0] p( 3616):    32
[0, 0] p( 3617):     1
[0, 0] p( 3618):    54
[0, 0] p( 3619):     1    7   11   47   77  329  517
[0, 0] p( 3620):     4   20  724
[0, 0] p( 3621):     3   51  213
[0, 0] p( 3622):     2
[0, 0] p( 3623):     1
[0, 0] p( 3624):    24
[0, 0] p( 3625):     1    5   25   29  125  145  725
[0, 0] p( 3626):     2   14   74   98  518
[0, 0] p( 3627):     9  117  279
[0, 0] p( 3628):     4
[0, 0] p( 3629):     1   19  191
[0, 0] p( 3630):     6   30   66  330  726
[0, 0] p( 3631):     1
[0, 0] p( 3632):    16
[0, 0] p( 3633):     3   21  519
[0, 0] p( 3634):     2   46  158
[0, 0] p( 3635):     1    5  727
[0, 0] p( 3636):    36
[0, 0] p( 3637):     1
[0, 0] p( 3638):     2   34  214
[0, 0] p( 3639):     3
[0, 0] p( 3640):     8   40   56  104  280  520  728
[0, 0] p( 3641):     1   11  331
[0, 0] p( 3642):     6
[0, 0] p( 3643):     1
[0, 0] p( 3644):     4
[0, 0] p( 3645):   729
[0, 0] p( 3646):     2
[0, 0] p( 3647):     1    7  521
[0, 0] p( 3648):   192
[0, 0] p( 3649):     1   41   89
[0, 0] p( 3650):     2   10   50  146  730
[0, 0] p( 3651):     3
[0, 0] p( 3652):     4   44  332
[0, 0] p( 3653):     1   13  281
[0, 0] p( 3654):    18  126  522
[0, 0] p( 3655):     1    5   17   43   85  215  731
[0, 0] p( 3656):     8
[0, 0] p( 3657):     3   69  159
[0, 0] p( 3658):     2   62  118
[0, 0] p( 3659):     1
[0, 0] p( 3660):    12   60  732
[0, 0] p( 3661):     1    7  523
[0, 0] p( 3662):     2
[0, 0] p( 3663):     9   99  333
[0, 0] p( 3664):    16
[0, 0] p( 3665):     1    5  733
[0, 0] p( 3666):     6   78  282
[0, 0] p( 3667):     1   19  193
[0, 0] p( 3668):     4   28  524
[0, 0] p( 3669):     3
[0, 0] p( 3670):     2   10  734
[0, 0] p( 3671):     1
[0, 0] p( 3672):   216
[0, 0] p( 3673):     1
[0, 0] p( 3674):     2   22  334
[0, 0] p( 3675):     3   15   21   75  105  147  525  735
[0, 0] p( 3676):     4
[0, 0] p( 3677):     1
[0, 0] p( 3678):     6
[0, 0] p( 3679):     1   13  283
[0, 0] p( 3680):    32  160  736
[0, 0] p( 3681):     9
[0, 0] p( 3682):     2   14  526
[0, 0] p( 3683):     1   29  127
[0, 0] p( 3684):    12
[0, 0] p( 3685):     1    5   11   55   67  335  737
[0, 0] p( 3686):     2   38  194
[0, 0] p( 3687):     3
[0, 0] p( 3688):     8
[0, 0] p( 3689):     1    7   17   31  119  217  527
[0, 0] p( 3690):    18   90  738
[0, 0] p( 3691):     1
[0, 0] p( 3692):     4   52  284
[0, 0] p( 3693):     3
[0, 0] p( 3694):     2
[0, 0] p( 3695):     1    5  739
[0, 0] p( 3696):    48  336  528
[0, 0] p( 3697):     1
[0, 0] p( 3698):     2   86
[0, 0] p( 3699):    27
[0, 0] p( 3700):     4   20  100  148  740
[0, 0] p( 3701):     1
[0, 0] p( 3702):     6
[0, 0] p( 3703):     1    7   23  161  529
[0, 0] p( 3704):     8
[0, 0] p( 3705):     3   15   39   57  195  285  741
[0, 0] p( 3706):     2   34  218
[0, 0] p( 3707):     1   11  337
[0, 0] p( 3708):    36
[0, 0] p( 3709):     1
[0, 0] p( 3710):     2   10   14   70  106  530  742
[0, 0] p( 3711):     3
[0, 0] p( 3712):   128
[0, 0] p( 3713):     1   47   79
[0, 0] p( 3714):     6
[0, 0] p( 3715):     1    5  743
[0, 0] p( 3716):     4
[0, 0] p( 3717):     9   63  531
[0, 0] p( 3718):     2   22   26  286  338
[0, 0] p( 3719):     1
[0, 0] p( 3720):    24  120  744
[0, 0] p( 3721):     1   61
[0, 0] p( 3722):     2
[0, 0] p( 3723):     3   51  219
[0, 0] p( 3724):     4   28   76  196  532
[0, 0] p( 3725):     1    5   25  149  745
[0, 0] p( 3726):   162
[0, 0] p( 3727):     1
[0, 0] p( 3728):    16
[0, 0] p( 3729):     3   33  339
[0, 0] p( 3730):     2   10  746
[0, 0] p( 3731):     1    7   13   41   91  287  533
[0, 0] p( 3732):    12
[0, 0] p( 3733):     1
[0, 0] p( 3734):     2
[0, 0] p( 3735):     9   45  747
[0, 0] p( 3736):     8
[0, 0] p( 3737):     1   37  101
[0, 0] p( 3738):     6   42  534
[0, 0] p( 3739):     1
[0, 0] p( 3740):     4   20   44   68  220  340  748
[0, 0] p( 3741):     3   87  129
[0, 0] p( 3742):     2
[0, 0] p( 3743):     1   19  197
[0, 0] p( 3744):   288
[0, 0] p( 3745):     1    5    7   35  107  535  749
[0, 0] p( 3746):     2
[0, 0] p( 3747):     3
[0, 0] p( 3748):     4
[0, 0] p( 3749):     1   23  163
[0, 0] p( 3750):     6   30  150  750
[0, 0] p( 3751):     1   11   31  121  341
[0, 0] p( 3752):     8   56  536
[0, 0] p( 3753):    27
[0, 0] p( 3754):     2
[0, 0] p( 3755):     1    5  751
[0, 0] p( 3756):    12
[0, 0] p( 3757):     1   13   17  221  289
[0, 0] p( 3758):     2
[0, 0] p( 3759):     3   21  537
[0, 0] p( 3760):    16   80  752
[0, 0] p( 3761):     1
[0, 0] p( 3762):    18  198  342
[0, 0] p( 3763):     1   53   71
[0, 0] p( 3764):     4
[0, 0] p( 3765):     3   15  753
[0, 0] p( 3766):     2   14  538
[0, 0] p( 3767):     1
[0, 0] p( 3768):    24
[0, 0] p( 3769):     1
[0, 0] p( 3770):     2   10   26   58  130  290  754
[0, 0] p( 3771):     9
[0, 0] p( 3772):     4   92  164
[0, 0] p( 3773):     1    7   11   49   77  343  539
[0, 0] p( 3774):     6  102  222
[0, 0] p( 3775):     1    5   25  151  755
[0, 0] p( 3776):    64
[0, 0] p( 3777):     3
[0, 0] p( 3778):     2
[0, 0] p( 3779):     1
[0, 0] p( 3780):   108  540  756
[0, 0] p( 3781):     1   19  199
[0, 0] p( 3782):     2   62  122
[0, 0] p( 3783):     3   39  291
[0, 0] p( 3784):     8   88  344
[0, 0] p( 3785):     1    5  757
[0, 0] p( 3786):     6
[0, 0] p( 3787):     1    7  541
[0, 0] p( 3788):     4
[0, 0] p( 3789):     9
[0, 0] p( 3790):     2   10  758
[0, 0] p( 3791):     1   17  223
[0, 0] p( 3792):    48
[0, 0] p( 3793):     1
[0, 0] p( 3794):     2   14  542
[0, 0] p( 3795):     3   15   33   69  165  345  759
[0, 0] p( 3796):     4   52  292
[0, 0] p( 3797):     1
[0, 0] p( 3798):    18
[0, 0] p( 3799):     1   29  131
[0, 0] p( 3800):     8   40  152  200  760
[0, 0] p( 3801):     3   21  543
[0, 0] p( 3802):     2
[0, 0] p( 3803):     1
[0, 0] p( 3804):    12
[0, 0] p( 3805):     1    5  761
[0, 0] p( 3806):     2   22  346
[0, 0] p( 3807):    81
[0, 0] p( 3808):    32  224  544
[0, 0] p( 3809):     1   13  293
[0, 0] p( 3810):     6   30  762
[0, 0] p( 3811):     1   37  103
[0, 0] p( 3812):     4
[0, 0] p( 3813):     3   93  123
[0, 0] p( 3814):     2
[0, 0] p( 3815):     1    5    7   35  109  545  763
[0, 0] p( 3816):    72
[0, 0] p( 3817):     1   11  347
[0, 0] p( 3818):     2   46  166
[0, 0] p( 3819):     3   57  201
[0, 0] p( 3820):     4   20  764
[0, 0] p( 3821):     1
[0, 0] p( 3822):     6   42   78  294  546
[0, 0] p( 3823):     1
[0, 0] p( 3824):    16
[0, 0] p( 3825):     9   45  153  225  765
[0, 0] p( 3826):     2
[0, 0] p( 3827):     1   43   89
[0, 0] p( 3828):    12  132  348
[0, 0] p( 3829):     1    7  547
[0, 0] p( 3830):     2   10  766
[0, 0] p( 3831):     3
[0, 0] p( 3832):     8
[0, 0] p( 3833):     1
[0, 0] p( 3834):    54
[0, 0] p( 3835):     1    5   13   59   65  295  767
[0, 0] p( 3836):     4   28  548
[0, 0] p( 3837):     3
[0, 0] p( 3838):     2   38  202
[0, 0] p( 3839):     1   11  349
[0, 0] p( 3840):   768
[0, 0] p( 3841):     1   23  167
[0, 0] p( 3842):     2   34  226
[0, 0] p( 3843):     9   63  549
[0, 0] p( 3844):     4  124
[0, 0] p( 3845):     1    5  769
[0, 0] p( 3846):     6
[0, 0] p( 3847):     1
[0, 0] p( 3848):     8  104  296
[0, 0] p( 3849):     3
[0, 0] p( 3850):     2   10   14   22   50   70  110  154  350  550  770
[0, 0] p( 3851):     1
[0, 0] p( 3852):    36
[0, 0] p( 3853):     1
[0, 0] p( 3854):     2   82   94
[0, 0] p( 3855):     3   15  771
[0, 0] p( 3856):    16
[0, 0] p( 3857):     1    7   19   29  133  203  551
[0, 0] p( 3858):     6
[0, 0] p( 3859):     1   17  227
[0, 0] p( 3860):     4   20  772
[0, 0] p( 3861):    27  297  351
[0, 0] p( 3862):     2
[0, 0] p( 3863):     1
[0, 0] p( 3864):    24  168  552
[0, 0] p( 3865):     1    5  773
[0, 0] p( 3866):     2
[0, 0] p( 3867):     3
[0, 0] p( 3868):     4
[0, 0] p( 3869):     1   53   73
[0, 0] p( 3870):    18   90  774
[0, 0] p( 3871):     1    7   49   79  553
[0, 0] p( 3872):    32  352
[0, 0] p( 3873):     3
[0, 0] p( 3874):     2   26  298
[0, 0] p( 3875):     1    5   25   31  125  155  775
[0, 0] p( 3876):    12  204  228
[0, 0] p( 3877):     1
[0, 0] p( 3878):     2   14  554
[0, 0] p( 3879):     9
[0, 0] p( 3880):     8   40  776
[0, 0] p( 3881):     1
[0, 0] p( 3882):     6
[0, 0] p( 3883):     1   11  353
[0, 0] p( 3884):     4
[0, 0] p( 3885):     3   15   21  105  111  555  777
[0, 0] p( 3886):     2   58  134
[0, 0] p( 3887):     1   13   23  169  299
[0, 0] p( 3888): 
[0, 0] p( 3889):     1
[0, 0] p( 3890):     2   10  778
[0, 0] p( 3891):     3
[0, 0] p( 3892):     4   28  556
[0, 0] p( 3893):     1   17  229
[0, 0] p( 3894):     6   66  354
[0, 0] p( 3895):     1    5   19   41   95  205  779
[0, 0] p( 3896):     8
[0, 0] p( 3897):     9
[0, 0] p( 3898):     2
[0, 0] p( 3899):     1    7  557
[0, 0] p( 3900):    12   60  156  300  780
[0, 0] p( 3901):     1   47   83
[0, 0] p( 3902):     2
[0, 0] p( 3903):     3
[0, 0] p( 3904):    64
[0, 0] p( 3905):     1    5   11   55   71  355  781
[0, 0] p( 3906):    18  126  558
[0, 0] p( 3907):     1
[0, 0] p( 3908):     4
[0, 0] p( 3909):     3
[0, 0] p( 3910):     2   10   34   46  170  230  782
[0, 0] p( 3911):     1
[0, 0] p( 3912):    24
[0, 0] p( 3913):     1    7   13   43   91  301  559
[0, 0] p( 3914):     2   38  206
[0, 0] p( 3915):    27  135  783
[0, 0] p( 3916):     4   44  356
[0, 0] p( 3917):     1
[0, 0] p( 3918):     6
[0, 0] p( 3919):     1
[0, 0] p( 3920):    16   80  112  560  784
[0, 0] p( 3921):     3
[0, 0] p( 3922):     2   74  106
[0, 0] p( 3923):     1
[0, 0] p( 3924):    36
[0, 0] p( 3925):     1    5   25  157  785
[0, 0] p( 3926):     2   26  302
[0, 0] p( 3927):     3   21   33   51  231  357  561
[0, 0] p( 3928):     8
[0, 0] p( 3929):     1
[0, 0] p( 3930):     6   30  786
[0, 0] p( 3931):     1
[0, 0] p( 3932):     4
[0, 0] p( 3933):     9  171  207
[0, 0] p( 3934):     2   14  562
[0, 0] p( 3935):     1    5  787
[0, 0] p( 3936):    96
[0, 0] p( 3937):     1   31  127
[0, 0] p( 3938):     2   22  358
[0, 0] p( 3939):     3   39  303
[0, 0] p( 3940):     4   20  788
[0, 0] p( 3941):     1    7  563
[0, 0] p( 3942):    54
[0, 0] p( 3943):     1
[0, 0] p( 3944):     8  136  232
[0, 0] p( 3945):     3   15  789
[0, 0] p( 3946):     2
[0, 0] p( 3947):     1
[0, 0] p( 3948):    12   84  564
[0, 0] p( 3949):     1   11  359
[0, 0] p( 3950):     2   10   50  158  790
[0, 0] p( 3951):     9
[0, 0] p( 3952):    16  208  304
[0, 0] p( 3953):     1   59   67
[0, 0] p( 3954):     6
[0, 0] p( 3955):     1    5    7   35  113  565  791
[0, 0] p( 3956):     4   92  172
[0, 0] p( 3957):     3
[0, 0] p( 3958):     2
[0, 0] p( 3959):     1   37  107
[0, 0] p( 3960):    72  360  792
[0, 0] p( 3961):     1   17  233
[0, 0] p( 3962):     2   14  566
[0, 0] p( 3963):     3
[0, 0] p( 3964):     4
[0, 0] p( 3965):     1    5   13   61   65  305  793
[0, 0] p( 3966):     6
[0, 0] p( 3967):     1
[0, 0] p( 3968):   128
[0, 0] p( 3969):    81  567
[0, 0] p( 3970):     2   10  794
[0, 0] p( 3971):     1   11   19  209  361
[0, 0] p( 3972):    12
[0, 0] p( 3973):     1   29  137
[0, 0] p( 3974):     2
[0, 0] p( 3975):     3   15   75  159  795
[0, 0] p( 3976):     8   56  568
[0, 0] p( 3977):     1   41   97
[0, 0] p( 3978):    18  234  306
[0, 0] p( 3979):     1   23  173
[0, 0] p( 3980):     4   20  796
[0, 0] p( 3981):     3
[0, 0] p( 3982):     2   22  362
[0, 0] p( 3983):     1    7  569
[0, 0] p( 3984):    48
[0, 0] p( 3985):     1    5  797
[0, 0] p( 3986):     2
[0, 0] p( 3987):     9
[0, 0] p( 3988):     4
[0, 0] p( 3989):     1
[0, 0] p( 3990):     6   30   42  114  210  570  798
[0, 0] p( 3991):     1   13  307
[0, 0] p( 3992):     8
[0, 0] p( 3993):     3   33  363
[0, 0] p( 3994):     2
[0, 0] p( 3995):     1    5   17   47   85  235  799
[0, 0] p( 3996):   108
[0, 0] p( 3997):     1    7  571
[0, 0] p( 3998):     2
[0, 0] p( 3999):     3   93  129
[0, 0] p( 4000):    32  160  800
[0, 0] p( 4001):     1
[0, 0] p( 4002):     6  138  174
[0, 0] p( 4003):     1
[0, 0] p( 4004):     4   28   44   52  308  364  572
[0, 0] p( 4005):     9   45  801
[0, 0] p( 4006):     2
[0, 0] p( 4007):     1
[0, 0] p( 4008):    24
[0, 0] p( 4009):     1   19  211
[0, 0] p( 4010):     2   10  802
[0, 0] p( 4011):     3   21  573
[0, 0] p( 4012):     4   68  236
[0, 0] p( 4013):     1
[0, 0] p( 4014):    18
[0, 0] p( 4015):     1    5   11   55   73  365  803
[0, 0] p( 4016):    16
[0, 0] p( 4017):     3   39  309
[0, 0] p( 4018):     2   14   82   98  574
[0, 0] p( 4019):     1
[0, 0] p( 4020):    12   60  804
[0, 0] p( 4021):     1
[0, 0] p( 4022):     2
[0, 0] p( 4023):    27
[0, 0] p( 4024):     8
[0, 0] p( 4025):     1    5    7   23   25   35  115  161  175  575  805
[0, 0] p( 4026):     6   66  366
[0, 0] p( 4027):     1
[0, 0] p( 4028):     4   76  212
[0, 0] p( 4029):     3   51  237
[0, 0] p( 4030):     2   10   26   62  130  310  806
[0, 0] p( 4031):     1   29  139
[0, 0] p( 4032):   576
[0, 0] p( 4033):     1   37  109
[0, 0] p( 4034):     2
[0, 0] p( 4035):     3   15  807
[0, 0] p( 4036):     4
[0, 0] p( 4037):     1   11  367
[0, 0] p( 4038):     6
[0, 0] p( 4039):     1    7  577
[0, 0] p( 4040):     8   40  808
[0, 0] p( 4041):     9
[0, 0] p( 4042):     2   86   94
[0, 0] p( 4043):     1   13  311
[0, 0] p( 4044):    12
[0, 0] p( 4045):     1    5  809
[0, 0] p( 4046):     2   14   34  238  578
[0, 0] p( 4047):     3   57  213
[0, 0] p( 4048):    16  176  368
[0, 0] p( 4049):     1
[0, 0] p( 4050):   162  810
[0, 0] p( 4051):     1
[0, 0] p( 4052):     4
[0, 0] p( 4053):     3   21  579
[0, 0] p( 4054):     2
[0, 0] p( 4055):     1    5  811
[0, 0] p( 4056):    24  312
[0, 0] p( 4057):     1
[0, 0] p( 4058):     2
[0, 0] p( 4059):     9   99  369
[0, 0] p( 4060):     4   20   28  116  140  580  812
[0, 0] p( 4061):     1   31  131
[0, 0] p( 4062):     6
[0, 0] p( 4063):     1   17  239
[0, 0] p( 4064):    32
[0, 0] p( 4065):     3   15  813
[0, 0] p( 4066):     2   38  214
[0, 0] p( 4067):     1    7   49   83  581
[0, 0] p( 4068):    36
[0, 0] p( 4069):     1   13  313
[0, 0] p( 4070):     2   10   22   74  110  370  814
[0, 0] p( 4071):     3   69  177
[0, 0] p( 4072):     8
[0, 0] p( 4073):     1
[0, 0] p( 4074):     6   42  582
[0, 0] p( 4075):     1    5   25  163  815
[0, 0] p( 4076):     4
[0, 0] p( 4077):    27
[0, 0] p( 4078):     2
[0, 0] p( 4079):     1
[0, 0] p( 4080):    48  240  816
[0, 0] p( 4081):     1    7   11   53   77  371  583
[0, 0] p( 4082):     2   26  314
[0, 0] p( 4083):     3
[0, 0] p( 4084):     4
[0, 0] p( 4085):     1    5   19   43   95  215  817
[0, 0] p( 4086):    18
[0, 0] p( 4087):     1   61   67
[0, 0] p( 4088):     8   56  584
[0, 0] p( 4089):     3   87  141
[0, 0] p( 4090):     2   10  818
[0, 0] p( 4091):     1
[0, 0] p( 4092):    12  132  372
[0, 0] p( 4093):     1
[0, 0] p( 4094):     2   46  178
[0, 0] p( 4095):     9   45   63  117  315  585  819
[0, 0] p( 4096): 
[0, 0] p( 4097):     1   17  241
[0, 0] p( 4098):     6
[0, 0] p( 4099):     1
[0, 0] p( 4100):     4   20  100  164  820
[0, 0] p( 4101):     3
[0, 0] p( 4102):     2   14  586
[0, 0] p( 4103):     1   11  373
[0, 0] p( 4104):   216
[0, 0] p( 4105):     1    5  821
[0, 0] p( 4106):     2
[0, 0] p( 4107):     3  111
[0, 0] p( 4108):     4   52  316
[0, 0] p( 4109):     1    7  587
[0, 0] p( 4110):     6   30  822
[0, 0] p( 4111):     1
[0, 0] p( 4112):    16
[0, 0] p( 4113):     9
[0, 0] p( 4114):     2   22   34  242  374
[0, 0] p( 4115):     1    5  823
[0, 0] p( 4116):    12   84  588
[0, 0] p( 4117):     1   23  179
[0, 0] p( 4118):     2   58  142
[0, 0] p( 4119):     3
[0, 0] p( 4120):     8   40  824
[0, 0] p( 4121):     1   13  317
[0, 0] p( 4122):    18
[0, 0] p( 4123):     1    7   19   31  133  217  589
[0, 0] p( 4124):     4
[0, 0] p( 4125):     3   15   33   75  165  375  825
[0, 0] p( 4126):     2
[0, 0] p( 4127):     1
[0, 0] p( 4128):    96
[0, 0] p( 4129):     1
[0, 0] p( 4130):     2   10   14   70  118  590  826
[0, 0] p( 4131):   243
[0, 0] p( 4132):     4
[0, 0] p( 4133):     1
[0, 0] p( 4134):     6   78  318
[0, 0] p( 4135):     1    5  827
[0, 0] p( 4136):     8   88  376
[0, 0] p( 4137):     3   21  591
[0, 0] p( 4138):     2
[0, 0] p( 4139):     1
[0, 0] p( 4140):    36  180  828
[0, 0] p( 4141):     1   41  101
[0, 0] p( 4142):     2   38  218
[0, 0] p( 4143):     3
[0, 0] p( 4144):    16  112  592
[0, 0] p( 4145):     1    5  829
[0, 0] p( 4146):     6
[0, 0] p( 4147):     1   11   13   29  143  319  377
[0, 0] p( 4148):     4   68  244
[0, 0] p( 4149):     9
[0, 0] p( 4150):     2   10   50  166  830
[0, 0] p( 4151):     1    7  593
[0, 0] p( 4152):    24
[0, 0] p( 4153):     1
[0, 0] p( 4154):     2   62  134
[0, 0] p( 4155):     3   15  831
[0, 0] p( 4156):     4
[0, 0] p( 4157):     1
[0, 0] p( 4158):    54  378  594
[0, 0] p( 4159):     1
[0, 0] p( 4160):    64  320  832
[0, 0] p( 4161):     3   57  219
[0, 0] p( 4162):     2
[0, 0] p( 4163):     1   23  181
[0, 0] p( 4164):    12
[0, 0] p( 4165):     1    5    7   17   35   49   85  119  245  595  833
[0, 0] p( 4166):     2
[0, 0] p( 4167):     9
[0, 0] p( 4168):     8
[0, 0] p( 4169):     1   11  379
[0, 0] p( 4170):     6   30  834
[0, 0] p( 4171):     1   43   97
[0, 0] p( 4172):     4   28  596
[0, 0] p( 4173):     3   39  321
[0, 0] p( 4174):     2
[0, 0] p( 4175):     1    5   25  167  835
[0, 0] p( 4176):   144
[0, 0] p( 4177):     1
[0, 0] p( 4178):     2
[0, 0] p( 4179):     3   21  597
[0, 0] p( 4180):     4   20   44   76  220  380  836
[0, 0] p( 4181):     1   37  113
[0, 0] p( 4182):     6  102  246
[0, 0] p( 4183):     1   47   89
[0, 0] p( 4184):     8
[0, 0] p( 4185):    27  135  837
[0, 0] p( 4186):     2   14   26   46  182  322  598
[0, 0] p( 4187):     1   53   79
[0, 0] p( 4188):    12
[0, 0] p( 4189):     1   59   71
[0, 0] p( 4190):     2   10  838
[0, 0] p( 4191):     3   33  381
[0, 0] p( 4192):    32
[0, 0] p( 4193):     1    7  599
[0, 0] p( 4194):    18
[0, 0] p( 4195):     1    5  839
[0, 0] p( 4196):     4
[0, 0] p( 4197):     3
[0, 0] p( 4198):     2
[0, 0] p( 4199):     1   13   17   19  221  247  323
[0, 0] p( 4200):    24  120  168  600  840
[0, 0] p( 4201):     1
[0, 0] p( 4202):     2   22  382
[0, 0] p( 4203):     9
[0, 0] p( 4204):     4
[0, 0] p( 4205):     1    5   29  145  841
[0, 0] p( 4206):     6
[0, 0] p( 4207):     1    7  601
[0, 0] p( 4208):    16
[0, 0] p( 4209):     3   69  183
[0, 0] p( 4210):     2   10  842
[0, 0] p( 4211):     1
[0, 0] p( 4212):   324
[0, 0] p( 4213):     1   11  383
[0, 0] p( 4214):     2   14   86   98  602
[0, 0] p( 4215):     3   15  843
[0, 0] p( 4216):     8  136  248
[0, 0] p( 4217):     1
[0, 0] p( 4218):     6  114  222
[0, 0] p( 4219):     1
[0, 0] p( 4220):     4   20  844
[0, 0] p( 4221):     9   63  603
[0, 0] p( 4222):     2
[0, 0] p( 4223):     1   41  103
[0, 0] p( 4224):   384
[0, 0] p( 4225):     1    5   13   25   65  169  325  845
[0, 0] p( 4226):     2
[0, 0] p( 4227):     3
[0, 0] p( 4228):     4   28  604
[0, 0] p( 4229):     1
[0, 0] p( 4230):    18   90  846
[0, 0] p( 4231):     1
[0, 0] p( 4232):     8  184
[0, 0] p( 4233):     3   51  249
[0, 0] p( 4234):     2   58  146
[0, 0] p( 4235):     1    5    7   11   35   55   77  121  385  605  847
[0, 0] p( 4236):    12
[0, 0] p( 4237):     1   19  223
[0, 0] p( 4238):     2   26  326
[0, 0] p( 4239):    27
[0, 0] p( 4240):    16   80  848
[0, 0] p( 4241):     1
[0, 0] p( 4242):     6   42  606
[0, 0] p( 4243):     1
[0, 0] p( 4244):     4
[0, 0] p( 4245):     3   15  849
[0, 0] p( 4246):     2   22  386
[0, 0] p( 4247):     1   31  137
[0, 0] p( 4248):    72
[0, 0] p( 4249):     1    7  607
[0, 0] p( 4250):     2   10   34   50  170  250  850
[0, 0] p( 4251):     3   39  327
[0, 0] p( 4252):     4
[0, 0] p( 4253):     1
[0, 0] p( 4254):     6
[0, 0] p( 4255):     1    5   23   37  115  185  851
[0, 0] p( 4256):    32  224  608
[0, 0] p( 4257):     9   99  387
[0, 0] p( 4258):     2
[0, 0] p( 4259):     1
[0, 0] p( 4260):    12   60  852
[0, 0] p( 4261):     1
[0, 0] p( 4262):     2
[0, 0] p( 4263):     3   21   87  147  609
[0, 0] p( 4264):     8  104  328
[0, 0] p( 4265):     1    5  853
[0, 0] p( 4266):    54
[0, 0] p( 4267):     1   17  251
[0, 0] p( 4268):     4   44  388
[0, 0] p( 4269):     3
[0, 0] p( 4270):     2   10   14   70  122  610  854
[0, 0] p( 4271):     1
[0, 0] p( 4272):    48
[0, 0] p( 4273):     1
[0, 0] p( 4274):     2
[0, 0] p( 4275):     9   45  171  225  855
[0, 0] p( 4276):     4
[0, 0] p( 4277):     1    7   13   47   91  329  611
[0, 0] p( 4278):     6  138  186
[0, 0] p( 4279):     1   11  389
[0, 0] p( 4280):     8   40  856
[0, 0] p( 4281):     3
[0, 0] p( 4282):     2
[0, 0] p( 4283):     1
[0, 0] p( 4284):    36  252  612
[0, 0] p( 4285):     1    5  857
[0, 0] p( 4286):     2
[0, 0] p( 4287):     3
[0, 0] p( 4288):    64
[0, 0] p( 4289):     1
[0, 0] p( 4290):     6   30   66   78  330  390  858
[0, 0] p( 4291):     1    7  613
[0, 0] p( 4292):     4  116  148
[0, 0] p( 4293):    81
[0, 0] p( 4294):     2   38  226
[0, 0] p( 4295):     1    5  859
[0, 0] p( 4296):    24
[0, 0] p( 4297):     1
[0, 0] p( 4298):     2   14  614
[0, 0] p( 4299):     3
[0, 0] p( 4300):     4   20  100  172  860
[0, 0] p( 4301):     1   11   17   23  187  253  391
[0, 0] p( 4302):    18
[0, 0] p( 4303):     1   13  331
[0, 0] p( 4304):    16
[0, 0] p( 4305):     3   15   21  105  123  615  861
[0, 0] p( 4306):     2
[0, 0] p( 4307):     1   59   73
[0, 0] p( 4308):    12
[0, 0] p( 4309):     1   31  139
[0, 0] p( 4310):     2   10  862
[0, 0] p( 4311):     9
[0, 0] p( 4312):     8   56   88  392  616
[0, 0] p( 4313):     1   19  227
[0, 0] p( 4314):     6
[0, 0] p( 4315):     1    5  863
[0, 0] p( 4316):     4   52  332
[0, 0] p( 4317):     3
[0, 0] p( 4318):     2   34  254
[0, 0] p( 4319):     1    7  617
[0, 0] p( 4320):   864
[0, 0] p( 4321):     1   29  149
[0, 0] p( 4322):     2
[0, 0] p( 4323):     3   33  393
[0, 0] p( 4324):     4   92  188
[0, 0] p( 4325):     1    5   25  173  865
[0, 0] p( 4326):     6   42  618
[0, 0] p( 4327):     1
[0, 0] p( 4328):     8
[0, 0] p( 4329):     9  117  333
[0, 0] p( 4330):     2   10  866
[0, 0] p( 4331):     1   61   71
[0, 0] p( 4332):    12  228
[0, 0] p( 4333):     1    7  619
[0, 0] p( 4334):     2   22  394
[0, 0] p( 4335):     3   15   51  255  867
[0, 0] p( 4336):    16
[0, 0] p( 4337):     1
[0, 0] p( 4338):    18
[0, 0] p( 4339):     1
[0, 0] p( 4340):     4   20   28  124  140  620  868
[0, 0] p( 4341):     3
[0, 0] p( 4342):     2   26  334
[0, 0] p( 4343):     1   43  101
[0, 0] p( 4344):    24
[0, 0] p( 4345):     1    5   11   55   79  395  869
[0, 0] p( 4346):     2   82  106
[0, 0] p( 4347):    27  189  621
[0, 0] p( 4348):     4
[0, 0] p( 4349):     1
[0, 0] p( 4350):     6   30  150  174  870
[0, 0] p( 4351):     1   19  229
[0, 0] p( 4352):   256
[0, 0] p( 4353):     3
[0, 0] p( 4354):     2   14  622
[0, 0] p( 4355):     1    5   13   65   67  335  871
[0, 0] p( 4356):    36  396
[0, 0] p( 4357):     1
[0, 0] p( 4358):     2
[0, 0] p( 4359):     3
[0, 0] p( 4360):     8   40  872
[0, 0] p( 4361):     1    7   49   89  623
[0, 0] p( 4362):     6
[0, 0] p( 4363):     1
[0, 0] p( 4364):     4
[0, 0] p( 4365):     9   45  873
[0, 0] p( 4366):     2   74  118
[0, 0] p( 4367):     1   11  397
[0, 0] p( 4368):    48  336  624
[0, 0] p( 4369):     1   17  257
[0, 0] p( 4370):     2   10   38   46  190  230  874
[0, 0] p( 4371):     3   93  141
[0, 0] p( 4372):     4
[0, 0] p( 4373):     1
[0, 0] p( 4374): 
[0, 0] p( 4375):     1    5    7   25   35  125  175  625  875
[0, 0] p( 4376):     8
[0, 0] p( 4377):     3
[0, 0] p( 4378):     2   22  398
[0, 0] p( 4379):     1   29  151
[0, 0] p( 4380):    12   60  876
[0, 0] p( 4381):     1   13  337
[0, 0] p( 4382):     2   14  626
[0, 0] p( 4383):     9
[0, 0] p( 4384):    32
[0, 0] p( 4385):     1    5  877
[0, 0] p( 4386):     6  102  258
[0, 0] p( 4387):     1   41  107
[0, 0] p( 4388):     4
[0, 0] p( 4389):     3   21   33   57  231  399  627
[0, 0] p( 4390):     2   10  878
[0, 0] p( 4391):     1
[0, 0] p( 4392):    72
[0, 0] p( 4393):     1   23  191
[0, 0] p( 4394):     2   26  338
[0, 0] p( 4395):     3   15  879
[0, 0] p( 4396):     4   28  628
[0, 0] p( 4397):     1
[0, 0] p( 4398):     6
[0, 0] p( 4399):     1   53   83
[0, 0] p( 4400):    16   80  176  400  880
[0, 0] p( 4401):    27
[0, 0] p( 4402):     2   62  142
[0, 0] p( 4403):     1    7   17   37  119  259  629
[0, 0] p( 4404):    12
[0, 0] p( 4405):     1    5  881
[0, 0] p( 4406):     2
[0, 0] p( 4407):     3   39  339
[0, 0] p( 4408):     8  152  232
[0, 0] p( 4409):     1
[0, 0] p( 4410):    18   90  126  630  882
[0, 0] p( 4411):     1   11  401
[0, 0] p( 4412):     4
[0, 0] p( 4413):     3
[0, 0] p( 4414):     2
[0, 0] p( 4415):     1    5  883
[0, 0] p( 4416):   192
[0, 0] p( 4417):     1    7  631
[0, 0] p( 4418):     2   94
[0, 0] p( 4419):     9
[0, 0] p( 4420):     4   20   52   68  260  340  884
[0, 0] p( 4421):     1
[0, 0] p( 4422):     6   66  402
[0, 0] p( 4423):     1
[0, 0] p( 4424):     8   56  632
[0, 0] p( 4425):     3   15   75  177  885
[0, 0] p( 4426):     2
[0, 0] p( 4427):     1   19  233
[0, 0] p( 4428):   108
[0, 0] p( 4429):     1   43  103
[0, 0] p( 4430):     2   10  886
[0, 0] p( 4431):     3   21  633
[0, 0] p( 4432):    16
[0, 0] p( 4433):     1   11   13   31  143  341  403
[0, 0] p( 4434):     6
[0, 0] p( 4435):     1    5  887
[0, 0] p( 4436):     4
[0, 0] p( 4437):     9  153  261
[0, 0] p( 4438):     2   14  634
[0, 0] p( 4439):     1   23  193
[0, 0] p( 4440):    24  120  888
[0, 0] p( 4441):     1
[0, 0] p( 4442):     2
[0, 0] p( 4443):     3
[0, 0] p( 4444):     4   44  404
[0, 0] p( 4445):     1    5    7   35  127  635  889
[0, 0] p( 4446):    18  234  342
[0, 0] p( 4447):     1
[0, 0] p( 4448):    32
[0, 0] p( 4449):     3
[0, 0] p( 4450):     2   10   50  178  890
[0, 0] p( 4451):     1
[0, 0] p( 4452):    12   84  636
[0, 0] p( 4453):     1   61   73
[0, 0] p( 4454):     2   34  262
[0, 0] p( 4455):    81  405  891
[0, 0] p( 4456):     8
[0, 0] p( 4457):     1
[0, 0] p( 4458):     6
[0, 0] p( 4459):     1    7   13   49   91  343  637
[0, 0] p( 4460):     4   20  892
[0, 0] p( 4461):     3
[0, 0] p( 4462):     2   46  194
[0, 0] p( 4463):     1
[0, 0] p( 4464):   144
[0, 0] p( 4465):     1    5   19   47   95  235  893
[0, 0] p( 4466):     2   14   22   58  154  406  638
[0, 0] p( 4467):     3
[0, 0] p( 4468):     4
[0, 0] p( 4469):     1   41  109
[0, 0] p( 4470):     6   30  894
[0, 0] p( 4471):     1   17  263
[0, 0] p( 4472):     8  104  344
[0, 0] p( 4473):     9   63  639
[0, 0] p( 4474):     2
[0, 0] p( 4475):     1    5   25  179  895
[0, 0] p( 4476):    12
[0, 0] p( 4477):     1   11   37  121  407
[0, 0] p( 4478):     2
[0, 0] p( 4479):     3
[0, 0] p( 4480):   128  640  896
[0, 0] p( 4481):     1
[0, 0] p( 4482):    54
[0, 0] p( 4483):     1
[0, 0] p( 4484):     4   76  236
[0, 0] p( 4485):     3   15   39   69  195  345  897
[0, 0] p( 4486):     2
[0, 0] p( 4487):     1    7  641
[0, 0] p( 4488):    24  264  408
[0, 0] p( 4489):     1   67
[0, 0] p( 4490):     2   10  898
[0, 0] p( 4491):     9
[0, 0] p( 4492):     4
[0, 0] p( 4493):     1
[0, 0] p( 4494):     6   42  642
[0, 0] p( 4495):     1    5   29   31  145  155  899
[0, 0] p( 4496):    16
[0, 0] p( 4497):     3
[0, 0] p( 4498):     2   26  346
[0, 0] p( 4499):     1   11  409
[0, 0] p( 4500):    36  180  900
[0, 0] p( 4501):     1    7  643
[0, 0] p( 4502):     2
[0, 0] p( 4503):     3   57  237
[0, 0] p( 4504):     8
[0, 0] p( 4505):     1    5   17   53   85  265  901
[0, 0] p( 4506):     6
[0, 0] p( 4507):     1
[0, 0] p( 4508):     4   28   92  196  644
[0, 0] p( 4509):    27
[0, 0] p( 4510):     2   10   22   82  110  410  902
[0, 0] p( 4511):     1   13  347
[0, 0] p( 4512):    96
[0, 0] p( 4513):     1
[0, 0] p( 4514):     2   74  122
[0, 0] p( 4515):     3   15   21  105  129  645  903
[0, 0] p( 4516):     4
[0, 0] p( 4517):     1
[0, 0] p( 4518):    18
[0, 0] p( 4519):     1
[0, 0] p( 4520):     8   40  904
[0, 0] p( 4521):     3   33  411
[0, 0] p( 4522):     2   14   34   38  238  266  646
[0, 0] p( 4523):     1
[0, 0] p( 4524):    12  156  348
[0, 0] p( 4525):     1    5   25  181  905
[0, 0] p( 4526):     2   62  146
[0, 0] p( 4527):     9
[0, 0] p( 4528):    16
[0, 0] p( 4529):     1    7  647
[0, 0] p( 4530):     6   30  906
[0, 0] p( 4531):     1   23  197
[0, 0] p( 4532):     4   44  412
[0, 0] p( 4533):     3
[0, 0] p( 4534):     2
[0, 0] p( 4535):     1    5  907
[0, 0] p( 4536):   648
[0, 0] p( 4537):     1   13  349
[0, 0] p( 4538):     2
[0, 0] p( 4539):     3   51  267
[0, 0] p( 4540):     4   20  908
[0, 0] p( 4541):     1   19  239
[0, 0] p( 4542):     6
[0, 0] p( 4543):     1    7   11   59   77  413  649
[0, 0] p( 4544):    64
[0, 0] p( 4545):     9   45  909
[0, 0] p( 4546):     2
[0, 0] p( 4547):     1
[0, 0] p( 4548):    12
[0, 0] p( 4549):     1
[0, 0] p( 4550):     2   10   14   26   50   70  130  182  350  650  910
[0, 0] p( 4551):     3  111  123
[0, 0] p( 4552):     8
[0, 0] p( 4553):     1   29  157
[0, 0] p( 4554):    18  198  414
[0, 0] p( 4555):     1    5  911
[0, 0] p( 4556):     4   68  268
[0, 0] p( 4557):     3   21   93  147  651
[0, 0] p( 4558):     2   86  106
[0, 0] p( 4559):     1   47   97
[0, 0] p( 4560):    48  240  912
[0, 0] p( 4561):     1
[0, 0] p( 4562):     2
[0, 0] p( 4563):    27  351
[0, 0] p( 4564):     4   28  652
[0, 0] p( 4565):     1    5   11   55   83  415  913
[0, 0] p( 4566):     6
[0, 0] p( 4567):     1
[0, 0] p( 4568):     8
[0, 0] p( 4569):     3
[0, 0] p( 4570):     2   10  914
[0, 0] p( 4571):     1    7  653
[0, 0] p( 4572):    36
[0, 0] p( 4573):     1   17  269
[0, 0] p( 4574):     2
[0, 0] p( 4575):     3   15   75  183  915
[0, 0] p( 4576):    32  352  416
[0, 0] p( 4577):     1   23  199
[0, 0] p( 4578):     6   42  654
[0, 0] p( 4579):     1   19  241
[0, 0] p( 4580):     4   20  916
[0, 0] p( 4581):     9
[0, 0] p( 4582):     2   58  158
[0, 0] p( 4583):     1
[0, 0] p( 4584):    24
[0, 0] p( 4585):     1    5    7   35  131  655  917
[0, 0] p( 4586):     2
[0, 0] p( 4587):     3   33  417
[0, 0] p( 4588):     4  124  148
[0, 0] p( 4589):     1   13  353
[0, 0] p( 4590):    54  270  918
[0, 0] p( 4591):     1
[0, 0] p( 4592):    16  112  656
[0, 0] p( 4593):     3
[0, 0] p( 4594):     2
[0, 0] p( 4595):     1    5  919
[0, 0] p( 4596):    12
[0, 0] p( 4597):     1
[0, 0] p( 4598):     2   22   38  242  418
[0, 0] p( 4599):     9   63  657
[0, 0] p( 4600):     8   40  184  200  920
[0, 0] p( 4601):     1   43  107
[0, 0] p( 4602):     6   78  354
[0, 0] p( 4603):     1
[0, 0] p( 4604):     4
[0, 0] p( 4605):     3   15  921
[0, 0] p( 4606):     2   14   94   98  658
[0, 0] p( 4607):     1   17  271
[0, 0] p( 4608): 
[0, 0] p( 4609):     1   11  419
[0, 0] p( 4610):     2   10  922
[0, 0] p( 4611):     3   87  159
[0, 0] p( 4612):     4
[0, 0] p( 4613):     1    7  659
[0, 0] p( 4614):     6
[0, 0] p( 4615):     1    5   13   65   71  355  923
[0, 0] p( 4616):     8
[0, 0] p( 4617):   243
[0, 0] p( 4618):     2
[0, 0] p( 4619):     1   31  149
[0, 0] p( 4620):    12   60   84  132  420  660  924
[0, 0] p( 4621):     1
[0, 0] p( 4622):     2
[0, 0] p( 4623):     3   69  201
[0, 0] p( 4624):    16  272
[0, 0] p( 4625):     1    5   25   37  125  185  925
[0, 0] p( 4626):    18
[0, 0] p( 4627):     1    7  661
[0, 0] p( 4628):     4   52  356
[0, 0] p( 4629):     3
[0, 0] p( 4630):     2   10  926
[0, 0] p( 4631):     1   11  421
[0, 0] p( 4632):    24
[0, 0] p( 4633):     1   41  113
[0, 0] p( 4634):     2   14  662
[0, 0] p( 4635):     9   45  927
[0, 0] p( 4636):     4   76  244
[0, 0] p( 4637):     1
[0, 0] p( 4638):     6
[0, 0] p( 4639):     1
[0, 0] p( 4640):    32  160  928
[0, 0] p( 4641):     3   21   39   51  273  357  663
[0, 0] p( 4642):     2   22  422
[0, 0] p( 4643):     1
[0, 0] p( 4644):   108
[0, 0] p( 4645):     1    5  929
[0, 0] p( 4646):     2   46  202
[0, 0] p( 4647):     3
[0, 0] p( 4648):     8   56  664
[0, 0] p( 4649):     1
[0, 0] p( 4650):     6   30  150  186  930
[0, 0] p( 4651):     1
[0, 0] p( 4652):     4
[0, 0] p( 4653):     9   99  423
[0, 0] p( 4654):     2   26  358
[0, 0] p( 4655):     1    5    7   19   35   49   95  133  245  665  931
[0, 0] p( 4656):    48
[0, 0] p( 4657):     1
[0, 0] p( 4658):     2   34  274
[0, 0] p( 4659):     3
[0, 0] p( 4660):     4   20  932
[0, 0] p( 4661):     1   59   79
[0, 0] p( 4662):    18  126  666
[0, 0] p( 4663):     1
[0, 0] p( 4664):     8   88  424
[0, 0] p( 4665):     3   15  933
[0, 0] p( 4666):     2
[0, 0] p( 4667):     1   13  359
[0, 0] p( 4668):    12
[0, 0] p( 4669):     1    7   23   29  161  203  667
[0, 0] p( 4670):     2   10  934
[0, 0] p( 4671):    27
[0, 0] p( 4672):    64
[0, 0] p( 4673):     1
[0, 0] p( 4674):     6  114  246
[0, 0] p( 4675):     1    5   11   17   25   55   85  187  275  425  935
[0, 0] p( 4676):     4   28  668
[0, 0] p( 4677):     3
[0, 0] p( 4678):     2
[0, 0] p( 4679):     1
[0, 0] p( 4680):    72  360  936
[0, 0] p( 4681):     1   31  151
[0, 0] p( 4682):     2
[0, 0] p( 4683):     3   21  669
[0, 0] p( 4684):     4
[0, 0] p( 4685):     1    5  937
[0, 0] p( 4686):     6   66  426
[0, 0] p( 4687):     1   43  109
[0, 0] p( 4688):    16
[0, 0] p( 4689):     9
[0, 0] p( 4690):     2   10   14   70  134  670  938
[0, 0] p( 4691):     1
[0, 0] p( 4692):    12  204  276
[0, 0] p( 4693):     1   13   19  247  361
[0, 0] p( 4694):     2
[0, 0] p( 4695):     3   15  939
[0, 0] p( 4696):     8
[0, 0] p( 4697):     1    7   11   61   77  427  671
[0, 0] p( 4698):   162
[0, 0] p( 4699):     1   37  127
[0, 0] p( 4700):     4   20  100  188  940
[0, 0] p( 4701):     3
[0, 0] p( 4702):     2
[0, 0] p( 4703):     1
[0, 0] p( 4704):    96  672
[0, 0] p( 4705):     1    5  941
[0, 0] p( 4706):     2   26  362
[0, 0] p( 4707):     9
[0, 0] p( 4708):     4   44  428
[0, 0] p( 4709):     1   17  277
[0, 0] p( 4710):     6   30  942
[0, 0] p( 4711):     1    7  673
[0, 0] p( 4712):     8  152  248
[0, 0] p( 4713):     3
[0, 0] p( 4714):     2
[0, 0] p( 4715):     1    5   23   41  115  205  943
[0, 0] p( 4716):    36
[0, 0] p( 4717):     1   53   89
[0, 0] p( 4718):     2   14  674
[0, 0] p( 4719):     3   33   39  363  429
[0, 0] p( 4720):    16   80  944
[0, 0] p( 4721):     1
[0, 0] p( 4722):     6
[0, 0] p( 4723):     1
[0, 0] p( 4724):     4
[0, 0] p( 4725):    27  135  189  675  945
[0, 0] p( 4726):     2   34  278
[0, 0] p( 4727):     1   29  163
[0, 0] p( 4728):    24
[0, 0] p( 4729):     1
[0, 0] p( 4730):     2   10   22   86  110  430  946
[0, 0] p( 4731):     3   57  249
[0, 0] p( 4732):     4   28   52  364  676
[0, 0] p( 4733):     1
[0, 0] p( 4734):    18
[0, 0] p( 4735):     1    5  947
[0, 0] p( 4736):   128
[0, 0] p( 4737):     3
[0, 0] p( 4738):     2   46  206
[0, 0] p( 4739):     1    7  677
[0, 0] p( 4740):    12   60  948
[0, 0] p( 4741):     1   11  431
[0, 0] p( 4742):     2
[0, 0] p( 4743):     9  153  279
[0, 0] p( 4744):     8
[0, 0] p( 4745):     1    5   13   65   73  365  949
[0, 0] p( 4746):     6   42  678
[0, 0] p( 4747):     1   47  101
[0, 0] p( 4748):     4
[0, 0] p( 4749):     3
[0, 0] p( 4750):     2   10   38   50  190  250  950
[0, 0] p( 4751):     1
[0, 0] p( 4752):   432
[0, 0] p( 4753):     1    7   49   97  679
[0, 0] p( 4754):     2
[0, 0] p( 4755):     3   15  951
[0, 0] p( 4756):     4  116  164
[0, 0] p( 4757):     1   67   71
[0, 0] p( 4758):     6   78  366
[0, 0] p( 4759):     1
[0, 0] p( 4760):     8   40   56  136  280  680  952
[0, 0] p( 4761):     9  207
[0, 0] p( 4762):     2
[0, 0] p( 4763):     1   11  433
[0, 0] p( 4764):    12
[0, 0] p( 4765):     1    5  953
[0, 0] p( 4766):     2
[0, 0] p( 4767):     3   21  681
[0, 0] p( 4768):    32
[0, 0] p( 4769):     1   19  251
[0, 0] p( 4770):    18   90  954
[0, 0] p( 4771):     1   13  367
[0, 0] p( 4772):     4
[0, 0] p( 4773):     3  111  129
[0, 0] p( 4774):     2   14   22   62  154  434  682
[0, 0] p( 4775):     1    5   25  191  955
[0, 0] p( 4776):    24
[0, 0] p( 4777):     1   17  281
[0, 0] p( 4778):     2
[0, 0] p( 4779):    81
[0, 0] p( 4780):     4   20  956
[0, 0] p( 4781):     1    7  683
[0, 0] p( 4782):     6
[0, 0] p( 4783):     1
[0, 0] p( 4784):    16  208  368
[0, 0] p( 4785):     3   15   33   87  165  435  957
[0, 0] p( 4786):     2
[0, 0] p( 4787):     1
[0, 0] p( 4788):    36  252  684
[0, 0] p( 4789):     1
[0, 0] p( 4790):     2   10  958
[0, 0] p( 4791):     3
[0, 0] p( 4792):     8
[0, 0] p( 4793):     1
[0, 0] p( 4794):     6  102  282
[0, 0] p( 4795):     1    5    7   35  137  685  959
[0, 0] p( 4796):     4   44  436
[0, 0] p( 4797):     9  117  369
[0, 0] p( 4798):     2
[0, 0] p( 4799):     1
[0, 0] p( 4800):   192  960
[0, 0] p( 4801):     1
[0, 0] p( 4802):     2   14   98  686
[0, 0] p( 4803):     3
[0, 0] p( 4804):     4
[0, 0] p( 4805):     1    5   31  155  961
[0, 0] p( 4806):    54
[0, 0] p( 4807):     1   11   19   23  209  253  437
[0, 0] p( 4808):     8
[0, 0] p( 4809):     3   21  687
[0, 0] p( 4810):     2   10   26   74  130  370  962
[0, 0] p( 4811):     1   17  283
[0, 0] p( 4812):    12
[0, 0] p( 4813):     1
[0, 0] p( 4814):     2   58  166
[0, 0] p( 4815):     9   45  963
[0, 0] p( 4816):    16  112  688
[0, 0] p( 4817):     1
[0, 0] p( 4818):     6   66  438
[0, 0] p( 4819):     1   61   79
[0, 0] p( 4820):     4   20  964
[0, 0] p( 4821):     3
[0, 0] p( 4822):     2
[0, 0] p( 4823):     1    7   13   53   91  371  689
[0, 0] p( 4824):    72
[0, 0] p( 4825):     1    5   25  193  965
[0, 0] p( 4826):     2   38  254
[0, 0] p( 4827):     3
[0, 0] p( 4828):     4   68  284
[0, 0] p( 4829):     1   11  439
[0, 0] p( 4830):     6   30   42  138  210  690  966
[0, 0] p( 4831):     1
[0, 0] p( 4832):    32
[0, 0] p( 4833):    27
[0, 0] p( 4834):     2
[0, 0] p( 4835):     1    5  967
[0, 0] p( 4836):    12  156  372
[0, 0] p( 4837):     1    7  691
[0, 0] p( 4838):     2   82  118
[0, 0] p( 4839):     3
[0, 0] p( 4840):     8   40   88  440  968
[0, 0] p( 4841):     1   47  103
[0, 0] p( 4842):    18
[0, 0] p( 4843):     1   29  167
[0, 0] p( 4844):     4   28  692
[0, 0] p( 4845):     3   15   51   57  255  285  969
[0, 0] p( 4846):     2
[0, 0] p( 4847):     1   37  131
[0, 0] p( 4848):    48
[0, 0] p( 4849):     1   13  373
[0, 0] p( 4850):     2   10   50  194  970
[0, 0] p( 4851):     9   63   99  441  693
[0, 0] p( 4852):     4
[0, 0] p( 4853):     1   23  211
[0, 0] p( 4854):     6
[0, 0] p( 4855):     1    5  971
[0, 0] p( 4856):     8
[0, 0] p( 4857):     3
[0, 0] p( 4858):     2   14  694
[0, 0] p( 4859):     1   43  113
[0, 0] p( 4860):   972
[0, 0] p( 4861):     1
[0, 0] p( 4862):     2   22   26   34  286  374  442
[0, 0] p( 4863):     3
[0, 0] p( 4864):   256
[0, 0] p( 4865):     1    5    7   35  139  695  973
[0, 0] p( 4866):     6
[0, 0] p( 4867):     1   31  157
[0, 0] p( 4868):     4
[0, 0] p( 4869):     9
[0, 0] p( 4870):     2   10  974
[0, 0] p( 4871):     1
[0, 0] p( 4872):    24  168  696
[0, 0] p( 4873):     1   11  443
[0, 0] p( 4874):     2
[0, 0] p( 4875):     3   15   39   75  195  375  975
[0, 0] p( 4876):     4   92  212
[0, 0] p( 4877):     1
[0, 0] p( 4878):    18
[0, 0] p( 4879):     1    7   17   41  119  287  697
[0, 0] p( 4880):    16   80  976
[0, 0] p( 4881):     3
[0, 0] p( 4882):     2
[0, 0] p( 4883):     1   19  257
[0, 0] p( 4884):    12  132  444
[0, 0] p( 4885):     1    5  977
[0, 0] p( 4886):     2   14  698
[0, 0] p( 4887):    27
[0, 0] p( 4888):     8  104  376
[0, 0] p( 4889):     1
[0, 0] p( 4890):     6   30  978
[0, 0] p( 4891):     1   67   73
[0, 0] p( 4892):     4
[0, 0] p( 4893):     3   21  699
[0, 0] p( 4894):     2
[0, 0] p( 4895):     1    5   11   55   89  445  979
[0, 0] p( 4896):   288
[0, 0] p( 4897):     1   59   83
[0, 0] p( 4898):     2   62  158
[0, 0] p( 4899):     3   69  213
[0, 0] p( 4900):     4   20   28  100  140  196  700  980
[0, 0] p( 4901):     1   13   29  169  377
[0, 0] p( 4902):     6  114  258
[0, 0] p( 4903):     1
[0, 0] p( 4904):     8
[0, 0] p( 4905):     9   45  981
[0, 0] p( 4906):     2   22  446
[0, 0] p( 4907):     1    7  701
[0, 0] p( 4908):    12
[0, 0] p( 4909):     1
[0, 0] p( 4910):     2   10  982
[0, 0] p( 4911):     3
[0, 0] p( 4912):    16
[0, 0] p( 4913):     1   17  289
[0, 0] p( 4914):    54  378  702
[0, 0] p( 4915):     1    5  983
[0, 0] p( 4916):     4
[0, 0] p( 4917):     3   33  447
[0, 0] p( 4918):     2
[0, 0] p( 4919):     1
[0, 0] p( 4920):    24  120  984
[0, 0] p( 4921):     1    7   19   37  133  259  703
[0, 0] p( 4922):     2   46  214
[0, 0] p( 4923):     9
[0, 0] p( 4924):     4
[0, 0] p( 4925):     1    5   25  197  985
[0, 0] p( 4926):     6
[0, 0] p( 4927):     1   13  379
[0, 0] p( 4928):    64  448  704
[0, 0] p( 4929):     3   93  159
[0, 0] p( 4930):     2   10   34   58  170  290  986
[0, 0] p( 4931):     1
[0, 0] p( 4932):    36
[0, 0] p( 4933):     1
[0, 0] p( 4934):     2
[0, 0] p( 4935):     3   15   21  105  141  705  987
[0, 0] p( 4936):     8
[0, 0] p( 4937):     1
[0, 0] p( 4938):     6
[0, 0] p( 4939):     1   11  449
[0, 0] p( 4940):     4   20   52   76  260  380  988
[0, 0] p( 4941):    81
[0, 0] p( 4942):     2   14  706
[0, 0] p( 4943):     1
[0, 0] p( 4944):    48
[0, 0] p( 4945):     1    5   23   43  115  215  989
[0, 0] p( 4946):     2
[0, 0] p( 4947):     3   51  291
[0, 0] p( 4948):     4
[0, 0] p( 4949):     1    7   49  101  707
[0, 0] p( 4950):    18   90  198  450  990
[0, 0] p( 4951):     1
[0, 0] p( 4952):     8
[0, 0] p( 4953):     3   39  381
[0, 0] p( 4954):     2
[0, 0] p( 4955):     1    5  991
[0, 0] p( 4956):    12   84  708
[0, 0] p( 4957):     1
[0, 0] p( 4958):     2   74  134
[0, 0] p( 4959):     9  171  261
[0, 0] p( 4960):    32  160  992
[0, 0] p( 4961):     1   11   41  121  451
[0, 0] p( 4962):     6
[0, 0] p( 4963):     1    7  709
[0, 0] p( 4964):     4   68  292
[0, 0] p( 4965):     3   15  993
[0, 0] p( 4966):     2   26  382
[0, 0] p( 4967):     1
[0, 0] p( 4968):   216
[0, 0] p( 4969):     1
[0, 0] p( 4970):     2   10   14   70  142  710  994
[0, 0] p( 4971):     3
[0, 0] p( 4972):     4   44  452
[0, 0] p( 4973):     1
[0, 0] p( 4974):     6
[0, 0] p( 4975):     1    5   25  199  995
[0, 0] p( 4976):    16
[0, 0] p( 4977):     9   63  711
[0, 0] p( 4978):     2   38  262
[0, 0] p( 4979):     1   13  383
[0, 0] p( 4980):    12   60  996
[0, 0] p( 4981):     1   17  293
[0, 0] p( 4982):     2   94  106
[0, 0] p( 4983):     3   33  453
[0, 0] p( 4984):     8   56  712
[0, 0] p( 4985):     1    5  997
[0, 0] p( 4986):    18
[0, 0] p( 4987):     1
[0, 0] p( 4988):     4  116  172
[0, 0] p( 4989):     3
[0, 0] p( 4990):     2   10  998
[0, 0] p( 4991):     1    7   23   31  161  217  713
[0, 0] p( 4992):   384
[0, 0] p( 4993):     1
[0, 0] p( 4994):     2   22  454
[0, 0] p( 4995):    27  135  999
[0, 0] p( 4996):     4
[0, 0] p( 4997):     1   19  263
[0, 0] p( 4998):     6   42  102  294  714
[0, 0] p( 4999):     1
[0, 0] p( 5000):     8   40  200 1000
[0, 0] p( 5001):     3
[0, 0] p( 5002):     2   82  122
[0, 0] p( 5003):     1
[0, 0] p( 5004):    36
[0, 0] p( 5005):     1    5    7   11   13   35   55   65   77   91  143  385  455  715 1001
[0, 0] p( 5006):     2
[0, 0] p( 5007):     3
[0, 0] p( 5008):    16
[0, 0] p( 5009):     1
[0, 0] p( 5010):     6   30 1002
[0, 0] p( 5011):     1
[0, 0] p( 5012):     4   28  716
[0, 0] p( 5013):     9
[0, 0] p( 5014):     2   46  218
[0, 0] p( 5015):     1    5   17   59   85  295 1003
[0, 0] p( 5016):    24  264  456
[0, 0] p( 5017):     1   29  173
[0, 0] p( 5018):     2   26  386
[0, 0] p( 5019):     3   21  717
[0, 0] p( 5020):     4   20 1004
[0, 0] p( 5021):     1
[0, 0] p( 5022):   162
[0, 0] p( 5023):     1
[0, 0] p( 5024):    32
[0, 0] p( 5025):     3   15   75  201 1005
[0, 0] p( 5026):     2   14  718
[0, 0] p( 5027):     1   11  457
[0, 0] p( 5028):    12
[0, 0] p( 5029):     1   47  107
[0, 0] p( 5030):     2   10 1006
[0, 0] p( 5031):     9  117  387
[0, 0] p( 5032):     8  136  296
[0, 0] p( 5033):     1    7  719
[0, 0] p( 5034):     6
[0, 0] p( 5035):     1    5   19   53   95  265 1007
[0, 0] p( 5036):     4
[0, 0] p( 5037):     3   69  219
[0, 0] p( 5038):     2   22  458
[0, 0] p( 5039):     1
[0, 0] p( 5040):   144  720 1008
[0, 0] p( 5041):     1   71
[0, 0] p( 5042):     2
[0, 0] p( 5043):     3  123
[0, 0] p( 5044):     4   52  388
[0, 0] p( 5045):     1    5 1009
[0, 0] p( 5046):     6  174
[0, 0] p( 5047):     1    7   49  103  721
[0, 0] p( 5048):     8
[0, 0] p( 5049):    27  297  459
[0, 0] p( 5050):     2   10   50  202 1010
[0, 0] p( 5051):     1
[0, 0] p( 5052):    12
[0, 0] p( 5053):     1   31  163
[0, 0] p( 5054):     2   14   38  266  722
[0, 0] p( 5055):     3   15 1011
[0, 0] p( 5056):    64
[0, 0] p( 5057):     1   13  389
[0, 0] p( 5058):    18
[0, 0] p( 5059):     1
[0, 0] p( 5060):     4   20   44   92  220  460 1012
[0, 0] p( 5061):     3   21  723
[0, 0] p( 5062):     2
[0, 0] p( 5063):     1   61   83
[0, 0] p( 5064):    24
[0, 0] p( 5065):     1    5 1013
[0, 0] p( 5066):     2   34  298
[0, 0] p( 5067):     9
[0, 0] p( 5068):     4   28  724
[0, 0] p( 5069):     1   37  137
[0, 0] p( 5070):     6   30   78  390 1014
[0, 0] p( 5071):     1   11  461
[0, 0] p( 5072):    16
[0, 0] p( 5073):     3   57  267
[0, 0] p( 5074):     2   86  118
[0, 0] p( 5075):     1    5    7   25   29   35  145  175  203  725 1015
[0, 0] p( 5076):   108
[0, 0] p( 5077):     1
[0, 0] p( 5078):     2
[0, 0] p( 5079):     3
[0, 0] p( 5080):     8   40 1016
[0, 0] p( 5081):     1
[0, 0] p( 5082):     6   42   66  462  726
[0, 0] p( 5083):     1   13   17   23  221  299  391
[0, 0] p( 5084):     4  124  164
[0, 0] p( 5085):     9   45 1017
[0, 0] p( 5086):     2
[0, 0] p( 5087):     1
[0, 0] p( 5088):    96
[0, 0] p( 5089):     1    7  727
[0, 0] p( 5090):     2   10 1018
[0, 0] p( 5091):     3
[0, 0] p( 5092):     4   76  268
[0, 0] p( 5093):     1   11  463
[0, 0] p( 5094):    18
[0, 0] p( 5095):     1    5 1019
[0, 0] p( 5096):     8   56  104  392  728
[0, 0] p( 5097):     3
[0, 0] p( 5098):     2
[0, 0] p( 5099):     1
[0, 0] p( 5100):    12   60  204  300 1020
[0, 0] p( 5101):     1
[0, 0] p( 5102):     2
[0, 0] p( 5103):   729
[0, 0] p( 5104):    16  176  464
[0, 0] p( 5105):     1    5 1021
[0, 0] p( 5106):     6  138  222
[0, 0] p( 5107):     1
[0, 0] p( 5108):     4
[0, 0] p( 5109):     3   39  393
[0, 0] p( 5110):     2   10   14   70  146  730 1022
[0, 0] p( 5111):     1   19  269
[0, 0] p( 5112):    72
[0, 0] p( 5113):     1
[0, 0] p( 5114):     2
[0, 0] p( 5115):     3   15   33   93  165  465 1023
[0, 0] p( 5116):     4
[0, 0] p( 5117):     1    7   17   43  119  301  731
[0, 0] p( 5118):     6
[0, 0] p( 5119):     1
[0, 0] p( 5120):  1024
[0, 0] p( 5121):     9
[0, 0] p( 5122):     2   26  394
[0, 0] p( 5123):     1   47  109
[0, 0] p( 5124):    12   84  732
[0, 0] p( 5125):     1    5   25   41  125  205 1025
[0, 0] p( 5126):     2   22  466
[0, 0] p( 5127):     3
[0, 0] p( 5128):     8
[0, 0] p( 5129):     1   23  223
[0, 0] p( 5130):    54  270 1026
[0, 0] p( 5131):     1    7  733
[0, 0] p( 5132):     4
[0, 0] p( 5133):     3   87  177
[0, 0] p( 5134):     2   34  302
[0, 0] p( 5135):     1    5   13   65   79  395 1027
[0, 0] p( 5136):    48
[0, 0] p( 5137):     1   11  467
[0, 0] p( 5138):     2   14  734
[0, 0] p( 5139):     9
[0, 0] p( 5140):     4   20 1028
[0, 0] p( 5141):     1   53   97
[0, 0] p( 5142):     6
[0, 0] p( 5143):     1   37  139
[0, 0] p( 5144):     8
[0, 0] p( 5145):     3   15   21  105  147  735 1029
[0, 0] p( 5146):     2   62  166
[0, 0] p( 5147):     1
[0, 0] p( 5148):    36  396  468
[0, 0] p( 5149):     1   19  271
[0, 0] p( 5150):     2   10   50  206 1030
[0, 0] p( 5151):     3   51  303
[0, 0] p( 5152):    32  224  736
[0, 0] p( 5153):     1
[0, 0] p( 5154):     6
[0, 0] p( 5155):     1    5 1031
[0, 0] p( 5156):     4
[0, 0] p( 5157):    27
[0, 0] p( 5158):     2
[0, 0] p( 5159):     1    7   11   67   77  469  737
[0, 0] p( 5160):    24  120 1032
[0, 0] p( 5161):     1   13  397
[0, 0] p( 5162):     2   58  178
[0, 0] p( 5163):     3
[0, 0] p( 5164):     4
[0, 0] p( 5165):     1    5 1033
[0, 0] p( 5166):    18  126  738
[0, 0] p( 5167):     1
[0, 0] p( 5168):    16  272  304
[0, 0] p( 5169):     3
[0, 0] p( 5170):     2   10   22   94  110  470 1034
[0, 0] p( 5171):     1
[0, 0] p( 5172):    12
[0, 0] p( 5173):     1    7  739
[0, 0] p( 5174):     2   26  398
[0, 0] p( 5175):     9   45  207  225 1035
[0, 0] p( 5176):     8
[0, 0] p( 5177):     1   31  167
[0, 0] p( 5178):     6
[0, 0] p( 5179):     1
[0, 0] p( 5180):     4   20   28  140  148  740 1036
[0, 0] p( 5181):     3   33  471
[0, 0] p( 5182):     2
[0, 0] p( 5183):     1   71   73
[0, 0] p( 5184): 
[0, 0] p( 5185):     1    5   17   61   85  305 1037
[0, 0] p( 5186):     2
[0, 0] p( 5187):     3   21   39   57  273  399  741
[0, 0] p( 5188):     4
[0, 0] p( 5189):     1
[0, 0] p( 5190):     6   30 1038
[0, 0] p( 5191):     1   29  179
[0, 0] p( 5192):     8   88  472
[0, 0] p( 5193):     9
[0, 0] p( 5194):     2   14   98  106  742
[0, 0] p( 5195):     1    5 1039
[0, 0] p( 5196):    12
[0, 0] p( 5197):     1
[0, 0] p( 5198):     2   46  226
[0, 0] p( 5199):     3
[0, 0] p( 5200):    16   80  208  400 1040
[0, 0] p( 5201):     1    7  743
[0, 0] p( 5202):    18  306
[0, 0] p( 5203):     1   11   43  121  473
[0, 0] p( 5204):     4
[0, 0] p( 5205):     3   15 1041
[0, 0] p( 5206):     2   38  274
[0, 0] p( 5207):     1   41  127
[0, 0] p( 5208):    24  168  744
[0, 0] p( 5209):     1
[0, 0] p( 5210):     2   10 1042
[0, 0] p( 5211):    27
[0, 0] p( 5212):     4
[0, 0] p( 5213):     1   13  401
[0, 0] p( 5214):     6   66  474
[0, 0] p( 5215):     1    5    7   35  149  745 1043
[0, 0] p( 5216):    32
[0, 0] p( 5217):     3  111  141
[0, 0] p( 5218):     2
[0, 0] p( 5219):     1   17  307
[0, 0] p( 5220):    36  180 1044
[0, 0] p( 5221):     1   23  227
[0, 0] p( 5222):     2   14  746
[0, 0] p( 5223):     3
[0, 0] p( 5224):     8
[0, 0] p( 5225):     1    5   11   19   25   55   95  209  275  475 1045
[0, 0] p( 5226):     6   78  402
[0, 0] p( 5227):     1
[0, 0] p( 5228):     4
[0, 0] p( 5229):     9   63  747
[0, 0] p( 5230):     2   10 1046
[0, 0] p( 5231):     1
[0, 0] p( 5232):    48
[0, 0] p( 5233):     1
[0, 0] p( 5234):     2
[0, 0] p( 5235):     3   15 1047
[0, 0] p( 5236):     4   28   44   68  308  476  748
[0, 0] p( 5237):     1
[0, 0] p( 5238):    54
[0, 0] p( 5239):     1   13   31  169  403
[0, 0] p( 5240):     8   40 1048
[0, 0] p( 5241):     3
[0, 0] p( 5242):     2
[0, 0] p( 5243):     1    7   49  107  749
[0, 0] p( 5244):    12  228  276
[0, 0] p( 5245):     1    5 1049
[0, 0] p( 5246):     2   86  122
[0, 0] p( 5247):     9   99  477
[0, 0] p( 5248):   128
[0, 0] p( 5249):     1   29  181
[0, 0] p( 5250):     6   30   42  150  210  750 1050
[0, 0] p( 5251):     1   59   89
[0, 0] p( 5252):     4   52  404
[0, 0] p( 5253):     3   51  309
[0, 0] p( 5254):     2   74  142
[0, 0] p( 5255):     1    5 1051
[0, 0] p( 5256):    72
[0, 0] p( 5257):     1    7  751
[0, 0] p( 5258):     2   22  478
[0, 0] p( 5259):     3
[0, 0] p( 5260):     4   20 1052
[0, 0] p( 5261):     1
[0, 0] p( 5262):     6
[0, 0] p( 5263):     1   19  277
[0, 0] p( 5264):    16  112  752
[0, 0] p( 5265):    81  405 1053
[0, 0] p( 5266):     2
[0, 0] p( 5267):     1   23  229
[0, 0] p( 5268):    12
[0, 0] p( 5269):     1   11  479
[0, 0] p( 5270):     2   10   34   62  170  310 1054
[0, 0] p( 5271):     3   21  753
[0, 0] p( 5272):     8
[0, 0] p( 5273):     1
[0, 0] p( 5274):    18
[0, 0] p( 5275):     1    5   25  211 1055
[0, 0] p( 5276):     4
[0, 0] p( 5277):     3
[0, 0] p( 5278):     2   14   26   58  182  406  754
[0, 0] p( 5279):     1
[0, 0] p( 5280):    96  480 1056
[0, 0] p( 5281):     1
[0, 0] p( 5282):     2   38  278
[0, 0] p( 5283):     9
[0, 0] p( 5284):     4
[0, 0] p( 5285):     1    5    7   35  151  755 1057
[0, 0] p( 5286):     6
[0, 0] p( 5287):     1   17  311
[0, 0] p( 5288):     8
[0, 0] p( 5289):     3  123  129
[0, 0] p( 5290):     2   10   46  230 1058
[0, 0] p( 5291):     1   11   13   37  143  407  481
[0, 0] p( 5292):   108  756
[0, 0] p( 5293):     1   67   79
[0, 0] p( 5294):     2
[0, 0] p( 5295):     3   15 1059
[0, 0] p( 5296):    16
[0, 0] p( 5297):     1
[0, 0] p( 5298):     6
[0, 0] p( 5299):     1    7  757
[0, 0] p( 5300):     4   20  100  212 1060
[0, 0] p( 5301):     9  171  279
[0, 0] p( 5302):     2   22  482
[0, 0] p( 5303):     1
[0, 0] p( 5304):    24  312  408
[0, 0] p( 5305):     1    5 1061
[0, 0] p( 5306):     2   14  758
[0, 0] p( 5307):     3   87  183
[0, 0] p( 5308):     4
[0, 0] p( 5309):     1
[0, 0] p( 5310):    18   90 1062
[0, 0] p( 5311):     1   47  113
[0, 0] p( 5312):    64
[0, 0] p( 5313):     3   21   33   69  231  483  759
[0, 0] p( 5314):     2
[0, 0] p( 5315):     1    5 1063
[0, 0] p( 5316):    12
[0, 0] p( 5317):     1   13  409
[0, 0] p( 5318):     2
[0, 0] p( 5319):    27
[0, 0] p( 5320):     8   40   56  152  280  760 1064
[0, 0] p( 5321):     1   17  313
[0, 0] p( 5322):     6
[0, 0] p( 5323):     1
[0, 0] p( 5324):     4   44  484
[0, 0] p( 5325):     3   15   75  213 1065
[0, 0] p( 5326):     2
[0, 0] p( 5327):     1    7  761
[0, 0] p( 5328):   144
[0, 0] p( 5329):     1   73
[0, 0] p( 5330):     2   10   26   82  130  410 1066
[0, 0] p( 5331):     3
[0, 0] p( 5332):     4  124  172
[0, 0] p( 5333):     1
[0, 0] p( 5334):     6   42  762
[0, 0] p( 5335):     1    5   11   55   97  485 1067
[0, 0] p( 5336):     8  184  232
[0, 0] p( 5337):     9
[0, 0] p( 5338):     2   34  314
[0, 0] p( 5339):     1   19  281
[0, 0] p( 5340):    12   60 1068
[0, 0] p( 5341):     1    7   49  109  763
[0, 0] p( 5342):     2
[0, 0] p( 5343):     3   39  411
[0, 0] p( 5344):    32
[0, 0] p( 5345):     1    5 1069
[0, 0] p( 5346):   486
[0, 0] p( 5347):     1
[0, 0] p( 5348):     4   28  764
[0, 0] p( 5349):     3
[0, 0] p( 5350):     2   10   50  214 1070
[0, 0] p( 5351):     1
[0, 0] p( 5352):    24
[0, 0] p( 5353):     1   53  101
[0, 0] p( 5354):     2
[0, 0] p( 5355):     9   45   63  153  315  765 1071
[0, 0] p( 5356):     4   52  412
[0, 0] p( 5357):     1   11  487
[0, 0] p( 5358):     6  114  282
[0, 0] p( 5359):     1   23  233
[0, 0] p( 5360):    16   80 1072
[0, 0] p( 5361):     3
[0, 0] p( 5362):     2   14  766
[0, 0] p( 5363):     1   31  173
[0, 0] p( 5364):    36
[0, 0] p( 5365):     1    5   29   37  145  185 1073
[0, 0] p( 5366):     2
[0, 0] p( 5367):     3
[0, 0] p( 5368):     8   88  488
[0, 0] p( 5369):     1    7   13   59   91  413  767
[0, 0] p( 5370):     6   30 1074
[0, 0] p( 5371):     1   41  131
[0, 0] p( 5372):     4   68  316
[0, 0] p( 5373):    27
[0, 0] p( 5374):     2
[0, 0] p( 5375):     1    5   25   43  125  215 1075
[0, 0] p( 5376):   768
[0, 0] p( 5377):     1   19  283
[0, 0] p( 5378):     2
[0, 0] p( 5379):     3   33  489
[0, 0] p( 5380):     4   20 1076
[0, 0] p( 5381):     1
[0, 0] p( 5382):    18  234  414
[0, 0] p( 5383):     1    7  769
[0, 0] p( 5384):     8
[0, 0] p( 5385):     3   15 1077
[0, 0] p( 5386):     2
[0, 0] p( 5387):     1
[0, 0] p( 5388):    12
[0, 0] p( 5389):     1   17  317
[0, 0] p( 5390):     2   10   14   22   70   98  110  154  490  770 1078
[0, 0] p( 5391):     9
[0, 0] p( 5392):    16
[0, 0] p( 5393):     1
[0, 0] p( 5394):     6  174  186
[0, 0] p( 5395):     1    5   13   65   83  415 1079
[0, 0] p( 5396):     4   76  284
[0, 0] p( 5397):     3   21  771
[0, 0] p( 5398):     2
[0, 0] p( 5399):     1
[0, 0] p( 5400):   216 1080
[0, 0] p( 5401):     1   11  491
[0, 0] p( 5402):     2   74  146
[0, 0] p( 5403):     3
[0, 0] p( 5404):     4   28  772
[0, 0] p( 5405):     1    5   23   47  115  235 1081
[0, 0] p( 5406):     6  102  318
[0, 0] p( 5407):     1
[0, 0] p( 5408):    32  416
[0, 0] p( 5409):     9
[0, 0] p( 5410):     2   10 1082
[0, 0] p( 5411):     1    7  773
[0, 0] p( 5412):    12  132  492
[0, 0] p( 5413):     1
[0, 0] p( 5414):     2
[0, 0] p( 5415):     3   15   57  285 1083
[0, 0] p( 5416):     8
[0, 0] p( 5417):     1
[0, 0] p( 5418):    18  126  774
[0, 0] p( 5419):     1
[0, 0] p( 5420):     4   20 1084
[0, 0] p( 5421):     3   39  417
[0, 0] p( 5422):     2
[0, 0] p( 5423):     1   11   17   29  187  319  493
[0, 0] p( 5424):    48
[0, 0] p( 5425):     1    5    7   25   31   35  155  175  217  775 1085
[0, 0] p( 5426):     2
[0, 0] p( 5427):    81
[0, 0] p( 5428):     4   92  236
[0, 0] p( 5429):     1   61   89
[0, 0] p( 5430):     6   30 1086
[0, 0] p( 5431):     1
[0, 0] p( 5432):     8   56  776
[0, 0] p( 5433):     3
[0, 0] p( 5434):     2   22   26   38  286  418  494
[0, 0] p( 5435):     1    5 1087
[0, 0] p( 5436):    36
[0, 0] p( 5437):     1
[0, 0] p( 5438):     2
[0, 0] p( 5439):     3   21  111  147  777
[0, 0] p( 5440):    64  320 1088
[0, 0] p( 5441):     1
[0, 0] p( 5442):     6
[0, 0] p( 5443):     1
[0, 0] p( 5444):     4
[0, 0] p( 5445):     9   45   99  495 1089
[0, 0] p( 5446):     2   14  778
[0, 0] p( 5447):     1   13  419
[0, 0] p( 5448):    24
[0, 0] p( 5449):     1
[0, 0] p( 5450):     2   10   50  218 1090
[0, 0] p( 5451):     3   69  237
[0, 0] p( 5452):     4  116  188
[0, 0] p( 5453):     1    7   19   41  133  287  779
[0, 0] p( 5454):    54
[0, 0] p( 5455):     1    5 1091
[0, 0] p( 5456):    16  176  496
[0, 0] p( 5457):     3   51  321
[0, 0] p( 5458):     2
[0, 0] p( 5459):     1   53  103
[0, 0] p( 5460):    12   60   84  156  420  780 1092
[0, 0] p( 5461):     1   43  127
[0, 0] p( 5462):     2
[0, 0] p( 5463):     9
[0, 0] p( 5464):     8
[0, 0] p( 5465):     1    5 1093
[0, 0] p( 5466):     6
[0, 0] p( 5467):     1    7   11   71   77  497  781
[0, 0] p( 5468):     4
[0, 0] p( 5469):     3
[0, 0] p( 5470):     2   10 1094
[0, 0] p( 5471):     1
[0, 0] p( 5472):   288
[0, 0] p( 5473):     1   13  421
[0, 0] p( 5474):     2   14   34   46  238  322  782
[0, 0] p( 5475):     3   15   75  219 1095
[0, 0] p( 5476):     4  148
[0, 0] p( 5477):     1
[0, 0] p( 5478):     6   66  498
[0, 0] p( 5479):     1
[0, 0] p( 5480):     8   40 1096
[0, 0] p( 5481):    27  189  783
[0, 0] p( 5482):     2
[0, 0] p( 5483):     1
[0, 0] p( 5484):    12
[0, 0] p( 5485):     1    5 1097
[0, 0] p( 5486):     2   26  422
[0, 0] p( 5487):     3   93  177
[0, 0] p( 5488):    16  112  784
[0, 0] p( 5489):     1   11  499
[0, 0] p( 5490):    18   90 1098
[0, 0] p( 5491):     1   17   19  289  323
[0, 0] p( 5492):     4
[0, 0] p( 5493):     3
[0, 0] p( 5494):     2   82  134
[0, 0] p( 5495):     1    5    7   35  157  785 1099
[0, 0] p( 5496):    24
[0, 0] p( 5497):     1   23  239
[0, 0] p( 5498):     2
[0, 0] p( 5499):     9  117  423
[0, 0] p( 5500):     4   20   44  100  220  500 1100
[0, 0] p( 5501):     1
[0, 0] p( 5502):     6   42  786
[0, 0] p( 5503):     1
[0, 0] p( 5504):   128
[0, 0] p( 5505):     3   15 1101
[0, 0] p( 5506):     2
[0, 0] p( 5507):     1
[0, 0] p( 5508):   324
[0, 0] p( 5509):     1    7  787
[0, 0] p( 5510):     2   10   38   58  190  290 1102
[0, 0] p( 5511):     3   33  501
[0, 0] p( 5512):     8  104  424
[0, 0] p( 5513):     1   37  149
[0, 0] p( 5514):     6
[0, 0] p( 5515):     1    5 1103
[0, 0] p( 5516):     4   28  788
[0, 0] p( 5517):     9
[0, 0] p( 5518):     2   62  178
[0, 0] p( 5519):     1
[0, 0] p( 5520):    48  240 1104
[0, 0] p( 5521):     1
[0, 0] p( 5522):     2   22  502
[0, 0] p( 5523):     3   21  789
[0, 0] p( 5524):     4
[0, 0] p( 5525):     1    5   13   17   25   65   85  221  325  425 1105
[0, 0] p( 5526):    18
[0, 0] p( 5527):     1
[0, 0] p( 5528):     8
[0, 0] p( 5529):     3   57  291
[0, 0] p( 5530):     2   10   14   70  158  790 1106
[0, 0] p( 5531):     1
[0, 0] p( 5532):    12
[0, 0] p( 5533):     1   11  503
[0, 0] p( 5534):     2
[0, 0] p( 5535):    27  135 1107
[0, 0] p( 5536):    32
[0, 0] p( 5537):     1    7   49  113  791
[0, 0] p( 5538):     6   78  426
[0, 0] p( 5539):     1   29  191
[0, 0] p( 5540):     4   20 1108
[0, 0] p( 5541):     3
[0, 0] p( 5542):     2   34  326
[0, 0] p( 5543):     1   23  241
[0, 0] p( 5544):    72  504  792
[0, 0] p( 5545):     1    5 1109
[0, 0] p( 5546):     2   94  118
[0, 0] p( 5547):     3  129
[0, 0] p( 5548):     4   76  292
[0, 0] p( 5549):     1   31  179
[0, 0] p( 5550):     6   30  150  222 1110
[0, 0] p( 5551):     1    7   13   61   91  427  793
[0, 0] p( 5552):    16
[0, 0] p( 5553):     9
[0, 0] p( 5554):     2
[0, 0] p( 5555):     1    5   11   55  101  505 1111
[0, 0] p( 5556):    12
[0, 0] p( 5557):     1
[0, 0] p( 5558):     2   14  794
[0, 0] p( 5559):     3   51  327
[0, 0] p( 5560):     8   40 1112
[0, 0] p( 5561):     1   67   83
[0, 0] p( 5562):    54
[0, 0] p( 5563):     1
[0, 0] p( 5564):     4   52  428
[0, 0] p( 5565):     3   15   21  105  159  795 1113
[0, 0] p( 5566):     2   22   46  242  506
[0, 0] p( 5567):     1   19  293
[0, 0] p( 5568):   192
[0, 0] p( 5569):     1
[0, 0] p( 5570):     2   10 1114
[0, 0] p( 5571):     9
[0, 0] p( 5572):     4   28  796
[0, 0] p( 5573):     1
[0, 0] p( 5574):     6
[0, 0] p( 5575):     1    5   25  223 1115
[0, 0] p( 5576):     8  136  328
[0, 0] p( 5577):     3   33   39  429  507
[0, 0] p( 5578):     2
[0, 0] p( 5579):     1    7  797
[0, 0] p( 5580):    36  180 1116
[0, 0] p( 5581):     1
[0, 0] p( 5582):     2
[0, 0] p( 5583):     3
[0, 0] p( 5584):    16
[0, 0] p( 5585):     1    5 1117
[0, 0] p( 5586):     6   42  114  294  798
[0, 0] p( 5587):     1   37  151
[0, 0] p( 5588):     4   44  508
[0, 0] p( 5589):   243
[0, 0] p( 5590):     2   10   26   86  130  430 1118
[0, 0] p( 5591):     1
[0, 0] p( 5592):    24
[0, 0] p( 5593):     1    7   17   47  119  329  799
[0, 0] p( 5594):     2
[0, 0] p( 5595):     3   15 1119
[0, 0] p( 5596):     4
[0, 0] p( 5597):     1   29  193
[0, 0] p( 5598):    18
[0, 0] p( 5599):     1   11  509
[0, 0] p( 5600):    32  160  224  800 1120
[0, 0] p( 5601):     3
[0, 0] p( 5602):     2
[0, 0] p( 5603):     1   13  431
[0, 0] p( 5604):    12
[0, 0] p( 5605):     1    5   19   59   95  295 1121
[0, 0] p( 5606):     2
[0, 0] p( 5607):     9   63  801
[0, 0] p( 5608):     8
[0, 0] p( 5609):     1   71   79
[0, 0] p( 5610):     6   30   66  102  330  510 1122
[0, 0] p( 5611):     1   31  181
[0, 0] p( 5612):     4   92  244
[0, 0] p( 5613):     3
[0, 0] p( 5614):     2   14  802
[0, 0] p( 5615):     1    5 1123
[0, 0] p( 5616):   432
[0, 0] p( 5617):     1   41  137
[0, 0] p( 5618):     2  106
[0, 0] p( 5619):     3
[0, 0] p( 5620):     4   20 1124
[0, 0] p( 5621):     1    7   11   73   77  511  803
[0, 0] p( 5622):     6
[0, 0] p( 5623):     1
[0, 0] p( 5624):     8  152  296
[0, 0] p( 5625):     9   45  225 1125
[0, 0] p( 5626):     2   58  194
[0, 0] p( 5627):     1   17  331
[0, 0] p( 5628):    12   84  804
[0, 0] p( 5629):     1   13  433
[0, 0] p( 5630):     2   10 1126
[0, 0] p( 5631):     3
[0, 0] p( 5632):   512
[0, 0] p( 5633):     1   43  131
[0, 0] p( 5634):    18
[0, 0] p( 5635):     1    5    7   23   35   49  115  161  245  805 1127
[0, 0] p( 5636):     4
[0, 0] p( 5637):     3
[0, 0] p( 5638):     2
[0, 0] p( 5639):     1
[0, 0] p( 5640):    24  120 1128
[0, 0] p( 5641):     1
[0, 0] p( 5642):     2   14   26   62  182  434  806
[0, 0] p( 5643):    27  297  513
[0, 0] p( 5644):     4   68  332
[0, 0] p( 5645):     1    5 1129
[0, 0] p( 5646):     6
[0, 0] p( 5647):     1
[0, 0] p( 5648):    16
[0, 0] p( 5649):     3   21  807
[0, 0] p( 5650):     2   10   50  226 1130
[0, 0] p( 5651):     1
[0, 0] p( 5652):    36
[0, 0] p( 5653):     1
[0, 0] p( 5654):     2   22  514
[0, 0] p( 5655):     3   15   39   87  195  435 1131
[0, 0] p( 5656):     8   56  808
[0, 0] p( 5657):     1
[0, 0] p( 5658):     6  138  246
[0, 0] p( 5659):     1
[0, 0] p( 5660):     4   20 1132
[0, 0] p( 5661):     9  153  333
[0, 0] p( 5662):     2   38  298
[0, 0] p( 5663):     1    7  809
[0, 0] p( 5664):    96
[0, 0] p( 5665):     1    5   11   55  103  515 1133
[0, 0] p( 5666):     2
[0, 0] p( 5667):     3
[0, 0] p( 5668):     4   52  436
[0, 0] p( 5669):     1
[0, 0] p( 5670):   162  810 1134
[0, 0] p( 5671):     1   53  107
[0, 0] p( 5672):     8
[0, 0] p( 5673):     3   93  183
[0, 0] p( 5674):     2
[0, 0] p( 5675):     1    5   25  227 1135
[0, 0] p( 5676):    12  132  516
[0, 0] p( 5677):     1    7  811
[0, 0] p( 5678):     2   34  334
[0, 0] p( 5679):     9
[0, 0] p( 5680):    16   80 1136
[0, 0] p( 5681):     1   13   19   23  247  299  437
[0, 0] p( 5682):     6
[0, 0] p( 5683):     1
[0, 0] p( 5684):     4   28  116  196  812
[0, 0] p( 5685):     3   15 1137
[0, 0] p( 5686):     2
[0, 0] p( 5687):     1   11   47  121  517
[0, 0] p( 5688):    72
[0, 0] p( 5689):     1
[0, 0] p( 5690):     2   10 1138
[0, 0] p( 5691):     3   21  813
[0, 0] p( 5692):     4
[0, 0] p( 5693):     1
[0, 0] p( 5694):     6   78  438
[0, 0] p( 5695):     1    5   17   67   85  335 1139
[0, 0] p( 5696):    64
[0, 0] p( 5697):    27
[0, 0] p( 5698):     2   14   22   74  154  518  814
[0, 0] p( 5699):     1   41  139
[0, 0] p( 5700):    12   60  228  300 1140
[0, 0] p( 5701):     1
[0, 0] p( 5702):     2
[0, 0] p( 5703):     3
[0, 0] p( 5704):     8  184  248
[0, 0] p( 5705):     1    5    7   35  163  815 1141
[0, 0] p( 5706):    18
[0, 0] p( 5707):     1   13  439
[0, 0] p( 5708):     4
[0, 0] p( 5709):     3   33  519
[0, 0] p( 5710):     2   10 1142
[0, 0] p( 5711):     1
[0, 0] p( 5712):    48  336  816
[0, 0] p( 5713):     1   29  197
[0, 0] p( 5714):     2
[0, 0] p( 5715):     9   45 1143
[0, 0] p( 5716):     4
[0, 0] p( 5717):     1
[0, 0] p( 5718):     6
[0, 0] p( 5719):     1    7   19   43  133  301  817
[0, 0] p( 5720):     8   40   88  104  440  520 1144
[0, 0] p( 5721):     3
[0, 0] p( 5722):     2
[0, 0] p( 5723):     1   59   97
[0, 0] p( 5724):   108
[0, 0] p( 5725):     1    5   25  229 1145
[0, 0] p( 5726):     2   14  818
[0, 0] p( 5727):     3   69  249
[0, 0] p( 5728):    32
[0, 0] p( 5729):     1   17  337
[0, 0] p( 5730):     6   30 1146
[0, 0] p( 5731):     1   11  521
[0, 0] p( 5732):     4
[0, 0] p( 5733):     9   63  117  441  819
[0, 0] p( 5734):     2   94  122
[0, 0] p( 5735):     1    5   31   37  155  185 1147
[0, 0] p( 5736):    24
[0, 0] p( 5737):     1
[0, 0] p( 5738):     2   38  302
[0, 0] p( 5739):     3
[0, 0] p( 5740):     4   20   28  140  164  820 1148
[0, 0] p( 5741):     1
[0, 0] p( 5742):    18  198  522
[0, 0] p( 5743):     1
[0, 0] p( 5744):    16
[0, 0] p( 5745):     3   15 1149
[0, 0] p( 5746):     2   26   34  338  442
[0, 0] p( 5747):     1    7  821
[0, 0] p( 5748):    12
[0, 0] p( 5749):     1
[0, 0] p( 5750):     2   10   46   50  230  250 1150
[0, 0] p( 5751):    81
[0, 0] p( 5752):     8
[0, 0] p( 5753):     1   11  523
[0, 0] p( 5754):     6   42  822
[0, 0] p( 5755):     1    5 1151
[0, 0] p( 5756):     4
[0, 0] p( 5757):     3   57  303
[0, 0] p( 5758):     2
[0, 0] p( 5759):     1   13  443
[0, 0] p( 5760):  1152
[0, 0] p( 5761):     1    7  823
[0, 0] p( 5762):     2   86  134
[0, 0] p( 5763):     3   51  339
[0, 0] p( 5764):     4   44  524
[0, 0] p( 5765):     1    5 1153
[0, 0] p( 5766):     6  186
[0, 0] p( 5767):     1   73   79
[0, 0] p( 5768):     8   56  824
[0, 0] p( 5769):     9
[0, 0] p( 5770):     2   10 1154
[0, 0] p( 5771):     1   29  199
[0, 0] p( 5772):    12  156  444
[0, 0] p( 5773):     1   23  251
[0, 0] p( 5774):     2
[0, 0] p( 5775):     3   15   21   33   75  105  165  231  525  825 1155
[0, 0] p( 5776):    16  304
[0, 0] p( 5777):     1   53  109
[0, 0] p( 5778):    54
[0, 0] p( 5779):     1
[0, 0] p( 5780):     4   20   68  340 1156
[0, 0] p( 5781):     3  123  141
[0, 0] p( 5782):     2   14   98  118  826
[0, 0] p( 5783):     1
[0, 0] p( 5784):    24
[0, 0] p( 5785):     1    5   13   65   89  445 1157
[0, 0] p( 5786):     2   22  526
[0, 0] p( 5787):     9
[0, 0] p( 5788):     4
[0, 0] p( 5789):     1    7  827
[0, 0] p( 5790):     6   30 1158
[0, 0] p( 5791):     1
[0, 0] p( 5792):    32
[0, 0] p( 5793):     3
[0, 0] p( 5794):     2
[0, 0] p( 5795):     1    5   19   61   95  305 1159
[0, 0] p( 5796):    36  252  828
[0, 0] p( 5797):     1   11   17   31  187  341  527
[0, 0] p( 5798):     2   26  446
[0, 0] p( 5799):     3
[0, 0] p( 5800):     8   40  200  232 1160
[0, 0] p( 5801):     1
[0, 0] p( 5802):     6
[0, 0] p( 5803):     1    7  829
[0, 0] p( 5804):     4
[0, 0] p( 5805):    27  135 1161
[0, 0] p( 5806):     2
[0, 0] p( 5807):     1
[0, 0] p( 5808):    48  528
[0, 0] p( 5809):     1   37  157
[0, 0] p( 5810):     2   10   14   70  166  830 1162
[0, 0] p( 5811):     3   39  447
[0, 0] p( 5812):     4
[0, 0] p( 5813):     1
[0, 0] p( 5814):    18  306  342
[0, 0] p( 5815):     1    5 1163
[0, 0] p( 5816):     8
[0, 0] p( 5817):     3   21  831
[0, 0] p( 5818):     2
[0, 0] p( 5819):     1   11   23  253  529
[0, 0] p( 5820):    12   60 1164
[0, 0] p( 5821):     1
[0, 0] p( 5822):     2   82  142
[0, 0] p( 5823):     9
[0, 0] p( 5824):    64  448  832
[0, 0] p( 5825):     1    5   25  233 1165
[0, 0] p( 5826):     6
[0, 0] p( 5827):     1
[0, 0] p( 5828):     4  124  188
[0, 0] p( 5829):     3   87  201
[0, 0] p( 5830):     2   10   22  106  110  530 1166
[0, 0] p( 5831):     1    7   17   49  119  343  833
[0, 0] p( 5832): 
[0, 0] p( 5833):     1   19  307
[0, 0] p( 5834):     2
[0, 0] p( 5835):     3   15 1167
[0, 0] p( 5836):     4
[0, 0] p( 5837):     1   13  449
[0, 0] p( 5838):     6   42  834
[0, 0] p( 5839):     1
[0, 0] p( 5840):    16   80 1168
[0, 0] p( 5841):     9   99  531
[0, 0] p( 5842):     2   46  254
[0, 0] p( 5843):     1
[0, 0] p( 5844):    12
[0, 0] p( 5845):     1    5    7   35  167  835 1169
[0, 0] p( 5846):     2   74  158
[0, 0] p( 5847):     3
[0, 0] p( 5848):     8  136  344
[0, 0] p( 5849):     1
[0, 0] p( 5850):    18   90  234  450 1170
[0, 0] p( 5851):     1
[0, 0] p( 5852):     4   28   44   76  308  532  836
[0, 0] p( 5853):     3
[0, 0] p( 5854):     2
[0, 0] p( 5855):     1    5 1171
[0, 0] p( 5856):    96
[0, 0] p( 5857):     1
[0, 0] p( 5858):     2   58  202
[0, 0] p( 5859):    27  189  837
[0, 0] p( 5860):     4   20 1172
[0, 0] p( 5861):     1
[0, 0] p( 5862):     6
[0, 0] p( 5863):     1   11   13   41  143  451  533
[0, 0] p( 5864):     8
[0, 0] p( 5865):     3   15   51   69  255  345 1173
[0, 0] p( 5866):     2   14  838
[0, 0] p( 5867):     1
[0, 0] p( 5868):    36
[0, 0] p( 5869):     1
[0, 0] p( 5870):     2   10 1174
[0, 0] p( 5871):     3   57  309
[0, 0] p( 5872):    16
[0, 0] p( 5873):     1    7  839
[0, 0] p( 5874):     6   66  534
[0, 0] p( 5875):     1    5   25   47  125  235 1175
[0, 0] p( 5876):     4   52  452
[0, 0] p( 5877):     9
[0, 0] p( 5878):     2
[0, 0] p( 5879):     1
[0, 0] p( 5880):    24  120  168  840 1176
[0, 0] p( 5881):     1
[0, 0] p( 5882):     2   34  346
[0, 0] p( 5883):     3  111  159
[0, 0] p( 5884):     4
[0, 0] p( 5885):     1    5   11   55  107  535 1177
[0, 0] p( 5886):    54
[0, 0] p( 5887):     1    7   29  203  841
[0, 0] p( 5888):   256
[0, 0] p( 5889):     3   39  453
[0, 0] p( 5890):     2   10   38   62  190  310 1178
[0, 0] p( 5891):     1   43  137
[0, 0] p( 5892):    12
[0, 0] p( 5893):     1   71   83
[0, 0] p( 5894):     2   14  842
[0, 0] p( 5895):     9   45 1179
[0, 0] p( 5896):     8   88  536
[0, 0] p( 5897):     1
[0, 0] p( 5898):     6
[0, 0] p( 5899):     1   17  347
[0, 0] p( 5900):     4   20  100  236 1180
[0, 0] p( 5901):     3   21  843
[0, 0] p( 5902):     2   26  454
[0, 0] p( 5903):     1
[0, 0] p( 5904):   144
[0, 0] p( 5905):     1    5 1181
[0, 0] p( 5906):     2
[0, 0] p( 5907):     3   33  537
[0, 0] p( 5908):     4   28  844
[0, 0] p( 5909):     1   19  311
[0, 0] p( 5910):     6   30 1182
[0, 0] p( 5911):     1   23  257
[0, 0] p( 5912):     8
[0, 0] p( 5913):    81
[0, 0] p( 5914):     2
[0, 0] p( 5915):     1    5    7   13   35   65   91  169  455  845 1183
[0, 0] p( 5916):    12  204  348
[0, 0] p( 5917):     1   61   97
[0, 0] p( 5918):     2   22  538
[0, 0] p( 5919):     3
[0, 0] p( 5920):    32  160 1184
[0, 0] p( 5921):     1   31  191
[0, 0] p( 5922):    18  126  846
[0, 0] p( 5923):     1
[0, 0] p( 5924):     4
[0, 0] p( 5925):     3   15   75  237 1185
[0, 0] p( 5926):     2
[0, 0] p( 5927):     1
[0, 0] p( 5928):    24  312  456
[0, 0] p( 5929):     1    7   11   49   77  121  539  847
[0, 0] p( 5930):     2   10 1186
[0, 0] p( 5931):     9
[0, 0] p( 5932):     4
[0, 0] p( 5933):     1   17  349
[0, 0] p( 5934):     6  138  258
[0, 0] p( 5935):     1    5 1187
[0, 0] p( 5936):    16  112  848
[0, 0] p( 5937):     3
[0, 0] p( 5938):     2
[0, 0] p( 5939):     1
[0, 0] p( 5940):   108  540 1188
[0, 0] p( 5941):     1   13  457
[0, 0] p( 5942):     2
[0, 0] p( 5943):     3   21  849
[0, 0] p( 5944):     8
[0, 0] p( 5945):     1    5   29   41  145  205 1189
[0, 0] p( 5946):     6
[0, 0] p( 5947):     1   19  313
[0, 0] p( 5948):     4
[0, 0] p( 5949):     9
[0, 0] p( 5950):     2   10   14   34   50   70  170  238  350  850 1190
[0, 0] p( 5951):     1   11  541
[0, 0] p( 5952):   192
[0, 0] p( 5953):     1
[0, 0] p( 5954):     2   26  458
[0, 0] p( 5955):     3   15 1191
[0, 0] p( 5956):     4
[0, 0] p( 5957):     1    7   23   37  161  259  851
[0, 0] p( 5958):    18
[0, 0] p( 5959):     1   59  101
[0, 0] p( 5960):     8   40 1192
[0, 0] p( 5961):     3
[0, 0] p( 5962):     2   22  542
[0, 0] p( 5963):     1   67   89
[0, 0] p( 5964):    12   84  852
[0, 0] p( 5965):     1    5 1193
[0, 0] p( 5966):     2   38  314
[0, 0] p( 5967):    27  351  459
[0, 0] p( 5968):    16
[0, 0] p( 5969):     1   47  127
[0, 0] p( 5970):     6   30 1194
[0, 0] p( 5971):     1    7  853
[0, 0] p( 5972):     4
[0, 0] p( 5973):     3   33  543
[0, 0] p( 5974):     2   58  206
[0, 0] p( 5975):     1    5   25  239 1195
[0, 0] p( 5976):    72
[0, 0] p( 5977):     1   43  139
[0, 0] p( 5978):     2   14   98  122  854
[0, 0] p( 5979):     3
[0, 0] p( 5980):     4   20   52   92  260  460 1196
[0, 0] p( 5981):     1
[0, 0] p( 5982):     6
[0, 0] p( 5983):     1   31  193
[0, 0] p( 5984):    32  352  544
[0, 0] p( 5985):     9   45   63  171  315  855 1197
[0, 0] p( 5986):     2   82  146
[0, 0] p( 5987):     1
[0, 0] p( 5988):    12
[0, 0] p( 5989):     1   53  113
[0, 0] p( 5990):     2   10 1198
[0, 0] p( 5991):     3
[0, 0] p( 5992):     8   56  856
[0, 0] p( 5993):     1   13  461
[0, 0] p( 5994):   162
[0, 0] p( 5995):     1    5   11   55  109  545 1199
[0, 0] p( 5996):     4
[0, 0] p( 5997):     3
[0, 0] p( 5998):     2
[0, 0] p( 5999):     1    7  857
[0, 0] p( 6000):    48  240 1200
[0, 0] p( 6001):     1   17  353
[0, 0] p( 6002):     2
[0, 0] p( 6003):     9  207  261
[0, 0] p( 6004):     4   76  316
[0, 0] p( 6005):     1    5 1201
[0, 0] p( 6006):     6   42   66   78  462  546  858
[0, 0] p( 6007):     1
[0, 0] p( 6008):     8
[0, 0] p( 6009):     3
[0, 0] p( 6010):     2   10 1202
[0, 0] p( 6011):     1
[0, 0] p( 6012):    36
[0, 0] p( 6013):     1    7  859
[0, 0] p( 6014):     2   62  194
[0, 0] p( 6015):     3   15 1203
[0, 0] p( 6016):   128
[0, 0] p( 6017):     1   11  547
[0, 0] p( 6018):     6  102  354
[0, 0] p( 6019):     1   13  463
[0, 0] p( 6020):     4   20   28  140  172  860 1204
[0, 0] p( 6021):    27
[0, 0] p( 6022):     2
[0, 0] p( 6023):     1   19  317
[0, 0] p( 6024):    24
[0, 0] p( 6025):     1    5   25  241 1205
[0, 0] p( 6026):     2   46  262
[0, 0] p( 6027):     3   21  123  147  861
[0, 0] p( 6028):     4   44  548
[0, 0] p( 6029):     1
[0, 0] p( 6030):    18   90 1206
[0, 0] p( 6031):     1   37  163
[0, 0] p( 6032):    16  208  464
[0, 0] p( 6033):     3
[0, 0] p( 6034):     2   14  862
[0, 0] p( 6035):     1    5   17   71   85  355 1207
[0, 0] p( 6036):    12
[0, 0] p( 6037):     1
[0, 0] p( 6038):     2
[0, 0] p( 6039):     9   99  549
[0, 0] p( 6040):     8   40 1208
[0, 0] p( 6041):     1    7  863
[0, 0] p( 6042):     6  114  318
[0, 0] p( 6043):     1
[0, 0] p( 6044):     4
[0, 0] p( 6045):     3   15   39   93  195  465 1209
[0, 0] p( 6046):     2
[0, 0] p( 6047):     1
[0, 0] p( 6048):   864
[0, 0] p( 6049):     1   23  263
[0, 0] p( 6050):     2   10   22   50  110  242  550 1210
[0, 0] p( 6051):     3
[0, 0] p( 6052):     4   68  356
[0, 0] p( 6053):     1
[0, 0] p( 6054):     6
[0, 0] p( 6055):     1    5    7   35  173  865 1211
[0, 0] p( 6056):     8
[0, 0] p( 6057):     9
[0, 0] p( 6058):     2   26  466
[0, 0] p( 6059):     1   73   83
[0, 0] p( 6060):    12   60 1212
[0, 0] p( 6061):     1   11   19   29  209  319  551
[0, 0] p( 6062):     2   14  866
[0, 0] p( 6063):     3  129  141
[0, 0] p( 6064):    16
[0, 0] p( 6065):     1    5 1213
[0, 0] p( 6066):    18
[0, 0] p( 6067):     1
[0, 0] p( 6068):     4  148  164
[0, 0] p( 6069):     3   21   51  357  867
[0, 0] p( 6070):     2   10 1214
[0, 0] p( 6071):     1   13  467
[0, 0] p( 6072):    24  264  552
[0, 0] p( 6073):     1
[0, 0] p( 6074):     2
[0, 0] p( 6075):   243 1215
[0, 0] p( 6076):     4   28  124  196  868
[0, 0] p( 6077):     1   59  103
[0, 0] p( 6078):     6
[0, 0] p( 6079):     1
[0, 0] p( 6080):    64  320 1216
[0, 0] p( 6081):     3
[0, 0] p( 6082):     2
[0, 0] p( 6083):     1    7   11   77   79  553  869
[0, 0] p( 6084):    36  468
[0, 0] p( 6085):     1    5 1217
[0, 0] p( 6086):     2   34  358
[0, 0] p( 6087):     3
[0, 0] p( 6088):     8
[0, 0] p( 6089):     1
[0, 0] p( 6090):     6   30   42  174  210  870 1218
[0, 0] p( 6091):     1
[0, 0] p( 6092):     4
[0, 0] p( 6093):     9
[0, 0] p( 6094):     2   22  554
[0, 0] p( 6095):     1    5   23   53  115  265 1219
[0, 0] p( 6096):    48
[0, 0] p( 6097):     1    7   13   67   91  469  871
[0, 0] p( 6098):     2
[0, 0] p( 6099):     3   57  321
[0, 0] p( 6100):     4   20  100  244 1220
[0, 0] p( 6101):     1
[0, 0] p( 6102):    54
[0, 0] p( 6103):     1   17  359
[0, 0] p( 6104):     8   56  872
[0, 0] p( 6105):     3   15   33  111  165  555 1221
[0, 0] p( 6106):     2   86  142
[0, 0] p( 6107):     1   31  197
[0, 0] p( 6108):    12
[0, 0] p( 6109):     1   41  149
[0, 0] p( 6110):     2   10   26   94  130  470 1222
[0, 0] p( 6111):     9   63  873
[0, 0] p( 6112):    32
[0, 0] p( 6113):     1
[0, 0] p( 6114):     6
[0, 0] p( 6115):     1    5 1223
[0, 0] p( 6116):     4   44  556
[0, 0] p( 6117):     3
[0, 0] p( 6118):     2   14   38   46  266  322  874
[0, 0] p( 6119):     1   29  211
[0, 0] p( 6120):    72  360 1224
[0, 0] p( 6121):     1
[0, 0] p( 6122):     2
[0, 0] p( 6123):     3   39  471
[0, 0] p( 6124):     4
[0, 0] p( 6125):     1    5    7   25   35   49  125  175  245  875 1225
[0, 0] p( 6126):     6
[0, 0] p( 6127):     1   11  557
[0, 0] p( 6128):    16
[0, 0] p( 6129):    27
[0, 0] p( 6130):     2   10 1226
[0, 0] p( 6131):     1
[0, 0] p( 6132):    12   84  876
[0, 0] p( 6133):     1
[0, 0] p( 6134):     2
[0, 0] p( 6135):     3   15 1227
[0, 0] p( 6136):     8  104  472
[0, 0] p( 6137):     1   17   19  323  361
[0, 0] p( 6138):    18  198  558
[0, 0] p( 6139):     1    7  877
[0, 0] p( 6140):     4   20 1228
[0, 0] p( 6141):     3   69  267
[0, 0] p( 6142):     2   74  166
[0, 0] p( 6143):     1
[0, 0] p( 6144): 
[0, 0] p( 6145):     1    5 1229
[0, 0] p( 6146):     2   14  878
[0, 0] p( 6147):     9
[0, 0] p( 6148):     4  116  212
[0, 0] p( 6149):     1   11   13   43  143  473  559
[0, 0] p( 6150):     6   30  150  246 1230
[0, 0] p( 6151):     1
[0, 0] p( 6152):     8
[0, 0] p( 6153):     3   21  879
[0, 0] p( 6154):     2   34  362
[0, 0] p( 6155):     1    5 1231
[0, 0] p( 6156):   324
[0, 0] p( 6157):     1   47  131
[0, 0] p( 6158):     2
[0, 0] p( 6159):     3
[0, 0] p( 6160):    16   80  112  176  560  880 1232
[0, 0] p( 6161):     1   61  101
[0, 0] p( 6162):     6   78  474
[0, 0] p( 6163):     1
[0, 0] p( 6164):     4   92  268
[0, 0] p( 6165):     9   45 1233
[0, 0] p( 6166):     2
[0, 0] p( 6167):     1    7  881
[0, 0] p( 6168):    24
[0, 0] p( 6169):     1   31  199
[0, 0] p( 6170):     2   10 1234
[0, 0] p( 6171):     3   33   51  363  561
[0, 0] p( 6172):     4
[0, 0] p( 6173):     1
[0, 0] p( 6174):    18  126  882
[0, 0] p( 6175):     1    5   13   19   25   65   95  247  325  475 1235
[0, 0] p( 6176):    32
[0, 0] p( 6177):     3   87  213
[0, 0] p( 6178):     2
[0, 0] p( 6179):     1   37  167
[0, 0] p( 6180):    12   60 1236
[0, 0] p( 6181):     1    7  883
[0, 0] p( 6182):     2   22  562
[0, 0] p( 6183):    27
[0, 0] p( 6184):     8
[0, 0] p( 6185):     1    5 1237
[0, 0] p( 6186):     6
[0, 0] p( 6187):     1   23  269
[0, 0] p( 6188):     4   28   52   68  364  476  884
[0, 0] p( 6189):     3
[0, 0] p( 6190):     2   10 1238
[0, 0] p( 6191):     1   41  151
[0, 0] p( 6192):   144
[0, 0] p( 6193):     1   11  563
[0, 0] p( 6194):     2   38  326
[0, 0] p( 6195):     3   15   21  105  177  885 1239
[0, 0] p( 6196):     4
[0, 0] p( 6197):     1
[0, 0] p( 6198):     6
[0, 0] p( 6199):     1
[0, 0] p( 6200):     8   40  200  248 1240
[0, 0] p( 6201):     9  117  477
[0, 0] p( 6202):     2   14  886
[0, 0] p( 6203):     1
[0, 0] p( 6204):    12  132  564
[0, 0] p( 6205):     1    5   17   73   85  365 1241
[0, 0] p( 6206):     2   58  214
[0, 0] p( 6207):     3
[0, 0] p( 6208):    64
[0, 0] p( 6209):     1    7  887
[0, 0] p( 6210):    54  270 1242
[0, 0] p( 6211):     1
[0, 0] p( 6212):     4
[0, 0] p( 6213):     3   57  327
[0, 0] p( 6214):     2   26  478
[0, 0] p( 6215):     1    5   11   55  113  565 1243
[0, 0] p( 6216):    24  168  888
[0, 0] p( 6217):     1
[0, 0] p( 6218):     2
[0, 0] p( 6219):     9
[0, 0] p( 6220):     4   20 1244
[0, 0] p( 6221):     1
[0, 0] p( 6222):     6  102  366
[0, 0] p( 6223):     1    7   49  127  889
[0, 0] p( 6224):    16
[0, 0] p( 6225):     3   15   75  249 1245
[0, 0] p( 6226):     2   22  566
[0, 0] p( 6227):     1   13  479
[0, 0] p( 6228):    36
[0, 0] p( 6229):     1
[0, 0] p( 6230):     2   10   14   70  178  890 1246
[0, 0] p( 6231):     3   93  201
[0, 0] p( 6232):     8  152  328
[0, 0] p( 6233):     1   23  271
[0, 0] p( 6234):     6
[0, 0] p( 6235):     1    5   29   43  145  215 1247
[0, 0] p( 6236):     4
[0, 0] p( 6237):    81  567  891
[0, 0] p( 6238):     2
[0, 0] p( 6239):     1   17  367
[0, 0] p( 6240):    96  480 1248
[0, 0] p( 6241):     1   79
[0, 0] p( 6242):     2
[0, 0] p( 6243):     3
[0, 0] p( 6244):     4   28  892
[0, 0] p( 6245):     1    5 1249
[0, 0] p( 6246):    18
[0, 0] p( 6247):     1
[0, 0] p( 6248):     8   88  568
[0, 0] p( 6249):     3
[0, 0] p( 6250):     2   10   50  250 1250
[0, 0] p( 6251):     1    7   19   47  133  329  893
[0, 0] p( 6252):    12
[0, 0] p( 6253):     1   13   37  169  481
[0, 0] p( 6254):     2  106  118
[0, 0] p( 6255):     9   45 1251
[0, 0] p( 6256):    16  272  368
[0, 0] p( 6257):     1
[0, 0] p( 6258):     6   42  894
[0, 0] p( 6259):     1   11  569
[0, 0] p( 6260):     4   20 1252
[0, 0] p( 6261):     3
[0, 0] p( 6262):     2   62  202
[0, 0] p( 6263):     1
[0, 0] p( 6264):   216
[0, 0] p( 6265):     1    5    7   35  179  895 1253
[0, 0] p( 6266):     2   26  482
[0, 0] p( 6267):     3
[0, 0] p( 6268):     4
[0, 0] p( 6269):     1
[0, 0] p( 6270):     6   30   66  114  330  570 1254
[0, 0] p( 6271):     1
[0, 0] p( 6272):   128  896
[0, 0] p( 6273):     9  153  369
[0, 0] p( 6274):     2
[0, 0] p( 6275):     1    5   25  251 1255
[0, 0] p( 6276):    12
[0, 0] p( 6277):     1
[0, 0] p( 6278):     2   86  146
[0, 0] p( 6279):     3   21   39   69  273  483  897
[0, 0] p( 6280):     8   40 1256
[0, 0] p( 6281):     1   11  571
[0, 0] p( 6282):    18
[0, 0] p( 6283):     1   61  103
[0, 0] p( 6284):     4
[0, 0] p( 6285):     3   15 1257
[0, 0] p( 6286):     2   14  898
[0, 0] p( 6287):     1
[0, 0] p( 6288):    48
[0, 0] p( 6289):     1   19  331
[0, 0] p( 6290):     2   10   34   74  170  370 1258
[0, 0] p( 6291):    27
[0, 0] p( 6292):     4   44   52  484  572
[0, 0] p( 6293):     1    7   29   31  203  217  899
[0, 0] p( 6294):     6
[0, 0] p( 6295):     1    5 1259
[0, 0] p( 6296):     8
[0, 0] p( 6297):     3
[0, 0] p( 6298):     2   94  134
[0, 0] p( 6299):     1
[0, 0] p( 6300):    36  180  252  900 1260
[0, 0] p( 6301):     1
[0, 0] p( 6302):     2   46  274
[0, 0] p( 6303):     3   33  573
[0, 0] p( 6304):    32
[0, 0] p( 6305):     1    5   13   65   97  485 1261
[0, 0] p( 6306):     6
[0, 0] p( 6307):     1    7   17   53  119  371  901
[0, 0] p( 6308):     4   76  332
[0, 0] p( 6309):     9
[0, 0] p( 6310):     2   10 1262
[0, 0] p( 6311):     1
[0, 0] p( 6312):    24
[0, 0] p( 6313):     1   59  107
[0, 0] p( 6314):     2   14   22   82  154  574  902
[0, 0] p( 6315):     3   15 1263
[0, 0] p( 6316):     4
[0, 0] p( 6317):     1
[0, 0] p( 6318):   486
[0, 0] p( 6319):     1   71   89
[0, 0] p( 6320):    16   80 1264
[0, 0] p( 6321):     3   21  129  147  903
[0, 0] p( 6322):     2   58  218
[0, 0] p( 6323):     1
[0, 0] p( 6324):    12  204  372
[0, 0] p( 6325):     1    5   11   23   25   55  115  253  275  575 1265
[0, 0] p( 6326):     2
[0, 0] p( 6327):     9  171  333
[0, 0] p( 6328):     8   56  904
[0, 0] p( 6329):     1
[0, 0] p( 6330):     6   30 1266
[0, 0] p( 6331):     1   13  487
[0, 0] p( 6332):     4
[0, 0] p( 6333):     3
[0, 0] p( 6334):     2
[0, 0] p( 6335):     1    5    7   35  181  905 1267
[0, 0] p( 6336):   576
[0, 0] p( 6337):     1
[0, 0] p( 6338):     2
[0, 0] p( 6339):     3
[0, 0] p( 6340):     4   20 1268
[0, 0] p( 6341):     1   17  373
[0, 0] p( 6342):     6   42  906
[0, 0] p( 6343):     1
[0, 0] p( 6344):     8  104  488
[0, 0] p( 6345):    27  135 1269
[0, 0] p( 6346):     2   38  334
[0, 0] p( 6347):     1   11  577
[0, 0] p( 6348):    12  276
[0, 0] p( 6349):     1    7  907
[0, 0] p( 6350):     2   10   50  254 1270
[0, 0] p( 6351):     3   87  219
[0, 0] p( 6352):    16
[0, 0] p( 6353):     1
[0, 0] p( 6354):    18
[0, 0] p( 6355):     1    5   31   41  155  205 1271
[0, 0] p( 6356):     4   28  908
[0, 0] p( 6357):     3   39  489
[0, 0] p( 6358):     2   22   34  374  578
[0, 0] p( 6359):     1
[0, 0] p( 6360):    24  120 1272
[0, 0] p( 6361):     1
[0, 0] p( 6362):     2
[0, 0] p( 6363):     9   63  909
[0, 0] p( 6364):     4  148  172
[0, 0] p( 6365):     1    5   19   67   95  335 1273
[0, 0] p( 6366):     6
[0, 0] p( 6367):     1
[0, 0] p( 6368):    32
[0, 0] p( 6369):     3   33  579
[0, 0] p( 6370):     2   10   14   26   70   98  130  182  490  910 1274
[0, 0] p( 6371):     1   23  277
[0, 0] p( 6372):   108
[0, 0] p( 6373):     1
[0, 0] p( 6374):     2
[0, 0] p( 6375):     3   15   51   75  255  375 1275
[0, 0] p( 6376):     8
[0, 0] p( 6377):     1    7  911
[0, 0] p( 6378):     6
[0, 0] p( 6379):     1
[0, 0] p( 6380):     4   20   44  116  220  580 1276
[0, 0] p( 6381):     9
[0, 0] p( 6382):     2
[0, 0] p( 6383):     1   13  491
[0, 0] p( 6384):    48  336  912
[0, 0] p( 6385):     1    5 1277
[0, 0] p( 6386):     2   62  206
[0, 0] p( 6387):     3
[0, 0] p( 6388):     4
[0, 0] p( 6389):     1
[0, 0] p( 6390):    18   90 1278
[0, 0] p( 6391):     1    7   11   77   83  581  913
[0, 0] p( 6392):     8  136  376
[0, 0] p( 6393):     3
[0, 0] p( 6394):     2   46  278
[0, 0] p( 6395):     1    5 1279
[0, 0] p( 6396):    12  156  492
[0, 0] p( 6397):     1
[0, 0] p( 6398):     2   14  914
[0, 0] p( 6399):    81
[0, 0] p( 6400):   256 1280
[0, 0] p( 6401):     1   37  173
[0, 0] p( 6402):     6   66  582
[0, 0] p( 6403):     1   19  337
[0, 0] p( 6404):     4
[0, 0] p( 6405):     3   15   21  105  183  915 1281
[0, 0] p( 6406):     2
[0, 0] p( 6407):     1   43  149
[0, 0] p( 6408):    72
[0, 0] p( 6409):     1   13   17   29  221  377  493
[0, 0] p( 6410):     2   10 1282
[0, 0] p( 6411):     3
[0, 0] p( 6412):     4   28  916
[0, 0] p( 6413):     1   11   53  121  583
[0, 0] p( 6414):     6
[0, 0] p( 6415):     1    5 1283
[0, 0] p( 6416):    16
[0, 0] p( 6417):     9  207  279
[0, 0] p( 6418):     2
[0, 0] p( 6419):     1    7   49  131  917
[0, 0] p( 6420):    12   60 1284
[0, 0] p( 6421):     1
[0, 0] p( 6422):     2   26   38  338  494
[0, 0] p( 6423):     3
[0, 0] p( 6424):     8   88  584
[0, 0] p( 6425):     1    5   25  257 1285
[0, 0] p( 6426):    54  378  918
[0, 0] p( 6427):     1
[0, 0] p( 6428):     4
[0, 0] p( 6429):     3
[0, 0] p( 6430):     2   10 1286
[0, 0] p( 6431):     1   59  109
[0, 0] p( 6432):    96
[0, 0] p( 6433):     1    7  919
[0, 0] p( 6434):     2
[0, 0] p( 6435):     9   45   99  117  495  585 1287
[0, 0] p( 6436):     4
[0, 0] p( 6437):     1   41  157
[0, 0] p( 6438):     6  174  222
[0, 0] p( 6439):     1   47  137
[0, 0] p( 6440):     8   40   56  184  280  920 1288
[0, 0] p( 6441):     3   57  339
[0, 0] p( 6442):     2
[0, 0] p( 6443):     1   17  379
[0, 0] p( 6444):    36
[0, 0] p( 6445):     1    5 1289
[0, 0] p( 6446):     2   22  586
[0, 0] p( 6447):     3   21  921
[0, 0] p( 6448):    16  208  496
[0, 0] p( 6449):     1
[0, 0] p( 6450):     6   30  150  258 1290
[0, 0] p( 6451):     1
[0, 0] p( 6452):     4
[0, 0] p( 6453):    27
[0, 0] p( 6454):     2   14  922
[0, 0] p( 6455):     1    5 1291
[0, 0] p( 6456):    24
[0, 0] p( 6457):     1   11  587
[0, 0] p( 6458):     2
[0, 0] p( 6459):     3
[0, 0] p( 6460):     4   20   68   76  340  380 1292
[0, 0] p( 6461):     1    7   13   71   91  497  923
[0, 0] p( 6462):    18
[0, 0] p( 6463):     1   23  281
[0, 0] p( 6464):    64
[0, 0] p( 6465):     3   15 1293
[0, 0] p( 6466):     2  106  122
[0, 0] p( 6467):     1   29  223
[0, 0] p( 6468):    12   84  132  588  924
[0, 0] p( 6469):     1
[0, 0] p( 6470):     2   10 1294
[0, 0] p( 6471):     9
[0, 0] p( 6472):     8
[0, 0] p( 6473):     1
[0, 0] p( 6474):     6   78  498
[0, 0] p( 6475):     1    5    7   25   35   37  175  185  259  925 1295
[0, 0] p( 6476):     4
[0, 0] p( 6477):     3   51  381
[0, 0] p( 6478):     2   82  158
[0, 0] p( 6479):     1   11   19   31  209  341  589
[0, 0] p( 6480):  1296
[0, 0] p( 6481):     1
[0, 0] p( 6482):     2   14  926
[0, 0] p( 6483):     3
[0, 0] p( 6484):     4
[0, 0] p( 6485):     1    5 1297
[0, 0] p( 6486):     6  138  282
[0, 0] p( 6487):     1   13  499
[0, 0] p( 6488):     8
[0, 0] p( 6489):     9   63  927
[0, 0] p( 6490):     2   10   22  110  118  590 1298
[0, 0] p( 6491):     1
[0, 0] p( 6492):    12
[0, 0] p( 6493):     1   43  151
[0, 0] p( 6494):     2   34  382
[0, 0] p( 6495):     3   15 1299
[0, 0] p( 6496):    32  224  928
[0, 0] p( 6497):     1   73   89
[0, 0] p( 6498):    18  342
[0, 0] p( 6499):     1   67   97
[0, 0] p( 6500):     4   20   52  100  260  500 1300
[0, 0] p( 6501):     3   33  591
[0, 0] p( 6502):     2
[0, 0] p( 6503):     1    7  929
[0, 0] p( 6504):    24
[0, 0] p( 6505):     1    5 1301
[0, 0] p( 6506):     2
[0, 0] p( 6507):    27
[0, 0] p( 6508):     4
[0, 0] p( 6509):     1   23  283
[0, 0] p( 6510):     6   30   42  186  210  930 1302
[0, 0] p( 6511):     1   17  383
[0, 0] p( 6512):    16  176  592
[0, 0] p( 6513):     3   39  501
[0, 0] p( 6514):     2
[0, 0] p( 6515):     1    5 1303
[0, 0] p( 6516):    36
[0, 0] p( 6517):     1    7   19   49  133  343  931
[0, 0] p( 6518):     2
[0, 0] p( 6519):     3  123  159
[0, 0] p( 6520):     8   40 1304
[0, 0] p( 6521):     1
[0, 0] p( 6522):     6
[0, 0] p( 6523):     1   11  593
[0, 0] p( 6524):     4   28  932
[0, 0] p( 6525):     9   45  225  261 1305
[0, 0] p( 6526):     2   26  502
[0, 0] p( 6527):     1   61  107
[0, 0] p( 6528):   384
[0, 0] p( 6529):     1
[0, 0] p( 6530):     2   10 1306
[0, 0] p( 6531):     3   21  933
[0, 0] p( 6532):     4   92  284
[0, 0] p( 6533):     1   47  139
[0, 0] p( 6534):    54  594
[0, 0] p( 6535):     1    5 1307
[0, 0] p( 6536):     8  152  344
[0, 0] p( 6537):     3
[0, 0] p( 6538):     2   14  934
[0, 0] p( 6539):     1   13  503
[0, 0] p( 6540):    12   60 1308
[0, 0] p( 6541):     1   31  211
[0, 0] p( 6542):     2
[0, 0] p( 6543):     9
[0, 0] p( 6544):    16
[0, 0] p( 6545):     1    5    7   11   17   35   55   77   85  119  187  385  595  935 1309
[0, 0] p( 6546):     6
[0, 0] p( 6547):     1
[0, 0] p( 6548):     4
[0, 0] p( 6549):     3  111  177
[0, 0] p( 6550):     2   10   50  262 1310
[0, 0] p( 6551):     1
[0, 0] p( 6552):    72  504  936
[0, 0] p( 6553):     1
[0, 0] p( 6554):     2   58  226
[0, 0] p( 6555):     3   15   57   69  285  345 1311
[0, 0] p( 6556):     4   44  596
[0, 0] p( 6557):     1   79   83
[0, 0] p( 6558):     6
[0, 0] p( 6559):     1    7  937
[0, 0] p( 6560):    32  160 1312
[0, 0] p( 6561): 
[0, 0] p( 6562):     2   34  386
[0, 0] p( 6563):     1
[0, 0] p( 6564):    12
[0, 0] p( 6565):     1    5   13   65  101  505 1313
[0, 0] p( 6566):     2   14   98  134  938
[0, 0] p( 6567):     3   33  597
[0, 0] p( 6568):     8
[0, 0] p( 6569):     1
[0, 0] p( 6570):    18   90 1314
[0, 0] p( 6571):     1
[0, 0] p( 6572):     4  124  212
[0, 0] p( 6573):     3   21  939
[0, 0] p( 6574):     2   38  346
[0, 0] p( 6575):     1    5   25  263 1315
[0, 0] p( 6576):    48
[0, 0] p( 6577):     1
[0, 0] p( 6578):     2   22   26   46  286  506  598
[0, 0] p( 6579):     9  153  387
[0, 0] p( 6580):     4   20   28  140  188  940 1316
[0, 0] p( 6581):     1
[0, 0] p( 6582):     6
[0, 0] p( 6583):     1   29  227
[0, 0] p( 6584):     8
[0, 0] p( 6585):     3   15 1317
[0, 0] p( 6586):     2   74  178
[0, 0] p( 6587):     1    7  941
[0, 0] p( 6588):   108
[0, 0] p( 6589):     1   11  599
[0, 0] p( 6590):     2   10 1318
[0, 0] p( 6591):     3   39  507
[0, 0] p( 6592):    64
[0, 0] p( 6593):     1   19  347
[0, 0] p( 6594):     6   42  942
[0, 0] p( 6595):     1    5 1319
[0, 0] p( 6596):     4   68  388
[0, 0] p( 6597):     9
[0, 0] p( 6598):     2
[0, 0] p( 6599):     1
[0, 0] p( 6600):    24  120  264  600 1320
[0, 0] p( 6601):     1    7   23   41  161  287  943
[0, 0] p( 6602):     2
[0, 0] p( 6603):     3   93  213
[0, 0] p( 6604):     4   52  508
[0, 0] p( 6605):     1    5 1321
[0, 0] p( 6606):    18
[0, 0] p( 6607):     1
[0, 0] p( 6608):    16  112  944
[0, 0] p( 6609):     3
[0, 0] p( 6610):     2   10 1322
[0, 0] p( 6611):     1   11  601
[0, 0] p( 6612):    12  228  348
[0, 0] p( 6613):     1   17  389
[0, 0] p( 6614):     2
[0, 0] p( 6615):    27  135  189  945 1323
[0, 0] p( 6616):     8
[0, 0] p( 6617):     1   13  509
[0, 0] p( 6618):     6
[0, 0] p( 6619):     1
[0, 0] p( 6620):     4   20 1324
[0, 0] p( 6621):     3
[0, 0] p( 6622):     2   14   22   86  154  602  946
[0, 0] p( 6623):     1   37  179
[0, 0] p( 6624):   288
[0, 0] p( 6625):     1    5   25   53  125  265 1325
[0, 0] p( 6626):     2
[0, 0] p( 6627):     3  141
[0, 0] p( 6628):     4
[0, 0] p( 6629):     1    7  947
[0, 0] p( 6630):     6   30   78  102  390  510 1326
[0, 0] p( 6631):     1   19  349
[0, 0] p( 6632):     8
[0, 0] p( 6633):     9   99  603
[0, 0] p( 6634):     2   62  214
[0, 0] p( 6635):     1    5 1327
[0, 0] p( 6636):    12   84  948
[0, 0] p( 6637):     1
[0, 0] p( 6638):     2
[0, 0] p( 6639):     3
[0, 0] p( 6640):    16   80 1328
[0, 0] p( 6641):     1   29  229
[0, 0] p( 6642):   162
[0, 0] p( 6643):     1    7   13   73   91  511  949
[0, 0] p( 6644):     4   44  604
[0, 0] p( 6645):     3   15 1329
[0, 0] p( 6646):     2
[0, 0] p( 6647):     1   17   23  289  391
[0, 0] p( 6648):    24
[0, 0] p( 6649):     1   61  109
[0, 0] p( 6650):     2   10   14   38   50   70  190  266  350  950 1330
[0, 0] p( 6651):     9
[0, 0] p( 6652):     4
[0, 0] p( 6653):     1
[0, 0] p( 6654):     6
[0, 0] p( 6655):     1    5   11   55  121  605 1331
[0, 0] p( 6656):   512
[0, 0] p( 6657):     3   21  951
[0, 0] p( 6658):     2
[0, 0] p( 6659):     1
[0, 0] p( 6660):    36  180 1332
[0, 0] p( 6661):     1
[0, 0] p( 6662):     2
[0, 0] p( 6663):     3
[0, 0] p( 6664):     8   56  136  392  952
[0, 0] p( 6665):     1    5   31   43  155  215 1333
[0, 0] p( 6666):     6   66  606
[0, 0] p( 6667):     1   59  113
[0, 0] p( 6668):     4
[0, 0] p( 6669):    27  351  513
[0, 0] p( 6670):     2   10   46   58  230  290 1334
[0, 0] p( 6671):     1    7  953
[0, 0] p( 6672):    48
[0, 0] p( 6673):     1
[0, 0] p( 6674):     2   94  142
[0, 0] p( 6675):     3   15   75  267 1335
[0, 0] p( 6676):     4
[0, 0] p( 6677):     1   11  607
[0, 0] p( 6678):    18  126  954
[0, 0] p( 6679):     1
[0, 0] p( 6680):     8   40 1336
[0, 0] p( 6681):     3   51  393
[0, 0] p( 6682):     2   26  514
[0, 0] p( 6683):     1   41  163
[0, 0] p( 6684):    12
[0, 0] p( 6685):     1    5    7   35  191  955 1337
[0, 0] p( 6686):     2
[0, 0] p( 6687):     9
[0, 0] p( 6688):    32  352  608
[0, 0] p( 6689):     1
[0, 0] p( 6690):     6   30 1338
[0, 0] p( 6691):     1
[0, 0] p( 6692):     4   28  956
[0, 0] p( 6693):     3   69  291
[0, 0] p( 6694):     2
[0, 0] p( 6695):     1    5   13   65  103  515 1339
[0, 0] p( 6696):   216
[0, 0] p( 6697):     1   37  181
[0, 0] p( 6698):     2   34  394
[0, 0] p( 6699):     3   21   33   87  231  609  957
[0, 0] p( 6700):     4   20  100  268 1340
[0, 0] p( 6701):     1
[0, 0] p( 6702):     6
[0, 0] p( 6703):     1
[0, 0] p( 6704):    16
[0, 0] p( 6705):     9   45 1341
[0, 0] p( 6706):     2   14  958
[0, 0] p( 6707):     1   19  353
[0, 0] p( 6708):    12  156  516
[0, 0] p( 6709):     1
[0, 0] p( 6710):     2   10   22  110  122  610 1342
[0, 0] p( 6711):     3
[0, 0] p( 6712):     8
[0, 0] p( 6713):     1    7   49  137  959
[0, 0] p( 6714):    18
[0, 0] p( 6715):     1    5   17   79   85  395 1343
[0, 0] p( 6716):     4   92  292
[0, 0] p( 6717):     3
[0, 0] p( 6718):     2
[0, 0] p( 6719):     1
[0, 0] p( 6720):   192  960 1344
[0, 0] p( 6721):     1   11   13   47  143  517  611
[0, 0] p( 6722):     2
[0, 0] p( 6723):    81
[0, 0] p( 6724):     4  164
[0, 0] p( 6725):     1    5   25  269 1345
[0, 0] p( 6726):     6  114  354
[0, 0] p( 6727):     1    7   31  217  961
[0, 0] p( 6728):     8  232
[0, 0] p( 6729):     3
[0, 0] p( 6730):     2   10 1346
[0, 0] p( 6731):     1   53  127
[0, 0] p( 6732):    36  396  612
[0, 0] p( 6733):     1
[0, 0] p( 6734):     2   14   26   74  182  518  962
[0, 0] p( 6735):     3   15 1347
[0, 0] p( 6736):    16
[0, 0] p( 6737):     1
[0, 0] p( 6738):     6
[0, 0] p( 6739):     1   23  293
[0, 0] p( 6740):     4   20 1348
[0, 0] p( 6741):     9   63  963
[0, 0] p( 6742):     2
[0, 0] p( 6743):     1   11  613
[0, 0] p( 6744):    24
[0, 0] p( 6745):     1    5   19   71   95  355 1349
[0, 0] p( 6746):     2
[0, 0] p( 6747):     3   39  519
[0, 0] p( 6748):     4   28  964
[0, 0] p( 6749):     1   17  397
[0, 0] p( 6750):    54  270 1350
[0, 0] p( 6751):     1   43  157
[0, 0] p( 6752):    32
[0, 0] p( 6753):     3
[0, 0] p( 6754):     2   22  614
[0, 0] p( 6755):     1    5    7   35  193  965 1351
[0, 0] p( 6756):    12
[0, 0] p( 6757):     1   29  233
[0, 0] p( 6758):     2   62  218
[0, 0] p( 6759):     9
[0, 0] p( 6760):     8   40  104  520 1352
[0, 0] p( 6761):     1
[0, 0] p( 6762):     6   42  138  294  966
[0, 0] p( 6763):     1
[0, 0] p( 6764):     4   76  356
[0, 0] p( 6765):     3   15   33  123  165  615 1353
[0, 0] p( 6766):     2   34  398
[0, 0] p( 6767):     1   67  101
[0, 0] p( 6768):   144
[0, 0] p( 6769):     1    7  967
[0, 0] p( 6770):     2   10 1354
[0, 0] p( 6771):     3  111  183
[0, 0] p( 6772):     4
[0, 0] p( 6773):     1   13  521
[0, 0] p( 6774):     6
[0, 0] p( 6775):     1    5   25  271 1355
[0, 0] p( 6776):     8   56   88  616  968
[0, 0] p( 6777):    27
[0, 0] p( 6778):     2
[0, 0] p( 6779):     1
[0, 0] p( 6780):    12   60 1356
[0, 0] p( 6781):     1
[0, 0] p( 6782):     2
[0, 0] p( 6783):     3   21   51   57  357  399  969
[0, 0] p( 6784):   128
[0, 0] p( 6785):     1    5   23   59  115  295 1357
[0, 0] p( 6786):    18  234  522
[0, 0] p( 6787):     1   11  617
[0, 0] p( 6788):     4
[0, 0] p( 6789):     3   93  219
[0, 0] p( 6790):     2   10   14   70  194  970 1358
[0, 0] p( 6791):     1
[0, 0] p( 6792):    24
[0, 0] p( 6793):     1
[0, 0] p( 6794):     2   86  158
[0, 0] p( 6795):     9   45 1359
[0, 0] p( 6796):     4
[0, 0] p( 6797):     1    7  971
[0, 0] p( 6798):     6   66  618
[0, 0] p( 6799):     1   13  523
[0, 0] p( 6800):    16   80  272  400 1360
[0, 0] p( 6801):     3
[0, 0] p( 6802):     2   38  358
[0, 0] p( 6803):     1
[0, 0] p( 6804):   972
[0, 0] p( 6805):     1    5 1361
[0, 0] p( 6806):     2   82  166
[0, 0] p( 6807):     3
[0, 0] p( 6808):     8  184  296
[0, 0] p( 6809):     1   11  619
[0, 0] p( 6810):     6   30 1362
[0, 0] p( 6811):     1    7   49  139  973
[0, 0] p( 6812):     4   52  524
[0, 0] p( 6813):     9
[0, 0] p( 6814):     2
[0, 0] p( 6815):     1    5   29   47  145  235 1363
[0, 0] p( 6816):    96
[0, 0] p( 6817):     1   17  401
[0, 0] p( 6818):     2   14  974
[0, 0] p( 6819):     3
[0, 0] p( 6820):     4   20   44  124  220  620 1364
[0, 0] p( 6821):     1   19  359
[0, 0] p( 6822):    18
[0, 0] p( 6823):     1
[0, 0] p( 6824):     8
[0, 0] p( 6825):     3   15   21   39   75  105  195  273  525  975 1365
[0, 0] p( 6826):     2
[0, 0] p( 6827):     1
[0, 0] p( 6828):    12
[0, 0] p( 6829):     1
[0, 0] p( 6830):     2   10 1366
[0, 0] p( 6831):    27  297  621
[0, 0] p( 6832):    16  112  976
[0, 0] p( 6833):     1
[0, 0] p( 6834):     6  102  402
[0, 0] p( 6835):     1    5 1367
[0, 0] p( 6836):     4
[0, 0] p( 6837):     3  129  159
[0, 0] p( 6838):     2   26  526
[0, 0] p( 6839):     1    7  977
[0, 0] p( 6840):    72  360 1368
[0, 0] p( 6841):     1
[0, 0] p( 6842):     2   22  622
[0, 0] p( 6843):     3
[0, 0] p( 6844):     4  116  236
[0, 0] p( 6845):     1    5   37  185 1369
[0, 0] p( 6846):     6   42  978
[0, 0] p( 6847):     1   41  167
[0, 0] p( 6848):    64
[0, 0] p( 6849):     9
[0, 0] p( 6850):     2   10   50  274 1370
[0, 0] p( 6851):     1   13   17   31  221  403  527
[0, 0] p( 6852):    12
[0, 0] p( 6853):     1    7   11   77   89  623  979
[0, 0] p( 6854):     2   46  298
[0, 0] p( 6855):     3   15 1371
[0, 0] p( 6856):     8
[0, 0] p( 6857):     1
[0, 0] p( 6858):    54
[0, 0] p( 6859):     1   19  361
[0, 0] p( 6860):     4   20   28  140  196  980 1372
[0, 0] p( 6861):     3
[0, 0] p( 6862):     2   94  146
[0, 0] p( 6863):     1
[0, 0] p( 6864):    48  528  624
[0, 0] p( 6865):     1    5 1373
[0, 0] p( 6866):     2
[0, 0] p( 6867):     9   63  981
[0, 0] p( 6868):     4   68  404
[0, 0] p( 6869):     1
[0, 0] p( 6870):     6   30 1374
[0, 0] p( 6871):     1
[0, 0] p( 6872):     8
[0, 0] p( 6873):     3   87  237
[0, 0] p( 6874):     2   14  982
[0, 0] p( 6875):     1    5   11   25   55  125  275  625 1375
[0, 0] p( 6876):    36
[0, 0] p( 6877):     1   13   23  299  529
[0, 0] p( 6878):     2   38  362
[0, 0] p( 6879):     3
[0, 0] p( 6880):    32  160 1376
[0, 0] p( 6881):     1    7  983
[0, 0] p( 6882):     6  186  222
[0, 0] p( 6883):     1
[0, 0] p( 6884):     4
[0, 0] p( 6885):    81  405 1377
[0, 0] p( 6886):     2   22  626
[0, 0] p( 6887):     1   71   97
[0, 0] p( 6888):    24  168  984
[0, 0] p( 6889):     1   83
[0, 0] p( 6890):     2   10   26  106  130  530 1378
[0, 0] p( 6891):     3
[0, 0] p( 6892):     4
[0, 0] p( 6893):     1   61  113
[0, 0] p( 6894):    18
[0, 0] p( 6895):     1    5    7   35  197  985 1379
[0, 0] p( 6896):    16
[0, 0] p( 6897):     3   33   57  363  627
[0, 0] p( 6898):     2
[0, 0] p( 6899):     1
[0, 0] p( 6900):    12   60  276  300 1380
[0, 0] p( 6901):     1   67  103
[0, 0] p( 6902):     2   14   34   58  238  406  986
[0, 0] p( 6903):     9  117  531
[0, 0] p( 6904):     8
[0, 0] p( 6905):     1    5 1381
[0, 0] p( 6906):     6
[0, 0] p( 6907):     1
[0, 0] p( 6908):     4   44  628
[0, 0] p( 6909):     3   21  141  147  987
[0, 0] p( 6910):     2   10 1382
[0, 0] p( 6911):     1
[0, 0] p( 6912): 
[0, 0] p( 6913):     1   31  223
[0, 0] p( 6914):     2
[0, 0] p( 6915):     3   15 1383
[0, 0] p( 6916):     4   28   52   76  364  532  988
[0, 0] p( 6917):     1
[0, 0] p( 6918):     6
[0, 0] p( 6919):     1   11   17   37  187  407  629
[0, 0] p( 6920):     8   40 1384
[0, 0] p( 6921):     9
[0, 0] p( 6922):     2
[0, 0] p( 6923):     1    7   23   43  161  301  989
[0, 0] p( 6924):    12
[0, 0] p( 6925):     1    5   25  277 1385
[0, 0] p( 6926):     2
[0, 0] p( 6927):     3
[0, 0] p( 6928):    16
[0, 0] p( 6929):     1   13   41  169  533
[0, 0] p( 6930):    18   90  126  198  630  990 1386
[0, 0] p( 6931):     1   29  239
[0, 0] p( 6932):     4
[0, 0] p( 6933):     3
[0, 0] p( 6934):     2
[0, 0] p( 6935):     1    5   19   73   95  365 1387
[0, 0] p( 6936):    24  408
[0, 0] p( 6937):     1    7  991
[0, 0] p( 6938):     2
[0, 0] p( 6939):    27
[0, 0] p( 6940):     4   20 1388
[0, 0] p( 6941):     1   11  631
[0, 0] p( 6942):     6   78  534
[0, 0] p( 6943):     1   53  131
[0, 0] p( 6944):    32  224  992
[0, 0] p( 6945):     3   15 1389
[0, 0] p( 6946):     2   46  302
[0, 0] p( 6947):     1
[0, 0] p( 6948):    36
[0, 0] p( 6949):     1
[0, 0] p( 6950):     2   10   50  278 1390
[0, 0] p( 6951):     3   21  993
[0, 0] p( 6952):     8   88  632
[0, 0] p( 6953):     1   17  409
[0, 0] p( 6954):     6  114  366
[0, 0] p( 6955):     1    5   13   65  107  535 1391
[0, 0] p( 6956):     4  148  188
[0, 0] p( 6957):     9
[0, 0] p( 6958):     2   14   98  142  994
[0, 0] p( 6959):     1
[0, 0] p( 6960):    48  240 1392
[0, 0] p( 6961):     1
[0, 0] p( 6962):     2  118
[0, 0] p( 6963):     3   33  633
[0, 0] p( 6964):     4
[0, 0] p( 6965):     1    5    7   35  199  995 1393
[0, 0] p( 6966):   162
[0, 0] p( 6967):     1
[0, 0] p( 6968):     8  104  536
[0, 0] p( 6969):     3   69  303
[0, 0] p( 6970):     2   10   34   82  170  410 1394
[0, 0] p( 6971):     1
[0, 0] p( 6972):    12   84  996
[0, 0] p( 6973):     1   19  367
[0, 0] p( 6974):     2   22  634
[0, 0] p( 6975):     9   45  225  279 1395
[0, 0] p( 6976):    64
[0, 0] p( 6977):     1
[0, 0] p( 6978):     6
[0, 0] p( 6979):     1    7  997
[0, 0] p( 6980):     4   20 1396
[0, 0] p( 6981):     3   39  537
[0, 0] p( 6982):     2
[0, 0] p( 6983):     1
[0, 0] p( 6984):    72
[0, 0] p( 6985):     1    5   11   55  127  635 1397
[0, 0] p( 6986):     2   14  998
[0, 0] p( 6987):     3   51  411
[0, 0] p( 6988):     4
[0, 0] p( 6989):     1   29  241
[0, 0] p( 6990):     6   30 1398
[0, 0] p( 6991):     1
[0, 0] p( 6992):    16  304  368
[0, 0] p( 6993):    27  189  999
[0, 0] p( 6994):     2   26  538
[0, 0] p( 6995):     1    5 1399
[0, 0] p( 6996):    12  132  636
[0, 0] p( 6997):     1
[0, 0] p( 6998):     2
[0, 0] p( 6999):     3
[0, 0] p( 7000):     8   40   56  200  280 1000 1400
[0, 0] p( 7001):     1
[0, 0] p( 7002):    18
[0, 0] p( 7003):     1   47  149
[0, 0] p( 7004):     4   68  412
[0, 0] p( 7005):     3   15 1401
[0, 0] p( 7006):     2   62  226
[0, 0] p( 7007):     1    7   11   13   49   77   91  143  539  637 1001
[0, 0] p( 7008):    96
[0, 0] p( 7009):     1   43  163
[0, 0] p( 7010):     2   10 1402
[0, 0] p( 7011):     9  171  369
[0, 0] p( 7012):     4
[0, 0] p( 7013):     1
[0, 0] p( 7014):     6   42 1002
[0, 0] p( 7015):     1    5   23   61  115  305 1403
[0, 0] p( 7016):     8
[0, 0] p( 7017):     3
[0, 0] p( 7018):     2   22   58  242  638
[0, 0] p( 7019):     1
[0, 0] p( 7020):   108  540 1404
[0, 0] p( 7021):     1    7   17   59  119  413 1003
[0, 0] p( 7022):     2
[0, 0] p( 7023):     3
[0, 0] p( 7024):    16
[0, 0] p( 7025):     1    5   25  281 1405
[0, 0] p( 7026):     6
[0, 0] p( 7027):     1
[0, 0] p( 7028):     4   28 1004
[0, 0] p( 7029):     9   99  639
[0, 0] p( 7030):     2   10   38   74  190  370 1406
[0, 0] p( 7031):     1   79   89
[0, 0] p( 7032):    24
[0, 0] p( 7033):     1   13  541
[0, 0] p( 7034):     2
[0, 0] p( 7035):     3   15   21  105  201 1005 1407
[0, 0] p( 7036):     4
[0, 0] p( 7037):     1   31  227
[0, 0] p( 7038):    18  306  414
[0, 0] p( 7039):     1
[0, 0] p( 7040):   128  640 1408
[0, 0] p( 7041):     3
[0, 0] p( 7042):     2   14 1006
[0, 0] p( 7043):     1
[0, 0] p( 7044):    12
[0, 0] p( 7045):     1    5 1409
[0, 0] p( 7046):     2   26  542
[0, 0] p( 7047):   243
[0, 0] p( 7048):     8
[0, 0] p( 7049):     1    7   19   53  133  371 1007
[0, 0] p( 7050):     6   30  150  282 1410
[0, 0] p( 7051):     1   11  641
[0, 0] p( 7052):     4  164  172
[0, 0] p( 7053):     3
[0, 0] p( 7054):     2
[0, 0] p( 7055):     1    5   17   83   85  415 1411
[0, 0] p( 7056):   144 1008
[0, 0] p( 7057):     1
[0, 0] p( 7058):     2
[0, 0] p( 7059):     3   39  543
[0, 0] p( 7060):     4   20 1412
[0, 0] p( 7061):     1   23  307
[0, 0] p( 7062):     6   66  642
[0, 0] p( 7063):     1    7 1009
[0, 0] p( 7064):     8
[0, 0] p( 7065):     9   45 1413
[0, 0] p( 7066):     2
[0, 0] p( 7067):     1   37  191
[0, 0] p( 7068):    12  228  372
[0, 0] p( 7069):     1
[0, 0] p( 7070):     2   10   14   70  202 1010 1414
[0, 0] p( 7071):     3
[0, 0] p( 7072):    32  416  544
[0, 0] p( 7073):     1   11  643
[0, 0] p( 7074):    54
[0, 0] p( 7075):     1    5   25  283 1415
[0, 0] p( 7076):     4  116  244
[0, 0] p( 7077):     3   21 1011
[0, 0] p( 7078):     2
[0, 0] p( 7079):     1
[0, 0] p( 7080):    24  120 1416
[0, 0] p( 7081):     1   73   97
[0, 0] p( 7082):     2
[0, 0] p( 7083):     9
[0, 0] p( 7084):     4   28   44   92  308  644 1012
[0, 0] p( 7085):     1    5   13   65  109  545 1417
[0, 0] p( 7086):     6
[0, 0] p( 7087):     1   19  373
[0, 0] p( 7088):    16
[0, 0] p( 7089):     3   51  417
[0, 0] p( 7090):     2   10 1418
[0, 0] p( 7091):     1    7 1013
[0, 0] p( 7092):    36
[0, 0] p( 7093):     1   41  173
[0, 0] p( 7094):     2
[0, 0] p( 7095):     3   15   33  129  165  645 1419
[0, 0] p( 7096):     8
[0, 0] p( 7097):     1   47  151
[0, 0] p( 7098):     6   42   78  546 1014
[0, 0] p( 7099):     1   31  229
[0, 0] p( 7100):     4   20  100  284 1420
[0, 0] p( 7101):    27
[0, 0] p( 7102):     2  106  134
[0, 0] p( 7103):     1
[0, 0] p( 7104):   192
[0, 0] p( 7105):     1    5    7   29   35   49  145  203  245 1015 1421
[0, 0] p( 7106):     2   22   34   38  374  418  646
[0, 0] p( 7107):     3   69  309
[0, 0] p( 7108):     4
[0, 0] p( 7109):     1
[0, 0] p( 7110):    18   90 1422
[0, 0] p( 7111):     1   13  547
[0, 0] p( 7112):     8   56 1016
[0, 0] p( 7113):     3
[0, 0] p( 7114):     2
[0, 0] p( 7115):     1    5 1423
[0, 0] p( 7116):    12
[0, 0] p( 7117):     1   11  647
[0, 0] p( 7118):     2
[0, 0] p( 7119):     9   63 1017
[0, 0] p( 7120):    16   80 1424
[0, 0] p( 7121):     1
[0, 0] p( 7122):     6
[0, 0] p( 7123):     1   17  419
[0, 0] p( 7124):     4   52  548
[0, 0] p( 7125):     3   15   57   75  285  375 1425
[0, 0] p( 7126):     2   14 1018
[0, 0] p( 7127):     1
[0, 0] p( 7128):   648
[0, 0] p( 7129):     1
[0, 0] p( 7130):     2   10   46   62  230  310 1426
[0, 0] p( 7131):     3
[0, 0] p( 7132):     4
[0, 0] p( 7133):     1    7 1019
[0, 0] p( 7134):     6  174  246
[0, 0] p( 7135):     1    5 1427
[0, 0] p( 7136):    32
[0, 0] p( 7137):     9  117  549
[0, 0] p( 7138):     2   86  166
[0, 0] p( 7139):     1   11   59  121  649
[0, 0] p( 7140):    12   60   84  204  420 1020 1428
[0, 0] p( 7141):     1   37  193
[0, 0] p( 7142):     2
[0, 0] p( 7143):     3
[0, 0] p( 7144):     8  152  376
[0, 0] p( 7145):     1    5 1429
[0, 0] p( 7146):    18
[0, 0] p( 7147):     1    7 1021
[0, 0] p( 7148):     4
[0, 0] p( 7149):     3
[0, 0] p( 7150):     2   10   22   26   50  110  130  286  550  650 1430
[0, 0] p( 7151):     1
[0, 0] p( 7152):    48
[0, 0] p( 7153):     1   23  311
[0, 0] p( 7154):     2   14   98  146 1022
[0, 0] p( 7155):    27  135 1431
[0, 0] p( 7156):     4
[0, 0] p( 7157):     1   17  421
[0, 0] p( 7158):     6
[0, 0] p( 7159):     1
[0, 0] p( 7160):     8   40 1432
[0, 0] p( 7161):     3   21   33   93  231  651 1023
[0, 0] p( 7162):     2
[0, 0] p( 7163):     1   13   19   29  247  377  551
[0, 0] p( 7164):    36
[0, 0] p( 7165):     1    5 1433
[0, 0] p( 7166):     2
[0, 0] p( 7167):     3
[0, 0] p( 7168):  1024
[0, 0] p( 7169):     1   67  107
[0, 0] p( 7170):     6   30 1434
[0, 0] p( 7171):     1   71  101
[0, 0] p( 7172):     4   44  652
[0, 0] p( 7173):     9
[0, 0] p( 7174):     2   34  422
[0, 0] p( 7175):     1    5    7   25   35   41  175  205  287 1025 1435
[0, 0] p( 7176):    24  312  552
[0, 0] p( 7177):     1
[0, 0] p( 7178):     2   74  194
[0, 0] p( 7179):     3
[0, 0] p( 7180):     4   20 1436
[0, 0] p( 7181):     1   43  167
[0, 0] p( 7182):    54  378 1026
[0, 0] p( 7183):     1   11  653
[0, 0] p( 7184):    16
[0, 0] p( 7185):     3   15 1437
[0, 0] p( 7186):     2
[0, 0] p( 7187):     1
[0, 0] p( 7188):    12
[0, 0] p( 7189):     1    7   13   79   91  553 1027
[0, 0] p( 7190):     2   10 1438
[0, 0] p( 7191):     9  153  423
[0, 0] p( 7192):     8  232  248
[0, 0] p( 7193):     1
[0, 0] p( 7194):     6   66  654
[0, 0] p( 7195):     1    5 1439
[0, 0] p( 7196):     4   28 1028
[0, 0] p( 7197):     3
[0, 0] p( 7198):     2  118  122
[0, 0] p( 7199):     1   23  313
[0, 0] p( 7200):   288 1440
[0, 0] p( 7201):     1   19  379
[0, 0] p( 7202):     2   26  554
[0, 0] p( 7203):     3   21  147 1029
[0, 0] p( 7204):     4
[0, 0] p( 7205):     1    5   11   55  131  655 1441
[0, 0] p( 7206):     6
[0, 0] p( 7207):     1
[0, 0] p( 7208):     8  136  424
[0, 0] p( 7209):    81
[0, 0] p( 7210):     2   10   14   70  206 1030 1442
[0, 0] p( 7211):     1
[0, 0] p( 7212):    12
[0, 0] p( 7213):     1
[0, 0] p( 7214):     2
[0, 0] p( 7215):     3   15   39  111  195  555 1443
[0, 0] p( 7216):    16  176  656
[0, 0] p( 7217):     1    7 1031
[0, 0] p( 7218):    18
[0, 0] p( 7219):     1
[0, 0] p( 7220):     4   20   76  380 1444
[0, 0] p( 7221):     3   87  249
[0, 0] p( 7222):     2   46  314
[0, 0] p( 7223):     1   31  233
[0, 0] p( 7224):    24  168 1032
[0, 0] p( 7225):     1    5   17   25   85  289  425 1445
[0, 0] p( 7226):     2
[0, 0] p( 7227):     9   99  657
[0, 0] p( 7228):     4   52  556
[0, 0] p( 7229):     1
[0, 0] p( 7230):     6   30 1446
[0, 0] p( 7231):     1    7 1033
[0, 0] p( 7232):    64
[0, 0] p( 7233):     3
[0, 0] p( 7234):     2
[0, 0] p( 7235):     1    5 1447
[0, 0] p( 7236):   108
[0, 0] p( 7237):     1
[0, 0] p( 7238):     2   14   22   94  154  658 1034
[0, 0] p( 7239):     3   57  381
[0, 0] p( 7240):     8   40 1448
[0, 0] p( 7241):     1   13  557
[0, 0] p( 7242):     6  102  426
[0, 0] p( 7243):     1
[0, 0] p( 7244):     4
[0, 0] p( 7245):     9   45   63  207  315 1035 1449
[0, 0] p( 7246):     2
[0, 0] p( 7247):     1
[0, 0] p( 7248):    48
[0, 0] p( 7249):     1   11  659
[0, 0] p( 7250):     2   10   50   58  250  290 1450
[0, 0] p( 7251):     3
[0, 0] p( 7252):     4   28  148  196 1036
[0, 0] p( 7253):     1
[0, 0] p( 7254):    18  234  558
[0, 0] p( 7255):     1    5 1451
[0, 0] p( 7256):     8
[0, 0] p( 7257):     3  123  177
[0, 0] p( 7258):     2   38  382
[0, 0] p( 7259):     1    7   17   61  119  427 1037
[0, 0] p( 7260):    12   60  132  660 1452
[0, 0] p( 7261):     1   53  137
[0, 0] p( 7262):     2
[0, 0] p( 7263):    27
[0, 0] p( 7264):    32
[0, 0] p( 7265):     1    5 1453
[0, 0] p( 7266):     6   42 1038
[0, 0] p( 7267):     1   13   43  169  559
[0, 0] p( 7268):     4   92  316
[0, 0] p( 7269):     3
[0, 0] p( 7270):     2   10 1454
[0, 0] p( 7271):     1   11  661
[0, 0] p( 7272):    72
[0, 0] p( 7273):     1    7 1039
[0, 0] p( 7274):     2
[0, 0] p( 7275):     3   15   75  291 1455
[0, 0] p( 7276):     4   68  428
[0, 0] p( 7277):     1   19  383
[0, 0] p( 7278):     6
[0, 0] p( 7279):     1   29  251
[0, 0] p( 7280):    16   80  112  208  560 1040 1456
[0, 0] p( 7281):     9
[0, 0] p( 7282):     2   22  662
[0, 0] p( 7283):     1
[0, 0] p( 7284):    12
[0, 0] p( 7285):     1    5   31   47  155  235 1457
[0, 0] p( 7286):     2
[0, 0] p( 7287):     3   21 1041
[0, 0] p( 7288):     8
[0, 0] p( 7289):     1   37  197
[0, 0] p( 7290):  1458
[0, 0] p( 7291):     1   23  317
[0, 0] p( 7292):     4
[0, 0] p( 7293):     3   33   39   51  429  561  663
[0, 0] p( 7294):     2   14 1042
[0, 0] p( 7295):     1    5 1459
[0, 0] p( 7296):   384
[0, 0] p( 7297):     1
[0, 0] p( 7298):     2   82  178
[0, 0] p( 7299):     9
[0, 0] p( 7300):     4   20  100  292 1460
[0, 0] p( 7301):     1    7   49  149 1043
[0, 0] p( 7302):     6
[0, 0] p( 7303):     1   67  109
[0, 0] p( 7304):     8   88  664
[0, 0] p( 7305):     3   15 1461
[0, 0] p( 7306):     2   26  562
[0, 0] p( 7307):     1
[0, 0] p( 7308):    36  252 1044
[0, 0] p( 7309):     1
[0, 0] p( 7310):     2   10   34   86  170  430 1462
[0, 0] p( 7311):     3
[0, 0] p( 7312):    16
[0, 0] p( 7313):     1   71  103
[0, 0] p( 7314):     6  138  318
[0, 0] p( 7315):     1    5    7   11   19   35   55   77   95  133  209  385  665 1045 1463
[0, 0] p( 7316):     4  124  236
[0, 0] p( 7317):    27
[0, 0] p( 7318):     2
[0, 0] p( 7319):     1   13  563
[0, 0] p( 7320):    24  120 1464
[0, 0] p( 7321):     1
[0, 0] p( 7322):     2   14 1046
[0, 0] p( 7323):     3
[0, 0] p( 7324):     4
[0, 0] p( 7325):     1    5   25  293 1465
[0, 0] p( 7326):    18  198  666
[0, 0] p( 7327):     1   17  431
[0, 0] p( 7328):    32
[0, 0] p( 7329):     3   21 1047
[0, 0] p( 7330):     2   10 1466
[0, 0] p( 7331):     1
[0, 0] p( 7332):    12  156  564
[0, 0] p( 7333):     1
[0, 0] p( 7334):     2   38  386
[0, 0] p( 7335):     9   45 1467
[0, 0] p( 7336):     8   56 1048
[0, 0] p( 7337):     1   11   23   29  253  319  667
[0, 0] p( 7338):     6
[0, 0] p( 7339):     1   41  179
[0, 0] p( 7340):     4   20 1468
[0, 0] p( 7341):     3
[0, 0] p( 7342):     2
[0, 0] p( 7343):     1    7 1049
[0, 0] p( 7344):   432
[0, 0] p( 7345):     1    5   13   65  113  565 1469
[0, 0] p( 7346):     2
[0, 0] p( 7347):     3   93  237
[0, 0] p( 7348):     4   44  668
[0, 0] p( 7349):     1
[0, 0] p( 7350):     6   30   42  150  210  294 1050 1470
[0, 0] p( 7351):     1
[0, 0] p( 7352):     8
[0, 0] p( 7353):     9  171  387
[0, 0] p( 7354):     2
[0, 0] p( 7355):     1    5 1471
[0, 0] p( 7356):    12
[0, 0] p( 7357):     1    7 1051
[0, 0] p( 7358):     2   26  566
[0, 0] p( 7359):     3   33  669
[0, 0] p( 7360):    64  320 1472
[0, 0] p( 7361):     1   17  433
[0, 0] p( 7362):    18
[0, 0] p( 7363):     1   37  199
[0, 0] p( 7364):     4   28 1052
[0, 0] p( 7365):     3   15 1473
[0, 0] p( 7366):     2   58  254
[0, 0] p( 7367):     1   53  139
[0, 0] p( 7368):    24
[0, 0] p( 7369):     1
[0, 0] p( 7370):     2   10   22  110  134  670 1474
[0, 0] p( 7371):    81  567 1053
[0, 0] p( 7372):     4   76  388
[0, 0] p( 7373):     1   73  101
[0, 0] p( 7374):     6
[0, 0] p( 7375):     1    5   25   59  125  295 1475
[0, 0] p( 7376):    16
[0, 0] p( 7377):     3
[0, 0] p( 7378):     2   14   34   62  238  434 1054
[0, 0] p( 7379):     1   47  157
[0, 0] p( 7380):    36  180 1476
[0, 0] p( 7381):     1   11   61  121  671
[0, 0] p( 7382):     2
[0, 0] p( 7383):     3   69  321
[0, 0] p( 7384):     8  104  568
[0, 0] p( 7385):     1    5    7   35  211 1055 1477
[0, 0] p( 7386):     6
[0, 0] p( 7387):     1   83   89
[0, 0] p( 7388):     4
[0, 0] p( 7389):     9
[0, 0] p( 7390):     2   10 1478
[0, 0] p( 7391):     1   19  389
[0, 0] p( 7392):    96  672 1056
[0, 0] p( 7393):     1
[0, 0] p( 7394):     2
[0, 0] p( 7395):     3   15   51   87  255  435 1479
[0, 0] p( 7396):     4  172
[0, 0] p( 7397):     1   13  569
[0, 0] p( 7398):    54
[0, 0] p( 7399):     1    7   49  151 1057
[0, 0] p( 7400):     8   40  200  296 1480
[0, 0] p( 7401):     3
[0, 0] p( 7402):     2
[0, 0] p( 7403):     1   11  673
[0, 0] p( 7404):    12
[0, 0] p( 7405):     1    5 1481
[0, 0] p( 7406):     2   14   46  322 1058
[0, 0] p( 7407):     9
[0, 0] p( 7408):    16
[0, 0] p( 7409):     1   31  239
[0, 0] p( 7410):     6   30   78  114  390  570 1482
[0, 0] p( 7411):     1
[0, 0] p( 7412):     4   68  436
[0, 0] p( 7413):     3   21 1059
[0, 0] p( 7414):     2   22  674
[0, 0] p( 7415):     1    5 1483
[0, 0] p( 7416):    72
[0, 0] p( 7417):     1
[0, 0] p( 7418):     2
[0, 0] p( 7419):     3
[0, 0] p( 7420):     4   20   28  140  212 1060 1484
[0, 0] p( 7421):     1   41  181
[0, 0] p( 7422):     6
[0, 0] p( 7423):     1   13  571
[0, 0] p( 7424):   256
[0, 0] p( 7425):    27  135  297  675 1485
[0, 0] p( 7426):     2   94  158
[0, 0] p( 7427):     1    7 1061
[0, 0] p( 7428):    12
[0, 0] p( 7429):     1   17   19   23  323  391  437
[0, 0] p( 7430):     2   10 1486
[0, 0] p( 7431):     3
[0, 0] p( 7432):     8
[0, 0] p( 7433):     1
[0, 0] p( 7434):    18  126 1062
[0, 0] p( 7435):     1    5 1487
[0, 0] p( 7436):     4   44   52  572  676
[0, 0] p( 7437):     3  111  201
[0, 0] p( 7438):     2
[0, 0] p( 7439):     1   43  173
[0, 0] p( 7440):    48  240 1488
[0, 0] p( 7441):     1    7 1063
[0, 0] p( 7442):     2  122
[0, 0] p( 7443):     9
[0, 0] p( 7444):     4
[0, 0] p( 7445):     1    5 1489
[0, 0] p( 7446):     6  102  438
[0, 0] p( 7447):     1   11  677
[0, 0] p( 7448):     8   56  152  392 1064
[0, 0] p( 7449):     3   39  573
[0, 0] p( 7450):     2   10   50  298 1490
[0, 0] p( 7451):     1
[0, 0] p( 7452):   324
[0, 0] p( 7453):     1   29  257
[0, 0] p( 7454):     2
[0, 0] p( 7455):     3   15   21  105  213 1065 1491
[0, 0] p( 7456):    32
[0, 0] p( 7457):     1
[0, 0] p( 7458):     6   66  678
[0, 0] p( 7459):     1
[0, 0] p( 7460):     4   20 1492
[0, 0] p( 7461):     9
[0, 0] p( 7462):     2   14   26   82  182  574 1066
[0, 0] p( 7463):     1   17  439
[0, 0] p( 7464):    24
[0, 0] p( 7465):     1    5 1493
[0, 0] p( 7466):     2
[0, 0] p( 7467):     3   57  393
[0, 0] p( 7468):     4
[0, 0] p( 7469):     1    7   11   77   97  679 1067
[0, 0] p( 7470):    18   90 1494
[0, 0] p( 7471):     1   31  241
[0, 0] p( 7472):    16
[0, 0] p( 7473):     3  141  159
[0, 0] p( 7474):     2   74  202
[0, 0] p( 7475):     1    5   13   23   25   65  115  299  325  575 1495
[0, 0] p( 7476):    12   84 1068
[0, 0] p( 7477):     1
[0, 0] p( 7478):     2
[0, 0] p( 7479):    27
[0, 0] p( 7480):     8   40   88  136  440  680 1496
[0, 0] p( 7481):     1
[0, 0] p( 7482):     6  174  258
[0, 0] p( 7483):     1    7 1069
[0, 0] p( 7484):     4
[0, 0] p( 7485):     3   15 1497
[0, 0] p( 7486):     2   38  394
[0, 0] p( 7487):     1
[0, 0] p( 7488):   576
[0, 0] p( 7489):     1
[0, 0] p( 7490):     2   10   14   70  214 1070 1498
[0, 0] p( 7491):     3   33  681
[0, 0] p( 7492):     4
[0, 0] p( 7493):     1   59  127
[0, 0] p( 7494):     6
[0, 0] p( 7495):     1    5 1499
[0, 0] p( 7496):     8
[0, 0] p( 7497):     9   63  153  441 1071
[0, 0] p( 7498):     2   46  326
[0, 0] p( 7499):     1
[0, 0] p( 7500):    12   60  300 1500
[0, 0] p( 7501):     1   13  577
[0, 0] p( 7502):     2   22   62  242  682
[0, 0] p( 7503):     3  123  183
[0, 0] p( 7504):    16  112 1072
[0, 0] p( 7505):     1    5   19   79   95  395 1501
[0, 0] p( 7506):    54
[0, 0] p( 7507):     1
[0, 0] p( 7508):     4
[0, 0] p( 7509):     3
[0, 0] p( 7510):     2   10 1502
[0, 0] p( 7511):     1    7   29   37  203  259 1073
[0, 0] p( 7512):    24
[0, 0] p( 7513):     1   11  683
[0, 0] p( 7514):     2   26   34  442  578
[0, 0] p( 7515):     9   45 1503
[0, 0] p( 7516):     4
[0, 0] p( 7517):     1
[0, 0] p( 7518):     6   42 1074
[0, 0] p( 7519):     1   73  103
[0, 0] p( 7520):    32  160 1504
[0, 0] p( 7521):     3   69  327
[0, 0] p( 7522):     2
[0, 0] p( 7523):     1
[0, 0] p( 7524):    36  396  684
[0, 0] p( 7525):     1    5    7   25   35   43  175  215  301 1075 1505
[0, 0] p( 7526):     2  106  142
[0, 0] p( 7527):     3   39  579
[0, 0] p( 7528):     8
[0, 0] p( 7529):     1
[0, 0] p( 7530):     6   30 1506
[0, 0] p( 7531):     1   17  443
[0, 0] p( 7532):     4   28 1076
[0, 0] p( 7533):   243
[0, 0] p( 7534):     2
[0, 0] p( 7535):     1    5   11   55  137  685 1507
[0, 0] p( 7536):    48
[0, 0] p( 7537):     1
[0, 0] p( 7538):     2
[0, 0] p( 7539):     3   21 1077
[0, 0] p( 7540):     4   20   52  116  260  580 1508
[0, 0] p( 7541):     1
[0, 0] p( 7542):    18
[0, 0] p( 7543):     1   19  397
[0, 0] p( 7544):     8  184  328
[0, 0] p( 7545):     3   15 1509
[0, 0] p( 7546):     2   14   22   98  154  686 1078
[0, 0] p( 7547):     1
[0, 0] p( 7548):    12  204  444
[0, 0] p( 7549):     1
[0, 0] p( 7550):     2   10   50  302 1510
[0, 0] p( 7551):     9
[0, 0] p( 7552):   128
[0, 0] p( 7553):     1    7   13   83   91  581 1079
[0, 0] p( 7554):     6
[0, 0] p( 7555):     1    5 1511
[0, 0] p( 7556):     4
[0, 0] p( 7557):     3   33  687
[0, 0] p( 7558):     2
[0, 0] p( 7559):     1
[0, 0] p( 7560):   216 1080 1512
[0, 0] p( 7561):     1
[0, 0] p( 7562):     2   38  398
[0, 0] p( 7563):     3
[0, 0] p( 7564):     4  124  244
[0, 0] p( 7565):     1    5   17   85   89  445 1513
[0, 0] p( 7566):     6   78  582
[0, 0] p( 7567):     1    7   23   47  161  329 1081
[0, 0] p( 7568):    16  176  688
[0, 0] p( 7569):     9  261
[0, 0] p( 7570):     2   10 1514
[0, 0] p( 7571):     1   67  113
[0, 0] p( 7572):    12
[0, 0] p( 7573):     1
[0, 0] p( 7574):     2   14 1082
[0, 0] p( 7575):     3   15   75  303 1515
[0, 0] p( 7576):     8
[0, 0] p( 7577):     1
[0, 0] p( 7578):    18
[0, 0] p( 7579):     1   11   13   53  143  583  689
[0, 0] p( 7580):     4   20 1516
[0, 0] p( 7581):     3   21   57  399 1083
[0, 0] p( 7582):     2   34  446
[0, 0] p( 7583):     1
[0, 0] p( 7584):    96
[0, 0] p( 7585):     1    5   37   41  185  205 1517
[0, 0] p( 7586):     2
[0, 0] p( 7587):    27
[0, 0] p( 7588):     4   28 1084
[0, 0] p( 7589):     1
[0, 0] p( 7590):     6   30   66  138  330  690 1518
[0, 0] p( 7591):     1
[0, 0] p( 7592):     8  104  584
[0, 0] p( 7593):     3
[0, 0] p( 7594):     2
[0, 0] p( 7595):     1    5    7   31   35   49  155  217  245 1085 1519
[0, 0] p( 7596):    36
[0, 0] p( 7597):     1   71  107
[0, 0] p( 7598):     2   58  262
[0, 0] p( 7599):     3   51  447
[0, 0] p( 7600):    16   80  304  400 1520
[0, 0] p( 7601):     1   11  691
[0, 0] p( 7602):     6   42 1086
[0, 0] p( 7603):     1
[0, 0] p( 7604):     4
[0, 0] p( 7605):     9   45  117  585 1521
[0, 0] p( 7606):     2
[0, 0] p( 7607):     1
[0, 0] p( 7608):    24
[0, 0] p( 7609):     1    7 1087
[0, 0] p( 7610):     2   10 1522
[0, 0] p( 7611):     3  129  177
[0, 0] p( 7612):     4   44  692
[0, 0] p( 7613):     1   23  331
[0, 0] p( 7614):   162
[0, 0] p( 7615):     1    5 1523
[0, 0] p( 7616):    64  448 1088
[0, 0] p( 7617):     3
[0, 0] p( 7618):     2   26  586
[0, 0] p( 7619):     1   19  401
[0, 0] p( 7620):    12   60 1524
[0, 0] p( 7621):     1
[0, 0] p( 7622):     2   74  206
[0, 0] p( 7623):     9   63   99  693 1089
[0, 0] p( 7624):     8
[0, 0] p( 7625):     1    5   25   61  125  305 1525
[0, 0] p( 7626):     6  186  246
[0, 0] p( 7627):     1   29  263
[0, 0] p( 7628):     4
[0, 0] p( 7629):     3
[0, 0] p( 7630):     2   10   14   70  218 1090 1526
[0, 0] p( 7631):     1   13  587
[0, 0] p( 7632):   144
[0, 0] p( 7633):     1   17  449
[0, 0] p( 7634):     2   22  694
[0, 0] p( 7635):     3   15 1527
[0, 0] p( 7636):     4   92  332
[0, 0] p( 7637):     1    7 1091
[0, 0] p( 7638):     6  114  402
[0, 0] p( 7639):     1
[0, 0] p( 7640):     8   40 1528
[0, 0] p( 7641):    27
[0, 0] p( 7642):     2
[0, 0] p( 7643):     1
[0, 0] p( 7644):    12   84  156  588 1092
[0, 0] p( 7645):     1    5   11   55  139  695 1529
[0, 0] p( 7646):     2
[0, 0] p( 7647):     3
[0, 0] p( 7648):    32
[0, 0] p( 7649):     1
[0, 0] p( 7650):    18   90  306  450 1530
[0, 0] p( 7651):     1    7 1093
[0, 0] p( 7652):     4
[0, 0] p( 7653):     3
[0, 0] p( 7654):     2   86  178
[0, 0] p( 7655):     1    5 1531
[0, 0] p( 7656):    24  264  696
[0, 0] p( 7657):     1   13   19   31  247  403  589
[0, 0] p( 7658):     2   14 1094
[0, 0] p( 7659):     9  207  333
[0, 0] p( 7660):     4   20 1532
[0, 0] p( 7661):     1   47  163
[0, 0] p( 7662):     6
[0, 0] p( 7663):     1   79   97
[0, 0] p( 7664):    16
[0, 0] p( 7665):     3   15   21  105  219 1095 1533
[0, 0] p( 7666):     2
[0, 0] p( 7667):     1   11   17   41  187  451  697
[0, 0] p( 7668):   108
[0, 0] p( 7669):     1
[0, 0] p( 7670):     2   10   26  118  130  590 1534
[0, 0] p( 7671):     3
[0, 0] p( 7672):     8   56 1096
[0, 0] p( 7673):     1
[0, 0] p( 7674):     6
[0, 0] p( 7675):     1    5   25  307 1535
[0, 0] p( 7676):     4   76  404
[0, 0] p( 7677):     9
[0, 0] p( 7678):     2   22  698
[0, 0] p( 7679):     1    7 1097
[0, 0] p( 7680):  1536
[0, 0] p( 7681):     1
[0, 0] p( 7682):     2   46  334
[0, 0] p( 7683):     3   39  591
[0, 0] p( 7684):     4   68  452
[0, 0] p( 7685):     1    5   29   53  145  265 1537
[0, 0] p( 7686):    18  126 1098
[0, 0] p( 7687):     1
[0, 0] p( 7688):     8  248
[0, 0] p( 7689):     3   33  699
[0, 0] p( 7690):     2   10 1538
[0, 0] p( 7691):     1
[0, 0] p( 7692):    12
[0, 0] p( 7693):     1    7   49  157 1099
[0, 0] p( 7694):     2
[0, 0] p( 7695):    81  405 1539
[0, 0] p( 7696):    16  208  592
[0, 0] p( 7697):     1   43  179
[0, 0] p( 7698):     6
[0, 0] p( 7699):     1
[0, 0] p( 7700):     4   20   28   44  100  140  220  308  700 1100 1540
[0, 0] p( 7701):     3   51  453
[0, 0] p( 7702):     2
[0, 0] p( 7703):     1
[0, 0] p( 7704):    72
[0, 0] p( 7705):     1    5   23   67  115  335 1541
[0, 0] p( 7706):     2
[0, 0] p( 7707):     3   21 1101
[0, 0] p( 7708):     4  164  188
[0, 0] p( 7709):     1   13  593
[0, 0] p( 7710):     6   30 1542
[0, 0] p( 7711):     1   11  701
[0, 0] p( 7712):    32
[0, 0] p( 7713):     9
[0, 0] p( 7714):     2   14   38   58  266  406 1102
[0, 0] p( 7715):     1    5 1543
[0, 0] p( 7716):    12
[0, 0] p( 7717):     1
[0, 0] p( 7718):     2   34  454
[0, 0] p( 7719):     3   93  249
[0, 0] p( 7720):     8   40 1544
[0, 0] p( 7721):     1    7 1103
[0, 0] p( 7722):    54  594  702
[0, 0] p( 7723):     1
[0, 0] p( 7724):     4
[0, 0] p( 7725):     3   15   75  309 1545
[0, 0] p( 7726):     2
[0, 0] p( 7727):     1
[0, 0] p( 7728):    48  336 1104
[0, 0] p( 7729):     1   59  131
[0, 0] p( 7730):     2   10 1546
[0, 0] p( 7731):     9
[0, 0] p( 7732):     4
[0, 0] p( 7733):     1   11   19   37  209  407  703
[0, 0] p( 7734):     6
[0, 0] p( 7735):     1    5    7   13   17   35   65   85   91  119  221  455  595 1105 1547
[0, 0] p( 7736):     8
[0, 0] p( 7737):     3
[0, 0] p( 7738):     2  106  146
[0, 0] p( 7739):     1   71  109
[0, 0] p( 7740):    36  180 1548
[0, 0] p( 7741):     1
[0, 0] p( 7742):     2   14   98  158 1106
[0, 0] p( 7743):     3   87  267
[0, 0] p( 7744):    64  704
[0, 0] p( 7745):     1    5 1549
[0, 0] p( 7746):     6
[0, 0] p( 7747):     1   61  127
[0, 0] p( 7748):     4   52  596
[0, 0] p( 7749):    27  189 1107
[0, 0] p( 7750):     2   10   50   62  250  310 1550
[0, 0] p( 7751):     1   23  337
[0, 0] p( 7752):    24  408  456
[0, 0] p( 7753):     1
[0, 0] p( 7754):     2
[0, 0] p( 7755):     3   15   33  141  165  705 1551
[0, 0] p( 7756):     4   28 1108
[0, 0] p( 7757):     1
[0, 0] p( 7758):    18
[0, 0] p( 7759):     1
[0, 0] p( 7760):    16   80 1552
[0, 0] p( 7761):     3   39  597
[0, 0] p( 7762):     2
[0, 0] p( 7763):     1    7 1109
[0, 0] p( 7764):    12
[0, 0] p( 7765):     1    5 1553
[0, 0] p( 7766):     2   22  706
[0, 0] p( 7767):     9
[0, 0] p( 7768):     8
[0, 0] p( 7769):     1   17  457
[0, 0] p( 7770):     6   30   42  210  222 1110 1554
[0, 0] p( 7771):     1   19  409
[0, 0] p( 7772):     4  116  268
[0, 0] p( 7773):     3
[0, 0] p( 7774):     2   26   46  338  598
[0, 0] p( 7775):     1    5   25  311 1555
[0, 0] p( 7776): 
[0, 0] p( 7777):     1    7   11   77  101  707 1111
[0, 0] p( 7778):     2
[0, 0] p( 7779):     3
[0, 0] p( 7780):     4   20 1556
[0, 0] p( 7781):     1   31  251
[0, 0] p( 7782):     6
[0, 0] p( 7783):     1   43  181
[0, 0] p( 7784):     8   56 1112
[0, 0] p( 7785):     9   45 1557
[0, 0] p( 7786):     2   34  458
[0, 0] p( 7787):     1   13  599
[0, 0] p( 7788):    12  132  708
[0, 0] p( 7789):     1
[0, 0] p( 7790):     2   10   38   82  190  410 1558
[0, 0] p( 7791):     3   21  147  159 1113
[0, 0] p( 7792):    16
[0, 0] p( 7793):     1
[0, 0] p( 7794):    18
[0, 0] p( 7795):     1    5 1559
[0, 0] p( 7796):     4
[0, 0] p( 7797):     3   69  339
[0, 0] p( 7798):     2   14 1114
[0, 0] p( 7799):     1   11  709
[0, 0] p( 7800):    24  120  312  600 1560
[0, 0] p( 7801):     1   29  269
[0, 0] p( 7802):     2   94  166
[0, 0] p( 7803):    27  459
[0, 0] p( 7804):     4
[0, 0] p( 7805):     1    5    7   35  223 1115 1561
[0, 0] p( 7806):     6
[0, 0] p( 7807):     1   37  211
[0, 0] p( 7808):   128
[0, 0] p( 7809):     3   57  411
[0, 0] p( 7810):     2   10   22  110  142  710 1562
[0, 0] p( 7811):     1   73  107
[0, 0] p( 7812):    36  252 1116
[0, 0] p( 7813):     1   13  601
[0, 0] p( 7814):     2
[0, 0] p( 7815):     3   15 1563
[0, 0] p( 7816):     8
[0, 0] p( 7817):     1
[0, 0] p( 7818):     6
[0, 0] p( 7819):     1    7 1117
[0, 0] p( 7820):     4   20   68   92  340  460 1564
[0, 0] p( 7821):     9   99  711
[0, 0] p( 7822):     2
[0, 0] p( 7823):     1
[0, 0] p( 7824):    48
[0, 0] p( 7825):     1    5   25  313 1565
[0, 0] p( 7826):     2   14   26   86  182  602 1118
[0, 0] p( 7827):     3
[0, 0] p( 7828):     4   76  412
[0, 0] p( 7829):     1
[0, 0] p( 7830):    54  270 1566
[0, 0] p( 7831):     1   41  191
[0, 0] p( 7832):     8   88  712
[0, 0] p( 7833):     3   21 1119
[0, 0] p( 7834):     2
[0, 0] p( 7835):     1    5 1567
[0, 0] p( 7836):    12
[0, 0] p( 7837):     1   17  461
[0, 0] p( 7838):     2
[0, 0] p( 7839):     9  117  603
[0, 0] p( 7840):    32  160  224 1120 1568
[0, 0] p( 7841):     1
[0, 0] p( 7842):     6
[0, 0] p( 7843):     1   11   23   31  253  341  713
[0, 0] p( 7844):     4  148  212
[0, 0] p( 7845):     3   15 1569
[0, 0] p( 7846):     2
[0, 0] p( 7847):     1    7   19   59  133  413 1121
[0, 0] p( 7848):    72
[0, 0] p( 7849):     1   47  167
[0, 0] p( 7850):     2   10   50  314 1570
[0, 0] p( 7851):     3
[0, 0] p( 7852):     4   52  604
[0, 0] p( 7853):     1
[0, 0] p( 7854):     6   42   66  102  462  714 1122
[0, 0] p( 7855):     1    5 1571
[0, 0] p( 7856):    16
[0, 0] p( 7857):    81
[0, 0] p( 7858):     2
[0, 0] p( 7859):     1   29  271
[0, 0] p( 7860):    12   60 1572
[0, 0] p( 7861):     1    7 1123
[0, 0] p( 7862):     2
[0, 0] p( 7863):     3
[0, 0] p( 7864):     8
[0, 0] p( 7865):     1    5   11   13   55   65  121  143  605  715 1573
[0, 0] p( 7866):    18  342  414
[0, 0] p( 7867):     1
[0, 0] p( 7868):     4   28 1124
[0, 0] p( 7869):     3  129  183
[0, 0] p( 7870):     2   10 1574
[0, 0] p( 7871):     1   17  463
[0, 0] p( 7872):   192
[0, 0] p( 7873):     1
[0, 0] p( 7874):     2   62  254
[0, 0] p( 7875):     9   45   63  225  315 1125 1575
[0, 0] p( 7876):     4   44  716
[0, 0] p( 7877):     1
[0, 0] p( 7878):     6   78  606
[0, 0] p( 7879):     1
[0, 0] p( 7880):     8   40 1576
[0, 0] p( 7881):     3  111  213
[0, 0] p( 7882):     2   14 1126
[0, 0] p( 7883):     1
[0, 0] p( 7884):   108
[0, 0] p( 7885):     1    5   19   83   95  415 1577
[0, 0] p( 7886):     2
[0, 0] p( 7887):     3   33  717
[0, 0] p( 7888):    16  272  464
[0, 0] p( 7889):     1    7   23   49  161  343 1127
[0, 0] p( 7890):     6   30 1578
[0, 0] p( 7891):     1   13  607
[0, 0] p( 7892):     4
[0, 0] p( 7893):     9
[0, 0] p( 7894):     2
[0, 0] p( 7895):     1    5 1579
[0, 0] p( 7896):    24  168 1128
[0, 0] p( 7897):     1   53  149
[0, 0] p( 7898):     2   22  718
[0, 0] p( 7899):     3
[0, 0] p( 7900):     4   20  100  316 1580
[0, 0] p( 7901):     1
[0, 0] p( 7902):    18
[0, 0] p( 7903):     1    7 1129
[0, 0] p( 7904):    32  416  608
[0, 0] p( 7905):     3   15   51   93  255  465 1581
[0, 0] p( 7906):     2  118  134
[0, 0] p( 7907):     1
[0, 0] p( 7908):    12
[0, 0] p( 7909):     1   11  719
[0, 0] p( 7910):     2   10   14   70  226 1130 1582
[0, 0] p( 7911):    27
[0, 0] p( 7912):     8  184  344
[0, 0] p( 7913):     1   41  193
[0, 0] p( 7914):     6
[0, 0] p( 7915):     1    5 1583
[0, 0] p( 7916):     4
[0, 0] p( 7917):     3   21   39   87  273  609 1131
[0, 0] p( 7918):     2   74  214
[0, 0] p( 7919):     1
[0, 0] p( 7920):   144  720 1584
[0, 0] p( 7921):     1   89
[0, 0] p( 7922):     2   34  466
[0, 0] p( 7923):     3   57  417
[0, 0] p( 7924):     4   28 1132
[0, 0] p( 7925):     1    5   25  317 1585
[0, 0] p( 7926):     6
[0, 0] p( 7927):     1
[0, 0] p( 7928):     8
[0, 0] p( 7929):     9
[0, 0] p( 7930):     2   10   26  122  130  610 1586
[0, 0] p( 7931):     1    7   11   77  103  721 1133
[0, 0] p( 7932):    12
[0, 0] p( 7933):     1
[0, 0] p( 7934):     2
[0, 0] p( 7935):     3   15   69  345 1587
[0, 0] p( 7936):   256
[0, 0] p( 7937):     1
[0, 0] p( 7938):   162 1134
[0, 0] p( 7939):     1   17  467
[0, 0] p( 7940):     4   20 1588
[0, 0] p( 7941):     3
[0, 0] p( 7942):     2   22   38  418  722
[0, 0] p( 7943):     1   13   47  169  611
[0, 0] p( 7944):    24
[0, 0] p( 7945):     1    5    7   35  227 1135 1589
[0, 0] p( 7946):     2   58  274
[0, 0] p( 7947):     9
[0, 0] p( 7948):     4
[0, 0] p( 7949):     1
[0, 0] p( 7950):     6   30  150  318 1590
[0, 0] p( 7951):     1
[0, 0] p( 7952):    16  112 1136
[0, 0] p( 7953):     3   33  723
[0, 0] p( 7954):     2   82  194
[0, 0] p( 7955):     1    5   37   43  185  215 1591
[0, 0] p( 7956):    36  468  612
[0, 0] p( 7957):     1   73  109
[0, 0] p( 7958):     2   46  346
[0, 0] p( 7959):     3   21 1137
[0, 0] p( 7960):     8   40 1592
[0, 0] p( 7961):     1   19  419
[0, 0] p( 7962):     6
[0, 0] p( 7963):     1
[0, 0] p( 7964):     4   44  724
[0, 0] p( 7965):    27  135 1593
[0, 0] p( 7966):     2   14 1138
[0, 0] p( 7967):     1   31  257
[0, 0] p( 7968):    96
[0, 0] p( 7969):     1   13  613
[0, 0] p( 7970):     2   10 1594
[0, 0] p( 7971):     3
[0, 0] p( 7972):     4
[0, 0] p( 7973):     1    7   17   67  119  469 1139
[0, 0] p( 7974):    18
[0, 0] p( 7975):     1    5   11   25   29   55  145  275  319  725 1595
[0, 0] p( 7976):     8
[0, 0] p( 7977):     3
[0, 0] p( 7978):     2
[0, 0] p( 7979):     1   79  101
[0, 0] p( 7980):    12   60   84  228  420 1140 1596
[0, 0] p( 7981):     1   23  347
[0, 0] p( 7982):     2   26  614
[0, 0] p( 7983):     9
[0, 0] p( 7984):    16
[0, 0] p( 7985):     1    5 1597
[0, 0] p( 7986):     6   66  726
[0, 0] p( 7987):     1    7   49  163 1141
[0, 0] p( 7988):     4
[0, 0] p( 7989):     3
[0, 0] p( 7990):     2   10   34   94  170  470 1598
[0, 0] p( 7991):     1   61  131
[0, 0] p( 7992):   216
[0, 0] p( 7993):     1
[0, 0] p( 7994):     2   14 1142
[0, 0] p( 7995):     3   15   39  123  195  615 1599
[0, 0] p( 7996):     4
[0, 0] p( 7997):     1   11  727
[0, 0] p( 7998):     6  186  258
[0, 0] p( 7999):     1   19  421
[0, 0] p( 8000):    64  320 1600
[0, 0] p( 8001):     9   63 1143
[0, 0] p( 8002):     2
[0, 0] p( 8003):     1   53  151
[0, 0] p( 8004):    12  276  348
[0, 0] p( 8005):     1    5 1601
[0, 0] p( 8006):     2
[0, 0] p( 8007):     3   51  471
[0, 0] p( 8008):     8   56   88  104  616  728 1144
[0, 0] p( 8009):     1
[0, 0] p( 8010):    18   90 1602
[0, 0] p( 8011):     1
[0, 0] p( 8012):     4
[0, 0] p( 8013):     3
[0, 0] p( 8014):     2
[0, 0] p( 8015):     1    5    7   35  229 1145 1603
[0, 0] p( 8016):    48
[0, 0] p( 8017):     1
[0, 0] p( 8018):     2   38  422
[0, 0] p( 8019):   729
[0, 0] p( 8020):     4   20 1604
[0, 0] p( 8021):     1   13  617
[0, 0] p( 8022):     6   42 1146
[0, 0] p( 8023):     1   71  113
[0, 0] p( 8024):     8  136  472
[0, 0] p( 8025):     3   15   75  321 1605
[0, 0] p( 8026):     2
[0, 0] p( 8027):     1   23  349
[0, 0] p( 8028):    36
[0, 0] p( 8029):     1    7   31   37  217  259 1147
[0, 0] p( 8030):     2   10   22  110  146  730 1606
[0, 0] p( 8031):     3
[0, 0] p( 8032):    32
[0, 0] p( 8033):     1   29  277
[0, 0] p( 8034):     6   78  618
[0, 0] p( 8035):     1    5 1607
[0, 0] p( 8036):     4   28  164  196 1148
[0, 0] p( 8037):     9  171  423
[0, 0] p( 8038):     2
[0, 0] p( 8039):     1
[0, 0] p( 8040):    24  120 1608
[0, 0] p( 8041):     1   11   17   43  187  473  731
[0, 0] p( 8042):     2
[0, 0] p( 8043):     3   21 1149
[0, 0] p( 8044):     4
[0, 0] p( 8045):     1    5 1609
[0, 0] p( 8046):    54
[0, 0] p( 8047):     1   13  619
[0, 0] p( 8048):    16
[0, 0] p( 8049):     3
[0, 0] p( 8050):     2   10   14   46   50   70  230  322  350 1150 1610
[0, 0] p( 8051):     1   83   97
[0, 0] p( 8052):    12  132  732
[0, 0] p( 8053):     1
[0, 0] p( 8054):     2
[0, 0] p( 8055):     9   45 1611
[0, 0] p( 8056):     8  152  424
[0, 0] p( 8057):     1    7 1151
[0, 0] p( 8058):     6  102  474
[0, 0] p( 8059):     1
[0, 0] p( 8060):     4   20   52  124  260  620 1612
[0, 0] p( 8061):     3
[0, 0] p( 8062):     2   58  278
[0, 0] p( 8063):     1   11  733
[0, 0] p( 8064):  1152
[0, 0] p( 8065):     1    5 1613
[0, 0] p( 8066):     2   74  218
[0, 0] p( 8067):     3
[0, 0] p( 8068):     4
[0, 0] p( 8069):     1
[0, 0] p( 8070):     6   30 1614
[0, 0] p( 8071):     1    7 1153
[0, 0] p( 8072):     8
[0, 0] p( 8073):    27  351  621
[0, 0] p( 8074):     2   22  734
[0, 0] p( 8075):     1    5   17   19   25   85   95  323  425  475 1615
[0, 0] p( 8076):    12
[0, 0] p( 8077):     1   41  197
[0, 0] p( 8078):     2   14 1154
[0, 0] p( 8079):     3
[0, 0] p( 8080):    16   80 1616
[0, 0] p( 8081):     1
[0, 0] p( 8082):    18
[0, 0] p( 8083):     1   59  137
[0, 0] p( 8084):     4  172  188
[0, 0] p( 8085):     3   15   21   33  105  147  165  231  735 1155 1617
[0, 0] p( 8086):     2   26  622
[0, 0] p( 8087):     1
[0, 0] p( 8088):    24
[0, 0] p( 8089):     1
[0, 0] p( 8090):     2   10 1618
[0, 0] p( 8091):     9  261  279
[0, 0] p( 8092):     4   28   68  476 1156
[0, 0] p( 8093):     1
[0, 0] p( 8094):     6  114  426
[0, 0] p( 8095):     1    5 1619
[0, 0] p( 8096):    32  352  736
[0, 0] p( 8097):     3
[0, 0] p( 8098):     2
[0, 0] p( 8099):     1    7   13   89   91  623 1157
[0, 0] p( 8100):   324 1620
[0, 0] p( 8101):     1
[0, 0] p( 8102):     2
[0, 0] p( 8103):     3  111  219
[0, 0] p( 8104):     8
[0, 0] p( 8105):     1    5 1621
[0, 0] p( 8106):     6   42 1158
[0, 0] p( 8107):     1   11   67  121  737
[0, 0] p( 8108):     4
[0, 0] p( 8109):     9  153  477
[0, 0] p( 8110):     2   10 1622
[0, 0] p( 8111):     1
[0, 0] p( 8112):    48  624
[0, 0] p( 8113):     1    7   19   61  133  427 1159
[0, 0] p( 8114):     2
[0, 0] p( 8115):     3   15 1623
[0, 0] p( 8116):     4
[0, 0] p( 8117):     1
[0, 0] p( 8118):    18  198  738
[0, 0] p( 8119):     1   23  353
[0, 0] p( 8120):     8   40   56  232  280 1160 1624
[0, 0] p( 8121):     3
[0, 0] p( 8122):     2   62  262
[0, 0] p( 8123):     1
[0, 0] p( 8124):    12
[0, 0] p( 8125):     1    5   13   25   65  125  325  625 1625
[0, 0] p( 8126):     2   34  478
[0, 0] p( 8127):    27  189 1161
[0, 0] p( 8128):    64
[0, 0] p( 8129):     1   11  739
[0, 0] p( 8130):     6   30 1626
[0, 0] p( 8131):     1   47  173
[0, 0] p( 8132):     4   76  428
[0, 0] p( 8133):     3
[0, 0] p( 8134):     2   14   98  166 1162
[0, 0] p( 8135):     1    5 1627
[0, 0] p( 8136):    72
[0, 0] p( 8137):     1   79  103
[0, 0] p( 8138):     2   26  626
[0, 0] p( 8139):     3
[0, 0] p( 8140):     4   20   44  148  220  740 1628
[0, 0] p( 8141):     1    7 1163
[0, 0] p( 8142):     6  138  354
[0, 0] p( 8143):     1   17  479
[0, 0] p( 8144):    16
[0, 0] p( 8145):     9   45 1629
[0, 0] p( 8146):     2
[0, 0] p( 8147):     1
[0, 0] p( 8148):    12   84 1164
[0, 0] p( 8149):     1   29  281
[0, 0] p( 8150):     2   10   50  326 1630
[0, 0] p( 8151):     3   33   39   57  429  627  741
[0, 0] p( 8152):     8
[0, 0] p( 8153):     1   31  263
[0, 0] p( 8154):    54
[0, 0] p( 8155):     1    5    7   35  233 1165 1631
[0, 0] p( 8156):     4
[0, 0] p( 8157):     3
[0, 0] p( 8158):     2
[0, 0] p( 8159):     1   41  199
[0, 0] p( 8160):    96  480 1632
[0, 0] p( 8161):     1
[0, 0] p( 8162):     2   14   22  106  154  742 1166
[0, 0] p( 8163):     9
[0, 0] p( 8164):     4   52  628
[0, 0] p( 8165):     1    5   23   71  115  355 1633
[0, 0] p( 8166):     6
[0, 0] p( 8167):     1
[0, 0] p( 8168):     8
[0, 0] p( 8169):     3   21 1167
[0, 0] p( 8170):     2   10   38   86  190  430 1634
[0, 0] p( 8171):     1
[0, 0] p( 8172):    36
[0, 0] p( 8173):     1   11  743
[0, 0] p( 8174):     2  122  134
[0, 0] p( 8175):     3   15   75  327 1635
[0, 0] p( 8176):    16  112 1168
[0, 0] p( 8177):     1   13   17   37  221  481  629
[0, 0] p( 8178):     6  174  282
[0, 0] p( 8179):     1
[0, 0] p( 8180):     4   20 1636
[0, 0] p( 8181):    81
[0, 0] p( 8182):     2
[0, 0] p( 8183):     1    7   49  167 1169
[0, 0] p( 8184):    24  264  744
[0, 0] p( 8185):     1    5 1637
[0, 0] p( 8186):     2
[0, 0] p( 8187):     3
[0, 0] p( 8188):     4   92  356
[0, 0] p( 8189):     1   19  431
[0, 0] p( 8190):    18   90  126  234  630 1170 1638
[0, 0] p( 8191):     1
[0, 0] p( 8192): 

None found for these integers:
1 = 
2 = 2 
3 = 3 
4 = 2^2 
6 = 2 × 3 
8 = 2^3 
9 = 3^2 
12 = 2^2 × 3 
16 = 2^4 
18 = 2 × 3^2 
24 = 2^3 × 3 
27 = 3^3 
32 = 2^5 
36 = 2^2 × 3^2 
48 = 2^4 × 3 
54 = 2 × 3^3 
64 = 2^6 
72 = 2^3 × 3^2 
81 = 3^4 
96 = 2^5 × 3 
108 = 2^2 × 3^3 
128 = 2^7 
144 = 2^4 × 3^2 
162 = 2 × 3^4 
192 = 2^6 × 3 
216 = 2^3 × 3^3 
243 = 3^5 
256 = 2^8 
288 = 2^5 × 3^2 
324 = 2^2 × 3^4 
384 = 2^7 × 3 
432 = 2^4 × 3^3 
486 = 2 × 3^5 
512 = 2^9 
576 = 2^6 × 3^2 
648 = 2^3 × 3^4 
729 = 3^6 
768 = 2^8 × 3 
864 = 2^5 × 3^3 
972 = 2^2 × 3^5 
1024 = 2^10 
1152 = 2^7 × 3^2 
1296 = 2^4 × 3^4 
1458 = 2 × 3^6 
1536 = 2^9 × 3 
1728 = 2^6 × 3^3 
1944 = 2^3 × 3^5 
2048 = 2^11 
2187 = 3^7 
2304 = 2^8 × 3^2 
2592 = 2^5 × 3^4 
2916 = 2^2 × 3^6 
3072 = 2^10 × 3 
3456 = 2^7 × 3^3 
3888 = 2^4 × 3^5 
4096 = 2^12 
4374 = 2 × 3^7 
4608 = 2^9 × 3^2 
5184 = 2^6 × 3^4 
5832 = 2^3 × 3^6 
6144 = 2^11 × 3 
6561 = 3^8 
6912 = 2^8 × 3^3 
7776 = 2^5 × 3^5 
8192 = 2^13 
Breaks:
0
0

Pattern:
[12, 24, 36, 48, 72, 96, 108, 144, 192, 216, 288, 324, 384, 432, 576, 648, 768, 864, 972, 1152, 1296, 1536] Dividing each by 12 gives <1, 2, 3, 4, 6, 8, 9, 12, 16, 18, 24, 27, 32, 36, 48, 54, 64, 72, 81, 96, 108, 128, >
[1]
[2]
[3, 9, 27, 81, 243, 729]
[4, 8, 16, 32, 64, 128, 256, 512, 1024]
[1]
[6, 18, 54, 162, 486, 1458] Dividing each by 6 gives <1, 3, 9, 27, 81, 243, >
[1]
[4, 8, 16, 32, 64, 128, 256, 512, 1024]
[3, 9, 27, 81, 243, 729]
[2]
[1]

Process finished with exit code 0
*/
