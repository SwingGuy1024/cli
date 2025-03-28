// use: uuid
package com.neptunedreams.tools.cli;

/*
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 12/10/20
 * <p>Time: 5:28 AM
 *
 * @author Miguel Mu\u00f1oz
 */

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.UUID;

/**
 * Generates a random UUID, prints it out, and copies it to the clipboard.
 */
public final class UuidGen {
  private UuidGen() { }

  public static void main(String[] args) {
    UuidGen uuidGen = processArgs(args);
    UUID uuid = UUID.randomUUID();
    if (uuidGen.useClipboard) {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      StringSelection stringSelection = new StringSelection(uuid.toString());
      clipboard.setContents(stringSelection, stringSelection);
      System.out.println(uuid + " -> Clipboard");
    } else {
      System.out.println(uuid);
    }
    System.exit(0); // Avoids bug where VM prints out diagnostics.
  }

  private static UuidGen processArgs(String[] args) {
    UuidGen uuidGen = new UuidGen();
    for (String arg : args) {
      if (arg.contains("h")) {
        System.out.println("Usage: uuid        Print out a new UUID and copy it to the clipboard.\n");
        System.out.println("       uuid -c     Print out a new UUID. (c for clean)");
        System.out.println("       uuid c\n");
        System.out.println("       uuid --help  Print out this text and exit.");
        System.out.println("       uuid --h");
        System.out.println("       uuid -help");
        System.out.println("       uuid -h");
        System.out.println("       uuid help");
        System.out.println("       uuid h");
        System.out.println("\nTo install, add this to your ~/.bash_profile:");
        System.out.println("alias uuid=\"java -cp ~/Tools/out/production/Tools UuidGen\"");
        System.exit(0);
      }
      String[] variations = {"c", "-c", "--c"};
      for (String v : variations) {
        if (v.equalsIgnoreCase(args[0])) {
          uuidGen.useClipboard = false;
          break;
        }
      }
    }
    return uuidGen;
  }

  private boolean useClipboard = true;
}
