
// use: dist
package com.neptunedreams.tools.cli;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 7/3/24</p>
 * <p>Time: 12:01 PM</p>
 * <p>@author Miguel Muñoz</p>
 */
public enum Dist {
  ;
  private static final String GREEN = "\uD83D\uDFE9";
  private static final String YELLOW = "\uD83D\uDFE8";
  private static final String DARKGRAY = "⬛"; // 0x2B1B (dark gray box)

  public static void main(String[] args) {
    String master = args[0].toUpperCase();
    List<String> candidates = new LinkedList<>(List.of(Arrays.copyOfRange(args, 1, args.length)));
    candidates.replaceAll(String::toUpperCase);
    Map<String, List<String>> duples = match(master, candidates);
    wordSort(duples);
  }

  private static Map<String, List<String>> match(String master, List<String> candidates) {
    TreeMap<String, List<String>> result = new TreeMap<>();
    for (String c : candidates) {
      result.merge(matchWord(c, master), new LinkedList<>(List.of(c)), Dist::reMap);
    }
    return result;
  }
  
  private static <T> List<T> reMap(List<T> fullList, List<T> newEntries) {
    fullList.addAll(newEntries);
    return fullList;
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
    StringBuilder builder = new StringBuilder();
    for (char c: pattern) {
      switch (c) {
        case '.' -> builder.append(DARKGRAY);
        case '/' -> builder.append(YELLOW);
        case 'X' -> builder.append(GREEN);
        default -> throw new IllegalStateException("Unexpected value: " + c);
      }
    }
    return builder.toString();
  }
  
  private static int indexOf(char[] array, char c) {
    for (int i = 0; i < (array.length); ++i) {
      if (array[i] == c) {
        return i;
      }
    }
    return -1;
  }
  
  private static void wordSort(Map<String, List<String>> map) {
    List<Integer> countList = new LinkedList<>();
    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
      final List<String> matches = entry.getValue();
      for (String match: matches) {
        System.out.printf("%s %s%n", entry.getKey(), match); // NON-NLS
      }
      System.out.println(); // NON-NLS
      countList.add(matches.size());
    }
    countList.sort(Collections.reverseOrder());
    for (int i: countList) {
      System.out.print(i);
      System.out.print('.');
    }
    System.out.println();
  }
}
