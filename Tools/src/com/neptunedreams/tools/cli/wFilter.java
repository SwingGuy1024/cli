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

  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
      filter(args);
    }
  }
  
  private static void filter(String[] args) throws IOException {
    Set<String> archive = getArchive();
    StringBuilder builder = new StringBuilder();
    for (String candidate: args) {
      if (!archive.contains(candidate.toUpperCase())) {
        builder.append(candidate).append(' ');
      }
    }
    System.out.println(builder);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection stringSelection = new StringSelection(builder.toString());
    clipboard.setContents(stringSelection, stringSelection);
  }
  
  private static Set<String> getArchive() throws IOException {
    URL url = new URL("https", WWW_ROCKPAPERSHOTGUN_COM, FILE);
    URLConnection connection = url.openConnection();
    connection.connect();
    Set<String> archive = new HashSet<>();
    try (BufferedReader ir = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      boolean searching = true;
      while(searching) {
        String line = ir.readLine();
        if ("<ul class=\"inline\">".equals(line)) {
          searching = false;
        }
      }
      boolean processing = true;
      while (processing) {
        String word = ir.readLine().trim();
        if (word.startsWith("<li>")) {
          archive.add(clean(word)); // NON-NLS
        } else {
          processing = false;
        }
      }
    }
    return archive;
  }
  
  private static String clean(String word) {
    int start = word.indexOf('>') + 1;
    int end = word.lastIndexOf('<');
    return word.substring(start, end);
  }
}
