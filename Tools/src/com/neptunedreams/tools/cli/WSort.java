// ue: wSort
package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>Sorts words, then prints them out in alphabetical order & upper case, with a blank line
 * whenever the word changes; then prints out the sizes of the word groups in descending order.</p>
 * <p>For example: If the user enters this:</p>
 * <pre>
 *   wSort a b c b d B d B e b
 * </pre>
 * <p>the output will look like this:</p>
 * <pre>
 *   A
 *
 *   B
 *   B
 *   B
 *   B
 *   B
 *  
 *   C
 *  
 *   D
 *   D
 *  
 *   E
 *   52111
 * </pre>
 * <p>Created by IntelliJ IDEA.
 * <br>Date: 7/3/24
 * <br>Time: 9:43 AM
 * <br>@author Miguel Muñoz</p>
 */
public enum WSort {
  ;

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      args = toString(System.in).split(" ");
    }
    List<String> sortedWords = new LinkedList<>(List.of(args));
    sortedWords.replaceAll(String::toUpperCase);
    Map<String, Integer> wordCount = new TreeMap<>();
    sortedWords.forEach(s -> wordCount.merge(s, 1, Integer::sum));
    Collections.sort(sortedWords);
    String priorWord = "";
    for (String s: sortedWords) {
      if (!s.equals(priorWord)) {
        System.out.println();
      }
      System.out.println(s);
      priorWord = s;
    }
    List<Integer> countSet = new LinkedList<>(wordCount.values());
    final Comparator<Integer> compareTo = Integer::compareTo;
    countSet.sort(compareTo.reversed());
    StringBuilder builder = new StringBuilder();
    for (int i: countSet) {
      System.out.print(i); // NON-NLS
      builder.append(i);
    }
    StringSelection ss = new StringSelection(builder.toString());
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
    System.out.println();
    System.exit(0); // Avoids bug where VM prints out diagnostics.
  }

  @SuppressWarnings("SameParameterValue")
  private static String toString(InputStream in) throws IOException {
    try (InputStreamReader reader = new InputStreamReader(in);
        StringWriter 
            writer = new StringWriter()) {
      reader.transferTo(writer);
      return writer.toString().trim();
    }
  }
}
