package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Strip formatting out of text that's on the clipboard. This reads the System Clipboard text, then writes it back
 * to the System Clipboard with all formatting removed, so it will paste as plain text.
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 6/3/24</p>
 * <p>Time: 7:01 PM</p>
 * <p>@author Miguel Muñoz</p>
 */
public enum ToPlain {
  ;
  public static void main(String[] args) throws IOException, UnsupportedFlavorException {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    String text = clipboard.getData(DataFlavor.stringFlavor).toString();
    StringSelection stringSelection = new StringSelection(text);
    clipboard.setContents(stringSelection, stringSelection);
    System.out.println(text);
  }
}
