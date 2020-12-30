//#!/usr/bin/java --source 11
package com.neptunedreams.tools.cli;

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
      if ((args.length == 1) && "install".equals(args[0])) {
        doInstallAndExit();
      } else {
        showUsageAndExit();
      }
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
         BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))
    ) {
      String oneLine = reader.readLine();
      if (oneLine == null) {
        throw new IOException("Empty file");
      }
      oneLine = stripLeadComment(oneLine);
      while (oneLine != null) {
        writer.append(oneLine).append(NEW_LINE);
        oneLine = reader.readLine();
      }
    }
    //noinspection ResultOfMethodCallIgnored
    new File(args[1]).setExecutable(true);
  }

  private static void doInstallAndExit() throws IOException {
    final String userDir = System.getProperty("user.dir");
    final String sourcePath = "/cli/Tools/src/com/neptunedreams/tools/cli/";
    final String src = userDir + sourcePath + "TrimTwo.java";

    final String userHome = System.getProperty("user.home");
    final String dst = userHome + "/bin/TrimTwo";

    main(new String[] {src, dst});
    System.out.println("TrimTwo installation Complete");
    System.exit(0); // Clean exit
  }

  // shebang starts with comment
  private static String stripLeadComment(final String s) {
    String st = s.trim();
    if (st.startsWith("//")) {
      st = st.substring(2).trim();
      if (st.startsWith("#")) {
        return st;
      }
    }
    return s;
  }

  private static void showUsageAndExit() {
    System.err.printf("Usage TrimTwo <source> <dest>%n"); // NON-NLS
    System.exit(0);
  }
}
