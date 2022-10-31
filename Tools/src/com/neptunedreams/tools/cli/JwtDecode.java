package com.neptunedreams.tools.cli;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.Base64;

// I hope this is right. I typed in some changes manually and didn't get a chance to test it,
// so there may be typos in this latest version, but the previous version works.

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/26/21
 * <p>Time: 1:48 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum JwtDecode {
  ;

  public static void main(String[] args) throws IOException {
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
    String token1 = decode(tokens[1]); // will get used twice
    System.out.println(token1);
    System.out.printf("Signature: %d characters%n", tokens[2].length());

    // get expiration time from token1
    try (BufferedReader reader = new BufferedReader(new StringReader(token1))) {
      String token;
      //noinspection NestedAssignment
      while ((token = reader.readLine()) != null) {
        if (!didProcess(token, "exp", "Expired")) {
          didProcess(token, "iat", "Issued");
        }
      }
    }
  }
  
  private static boolean didProcess(String token, String key, String label) {
    if (token.contains(('"' + key + "\":"))) {
      System.out.printf("%s at %s%n", label, getTime(getSeconds(token)));
      return true;
    }
    return false;
  }

  private static long getSeconds(final String token) {
    int dotSpot = token.indexOf(':');
    String timeSeconds = token.substring(dotSpot + 1);
    return Long.parseLong(trimExtras(timeSeconds.trim()));
  }

  private static String decode(String s) {
    return format(new String(Base64.getDecoder().decode(s)));
  }

  // Primitive JSON Formatter. This doesn't handle special cases, but for
  // most JwtTokens, it will work fine.
  private static String format(String json) {
    //noinspection NonConstantStringShouldBeStringBuffer
    String indent = "";
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < json.length(); ++i) {
      char c = json.charAt(i);
      if ((c == '}') || (c == ']')) {
        indent = indent.substring(2);
        newLine(builder, indent);
      }
      builder.append(c);
      if ((c == '{') || (c == '[')) {
        indent += "  ";
        newLine(builder, indent);
      } else if (c == ',') {
        newLine(builder, indent);
      }
    }
    return builder.toString();
  }

  private static void newLine(StringBuilder builder, String indent) {
    builder.append('\n')
        .append(indent);
  }

  /**
   * Strips out anything after the last digit character. This will most likely be a comma, if anything.
   * @param input the input String of mostly digits.
   * @return The string with any non-digit characters stripped from the end.
   */
  private static String trimExtras(String input) {
    int i= input.length();
    while (i-- > 0) {
      char c = input.charAt(i);
      if (Character.isDigit(c)) {
        break;
      }
    }
    return input.substring(0, i+1);
  }

  private static String getTime(long seconds) {
    Instant instant = Instant.ofEpochSecond(seconds);
    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.FULL);
    return formatter.format(zonedDateTime);
  }
}

