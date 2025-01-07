
// use: dist
package com.neptunedreams.tools.cli;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

  public static void main(final String[] args) {
    final String master = args[0].toUpperCase();
    final List<String> candidates = new LinkedList<>(List.of(Arrays.copyOfRange(args, 1, args.length)));
    candidates.replaceAll(String::toUpperCase);
    final Map<Result, List<String>> duples = match(master, candidates);
    wordSort(duples);
  }

  private static Map<Result, List<String>> match(final String master, final List<String> candidates) {
    final TreeMap<Result, List<String>> result = new TreeMap<>();
    for (final String c : candidates) {
      result.merge(matchWord(c, master), new LinkedList<>(List.of(c)), Dist::reMap);
    }
    return result;
  }
  
  private static <T> List<T> reMap(final List<T> fullList, final List<T> newEntries) {
    fullList.addAll(newEntries);
    return fullList;
  }

  private static Result matchWord(final String candidate, final String master) {
    if (candidate.length() != master.length()) {
      throw new IllegalStateException(String.format("Mismatching lengths: %d != %d for (%s, %s)",
          candidate.length(), master.length(), candidate, master));
    }
    final char[] pattern = ".....".toCharArray();
    final char[] mChars = master.toUpperCase().toCharArray();
    final char[] cChars = candidate.toCharArray();

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
      final char c = mChars[i];
      if (c != '.') {
        final int index = indexOf(cChars, c);
        if (index >= 0) {
          pattern[i] = '/';
          cChars[index] = '.';
        }
      }
    }
    int score = 0;
    final StringBuilder builder = new StringBuilder();
    for (final char c: pattern) {
      switch (c) {
        case '.' -> builder.append(DARKGRAY);
        case '/' -> { 
          score += 11;
          builder.append(YELLOW);
        }
        case 'X' -> {
          score += 20;
          builder.append(GREEN);
        }
        default -> throw new IllegalStateException("Unexpected value: " + c);
      }
    }
    return new Result(builder.toString(), score);
  }
  
  private static int indexOf(final char[] array, final char c) {
    for (int i = 0; i < (array.length); ++i) {
      if (array[i] == c) {
        return i;
      }
    }
    return -1;
  }
  
  private static void wordSort(final Map<Result, List<String>> map) {
    final List<Integer> countList = new LinkedList<>();
    for (final Map.Entry<Result, List<String>> entry : map.entrySet()) {
      final List<String> matches = entry.getValue();
      for (final String match: matches) {
        System.out.printf("%s %s%n", entry.getKey(), match); // NON-NLS
      }
      System.out.println(); // NON-NLS
      countList.add(matches.size());
    }
    countList.sort(Collections.reverseOrder());
    for (final int i: countList) {
      System.out.print(i);
      System.out.print('.');
    }
    System.out.println();
  }
  
  private static class Result implements Comparable<Result> {
    private final String word;
    private final int score;

    private final Comparator<Result> comparator = 
        Comparator
            .<Result>comparingInt(r -> r.score)
            .thenComparing(r-> r.word);

    Result(final String s, final int score) {
      word = s;
      this.score = score;
    }

    @Override
    public int hashCode() {
      return word.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
      return (obj instanceof Result) && ((Result) obj).word.equals(word);
    }
    
    @Override
    public int compareTo(final Dist.Result o) {
      return comparator.compare(this, o);
//      return (Integer.compare(score, o.score);
    }

    @Override
    public String toString() {
      return word;
    }
  }
}
