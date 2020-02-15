package com.neptunedreams.tools.exp;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class CBContents {
    public static void main(String[] args) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        DataFlavor[] flavors = clipboard.getAvailableDataFlavors();
        System.out.printf("Flavor Count: %d%n", flavors.length);
        for (DataFlavor flavor: flavors) {
            Class<?> representationClass = flavor.getRepresentationClass();
            String array = "";
            String charset = flavor.getParameter("charset");
            if (representationClass.isArray()) {
                array = "[Array]";
                representationClass = representationClass.getComponentType();
            }
            System.out.printf("%-118s %-10s %-25s %s%n", flavor, charset, representationClass, array);
        }
        System.out.println("\n---\n\n");
        for (DataFlavor flavor: flavors) {
            System.out.printf("Flavor: %s%n", flavor);
            String text;
            String charset = getCharset(flavor);
            if (charset == null) { charset = "US-ASCII"; }
            try {
                Object data = clipboard.getData(flavor);
                if (data instanceof Reader) {
                    text = readFromReader((Reader) data);
                } else if (data instanceof String) {
                    text = ((String) data);
                } else if (data instanceof InputStream) {
                    text = readFromReader(new InputStreamReader((InputStream) data, charset));
                } else if (data instanceof CharBuffer) {
                    CharBuffer buffer = (CharBuffer) data;
                    text = buffer.toString();
                } else if (data instanceof ByteBuffer) {
                    ByteBuffer buffer = (ByteBuffer) data;
                    text = new String(buffer.array(), charset);
                } else if (data.getClass().isArray()) {
                    Class<?> componentType = data.getClass().getComponentType();
                    if (componentType == Character.TYPE) {
                        text = new String((char[]) data);
                    } else if (data instanceof byte[]) {
                        text = new String((byte[]) data, charset);
                    } else {
                        text = String.format("Unknown type: %s array", componentType);
                    }
                } else {
                    text = data.toString();
                }
                
                if (flavor.isMimeTypeEqual("text/html")) {
                    text = parseHtml(text);
                }

                System.out.printf("  Contents: %s%n%n", text);
            } catch (UnsupportedFlavorException | IOException e) {
                System.err.println(flavor);
                e.printStackTrace();
            }
        }
    }

    private static String parseHtml(String text) throws IOException {
        ParserDelegator delegator = new ParserDelegator();
        final StringBuilder rawText = new StringBuilder();
        HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
            @Override
            public void handleText(char[] data, int pos) { 
                rawText.append(data);
            }

            @Override
            public void handleEndOfLineString(String eol) {
                rawText.append('\n');
            }

            @Override
            public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
                if (t == HTML.Tag.P) {
                    rawText.append('\n');
                }
            }
        };
        delegator.parse(new StringReader(text), callback, true); // ignore charset if it's an array
        text = rawText.toString();
        return text;
    }

    private static String readFromReader(Reader data) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(data);
        String text = "";
        while (text != null) {
            builder.append(text);
            text = reader.readLine();
        }
        reader.close();
        return builder.toString();
    }
    
    private static String getCharset(DataFlavor flavor) {
        String charSet = flavor.getParameter("charset");
        if (charSet == null) {
            String src = flavor.toString();
            int csSpot = src.indexOf("charset");
            if (csSpot >= 0) {
                int equalIndex = src.indexOf('=', csSpot) + 1;
                int end = src.indexOf(']', csSpot);
                charSet = src.substring(equalIndex, end);
                System.out.printf("Revised charset: %s%n", charSet);
            }
        }
        return charSet;
    }
}
