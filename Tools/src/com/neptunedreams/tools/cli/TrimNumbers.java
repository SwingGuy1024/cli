package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Sometimes, when code is copied from a web page, line numbers are interspersed through the code, making it hard to past it into a valid 
 * source file. When the line numbers are alone on a line, this utility may be used to skip over the numbered lines.
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 1/5/21
 * <p>Time: 4:19 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum TrimNumbers {
  ;
  private static final char NEW_LINE = '\n';

  public static void main(String[] args) throws IOException, UnsupportedFlavorException {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    String input = clipboard.getData(DataFlavor.stringFlavor).toString();

    try (BufferedReader reader = new BufferedReader(new StringReader(input));
         StringWriter writer = new StringWriter()
    ) {
      String oneLine = reader.readLine();
      while (oneLine != null) {
        if (!isNumber(oneLine)) {
          writer.append(oneLine).append(NEW_LINE);
        }
        oneLine = reader.readLine();
      }
      StringSelection output = new StringSelection(writer.toString());
      clipboard.setContents(output, output);
    }
  }
  
  private static boolean isNumber(String s) {
    for (int i=0; i<s.length(); ++i) {
      if (!Character.isDigit(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
