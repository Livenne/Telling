package com.livenne.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class AvatarButton extends JButton {
    private final Image image;
    private final int size;

    public AvatarButton(BufferedImage img, int size) {
        this.image = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        this.size = size;

        setPreferredSize(new Dimension(size, size));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, size, size, 50, 50));
        Shape circle = new Ellipse2D.Double(1, 1, size - 2, size - 2);
        g2.setClip(circle);
        g2.drawImage(image, 1, 1, size - 2, size - 2, this);
        g2.dispose();
    }
}
