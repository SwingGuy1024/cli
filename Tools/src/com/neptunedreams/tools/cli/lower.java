package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/1/23
 * <p>Time: 2:06 AM
 *
 * @author Miguel Mu–oz
 */
public enum lower {
  ;

  public static void main(String[] args) {
    StringBuilder builder = new StringBuilder();
    for (String arg: args) {
      builder.append(arg.toLowerCase()).append(' ');
    }
    final String out = builder.toString().trim();
    System.out.println(out);
    StringSelection ss = new StringSelection(out);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
  }
}
