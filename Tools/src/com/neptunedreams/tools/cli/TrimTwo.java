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
 * <p>This tool has an "install" option that assumes the existence of a directory at {@code {$HOME}/bin}, and assumes this directory
 * is on your classpath.</p>
 * <p>To install all the other tools, you need to first install this tool. There are three ways to do this. The easiest is to run
 * this inside your IDE, with the install option. Or you can run it from the root of your compiled files, like this:</p>
 * <pre>java -cp . com.neptunedreams.tools.cli.TrimTwo install</pre>
 * <p>Or you can copy it to your {@code {$HOME}/bin} directory and add the shebang line manually.</p>
 * <p>It's called "TrimTwo" because it used assume the shebang line was the first line, but commented out. It would trim the
 * first two characters after copying. Now it just adds a shebang line. It assumes you have java 11 installed. If you want
 * to use a later version of Java, change the definition of SHEBANG_LINE</p>
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
    boolean isAllowedToMakeExecutable = processFile(args[0], args[1]);
    if (isAllowedToMakeExecutable) {
      System.out.println(args[0]);
    } else {
      System.err.printf("Unable to make files executable.%n"); // NON-NLS
    }
  }

  /**
   * Process the file: Modify it, copy it, and set it to executable (if allowed to do so)
   * @param filePath The path to the input file
   * @param outPathDirectory The destination path
   * @return true if the file was successfully set as executable
   * @throws IOException If the destination file couldn't be created.
   */
  private static boolean processFile(String filePath, String outPathDirectory) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String oneLine = reader.readLine();
      String outFileName;
      if (oneLine.toLowerCase().startsWith(USE_PREFIX)) {
        outFileName = oneLine.substring(USE_PREFIX.length()).trim();
      } else {
        // strip out the file extension. File extensions disable the Shebang feature.
        int dSpot = Math.max(0, filePath.lastIndexOf('/'));
        String simpleName = filePath.substring(dSpot);
        outFileName = simpleName.replaceAll("\\.java", "");
      }
      String fullOutPath = makeOutPath(outPathDirectory, outFileName);
      return writeToFile(oneLine, reader, fullOutPath);
    }
  }

  private static String makeOutPath(final String outPathDirectory, final String outFileName) {
    if (outPathDirectory.endsWith("/")) {
      return String.format("%s%s", outPathDirectory, outFileName);
    }
    return String.format("%s/%s", outPathDirectory, outFileName);
  }

  /**
   * Write a modified copy of the file to the destination directory specified by outPath. The modification is to add a shebang line
   * at the beginning of the file. Also sets the output file to be executable, if it has permission to do so.
   * @param oneLine The first line of the input file, which has already been read.
   * @param reader The reader from which the rest of the input lines will be read.
   * @param outPath The path of the output file.
   * @return True if it was able to set the file's executable flag, false otherwise.
   * @throws IOException If the output file can't be created.
   */
  private static boolean writeToFile(final String oneLine, final BufferedReader reader, String outPath) throws IOException {
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
    return new File(outPath).setExecutable(true);
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
