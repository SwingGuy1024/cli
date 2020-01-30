package com.neptunedreams.tools.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public final class JsonTools {
    private JsonTools() { }

    /**
     * Convert a JSON string to pretty print version
     * Source: https://coderwall.com/p/ab5qha/convert-json-string-to-pretty-print-java-gson
     *
     * @param jsonString The raw JSON String
     * @return Prettified String. If an exception is thrown while converting (because it's
     * not JSON), returns the original String.
     */
    @SuppressWarnings("WeakerAccess")
    public static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        try {
            JsonObject json = parser.parse(jsonString).getAsJsonObject(); // throws RuntimeExceptions

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            return gson.toJson(json);
        } catch (RuntimeException e) {
            // it's not always a json String. If it's not, just return it.
            return jsonString;
        }
    }

    /**
     * Most values arrive as property expressions as P=S, where only S is the JSON String. So we split the value at
     * the = character, and just prettify what follows it. But we put the whole thing in the pane. If there is no
     * equal sign, we try to prettify the whole String.
     * @param jsonString The property expression to prettify.
     * @return A property expression with a prettified value. If the expression is not a property expression, the
     * prettified String. If it's not a JSON String, we just return it.
     */
    public static String propertyToPrettyFormat(String jsonString) {
        // for p=s where p is a property name and s is a JSON String
        int eSpot = jsonString.indexOf('=');
        if (eSpot >= 0) {
            int jSpot = eSpot+1;
            String head = jsonString.substring(0, jSpot);
            String tail = jsonString.substring(jSpot);
            return head+toPrettyFormat(tail);
        }
        return toPrettyFormat(jsonString);
    }

    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        Clipboard clipboard = null;
        String input;
        if (args.length > 0) {
            input = args[1];
        } else {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            input = clipboard.getData(DataFlavor.stringFlavor).toString();
        }
        String output = toPrettyFormat(input);
        if (clipboard == null) {
            System.out.println(output);
        } else {
            StringSelection stringSelection = new StringSelection(output);
            clipboard.setContents(stringSelection, stringSelection);
        }
    }
}
