package com.neptunedreams.tools.gui;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;

import static com.neptunedreams.tools.UrlConvert.*;
import static com.neptunedreams.tools.json.JsonTools.*;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/17/18
 * <p>Time: 6:29 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("MagicCharacter")
public final class CharConvert extends SplitComponent {

    // I used to save the start and end by selecting the text. But I discovered that even a disabled text component
    // will respond to mouse clicks and change the selection. So now I save the values outside the component.
    private int snippetStart = 0;
    private int snippetEnd = 0;
    private JButton toRight=null;
    private JButton toLeft=null;

    public static void main(String[] args) {
        //noinspection HardCodedStringLiteral
        JFrame frame = new JFrame("Character Converter");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.add(new CharConvert());
        frame.pack();
        frame.setVisible(true);
    }

    private CharConvert() {
        super();
    }


    @Override
    protected AbstractButton[] makeButtons() {
        toRight = makeButton("->", e -> goRight());
        toLeft = makeButton("<-", e -> goLeft());

        return new AbstractButton[]{
                toRight,
                toLeft,
        };
    }

    /*
        If we converted the whole String, it will have some ampersands that started out as ampersands, and others
        that started out as %26. When we convert back, we want to preserve just the ampersands that started out
        that way. There's no way to do this. So I divide the input text into segments separated by ampersands.
        The user will first click into the left side. The segment with the click point will be the only portion
        that gets moved to the right. And it gets pre-selected on the left as the pane is frozen. That way, after
        editing, it can be inserted into the same place.
     */
    private void goRight() {
        String leftText = left.getText();
        int dotPoint = left.getSelectionStart();
        int start = leftText.substring(0, dotPoint).lastIndexOf('&');
        if (start < 0) {
            start = 0;
        } else {
            start++;
        }
        int length = leftText.substring(dotPoint).indexOf('&');
        if (length < 0) {
            length = leftText.length() - start;
        } else {
            length += dotPoint - start;
        }
        int end = start + length;
        left.setSelectionStart(start);
        snippetStart = start;
        left.setSelectionEnd(end);
        snippetEnd = end;
        String text = leftText.substring(start, end);
        right.setText(propertyToPrettyFormat(decodeAscii(text)));
        enable(left, false);
        enable(right, true);
        enable(toRight, false);
        enable(toLeft, true);
        right.requestFocus();
    }

    private void goLeft() {
        String compare = left.getText();
        StringBuilder builder = new StringBuilder(encodeAscii(stripLineBreaks(right.getText())));
        StringBuilder leftBuilder = new StringBuilder(compare);
        leftBuilder.replace(snippetStart, snippetEnd, builder.toString());
        left.setText(leftBuilder.toString());
        left.setSelectionEnd(snippetStart + builder.length());
        enable(left, true);
        enable(right, false);
        enable(toLeft, false);
        enable(toRight, true);
        left.setSelectionStart(snippetStart);
        left.setSelectionEnd(snippetStart);
        try {
            left.scrollRectToVisible(left.modelToView(left.getSelectionStart()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        right.setText("");
        left.requestFocus();
    }
}