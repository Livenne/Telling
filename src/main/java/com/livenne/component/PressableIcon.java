package com.livenne.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PressableIcon extends JComponent {
    private final Icon icon;
    private boolean pressed = false;
    private Point pressPoint = new Point();
    private final int PRESS_OFFSET = 1;
    private final int ANIMATION_DURATION = 120;

    public PressableIcon(String path, int size) {
        this.icon = new FlatSVGIcon(path, size, size);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(size, size + PRESS_OFFSET));
        setMaximumSize(new Dimension(size, size + PRESS_OFFSET));

        // 鼠标事件监听器
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                pressPoint.setLocation(e.getPoint());
                animatePress();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                animateRelease();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pressed = false;
                animateRelease();
                repaint();
            }
        });
    }

    // 平滑动画
    private void animatePress() {
        new Thread(() -> {
            try {
                for (int i = 0; i <= PRESS_OFFSET; i++) {
                    SwingUtilities.invokeLater(() -> repaint());
                    Thread.sleep(ANIMATION_DURATION / PRESS_OFFSET);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void animateRelease() {
        new Thread(() -> {
            try {
                for (int i = PRESS_OFFSET; i >= 0; i--) {
                    SwingUtilities.invokeLater(() -> repaint());
                    Thread.sleep(ANIMATION_DURATION / PRESS_OFFSET);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        int x = (getWidth() - icon.getIconWidth()) / 2;
        int y = (getHeight() - icon.getIconHeight() - PRESS_OFFSET) / 2;

        // 按下状态时，图标下移
        if (pressed) {
            y += PRESS_OFFSET;
        }

        // 绘制图标
        icon.paintIcon(this, g2d, x, y);
    }
}