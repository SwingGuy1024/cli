//#!/usr/bin/java --source 11
package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Useful for pasting header information, taken from the Chrome inspector, into postman. The trouble with this is that
 * the Chrome inspector inserts a space after the colon for readability. So when you paste it into postman, you need to
 * strip off a leading space from each field.
 * <p>
 * To use this, just launch it after copying data from the Chrome inspector, but before you paste it into Postman. This
 * will read the text from the clipboard, strip out all leading spaces, and put it back onto the clipboard for pasting.
 */
public enum HeaderPaste {
  ;

  public static void main(String[] args) throws IOException, UnsupportedFlavorException {
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    String data = systemClipboard.getData(DataFlavor.stringFlavor).toString();
    StringSelection filteredContent = new StringSelection(filter(data));
    systemClipboard.setContents(filteredContent, filteredContent);
  }

  private static String filter(String data) {
    //noinspection DynamicRegexReplaceableByCompiledPattern
    return data.replace(": ", ":");
  }
}
