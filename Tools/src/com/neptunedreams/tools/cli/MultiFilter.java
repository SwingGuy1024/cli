// use: decrypt
package com.neptunedreams.tools.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Class for solving cryptograms. This uses multiple data files extracted from the enable1 word list.
 * It's designed to work from any directory.</p>
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 4/20/24</p>
 * <p>Time: 5:01&#x202F;PM</p>
 * <p>@author Miguel Mu&ntilde;oz</p>
 */
public final class MultiFilter {
  private static final Map<Integer, Set<String>> lengthMap = new HashMap<>();

  // The longest word in the original dictionary is 28 letters.
  private static final int MAX_WORD_LENGTH = 28;

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.out.printf("Usage: MultiFilter <word> [<word> ...]%n"); // NON-NLS
      return;
    }
    MultiFilter multiFilter = new MultiFilter();
    multiFilter.matchList(args);
  }

  // Q WSDVML NTQN KMSV LMN YQOFS UMFD NEPS CEOO LMN YQOFS UMFD QKYEXS
  // "QKYEXS", "WSDVML", "YQOFS"
  private MultiFilter() throws IOException {
    for (int i = 1; i <= MAX_WORD_LENGTH; ++i) {
      final String filePath = String.format("/words/words_%d.txt", i);
      Path path = getPath(filePath);
      try (Stream<String> stream = Files.lines(path)) {
        Set<String> set = stream.collect(Collectors.toCollection(TreeSet::new));
        lengthMap.put(i, set);
      }
    }
  }

  private void matchList(String... allWords) {
    matchList(System.out::println, allWords);
  }

  private void matchList(Consumer<List<String>> consumer, String... cipherWords) {
    String cipherWord = cipherWords[0];
    String[] remainingCiphers = Arrays.copyOfRange(cipherWords, 1, cipherWords.length);
//    System.out.printf("%nSearching for:%n%s, %s%n", cipherWord, Arrays.toString(remainingCiphers)); // NON-NLS
    System.out.printf("Searching for:%n%s -- %n", Arrays.toString(cipherWords)); // NON-NLS
    String[] lowRemainers = new String[remainingCiphers.length];
    int i = 0;
    for (String remainer : remainingCiphers) {
      lowRemainers[i++] = remainer.toLowerCase();
    }
    Map<Character, Character> cipher = new HashMap<>();
    List<String> pastWords = new ArrayList<>();
    long start = System.currentTimeMillis();
    int count = match(consumer, cipher, pastWords, cipherWord.toLowerCase(), lowRemainers);
    long time = System.currentTimeMillis() - start;
    double millis = (time / 1000.0);
    String clock = new DecimalFormat().format(millis);
    System.out.printf("%d matches in %s ms%n", count, clock); // NON-NLS
    
  }

  private static Set<String> wordsForSize(String cipherWord) {
    int length = cipherWord.length();
    return lengthMap.get(length);
  }

  private int match(
      Consumer<List<String>> consumer,
      Map<Character, Character> cipherKey,
      List<String> pastWords,
      String cipherWord,
      String... futureWords
  ) {
    Set<String> clearWords = wordsForSize(cipherWord);

    int count = 0;
    Set<Character> newKeyChars = new HashSet<>();
    for (String clearWord : clearWords) {
      char[] clearLetters = clearWord.toCharArray();
      int index = 0;
      boolean match = true;
      for (char c : cipherWord.toCharArray()) {
        final char clearChar = clearLetters[index++];
        if (cipherKey.containsKey(clearChar) || cipherKey.containsValue(c)) {
          final Character ch = cipherKey.get(clearChar);
          if ((ch == null) || (ch != c)) {
            match = false;
            break;
          }
        } else {
          cipherKey.put(clearChar, c);
          newKeyChars.add(clearChar);
        }
      }
      if (match) {
        List<String> allWords = new ArrayList<>(pastWords.size() + 1);
        allWords.addAll(pastWords);
        if (futureWords.length == 0) {
          allWords.add(clearWord);
          consumer.accept(allWords); // NON-NLS
          count++;
        } else {
          allWords.add(clearWord);
          final String[] nextFutureWords = Arrays.copyOfRange(futureWords, 1, futureWords.length);
          count += match(consumer, cipherKey, allWords, futureWords[0], nextFutureWords); // recurse
        }
      }
      // Remove all new characters from cipherKey that were added by this clearWord.
      newKeyChars.forEach(cipherKey::remove);
      newKeyChars.clear();
    }
    return count;
  }

  private Path getPath(String path) {
    String fPath = System.getProperty("jdk.launcher.sourcefile");
    int lastSlash = fPath.lastIndexOf('/');
    String fDir = fPath.substring(0, lastSlash);
    return Path.of(fDir + path);
  }
}
