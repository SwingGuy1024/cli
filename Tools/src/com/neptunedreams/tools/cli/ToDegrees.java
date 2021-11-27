package com.neptunedreams.tools.cli;

import java.text.NumberFormat;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/9/21
 * <p>Time: 10:14 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum ToDegrees {
  ;

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.printf(
          "Usage: ToHours -nnn¡nn\\\"nn.nnn' [-nnn¡nn\\\"nn.nnn']%n" +
          "       ToHours -nnn¡nn\\\"nn.nnn', [-nnn¡nn\\\"nn.nnn',...]%n" +
          "Note: Be sure to escape the minutes symbol"); // NON-NLS
      System.exit(0);
    }
    StringBuilder builder = new StringBuilder();
    for (String arg : args) {
      builder.append(toDegrees(arg))
          .append(", ");
    }
    int length = builder.length();
    builder.delete(length - 2, length); // strip off trailing ", "
    System.out.println(builder);
  }
  
  private static String toDegrees(String dms) {
    String signChar;
    if (dms.startsWith("-")) {
      signChar = "-";
      dms = dms.substring(1);
    } else {
      signChar = "";
    }
    int dSpot = dms.indexOf('\u00b0');
    int mSpot = dms.indexOf('\"');
    int sSpot = dms.indexOf('\'');
    int degrees = Integer.parseInt(dms.substring(0, dSpot));
    int minutes = Integer.parseInt(dms.substring(dSpot+1, mSpot));
    double seconds = Double.parseDouble(dms.substring(mSpot+1, sSpot));
    double decimalDegrees = degrees + (minutes / 60.0) + (seconds / 3600.0);
    
    return String.format("%s%7.6f", signChar, decimalDegrees);
  }
  
  // Test data: 35¡06\"38.635', -118¡40\"09.271', 35¡06\"38.146', -118¡40\"06.496', 35¡07\"01.808', -118¡40\"02.399', 35¡07\"00.991', -118¡39\"56.974' -8¡39\"56.974'
  // Should produce: 35.110732, -118.669242 35.110596 -118.668471 35.117169 -118.667333 35.116942 -118.665826
}
