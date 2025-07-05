package com.livenne.panel;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {

    private static OptionPanel instance;

    public static OptionPanel getInstance() {
        if (instance == null) {
            instance = new OptionPanel();
        }
        return instance;
    }

    private static final int width = 380;
    public OptionPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, 0));
        add(CubePanel.getInstance(), BorderLayout.WEST);
        add(ListPanel.getInstance(), BorderLayout.CENTER);
        add(ProfileCardPanel.getInstance(), BorderLayout.SOUTH);
    }
}
