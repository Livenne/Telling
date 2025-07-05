package com.livenne.panel;

import com.livenne.utils.Settings;

import javax.swing.*;
import java.awt.*;

public class InteractPanel extends JPanel {

    private static InteractPanel instance;

    public static InteractPanel getInstance() {
        if (instance == null) {
            instance = new InteractPanel();
        }
        return instance;
    }

    public InteractPanel() {
        setLayout(new BorderLayout());
        setBackground(Settings.COLOR_BACKGROUND_6);
        add(TitlePanel.getInstance(), BorderLayout.NORTH);
        add(MessagePanel.getInstance(), BorderLayout.CENTER);
        add(InputPanel.getInstance(), BorderLayout.SOUTH);
    }
}
