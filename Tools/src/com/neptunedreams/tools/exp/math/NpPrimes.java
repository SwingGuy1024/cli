// exclude
package com.neptunedreams.tools.exp.math;

import java.math.BigInteger;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 9/30/23
 * <p>Time: 11:33 PM
 *
 * @author Miguel Mu–oz
 */
public enum NpPrimes {
  ;
  private static BigInteger build(int nines) {
    StringBuilder builder = new StringBuilder(nines * 2);
    builder.append("9".repeat(nines));
    builder.append("0".repeat(nines - 1));
    builder.append('1');
    return new BigInteger(builder.toString());
  }

  public static void main(String[] args) {
    ThreeSmooth threeSmooth = new ThreeSmooth(0);
    int index = 0;
    while (threeSmooth.hasNext()) {
      long start = System.currentTimeMillis();
      long i = threeSmooth.next();
      System.out.printf("%4d: %d", ++index, i);
      BigInteger pNum = build((int)i);
      if (pNum.isProbablePrime(1)) {
        System.out.print(" produces prime."); // NON-NLS
        if (i > 8) {
          long trial = System.currentTimeMillis() - start;
          System.out.printf(" (%s)%n", formatTime(trial)); // NON-NLS
          return;
        }
      }
      long trial = (System.currentTimeMillis() - start);
      System.out.printf("(%s) %d%n", formatTime(trial), trial); // NON-NLS
    }
  }

  private static boolean loop(ThreeSmooth threeSmooth) {
    long start = System.currentTimeMillis();
    long i= threeSmooth.next();
    System.out.print(i);
    BigInteger pNum = build((int)i);
    if (pNum.isProbablePrime(1)) {
      System.out.print(" produces prime."); // NON-NLS
      if (i > 8) {
        long trial = System.currentTimeMillis() - start;
        System.out.printf(" (%s)%n", formatTime(trial)); // NON-NLS
        return true;
      }
    }
    long trial = (System.currentTimeMillis() - start);
    System.out.printf(" (%s) %d%n", formatTime(trial), trial); // NON-NLS
    return false;
  }

  private static String formatTime(long millis) {
    long seconds = millis / 1000L;
    long minutes = seconds /60;
    seconds %= 60;
    long hours = minutes / 60;
    minutes %= 60;
    long frac = millis % 1000L;
    return String.format("%d:%02d:%02d.%03d", hours, minutes, seconds, frac);
  }
}
