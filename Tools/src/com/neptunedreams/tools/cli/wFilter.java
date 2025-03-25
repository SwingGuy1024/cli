package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Examines an array of words and removes any that are past solutions to Wordle.</p>
 * <p>This uses the web page at www.rockpapershotgun.com/wordle-past-answers</p> 
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 8/27/24</p>
 * <p>Time: 9:38 AM</p>
 * <p>@author Miguel Muñoz</p>
 */
public enum wFilter {
  ;

  public static final String WWW_ROCKPAPERSHOTGUN_COM = "www.rockpapershotgun.com";
  public static final String FILE = "wordle-past-answers";

  public static void main(final String[] args) throws IOException {
    if (args.length > 0) {
      filter(args);
    }
    System.exit(0); // Avoids bug where VM prints out diagnostics.
  } // End of main(String[])
  
  private static void filter(final String[] args) throws IOException {
    final Set<String> archive = getArchive();
    final StringBuilder builder = new StringBuilder();
    for (final String candidate: args) {
      if (!archive.contains(candidate.toUpperCase())) {
        builder.append(candidate).append(' ');
      }
    }
    System.out.println(builder);
    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    final StringSelection stringSelection = new StringSelection(builder.toString());
    clipboard.setContents(stringSelection, stringSelection);
  }
  
  private static Set<String> getArchive() throws IOException {
    final URL url = new URL("https", WWW_ROCKPAPERSHOTGUN_COM, FILE);
    final URLConnection connection = url.openConnection();
    connection.connect();
    final Set<String> archive = new HashSet<>();
    try (final BufferedReader ir = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      boolean searching = true;
      while(searching) {
        final String line = ir.readLine();
        if ("<ul class=\"inline\">".equals(line)) {
          searching = false;
        }
      }
      boolean processing = true;
      while (processing) {
        final String word = ir.readLine().trim();
        if (word.startsWith("<li>")) {
          archive.add(clean(word)); // NON-NLS
        } else {
          processing = false;
        }
      }
    }
    return archive;
  }
  
  private static String clean(final String word) {
    final int start = word.indexOf('>') + 1;
    final int end = word.lastIndexOf('<');
    return word.substring(start, end);
  }
}
