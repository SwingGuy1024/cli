package com.neptunedreams.tools.exp.codeleek;

import java.util.Arrays;
import java.util.HashMap;

/*
You are given an integer n. An array nums of length n + 1 is generated in the following way:

nums[0] = 0
nums[1] = 1
nums[2 * i] = nums[i] when 2 <= 2 * i <= n
nums[2 * i + 1] = nums[i] + nums[i + 1] when 2 <= 2 * i + 1 <= n
Return the maximum integer in the array nums???.

Input: n = 7
Output: 3
Explanation: According to the given rules:
  nums[0] = 0
  nums[1] = 1
  nums[(1 * 2) = 2] = nums[1] = 1
  nums[(1 * 2) + 1 = 3] = nums[1] + nums[2] = 1 + 1 = 2
  nums[(2 * 2) = 4] = nums[2] = 1
  nums[(2 * 2) + 1 = 5] = nums[2] + nums[3] = 1 + 2 = 3
  nums[(3 * 2) = 6] = nums[3] = 2
  nums[(3 * 2) + 1 = 7] = nums[3] + nums[4] = 2 + 1 = 3
Hence, nums = [0,1,1,2,1,3,2,3], and the maximum is 3.
 */

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/7/20
 * <p>Time: 6:51 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public class ContestA {
  public int getMaximumGenerated(final int n) {
    if (n < 2) {
      return n;
    }

    int max = 1;
    int[] nums = new int[n + 1];
    nums[1] = 1;
    int i = 1;
    for (int x = 2; x < n; x+=2) {
      nums[x] = nums[i];
      int next = nums[i] + nums[i + 1];
      nums[x + 1] = next;
      i++;
      max = Math.max(max, next);
    }
    if (n%2 == 0) {
      nums[n] = nums[n/2];
      max = Math.max(max, nums[n/2]);
    }
    System.out.println(Arrays.toString(nums));
    for (int a=0; a<nums.length-1; a+= 2) {
      System.out.println(nums[a] + ", " + nums[a+1]);
    }
    HashMap<Character, Integer> iMap = new HashMap<>();
    return max;

  }

  public static void main(String[] args) {
    System.out.println();
    final int maximumGenerated = new ContestA().getMaximumGenerated(2);
    System.out.println(maximumGenerated);
  }
}
