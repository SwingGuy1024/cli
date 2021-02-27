package com.neptunedreams.tools.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/13/21
 * <p>Time: 3:21 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public class DigitCharacters extends JPanel {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Digits");
    frame.add(new DigitCharacters());
    frame.pack();
    frame.setLocationByPlatform(true);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }
  
  private DigitCharacters() {
    super(new BorderLayout());
    JTextArea textField = new JTextArea(20, 40);
//    textField.setFont(Font.getFont("Arial Unicode MS"));
    textField.setFont(new Font("Arial Unicode MS", Font.PLAIN, 28));
    textField.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(textField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    add(scrollPane, BorderLayout.CENTER);
    
    boolean isDigit = false;
    for (int c = 26; c < (Character.MAX_CODE_POINT - 1); ++c) {
      if (Character.isDigit(c)) {
        isDigit = true;
        textField.append(String.valueOf((char)c));
      } else {
        if (isDigit) {
          isDigit = false;
          textField.append("\n\n");
        }
      }
    }
  }
}
