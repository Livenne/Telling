package com.livenne.panels;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.livenne.Settings;
import com.livenne.component.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class TitlePanel extends JPanel {
    private Point dragStartPoint;
    private JFrame parentFrame;

    public TitlePanel(JFrame frame) {
        super(new BorderLayout());
        this.parentFrame = frame;
        setPreferredSize(new Dimension(0, 28));
        setBackground(Settings.COLOR_TRANSPARENT);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartPoint = e.getPoint();
                SwingUtilities.convertPointToScreen(dragStartPoint, TitlePanel.this);
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStartPoint != null) {
                    Point currentPoint = e.getPoint();
                    SwingUtilities.convertPointToScreen(currentPoint, TitlePanel.this);
                    int deltaX = currentPoint.x - dragStartPoint.x;
                    int deltaY = currentPoint.y - dragStartPoint.y;

                    Point frameLocation = parentFrame.getLocation();
                    parentFrame.setLocation(
                            frameLocation.x + deltaX,
                            frameLocation.y + deltaY
                    );
                    dragStartPoint = currentPoint;
                }
            }
        });

        {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setBackground(Settings.COLOR_TRANSPARENT);
            panel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 1));
            add(panel, BorderLayout.EAST);

            int border = 10;
            int iconSize = 12;

            {
                Button button = new Button(new FlatSVGIcon("static/mini.svg",iconSize,iconSize));
                button.setBorder(BorderFactory.createEmptyBorder(border,border,border,border));
                button.setOpaque(true);
                button.setBackground(Settings.COLOR_TRANSPARENT);
                button.onMouseHover(e -> {
                    button.setIcon(new FlatSVGIcon("static/mini_fire.svg", iconSize, iconSize));
                    button.repaint();
                }).onMouseExited(e -> {
                    button.setIcon(new FlatSVGIcon("static/mini.svg", iconSize, iconSize));
                    button.repaint();
                }).onPaint(g->{
                    g.setColor(Settings.COLOR_BACKGROUND_1);
                    g.fillRect(button.getWidth()-button.getIcon().getIconWidth()-border, button.getHeight()-button.getIcon().getIconHeight()-border+2, iconSize,iconSize);
                });
                panel.add(button);
            }
            panel.add(Box.createHorizontalStrut(4));
            {
                Button button = new Button(new FlatSVGIcon("static/window.svg",iconSize,iconSize));
                button.setBorder(BorderFactory.createEmptyBorder(border,border,border,border));
                button.setOpaque(true);
                button.setBackground(Settings.COLOR_TRANSPARENT);

                button.onMouseHover(e -> {
                    button.setIcon(new FlatSVGIcon("static/window_fire.svg", iconSize, iconSize));
                    button.repaint();
                }).onMouseExited(e -> {
                    button.setIcon(new FlatSVGIcon("static/window.svg", iconSize, iconSize));
                    button.repaint();
                }).onPaint(g->{
                    g.setColor(Settings.COLOR_BACKGROUND_1);
                    g.fillRect(button.getWidth()-button.getIcon().getIconWidth()-border, button.getHeight()-button.getIcon().getIconHeight()-border+2, iconSize,iconSize);
                });
                panel.add(button);
            }
            panel.add(Box.createHorizontalStrut(4));
            {
                Button button = new Button(new FlatSVGIcon("static/close.svg",iconSize,iconSize));
                button.setBorder(BorderFactory.createEmptyBorder(border,border,border,border));
                button.setOpaque(true);
                button.setBackground(Settings.COLOR_TRANSPARENT);

                button.onMouseHover(e -> {
                    button.setIcon(new FlatSVGIcon("static/close_fire.svg", iconSize, iconSize));
                    button.repaint();
                }).onMouseExited(e -> {
                    button.setIcon(new FlatSVGIcon("static/close.svg", iconSize, iconSize));
                    button.repaint();
                }).onPaint(g->{
                    g.setColor(Settings.COLOR_BACKGROUND_1);
                    g.fillRect(button.getWidth()-button.getIcon().getIconWidth()-border, button.getHeight()-button.getIcon().getIconHeight()-border+2, iconSize,iconSize);
                });
                panel.add(button);
            }

        }

    }
}
