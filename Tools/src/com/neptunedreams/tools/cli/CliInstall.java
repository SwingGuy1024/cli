package com.neptunedreams.tools.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/**
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 5/3/24</p>
 * <p>Time: 4:06?PM</p>
 * <p>@author Miguel Mu�oz</p>
 */
public enum CliInstall {
  ;
  private static final char NEW_LINE = '\n';
  private static final String USE_PREFIX = "// use: ";
  private static final String SHEBANG_LINE = "#!/usr/bin/java --source 17\n";
  private static final String[] EMPTY_ARRAY = new String[0];
  private static final FilenameFilter JAVA_FILES = (f, n) -> n.endsWith(".java");
  public static final String NEPTUNE_CLI_SOURCE = "NeptuneCliSource";
  public static final String NEPTUNE_EXEC_DIR = "NeptuneExecDir";

  public static void main(String[] args) throws IOException {
    String userHome = System.getProperty("user.home");
    final String defaultSrc = userHome + "/Documents/DevDocs/Tools/cli/Tools/src/com/neptunedreams/tools/cli";
    String cliSourceDir = getDirectoryEnv(NEPTUNE_CLI_SOURCE, defaultSrc);
    String cliExecDir = getDirectoryEnv(NEPTUNE_EXEC_DIR, userHome + "/bin");
    installSource(cliSourceDir, cliExecDir);
  }

  private static void installSource(String cliSourceDir, String cliExecDir) throws IOException {
    final String sourceDir = trimTrailingSlash(cliSourceDir);
    String execDir = trimTrailingSlash(cliExecDir);
    //noinspection DataFlowIssue
    String[] javaFiles = notNull(Path.of(sourceDir).toFile().list(JAVA_FILES));
    for (String fileName: javaFiles) {
      processPath(fileName, sourceDir, execDir);
    }
  }
  
  private static void processPath(String fileName, String path, String execDir) throws IOException {
    CopyResult isAllowedToMakeExecutable = processFile(path + '/' + fileName, execDir);
    switch (isAllowedToMakeExecutable) {
      case Created -> System.out.printf("Created %s%n", fileName);
      case Updated -> System.out.printf("Updated %s%n", fileName);
      case CreatedFailed -> System.out.printf("Created %s -- Failed to make executable%n", fileName); // NON-NLS
      case UpdatedFailed -> System.out.printf("Updated %s -- Failed to make executable%n", fileName); // NON-NLS
      case Skipped -> { }
      default -> //noinspection RedundantLabeledSwitchRuleCodeBlock
      { throw new IllegalStateException(String.format("Unused value: %s", isAllowedToMakeExecutable)); }
    }
  }
  
  private static String[] notNull(String[] array) {
    return (array == null) ? EMPTY_ARRAY : array;
  }

  /**
   * Process the file: Modify it, copy it, and set it to executable (if allowed to do so)
   *
   * @param filePath         The path to the input file
   * @param outPathDirectory The destination path
   * @return One of {Created, Updated, Skipped, CreatedFailed, UpdatedFailed}
   * @throws IOException If the destination file couldn't be created.
   */
  @SuppressWarnings("OverlyBroadThrowsClause")
  private static CopyResult processFile(String filePath, String outPathDirectory) throws IOException {
//    System.out.println("-- " + filePath.substring(filePath.lastIndexOf('/') + 1));
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String oneLine = reader.readLine();
      String outFileName;
      int useIndex = oneLine.toLowerCase().indexOf(USE_PREFIX);
      if (useIndex >= 0) {
        outFileName = oneLine.substring(useIndex + USE_PREFIX.length()).trim();
      } else {
        // strip out the file extension. File extensions disable the Shebang feature.
        int dSpot = Math.max(0, filePath.lastIndexOf('/'));
        String simpleName = filePath.substring(dSpot);
        outFileName = simpleName.replaceAll("\\.java", "");
      }
      CopyResult copyResult;
      String fullOutPath = makeOutPath(outPathDirectory, outFileName);
      final Path path = Path.of(fullOutPath);
      //noinspection IfStatementWithNegatedCondition
      if (!path.toFile().exists()) {
        copyResult = CopyResult.Created;
      } else {
        FileTime destFileTime = Files.getLastModifiedTime(path);
        FileTime srceFileTime = Files.getLastModifiedTime(Path.of(filePath));
        if (srceFileTime.compareTo(destFileTime) > 0) {
          copyResult = CopyResult.Updated;
        } else {
          return CopyResult.Skipped;
        }
      }
      final boolean copied = writeToFile(oneLine, reader, fullOutPath);
      return copied ? copyResult : failed(copyResult);
    }
  }

  private static CopyResult failed(CopyResult result) {
    return (result == CopyResult.Created) ? CopyResult.CreatedFailed : CopyResult.UpdatedFailed;
  }

  private static String makeOutPath(final String outPathDirectory, final String outFileName) {
    if (outPathDirectory.endsWith("/")) {
      return String.format("%s%s", outPathDirectory, outFileName);
    }
    return String.format("%s/%s", outPathDirectory, outFileName);
  }

  private static String trimTrailingSlash(String dir) {
    StringBuilder builder = new StringBuilder(dir);
    while (builder.charAt(builder.length()-1) == '/') {
      builder.deleteCharAt(builder.length()-1);
    }
    return builder.toString();
  }

  /**
   * Write a modified copy of the file to the destination directory specified by outPath. The modification is to add a shebang line
   * at the beginning of the file. Also sets the output file to be executable, if it has permission to do so.
   *
   * @param oneLine The first line of the input file, which has already been read.
   * @param reader  The reader from which the rest of the input lines will be read.
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

  private static String getDirectoryEnv(String envVar, String defaultPath) {
    //noinspection CallToSystemGetenv
    String ePath = System.getenv(envVar);
    if (ePath == null) {
      ePath = defaultPath;
    }
    return ePath;
  }

  enum CopyResult {
    Created, Updated, Skipped, CreatedFailed, UpdatedFailed
  }
}
