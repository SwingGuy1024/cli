package com.neptunedreams.tools.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;

public class AsciiTable extends JPanel {
  public static void main(String[] args) {
    JFrame frame = new JFrame("ASCII Values");
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setLocationByPlatform(true);
    frame.add(new AsciiTable());
    frame.pack();
    frame.setVisible(true);
  }
  
  private enum Code {
    NUL,
    SOH,
    STX,
    ETX,
    EOT,
    ENQ,
    ACK,
    BEL,
    BS,
    HT,
    LF,
    VT,
    FF,
    CR,
    SO,
    SI,
    DLE,
    DC1,
    DC2,
    DC3,
    DC4,
    NAK,
    SYN,
    ETB,
    CAN,
    EM,
    SUB,
    ESC,
    FS,
    GS,
    RS,
    US
  }
  
  private Map<Integer, String> highCodes = makeHighCodes();
  
  private Map<Integer, String> makeHighCodes() {
    Map<Integer, String> highMap = new HashMap<>();
    highMap.put(0x7F, "DEL");
    highMap.put(0x80, "XXX");
    highMap.put(0x81, "XXX");
    highMap.put(0x82, "BPH");
    highMap.put(0x83, "NBH");
    highMap.put(0x84, "IND");
    highMap.put(0x85, "NEL");
    highMap.put(0x86, "SSA");
    highMap.put(0x87, "ESA");
    highMap.put(0x88, "HTS");
    highMap.put(0x89, "HTJ");
    highMap.put(0x8A, "VTS");
    highMap.put(0x8B, "PLD");
    highMap.put(0x8C, "PLU");
    highMap.put(0x8D, "RI");
    highMap.put(0x8E, "SS2");
    highMap.put(0x8F, "SS3");
    highMap.put(0x90, "DCS");
    highMap.put(0x91, "PU1");
    highMap.put(0x92, "PU2");
    highMap.put(0x93, "STS");
    highMap.put(0x94, "CCH");
    highMap.put(0x95, "MW");
    highMap.put(0x96, "SPA");
    highMap.put(0x97, "EPA");
    highMap.put(0x98, "SOS");
    highMap.put(0x99, "XXX");
    highMap.put(0x9A, "SCI");
    highMap.put(0x9B, "CSI");
    highMap.put(0x9C, "ST");
    highMap.put(0x9D, "OSC");
    highMap.put(0x9E, "PM");
    highMap.put(0x9F, "APC");
    highMap.put(0xA0, "NBS");
    highMap.put(0xAD, "SHY");
    return highMap;
  }
  
  private final Font displayFont = Font.decode(Font.MONOSPACED);
  
  @SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
  AsciiTable() {
    super(new GridLayout(0, 8));
    for (int i=0; i<8; ++i) {
      JComponent label = makeLabel(" dec hx  c");
      label.setBorder(new MatteBorder(0, 1, 1, 0, Color.BLACK));
      add(label);
    }
    Code[] values = Code.values();
    List<JComponent> labelList = new LinkedList<>();
    for (int i=0; i<32; ++i) {
      labelList.add(makeCharView(values[i], i));
    }
    for (int i=32; i<256; ++i) {
      labelList.add(makeCharView(i));
    }
    
    for (int i=0; i<32; ++i) {
      for (int c=0; c<256; c+=32) {
        add(labelList.get(i+c));
      }
    }
    setBorder(new MatteBorder(1, 0, 1, 1, Color.BLACK));
  }
  
  private JComponent makeCharView(Code code, int value) {
    return makeLabel(String.format(" %3d %02x %-3s ", value, value, code.toString()));
  }
  
  private JComponent makeCharView(int value) {
    if (highCodes.containsKey(value)) {
      return makeLabel(String.format(" %3d %02x %-3s ", value, value, highCodes.get(value)));
    }
    return makeLabel(String.format(" %3d %02x  %c  ", value, value, (char)value));
  }
  
  private JComponent makeLabel(String s) {
    JLabel label = new JLabel(s);
    label.setFont(displayFont);
    label.setBorder(new MatteBorder(0, 1, 0, 0, Color.BLACK));
    return label;
  }
}
