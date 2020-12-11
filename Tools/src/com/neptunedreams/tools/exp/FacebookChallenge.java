package com.neptunedreams.tools.exp;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/17/20
 * <p>Time: 1:47 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "HardCodedStringLiteral", "StringConcatenation"})
public class FacebookChallenge {
// Welcome to Facebook!

/*
  Question:
  Given a
  string S
  consisting of
  lowercase English
  characters determine if
  you can
  make it
  a palindrome
  by removing
  at most 1character.
      Examples:
  tacocats -->True  #tacocats -->tacocat
  racercar -->True  #racercar -->racecar,racrcar
  abcd -->False
*/

// "ab-----ab" "abccba"

  public boolean isCandidate(String s) {
    if (s.length() <= 2) {
      return true;
    }
    char[] data = s.toCharArray();
    int head = 0;
    int tail = data.length - 1;
    while (head < tail) {
      if (data[head] != data[tail]) {
        if (isPalindrome(data, head, tail - 1)) {
          return true;
        }
        return isPalindrome(data, head + 1, tail);
      }
      head++;
      tail--;
    }
    return true;
  }

  private boolean isPalindrome(char[] data, int head, int tail) {
    while (head < tail) {
      if (data[head++] != data[tail--]) {
        return false;
      }
    }
    return true;
  }


/*
  Q: Same, but
  removing at
  most N
  characters.
*/

  public boolean isCandidate(String s, int maxRemoved) {
    isCandidate:
    while (true) {
      int head = 0;
      int tail = s.length() - 1;
      if (maxRemoved == 0) {
        return isPalindrome(s.toCharArray(), head, tail);
      }

      if (s.length() <= (maxRemoved + 1)) {
        return true;
      }

      while (head < tail) {
        if (s.charAt(head) != s.charAt(tail)) {
          if (isCandidate(s.substring(head, tail), maxRemoved - 1)) {
            return true;
          }
          maxRemoved--;
          s = s.substring(head + 1, tail + 1);
          continue isCandidate;
        }
        head++;
        tail--;
      }
      return true;
    }
  }

  public static void main(String[] args) {
    test1("");
    test1("a");
    test1("ab");
    test1f("abc");
    test1("aba");
    test1("abca");
    test1f("abcab");
    test1("abcca");
    test1f("abcda");
    test1("racercar");
    test1f("racercars");

    testN("", 2, 0);
    testN("a", 2, 0);
    testN("ab", 2, 1);
    testN("abc", 3, 2);
    testN("abcd", 4, 3);
    testN("abcda", 6, 2);
    testN("racercars", 5, 2);
//    testN("", 2, 0);
//    testN("", 2, 0);
  }
  
  private static void testN(String s, int maxTest, int maxOK) {
    FacebookChallenge fc = new FacebookChallenge();
    for (int n=maxTest; n>= maxOK; --n) {
      if (!fc.isCandidate(s, n)) {
        System.out.println("* Fail at " + n + ", " + s);
      }
    }
    if (maxOK > 0) {
      if (fc.isCandidate(s, maxOK-1)) {
        System.out.println("* Fail at Max " + maxOK + ": " + s);
      }
    }
    System.out.println(s + ", " + maxTest + ", " + maxOK + " ok");
  }
  
  private static void test1(String s) {
    if (!new FacebookChallenge().isCandidate(s)) {
      System.out.println("* Fail: " + s);
    }
    System.out.println(s + " ok");
  }


  private static void test1f(String s) {
    if (new FacebookChallenge().isCandidate(s)) {
      System.out.println("* Fail: " + s);
    }
    System.out.println(s + " ok");
  }
}
