package com.livenne.panel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private static MainPanel instance;
    public static MainPanel getInstance() {
        if (instance == null) {
            instance = new MainPanel();
        }
        return instance;
    }

    public MainPanel() {
        setLayout(new BorderLayout());
        add(OptionPanel.getInstance(),BorderLayout.WEST);
        add(InteractPanel.getInstance(),BorderLayout.CENTER);
    }
}
