package com.neptunedreams.tools.cli;

import java.math.BigInteger;

/**
 * <p>This generates a nine-number of the form 999001, where there's a string of nines that make up half the digits.
 * The remaining digits are all zeroes except for a final one. The number of digits is exactly twice the number
 * of nines.</p)
 * <p>Prime factors:</p>
 * 
 * <pre>
 *   <b>n24</b>
 *   8929        (111994624258035614290513943330720125433979169)
 *   111994624258035614290513943330720125433979169
 *   
 *   <b>n27</b>
 *   1459
 * </pre>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 8/13/23
 * <p>Time: 4:59 PM
 *
 * @author Miguel Mu–oz
 */
public enum NinePrimes {
  ;
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: NinePrimes <n>   (where n is an integer > 0)");
      System.exit(1);
    }
    System.out.println(getCandidate(Integer.parseInt(args[0])));
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
}
