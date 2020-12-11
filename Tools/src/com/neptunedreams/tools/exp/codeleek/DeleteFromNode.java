package com.neptunedreams.tools.exp.codeleek;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/4/20
 * <p>Time: 2:54 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public class DeleteFromNode {
  public ListNode removeNthFromEnd(ListNode head, int n) {
    Deque<ListNode> track = new LinkedList<>();
    ListNode current = head;
    int trackSize = n + 1;
    while (current != null) {
      track.push(current);
      // store 1 more than n
      if (track.size() > trackSize) {
        track.removeLast();
      }
      current = current.next;
    }
    if (track.size() == n) {
      return head.next;
    }
    ListNode headOfTarget = track.removeLast();
    ListNode target = headOfTarget.next;
    ListNode afterTarget = target.next;
    headOfTarget.next = afterTarget;
    return head;
  }


  /**
   * Definition for singly-linked list.
   * public class ListNode {
   *     int val;
   *     ListNode next;
   *     ListNode() {}
   *     ListNode(int val) { this.val = val; }
   *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
   * }
   */
  public static class ListNode {
    int val;
    ListNode next;

    ListNode() {}

    ListNode(int val) { this.val = val; }

    ListNode(int val, ListNode next) {
      this.val = val;
      this.next = next;
    }
  }
}

//[1,2,3,4,5]
//2
//[1,2,3,4,5]
//1
//[1,2,3,4,5]
//5
//[1]
//1
