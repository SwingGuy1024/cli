package com.mm.exp.iterate;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>DeIterater test</p>
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 11/8/24</p>
 * <p>Time: 8:32 AM</p>
 * <p>@author Miguel Muñoz</p>
 */
class DeIteratorTest {
  public static final String ALPHA = "Alpha";
  public static final String BRAVO = "Bravo";
  public static final String CHARLIE = "Charlie";
  public static final String DINGO = "Dingo";
  public static final String ECHO = "Echo";
  private static final String[] array = {ALPHA, BRAVO, CHARLIE, DINGO, ECHO};
  public static final String REPLACED = "-Replaced";

  private List<String> backingList = null;
  private DeIterator<String> deIterator=null;
  private DeIterator<String> generateModifyingIterator() {
    backingList = new ArrayList<>(Arrays.asList(array));
    deIterator = DeIterator.from(backingList);
    return deIterator;
  }
  
  private DeIterator<String> generateImmutableIterator() {
    // Not actually immutable, but the size of the list can't be changed, which is all I need.
    backingList = Collections.unmodifiableList(Arrays.asList(array));
    deIterator = new DeIterator<>(backingList.listIterator());
    return deIterator;
  }

  /**
   * <p>Tests everything that should change when a retrieval method is called.</p>
   * <p>Normally, the expected next index should be two plus the expected previous index. However, 
   * there are two cases where they should differ by one: a) When the DeIterator is instantiated,
   * b) When the last retrieved element has been removed. For these cases the value of normal should
   * be false.</p>
   * <p>When changing directions, the index value should be adjusted by a value of 2 to account for 
   * the skipped element.</p>
   * @param index The index of the item we just retrieved
   * @param retrievedValue The value we just retrieved
   */
  private void assertAllMethods(int index, @Nullable String retrievedValue) {

    int expectedPreviousIndex = index - 1;
    int expectedNextIndex;
    int delta;
    // null means we haven't retrieved anything, or we removed what we retrieved.
    if (retrievedValue == null) {
      // When there's no retrieved value, we have either retrieved nothing yet, or removed the retrieved element.
      // So previousIndex() and nextIndex() should differ by one...
      expectedNextIndex = index;
      final String expectedValue = (index < backingList.size()) ? backingList.get(index) : "(none)";
      printValues(index, '[' + expectedValue + ']', expectedPreviousIndex, expectedNextIndex);
      delta = 1;
    } else {
      // ...For all other cases, they should differ by two.
      expectedNextIndex = index + 1;

      final String expected = backingList.get(index);
      assertEquals(expected, retrievedValue,
          () -> String.format("Incorrect retrieval at i=%d, expected %s, found %s", index, expected, retrievedValue));
      printValues(index, retrievedValue, expectedPreviousIndex, expectedNextIndex);
      delta = 2;
    }

    final int previousIndex = deIterator.previousIndex();
    final int nextIndex = deIterator.nextIndex();
    assertEquals(delta, nextIndex - previousIndex, "Error in calling assertAllMethods()");
    assertThat(previousIndex).isEqualTo(expectedPreviousIndex)
        .describedAs("Previous Index: {}", previousIndex);
    assertThat(nextIndex).isEqualTo(expectedNextIndex)
        .describedAs("Next Index: {}", nextIndex);
    assertThat(deIterator.hasPrevious()).isEqualTo(expectedPreviousIndex > -1);
    final boolean expected = expectedNextIndex < backingList.size();
    assertThat(deIterator.hasNext()).isEqualTo(expected)
        .describedAs(() -> String.format("hasNext() should be %b at index %d", expected, index));
    //noinspection PointlessBooleanExpression
    assertEquals(deIterator.hasPrevious() == false, deIterator.previousIndex() == -1);
    //noinspection PointlessBooleanExpression
    assertEquals(deIterator.hasNext() == false, deIterator.nextIndex() == backingList.size());
  }

  private void printValues(int index, String retrievedValue, int expectedPreviousIndex, int expectedNextIndex) {
    // Here's how to understand the printed line, from an example:
    //   Indexes: x -1 -  0 -  1  . ( 5)-- T: Alpha   from [Alpha, Bravo, Charlie, Dingo, Echo]
    // The x after Indexes means hasPrevious returned false. It shows a dot for true.
    // The three numbers that follow are previousIndex(), index, and nextIndex().
    // The . that follows them means hasNext returned true. It shows an x for false.
    // ( 5) is the size of the backing list. This is followed by the entire backing list.
    System.out.printf("Indexes: %c %2d - %2d - %2d  %c (size =%2d)-- T: %-12s from %s%n",
        deIterator.hasPrevious()? '.' : 'x', expectedPreviousIndex, index, expectedNextIndex,
        deIterator.hasNext()? '.' : 'x', backingList.size(), retrievedValue, backingList); // NON-NLS
  }

  @Test
  public void testNextExceptionAtEnd() {
    DeIterator<String> iterator = generateModifyingIterator();
    iterator.next();
    while (iterator.hasNext()) {
      iterator.next();
    }
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
  public void testNextExceptionAtBeginning() {
    DeIterator<String> iterator = generateModifyingIterator();
    iterator.next();
    while (iterator.hasNext()) {
      iterator.next();
    }
    while (iterator.hasPrevious()) {
      iterator.previous();
    }
    assertThrows(NoSuchElementException.class, iterator::previous);
  }

  @Test
  public void testForbiddenRemoveForward() {
    DeIterator<String> iterator = generateImmutableIterator();
    iterator.next();

    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }

  @Test
  public void testForbiddenRemoveReverse() {
    DeIterator<String> iterator = generateImmutableIterator();
    iterator.next();
    iterator.next();
    iterator.previous();

    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }
  
  @Test
  public void testRemoveTwice() {
    DeIterator<String> iterator = generateModifyingIterator();
    String element = iterator.next();
    while (!element.equals(CHARLIE)) {
      element = iterator.next();
    }
    iterator.remove();
    assertThrows(IllegalStateException.class, iterator::remove);
  }

  @Test
  public void testAllowedRemoveForward() {
    DeIterator<String> iterator = generateModifyingIterator();
    iterator.next();
    iterator.next();

    iterator.remove();

    assertThat(iterator.next()).isEqualTo(CHARLIE);
    assertThat(iterator.previous()).isEqualTo(ALPHA);
  }

  @Test
  public void testAllowedRemoveReverse() {
    DeIterator<String> iterator = generateModifyingIterator();
    iterator.next();
    iterator.next();
    iterator.next();
    iterator.previous();

    iterator.remove();

    assertThat(iterator.next()).isEqualTo(CHARLIE);
    assertThat(iterator.previous()).isEqualTo(ALPHA);
  }

  @Test
  public void testForEachRemaining() {
    DeIterator<String> iterator = generateModifyingIterator();
    iterator.next();
    iterator.next();
    iterator.next();
    iterator.previous();
    Set<String> set = new HashSet<>();

    iterator.forEachRemaining(set::add);

    Set<String> expectedSet = new HashSet<>(Arrays.asList(CHARLIE, DINGO, ECHO));
    assertThat(set.toArray()).containsAll(expectedSet);
    assertThat(set.size()).isEqualTo(3);
  }

  @Test
  public void testNext() {
    DeIterator<String> iterator = generateImmutableIterator();
    while (iterator.hasNext()) { // Go to the end.
      iterator.next();
    }
    String element = null;
    while (!BRAVO.equals(element)) { // Go back to Bravo
      element = iterator.previous();
    }
    
    element = iterator.next();
    
    assertThat(element).isEqualTo(CHARLIE);
  }

  @Test
  public void testPrevious() {
    DeIterator<String> iterator = generateImmutableIterator();
    String element = null;
    while (!DINGO.equals(element)) { // Go to Dingo
      element = iterator.next();
    }
    
    element = iterator.previous();
    
    assertThat(element).isEqualTo(CHARLIE);
  }

  @Test
  public void testHasNextAndHasPrevious() {
    // Test hasNext()
    DeIterator<String> iterator = generateModifyingIterator();
    assertTrue(iterator.hasNext());
    assertFalse(iterator.hasPrevious());
    iterator.next();
    assertFalse(iterator.hasPrevious());
    
    // iterate numerically, so as not to use hasNext() in the loop condition.
    for (int i=1; i<array.length; ++i) {
      assertTrue(iterator.hasNext());
      iterator.next();
      assertTrue(iterator.hasPrevious());
    }

    assertFalse(iterator.hasNext());

    // test hasPrevious()
    // Since this a DeIterator, we can only go back four times!
    final int limit = array.length -1 ;
    for (int i = 0; i < limit; ++i) {
      assertTrue(iterator.hasPrevious());
      iterator.previous();
      assertTrue(iterator.hasNext());
    }

    assertFalse(iterator.hasPrevious());

    // test hasNext() & hasPrevious() after calling remove() at beginning.
    iterator.remove();
    assertTrue(iterator.hasNext());
    assertFalse(iterator.hasPrevious());

    // test hasNext() & hasPrevious() after calling remove() at the end.
    while (iterator.hasNext()) {
      iterator.next();
    }

    iterator.remove();
    assertFalse(iterator.hasNext());
    assertTrue(iterator.hasPrevious());

    // test hasNext() & hasPrevious() after calling remove() in the middle.
    String element = null;
    while (!CHARLIE.equals(element)) {
      element = iterator.previous();
    }
    iterator.remove();
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasPrevious());
  }

  @Test
  public void testNextIndexAndPreviousIndex() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;

    // First case is special, because I want iterator.previousIndex() to return -1 twice in a row, before and after
    // I make the first call to iterator.next(). However, iterator.nextIndex() should advance from 0 to 1.
    assertAllMethods(index, null);

    String element = iterator.next();
    assertAllMethods(index++, element);
    for (int i=index; i<backingList.size(); i++) {
      assertThat(iterator.nextIndex()).isEqualTo(index);
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    index--; // because we're switching directions, index goes down by two.
    while (iterator.hasPrevious()) {
      element = iterator.previous();
      assertAllMethods(--index, element);
    }
    assertThat(iterator.previousIndex()).isEqualTo(-1);
    assertThat(iterator.nextIndex()).isEqualTo(1);

    index++; // because we're switching directions
    element = null;
    while (!CHARLIE.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    iterator.remove();
    assertAllMethods(--index, null);

    // We've reversed direction at the high end. Now we need to reverse direction at the low end.
    while (iterator.hasPrevious()) {
      element = iterator.previous();
      assertAllMethods(--index, element);
    }
    
    index++; // because we're switching directions
    element = iterator.next();
    assertAllMethods(index, element);
  }

  @Test
  public void testRemoveHighAndLow() {
    // This tests the remove operation at each end of the list.
    int index = 0;
    DeIterator<String> iterator = generateModifyingIterator();
    assertAllMethods(index, null);

    String element;

    while (iterator.hasNext()) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    iterator.remove();
    assertAllMethods(--index, null);
    
    element = iterator.previous();
    assertAllMethods(--index, DINGO);
    while (iterator.hasPrevious()) {
      element = iterator.previous();
      assertAllMethods(--index, element);
    }
    assertThat(element).isEqualTo(ALPHA);
    assertThat(iterator.previousIndex()).isEqualTo(-1);
    assertThat(iterator.nextIndex()).isEqualTo(1);
    
    iterator.remove();
    assertAllMethods(index, null);
    element = iterator.next();
    assertAllMethods(index++, element);
    element = iterator.next();
    assertAllMethods(index, element);
  }

  @Test
  public void testSetForbidden() {
    DeIterator<String> iterator = generateImmutableIterator();
    iterator.next();
    assertThrows(UnsupportedOperationException.class, () -> iterator.set(""));
  }

  @Test
  public void testAddForbidden() {
    DeIterator<String> iterator = generateImmutableIterator();
    assertThrows(UnsupportedOperationException.class, () -> iterator.add(""));
  }
  
  @Test
  public void testNext_thenSet_ThenNext() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    String element = null;
    while (!CHARLIE.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }

    final String newCharlie = element + REPLACED;
    iterator.set(newCharlie);
    assertEquals(newCharlie, backingList.get(2)); // list element 2 has been replaced
    assertEquals(array.length, backingList.size()); // list has not changed size.

    element = iterator.next();
    assertEquals(DINGO, element);
    assertAllMethods(index, element);
  }

  @Test
  public void testNext_ThenSet_ThenPrevious() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    String element = null;
    while (!CHARLIE.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }

    final String newCharlie = element + REPLACED;
    iterator.set(newCharlie);
    assertEquals(newCharlie, backingList.get(2)); // list element 2 has been replaced
    assertEquals(array.length, backingList.size()); // list has not changed size.

    index--;
    element = iterator.previous();
    assertEquals(BRAVO, element);
    assertAllMethods(--index, element);
  }
  
  @Test
  public void testPrevious_ThenSet_ThenPrevious() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    String element = null;
    while (!DINGO.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    --index;
    element = iterator.previous();
    assertAllMethods(--index, element);
    assertEquals(CHARLIE, element);
    final String newCharlie = element + REPLACED;
    iterator.set(newCharlie);
    assertEquals(newCharlie, backingList.get(2)); // list element 2 has been replaced
    assertEquals(array.length, backingList.size()); // list has not changed size.

    element = iterator.previous();
    assertAllMethods(--index, element);
    assertEquals(BRAVO, element);
  }
  
  @Test
  public void testPrev_ThenSetFirstElement() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    String element = iterator.next();
    assertAllMethods(index++, element);
    assertEquals(ALPHA, element);
    assertFalse(iterator.hasPrevious());
    String alphaReplaced = ALPHA + REPLACED;
    iterator.set(alphaReplaced);

    assertEquals(alphaReplaced, backingList.get(0));
    assertEquals(array.length, backingList.size());
    assertFalse(iterator.hasPrevious());
    
    element = iterator.next();
    assertAllMethods(index, element);
    assertEquals(BRAVO, element);
    
    --index;
    element = iterator.previous();
    assertAllMethods(index, element);
    assertEquals(alphaReplaced, element);
  }
  
  @Test
  public void testSetAtStart() {
    DeIterator<String> iterator = generateModifyingIterator();
    String alphaReplaced = ALPHA + REPLACED;
    assertThrows(IllegalStateException.class, () -> iterator.set(alphaReplaced));
  }

  @Test
  public void testSetAllowed() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    String element = null;
    while (!CHARLIE.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }

    final String newCharlie = element + REPLACED;
    iterator.set(newCharlie);
    assertEquals(newCharlie, backingList.get(2)); // list element 2 has been replaced
    assertEquals(array.length, backingList.size()); // list has not changed size.

    element = iterator.next();
    assertEquals(DINGO, element);
    assertAllMethods(index++, element);

    index--;
    element = iterator.previous();
    assertEquals(newCharlie, element);
    assertAllMethods(--index, element);
    element = iterator.previous();
    assertEquals(BRAVO, element);

    String newBravo = BRAVO + REPLACED;
    iterator.set(newBravo);
    System.out.println(backingList);
    assertEquals(newBravo, backingList.get(1)); // list element 1 has been replaced
    assertEquals(array.length, backingList.size()); // list has not changed size.
    assertAllMethods(--index, newBravo);

    element = iterator.previous();
    assertAllMethods(--index, element);
    assertEquals(ALPHA, element);
    String newAlpha = element + REPLACED;
    iterator.set(newAlpha);
    assertEquals(newAlpha, backingList.get(0)); // list element 0 has been replaced
    assertEquals(array.length, backingList.size()); // list has not changed size.
    assertAllMethods(index, newAlpha);

    element = iterator.next();
    assertEquals(newBravo, element);
    index++;
    assertAllMethods(index++, element);
    while (iterator.hasNext()) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    String newEcho = ECHO + REPLACED;
    iterator.set(newEcho);
    assertAllMethods(--index, newEcho);
    System.out.println(backingList);
  }
  
  @Test
  public void testAddAtStart() {
    DeIterator<String> iterator = generateModifyingIterator();
    assertAllMethods(0, null);

    // Test insert at the beginning of a list:
    final String newZeroValue = "AddAtZero";
    iterator.add(newZeroValue);
    int index = 0;
    assertAllMethods(index, newZeroValue);
    
    String element = iterator.next();
    assertEquals(ALPHA, element);
  }
  
  @Test
  public void testNext_ThenAdd_ThenNext() {
    DeIterator<String> iterator = generateModifyingIterator();
    assertAllMethods(0, null);
    String element=null;
    int index = 0;

    while (!DINGO.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    final String afterDingo = "AfterDingo";
    iterator.add(afterDingo);
    System.out.println(backingList);
    assertAllMethods(index++, afterDingo);
    
    element = iterator.next();
    assertAllMethods(index, element);
    assertEquals(ECHO, element);
  }
  
  @Test
  public void testNext_ThenAdd_ThenPrevious() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    assertAllMethods(index, null);

    String element = null;
    while (!DINGO.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    final String afterDingo = "AfterDingo";
    iterator.add(afterDingo);
    System.out.println(backingList);
    assertAllMethods(index, afterDingo);

    // test calling next(), then calling add(), then calling previous().
    element = iterator.previous();
    System.out.println(backingList);
    assertEquals(DINGO, element);
    assertAllMethods(--index, element);
  }
  
  @Test
  public void testPrevious_ThenAdd_ThenNext() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    assertAllMethods(index, null);

    System.out.println(backingList);
    String element = null;
    while (!DINGO.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    index--;
    while (!BRAVO.equals(element)) {
      element = iterator.previous();
      assertAllMethods(--index, element);
    }
    final String beforeBravo = "BeforeBravo";
    iterator.add(beforeBravo);
    System.out.println(backingList);
    assertAllMethods(index, beforeBravo);
    index++;
    element = iterator.next();
    assertAllMethods(index, element);
    assertEquals(BRAVO, element);
  }

  @Test
  public void testPrevious_ThenAdd_ThenPrevious() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    assertAllMethods(index, null);

    System.out.println(backingList);
    String element = null;
    while (!DINGO.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    index--;
    while (!BRAVO.equals(element)) {
      element = iterator.previous();
      assertAllMethods(--index, element);
    }
    final String beforeBravo = "BeforeBravo";
    iterator.add(beforeBravo);
    System.out.println(backingList);
    assertAllMethods(index, beforeBravo);
    element = iterator.previous();
    assertAllMethods(--index, element);
    assertEquals(ALPHA, element);
  }
  
  @Test
  public void testRemove_ThenAdd() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    assertAllMethods(index, null);

    System.out.println(backingList);
    String element = null;

    while (!CHARLIE.equals(element)) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    iterator.remove();

    assertAllMethods(--index, null);
    final String replacingCharlie = "ReplacingCharlie";
    iterator.add(replacingCharlie);
    System.out.println(backingList);
    assertAllMethods(index, replacingCharlie);
    assertEquals(replacingCharlie, backingList.get(index));
  }
  
  @Test
  public void testAppend_thenPrevious() {
    DeIterator<String> iterator = generateModifyingIterator();
    int index = 0;
    assertAllMethods(index, null);

    System.out.println(backingList);

    String element;
    while (iterator.hasNext()) {
      element = iterator.next();
      assertAllMethods(index++, element);
    }
    final String appended = "Appended";
    iterator.add(appended);
    System.out.println(backingList);
    assertFalse(iterator.hasNext());

    element = iterator.previous();
    System.out.println(backingList);
    assertEquals(ECHO, element);
    element = iterator.next();
    assertEquals(appended, element);
  }
}
