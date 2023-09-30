// Use: millis
package com.neptunedreams.tools.cli;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * Convert a number of milliseconds into a time, based on the Java Epoch.
 * <p>
 * Rules for Shebang Java: Add a Shebang line at the top. It must start with #!, have a complete path to the java executable, and
 * must specify a source. Then, the extension must be removed from the file, and it must be made executable.
 * The java file may call system libraries, but must not call any other user classes. All the code must sit in a single file.
 * <p>
 * To use, type this: TimeConvert 1550785775435
 */
public enum TimeConvert {
  ;

  public static void main(String[] args) {
    if ((args.length == 1) && args[0].startsWith("-h")) {
      System.out.println("Converts milliseconds to time");
      System.out.println("Usage: millis <milliseconds> [<milliseconds> ...]");

      return;
    }
    if (args.length == 0) {
      long millis = System.currentTimeMillis();
      System.out.printf("  at %s%n  %d ms%n  %d seconds%n", getTime(millis), millis, millis / 1000);
    }
    for (String s : args) {
      System.out.println(textToTime(s));
    }
  }

  private static String textToTime(String txt) {
    try {
      long timeMillis = Long.parseLong(txt);
      return getTime(timeMillis);
    } catch (NumberFormatException e) {
      return "Bad input. Time not in milliseconds: " + txt;
    }
  }

  private static String getTime(long timeMillis) {
    Instant instant = Instant.ofEpochMilli(timeMillis);
    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
    return zonedDateTime.toString();
  }
}
