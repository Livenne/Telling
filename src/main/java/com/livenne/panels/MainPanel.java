package com.livenne.panels;

import com.livenne.Settings;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel() {
        super(new BorderLayout());
        setBackground(Settings.COLOR_TRANSPARENT);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int border = Settings.WINDOW_BORDER;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g2d.setColor(Settings.COLOR_WINDOW_BORDER);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(),Settings.OUT_ARC,Settings.OUT_ARC);

        g2d.setColor(Settings.COLOR_BACKGROUND_1);
        g2d.fillRoundRect(border, border, getWidth()-(2*border), getHeight()-(2*border),Settings.OUT_ARC-border,Settings.OUT_ARC-border);
    }
}
