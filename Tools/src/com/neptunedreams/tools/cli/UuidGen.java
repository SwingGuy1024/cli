package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.UUID;

/**
 * Add this to your ~/.bash_profile
 * alias uuid="java -cp ~/Tools/out/production/Tools com.neptunedreams.tools.cli.UuidGen"
 * or
 * alias uuid='java -cp ~/Tools/out/production/Tools com.neptunedreams.tools.cli.UuidGen -c'
 * alias uuidnc='java -cp ~/Tools/out/production/Tools com.neptunedreams.tools.cli.UuidGen'
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
    }

    private static UuidGen processArgs(String[] args) {
        UuidGen uuidGen = new UuidGen();
        for (String arg: args) {
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
                System.out.println("alias uuid=\"java -cp ~/Tools/out/production/Tools com.neptunedreams.tools.cli.UuidGen\"");
                System.exit(0);
            }
            String[] variations = { "c", "-c", "--c" };
            for (String v: variations) {
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
