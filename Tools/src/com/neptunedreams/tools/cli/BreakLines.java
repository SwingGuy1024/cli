// use: brklns
package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Convertes clipboard text, replacing literal \n with new line character, etc.
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 7/13/21
 * <p>Time: 11:44 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum BreakLines {
  ;

  public static void main(String[] args) throws IOException, UnsupportedFlavorException {
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    String data = systemClipboard.getData(DataFlavor.stringFlavor).toString();
    StringSelection filteredContent = new StringSelection(filter(data));
    systemClipboard.setContents(filteredContent, filteredContent);
    System.out.println(filter(data));
    System.exit(0); // Avoids bug where VM prints out diagnostics.
  }
  
  private static String filter(String data) {
    return data
        .replace("\\n", "\n")
        .replace("\\r", "\r")
        .replace("\\f", "\f")
        .replace("\\t", "\t");
  }
}
