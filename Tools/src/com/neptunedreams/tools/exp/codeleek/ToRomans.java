package com.neptunedreams.tools.exp.codeleek;

import java.util.Arrays;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/4/21
 * <p>Time: 1:36 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public class ToRomans {
  private static int[] divisors = {1000, 500, 100, 50, 10, 5, 1};
  private static char[] romans = {'M', 'D', 'C', 'L', 'X', 'V', 'I'};

  public String intToRoman(final int num) {
    int val = num;
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < divisors.length; i += 2) {
      int mod = divisors[i];
      int v = val / mod;
      if (v > 0) {
        if (v == 9) {
          builder.append(romans[i]);
          builder.append(romans[i - 2]);
        } else if (v >= 5) {
          builder.append(romans[i - 1]);
          builder.append(repeat(romans[i], v-5));
        } else if (v == 4) {
          builder.append(romans[i]);
          builder.append(romans[i - 1]);
        } else {
          builder.append(repeat(romans[i], v));
        }
        val -= v * divisors[i];
      }
    }
    return builder.toString();
  }
  
  private static String repeat(char c, int count) {
    char[] data = new char[count];
    Arrays.fill(data, c);
    return new String(data);
  }

  public static void main(String[] args) {
    int[] testCases = {1, 4, 5, 6, 8, 9, 10, 11, 13, 14, 15, 30, 32, 36, 39, 125, 234, 345, 456, 567, 678, 789, 891, 912, 1234, 2345, 3456, 1567, 2678, 3789, 2001, 2493, 2587, 3888, 3999};
    ToRomans s = new ToRomans();
    for (int test : testCases) {
      System.out.printf("%4d = %s%n", test, s.intToRoman(test));
    }
  }
}