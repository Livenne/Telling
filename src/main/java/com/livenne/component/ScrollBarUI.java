package com.livenne.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ScrollBarUI extends BasicScrollBarUI {
    private static final int SCROLLBAR_WIDTH = 6;
    private static final Color TRACK_COLOR = new Color(44, 44, 44);
    private static final Color THUMB_COLOR = new Color(120, 120, 120);

    @Override
    protected void configureScrollBarColors() {
        trackColor = TRACK_COLOR;
        thumbColor = THUMB_COLOR;
    }
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = SCROLLBAR_WIDTH;
        g2.setColor(thumbColor);
        g2.fillRoundRect(
                thumbBounds.x,
                thumbBounds.y,
                SCROLLBAR_WIDTH,
                thumbBounds.height,
                arc, arc
        );

        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(trackColor);
        g2.fillRect(trackBounds.x, trackBounds.y, SCROLLBAR_WIDTH,
                trackBounds.height);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(SCROLLBAR_WIDTH, SCROLLBAR_WIDTH);
    }
}