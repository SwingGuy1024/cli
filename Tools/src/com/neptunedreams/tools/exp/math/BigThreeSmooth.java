package com.neptunedreams.tools.exp.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <p>Iterator over BigInteger to generate Three-Smooth numbers in order, which are numbers with no prime factors
 * greater than 3, or more precicely, integers of the form 2<sup>n</sup>3<sup>m</sup>.</p>
 * <p>Here's how it works. Here is a table of three-smooth numbers, arranged by the exponents of 2 and 3. Moving
 * horizontally along the table raises the exponent of 2 by one, and moving vertically, does that for the
 * exponent of 3:</p>
 * <pre>
 *
 * <b>\u2193 Exponent of 3</b>
 * <b>  <u>   0    1    2    3    4    5</u>  \u2190 Exponent of 2</b>
 * <b>0\u2595</b>   1    2    4    8   16   32 ...
 * <b>1\u2595</b>   3    6   12   24   48   96
 * <b>2\u2595</b>   9   18   36   72  144  288
 * <b>3\u2595</b>  27   54  108  216  432  864
 * <b>4\u2595</b>  81  162  324  648 1296 2592
 * </pre>
 * <p>As you can see, each element in the table is double the element to its left, and triple the element above it.</p>
 * <p>After finding a few numbers in the sequence, here's the procedure to find the next on. For each row, you look at
 * the element to the right of the last one used. You do this for every row, starting from the top, until you reach
 * a row that gives you its first element. You return the smallest number you find. So, say the last one you found
 * was 12. You consider the 16 from row 1, the 24 from row 2, the 18 from row three, and the 27 from row 4. You stop
 * there, because 27 is the first element of its row. Of those four numbers, the lowest is 16, so that's what you
 * return. The next time you look, you'll include the 32 from the first row, but none of the others change.</p>
 * <p>In practice, I don't generate the entire table, I just keep a single SortedSet, called {@code rows}, which
 * includes the "next" element from each row. I also keep track of which number comes from the last row. By using a
 * Tree, it runs faster, although speed isn't an issue. So after finding 12, {@code rows} would have these elements:
 * { 16, 24, 18, 27 }. This return the lowest one, which is 16. Then it doubles that value and replace it in the table,
 * so {@code rows} becomes { 32, 24, 18, 27 }. If it needs to replace the last element, it first triples it and
 * appends it to {@code rows}. So if the last number it returned was 24, the List will have these four elements:
 * { 32, 48, 36, 27 }. Then, after returning the lowest element, which is 27, {@code rows} will now contain five
 * elements: { 32, 48, 36, 54, 81 }.</p>
 * <p>The number of elements in {@code rows} will be ⌈Log<sub>3</sub>(R)⌉, where R is the last result returned. So, to
 * begin after one million, start the generator with any number n where 2<sup>12</sup>3<sup>5</sup> < n ≤ 1,000,000.
 * This range begins after the last threeSmooth number before 1,000,000, which is 995328. Log<sub>3</sub>(1,000,000)
 * is 12.58… so the {@code rows} list will have 13 elements.</p>
 * <p>This class implements the {@code rows} array as a TreeSet instead of an ArrayList, to improve performance.</p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/1/23
 * <p>Time: 12:34 PM
 *
 * @author Miguel Muñoz
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class BigThreeSmooth implements Iterator<BigInteger> {
//  private static final int maxRows = 40; // Forty rows will get you to 2^63
  public static final BigInteger THREE = BigInteger.valueOf(3);
  private final SortedSet<BigInteger> rows = new TreeSet<>();
  private BigInteger maxThreePower = BigInteger.ONE;

  /**
   * Create a ThreeSmooth iterator that starts from zero.
   */
  private BigThreeSmooth() {
    rows.add(BigInteger.ONE);
  }

  /**
   * Create a BigThreeSmooth iterator that starts with the specified value, or the first threeSmooth number after that
   * value.
   *
   * @param start Starting threeSmooth number. The first number returned by the iterator will be the lowest threeSmooth
   *              number greater than or equal to {@code start}
   */
  private BigThreeSmooth(BigInteger start) {
    ArrayList<BigInteger> tempRows = new ArrayList<>(40);
    BigInteger nextRow = BigInteger.ONE;
    tempRows.add(nextRow);
    int index = 0;
    BigInteger nextColumn = BigInteger.ONE;
    while (tempRows.get(tempRows.size() - 1).compareTo(start) < 0) {
      while (nextRow.compareTo(start) < 0) {
        nextRow = nextRow.multiply(BigInteger.TWO);
        tempRows.set(index, nextRow);
      }
      index++;
      nextColumn = nextColumn.multiply(THREE);
      nextRow = nextColumn;
      tempRows.add(nextColumn);
    }
    rows.addAll(tempRows);
  }

  /**
   * Create a BigThreeSmooth iterator that starts with the specified integer or long value, or the first threeSmooth
   * number after that value.
   *
   * @param start Starting threeSmooth number. The first number returned by the iterator will be the lowest threeSmooth
   *              number greater than or equal to {@code start}
   */
  @SuppressWarnings("unused")
  public BigThreeSmooth(long start) {
    this(BigInteger.valueOf(start));
  }


  @Override
  public boolean hasNext() {
    return true;
  }

  @SuppressWarnings("IteratorNextCanNotThrowNoSuchElementException") // There's always a next element!
  public BigInteger next() {
    BigInteger nextCandidate = rows.first();

    rows.remove(nextCandidate);
    final BigInteger newNext = nextCandidate.multiply(BigInteger.TWO);
    rows.add(newNext);
    if (nextCandidate.equals(maxThreePower)) {
      maxThreePower = maxThreePower.multiply(THREE);
      rows.add(maxThreePower);
    }
    return nextCandidate;
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

    BigInteger limit = BigInteger.valueOf(Long.MAX_VALUE);
    System.out.println("Limit: " + limit);
    BigInteger nextThreeSmooth;
    int count = 0;
    long start = System.currentTimeMillis();
    BigThreeSmooth threeSmooth = new BigThreeSmooth();
    do {
      nextThreeSmooth = threeSmooth.next();
      System.out.printf("%3d: %d (%d rows)%n", ++count, nextThreeSmooth, threeSmooth.rows.size());
    } while (threeSmooth.hasNext() && (nextThreeSmooth.compareTo(limit) <= 0));
    long end = System.currentTimeMillis();
    System.out.println(format(end - start));

//    System.out.println("\n\nStarting with 243");
//    BigThreeSmooth nineHundred = new BigThreeSmooth(243L);
//    do {
//      nextThreeSmooth = nineHundred.next();
//      System.out.printf("%3d: %d%n", ++count, nextThreeSmooth);
//    } while (threeSmooth.hasNext() && (nextThreeSmooth.compareTo(maxLong) < 0));
  }
}


