package com.neptunedreams.tools.exp.codeleek;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/30/20
 * <p>Time: 4:04 AM
 *
 * @author Miguel Mu\u00f1oz
 */
class Sum3 {
  public List<List<Integer>> threeSum(int[] nums) {
    if (nums.length < 3) {
      return new LinkedList<>();
    }
    // LinkedList<List<Integer>> result = new LinkedList<>();
    Arrays.sort(nums);
    Set<List<Integer>> result = new HashSet<>();
    // System.out.println();
    // System.out.println(Arrays.toString(nums));
    // System.out.printf("%d entries%n", nums.length);
    for (int i = 0; i < (nums.length - 2); i++) {
      int ii = nums[i];
      if ((i > 1) && (ii == nums[i - 2])) {
        continue;
      }
      int j = i + 1;
      int k = nums.length - 1;
      int jj = nums[j];
      int kk = nums[k];
      while (j < k) {
        int sum = ii + jj + kk;
        // System.out.printf("At (%d, %d, %d) testing %d, %d, %d = %d%n", i, j, k, ii, jj, kk, sum);
        if (sum == 0) {
          List<Integer> solution = Arrays.asList(ii, jj, kk);
          result.add(solution);
          j++;
          while ((jj == nums[j]) && (j < k)) {
            j++;
          }
          jj = nums[j];
        } else if (sum > 0) {
          k--;
          while ((kk == nums[k]) && (k > j)) {
            k--;
          }
          kk = nums[k];
        } else {
          j++;
          while ((jj == nums[j]) && (j < k)) {
            j++;
          }
          jj = nums[j];
        }
      }
    }
    return new LinkedList<>(result);
  }
}

// [-1,0,1,2,-1,-4]
// [-2,0,1,1,2]
// [-1,0,1,2,-1,-4,-2,-3,3,0,4]
// [1,1,1]
// [0,0,0]
// [0,0,0,0]
// [0,0,0,0,0,0,0,0,0,0]