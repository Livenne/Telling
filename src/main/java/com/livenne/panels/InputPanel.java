package com.livenne.panels;

import com.livenne.Settings;
import com.livenne.component.ScrollBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

public class InputPanel extends JPanel {
    public InputPanel() {
        setBackground(Settings.COLOR_TRANSPARENT);
        setLayout(new BorderLayout());
//        setBorder(BorderFactory.createEmptyBorder(0,8,15,8));
        add(new HeadPanel(), BorderLayout.NORTH);


        {
            JScrollPane scrollPane = new JScrollPane(new ChatPanel());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getVerticalScrollBar().setBlockIncrement(64);
            scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI());
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(scrollPane, BorderLayout.CENTER);
        }

        {
            String placeholder = "发消息";
            JTextField tf = new JTextField(placeholder){
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Settings.COLOR_BACKGROUND_3);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), Settings.MINI_ARC, Settings.MINI_ARC);

                    super.paintComponent(g);
                }
            };
            tf.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (tf.getText().equals(placeholder)) {
                        tf.setText(null);
                        tf.setHorizontalAlignment(JTextField.LEFT);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (tf.getText().isEmpty()) {
                        tf.setText(placeholder);
                        tf.setHorizontalAlignment(JTextField.CENTER);
                    }
                }
            });
            tf.addActionListener(e -> {
                InputPanel.this.requestFocus();
            });
            int length = 50;
            tf.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (tf.getText().length() > length) {
                        e.consume();
                    }
                }
            });
            int height = 55;
            tf.setPreferredSize(new Dimension(0, height));
            tf.setMinimumSize(new Dimension(0, height));
            tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            tf.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
            tf.setOpaque(false);
            tf.setForeground(Settings.COLOR_FONT_1);
            tf.setCaretColor(Settings.COLOR_FONT_1);
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setFont(Settings.FONT_MICROSOFT_YaHei);
            add(tf,BorderLayout.SOUTH);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        int width = getWidth();
        int height = getHeight();
        int radius = Settings.IN_ARC;

        GeneralPath path = new GeneralPath();
        path.moveTo(0, 0);
        path.lineTo(width - radius, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height - radius);
        path.curveTo(
                width, height - radius,
                width, height,
                width - radius, height
        );
        path.lineTo(0, height);
        path.lineTo(0, 0);
        path.closePath();

        g2d.setColor(Settings.COLOR_BACKGROUND_2);
        g2d.fill(path);

    }
}
