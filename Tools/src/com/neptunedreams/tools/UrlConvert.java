package com.neptunedreams.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/17/18
 * <p>Time: 6:26 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"MagicCharacter", "MagicNumber", "HardcodedLineSeparator"})
public enum UrlConvert {
    ;
    public static String decodeAscii(final String input) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            int tail = 2;
            int ii = 0;
            while (ii < (input.length() - tail)) {
                char c = input.charAt(ii);
                if ((c == '%') && isCode(input, ii + 1)) {
                    outputStream.write(getCode(input.substring(ii + 1, ii + 3)));
                    ii += 3;
                } else if (c > 0xFF) {
                    int ic = (int) c;
                    throw new IllegalArgumentException(String.format("Character %c (%d = 0x%02X) too high", c, ic, ic));
                } else {
                    outputStream.write(c);
                    ii++;
                }
            }
            while (ii < input.length()) {
                outputStream.write(input.charAt(ii));
                ++ii;
            }
            return toUtf8(outputStream.toByteArray());
        } catch (IOException e) {
            //noinspection ProhibitedExceptionThrown
            throw new RuntimeException(e); // made necessary by the try() {} statement. This shouldn't happen
        }
    }

    public static String encodeAscii(String input) {
        StringBuilder builder = new StringBuilder();
        byte[] utf8Text = toByteData(input);
        for (byte b : utf8Text) {
            char c = (char) b;
            if (charsToSkip.contains(c)) {
                builder.append(c);
            } else {
                builder.append(String.format("%%%02X", b));// NON-NLS
            }
        }
        return builder.toString();
    }

//    public static void charTally(String s) {
//        Set<Character> cSet = new TreeSet<>();
//        for (int ii=0; ii<s.length(); ++ii) {
//            cSet.add(s.charAt(ii));
//        }
//        for (Character c: cSet) {
//            showChar(c);
//        }
//        System.out.println("\n\nMissing:");
//        for (char c=32; c<0x7f; ++c) {
//            if (!cSet.contains(c)) {
//                showChar(c);
//            }
//        }
//
//        Set<Character> codedSet = new TreeSet<>();
//        String encoded = decodeAscii(s);
//        for (int i=0; i<encoded.length(); ++i) {
//            char c = encoded.charAt(i);
//            if (!cSet.contains(c)) {
//                codedSet.add(c);
//            }
//        }
//        System.out.println("\n\nCoded:");
//        for (char c: codedSet) {
//            System.out.print(c);
//        }
//        System.out.println("");
//    }

//    private static void showChar(Character c) {
//        System.out.printf("%c (%2d = 0x%02X)%n", c, (int)c, (int)c);
//    }


    private static int getCode(String s) {
        return (char) Integer.parseUnsignedInt(s, 16);
    }

    private static boolean isCode(String s, int ii) {
        return (isHex(s.charAt(ii)) && isHex(s.charAt(ii + 1)));
    }

    private static boolean isHex(char c) {
        if ((c >= '0') && (c <= '9')) {
            return true;
        }
        if ((c >= 'A') && (c <= 'F')) {
            return true;
        }
        return (c >= 'a') && (c <= 'f');
    }

    @SuppressWarnings("HardCodedStringLiteral")
    public static void main(String[] args) {
        if (args.length > 0) {
            String start = args[0];
            String middle = decodeAscii(start);
            String end = encodeAscii(middle);
            if (start.equals(end)) {
                System.out.println("Passed");
            } else {
                System.out.println("Failed");
                System.out.printf("Lengths: %d & %d%n", start.length(), end.length());
                int j = 0;
                for (int ii = 0; ii < start.length(); ++ii) {
                    if (start.charAt(ii) != end.charAt(j)) {
                        System.out.printf("Mismatch at %d: %s, %s%n", ii, start.substring(ii, ii + 10), end.substring(j, j + 10));
                        j += 2;
                    }
                    j++;
                }
            }
        }
//        charTally(args[0]);
//        test();
    }

//    private static void test() {
//        test("%7bBraces%7D", "{Braces}");
//        test("x%7bBraces%7dx", "x{Braces}x");
//        test("xy%7bBraces%7dxy", "xy{Braces}xy");
//        test("abc%3cdef%3Eghi", "abc<def>ghi");
//        test("%7BBraces%7", "{Braces%7");
//        test("%7BBraces%", "{Braces%");
//        test("Brace %7g %3C %3E", "Brace %7g < >");
//        test("Brace %g7 %3C %3E", "Brace %g7 < >");
//        testStrip("abc\n    def\n ghi\njkl\n  ", "abcdefghijkl");
//        testStrip("abc \n    def\n ghi\njkl\n  ", "abc defghijkl");
//        testStrip("abc \n    de  f\n ghi\njkl\nmno", "abc de  fghijklmno");
//    }

    public static String stripLineBreaks(String txt) {
        int fromIndex = 0;
        StringBuilder builder = new StringBuilder();
        int newLineSpot = txt.indexOf('\n');
        boolean trailingSpaceFound = false;
        while (newLineSpot >= 0) {
            builder.append(txt, fromIndex, newLineSpot);
            int index = newLineSpot + 1;
            char space = txt.charAt(index++);
            trailingSpaceFound = false;
            while (((space == ' ') || (space == '\n')) && (index < txt.length())) {
                space = txt.charAt(index++);
                trailingSpaceFound = true;
            }
            fromIndex = index - 1;
            newLineSpot = txt.indexOf('\n', fromIndex);
        }
        if (!trailingSpaceFound) {
            builder.append(txt.substring(fromIndex));
        }
        return builder.toString();
    }

//    private static void test(String i, String o) {
//        String out = decodeAscii(i);
//        if (!out.equals(o)) {
//            throw new AssertionError(String.format("%s to %s instead of %s", i, out, o));
//        }
//        System.out.println(out);
//    }

//    private static void testStrip(String input, String expected) {
//        String out = stripLineBreaks(input);
//        if (!out.equals(expected)) {
//            throw new AssertionError(String.format("TS: <%s> to <%s> instead of <%s>", input, out, expected));
//        }
//        System.out.println(out);
//    }

    private static String toUtf8(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    private static byte[] toByteData(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private static final Set<Character> charsToSkip = makeNonSkipChars();

    private static Set<Character> makeNonSkipChars() {
        Set<Character> set = new HashSet<>();
        addRange(set, 'a', 'z');
        addRange(set, 'A', 'Z');
        addRange(set, '0', '9');
        set.add('(');
        set.add(')');
        set.add('+');
        set.add('-');
        set.add('.');
        set.add('=');
        set.add('_');
        return set;
    }

    private static void addRange(Set<? super Character> cSet, char first, char last) {
        for (char c = first; c <= last; ++c) {
            cSet.add(c);
        }
    }

    /**
     * Translates REST call form data from Chrome Inspector format to Postman bulk edit format. Form data viewed in the
     * Chrome inspect tool should be viewed using the "View Source" button. Text pasted into postman request body
     * should use the form-data or x-www-form-urlencoded format, and should be pasted in to the Bulk-Edit view, before
     * switching to the Key-Value Edit view. This class translates ampersands into new lines, and equals into colons.
     * Then it decodes the url-encoded characters, producing a UTF-8 encoded byte-array, which it converts to a String.
     * @param source The source, taken from Chrome Inspect Form Data using View Source
     * @return A postman form data String that may be pasted into postman's form-data or x-www-form-urlencoded Bulk
     * Edit view.
     */
    public static String chromeToPostman(String source) {
        StringBuilder builder = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(source, "&");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            processChromeToken(token, builder);
        }
        return builder.toString();
    }

    private static void processChromeToken(String token, StringBuilder result) {
        /*
        Postman x-www-form-urlencoded Bulk Edit:
        key:value\n

        Chrome Inspect "View Source"
        key=value& -- all url encoded utf-8
        */
        int eSpot = token.indexOf('=');
        if (eSpot >= 0) {
            String head = token.substring(0, eSpot);
            String tail = token.substring(eSpot+1); // skip the equal sign
            result
                    .append(decodeAscii(head))
                    .append(':')
                    .append(decodeAscii(tail))
                    .append('\n');
        } else {
            result.append(decodeAscii(token));
        }
    }
}
