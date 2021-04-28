// use: where
package com.neptunedreams.tools.cli;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * <p>Finds the specified executable file on the path, and prints all directories containing the executable file. This does what the
 * standard unix {@code whereis} command is supposed to do, but doesn't. The {@code whereis} command sometimes
 * fails because it only looks in the standard binary directories for the specified programs, whereas this tool looks in every
 * directory on the path.</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 4/24/21
 * <p>Time: 1:27 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("CallToRuntimeExec")
public enum Where {
  ;

  private static final String FILE_SEPARATOR = System.getProperty("file.separator");

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.printf("Usage: where <dir>%n"); // NON-NLS
      System.exit(0);
    }
    Process pathProcess = Runtime.getRuntime().exec("path");
    String path = new String(pathProcess.getInputStream().readAllBytes(), Charset.defaultCharset());
    String separator = System.getProperty("path.separator");
    String target = args[0];
    String[] pathElements = path.split(separator);
    
    // Make sure the path elements are all valid, then continue.
    Arrays.stream(pathElements)
        .map(String::trim)
        .map(Where::coverTilde)
        .map(File::new)
        .filter(dir-> !dir.isDirectory())
        .forEach(d-> System.out.printf("Warning: Path element %s is not a directory%n", d));
    
    // Warns me about a possible null value from dir.list(), but this can't happen because I test it for a directory.
    // (It can't be a directory if the path is correct, but it's possible to put a non-existent path or even a file onto
    // the path by accident.)
    //noinspection ConstantConditions
    Arrays.stream(pathElements)
        .map(String::trim) // last entry mysteriously gets a \n appended without this.
        .map(Where::coverTilde)
        .map(File::new)
        .filter(dir-> dir.isDirectory() && Arrays.asList(dir.list()).contains(target))
        .forEach(d->printPath(d, target));
  }
  
  private static void printPath(File dirPath, String target) {
    String fullPath = String.format("%s%s%s", dirPath, FILE_SEPARATOR, target);
    System.out.printf("%s%s%n", fullPath, isLinkOrExecutable(fullPath)); // NON-NLS
  }
  
  private static String isLinkOrExecutable(String filePath) {
    Path path= Path.of(filePath);
    if (Files.isSymbolicLink(path)) {
      return "@";
    }
    if (Files.isExecutable(path)) {
      return "*";
    }
    return "";
  }

  private static String coverTilde(String dirName) {
    if (dirName.startsWith("~")) {
      return String.format("%s%s", System.getProperty("user.home"), dirName.substring(1));
    }
    return dirName;
  }
}
