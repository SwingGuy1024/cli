// use: dist
package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 7/3/24</p>
 * <p>Time: 12:01 PM</p>
 * <p>@author Miguel Muñoz</p>
 */
public enum Dist {
  ;

  public static void main(String[] args) {
    String master = args[0].toUpperCase();
    List<String> candidates = List.of(Arrays.copyOfRange(args, 1, args.length));
    List<Duple> duples = match(master, candidates);
    StringBuilder builder = new StringBuilder();
    for (Duple duple : duples) {
      builder
          .append(duple.pattern())
          .append(' ');
    }
    String result = builder.toString();
    StringSelection ss = new StringSelection(result);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
    for (String s: candidates) {
      System.err.print(s + ' ');
    }
    
    // Print the result to both System.err and System.out, because we assume the output will be piped to WSort,
    // but we still want to see it..
    System.err.println();
    System.err.println(result);
    System.out.println(result);
  }

  private static List<Duple> match(String master, List<String> candidates) {
    List<Duple> result = new LinkedList<>();
    for (String c : candidates) {
      result.add(new Duple(master, matchWord(c, master)));
    }
    return result;
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
