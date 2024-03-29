package com.neptunedreams.tools.exp.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <p>Iterator to generate Three-Smooth numbers in order, which are numbers with no prime factors greater than 3.</p>
 * <p>Here's how it works. Here is a table of three-smooth numbers, arranged by the exponents of 2 and 3. Moving
 * horizontally along the table raises the exponent of 2 by one, and moving vertically, does that for the
 * exponent of 3:</p>
 * <pre>
 *
 * <b>\u2193 Exponent of 3</b>
 *     <b>0    1    2    3    4    5  \u2190 Exponent of 2</b> 
 * <b>0</b>   1    2    4    8   16   32 ...
 * <b>1</b>   3    6   12   24   48   96
 * <b>2</b>   9   18   36   72  144  288
 * <b>3</b>  27   54  108  216  432  864
 * <b>4</b>  81  162  324  648 1296 2592
 * </pre>
 * <p>As you can see, each element in the table is double the element to its left, and triple the element above it.</p>
 * <p>After finding a few numbers in the sequence, here's the procedure to find the next on. For each row, you look at
 * the element to the right of the last one used. You do this for every row, starting from the top, until you reach
 * a row that gives you its first element. You return the smallest number you find. So, say the last one you found
 * was 12. You consider the 16 from row 1, the 24 from row 2, the 18 from row three, and the 27 from row 4. You stop
 * there, because 27 is the first element of its row. Of those four numbers, the lowest is 16, so that's what you
 * return. The next time you look, you'll include the 32 from the first row, but none of the others change.</p>
 * <p>I practice, I don't generate the entire table, I just keep a single List, called {@code rows}, which
 * includes the "next" element from its row. So after finding 12, {@code rows} would have these elements:
 * { 16, 24, 18, 27 }. This return the lowest one, which is 16. Then it doubles that value and replace it in the table,
 * so {@code rows} becomes { 32, 24, 18, 27 }. If it needs to replace the last element, it first triples it and
 * appends it to {@code rows}. So if the last number it returned was 24, the List will have these four elements:
 * { 32, 48, 36, 27 }. Then, after returning the lowest element, which is 27, {@code rows} will now contain five
 * elements: { 32, 48, 36, 54, 81 }.</p>
 * <p>The number of elements in {@code rows} will be Log<sub>3</sub>(R), where R is the last result returned. So, to
 * begin after one million, start the generator with 2<sup>12</sup>3<sup>5</sup>. This produces the last
 * threeSmooth number before 1,000,000, which is 995328. Log<sub>3</sub>(1,000,000) is 12.58… so the {@code rows} list
 * will have 13 values.</p>
 * <p>To do: Add a limit member</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 9/26/23
 * <p>Time: 12:04 PM
 *
 * @author Miguel Muñoz
 */
public final class ThreeSmooth implements Iterator<Integer> {
  private static final int maxRows = 40; // Forty rows will get you to 2^63
  /**
   * Create a ThreeSmooth iterator that starts from zero.
   */
  public ThreeSmooth() {
    rows.add(1);
  }

  /**
   * Create a ThreeSmooth iterator that starts with the specified value, or the first threeSmooth number after that value.
   * @param start Starting ThreeSmooth number. The first number returned by the iterator will be the lowest threeSmooth
   *              number greater than or equal to {@code start}
   */
  public ThreeSmooth(int start) {
    int nextRow = 1;
    rows.add(nextRow);
    int index = 0;
    int nextColumn = 1;
    while (rows.get(rows.size()-1) < start) {
      while (nextRow < start) {
        nextRow *= 2;
        rows.set(index, nextRow);
      }
      index++;
      nextColumn *= 3;
      nextRow = nextColumn;
      rows.add(nextColumn);
    }
  }
  
  private final List<Integer> rows = new ArrayList<>(maxRows);
  private boolean limitReached = false;

  @Override
  public boolean hasNext() {
    return !limitReached;
  }

  public Integer next() {
    if (limitReached) {
      throw new NoSuchElementException("Limit exceeded");
    }
    Iterator<Integer> itr = rows.iterator();
    int index = 0;
    int lowIndex = 0;
    int lowest = itr.next();
    int nextCandidate = lowest;
    while (itr.hasNext()) {
      index++;
      nextCandidate = itr.next();
      if (nextCandidate < lowest) {
        lowest = nextCandidate;
        lowIndex = index;
      }
    }
    if ((lowIndex+1) == rows.size()) {
      // nextCandidate is now the last element read
      final int next3Power = nextCandidate * 3;
      // The maximum value of the entire sequence will always be a power of two, but we max out on the 3 powers first.
      if (next3Power < 0) {
        rows.add(Integer.MAX_VALUE);
      } else {
        rows.add(next3Power);
      }
    }
    int next = rows.get(lowIndex);
    int newNext = next * 2;
    if (newNext < next) { // test for overflow (newNext will be negative)
      limitReached = true;
    }
    rows.set(lowIndex, next*2);
    return next;
  }
  
  private static void printExp(int base, int exp) {
    switch (exp) {
      case 1:
        System.out.print(base);
        break;
      case 0:
        break;
      default:
        System.out.printf("%d^%d", base, exp);
    }
  }

  /**
   * Convert milliseconds to HMS
   *
   * @param ms time in milliseconds
   * @return A String of the form h:MM:ss.mmm
   */
  public static String format(long ms) {
    long tail = ms % 1000L;
    long allSeconds = ms / 1000L;
    long seconds = allSeconds % 60L;
    long allMinutes = allSeconds / 60L;
    long minutes = allMinutes % 60L;
    long allHours = allMinutes / 60L;
    long hours = allHours % 24L;
    if (hours == 0) {
      return String.format("   %2d:%02d.%03d", minutes, seconds, tail);
    }
    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, tail);
  }

  public static void main(String[] args) {
    System.out.println('\n');

//        int limit = Integer.MAX_VALUE-1;
//    int limit = 4437222213480873985L;
    int limit = 1000000;
    System.out.println("Limit: " + limit);
    int nextThreeSmooth;
    int count = 0;
    long start = System.currentTimeMillis();
    ThreeSmooth threeSmooth = new ThreeSmooth();
    do {
      nextThreeSmooth = threeSmooth.next();
      System.out.printf("%3d: %d%n", ++count, nextThreeSmooth);
    } while (threeSmooth.hasNext() && (nextThreeSmooth <= limit));
//    } while (threeSmooth.hasNext());
    long end = System.currentTimeMillis();
    System.out.println(format(end-start));
    
    System.out.println("\n\nStarting with 243");
    ThreeSmooth nineHundred = new ThreeSmooth(243);
    do {
      nextThreeSmooth = nineHundred.next();
      System.out.printf("%3d: %d%n", ++count, nextThreeSmooth);
    } while (threeSmooth.hasNext() && (nextThreeSmooth < limit));
  }
}
