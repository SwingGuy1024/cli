package com.neptunedreams.tools.gui;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import static com.neptunedreams.tools.UrlConvert.*;

/**
 Translates REST call form data from Chrome Inspector format to Postman bulk edit format. Form data viewed in the
 Chrome inspect tool should be viewed using the "View Source" button. Text pasted into postman request body should
 use the x-www-form-urlencoded format, and should be pasted in to the Bulk-Edit view, before switching to the
 Key-Value Edit view. This class translates ampersands into new lines, and equals into colons. Then it decodes the
 url-encoded characters, producing a UTF-8 encoded byte-array, which it converts to a String.
 */
public class ChromeToPostman extends SplitComponent {

    public static void main(String[] args) {
        //noinspection HardCodedStringLiteral
        JFrame frame = new JFrame("Chrome to Postman");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.add(new ChromeToPostman());
        frame.pack();
        frame.setVisible(true);
    }

    private ChromeToPostman() {
        super();
    }

    @Override
    protected AbstractButton[] makeButtons() {

        return new JButton[] {
                makeButton("->", e->goRight())
        };
    }

    private void goRight() {
        String leftText = left.getText();
        right.setText(chromeToPostman(leftText));
        right.requestFocus();

        // put the text on the clipboard.
        StringSelection stringSelection = new StringSelection(right.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
    }
}

