package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Sort a list of words separated by spaces.
 * Accepts piped input.
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 9/21/24</p>
 * <p>Time: 1:45 AM</p>
 * <p>@author Miguel Muñoz</p>
 */
public enum SortWord {
  ;
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      final int available = System.in.available();
      if (available == 0) {
        System.err.println("Usage SortWord <word list>");
        System.err.println("(May be used with pipes.)"); // NON-NLS
        System.exit(0);
      } else {
        final byte[] bytes = System.in.readAllBytes();
        final String argsAsString = new String(bytes, Charset.defaultCharset());
        args = argsAsString.split(" ");
      }
    }
    Arrays.sort(args, String.CASE_INSENSITIVE_ORDER);
    final StringBuilder builder = new StringBuilder();
    for (final String s: args) {
      builder
          .append(s.trim())
          .append(' ');
    }
    final String output = builder.toString();
    final StringSelection stringSelection = new StringSelection(output);
    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, stringSelection);
    System.out.println(output);
    System.exit(0); // Avoids bug where VM prints out diagnostics.
  }
}
