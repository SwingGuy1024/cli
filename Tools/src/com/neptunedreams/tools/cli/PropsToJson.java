package com.neptunedreams.tools.cli;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * todo: modify for prop categories: a.b.c, a.b.d
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/30/22
 * <p>Time: 11:46 PM
 *
 * @author Miguel Mu–oz
 */
public enum PropsToJson {
  ;
  private static final String userDir = System.getProperty("user.dir");

  public static void main(String[] args) throws IOException {
    Properties props = loadFromFile(args[0]);
    String json = toJson(props);
    System.out.println(format(json));
  }
  
  public static Properties loadFromFile(String name) throws IOException {
    File path = new File(userDir, name);
    @SuppressWarnings("TooBroadScope")
    Properties properties = new Properties();
    try (FileReader reader = new FileReader(path)) {
      properties.load(reader);
      return properties;
    }
  }
  
  public static String toJson(Properties props) {
    StringBuilder builder = new StringBuilder();
    builder.append('{');
    Set<String> propNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    propNames.addAll(props.stringPropertyNames());
    Iterator<String> itr = propNames.iterator();
    appendValue(props, builder, itr.next());
    while (itr.hasNext()) {
      builder.append(',');
      appendValue(props, builder, itr.next());
    }
    builder.append('}');
    return builder.toString();
  }
  
  private static void appendValue(Properties props, StringBuilder builder, String key) {
    String value = props.getProperty(key);
    try {
      long iValue = Long.parseLong(value);
      appendInteger(builder, key, iValue);
    } catch (NumberFormatException nfe) {
      appendString(builder, key, value);
    }
  }

  private static void appendString(StringBuilder builder, String key, String value) {
    builder
        .append('"')
        .append(key)
        .append("\":\"")
        .append(value)
        .append('"');
  }

  private static void appendInteger(StringBuilder builder, String key, long value) {
    builder
        .append('"')
        .append(key)
        .append("\":")
        .append(value);
  }

  // Primitive JSON Formatter. This doesn't handle special cases, but for
  // most JwtTokens, it will work fine.
  private static String format(String json) {
    //noinspection NonConstantStringShouldBeStringBuffer
    String indent = "";
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < json.length(); ++i) {
      char c = json.charAt(i);
      if ((c == '}') || (c == ']')) {
        indent = indent.substring(2);
        newLine(builder, indent);
      }
      builder.append(c);
      if ((c == '{') || (c == '[')) {
        indent += "  ";
        newLine(builder, indent);
      } else if (c == ',') {
        newLine(builder, indent);
      }
    }
    return builder.toString();
  }

  private static void newLine(StringBuilder builder, String indent) {
    builder.append('\n')
        .append(indent);
  }
}
