package com.livenne.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CubePanel extends JPanel {

    private static CubePanel instance;
    public static CubePanel getInstance() {
        if (instance == null) {
            instance = new CubePanel();
        }
        return instance;
    }

    private static final int width = 70;

    public CubePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(width, 0));
        {
            JButton button = new JButton(new FlatSVGIcon("static/icon.svg", 35, 35));
            Color color = new Color(55,85,230);
            button.setBackground(color);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.putClientProperty("FlatLaf.style", "arc:25");


            button.addMouseListener(new MouseAdapter() {
                private final int offset = 1;
                private final Point originalLocation = new Point();

                @Override
                public void mousePressed(MouseEvent e) {
                    originalLocation.setLocation(button.getLocation());
                    button.setLocation(originalLocation.x, originalLocation.y + offset);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setLocation(originalLocation);
                }
            });

            add(button);
        }
    }
}
