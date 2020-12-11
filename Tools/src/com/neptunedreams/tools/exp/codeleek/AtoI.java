package com.neptunedreams.tools.exp.codeleek;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/29/20
 * <p>Time: 3:02 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("MagicCharacter")
public class AtoI {
  public static final int last = Integer.MAX_VALUE / 10;
  public static final int tailPos = Integer.MAX_VALUE - (last * 10);
  public static final int tailNeg = tailPos + 1;

  public int myAtoi(String s) {
    int nonWhite = 0;
    while ((nonWhite < s.length()) && (s.charAt(nonWhite) == ' ')) {
      nonWhite++;
    }
    if (nonWhite == s.length()) {
      return 0;
    }
    char c = s.charAt(nonWhite);
    boolean positive = true;
    if ((c == '-') || (c == '+')) {
      positive = c == '+';
      nonWhite++;
    }
    int r = 0;
    while ((nonWhite < s.length()) && Character.isDigit(s.charAt(nonWhite))) {
      if (r > last) {
        return positive ? Integer.MAX_VALUE : Integer.MIN_VALUE;
      }
      char nextChar = s.charAt(nonWhite);
      int nextDigit = nextChar - '0';
      if (r == last) {
        if (positive) {
          if (nextDigit >= tailPos) {
            return Integer.MAX_VALUE;
          }
        } else {
          if (nextDigit >= tailNeg) {
            return Integer.MIN_VALUE;
          }
        }
      }
      r *= 10;
      r += nextDigit;
      nonWhite++;
    }
    return positive ? r : -r;
  }

  @SuppressWarnings({"HardCodedStringLiteral", "UseOfSystemOutOrSystemErr"})
  public static void main(String[] args) {
    String[] data = {
//        "",
//        "42",
//        "  -38x", // NON-NLS
//        "99999999999999999999",
//        "    43 x", // NON-NLS
        " 2147483646",
        " 2147483647",
        " 2147483648",
        " -2147483646",
        " -2147483646x",
        " -2147483647",
        " -2147483648",
        " -2147483649",
        " 2147483650",
        " -2147483650"
    };
    
    AtoI atoI = new AtoI();
    for (String s: data) {
      System.out.printf("%20s -> %11s%n", s, atoI.myAtoi(s));
    }
  }
}
