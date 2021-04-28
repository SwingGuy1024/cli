package com.neptunedreams.tools.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>This works like the Unix cp command, except it also adds a Shebang line.</p>
 * <p>It also optionally changes the name of the file to xxx if the first line is a comment of this form:</p>
 * <pre>// use: xxx</pre>
 * <p>It also sets the resulting file to executable. This essentially lets me release a Shebang-enabled java file.</p>
 * <p>It's called "TrimTwo" because it used to trim the comment on an existing Shebang line, but now it just adds one.</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 12/21/20
 * <p>Time: 3:28 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum TrimTwo {
  ;

  private static final char NEW_LINE = '\n';
  private static final String USE_PREFIX = "// use: ";
  private static final String SHEBANG_LINE = "#!/usr/bin/java --source 11\n";

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      if ((args.length == 1) && "install".equals(args[0])) {
        doInstallAndExit();
      } else {
        showUsageAndExit();
      }
    }
    processFile(args[0], args[1]);
  }

  private static void processFile(String filePath, String outPathDirectory) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String oneLine = reader.readLine();
      String outFileName;
      if (oneLine.toLowerCase().startsWith(USE_PREFIX)) {
        outFileName = oneLine.substring(USE_PREFIX.length()).trim();
      } else {
        int dSpot = Math.max(0, filePath.lastIndexOf('/'));
        String simpleName = filePath.substring(dSpot);
        outFileName = simpleName.replaceAll("\\.java", "");
      }
      String fullOutPath = makeOutPath(outPathDirectory, outFileName);
      writeToFile(oneLine, reader, fullOutPath);
    }
  }

  private static String makeOutPath(final String outPathDirectory, final String outFileName) {
    if (outPathDirectory.endsWith("/")) {
      return String.format("%s%s", outPathDirectory, outFileName);
    }
    return String.format("%s/%s", outPathDirectory, outFileName);
  }

  private static void writeToFile(final String oneLine, final BufferedReader reader, String outPath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outPath))) {
      writer.write(SHEBANG_LINE);
      String line = oneLine;
      while (line != null) {
        writer
            .append(line)
            .append(NEW_LINE);
        line = reader.readLine();
      }
    }
    //noinspection ResultOfMethodCallIgnored
    new File(outPath).setExecutable(true);
  }

  private static void doInstallAndExit() throws IOException {
    final String userDir = System.getProperty("user.dir");
    final String sourcePath = "/cli/Tools/src/com/neptunedreams/tools/cli/";
    final String src = userDir + sourcePath + "TrimTwo.java";

    final String userHome = System.getProperty("user.home");
    final String dst = userHome + "/bin";

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
    System.err.printf("Usage TrimTwo <source> <destinationDirectory>%n"); // NON-NLS
    System.exit(0);
  }
}
