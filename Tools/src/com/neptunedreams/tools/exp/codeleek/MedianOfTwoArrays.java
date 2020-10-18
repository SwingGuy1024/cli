package com.neptunedreams.tools.exp.codeleek;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Code challenge presented at https://leetcode.com/problems/median-of-two-sorted-arrays/submissions/
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/14/20
 * <p>Time: 10:14 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "AccessingNonPublicFieldOfAnotherObject"})
public class MedianOfTwoArrays {
  @SuppressWarnings("MagicNumber")
  public static void main(String[] args) {

    int[] alpha = { 5, 8, 9 };
    int[] bravo = { 11, 14, 16 };
    int[] charlie = { 3, 4, 5, 6, 7, 10, 12, 13 };
    int[] delta = {3, 5, 11 };
    //noinspection ZeroLengthArrayAllocation
    int[] echo = { };
    int[] foxtrot = { 6 };
    int[] golf = { 6, 8, 9, 14};
    int[] hotel = {12, 15, 18 };
    int[] romeo = {3, 4, 5, 6, 7, 8 };
    int[] juliet = {0, 1, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
    int[] kilo = {0, 1, 2, 4 };
    int[] lima = {3, 5, 6, 7, 8, 9, 10, 11};
    

    solve(kilo, lima);
    solve(romeo, juliet);
    solve(alpha, hotel);
    solve(alpha, charlie); // Works.
    solve(charlie, bravo);
    solve(delta, charlie);
    solve(charlie, echo);
    solve(charlie, foxtrot);
    solve(golf, charlie);
    List<TestCase> caseList = new LinkedList<>();
    // In each loop, the max value should be largeSize + 1
    for (int i=0; i<9; ++i) {
      caseList.add(new TestCase(i, 3, 8));
    }
    for (int i=0; i<8; ++i) {
      caseList.add(new TestCase(i, 4, 7));
    }
    
//    Map<Integer, Integer> map = new TreeMap<>(Comparator.<Integer>naturalOrder().reversed());
    
    for (TestCase testCase: caseList) {
      double value = solve(testCase.small, testCase.large);
      if (value != testCase.median) {
        throw new AssertionError();
      }
    }

    List<RandomTestCase> randomTestCases = new LinkedList<>();
    Random seed = new Random(5L);
    for (int i=0; i<1000; ++i) {
      randomTestCases.add(new RandomTestCase(seed, 3, 8));
      randomTestCases.add(new RandomTestCase(seed, 4, 7));
      randomTestCases.add(new RandomTestCase(seed, 3, 9));
      randomTestCases.add(new RandomTestCase(seed, 4, 8));
    }
    for (int i = 0; i < 1000; ++i) {
      randomTestCases.add(new RandomTestCase(seed, getDupStreamSupplier(), 3, 8));
      randomTestCases.add(new RandomTestCase(seed, getDupStreamSupplier(), 4, 7));
      randomTestCases.add(new RandomTestCase(seed, getDupStreamSupplier(), 3, 9));
      randomTestCases.add(new RandomTestCase(seed, getDupStreamSupplier(), 4, 8));
    }
    
    for (int smallSize = 1; smallSize<15; ++smallSize) {
      for (int largeSize = smallSize; largeSize < 15; ++largeSize) {
       for (int i=0; i<1000; ++i) {
         randomTestCases.add(new RandomTestCase(seed, smallSize, largeSize));
       }
      }
    }
    for (RandomTestCase randomTestCase: randomTestCases) {
      double value = solve(randomTestCase.small, randomTestCase.large);
      if (value != randomTestCase.median) {
        throw new AssertionError(String.format("Mismatch id=%d: %3.1f != %3.1f", randomTestCase.id, randomTestCase.median, value));  // NON-NLS
      }
    }
  }
  
  private static class TestCase {
    private final double median;
    private final int[] small;
    private final int[] large;
    
    TestCase(int startingIndexInSmallArray, int smallSize, int largeSize) {
      System.out.printf("startingIndexInSmallArray(%d, %d, %d)%n", startingIndexInSmallArray, smallSize, largeSize); // NON-NLS
      small = new int[smallSize];
      large = new int[largeSize];
      int base = 100;

      int inSmall = startingIndexInSmallArray;
      for (int index=0; index<smallSize; ++index) {
        small[index] = base + inSmall++;
      }
      int i=0;
      for (int index=0; index<startingIndexInSmallArray; ++index) {
        large[index] = base + i++;
      }
      int breakValue = startingIndexInSmallArray + smallSize;
      for (int index=startingIndexInSmallArray; index<largeSize; ++index) {
        large[index] = base + breakValue++;
      }
      median = base + ((smallSize + largeSize) / 2.0);
    }
  }
  
  private static class RandomTestCase {
    private static final AtomicInteger idGen = new AtomicInteger(0);
    private final int id;
    private static final Integer[] EMPTY = new Integer[0];
    private final double median;
    private final int[] small;
    private final int[] large;
    
    RandomTestCase(Random seed, int smallSize, int largeSize) {
      this(seed, getStreamSupplier(), smallSize, largeSize);
    }

    RandomTestCase(Random seed, Function<Integer, IntStream> intStreamSupplier, int smallSize, int largeSize) {
      id = idGen.incrementAndGet();
      List<Integer> iList = toRandomAccessList(intStreamSupplier.apply(smallSize + largeSize));
      if (((iList.size()) % 2) == 0) {
        int half = iList.size()/2;
        median = (iList.get(half-1) + iList.get(half))/2.0;
      } else {
        median = iList.get(iList.size()/2);
      }
      List<Integer> smallList = new ArrayList<>();
      for (int i=0; i<smallSize; ++i) {
        smallList.add(iList.remove(seed.nextInt(largeSize)));
      }
      Collections.sort(smallList);
      small = toIntArray(smallList.toArray(EMPTY));
      large = toIntArray(iList.toArray(EMPTY));
    }

  }
  
  private static int[] toIntArray(Integer[] array) {
    int[] out = new int[array.length];
    int index=0;
    for (Integer i: array) {
      out[index++] = i;
    }
    return out;
  }
  
  private static IntStream getStream(int max) {
    return IntStream.range(0, max);
  }
  
  private static Function<Integer, IntStream> getStreamSupplier() {
    return MedianOfTwoArrays::getStream;
  }

  private static IntStream getDupStream(int max) {
    return IntStream.range(0, max).map((i) -> i/2);
  }
  
  private static Function<Integer, IntStream> getDupStreamSupplier() {
    return MedianOfTwoArrays::getDupStream;
  }
  
  private static List<Integer> toRandomAccessList(IntStream intStream) {
    return intStream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  private static double solve(int[] a, int[] b) {
    MedianOfTwoArrays solution = new MedianOfTwoArrays();
//    System.out.printf("id: %d%n", id);
//    System.out.printf("%s  (%d)%n", asString(a), a.length);
//    System.out.printf("%s  (%d) total %d%n", asString(b), b.length, a.length + b.length);
//    System.out.printf("%5.1f%n%n", median);
    return solution.findMedianSortedArrays(a, b);
  }

  public double findMedianSortedArrays(int[] numsA, int[] numsB) {
    assert isSortedAssertion(numsA);
    assert isSortedAssertion(numsB);

    if (numsA.length == 0) {
      return getMedian(numsB);
    }
    if (numsB.length == 0) {
      return getMedian(numsA);
    }

    ArrayInfo infoA = new ArrayInfo(numsA);
    ArrayInfo infoB = new ArrayInfo(numsB);

    if (numsA.length > numsB.length) {
      return findMedian(infoA, infoB);
    }
    return findMedian(infoB, infoA);
  }

  private double findMedian(ArrayInfo alpha, ArrayInfo bravo) {
    int shortLength = bravo.array.length;
    int longMedianIndex = alpha.array.length/2;
    alpha.highIndex = Math.min(alpha.highIndex, longMedianIndex + shortLength);
    alpha.lowIndex = Math.max(alpha.lowIndex, longMedianIndex - shortLength);
    
    int total = alpha.array.length + bravo.array.length;
    int half = total / 2;
    double found;
    if ((total % 2) == 0) {
      found = searchInEvenArrays(alpha, bravo, half);
      if (Double.isNaN(found)) {
        return searchInEvenArrays(bravo, alpha, half);
      }
    } else {
      found = searchInOddArrays(alpha, bravo, half);
      if (Double.isNaN(found)) {
        return searchInOddArrays(bravo, alpha, half);
      }
    }
    return found;
  }

  private double searchInOddArrays(final ArrayInfo alpha, final ArrayInfo bravo, final int half) {
    int priorIndex;
    int index = -1;
    do {
      priorIndex = index;
      index = (alpha.highIndex + alpha.lowIndex) / 2;
      Status status = isMedianInOddTotal(index, alpha, bravo, half);
      if (status == Status.EQUAL) {
        return alpha.array[index];
      }
    } while (priorIndex != index);
    return Double.NaN;
  }

  private double searchInEvenArrays(final ArrayInfo alpha, final ArrayInfo bravo, final int half) {
    int priorIndex;
    int index = -1;
    do {
      priorIndex = index;
      index = (alpha.highIndex + alpha.lowIndex) / 2;
      int status = isMedianInEvenTotal(index, alpha, bravo, half);
      if (status != -1) {
        int rival;
        if (status >= bravo.array.length) {
          rival = alpha.array[index+1];
        } else if ((index + 1) == alpha.array.length) {
          rival = bravo.array[status];
        } else {
          rival = Math.min(bravo.array[status], alpha.array[index + 1]);
        }
        //noinspection MagicNumber
        return (alpha.array[index] + rival)/2.0;
      }
    } while (priorIndex != index);
    return Double.NaN;
  }

  private double getMedian(int[] array) {
    int half = array.length / 2;
    if ((array.length % 2) == 0) {
      int highValue = array[half];
      int lowValue = array[half - 1];
      //noinspection MagicNumber
      return (highValue + lowValue) / 2.0;
    }
    return array[half];
  }

  public int isMedianInEvenTotal(int index, ArrayInfo owner, ArrayInfo other, int half) {
    int candidate = owner.array[index];
    if (((index + 1 + other.array.length) == half) && (other.array[other.array.length - 1] < candidate)) {
      return other.array.length;
    }
    int otherIndex = half - index - 1;
    if (otherIndex < 0) {
      owner.highIndex = index;
      return -1;
    }
    if (otherIndex >= other.array.length) {
      owner.lowIndex = index;
      return -1;
    }
    if (candidate > other.array[otherIndex]) {
      owner.highIndex = index;
      other.lowIndex = otherIndex;
      return -1; // candidate is bigger than the median
    }
    if ((otherIndex != 0) && (candidate < other.array[otherIndex - 1])) {
//      if (candidate > )
      owner.lowIndex = index;
      other.highIndex = otherIndex;
      return -1; // candidate is smaller than the median
    }
    return otherIndex;
  }

  public Status isMedianInOddTotal(final int index, ArrayInfo owner, ArrayInfo other, int half) {
    int candidate = owner.array[index];
    if (((index + other.array.length) == half) && (other.array[other.array.length - 1] < candidate)) {
      return Status.EQUAL;
    }
    int otherIndex = half - index;
    if (otherIndex < 0) {
      owner.highIndex = index;
      return Status.BIGGER;
    }
    if (otherIndex >= other.array.length) {
      owner.lowIndex = index;
      return Status.SMALLER;
    }
    if ((candidate > other.array[otherIndex])) {
      owner.highIndex = index;
      other.lowIndex = otherIndex;
      return Status.BIGGER; // candidate is bigger than the median
    }
    if ((otherIndex != 0) && (candidate < other.array[otherIndex - 1])) {
      owner.lowIndex = index;
      other.highIndex = otherIndex;
      return Status.SMALLER; // candidate is smaller than the median
    }
    return Status.EQUAL;
  }

  private static class ArrayInfo {
    private int highIndex; // Nothing higher than this index can be the median
    private int lowIndex;  // Nothing lower can be the median
    private final int[] array;

    ArrayInfo(int[] a) {
      array = a;
      lowIndex = -1;
      highIndex = a.length; // The half-way formula will max out at length-1
    }
  }

  public enum Status {
    SMALLER, EQUAL, BIGGER
  }
  
  private static boolean isSortedAssertion(int[] array) {
    if (array.length < 2) {
      return true;
    }
    int prior = array[0];
    for (int i=1; i<array.length; ++i) {
      if (array[i] < prior) {
        return false;
      }
      prior = array[i];
    }
    return true;
  }
  
  @SuppressWarnings("unused")
  public static String asString(int[] array) {
    //noinspection MagicCharacter
    return Arrays.toString(array).replace('[', '{').replace(']', '}');
  }
}
