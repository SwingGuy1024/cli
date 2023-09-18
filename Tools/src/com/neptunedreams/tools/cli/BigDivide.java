package com.neptunedreams.tools.cli;

import java.math.BigInteger;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 8/15/23
 * <p>Time: 4:15 AM
 *
 * @author Miguel Mu–oz
 */
public enum BigDivide {
  ;
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: <n> <d> -- where n and d are the numerator and divisor");
      System.exit(0);
    }
    BigInteger num = load(args[0]);
    BigInteger div = load(args[1]);
    BigInteger quotient = num.divide(div);
    final BigInteger remainder = num.mod(div);
    if (remainder.equals(BigInteger.ZERO)) {
      System.out.println(quotient);
    } else {
      System.out.printf("%s%nR %s%n", quotient, remainder); // NON-NLS
    }
  }
  
  private static BigInteger load(String input) {
    try {
      return new BigInteger(input);
    } catch (NumberFormatException e) {
      if (Character.isLetter(input.charAt(0))) {
        return getCandidate(Integer.parseInt(input.substring(1)));
      }
    }
    throw new IllegalArgumentException(input);
  }

  private static BigInteger getCandidate(int k) {
    BigInteger product = BigInteger.ONE;
    BigInteger sum = BigInteger.ZERO;
    for (int i = 0; i < k; ++i) {
      sum = sum.add(product);
      product = product.multiply(BigInteger.TEN);
    }
    sum = sum
        .multiply(BigInteger.valueOf(9))
        .multiply(product)
        .add(BigInteger.ONE);
    return sum;
  }

}
