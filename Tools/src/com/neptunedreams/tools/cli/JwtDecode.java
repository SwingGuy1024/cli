package com.neptunedreams.tools.cli;

import java.util.Base64;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/26/21
 * <p>Time: 1:48 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum JwtDecode {
  ;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.printf("Usage: JwtDecode <token>%n"); // NON-NLS
      return;
    }
    String[] tokens = args[0].split("\\.");
    if (tokens.length != 3) {
      System.err.printf("Invalid token%n"); // NON-NLS
      return;
    }
    System.out.println(decode(tokens[0]));
    System.out.println(decode(tokens[1]));
  }
  
  private static String decode(String s) {
    return new String(Base64.getDecoder().decode(s));
  }
}
