// use: wSort
package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;

/**
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 7/3/24</p>
 * <p>Time: 9:43 AM</p>
 * <p>@author Miguel Muñoz</p>
 */
public enum WSort {
  ;

  public static void main(String[] args) {
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
  }
}
