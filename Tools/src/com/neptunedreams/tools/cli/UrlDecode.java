// Use: urlDecode
package com.neptunedreams.tools.cli;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/5/21
 * <p>Time: 7:50 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum UrlDecode {
  ;

  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.printf("Usage: decodeUrl <url>%n"); // NON-NLS
      System.exit(1);
    }
    String input = args[0];
    String decoded = decode(input);
    System.out.println(decoded);
  }

  private static String decode(final String input) {
    StringBuilder builder = new StringBuilder();
    String source = input.substring(0, input.length()-2);
    int pSpot = source.indexOf('%');
    int prior = 0;
    while (pSpot >= 0) {
      builder.append(input, prior, pSpot);
      char c = 0;
      try {
        c = (char) Short.parseShort(input.substring(pSpot+1, pSpot+3), 16);
      } catch (NumberFormatException e) {
        final String badValue = input.substring(pSpot, pSpot + 3);
        System.err.printf("%s at %d: %s%n", e.getMessage(), pSpot, badValue); // NON-NLS;
        throw new IllegalArgumentException(badValue, e);
      }
      builder.append(c);
      prior = pSpot+3;
      pSpot = source.indexOf('%', prior);
    }
    builder.append(input.substring(prior));
    return builder.toString();
  }
}
