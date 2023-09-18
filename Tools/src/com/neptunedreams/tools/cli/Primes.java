// use: primes
package com.neptunedreams.tools.cli;

/**
 * 
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 6/9/22
 * <p>Time: 2:12 PM
 *
 * @author Miguel Mu–oz
 */
@SuppressWarnings("HardCodedStringLiteral")
public enum Primes {
  ;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Prints out the prime factors of the input number. Works reasonably quickly for n < 2^31.");
      System.err.println("For 2^31-1 = 2147483647, it takes about 10 seconds on my Mac.");
      System.err.printf("Usage: primes n%n");
      System.exit(-1);
    }

    long arg = Long.parseLong(args[0]);
    findPrimes(arg);
  }

  private static void findPrimes(long arg) {
    arg = (arg < 0L) ? -arg : arg;
    int exp = 0;
    while ((arg % 2L) == 0) {
      exp++;
      arg /= 2L;
    }
    printExp(2, exp);
    long odd = 3;
    while (odd <= arg) {
      exp = 0;
      while ((arg % odd) == 0) {
        exp++;
        arg /= odd;
      }
      printExp(odd, exp);
      odd += 2;
    }
  }

  private static void printExp(long base, int exp) {
    switch (exp) {
      case 0:
        break;
      case 1:
        System.out.println(base);
        break;
      default:
        System.out.printf("%d^%d%n", base, exp); // NON-NLS
        break;
    }
  }
}
