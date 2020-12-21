//#!/usr/bin/java --source 11

import com.neptunedreams.tools.UrlConvert;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Convert HTTP form data, captured using Chrome:inspect, from raw source, into text suitable for use in Postman,
 * when pasted into
 * Add this to your ~/.bash_profile
 * alias topost="java -cp ~/Tools/out/production/Tools ToPost"
 * This doesn't work with the Shebang line, because I call a method from another class. But I think I don't really need this anymore.
 */
public enum ToPost {
  ;

  public static void main(String[] args) {
    if (args.length > 0) {
      System.err.println("Convert clipboard text from chrome inspect format to postman format.");
    } else {
      convert();
    }
  }

  private static void convert() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    try {
      String s = clipboard.getData(DataFlavor.stringFlavor).toString();
      String output = UrlConvert.chromeToPostman(s);
      StringSelection stringSelection = new StringSelection(output);
      clipboard.setContents(stringSelection, stringSelection);
    } catch (UnsupportedFlavorException e) {
      System.err.println("Unsupported Data on Clipboard: " + e.getLocalizedMessage());
    } catch (IOException e) {
      System.err.println("Unknown Exception: " + e.getLocalizedMessage());
    }
  }
}
