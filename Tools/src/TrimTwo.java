#!/usr/bin/java --source 11

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 12/21/20
 * <p>Time: 3:28 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public enum TrimTwo {
  ;

  @SuppressWarnings("HardcodedLineSeparator")
  private static final char NEW_LINE = '\n';

  @SuppressWarnings("OverlyBroadThrowsClause")
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
  }
  
  private static void showUsageAndExit() {
    System.err.printf("Usage TrimTwo <source> <dest>%n"); // NON-NLS
    System.exit(0);
  }
}
