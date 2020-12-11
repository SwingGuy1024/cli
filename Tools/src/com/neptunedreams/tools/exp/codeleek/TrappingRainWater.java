package com.neptunedreams.tools.exp.codeleek;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/17/20
 * <p>Time: 5:16 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class TrappingRainWater {

  public static void main(String[] args) {
    int[] map1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
    int[] map2 = {4, 2, 0, 3, 2, 5};
    
    int volume = new TrappingRainWater().trap(map2);
    System.out.println(volume);
    System.out.println(new TrappingRainWater().trap(map1));
  }
  
  // starts here
  public int trap(int[] height) {
    if (height.length == 0) {
      return 0;
    }
    int maxIndex = maxIndex(height);
    // Map from height to index:
    Comparator<Integer> descending = Comparator.<Integer>naturalOrder().reversed();
    Map<Integer, Integer> rightMap = new TreeMap<>(descending);
    for (int i = maxIndex + 1; i < height.length; ++i) {
      rightMap.put(height[i], i);
    }
    Map<Integer, Integer> leftMap = new TreeMap<>(descending);
    for (int i = maxIndex - 1; i >= 0; --i) {
      leftMap.put(height[i], i);
    }

    List<Segment> rightSegments = new LinkedList<>();
    List<Segment> leftSegments = new LinkedList<>();

    int damIndex = maxIndex;
    for (int h : rightMap.keySet()) {
      int index = rightMap.get(h);
      if (index > damIndex) {
        Segment segment = new Segment(h, damIndex, index);
        rightSegments.add(segment);
        damIndex = index;
      }
    }
    damIndex = maxIndex;
    for (int h : leftMap.keySet()) {
      int index = leftMap.get(h);
      if (index < damIndex) {
        leftSegments.add(new Segment(h, index, damIndex));
        damIndex = index;
      }
    }

    int volume = 0;
    for (Segment s : leftSegments) {
      volume += s.getVolume(height);
    }
    for (Segment s : rightSegments) {
      volume += s.getVolume(height);
    }
    return volume;
  }

  private static class Segment {
    private final int height; // smaller of two heights
    private final int startIndex;
    private final int endIndex;

    Segment(int h, int st, int e) {
      height = h;
      startIndex = st;
      endIndex = e;
    }

    int getVolume(int[] heightList) {
      int volume = 0;
      for (int i = startIndex+1; i < endIndex; ++i) {
        final int next = height - heightList[i];
        volume += next;
      }
      return volume;
    }
  }

  private int maxIndex(int[] a) {
    int max = a[0];
    int index = 0;
    for (int i = 1; i < a.length; i++) {
      if (a[i] > max) {
        max = a[i];
        index = i;
      }
    }
    return index;
  }
}
