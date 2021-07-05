package com.neptunedreams.tools.exp.codeleek;

//import com.neptunedreams.tools.exp.SandBox;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/18/20
 * <p>Time: 4:26 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class AddTwoNumbers {
  public static void main(String[] args) {
    ListNode n243 = makeNode(3, 4, 2);
    ListNode n465 = makeNode(5, 6, 4);
    ListNode n708 = makeNode(8, 0, 7);
    ListNode n1024 = makeNode(1, 0, 2, 4);
    ListNode n32768 = makeNode(3, 2, 7, 6, 8);
    ListNode n9999999 = makeNode(9, 9, 9, 9, 9, 9, 9);
    ListNode n9999 = makeNode(9, 9, 9, 9);

    add(n243, n465);
    add(n243, n708);
    add(n708, n1024);
    add(n32768, n708);
    add(n32768, n243);
    add(n9999, n9999999);

  }

  private static void add(ListNode a, ListNode b) {
    ListNode s1 = Solution.addTwoNumbers(a, b);
    ListNode s2 = Solution.addTwoNumbers(b, a);
    System.out.println(a);
    System.out.println(b);
    System.out.println(s1);
    System.out.println(s2);
    System.out.println();
  }

  private static ListNode makeNode(int... ints) {
//    ListNode list = new ListNode();
//    ListNode next = list;
    ListNode last = null;
    for (int i : ints) {
      ListNode next = new ListNode(i, last);
      last = next;
    }
    System.out.println("Node: " + last.toString()); // NON-NLS
    return last;
  }

  @SuppressWarnings("MagicCharacter")
  public static class ListNode {
    private int val;
    private ListNode next;

    ListNode() {}

    ListNode(int val) { this.val = val; }

    ListNode(int val, ListNode next) {
      this.val = val;
      this.next = next;
    }

    @Override
    public String toString() {
      ListNode temp = this;
      StringBuilder builder = new StringBuilder("[");
      do {
        builder.append(temp.val);
        temp = temp.next;
        if (temp != null) {
          builder.append(',');
        }
      } while (temp != null);
      builder.append(']');
      return builder.toString();
    }
  }


  /**
   * Definition for singly-linked list.
   * public class ListNode {
   * int val;
   * ListNode next;
   * ListNode() {}
   * ListNode(int val) { this.val = val; }
   * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
   * }
   */
  enum Solution {
    ;

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
      ListNode result = new ListNode();
      boolean carry = false;
      ListNode lastResultDigit = result;
      while ((l1 != null) || (l2 != null) || carry) {
        int n1 = nextInt(l1);
        int n2 = nextInt(l2);
        int sum = n1 + n2;
        if (carry) {
          sum++;
        }
        carry = sum > 9;
        if (carry) {
          sum -= 10;
        }
        ListNode nextDigit = new ListNode(sum);
        lastResultDigit.next = nextDigit;
        lastResultDigit = nextDigit;
        l1 = nextList(l1);
        l2 = nextList(l2);
      }
      return result.next;
    }

    private static int nextInt(ListNode node) {
      if (node == null) {
        return 0;
      }
      return node.val;
    }

    private static ListNode nextList(ListNode node) {
      if (node == null) {
        return null;
      }
      return node.next;
    }
  }
}
