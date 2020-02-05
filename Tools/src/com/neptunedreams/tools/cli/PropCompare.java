package com.neptunedreams.tools.cli;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Compares contents of two properties files, or of one properties file with those on the clipboard.
 */
public class PropCompare {
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
  }

  private static Properties getPropertiesFromClipboard() throws UnsupportedFlavorException, IOException {
    Properties cbProps = new Properties();
    String txt = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
    StringReader clipReader = new StringReader(txt);
    cbProps.load(clipReader);
    return cbProps;
  }

  private static Properties getPropertiesFromFile(String fileName) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(fileName));
    return props;
  }

  private static void showUsageAndExit() {
    System.err.println("Usage: java com.neptunedreams.tools.cli.PropCompare <fileName>");
    System.err.println("       java com.neptunedreams.tools.cli.PropCompare <leftName> <rightName>");
    System.exit(0);
  }
  
  private static void compare(Properties left, Properties right) {
    Map<String, String> leftMap = copyToMap(left);
    Map<String, String> rightMap = copyToMap(right);
    Set<String> propsMissingFromLeft = getMissing(leftMap, rightMap);
    Set<String> propsMissingFromRight = getMissing(rightMap, leftMap);
    System.out.printf("Properties missing from left: %d/%d %n", propsMissingFromLeft.size(), rightMap.size());
    showProperties(propsMissingFromLeft, rightMap);

    System.out.printf("%nProperties missing from right: %d/%d %n", propsMissingFromRight.size(), leftMap.size());
    showProperties(propsMissingFromRight, leftMap);
    
    Set<String> commonKeys = getCommonKeys(leftMap, rightMap);
    System.out.printf("%nChanged Values:%n");
    int count = 0;
    for (String key: commonKeys) {
      String leftValue = leftMap.get(key);
      String rightValue = rightMap.get(key);
      if (!leftValue.equals(rightValue)) {
        System.out.printf("%n%s%n", key);
        System.out.printf("<  =%s%n", leftValue);
        System.out.printf(">  =%s%n", rightValue);
        count++;
      }
    }
    System.out.printf("Total of %d mismatches%n", count);
  }
  
  private static Set<String> getCommonKeys(Map<String, String> leftMap, Map<String, String> rightMap) {
    Set<String> commonKeys = new TreeSet<>();
    for (String key: leftMap.keySet()) {
      if (rightMap.containsKey(key)) {
        commonKeys.add(key);
      }
    }
    return commonKeys;
  }
  
  private static void showProperties(Set<String> missing, Map<String, String> map) {
    for (String key: missing) {
      System.out.printf("%s=%s%n", key, map.get(key));
    }
  }
  
  private static Map<String, String> copyToMap(Properties props) {
    Map<String, String> map = new HashMap<>();
    for (String pName: props.stringPropertyNames()) {
      map.put(pName, props.getProperty(pName));
    }
    return map;
  }
  
  private static Set<String> getMissing(Map<String, String> from, Map<String, String> source) {
    Set<String> missingKeySet = new TreeSet<>();
    for (String key: source.keySet()) {
      if (!from.containsKey(key)) {
        missingKeySet.add(key);
      }
    }
    return missingKeySet;
  }
}
