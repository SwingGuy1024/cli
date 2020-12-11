package com.neptunedreams.tools.exp.codeleek;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/29/20
 * <p>Time: 2:13 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public class LongestPalindrome {
  public String longestPalindrome(String s) {
    String longest = s.substring(0, 1);
    for (int c = 1; c < s.length(); ++c) {
      int back = c - 1;
      int fwd = c;
      // look for even-length palindromes
      while ((back >= 0) && (fwd < s.length())) {
        if (s.charAt(back) == s.charAt(fwd)) {
          if (((fwd - back) + 1) > longest.length()) {
            longest = s.substring(back, fwd + 1);
          }
        } else {
          break;
        }
        fwd++;
        back--;
      }
      back = c - 1;
      fwd = c + 1;
      // Look for odd-length palindromes
      while ((back >= 0) && (fwd < s.length())) {
        if (s.charAt(back) == s.charAt(fwd)) {
          if (((fwd - back) + 1) > longest.length()) {
            longest = s.substring(back, fwd + 1);
          }
        } else {
          break;
        }
        back--;
        fwd++;
      }
    }
    return longest;
  }}
