//#!/usr/bin/java --source 11

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This works like the Unix cp command, except it also trims the first line if it detects that it's a Shebang line that has been 
 * commented out. It also sets the resulting file to executable. This essentially lets me release a Shebang-enabled java file. I
 * comment out the Shebang file because IntelliJ doesn't recognize it.
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 12/21/20
 * <p>Time: 3:28 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum TrimTwo {
  ;

  private static final char NEW_LINE = '\n';

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      showUsageAndExit();
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
         BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))
    ) {
      String oneLine = reader.readLine();
      if (oneLine == null) {
        throw new IOException("Empty file");
      }
      if (oneLine.startsWith("//#")) {
        oneLine = oneLine.substring(2);
      } else if (oneLine.startsWith("// #")) {
        oneLine = oneLine.substring(3);
      }
      while (oneLine != null) {
        writer.append(oneLine).append(NEW_LINE);
        oneLine = reader.readLine();
      }
    }
    //noinspection ResultOfMethodCallIgnored
    new File(args[1]).setExecutable(true);
  }

  private static void showUsageAndExit() {
    System.err.printf("Usage TrimTwo <source> <dest>%n"); // NON-NLS
    System.exit(0);
  }
}
