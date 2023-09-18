
package com.neptunedreams.tools.cli;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/6/23
 * <p>Time: 2:03 AM
 *
 * @author Miguel Mu–oz
 */
public enum ToLocal {
  ;

  public static void main(String[] args) {
    final ZoneId zone = ZoneId.systemDefault();
    System.out.printf("Now: %s%n", DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.ofInstant(Instant.now(), zone))); // NON-NLS
    for (String s: args) {
      process(s, zone);
    }
  }
  
  private static void process(String timeString, ZoneId zone) {
//    TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(timeString);
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    System.out.printf("Formatter: %s for %s%n", formatter, timeString); // NON-NLS
    final Instant now = Instant.now();
    System.out.printf("Instant: %s%n", now); // NON-NLS
    System.out.printf("Now: %s%n", formatter.format(now)); // NON-NLS
    TemporalAccessor temporalAccessor = formatter.parse(timeString);
    System.out.printf("TemporalEccessor: %s%n", temporalAccessor); // NON-NLS
    final ZonedDateTime from = ZonedDateTime.from(temporalAccessor);
    System.out.printf("ZonedDateTime:   %s%n", from); // NON-NLS
    ZonedDateTime z = from.withZoneSameLocal(zone);
    System.out.printf("ZonedDateTime 2: %s%n", z); // NON-NLS
    System.out.println(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(z));
  }
}
