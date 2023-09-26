// skip
package com.neptunedreams.tools.cli;

import java.math.BigInteger;
import java.util.Date;

/**
 * Test which numbers of the form 999001 (where n = 3) are prime. As far as I can tell, only numbers 2, 4, 6, and 8
 * are prime. This tests all numbers for n = 1 to limit, which can be changed. This takes a long time for large limits.
 * when limit is 2000, it takes about 12 minutes. When limit is 4000, it takes 12 hours.
 * Here's how long it takes for three values of n:
 * <pre>
 *   <u>n</u>    <u>Time</u>
 *   1000  0:00:53 
 *   2000  0:12:28
 *   3000  1:22:00
 *   4000 12:00:00
 * </pre>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 8/10/23
 * <p>Time: 8:53 PM
 *
 * @author Miguel Mu–oz
 */
public enum PrimeGen {
  ;
  private static final BigInteger NINE = new BigInteger("9");
  public static void main(String[] args) {
    System.out.println(new Date());
    int limit = 1_000;
    System.out.printf("Limit: %d%n", limit); // NON-NLS
    long start = System.currentTimeMillis();
    for (int i=0; i<limit; ++i) {
      process(i);
    }
    long end = System.currentTimeMillis();
    long duration = end - start;
    System.out.printf("%d numbers took %d ms%n", limit, duration); // NON-NLS
    System.out.println(format(duration));
  }
  
  private static void process(int i) {
    BigInteger candidate = getCandidate(i);
    if (candidate.isProbablePrime(100)) {
      System.out.printf("n = %6d: %s%n", i, candidate); // NON-NLS
//    } else if (candidate.mod(SEVEN).equals(BigInteger.ZERO)) {
//      System.out.printf("7 7 Not Prime: %s%n", candidate); // NON-NLS
//    } else {
//      System.out.printf("*** Not Prime: %s%n", candidate); // NON-NLS
    }
  }
  
  private static BigInteger getCandidate(int k) {
//    int k = 2*n;
    BigInteger product = BigInteger.ONE;
    BigInteger sum = BigInteger.ZERO;
    for (int i=0; i< k; ++i) {
      product = product.multiply(BigInteger.TEN);
      sum = sum.add(product);
    }
    sum = sum.multiply(NINE);
    sum = sum.multiply(product).divide(BigInteger.TEN);
    sum = sum.add(BigInteger.ONE);
    return sum;
  }
  
  public static String format(long ms) {
    long tail = ms % 1000L;
    long allSeconds = ms / 1000L;
    long seconds = allSeconds % 60L;
    long allMinutes = allSeconds / 60L;
    long minutes = allMinutes % 60L;
    long allHours = allMinutes / 60L;
    long hours = allHours % 24L;
    long allDays = allHours / 24L;
    return String.format("%d days %02d:%02d:%02d.%03d", allDays, hours, minutes, seconds, tail);
  }
}
