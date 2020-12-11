package com.neptunedreams.tools.exp.codeleek;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/*
You have an inventory of different colored balls, and there is a customer that wants orders balls of any color.

The customer weirdly values the colored balls. Each colored ball's value is the number of balls of that color you currently have in your inventory. For example, if you own 6 yellow balls, the customer would pay 6 for the first yellow ball. After the transaction, there are only 5 yellow balls left, so the next yellow ball is then valued at 5 (i.e., the value of the balls decreases as you sell more to the customer).

You are given an integer array, inventory, where inventory[i] represents the number of balls of the ith color that you initially own. You are also given an integer orders, which represents the total number of balls that the customer wants. You can sell the balls in any order.

Return the maximum total value that you can attain after selling orders colored balls. As the answer may be too large, return it modulo 109 + 7.

This one, my solution took to long. I never submitted a fast enough solution.
 */

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/7/20
 * <p>Time: 8:09 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public class ContestC {
  BigInteger mod = BigInteger.valueOf(1000000007);

  public int maxProfit(int[] inventory, int orders) {
    Arrays.sort(inventory);
    BigInteger totalSale = BigInteger.ZERO;
    int salePriceIndex = inventory.length - 1;
    int salePrice = inventory[salePriceIndex];
    int count = 0;
    while (true) {
      int countForPrice = 0;
      int remaining = orders - count;
      remaining = Math.min(remaining, inventory.length);
      int last = inventory.length - remaining;
      for (int i = inventory.length - 1; (i >= last) && (inventory[i] >= salePrice); --i) {
        countForPrice++;
      }
      totalSale = totalSale.add(BigInteger.valueOf(countForPrice*salePrice));
//      System.out.println(totalSale);
      int newIndex = Math.max(0, salePriceIndex-1);
      if (inventory[newIndex] == (salePrice - 1)) {
        salePriceIndex = newIndex;
        salePrice = inventory[salePriceIndex];
      } else {
        salePrice--;
      }
      count += countForPrice;
      if (count >= orders) {
        break;
      }
    }
    return totalSale.mod(mod).intValue();
  }
  
  private int tNumber(int min, int max) {
    // if max-min is odd, rows will be even. If max-min is even, max+min will be even.
    int rows = (max - min) + 1;
    return ((min + max) * rows) / 2; // either way, the product will be even.
  }

  public int maxProfit1(int[] inventory, int orders) {
    Arrays.sort(inventory);
    BigInteger totalSale = BigInteger.ZERO;
    int salePrice = inventory[inventory.length - 1];
    int count = 0;
    outer:
    while (true) {
      for (int i = inventory.length - 1; (i >= 0) && (inventory[i] == salePrice); --i) {
        count++;
        totalSale = totalSale.add(BigInteger.valueOf(salePrice));
        inventory[i]--;
        if (count == orders) {
          break outer;
        }
      }
      salePrice = inventory[inventory.length - 1];
    }
    return totalSale.mod(mod).intValue();
  }

  public static void main(String[] args) {
    ContestC contestC = new ContestC();
    int sale = contestC.maxProfit(new int[] { 3, 5} , 6);
    System.out.println(sale);
  }
}
