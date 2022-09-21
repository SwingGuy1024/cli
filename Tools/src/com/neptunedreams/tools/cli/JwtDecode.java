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
    return format(new String(Base64.getDecoder().decode(s)));
  }
  
  // Primitive JSON Formatter. This doesn't handle special cases, but for
  // most JwtTokens, it will work fine.
  private static String format(String json) {
    String indent = "";
    StringBuilder builder = new StringBuilder();
    for (int i=0; i<json.length(); ++i) {
      char c = json.charAt(i);
      if (c == '}') {
        indent = indent.substring(2);
        newLine(builder, indent);
      }
      builder.append(c);
      if (c == '{') {
        indent += "  ";
        newLine(builder, indent);
      } else if (c == ',') {
        newLine(builder, indent);
      }
    }
    return builder.toString();
  }
  
  private static newLine(StringBuilder builder, String indent) {
    builder.append('\n')
      .append(indent);
  }
}
