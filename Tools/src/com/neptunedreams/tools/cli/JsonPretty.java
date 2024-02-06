// use: pj
package com.neptunedreams.tools.cli;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * <p>Prettify a JSON String. This will fail if an open brace or bracket appears inside quotes,
 * but this is a rare case. In most cases, it works fine.</p>
 * <p>Created by IntelliJ IDEA.</p>
 * <p>Date: 2/5/24</p>
 * <p>Time: 1:28&nbsp;PM</p>
 * @author Miguel Mu&ntilde;oz
 */
public final class JsonPretty {
  
  private final ListIterator<Character> iterator;
  private final StringBuilder builder = new StringBuilder();

  private JsonPretty(String json) {
    iterator = getListIterator(json);
  }

  public static void main(String[] args) {
    String input;
    if (args.length == 0) {
      try (
        StringWriter writer = new StringWriter();
        InputStreamReader isReader = new InputStreamReader(System.in)
      ) {
        isReader.transferTo(writer);
        input = writer.toString();
      } catch (IOException ioe) {
        throw new IllegalStateException(ioe);
      }
    } else {
      input = String.join(" ", args);
    }
    JsonPretty jsonPretty = new JsonPretty(input);
    System.out.println(jsonPretty.format());
  }

  private String format() {
    //noinspection NonConstantStringShouldBeStringBuffer
    String indent = "";
    while (iterator.hasNext()) {
      char c = iterator.next();
      if ((c == '}') || (c == ']')) {
        indent = indent.substring(2);
        newLine(builder, indent);
      }
      builder.append(c);
      if (isOpenChar(c)) {
        indent += "  ";
        newLine(builder, indent);
      } else if (c == ',') {
        newLine(builder, indent);
      }
    }
    builder.append('\n');
    return builder.toString();
  }

  /**
   * <p>Process the case where the '{' or '[' starts an empty block. In this case, we don't
   * follow the open character with a new line.</p>
   * <p>If the open character starts an empty block, this method also adds the closing character
   * after skipping any white space in between.</p>
   * @param c The character to test
   * @return true if the character is an open character (a '{' or '[' character) and it has
   * contents that should begin on a new line; returns false if c is not an open character or if
   * it is an open character that begins an empty block
   */
  private boolean isOpenChar(char c) {
    if (processOpenChar(c, '{', '}')) {
      return true;
    }
    //noinspection RedundantIfStatement
    if (processOpenChar(c, '[', ']')) {
      return true;
    }
    return false;
  }
  
  private boolean processOpenChar(char c, char open, char close) {
    if (c == open) {
      char nxt = iterator.next();
      while (Character.isWhitespace(nxt)) {
        nxt = iterator.next();
      }
      if (nxt == close) {
        builder.append(nxt);
        return false;
      } else {
        iterator.previous();
        return true;
      }
    }
    return false;
  }

  private static void newLine(StringBuilder builder, String indent) {
    builder.append('\n')
        .append(indent);
  }

  private static ListIterator<Character> getListIterator(String s) {
    return new ListIterator<>() {
      private final char[] data = s.toCharArray();
      private int i = 0;

      @Override
      public boolean hasNext() {
        return i < data.length;
      }

      @Override
      public Character next() {
        if (i >= data.length) {
          throw new NoSuchElementException("next");
        }
        return data[i++];
      }

      @Override
      public boolean hasPrevious() {
        return i > 0;
      }

      @Override
      public Character previous() {
        if (i <= 0) {
          throw new NoSuchElementException("previous");
        }
        return data[--i];
      }

      @Override
      public int nextIndex() {
        return i;
      }

      @Override
      public int previousIndex() {
        return i - 1;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("remove");
      }

      @Override
      public void set(Character character) {
        throw new UnsupportedOperationException("set");
      }

      @Override
      public void add(Character character) {
        throw new UnsupportedOperationException("add");
      }
    };
  }
}
