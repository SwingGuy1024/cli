// use: splitpath
package com.neptunedreams.tools.cli;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

/**
 * This is a very narrowly tailored utility. It's meant to split a path onto several lines. To use it, type
 * <pre>$ path | splitpath</pre>
 * <p>Alternatively, you may specify a regex parameter for where to split. For example, to split the path on a slash, type this: </p>
 * <pre>$ path | splitpath /</pre>
 * <p>By default, it splits on the path separator character for the operating system</p>
 * <p>In principle, it would make sense to make the separator an option, specified as "-s :"</p>
 * <p> This would allow us to specify the input on the command line as well as using System.in.
 * This is how most unix commands are written. I haven't bothered because I don't yet need this.</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 3/12/21
 * <p>Time: 7:47 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public enum SplitPath {
  ;
  public static void main(String[] args) throws IOException {
    String separator;
    if (args.length == 0) {
      separator = System.getProperty("path.separator");
    } else {
      separator = args[0];
    }
    try (InputStreamReader reader = new InputStreamReader(System.in)) {
      StringWriter writer = new StringWriter();
      reader.transferTo(writer);
      String[] pieces = writer.toString().split(separator);
      for (String p : pieces) {
        System.out.println(p);
      }
    }
  }
}
