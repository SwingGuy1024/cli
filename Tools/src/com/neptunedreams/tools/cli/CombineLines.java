package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * <p>Extracts text data from the clipboard and replaces internal line breaks with a single space.</p>
 * <p>This functionality has been rolled into the GUI tool ClipboardTray</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/29/22
 * <p>Time: 4:53 PM
 *
 * @author Miguel Muñoz
 */
public enum CombineLines {
  ;
  public static void main(final String[] args) throws IOException, UnsupportedFlavorException {
    final Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    String data = systemClipboard.getData(DataFlavor.stringFlavor).toString();
//    System.out.printf("%s%n%n", data); // NON-NLS
    while (data.contains(" \r\n")) {
      data = swapAll(data, " \r\n", "\r\n");
    }
    while (data.contains("\r\n ")) {
      data = swapAll(data, "\r\n ", "\r\n");
    }
    while (data.contains("\r\n")) {
      data = swapAll(data, "\r\n", " ");
    }
    final StringSelection contents = new StringSelection(data.trim() + "\r\n ");
    systemClipboard.setContents(contents, contents);
    System.exit(0); // Avoids bug where VM prints out diagnostics.
  }
  
  private static String swapAll(final String src, final String bad, final String good) {
    final int badLen = bad.length();
    final StringBuilder builder = new StringBuilder(src);
    while (builder.indexOf(bad) >= 0) {
      final int index = builder.lastIndexOf(bad);
      builder.replace(index, index+badLen, good);
    }
    return builder.toString();
  }
}
