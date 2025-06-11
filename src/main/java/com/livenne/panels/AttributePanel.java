package com.livenne.panels;

import com.livenne.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class AttributePanel extends JPanel {
    public AttributePanel() {
        setBackground(Settings.COLOR_TRANSPARENT);
        setPreferredSize(new Dimension(280, 0));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        {
            String placeholder = "添加好友/群聊";
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
                AttributePanel.this.requestFocus();
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
            int height = 37;
            tf.setPreferredSize(new Dimension(0, height));
            tf.setMinimumSize(new Dimension(0, height));
            tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            tf.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
            tf.setOpaque(false);
            tf.setForeground(Settings.COLOR_FONT_1);
            tf.setCaretColor(Settings.COLOR_FONT_1);
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setFont(Settings.FONT_MICROSOFT_YaHei);
            add(tf,BorderLayout.NORTH);
        }
        {

        }
        {
            JPanel panel = new JPanel(){
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    g2d.setColor(Settings.COLOR_BACKGROUND_4);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), Settings.OUT_ARC, Settings.OUT_ARC);

                    float border = 0.3F;
                    g2d.setColor(Settings.COLOR_BACKGROUND_3);
                    g2d.draw(new RoundRectangle2D.Float(0,0,getWidth()-2*border,getHeight()-2*border,Settings.OUT_ARC,Settings.OUT_ARC));

                }
            };
            int height = 55;
            panel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
            panel.setPreferredSize(new Dimension(0, height));
            panel.setBackground(Settings.COLOR_TRANSPARENT);
            add(panel,BorderLayout.SOUTH);
        }
    }
}