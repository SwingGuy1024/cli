// use: tc
package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

/**
 * Converts the text to Tilte Case
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/1/23
 * <p>Time: 2:38 AM
 *
 * @author Miguel Mu–oz
 */
public enum TitleCase {
  ;

  public static void main(String[] args) {
    StringBuilder builder = new StringBuilder();
    for (String arg: args) {
      process(arg, builder);
    }
    final String out = builder.toString().trim();
    if (!out.isEmpty()) {
      System.out.println(out);
      StringSelection ss = new StringSelection(out);
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
    }
  }

  private static void process(String arg, StringBuilder builder) {
    boolean wordStart = true;
    for (int i=0; i<arg.length(); ++i) {
      char c = arg.charAt(i);
      if (Character.isWhitespace(c)) {
        wordStart = true;
        builder.append(c);
      } else {
        if (Character.isLetter(c)) {
          if (wordStart) {
            builder.append(Character.toUpperCase(c));
            wordStart = false;
          } else {
            builder.append(Character.toLowerCase(c));
          }
        } else {
          builder.append(c);
        }
      }
    }
    builder.append(' ');
  }
}
