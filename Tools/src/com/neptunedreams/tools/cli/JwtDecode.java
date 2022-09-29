package com.neptunedreams.tools.cli;

import java.io.*
import java.time.*
import java.time.format.*
import java.util.Base64;
import java.util.Formatter;

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
    String token1 = decode(tokens[1]);
    System.out.println(token1);
    System.out.println(Signature: %d characters%n", tokens[2].length());

    // get expiration time
    BufferedReader reader = new BufferedReader(new StringReader(token1));
    String token = "";
    while (token != null) {
      if (token.contains("\"exp\":")) {
        int dotSpot = token.indexOf(':');
        String timeSeconds = token.substring(dotSpot + 1);
        long time = Long.parseLong(timeSeconds.trim() + "000"); // convert to milliseconds first
        System.out.printf("Expires at %s%n", getTime(time));
      }
      token = reader.readLine();
    }
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

  private static String getTime(long milliseconds) {
    Instant instant = Instant.ofEpochMillis(timeMillis);
    ZonedDateTime zoned DateTime = instant.atZone(ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.FULL);
    return formatter.format(zonedDateTime);
  }
}
