package com.neptunedreams.tools.exp.codeleek;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/7/20
 * <p>Time: 5:49 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"StringConcatenation", "UseOfSystemOutOrSystemErr", "HardCodedStringLiteral"})
public class Parentheses {
  private static final char cl = ')';
  private static final char op = '(';

  public List<String> generateParenthesis(int n) {
    List<List<String>> resultsForN = new ArrayList<>();
    for (int i=0; i <= n; i++) {
      List<String> result = new ArrayList<>();
      if (i == 0) {
        result.add("");
      } else {
        for (int c = 0; c < i; ++c) {
          for (String left : resultsForN.get(c)) {
            for (String right : resultsForN.get(i - 1 - c)) {
              result.add(op + left + cl + right); // NON-NLS
            }
          }
        }
      }
      resultsForN.add(result);
    }
    return resultsForN.get(n);
  }

  public List<String> generateParenthesis1(int n) {
    List<String> result = new LinkedList<>();
    if (n == 0) {
      result.add("");
    } else {
      for (int c=0; c<n; ++c) {
        for (String left: generateParenthesis1(c)) {
          for (String right: generateParenthesis1(n - 1 - c)) {
            result.add(op + left + cl + right);
          }
        }
      }
    }
    return result;
  }

  public List<String> generateParenthesis2(int n) {
    State state = new State(new char[]{op}, 1, 1, 0);
    List<State> stateList = new LinkedList<>();
    process(stateList, state, n);
    List<String> results = new LinkedList<>();
    for (State s : stateList) {
      results.add(String.valueOf(s.head));
    }
    return results;
  }
  
  void process(List<State> results, State state, int n) {
    // List<State> newResults = new LinkedList<>();
    if (state.isGood(n)) {
      results.add(state);
    } else if ((state.cCount < n) && (state.balance > 0)){
      State newState = new State(append(state.head, cl), state.balance - 1, state.oCount, state.cCount + 1);
      if (newState.isGood(n)) {
        results.add(newState);
      } else {
        process(results, newState, n);
      }
    }
    if (state.oCount < n) {
      State newState = new State(append(state.head, op), state.balance + 1, state.oCount + 1, state.cCount);
      process(results, newState, n);
    }
  }

  char[] append(char[] old, char cc) {
    char[] newC = new char[old.length + 1];
    int i = 0;
    for (char c : old) {
      newC[i++] = c;
    }
    newC[i] = cc;
    return newC;
  }

  private static class State {
    char[] head;
    // int n;
    int balance;
    int oCount;
    int cCount;

    State(char[] head, int balance, int oCount, int cCount) {
      this.head = head;
      // this.n = n;
      this.balance = balance;
      this.oCount = oCount;
      this.cCount = cCount;
    }

    boolean isGood(int n) {
      return (balance == 0) && (oCount == n);
    }

    @Override
    public String toString() {
      return String.format("%s %d - %d = %d", new String(head), oCount, cCount, balance);
    }
  }

  public List<String> generateParenthesisBruteForce(int n) {
    List<String> results = new LinkedList<>();
    int digits = 2 * n;
    int range = 1 << (digits);
    // 1 is open, 2 is closed
    for (int i = 1; i < range; i += 2) {
      int balance = 0;
      char[] data = new char[digits];
      int index = 0;
      for (int mask = 1; mask < range; mask = mask << 1) {
        // count 1s
        if ((i & mask) == 0) {
          balance--;
          if (balance < 0) {
            index++;
            break;
          }
          data[index++] = ')';
        } else {
          balance++;
          data[index++] = '(';
        }
      }
      if (balance == 0) {
        results.add(new String(data));
      }
    }
    return results;
  }

  public static void main(String[] args) {
    Parentheses p = new Parentheses();
    Format fmt = NumberFormat.getInstance();
    for (int t=0; t<30; ++t) {
      List<Result> results = new LinkedList<>();
      for (int i = 0; i < 15; ++i) {
        long start = System.nanoTime();
        List<String> result = p.generateParenthesis(i);
        long mark = System.nanoTime();
        List<String> result2 = p.generateParenthesis1(i);
        long end = System.nanoTime();
//        System.out.println(i);
        final long fast = mark - start;
        System.out.printf("For %2d, t = %d ms -- %d results%n", i, fast, result.size()); // NON-NLS
        final long slow = end - mark;
        System.out.printf("For %2d, t = %d ms -- %d results%n%n", i, slow, result2.size()); // NON-NLS
//        System.out.printf("3^n = %f    4^n = %f%n", Math.pow(3, i), Math.pow(4, i)); // NON-NLS
//        System.out.println();
        results.add(new Result(i, slow, fast, result.size()));
//      for (String s : result) {
//        System.out.println(s);
//      }
      }
      long cumulative = 0;
      int n=0;
      for (Result r: results) {
        cumulative += r.size * 2 * n++;
        System.out.printf("%2d     %14s ns = %7s ms        %14s ns = %7s ms  %9s  %12s%n",
            r.n,
            fmt.format(r.nsSlow),
            fmt.format((r.nsSlow + 500000) / 1000000),
            fmt.format(r.nsFast),
            fmt.format((r.nsFast + 500000) / 1000000),
            fmt.format(r.size),
            fmt.format(cumulative)
        ); // NON-NLS
      }
      System.out.println();
      cumulative = 0;
      Iterator<Result> itr = results.iterator();
      itr.next();
      while (itr.hasNext()) {
        Result r = itr.next();
        cumulative += r.size * 2 * r.n;
        System.out.printf("%d\t%d\t%d\t%d\t%d%n", r.n, r.nsSlow, r.nsFast, r.size, cumulative); // NON-NLS
      }
      System.out.println("\n\n");
    }
  }
  
  private static class Result {
    final int n;
    final long nsSlow;
    final long nsFast;
    final int size;
    
    Result(int n, long nsSlow, long nsFast, int size) {
      this.n = n;
      this.nsSlow = nsSlow;
      this.nsFast = nsFast;
      this.size = size;
    }
  }
}

// 3: ["((()))","(()())","(())()","()(())","()()()"]

/*
Results:
 0                273 ns =       0 ms                   273 ns =       0 ms        1
 1                176 ns =       0 ms                   176 ns =       0 ms        1
 2                712 ns =       0 ms                   712 ns =       0 ms        2
 3              1,123 ns =       0 ms                 1,123 ns =       0 ms        5
 4              3,015 ns =       0 ms                 3,015 ns =       0 ms       14
 5              7,797 ns =       0 ms                 7,797 ns =       0 ms       42
 6             52,175 ns =       0 ms                52,175 ns =       0 ms      132
 7             73,572 ns =       0 ms                73,572 ns =       0 ms      429
 8            256,881 ns =       0 ms               256,881 ns =       0 ms     1430
 9            928,491 ns =       0 ms               928,491 ns =       0 ms     4862
10          3,458,750 ns =       3 ms             3,458,750 ns =       3 ms    16796
11         12,754,969 ns =      12 ms            12,754,969 ns =      12 ms    58786
12         49,987,495 ns =      49 ms            49,987,495 ns =      49 ms   208012
13        876,297,813 ns =     876 ms           876,297,813 ns =     876 ms   742900
14      1,428,112,152 ns =   1,428 ms         1,428,112,152 ns =   1,428 ms  2674440
15      8,260,554,147 ns =   8,260 ms         8,260,554,147 ns =   8,260 ms  9694845

 */