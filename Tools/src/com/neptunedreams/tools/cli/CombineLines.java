package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Extracts text data from the clipboard and replaces internal line breaks with a single space.
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/29/22
 * <p>Time: 4:53 PM
 *
 * @author Miguel Mu–oz
 */
public enum CombineLines {
  ;
  public static void main(String[] args) throws IOException, UnsupportedFlavorException {
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
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
    StringSelection contents = new StringSelection(data.trim() + "\r\n ");
    systemClipboard.setContents(contents, contents);
  }
  
  private static String swapAll(String src, String bad, String good) {
    int badLen = bad.length();
    StringBuilder builder = new StringBuilder(src);
    while (builder.indexOf(bad) >= 0) {
      int index = builder.lastIndexOf(bad);
      builder.replace(index, index+badLen, good);
    }
    return builder.toString();
  }
}
