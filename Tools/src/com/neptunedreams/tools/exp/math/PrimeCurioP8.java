package com.neptunedreams.tools.exp.math;

import java.math.BigInteger;

/**
 * <p>This was written to test a claim on the <a href="https://t5k.org/curios/page.php?short=9999999900000001">Prime
 * Curios</a> web site for the number '''9999999900000001'''. Here's the claim:</p>
 * <p><i>9999999900000001 is the 8th known prime, p, such that (p)<sup>2</sup> divides (p)<sub>3</sub>. Can you find
 * other cases like this?</i> </p>
 * <p> In this claim, the notation (p)<sub>3</sub> means the digits in p are repeated three times. So if p is 13,
 * (p)<sub>3</sub> is 131313.</p>
 * <p><b>Also of Note</b></p>
 * <p>The sixth member of this family, 99990001, when repeated 3 times, is divisible by the first four members of this
 * family, 3, 13, 37, and 9901.</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/3/23
 * <p>Time: 1:58 AM
 *
 * @author Miguel Mu–oz
 */
public enum PrimeCurioP8 {
  ;

  public static void main(String[] args) {
    int index = 0;
    BigInteger candidate = BigInteger.ONE;
    do {
      candidate = candidate.nextProbablePrime();
      BigInteger cSq = candidate.multiply(candidate);
      //noinspection CallToNumericToString
      String s = candidate.toString();
      BigInteger c3 = new BigInteger(s+s+s);
      if (c3.mod(cSq).equals(BigInteger.ZERO)) {
        System.out.printf("%d: For %s, %s / %s = %s%n", ++index, candidate, c3, cSq, c3.divide(cSq)); // NON-NLS
      }
    } while (index < 8);
  }
}
