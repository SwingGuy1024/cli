package com.neptunedreams.tools.cli;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * Convert a number of milliseconds into an time, based on the Java Epoch.
 *
 * Add this to your ~/.bash_profile
 * alias millis="java -cp ~/Tools/out/production/Tools com.neptunedreams.tools.cli.TimeConvert"
 *
 * Then type this: millis 1550785775435
 */
public final class TimeConvert {
    private TimeConvert() { }

    public static void main(String[] args) {
        if (args.length == 0) {
            long millis = System.currentTimeMillis();
            System.out.println("Converts milliseconds to time");
            System.out.println("Usage: millis <milliseconds> [<milliseconds> ...]");
            System.out.printf("  at %s%n  %d ms%n  %d seconds%n", getTime(millis), millis, millis/1000);

            return;
        }
        for (String s: args) {
            System.out.println(textToTime(s));
        }
    }

    private static String textToTime(String txt) {
        try {
            long timeMillis = Long.parseLong(txt);
            return getTime(timeMillis);
        } catch (NumberFormatException e) {
            return "Bad input. Time not in milliseconds: " + txt;
        }
    }

    private static String getTime(long timeMillis) {
        Instant instant = Instant.ofEpochMilli(timeMillis);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toString();
    }
}
