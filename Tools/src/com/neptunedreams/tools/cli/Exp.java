// skip
package com.neptunedreams.tools.cli;

import java.math.BigInteger;
import java.util.TreeMap;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 8/13/23
 * <p>Time: 8:52 PM
 *
 * @author Miguel Mu–oz
 */
public enum Exp {
  ;
  public static final BigInteger NINE = new BigInteger("9");

  public static void main(String[] args) {
    BigInteger n27 = getCandidate(27);
    TreeMap<Integer, BigInteger> map = new TreeMap<>();
    int[] seeds = {
        189,
        297,
        351,
        459,
        513,
        621,
        873,
        837,
        999
    };
    for (int seed: seeds) {
      map.put(seed, getCandidate(seed));
    }
    int previous = 27;
    for (Integer seed: map.keySet()) {
      final boolean isFactor = map.get(seed).mod(n27).equals(BigInteger.ZERO);
      final int diff = isFactor ? (seed - previous) : 0;
      System.out.printf("%d (%3d): %b%n", seed, diff, isFactor); // NON-NLS
      if (isFactor) {
        previous = seed;
      }
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
        .multiply(NINE)
        .multiply(product)
        .add(BigInteger.ONE);
    return sum;
  }
}
