package com.livenne.panels;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.livenne.Settings;
import com.livenne.component.Button;
import com.livenne.component.PressableIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FunctionPanel extends JPanel {
    public FunctionPanel() {
        setPreferredSize(new Dimension(60, 0));
        setBackground(Settings.COLOR_TRANSPARENT);
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        {
            Button icon = new Button(new FlatSVGIcon("static/icon.svg",35,35));
            icon.setBackground(Color.WHITE);
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(icon);
        }
        add(Box.createVerticalStrut(8));
        {
            JSeparator split = new JSeparator(){
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(Settings.COLOR_WINDOW_BORDER);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(),3,3);
                }
            };
            split.setBackground(Settings.COLOR_TRANSPARENT);
            split.setPreferredSize(new Dimension(0,3));
            add(split);
        }
        add(Box.createVerticalStrut(8));

        add(Box.createVerticalGlue());

        {
            Button more = new Button(new FlatSVGIcon("static/more.svg",26,26));
            more.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(more);
        }
        add(Box.createVerticalStrut(2));

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setColor(Settings.COLOR_BACKGROUND_2);
        g2d.fillRoundRect(0,0,getWidth(),getHeight(),Settings.MINI_ARC,Settings.MINI_ARC);
    }

}
