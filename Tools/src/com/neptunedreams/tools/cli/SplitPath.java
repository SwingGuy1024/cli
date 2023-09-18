// use: splitpath
package com.neptunedreams.tools.cli;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * This is a very narrowly tailored utility. It's meant to split a path onto several lines. To use it, type
 * <pre>$ path | splitpath</pre>
 * <p>Alternatively, you may specify a regex parameter for where to split. For example, to split the path on a slash, type this: </p>
 * <pre>$ path | splitpath /</pre>
 * <p>By default, it splits on the path separator character for the operating system</p>
 * <p>You may also pass a string to filter. It examines each parameter to see if it is a single 
 * character. Single characters are treated as separators, and longer parameters are input text
 * to filter. If no longer parameters are found, it uses the input stream, which will usually
 * come from a pipe.</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 3/12/21
 * <p>Time: 7:47 AM
 *
 * @author Miguel Mu–oz
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "HardCodedStringLiteral", "HardcodedFileSeparator"})
public enum SplitPath {
  ;
  public static void main(String[] args) throws IOException {
    String separator = System.getProperty("path.separator");
    if ((args.length == 1) && (args[0].startsWith("-h") || args[0].startsWith("--h"))) {
      showUsageAndExit();
    }
    if (args.length == 0) {
      String path = readSystemInput();
      process(separator, path);
    } else {
      int start = 0;
      if (args[0].length() == 1) {
        separator = args[0];
        start = 1;
      }
      process(separator, combineArgs(args, start));
    }
  }

  private static String combineArgs(String[] args, int index) {
    // I'm assuming here that no path has two or more consecutive spaces. 
    StringBuilder builder = new StringBuilder(args[index]);
    for (int i=index+1; i<args.length; ++i) {
      builder.append(' ').append(args[i]);
    }
    return builder.toString();
  }

  private static void process(String separator, String path) {
    String[] pieces = path.split(separator);
    for (String p : pieces) {
      System.out.println(p);
    }
  }
  
  private static String readSystemInput() throws IOException {
    try (InputStreamReader reader = new InputStreamReader(System.in)) {
      StringWriter writer = new StringWriter();
      reader.transferTo(writer);
      return writer.toString();
    }
  }
  
  private static void showUsageAndExit() {
    System.err.println("Usage: echo $path | splitpath ");
    System.err.println("       echo $path | : splitpath");
    System.err.println("       path | splitpath");
    System.err.println("       splitpath a/b/c");
    System.err.println("       splitpath a/b/c /");
    System.err.println("       splitpath / a/b/c");
    System.exit(1);
  }
}
