package com.neptunedreams.tools.exp.examity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/21/20
 * <p>Time: 3:02 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public class Exam {
  public static String reverse(String s) {
    StringBuilder builder = new StringBuilder();
    boolean even = false;
    for (int i = s.length() - 1; i >= 0; --i) {
      char c = s.charAt(i);
      if (even) {
        c = Character.toLowerCase(c);
      } else {
        c = Character.toUpperCase(c);
      }
      builder.append(c);
      even = !even;
    }
    return builder.toString();
  }

  public static void defineArray() {
    String[] array = {"Alpha", "Bravo", "Charlie", "Delta"};
    int length = 0;
    for (String s : array) {
      length += s.length();
    }
    System.out.println("Total Length: " + length);
  }

  public void defineHashMap() {
    Map<Character, String> map = new HashMap<>();
    String[] words = {"Alpha", "Bravo", "Charlie", "Delta"};
    for (String s : words) {
      map.put(s.charAt(0), s);
    }

    System.out.println("Radio Code Words");
    for (Character c : map.keySet()) {
      System.out.printf("%c: %s%n", c, map.get(c));
    }
  }

  public static void main(String[] args) {
    System.out.println("---");
    Random random = new Random(5);
    double k = Math.log(60.0);
    List<PhoneCall> phoneCall = new ArrayList<>();
//    ZonedDateTime now = ZonedDateTime.now();
    LocalDateTime localDateTime = LocalDateTime.of(2020, 2, 2, 12, 0);
    ZonedDateTime now = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
    
    int rangeInMinutes = 120;
    for (int i=0; i<20; ++i) {
      
      ZonedDateTime start = now.plusMinutes(random.nextInt(rangeInMinutes/8));
      final int seconds = (int)(30*(Math.exp(random.nextDouble()*k) + 0.5));
      ZonedDateTime endTime = start.plusSeconds(seconds);
      phoneCall.add(new PhoneCall(start, endTime));
    }
    int max;
    
//    max = new Exam().maxOverlapOrderN(phoneCall);
//    System.out.println(max);
    max = new Exam().betterMaxOverlap(phoneCall);
    System.out.println(max);
    
    max = new Exam().trueOverlapMax(phoneCall);
    System.out.println(max);
    
    
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("+HH:mm");
////    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_TIME;
//    System.out.println(formatter);
////    formatter = DateTimeFormatterBuilder.
//    for (PhoneCall pc: phoneCall) {
//      System.out.printf("%6s -- %6s%n", formatter.format(pc.getStartTime()), formatter.format(pc.getEndTime()));
//    };
  }
  
  /*
  Going back to the PhoneCalls table in Fig. 2, write an algorithm (or function) for determining the maximum number of concurrent calls 
  that took place between a given start and end date. In other words, over some given period of time, some calls overlap and some don't. 
  What is the maximun number of overlapping calls?
  */

  static DateTimeFormatter ff = DateTimeFormatter.ofPattern("HH:mm");
  static AtomicInteger pcId = new AtomicInteger(0);

  static class PhoneCall {
    int id = pcId.incrementAndGet();
    String phoneCall;
    ZonedDateTime startTime;
    ZonedDateTime endTime;

    public PhoneCall(final ZonedDateTime startTime, final ZonedDateTime endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
    }

    public ZonedDateTime getStartTime() {
      return startTime;
    }

    public ZonedDateTime getEndTime() {
      return endTime;
    }

    public int getId() {
      return id;
    }

    @Override
    public boolean equals(final Object o) {

      return this == o;
//      if (!(o instanceof PhoneCall)) { return false; } // implicitly checks for null
    }

    @Override
    public int hashCode() {
      int result = startTime.hashCode();
      result = 31 * result + endTime.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return String.format("%5s -- %5s [%d]", ff.format(getStartTime()), ff.format(getEndTime()), id);
    }
  }

  public int maximumOverlap(List<PhoneCall> calls) {
    // Insert code to ensure list uses RandomAccess
    int max = 0;
    sortByStartTime(calls);

    for (int outer = 0; outer < calls.size(); ++outer) {
      PhoneCall left = calls.get(outer);
      int count = 0;
      for (int inner = outer+1; inner < calls.size(); ++inner) {
        PhoneCall right = calls.get(inner);
        if (overlap(left, right)) {
          count++;
        } else if (left.getStartTime().isAfter(right.getEndTime())) {
          break;
        }
      }
      for (int inner = outer - 1; inner >= 0; --inner) {
        PhoneCall right = calls.get(inner);
        if (overlap(left, right)) {
          count++;
        } else if(left.getEndTime().isBefore(right.getStartTime())) {
          break;
        }
      }
      max = Math.max(max, count);
    }
    return max;
  }
  
  public int maxOverlapOrderN(List<PhoneCall> calls) {
    sortByStartTime(calls);
    Map<PhoneCall, Integer> map = new TreeMap<>(Comparator.comparing(PhoneCall::getStartTime).thenComparing(PhoneCall::getId));
    for (int outer = 0; outer < calls.size(); ++outer) {
      PhoneCall left = calls.get(outer);
      for (int inner = outer+1; inner < calls.size(); ++inner) {
        PhoneCall right = calls.get(inner);
        if (overlap(left, right)) {
          insertOrIncrement(map, right);
          insertOrIncrement(map, left);
        } else if (right.getStartTime().isAfter(left.getEndTime())) {
          break;
        }
      }
    }
    final OptionalInt max = map.values().stream().mapToInt(Integer::intValue).max();
    
    DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");
    for (PhoneCall c: map.keySet()) {
      System.out.printf("%6s -- %6s  %d%n", f.format(c.getStartTime()), f.format(c.getEndTime()), map.get(c));
    }
    return max.orElse(0);
  }
  
  public int betterMaxOverlap(List<PhoneCall> calls) {
    final Comparator<PhoneCall> phoneCallComparator = Comparator.comparing(PhoneCall::getStartTime).thenComparing(PhoneCall::getId);
    calls.sort(phoneCallComparator);
    Map<PhoneCall, Integer> map = new HashMap<>();
    int max = 0;
    for (int outer=0; outer < calls.size(); ++outer) {
      PhoneCall thisOne = calls.get(outer);
      for (int inner = outer+1; inner<calls.size(); ++inner) {
        PhoneCall thatOne = calls.get(inner);
        if (thatOne.getStartTime().isBefore(thisOne.getEndTime())) {
          insertOrIncrement(map, thisOne);
          insertOrIncrement(map, thatOne);
        } else {
          break;
        }
      }
      final Integer count = map.get(thisOne);
      max = Math.max(count, max);
      System.out.printf("%8s -- %8s (%8s)  %d%n",
          ffs.format(thisOne.getStartTime()),
          ffs.format(thisOne.getEndTime()),
          Duration.between(thisOne.getStartTime(), thisOne.getEndTime()),
          count);
      map.remove(thisOne); // No longer needed in the map.
    }
//    final OptionalInt max = map.values().stream().mapToInt(Integer::intValue).max();

    return max;
//    return max.orElse(0);
  }
  
  private static boolean overlap(PhoneCall a, PhoneCall b) {
    return !a.getStartTime().isAfter(b.getEndTime()) && !b.getStartTime().isAfter(a.getEndTime());
  }
  
  
  
  private void insertOrIncrement(Map<PhoneCall, Integer> map, PhoneCall call) {
    if(map.containsKey(call)) {
      map.put(call, map.get(call) + 1);
    } else {
      map.put(call, 1);
    }
  }
  
  private void sortByStartTime(List<PhoneCall> c) {
    c.sort(Comparator.comparing(PhoneCall::getStartTime));
  }
  
  DateTimeFormatter ffs = DateTimeFormatter.ofPattern("HH:mm:ss");
  public int trueOverlapMax(List<PhoneCall> phoneCalls) {
    SortedSet<Event> eventSet = new TreeSet<>();
    for (PhoneCall phoneCall: phoneCalls) {
      eventSet.add(new Event(phoneCall.getStartTime(), EType.JOIN));
      eventSet.add(new Event(phoneCall.getEndTime(), EType.DROP));
    }
    int max = 0;
    int count = 0;
    for (Event e: eventSet) {
      if (e.type == EType.JOIN) {
        count ++;
        max = Math.max(count, max);
      } else {
        count--;
      }
      System.out.printf("%5s %s %3d%n", ffs.format(e.time), e.type, count);
    }
    return max;
  }
  
  
  AtomicInteger eventIdSource = new AtomicInteger(0);
  private Comparator<Event> comparator = Comparator.comparing((Event e) -> e.time).thenComparing((e) -> e.type).thenComparing(e -> e.id);
  public class Event implements Comparable<Event> {
    final int id = eventIdSource.incrementAndGet();
    final ZonedDateTime time;
    final EType type;
    
    Event(ZonedDateTime time, EType type) {
      this.time = time;
      this.type = type;
    }

    @Override
    public int compareTo(final Event o) {
      return comparator.compare(this, o);
    }
  }
  
  enum EType { JOIN, DROP }
}
