package com.neptunedreams.tools.cli;

import java.math.BigInteger;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 7/19/23
 * <p>Time: 2:40 AM
 *
 * @author Miguel Mu–oz
 */
public enum BigPrimes {
  ;

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Prints out the prime factors of the input number. Works reasonably quickly for n < 2^31.");
      System.err.println("For 2^31-1 = 2147483647, it takes about 10 seconds on my Mac.");
      System.err.printf("Usage: primes n%n");
      System.exit(-1);
    }

    BigInteger bigArg = parse(String.join("", args)); // concatenates all arguments
    if (bigArg.compareTo(BigInteger.ZERO) < 0) {
      bigArg = bigArg.negate();
    }
    long start = System.currentTimeMillis();
    int certainty = 1;
    System.out.printf("Certainty: %d%n", certainty);
    BigInteger factor = new BigInteger("2");
    while (factor.compareTo(bigArg) <= 0) { // while (factor < bigArg)
      // Without this test, it takes about a half hour to determine that 715827881 is prime.
      // Doing this all with integers, that takes about 6 seconds.
      //
      // For 2147483647214748364721474836472147483647
      // It takes 126:40.186 to complete when certainty is 1.
      if (bigArg.isProbablePrime(certainty)) {
        printExp(bigArg, 1, BigInteger.ZERO);
        break;
      } else {
        int exp = 0;
        while ((bigArg.mod(factor)).equals(BigInteger.ZERO)) {
          exp++;
          bigArg = bigArg.divide(factor);
        }
        printExp(factor, exp, bigArg);
        factor = factor.nextProbablePrime();
      }
    }
    long duration = System.currentTimeMillis() - start;
//    NumberFormat format = NumberFormat.getNumberInstance();
    System.out.printf("Took %d milliseconds = %s%n", duration, format(duration)); // NON-NLS
  }
  
  private static BigInteger parse(String s) {
    try {
      return new BigInteger(s);
    } catch (NumberFormatException e) {
      if (Character.isLetter(s.charAt(0))) {
        int p9 = Integer.parseInt(s.substring(1));
        final BigInteger candidate = getCandidate(p9);
        System.out.printf("bigPrimes %s%n", candidate); // NON-NLS
        return candidate;
      }
      throw e;
    }
  }

  private static BigInteger getCandidate(int k) {
    BigInteger product = BigInteger.ONE;
    BigInteger sum = BigInteger.ZERO;
    for (int i = 0; i < k; ++i) {
      sum = sum.add(product);
      product = product.multiply(BigInteger.TEN);
    }
    sum = sum
        .multiply(BigInteger.valueOf(9L))
        .multiply(product)
        .add(BigInteger.ONE);
    return sum;
  }

  private static String format(long ms) {
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


  private static void printExp(BigInteger base, int exp, BigInteger opposite) {
    switch (exp) {
      case 0:
        break;
      case 1:
        if (opposite.equals(BigInteger.ZERO)) {
          System.out.printf("%s%n", base); // NON-NLS
        } else {
          System.out.printf("%s        (%s)%n", base, opposite);
        }
        break;
      default:
        System.out.printf("%s^%d    (%s)%n", base, exp, opposite);
        break;
    }
  }
}
