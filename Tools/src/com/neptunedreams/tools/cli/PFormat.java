package com.neptunedreams.tools.cli;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Prime factorization may be done at <a href="https://www.alpertron.com.ar/ECM.HTM">Integer Factorization Calculator</a></p>
 * <p>This will format the output of the prime factorization page to something more readable. To use this,
 * select the entire block of blue characters put out by the web page, and pass them to this as a single
 * parameter. (Put the whole string in quotes.)</p>
 * <p>This will convert this: </p>
 * <p>999001 = 19 × 52579</p>
 * <p>into this:</p>
 * <pre>
 *   BigFactor 999001
 *   19       (52579)
 *   52579
 * </pre>
 * <p>Sample Result:</p>
 * <pre>
 *   BigFactor 97436191823476198237469834598726482347813746198347561983475691834765983746918379518761987651984759183475916871938471951981981003108756137860834756018745681764857682782517198237461928374619283746129834761298374612983746129837461928374619283746192873
 * 3       (32478730607825399412489944866242160782604582066115853994491897278255327915639459839587329217328253061158638957312823983993993667702918712620278252006248560588285894260839066079153976124873094582043278253766124870994582043279153976124873094582064291)
 * 163741       (198354294940335037727202990492559351552785081721229588157467569382471878855262028689133016271601205935951526846133979785111814803274187360650528896282840342909142452170434198393523773061561213025713036159337764341213147857159501750477113823551)
 * 198354294940335037727202990492559351552785081721229588157467569382471878855262028689133016271601205935951526846133979785111814803274187360650528896282840342909142452170434198393523773061561213025713036159337764341213147857159501750477113823551       
 * </pre>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 8/15/23
 * <p>Time: 12:08 PM
 *
 * @author Miguel Muñoz
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public enum PFormat {
  ;

  public static final String[] EMPTY = new String[0];
  public static final char TIMES = '\u00D7'; // Multiplication cross

  public static void main(String[] args) {
    String input = String.join("", args);
    input = input.replace(" ", "");
    int eSpot = input.indexOf('=');
    String trim1 = input.substring(eSpot+1);
    int pSpot = trim1.indexOf('(');
    String trim2;
    if (pSpot >= 0) {
      trim2 = trim1.substring(0, pSpot);
    } else {
      trim2 = trim1;
    }
    int p2Spot = input.indexOf('(');
    String product;
    if (p2Spot >= 0) {
      product = input.substring(0, p2Spot);
    } else {
      product = input.substring(0, eSpot);
    }
    format(product, trim2);
  }
  
  private static void format(String product, String fString) {
    String[] factors = split(fString);
    BigInteger p = new BigInteger(product);
    StringBuilder builder = new StringBuilder("BigFactor ").append(product).append('\n');
    for (String factor: factors) {
      final BigInteger remaining = p.divide(new BigInteger(factor.trim()));
      builder.append(factor)
          .append("       ");
      if (remaining.compareTo(BigInteger.ONE) > 0) {
        builder.append('(')
            .append(remaining)
            .append(')');
      }
      builder.append('\n');
      p = remaining;
    }
    System.out.println(builder);
  }
  
  private static String[] split(String s) {
    List<String> elements = new LinkedList<>();
    int xSpot = s.indexOf(TIMES);
    while (xSpot >= 0) {
      final String newS = s.substring(0, xSpot);
      elements.add(newS);
      s = s.substring(xSpot+1);
      xSpot = s.indexOf(TIMES);
    }
    elements.add(s);
    return elements.toArray(EMPTY);
  }
}
