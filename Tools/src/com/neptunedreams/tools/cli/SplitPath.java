// use: splitpath
package com.neptunedreams.tools.cli;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

/**
 * This is a very narrowly tailored utility. It's meant to split a path onto several lines. To use it, type
 * <pre>$ path | splitpatd</pre>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 3/12/21
 * <p>Time: 7:47 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum SplitPath {
  ;
  public static void main(String[] args) throws IOException {
    StringWriter writer;
    try (Reader reader = new InputStreamReader(System.in)) {
      writer = new StringWriter();
      reader.transferTo(writer);
    }
    String[] pieces = writer.toString().split(":");
    for (String p: pieces) {
      System.out.println(p);
    }
  }
}
