package com.neptunedreams.tools.cli;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Add this to your ~/.bash_profile
 * alias sysProps="java -cp ~/Tools/out/production/Tools SysProps"
 */
@SuppressWarnings({"HardCodedStringLiteral", "UseOfSystemOutOrSystemErr", "HardcodedFileSeparator", "HardcodedLineSeparator", "MagicNumber"})
public enum SysProps {
  ;

  private static final Map<Character, String> specialChars = makeSpecialCharacters();

  public static void main(String[] args) {
//    specialChars.forEach((c, s) -> System.out.printf("%s = %s%n", s, String.format("\\u%04x", (int) c)));

    Properties props = System.getProperties();
    Map<String, String> propMap = new TreeMap<>();
    int maxLength = 0;
    for (String key : props.stringPropertyNames()) {
      propMap.put(key, props.getProperty(key));
      maxLength = Math.max(maxLength, key.length());
    }
    String format = String.format("%%-%ds = %s", maxLength, "%s%n"); //  Gives "%-29s = %s%n"
//        String pathFormat = String.format("%c%ds%s", '%', maxLength, " %s%n"); // Gives "%29s %s%n"
    for (Map.Entry<String, String> stringStringEntry : propMap.entrySet()) {
      String value = asPath(stringStringEntry.getValue(), maxLength);
      System.out.printf(format, stringStringEntry.getKey(), value);
    }
  }
  
  
  private static Map<Character, String> makeSpecialCharacters() {
    Map<Character, String> map = new HashMap<>();
    map.put('\n', "\\n");
    map.put('\r', "\\r");
    map.put('\f', "\\f");
    map.put('\t', "\\t");
    return map;
  }

  private static String asPath(String text, int indent) {
    if (text.length() < 5) {
      return escapedPath(text);
    }
    if (text.contains("://")) {
      return text; // text is a URL, not a path.
    }
    char separator = File.pathSeparatorChar;
    int sepCount = count(text, separator);
    if (sepCount > 0) {
      char fileSeparator = File.separatorChar;
      // If there are more file separators (/) than path separators (: or ;) then it's a list of paths.
      if (count(text, fileSeparator) > sepCount) {
        return pathString(text, indent);
      }
    }
    return text;
  }

  /**
   * Escape the non-printing characters. We only bother with this for short Strings. As of this writing, there's
   * really only one property that needs this: line.separator.
   *
   * @param s The string to escape
   * @return The escaped String, with characters less than the space character or bigger than 127 displayed
   * using backslash-u notation.
   */
  private static String escapedPath(String s) {
    StringBuilder builder = new StringBuilder();
    for (int ii = 0; ii < s.length(); ++ii) {
      char c = s.charAt(ii);
      if (specialChars.containsKey(c)) {
        builder
            .append(specialChars.get(c))
            .append(" = ");
      }
      if ((c < 0x20) || (c > 0xFF)) {
        String fmt = String.format("%04x", (int) c);
        builder.append("\\u").append(fmt);
      } else {
        builder.append(c);
      }
    }
    return builder.toString();
  }

  /**
   * Counts how many times char c appears in String s
   *
   * @param s The String
   * @param c The character to count
   * @return The number of times c appears in s
   */
  private static int count(String s, char c) {
    int count = 0;
    int index = 0;
    index = s.indexOf(c, index);
    while (index >= 0) {
      count++;
      index = s.indexOf(c, index + 1);
    }
    return count;
  }

  /**
   * If s is a path (if it has at least one path separator, and has more file separators than path separators),
   * returns a formatted path, which is a path where separators are followed by line breaks and indentation.
   *
   * @param s A String that has been determined to be a path
   * @return a formatted path derived from s
   */
  private static String pathString(String s, int indent) {
    String format = String.format("%n%%%ds", indent + 3); // Gives "\n%29s"
    String lead = String.format(format, " ");
    StringBuilder builder = new StringBuilder(s);
    int tail = s.length();
    while (--tail >= 0) {
      if (builder.charAt(tail) == File.pathSeparatorChar) {
        builder.insert(tail + 1, lead);
      }
    }
    return builder.toString();
  }
}
