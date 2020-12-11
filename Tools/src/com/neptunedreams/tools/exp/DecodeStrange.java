package com.neptunedreams.tools.exp;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/2/20
 * <p>Time: 2:15 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public class DecodeStrange {
  private static final String message = "WA AX Emon Do Wah I Wile En Ish ni la hi per est she heel ora hasik la tesh him ora hasik la tesh gey babai Hodan magai lish shori tuxduba alin do wah ira him.";

  public static void main(String[] args) {
    System.out.printf("%s%n%n", message);
    String transformed = message;
    for (int i=0; i<26; ++i) {
      transformed = transform(transformed);
      System.out.printf("%s%n%n", transformed); // NON-NLS
    }

    System.out.println("---");
    StringBuilder b = new StringBuilder();
    for (int i=0; i<message.length()-1; i+=2) {
      b.append(message.charAt(i+1)).append(message.charAt(i));
    }
    System.out.println(b.toString());
  }
  
  public static String transform(String m) {
    StringBuilder builder = new StringBuilder(m.length());
    for (int i = 0; i < (m.length()); i++) {
      char c = Character.toLowerCase(m.charAt(i));
      if (Character.isLetter(c)) {
        c++;
        if (c > 'z') {
          c = 'a';
        }
      }
      builder.append(c);
    }
    return builder.toString();
  }
}
