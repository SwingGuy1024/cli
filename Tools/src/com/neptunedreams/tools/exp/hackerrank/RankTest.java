package com.neptunedreams.tools.exp.hackerrank;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/10/21
 * <p>Time: 11:31 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public class RankTest {
  private static class Result {
    private String leftDigits;
    private String rightDigits;
    public Result(String s) {
      int dotSpot = s.indexOf('.');
      if (dotSpot < 0) {
        leftDigits = s;
        rightDigits = "";
      } else {
        leftDigits = s.substring(0, dotSpot);
        rightDigits = s.substring(dotSpot+1);
      }
    }
    
    public String add(Result that) {
      int length = Math.max(rightDigits.length(), that.rightDigits.length());
      StringBuilder sumBuffer = new StringBuilder();
      int carry = 0;
      for (int i=length-1; i>=0; --i) {
        
        int d1 = getRightDigit(this.rightDigits, i);
        int d2 = getRightDigit(that.rightDigits, i);
        int sum = d1+d2 + carry;
        if (sum > 9) {
          sumBuffer.insert(0, (char)('0' + (sum - 10)));
          carry = 1;
        } else {
          sumBuffer.insert(0, (char)('0' + sum));
          carry = 0;
        }
      }
      sumBuffer.insert(0, '.');
      
      length = Math.max(leftDigits.length(), that.leftDigits.length());
      while (leftDigits.length() < that.leftDigits.length()) {
        leftDigits = '0' + leftDigits;
      }
      while (that.leftDigits.length() < leftDigits.length()) {
        that.leftDigits = '0' + that.leftDigits;
      }
      for (int i= length-1; i>=0; --i) {
        int d1 = getLeftDigit(this.leftDigits, i);
        int d2 = getLeftDigit(that.leftDigits, i);
        int sum = d1+d2+carry;
        if (sum > 9) {
          carry = 1;
          sumBuffer.insert(0, (char)('0' + (sum - 10)));
        } else {
          carry = 0;
          sumBuffer.insert(0, (char)('0' + sum));
        }
      }
      if (carry > 0) {
        sumBuffer.insert(0, (char)('0' + carry));
      }
      while (sumBuffer.charAt(sumBuffer.length()-1) == '0') {
        sumBuffer.deleteCharAt(sumBuffer.length()-1);
      }
      return sumBuffer.toString();
    }
    
    private int getLeftDigit(String s, int i) {
      if (i >= s.length()) {
        return 0;
      }
      return s.charAt(i) - '0';
    }
    
    private int getRightDigit(String s, int i) {
      if (i >= s.length()) {
        return 0;
      }
      return ((int)s.charAt(i)) - '0';
    }
  }

  public static void main(String[] args) {
    test("12345", "1");
    test("123.4", "4.6");
    test("3", "0.05");
  }
  
  private static void test(String s1, String s2) {
    System.out.printf("%s + %s = %s%n", s1, s2, new Result(s1).add(new Result(s2)));
  }
}
