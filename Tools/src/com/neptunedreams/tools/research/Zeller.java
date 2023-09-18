package com.neptunedreams.tools.research;

import java.util.function.IntFunction;

/**
 * <p>This is a class to test a claim made in the Wikipedia article about Zeller's Congruence. The article, from
 * 2023/9/17, has a section called Common Simplification, which I suspect is incorrect. Here are the formulas it
 * gives:<br><br>
 * <a href="https://wikimedia.org/api/rest_v1/media/math/render/svg/6aa6f22c7650128e4ec1bb7e0a768ec39d6913cc">Gregorian:</a></p>
 * 
 * <p><img src="./docs/Gregorian.png" width="473"><br><br>
 * <a href="https://wikimedia.org/api/rest_v1/media/math/render/svg/72deb30de7fcd1bfb5d9c9e6b1643e562ddc0d59">Julian:</a></p>
 * <p><img src="./docs/Julian.png" width="360"><br></p>
 * <p>This class was written to test these against working formulas.</a></p>
 * <p>The "Common Simplification" formulas worked perfectly.</p>
 *
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 9/17/23
 * <p>Time: 3:59 PM
 *
 * @author Miguel Mu–oz
 */
@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
public enum Zeller {
  ;
  
  private static final class GregorianDay {
    private final int day;
    private final int month;
    private final int year;

    private GregorianDay(int day, int month, int year) {
      this.day = day;
      this.month = month;
      this.year = year;
      if (((year % 100) == 0) && (month == 1) && (day == 1)) {
        System.out.println(year);
      }
    }

    private GregorianDay next() {
      int daysInMonth = daysInMonth();
      int nextDay = day + 1;
      if (nextDay <= daysInMonth) {
        return new GregorianDay(nextDay, month, year);
      }
      // new month
      if (month < 12) {
        return new GregorianDay(1, month + 1, year);
      }
      // new year
      return new GregorianDay(1, 1, year + 1);
    }

    public int daysInFebruary(int year) {
      return ((year % 4) > 0) ? 28 :
          (((year % 100) > 0) ? 29 :
              (((year % 400) > 0) ? 28 : 29));
    }

    public int daysInMonth() {
      switch (month) {
        case 2:
          return daysInFebruary(year);
        case 4:
        case 6:
        case 9:
        case 11:
          return 30;
        default:
          return 31;
      }
    }

    public int getWeekDayGregorian() {
        int theMonth = month;
        int theYear = year;
        
      if (theMonth < 3) {
        theMonth += 12;
        theYear -= 1;
      }
      int k = theYear % 100;
      int j = theYear / 100;
      return (day + ((13 * (theMonth + 1)) / 5) + k + (k / 4) + (j / 4) + (5 * j)) % 7;
    }
  
    public int getWeekDayGregSimplified() {
      int theMonth = month;
      int theYear = year;
      if (theMonth < 3) {
        theMonth += 12;
        theYear -= 1;
      }
      return (((day + ((13 * (theMonth + 1)) / 5) + theYear + (theYear / 4)) - (theYear / 100)) + (theYear / 400)) % 7;
    }

    @Override
    public String toString() {
      return String.format("%d/%d/%d", year, month, day);
    }
  }

  private static final class JulianDay {
    private final int day;
    private final int month;
    private final int year;

    private JulianDay(int day, int month, int year) {
      this.day = day;
      this.month = month;
      this.year = year;
      if (((year % 100) == 0) && (month == 1) && (day == 1)) {
        System.out.println(year);
      }
    }

    private JulianDay next() {
      int daysInMonth = daysInMonth();
      int nextDay = day + 1;
      if (nextDay <= daysInMonth) {
        return new JulianDay(nextDay, month, year);
      }
      // new month
      if (month < 12) {
        return new JulianDay(1, month + 1, year);
      }
      // new year
      return new JulianDay(1, 1, year + 1);
    }

    public int daysInFebruary() {
      return ((year % 4) > 0) ? 28 : 29;
    }

    public int daysInMonth() {
      switch (month) {
        case 2:
          return daysInFebruary();
        case 4:
        case 6:
        case 9:
        case 11:
          return 30;
        default:
          return 31;
      }
    }

    public int getWeekDayJulian() {
      int theMonth = month;
      int theYear = year;

      if (theMonth < 3) {
        theMonth += 12;
        theYear -= 1;
      }
      int k = theYear % 100;
      int j = theYear / 100;
      return (day + ((13 * (theMonth + 1)) / 5) + k + (k / 4) + 5 + (6 * j)) % 7;
    }

    public int getWeekDayJulianSimplified() {
      int theMonth = month;
      int theYear = year;
      if (theMonth < 3) {
        theMonth += 12;
        theYear -= 1;
      }
      return ((day + ((13 * (theMonth + 1)) / 5) + theYear + (theYear / 4)) + 5) % 7;
    }

    @Override
    public String toString() {
      return String.format("%d/%d/%d", year, month, day);
    }
  }

  public static void main(String[] args) {
    doGregorian();

    doJulian();

    doReplacements();
  }

  private static void doJulian() {
    System.out.println("Julian:");
    JulianDay today = new JulianDay(17, 9, 2023);
    int weekDay = today.getWeekDayJulian();
    int weekDay2 = today.getWeekDayJulianSimplified();
    System.out.println("Day is " + weekDay + " and " + weekDay2);
    
    JulianDay theDay = new JulianDay(1, 1, 1);
    int priorDay = 6;
    final int actual = (theDay.getWeekDayJulian() + 6) % 7;
    if (priorDay != actual) {
      System.out.println("Bad initial day. Expected 6, got " + actual);
      throw new IllegalStateException(String.valueOf(actual));
    }
    
    while (theDay.year < 2024) {
      int expected = (priorDay + 1) %7;
      int actualJulian = theDay.getWeekDayJulian();
      int actualJulianSimplified = theDay.getWeekDayJulianSimplified();
      if (expected != actualJulian) {
        System.out.printf("Julian Error on %s: %d != %d%n", theDay, expected, actualJulian);
        System.exit(1);
      }
      if (expected != actualJulianSimplified) {
        System.out.printf("Simplified Julian Error on %s: %d != %d%n", theDay, expected, actualJulianSimplified);
        System.exit(1);
      }
      priorDay = expected;
      theDay = theDay.next();
    }
    System.out.println("End on " + theDay);
  }

  private static void doGregorian() {
    System.out.println("Gregorian:");
    GregorianDay today = new GregorianDay(17, 9, 2023);
    int weekDay = today.getWeekDayGregorian();
    int weekDay2 = today.getWeekDayGregSimplified();
    System.out.println("Day is " + weekDay + " and " + weekDay2);

    GregorianDay theDay = new GregorianDay(1, 1, 1400);
    int priorDay = 3;
    int actual = (theDay.getWeekDayGregorian() + 6) % 7;
    if (priorDay != actual) {
      System.out.println("Bad initial day. Expected 6, got " + actual);
      throw new IllegalStateException(String.valueOf(actual));
    }

    while (theDay.year < 2024) {
      int expected = (priorDay + 1) % 7;
      int actualGregorian = theDay.getWeekDayGregorian();
      int actualGregSimplified = theDay.getWeekDayGregSimplified();
      if (expected != actualGregorian) {
        System.out.printf("Gregorian Error on %s: %d != %d%n", theDay, expected, actualGregorian); // NON-NLS
        System.exit(1);
      }
      if (expected != actualGregSimplified) {
        System.out.printf("Simplified Error on %s: %d != %d%n", theDay, expected, actualGregSimplified); // NON-NLS
        System.exit(1);
      }
      priorDay = expected;
      theDay = theDay.next();
    }
    System.out.println("end on " + theDay);
  }

  private static void doReplacements() {
    doReplacement("Michael Keith", Zeller::michaelKeithMonth);
    doReplacement("JR Stockton", Zeller::jRStocktonMonth);
    doReplacement("Claus T¿ndering", Zeller::clausTonderingMonth);
  }

  private static void doReplacement(String name, IntFunction<Integer> function) {
    System.out.println(name + ':');
    for (int month = 3; month <= 14; ++month) {
      int zeller = zellerMonth(month);
      int testMonth = function.apply(month);
      System.out.printf("month %d: delta = %d%n", month, ((7 + testMonth) - zeller) % 7); // NON-NLS
    }
  }

  private static int zellerMonth(int month) {
    return ((13 * (month + 1)) / 5) % 7;
  }

  private static int michaelKeithMonth(int month) {
    return (((23 * month) / 9) + 4) % 7;
  }

  private static int jRStocktonMonth(int month) {
    return (((13 * (month - 2)) / 5) + 2 + 1) % 7; // JR stockton uses +2 to produce a Sunday-is-zero version
  }

  private static int clausTonderingMonth(int month) {
    return (((31 * (month - 2)) / 12) + 1) % 7;
  }
}
