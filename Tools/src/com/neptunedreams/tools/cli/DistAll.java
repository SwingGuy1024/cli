package com.neptunedreams.tools.cli;
// use: distAll

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 7/29/24</p>
 * <p>Time: 12:48 AM</p>
 * <p>@author Miguel Muñoz</p>
 */
public enum DistAll {
  ;

  public static void main(String[] args) {
    Map<String, String> results = new HashMap<>();
    for (String s : args) {
      String result = dist(s, args);
      results.put(s, result);
    }
    for (String s : args) {
      System.out.printf("%s:%s%n", s, results.get(s)); // NON-NLS
    }
  }
  
  private static String dist(String master, String[] candidates) {
    List<String> patterns = match(master, List.of(candidates));
    String result = wSort(patterns);    
//    StringBuilder builder = new StringBuilder();
//    for (Dist.Duple duple : duples) {
//      builder
//          .append(duple.pattern())
//          .append(' ');
//    }
//    String result = builder.toString();
    return result;
//    for (String s : candidates) {
//      System.err.print(s + ' ');
//    }
//    System.err.println();
//    System.err.println(result);
//    System.out.println(result);
  }

  private static List<String> match(String master, List<String> candidates) {
    List<String> result = new LinkedList<>();
    for (String c : candidates) {
      result.add(matchWord(c, master));
    }
    return result;
  }

  public static String wSort(List<String> sortedWords) {
    Map<String, Integer> wordCount = new TreeMap<>();
    sortedWords.forEach(s -> wordCount.merge(s, 1, Integer::sum));
    Collections.sort(sortedWords);
//    String priorWord = "";
//    for (String s : sortedWords) {
//      if (!s.equals(priorWord)) {
//        System.out.println();
//      }
//      System.out.println(s);
//      priorWord = s;
//    }
    List<Integer> countSet = new LinkedList<>(wordCount.values());
    final Comparator<Integer> compareTo = Integer::compareTo;
    countSet.sort(compareTo.reversed());
    StringBuilder builder = new StringBuilder();
    for (int i : countSet) {
//      System.out.print(i); // NON-NLS
      builder.append(i).append('.');
    }
    builder.deleteCharAt(builder.length()-1);
    return builder.toString();
//    StringSelection ss = new StringSelection(builder.toString());
//    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
//    System.out.println();
  }

  private static String matchWord(String candidate, String master) {
    if (candidate.length() != master.length()) {
      throw new IllegalStateException(String.format("Mismatching lengths: %d != %d for (%s, %s)",
          candidate.length(), master.length(), candidate, master));
    }
    char[] pattern = ".....".toCharArray();
    char[] mChars = master.toUpperCase().toCharArray();
    char[] cChars = candidate.toCharArray();

    // Find Green matches 
    for (int i = 0; i < pattern.length; ++i) {
      if (cChars[i] == mChars[i]) {
        pattern[i] = 'X';
        mChars[i] = '.';
        cChars[i] = '.';
      }
    }

    // find Yellow Matches
    for (int i = 0; i < pattern.length; ++i) {
      char c = mChars[i];
      if (c != '.') {
        int index = indexOf(cChars, c);
        if (index >= 0) {
          pattern[i] = '/';
          cChars[index] = '.';
        }
      }
    }
    return new String(pattern);
  }

  private static int indexOf(char[] array, char c) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == c) {
        return i;
      }
    }
    return -1;
  }

  record Duple(String word, String pattern) { }
}
