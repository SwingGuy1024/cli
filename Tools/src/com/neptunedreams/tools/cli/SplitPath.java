// use: splitpath
package com.neptunedreams.tools.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "HardCodedStringLiteral", "HardcodedFileSeparator"})
public enum SplitPath {
  ;
  public static void main(String[] args) throws IOException {
    String separator = System.getProperty("path.separator");
    InputStream inputStream = System.in;
    if (args.length == 1) {
      if (args[0].length() == 1) {
        separator = args[0];
      } else {
        inputStream = stringToInputStream(args[0]);
      }
    } else if (args.length == 2) {
      if (args[0].length() == 1) {
        separator = args[0];
        inputStream = stringToInputStream(args[1]);
      } else if (args[1].length() == 1) {
        separator = args[1];
        inputStream = stringToInputStream(args[0]);
      } else {
        showUsage();
      }
    } else {
      showUsage();
    }
    process(separator, inputStream);
  }
  
  private static InputStream stringToInputStream(String s) {
    StringReader reader = new StringReader(s);

    return new InputStream() {
      @Override
      public int read() throws IOException {
        return reader.read();
      }
    };
  }
  
  private static void process(String separator, InputStream in) throws IOException {
    try (InputStreamReader reader = new InputStreamReader(in)) {
      StringWriter writer = new StringWriter();
      reader.transferTo(writer);
      String[] pieces = writer.toString().split(separator);
      for (String p : pieces) {
        System.out.println(p);
      }
    }
  }
  
  private static void showUsage() {
    System.err.println("Usage: echo $path | splitpath ");
    System.err.println("       echo $path | splitpath :");
    System.err.println("       splitpath a/b/c");
    System.err.println("       splitpath a/b/c /");
    System.err.println("       splitpath / a/b/c");
    System.exit(1);
  }
}
