package com.neptunedreams.tools.exp.codeleek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
A string s is called good if there are no two different characters in s that have the same frequency.

Given a string s, return the minimum number of characters you need to delete to make s good.

The frequency of a character in a string is the number of times it appears in the string. For example, in the string "aab", the frequency of 'a' is 2, while the frequency of 'b' is 1.
 */

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/7/20
 * <p>Time: 7:27 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public class ContestB {
  public int minDeletions(String s) {
    Map<Character, Integer> fMap = new HashMap<>();
    char[] data = s.toCharArray();
    for (int i = 0; i < data.length; ++i) {
      char c = data[i];
      Integer count = fMap.get(c);
      if (count == null) {
        fMap.put(c, 1);
      } else {
        fMap.put(c, count + 1);
      }
    }

    int fSum = 0;
    for (Integer i : fMap.values()) {
      fSum += i;
    }
    List<Integer> fList = new ArrayList<>(fMap.values());
    Collections.sort(fList, Comparator.reverseOrder());
    int lastHead = fList.remove(0);
    List<Integer> revised = new LinkedList<>();
    while (!fList.isEmpty() && (lastHead > 0)) {
      revised.add(lastHead);
      int i=0;
      int newHead = lastHead-1;
      for (int head: fList) {
        if (head == lastHead) {
          fList.set(i++, newHead);
        } else {
          break;
        }
      }
      lastHead = fList.remove(0);
    }
    revised.add(lastHead);
    
    int newSum = 0;
    for (int i: revised) {
      newSum += i;
    }
    return fSum - newSum;
  }

  public static void main(String[] args) {
//    System.out.println(new ContestB().minDeletions("abcabcabcabcabcd"));
    System.out.println(new ContestB().minDeletions("bbcebab"));
  }
}
