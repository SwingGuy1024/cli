package com.neptunedreams.tools.cli;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 11/9/21
 * <p>Time: 8:27 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public enum ToDms {
  ;

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.printf(
          "Usage: ToDms n.nnn [n.nnn...]%n" +
          "       ToDms n.nnn, [n.nnn,...]%n"); // NON-NLS
      System.exit(0);
    }
    StringBuilder builder = new StringBuilder();
    for (String arg: args) {
      builder.append(toDms(arg))
          .append(", ");
    }
    int length = builder.length();
    builder.delete(length-2, length); // strip off trailing ", "
    System.out.println(builder);
  }
  
  private static String toDms(String pDecimal) {
    String decimal = trimComma(pDecimal);
    double degrees = Double.parseDouble(decimal);
    String sign = (degrees < 0.0) ? "-" : "";
    degrees = Math.abs(degrees);
    long d = (long) degrees;
    double minutes = (degrees - d) * 60;
    long m = (long) (minutes);
    double seconds = (minutes - m) * 60;
    return String.format("%s%d\u00b0%02d\"%06.3f'", sign, d, m, seconds);
  }
  
  private static String trimComma(String s) {
    if (s.endsWith(",")) {
      return s.substring(0, s.length()-1);
    }
    return s;
  }
}
