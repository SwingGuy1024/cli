//skip
package com.neptunedreams.tools.exp.math;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>This was inspired by the PrimeCurios entry for 9999999900000001. It makes this claim:</p>
 * <p>9999999900000001 is the 8th known prime, p, such that (p)<sup>2</sup> divides (p)<sub>3</sub></p>
 * <p>Here, p<sub>3</sub> means p concatenated with itself 3 times. (See <a href="https://math.stackexchange.com/questions/4775425/prime-numbers-p-for-which-p2-divides-p-3-what-does-p-3-mean-h>My stackExchange
 * question</a> about this. The answer is documented <a href="https://oeis.org/A147554">here</a></p>
 * <p>I wrote this class to find out if other members of that family have this same property</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 9/25/23
 * <p>Time: 12:03 PM
 *
 * @author Miguel Mu–oz
 */
public enum PrimeConcatenators {
  ;
  public static void main(String[] args) {
    for (int i=1; i<=100; ++i) {
      BigInteger p = p(i);
      BigInteger pSq = p.multiply(p);
      BigInteger p_3 = new BigInteger(String.format("%s%s%s", p, p, p));
      BigInteger remainder = p_3.remainder(pSq);
      System.out.printf("%4d: %c %s%n", i, remainder.equals(BigInteger.ZERO)? '*' : ' ', remainder);
    }

    System.out.println();
    Map<Long, Integer> factorMap = new HashMap<>();
    long start = System.currentTimeMillis();
    long split = start;
    for (long i=1; i<10000000000L; ++i) {
      BigInteger p = BigInteger.valueOf(i);
      BigInteger pSq = p.multiply(p);
      BigInteger p_3 = new BigInteger(String.format("%s%s%s", p, p, p));
      BigInteger remainder = p_3.remainder(pSq);
      if (remainder.equals(BigInteger.ZERO)) {
        long now = System.currentTimeMillis();

        final long splitTime = now - split;
        System.out.printf("%s ms -- %10d: %s%n", format(splitTime), i, p.isProbablePrime(1)? "" : findPrimes(i, factorMap));
        split = now;
      }
    }
    long end = System.currentTimeMillis();
    //noinspection TooBroadScope
    final long duration = end - start;

    System.out.println();
    System.out.printf("Prime Factors: %s%n", factorMap); // NON-NLS
    
    System.out.printf("%d ms%n", duration); // NON-NLS
    System.out.printf("%s%n", format(duration)); // NON-NLS
  }
  
  private static BigInteger p(int i) {
    char[] nineArray = new char[i];
    Arrays.fill(nineArray, '9');
    BigInteger allNines = new BigInteger(new String(nineArray));
    return allNines.multiply(allNines).add(allNines).add(BigInteger.ONE);
  }

  private static String findPrimes(long arg, Map<Long, Integer> primeFactors) {
    int exp = 0;
    while ((arg % 2L) == 0) {
      exp++;
      arg /= 2L;
    }
    StringBuilder builder = new StringBuilder();
    printExp(2, exp, builder);
    long odd = 3;
    while (odd <= arg) {
      exp = 0;
      while ((arg % odd) == 0) {
        exp++;
        arg /= odd;
      }
      printExp(odd, exp, builder);
      if (exp > 0) {
        primeFactors.merge(odd, 1, Integer::sum);
      }
      odd += 2;
    }
    if (builder.toString().endsWith(", ")) {
      final int length = builder.length();
      builder.delete(length -2, length);
    }
    return builder.toString();
  }

  private static void printExp(long base, int exp, StringBuilder b) {
    switch (exp) {
      case 0:
        break;
      case 1:
        b.append(base).append(", ");
        break;
      default:
        b.append(base)
            .append('^')
            .append(exp)
            .append(", ");
//        System.out.printf("%d^%d%n", base, exp); // NON-NLS
        break;
    }
  }

  /**
   * Convert milliseconds to HMS
   * @param ms time in milliseconds
   * @return A String of the form h:MM:ss.mmm
   */
  public static String format(long ms) {
    long tail = ms % 1000L;
    long allSeconds = ms / 1000L;
    long seconds = allSeconds % 60L;
    long allMinutes = allSeconds / 60L;
    long minutes = allMinutes % 60L;
    long allHours = allMinutes / 60L;
    long hours = allHours % 24L;
    if (hours == 0) {
      return String.format("   %2d:%02d.%03d", minutes, seconds, tail);
    }
    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, tail);
  }
}
