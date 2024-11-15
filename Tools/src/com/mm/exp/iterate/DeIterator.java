package com.mm.exp.iterate;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * <p>A DeIterator (Double-Ended Iterator) is a variation of a ListIterator, with different semantics. 
 * Unlike a ListIterator, when you call next() after calling prev(), a DeIterator doesn't return the same
 * value. This makes it easier to use in some situations, such as a user interface that lets the user view
 * different elements of an ordered list. The semantics, which are similar to those in ListIterater, are
 * described in the documentation of the different methods.</p>
 * 
 * <p>This class can wrap any implementation of java.util.ListIterator. You may create one directly from a
 * list, using the {@code from()} factory method, or wrap a ListIterator using the constructor.</p>
 * 
 * <p>Because of the different semantics, the {@code previousIndex()} and {@code nextIndex()} methods normally
 * return numbers that differ by two. The exceptions to this are before {@code next()} has been called for the
 * first time, and after an element has been removed. In these cases, the two values will differ by one.</p>
 * 
 * While this class wraps a standard ListIterator, it's not actually a decorator, because, with its different
 * semantics, it does not implement ListIterator, even the the API is almost the same.
 *
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 11/8/24</p>
 * <p>Time: 2:53 AM</p>
 * <p>@author Miguel Muñoz</p>
 */
public class DeIterator<E> {
  private final ListIterator<E> wrapped;

  // I need both a skipOneOnPrevious and a skipOneOnNext, because after calling remove(), both must be false.
  // The relationship between the wrapped ListIterator's cursor and the values returned by next() and previous()
  // depends on which of these two values is true. Never will they both be true, but they may both be false.
  // Usually, nextIndex() == previousIndex()+2. But when the DeIterator is first constructed, and after a call
  // to remove(), these will both be false, and nextIndex() == previousIndex()+1. 
  // When the last retrieval method was next(), then skipOneOnPrevious will be true, and the wrapped.cursor
  // will be equal to the value of nextIndex(). And when the last retrieval method was previous(), then
  // skipOneOnNext will be true, and the wrapped.cursor will be nextIndex()-1.

  private boolean skipOneOnPrevious = false;
  private boolean skipOneOnNext = false;
  
  // When the standard listIterator executes hasNext(), it compares the current index with the size of the list.
  // We can't do that, because we don't have the size of the list. Normally, our hasNext() method just calls the
  // wrapped ListIterator.hasNext() method. Knowing the size is only important when the last element gets removed.
  // So when we reach the end of the list, we know that wrapped.hasNext() will return false, and we can infer
  // the size of the list from that, and return a correct value for hasNext() This is really only used when the 
  // user has removed elements from the end of the list, and the list size is going down. And it's only used to 
  // implement hasNext() correctly.
  private int inferredListSize = -1;

  /**
   * Create a new ListIterator from a List. This wraps an instance of the ListIterator generated by the List.
   * @param list The List to iterate over
   * @return A DeIterator
   * @param <E> The element type of the backing List.
   */
  public static <E> DeIterator<E> from(List<E> list) {
    return new DeIterator<>(list.listIterator());
  }

  /**
   * WrapsIterator
   * @param wrappedIterator the ListIterator to wrap. 
   */
  public DeIterator(ListIterator<E> wrappedIterator) {
    wrapped = wrappedIterator;
  }

  /**
   <p>* Returns the next element in the list. This method may be called repeatedly to iterate through the list,
   or intermixed with called to {@link #previous}() to go back and forth.</p>
   * @return The next element in the list.
   * @throws NoSuchElementException if there is no next element
   */
  public E next() {
    if (skipOneOnNext) {
      wrapped.next();
    }
    skipOneOnPrevious = true;
    skipOneOnNext = false;

    final E next = wrapped.next();
    if (hasNext()) {
      inferredListSize = -1;
    } else {
      // This value only gets set when we're at the end of the list.
      inferredListSize = wrapped.nextIndex();
    }
    return next;
  }

  /**
   * <p>Return the previous element in the list. This method may be called repeatedly to iterate through the list
   * backwards, or intermixed with calls to {@link #next}() to go back and forth.</p>
   * @return The previous element in the list.
   * @throws NoSuchElementException if there is no previous element
   */
  public E previous() {
    if (skipOneOnPrevious) {
      wrapped.previous();
    }
    skipOneOnPrevious = false;
    skipOneOnNext = true;
    return wrapped.previous();
  }

  /**
   * <p>Returns {@code true} if this DeIterator has more elements when traversing the list in the forward
   * direction.(In other words, returns {@code true} if {@link #next} would return an element rather than
   * throwing an exception.)</p> 
   * 
   * @return {@code true} if the list iterator has more elements when traversing the list forward.
   */
  public boolean hasNext() {
    final boolean hasN = wrapped.hasNext();
    final int nIndex = nextIndex();

    // This only happens when we're at the end of the list, or when we removed the last element of the list.
    // (Compare this method to hasPrevious().)
    if (nIndex == inferredListSize) {
      return false;
    }
    return hasN;
  }

  /**
   * <p>Returns {@code true} if this DeIterator has more elements when traversing the list backwards. (In other
   * words, returns {@code true} if {@link #previous} would return an element rather than throwing an
   * exception.)</p>
   * @return {@code true} if the list iterator has more elements when traversing the list backwards
   */
  public boolean hasPrevious() {
    final boolean hasP = wrapped.hasPrevious();
    // We do this for cases where previousIndex() will skip one element and go to -1.
    return hasP && (previousIndex() >= 0); // previousIndex() = -1 when we're at the beginning of the list.
  }

  /**
   * <p>Returns the index of the element that would be returned by a subsequent call to {@link #next}.
   * (Returns list size if the list iterator is at the end of the list.)</p>
   *
   * @return the index of the element that would be returned by a
   * subsequent call to {@code next}, or list size if the list
   * iterator is at the end of the list
   */
  public int nextIndex() {
    int nextIndex = wrapped.nextIndex();
    if (skipOneOnNext) {
      nextIndex++;
    }
    return nextIndex;
  }

  /**
   * <p>Returns the index of the element that would be returned by a subsequent call to {@link #previous}.
   * (Returns -1 if the list iterator is at the beginning of the list.)</p>
   *
   * @return the index of the element that would be returned by a subsequent call to {@code previous}, or -1
   * if the list iterator is at the beginning of the list
   */
  public int previousIndex() {
    int prevIndex = wrapped.previousIndex();
    if (skipOneOnPrevious) {// && wrapped.hasPrevious()) {
      prevIndex--;
    }
    return prevIndex;
  }

  /**
   * <p>Removes from the list the last element that was returned by {@link #next} or {@link #previous} (optional
   * operation).  This call can only be made once per call to {@code next} or {@code previous}. It can be made
   * only if {@link #add} has not been called after the last call to {@code next} or {@code previous}.</p>
   *
   * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this DeIterator
   * @throws IllegalStateException         if neither {@code next} nor
   *                                       {@code previous} have been called, or {@code remove} or
   *                                       {@code add} have been called after the last call to
   *                                       {@code next} or {@code previous}
   */
  public void remove() {
    wrapped.remove();
    skipOneOnPrevious = false;
    skipOneOnNext = false;
    if (inferredListSize > -1) {
      inferredListSize--;
    }
  }

  public void set(E e) {
    wrapped.set(e);
  }

  /**
   * <p>Add an element into the list. If the last retrieval call was {@code next()}, it inserts the new element after the element that
   * {@code next()} just returned. Likewise, if the last retrieval call was {@code previous()}, it inserts the new
   * element before the element that {@code previous()} just returned.</p>
   * <p>If no retrieval method has been called yet, it inserts the element at the beginning of the list. If the last
   * element retrieved was removed, it replaces the removed element. More precicely, it inserts the new element before
   * the element that would be returned by a call to {@code next()}, if any, or at the end of the list.</p>
   * <p>Once added, the DeIterator treats it as if it were the last element returned by {@code previous()} or 
   * {@code next()}, so the element returned by a subsequent call to {@code previous()} or {@code next()} will not
   * change as a result of this operation. A call to {@code previous()} or {@code next()} immediately following the
   * call to {@code add()} will never return the newly-added element.</p> 
   * @param e the element to insert
   */
  public void add(E e) {
    wrapped.add(e);
    if (skipOneOnNext == skipOneOnPrevious) {
      skipOneOnPrevious = true;
    } else if (skipOneOnNext) {
      /*
          We were moving backward before calling add(), so the wrapped cursor will need to be before the element
          that we just added. But wrapped.add() puts it after the added element, so we need to go back one.
       */
      wrapped.previous();
    }
  }

  /**
   * Performs the given action for each remaining element until all elements
   * have been processed or the action throws an exception.  Actions are
   * performed in the order of iteration, if that order is specified.
   * Exceptions thrown by the action are relayed to the caller.
   * <p>
   * The behavior of an iterator is unspecified if the action modifies the
   * collection in any way (even by calling the {@link #remove remove} method
   * or other mutator methods of {@code Iterator} subtypes),
   * unless an overriding class has specified a concurrent modification policy.
   * <p>
   * Subsequent behavior of an iterator is unspecified if the action throws an
   * exception.
   *
   * @param action The action to be performed for each element
   * @throws NullPointerException if the specified action is null
   * @implSpec <p>The default implementation behaves as if:
   * <pre>
   * while (hasNext())
   *     action.accept(next());
   * </pre>
   */
  void forEachRemaining(Consumer<? super E> action) {
    Objects.requireNonNull(action);
    while (hasNext()) {
      action.accept(next());
    }
  }
}