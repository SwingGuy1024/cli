package com.neptunedreams.tools.exp;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Synopsis: java.awt.datatransfer.Clipboard fails to transfer data to Debian Linux OS
 * 
 * Description: Strings placed on the System Clipboard does not transfer to OS clipboard after java application exits.
 * 
 * System/OS/Java:
 * openjdk version "11.0.11" 2021-06-29
 * OpenJDK Runtime Environment (build 11.0.11+9-google-release-382113311)
 * OpenJDK 64-Bit Server VM (build 11.0.11+9-google-release-382113311, mixed mode, sharing)
 *
 * No LSB modules are available.
 * Distributor ID:	Debian
 * Description:	Debian GNU/Linux rodete
 * Release:	rodete
 * Codename:	rodete
 * 
 * Steps to reproduce:
 * 1. Run the test code. It should print something like this:
 *   Success: Input at 1626556868517
 *   (Now try pasting into a non-Java application.)
 *
 * 2. Go to any non-java application and try to paste into any standard text field, such as a browser search field.
 * 
 * Expected Result: The text "Input at 1626556868517" from the previous output should get pasted into the text field.
 * 
 * Actual Result: The paste item is disabled in the edit menu and popup menu.
 *
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 7/14/21
 * <p>Time: 8:01 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum ClipboardBug {
  ;

  public static void main(String[] args) throws IOException, UnsupportedFlavorException {
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    String input = "Input at " + System.currentTimeMillis();
    StringSelection filteredContent = new StringSelection(input);
    systemClipboard.setContents(filteredContent, filteredContent);

    String result = Toolkit.getDefaultToolkit()
        .getSystemClipboard()
        .getData(DataFlavor.stringFlavor)
        .toString();
    if (result.equals(input)) {
      System.out.println("Success: " + result);
      System.out.println("(Now try pasting into a non-Java application.)");
    } else {
      System.out.printf("Mismatch: %s vs %s%n", input, result);
    }
    // Comments on bug JDK-8199261 suggest this might help.
    System.exit(0); // this doesn't help
  }
}
