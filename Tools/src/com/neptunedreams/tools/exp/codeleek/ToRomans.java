package com.neptunedreams.tools.exp.codeleek;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

  /**
   * Convert a positive, non-zero integer to Roman numerals.
   * Roman numerals are usually written largest to smallest from left to right. However, the numeral for four is not IIII. Instead,
   * the number four is written as IV. Because the one is before the five we subtract it making four. The same principle applies to
   * the number nine, which is written as IX. There are six instances where subtraction is used:
   *
   * I can be placed before V (5) and X (10) to make 4 and 9. 
   * X can be placed before L (50) and C (100) to make 40 and 90. 
   * C can be placed before D (500) and M (1000) to make 400 and 900.
   *
   * Zero cannot be expressed in Roman numerals. The Romans never invented zero. (Yeah. I'm serious.)
   * @param num The number to convert
   * @return A string expressing the number in Roman numerals
   */
  public String intToRoman(final int num) {
    if ((num < 1) || (num > 3999)) {
      throw new IllegalArgumentException(String.format("Value %d is out of range (1 - 3999)", num));
    }
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
  
  private static char[] repeat(char c, int count) {
    char[] data = new char[count];
    Arrays.fill(data, c);
    return data;
  }

  public static void main(String[] args) {
    int[] testCases = {1, 4, 5, 6, 8, 9, 10, 11, 13, 14, 15, 30, 32, 36, 39, 125, 234, 345, 456, 567, 678, 789, 890, 891, 900, 901, 912, 1234, 2345, 3456, 1567, 2678, 3789, 2001, 2493, 2587, 3888, 3999};
    ToRomans s = new ToRomans();
    for (int test : testCases) {
      System.out.printf("%4d = %s%n", test, s.intToRoman(test));
    }
    test();
  }
  
  private static void test() {
    Map<Integer, String> dataMap = new HashMap<>();
    load(dataMap, 1, "I");
    load(dataMap, 4, "IV");
    load(dataMap, 5, "V");
    load(dataMap, 6, "VI");
    load(dataMap, 8, "VIII");
    load(dataMap, 9, "IX");
    load(dataMap, 10, "X");
    load(dataMap, 11, "XI");
    load(dataMap, 13, "XIII");
    load(dataMap, 14, "XIV");
    load(dataMap, 15, "XV");
    load(dataMap, 30, "XXX");
    load(dataMap, 32, "XXXII");
    load(dataMap, 36, "XXXVI");
    load(dataMap, 39, "XXXIX");
    load(dataMap, 125, "CXXV");
    load(dataMap, 234, "CCXXXIV");
    load(dataMap, 345, "CCCXLV");
    load(dataMap, 456, "CDLVI");
    load(dataMap, 567, "DLXVII");
    load(dataMap, 678, "DCLXXVIII");
    load(dataMap, 789, "DCCLXXXIX");
    load(dataMap, 890, "DCCCXC");
    load(dataMap, 891, "DCCCXCI");
    load(dataMap, 900, "CM");
    load(dataMap, 901, "CMI");
    load(dataMap, 912, "CMXII");
    load(dataMap, 1234, "MCCXXXIV");
    load(dataMap, 2345, "MMCCCXLV");
    load(dataMap, 3456, "MMMCDLVI");
    load(dataMap, 1567, "MDLXVII");
    load(dataMap, 2678, "MMDCLXXVIII");
    load(dataMap, 3789, "MMMDCCLXXXIX");
    load(dataMap, 2001, "MMI");
    load(dataMap, 2493, "MMCDXCIII");
    load(dataMap, 2587, "MMDLXXXVII");
    load(dataMap, 3888, "MMMDCCCLXXXVIII");
    load(dataMap, 3999, "MMMCMXCIX");
    ToRomans toRomans = new ToRomans();
    for (Integer i: dataMap.keySet()) {
      String expected = dataMap.get(i);
      String actual = toRomans.intToRoman(i);
//      assertEquals(String.valueOf(i), expected, actual);
      if (!expected.equals(actual)) { // in JUnit, replace this block with the line commented out above.
        throw new AssertionError(String.format("For %d, expected %s, got %s", i, expected, actual));
      }
    }
  }
  
  private static void load(Map<Integer, String> map, int i, String result) {
    map.put(i, result);
  }
}