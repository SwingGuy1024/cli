package com.neptunedreams.tools.exp.codeleek;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/29/20
 * <p>Time: 2:54 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public class ZigZag {
  public String convert(String s, int numRows) {
    if (numRows == 1) {
      return s;
    }
    char[] data = s.toCharArray();
    char[] output = new char[s.length()];
    int interval = (numRows - 1) * 2;
    int index = 0;
    int len = s.length();

    // First row
    for (int source = 0; source < len; source += interval) {
      output[index++] = data[source];
    }

    // In-between rows
    for (int row = 1; (row <= (numRows - 2)) && (index < len); ++row) {
      output[index++] = data[row];
      for (int source = row + interval; source < (len + (row * 2)); source += interval) {
        int upIndex = source - (row*2);
        if (upIndex < len) {
          output[index++] = data[upIndex];
          if (source < len) {
            output[index++] = data[source];
          }
        }
      }
    }

    // Last row
    for (int source = numRows - 1; source < len; source += interval) {
      output[index++] = data[source];
    }
    return new String(output);

    /*
    Brute Force Approach was too slow.
     */
    // StringBuilder[] builders = new StringBuilder[numRows];
    // for (int i=0; i<numRows; i++) {
    //   builders[i] = new StringBuilder();
    // }
    // int index = 1;
    // while (index < s.length()) {
    //   for (int row=1; row<numRows && index<s.length(); ++row) {
    //     builders[row].append(s.charAt(index++));
    //   }
    //   for (int i=numRows-2; i>=0 && index<s.length(); --i) {
    //     builders[i].append(s.charAt(index++));
    //   }
    // }
    // StringBuilder b = new StringBuilder(s.length());
    // b.append(s.charAt(0));
    // for (StringBuilder segment: builders) {
    //   // System.out.println(segment.toString());
    //   b.append(segment.toString());
    // }
    // return b.toString();
  }

  public static void main(String[] args) {
    String test = "PAYPALISHIRING";
    ZigZag zigZag = new ZigZag();
    doTest(zigZag, test, 3);
    doTest(zigZag, test, 3);
    doTest(zigZag, "A", 1);
    doTest(zigZag, "B", 4);
  }
  
  private static void doTest(ZigZag zigZag, String s, int numRows) {
    System.out.printf("%s%n%s%n%n", s, zigZag.convert(s, numRows)); // NON-NLS
  }
}
