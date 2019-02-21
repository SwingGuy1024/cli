package com.neptunedreams.tools.gui;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

public abstract class SplitComponent extends JPanel {
    @SuppressWarnings({"WeakerAccess", "RedundantSuppression"})
    protected final JTextArea left, right;

    @SuppressWarnings({"OverridableMethodCallDuringObjectConstruction", "WeakerAccess"})
    protected SplitComponent() {
        super(new GridBagLayout());
        left = makeTextArea();
        right = makeTextArea();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        add(wrap(left), constraints);
        constraints.gridx = 2;
        add(wrap(right), constraints);
        constraints.gridx = 1;
        constraints.weightx = 0.0;
        finishBuilding(constraints);
    }

    private void finishBuilding(GridBagConstraints constraints) {
        add(makeCenterPane(makeButtons()), constraints);
    }

    private JTextArea makeTextArea() {
        final int rows = 40, columns = 80;
        JTextArea jTextArea = new JTextArea(rows, columns);
        Font monospaced = Font.decode(Font.MONOSPACED);
        System.out.println("Font: " + monospaced);
        jTextArea.setFont(monospaced);
        return jTextArea;
    }

    protected abstract AbstractButton[] makeButtons();

    private JComponent makeCenterPane(AbstractButton... builders) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, panel.getBackground()));
        for (AbstractButton button : builders) {
            panel.add(button);
        }

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(BorderLayout.NORTH, panel);
        return outerPanel;
    }


    @SuppressWarnings("WeakerAccess")
    protected void enable(JComponent component, boolean enable) {
        component.setEnabled(enable);
        component.setBackground(enable ? Color.white : Color.LIGHT_GRAY);
    }

    @SuppressWarnings("WeakerAccess")
    final protected JScrollPane wrap(JTextArea textContents) {
        JScrollPane scrollPane = new JScrollPane(
                textContents,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        textContents.setWrapStyleWord(true);
        textContents.setLineWrap(true);
        return scrollPane;
    }

    @SuppressWarnings("WeakerAccess")
    static protected JButton makeButton(String name, ActionListener listener) {
        JButton button = new JButton(name);
        button.addActionListener(listener);
        return button;
    }
}
