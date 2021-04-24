// use: where
package com.neptunedreams.tools.cli;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * <p>Finds the specified executable file on the path, and prints all directories containing the executable file. This does what the
 * standard unix {@code whereis} command is supposed to do, but doesn't, at least on my system. The {@code whereis} command sometimes
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

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.printf("Usage: where <dir>%n"); // NON-NLS
      System.exit(0);
    }
    Process pathProcess = Runtime.getRuntime().exec("path");
    String path = new String(pathProcess.getInputStream().readAllBytes(), Charset.defaultCharset());
    String separator = System.getProperty("path.separator");
    String fileSeparator = System.getProperty("file.separator");
    String target = args[0];
    String[] pathElements = path.split(separator);
    // Warns me about a possible null value from dir.list(), but this can't happen because I test it for a directory.
    // (It can't be a directory if the path is correct, but it's possible to put a non-existent path or even a file onto
    // the path by accident.)
    //noinspection ConstantConditions
    Arrays.stream(pathElements)
        .map(String::trim)
        .map(File::new)
        .filter(dir-> dir.isDirectory() && Arrays.asList(dir.list()).contains(target))
        .forEach(d->System.out.printf("%s%s%s%n", d, fileSeparator, target));
  }
}
