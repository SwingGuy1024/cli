package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Compares contents of two properties files, or of one properties file with those on the clipboard.
 */
public enum PropCompare {
  ;

  private static final char STAR = '*';
  private static final char SPACE = ' ';

  public static void main(String[] args) throws IOException, UnsupportedFlavorException {
    switch (args.length) {
      case 1:
        Properties fileProps = getPropertiesFromFile(args[0]);
        Properties cbProps = getPropertiesFromClipboard();
        compare(fileProps, cbProps);
      break;
      case 2:
        Properties leftProps = getPropertiesFromFile(args[0]);
        Properties rightProps = getPropertiesFromFile(args[1]);
        compare(leftProps, rightProps);
      break;
      default:
        showUsageAndExit();
    }
    System.exit(0); // Avoids bug where VM prints out diagnostics.
  }

  private static Properties getPropertiesFromClipboard() throws UnsupportedFlavorException, IOException {
    Properties cbProps = new Properties();
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    String txt = systemClipboard.getData(DataFlavor.stringFlavor).toString();
    cbProps.load(new StringReader(txt));
    return cbProps;
  }

  private static Properties getPropertiesFromFile(String fileName) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(fileName));
    return props;
  }

  private static void showUsageAndExit() {
    System.err.println("Usage: java PropCompare <fileName>");
    System.err.println("       java PropCompare <leftName> <rightName>");
    System.exit(0);
  }

  private static void compare(Properties left, Properties right) {
    Set<String> propsMissingFromLeft = getMissing(left, right);
    Set<String> propsMissingFromRight = getMissing(right, left);
    System.out.printf("Properties missing from left: %d/%d %n", propsMissingFromLeft.size(), right.size());
    showProperties(propsMissingFromLeft, right);

    System.out.printf("%nProperties missing from right: %d/%d %n", propsMissingFromRight.size(), left.size());
    showProperties(propsMissingFromRight, left);

    Set<String> commonKeys = getCommonKeys(left, right);
    System.out.printf("%nMismatches:%n");
    int count = 0;
    boolean trailingSpace = false;
    for (String key: commonKeys) {
      String leftValue = left.getProperty(key);
      String rightValue = right.getProperty(key);
      char leftTrailChar = trailingSpaceMark(leftValue);
      char rightTrailChar = trailingSpaceMark(rightValue);

      if (!leftValue.equals(rightValue)) {
        if (!trailingSpace && ((leftTrailChar == STAR) || (rightTrailChar == STAR))) {
          trailingSpace = true;
        }
        System.out.printf("<%c %s=%s%n", leftTrailChar, key, leftValue);
        System.out.printf(">%c %s=%s%n%n", rightTrailChar, key, rightValue);
        count++;
      }
    }
    System.out.printf("Total of %d mismatches%n", count);
    if (trailingSpace) {
      System.out.printf("%c Has one or more trailing spaces%n", STAR);
    }
  }

  private static char trailingSpaceMark(String s) {
    return s.equals(s.trim())? SPACE : STAR;
  }

  private static Set<String> getCommonKeys2(Properties leftMap, Properties rightMap) {
    Set<String> commonNames = new TreeSet<>();
    for (String name : leftMap.stringPropertyNames()) {
      if (rightMap.containsKey(name)) {
        commonNames.add(name);
      }
    }
    return commonNames;
  }

  private static Set<String> getCommonKeys(Properties leftMap, Properties rightMap) {    
    //noinspection UnnecessaryLocalVariable
    TreeSet<String> commonNames = leftMap.stringPropertyNames()
        .stream()
        .filter(rightMap::containsKey)
        .collect(Collectors.toCollection(TreeSet::new));
    return commonNames;
  }

  private static void showProperties(Set<String> missing, Properties map) {
    for (String key: missing) {
      System.out.printf("%s=%s%n", key, map.getProperty(key));
    }
  }

  private static Set<String> getMissing(Properties from, Properties source) {
    Set<String> missingKeySet = new TreeSet<>();
    for (String key: source.stringPropertyNames()) {
      if (!from.containsKey(key)) {
        missingKeySet.add(key);
      }
    }
    return missingKeySet;
  }
}
