package com.neptunedreams.tools.exp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This was written to clarify how a ListIterator behaves when the direction gets reversed.
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 4/6/20
 * <p>Time: 4:32 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"HardCodedStringLiteral", "UseOfObsoleteCollectionType", "UseOfSystemOutOrSystemErr", "HardcodedLineSeparator", "StringConcatenation"})
public enum ListIteratorTest {
  ;

  public static void main(String[] args) {
    List<String> list = Arrays.asList("alpha", "bravo", "charlie", "delta", "echo", "foxtrot");
    testList(list);
    testList(new LinkedList<>(list));
    testList(new ArrayList<>(list));
    testList(new Vector<>(list));
    testList(new CopyOnWriteArrayList<>(list));
  }

  private static void testList(final List<String> list) {
    System.out.println("\n" + list.getClass());
    System.out.println("List of " + list.getClass());
    ListIterator<String> listIterator = list.listIterator();
    for (int i=0; i<4; ++i) {
      if (listIterator.hasNext()) {
        String next = listIterator.next();
        System.out.println(next);
      }
    }
    System.out.println(" -- backwards --");
    System.out.printf(" %d <-> %d%n", listIterator.previousIndex(), listIterator.nextIndex());
    for (int i=0; i<3; ++i) {
      if (listIterator.hasPrevious()) {
        String prev = listIterator.previous();
        System.out.println(prev);
      }
    }
    System.out.println(" -- forward --");
    System.out.printf(" %d <-> %d%n", listIterator.previousIndex(), listIterator.nextIndex());
    while (listIterator.hasNext()) {
      System.out.println(listIterator.next());
    }
  }
}
