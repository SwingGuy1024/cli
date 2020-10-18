package com.neptunedreams.tools.exp.codeleek;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/14/20
 * <p>Time: 5:24 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class MultiplyTwoNumbers {
  static final String DIGITS = "0123456789";

  public static void main(String[] args) {
    MultiplyTwoNumbers multiplyTwoNumbers = new MultiplyTwoNumbers();
    String m1 = "12345679";
    String m2 = "45";
    String m3 = "142857";
    String m4 = "5";
    String m5 = "66";
    String m6 = "384";
    String m7 = "27";
    multiply(multiplyTwoNumbers, "0", "0");
    multiply(multiplyTwoNumbers, m6, m7);
    multiply(multiplyTwoNumbers, m1, m2);
    multiply(multiplyTwoNumbers, m3, m4);
    multiply(multiplyTwoNumbers, m3, m5);
    multiply(multiplyTwoNumbers, m3, m6);
    multiply(multiplyTwoNumbers, m2, m5);
    multiply(multiplyTwoNumbers, "4567", "32767");
    multiply(multiplyTwoNumbers, "000009135498765106486877404868406868406860684068", "048984098409898098294916584686488789985");
  }

  private static void multiply(final MultiplyTwoNumbers multiplyTwoNumbers, final String m1, final String m2) {
    String r1 = multiplyTwoNumbers.multiply(m2, m1);
    System.out.println(m1);
    System.out.println(m2);
    System.out.println(r1);
    System.out.println(multiplyTwoNumbers.multiply(m1, m2));
    
    BigInteger b1 = new BigInteger(m1);
    BigInteger verify = b1.multiply(new BigInteger(m2));
    System.out.printf("%s Verify%n", verify); // NON-NLS
    if (!verify.toString().equals(r1)) {
      throw new AssertionError("Mismatch: verify != r1");
    }
    System.out.println();
  }

  public String multiply(String num1, String num2) {
    // least significant digits come first
    int[] d1 = getDigits(num1);
    int[] d2 = getDigits(num2);

    List<int[]> partialSums = new LinkedList<>();
    for (final int j : d1) {
      partialSums.add(scale(d2, j));
    }

    int[] result = new int[num1.length() + num2.length()];
//    Arrays.fill(result, 0);
    int tenShift = 0;
    for (int[] partial : partialSums) {
      for (int i = 0; i < partial.length; i++) {
        addToResult(partial[i], result, i+tenShift);
      }
      tenShift++;
    }
    StringBuilder builder = new StringBuilder();
    for (int i = result.length - 1; i >= 0; --i) {
      builder.append(result[i]);
    }
    //noinspection MagicCharacter
    while ((builder.charAt(0) == '0') && (builder.length() > 1)) {
      builder.deleteCharAt(0);
    }
    return builder.toString();
  }

  private static int[] scale(int[] number, int multiple) {
    int[] result = new int[number.length + 1];
//    Arrays.fill(result, 0); // not sure if necessary!
    for (int i = 0; i < number.length; ++i) {
      addToResult(number[i] * multiple, result, i);
    }
    return result;
  }

  private static void addToResult(int product, int[] result, int index) {
    int ones = product % 10;
    int tens = product / 10;
    result[index] += ones;
    if (result[index] > 9) {
      result[index + 1]++;
      result[index] -= 10;
    }
    if (tens > 0) {
      result[index + 1] += tens;
    }
  }

  static int[] getDigits(String num) {
    int len = num.length();
    int[] result = new int[len];
    for (int i = 0; i < num.length(); ++i) {
      result[len - i - 1] = DIGITS.indexOf(num.charAt(i));
    }
    return result;
  }
}
