package com.livenne.component;

import com.livenne.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class BorderContainer{
    private int up,down,left,right;
    private JPanel panel;
    public BorderContainer(JPanel jPanel,int margin) {
        panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(Settings.COLOR_TRANSPARENT);
        panel.add(jPanel, BorderLayout.CENTER);
        setMargin(margin);
    }

    public BorderContainer setMargin(int margin) {
        this.up = margin;
        this.down = margin;
        this.left = margin;
        this.right = margin;
        return this;
    }

    public BorderContainer up(int up) {
        this.up = up;
        return this;
    }
    public BorderContainer down(int down) {
        this.down = down;
        return this;
    }
    public BorderContainer left(int left) {
        this.left = left;
        return this;
    }
    public BorderContainer right(int right) {
        this.right = right;
        return this;
    }

    public JPanel getPanel() {
        JPanel upPanel = new JPanel();
        upPanel.setOpaque(false);
        upPanel.setPreferredSize(new Dimension(0,up));
        panel.add(upPanel,BorderLayout.NORTH);

        JPanel downPanel = new JPanel();
        downPanel.setOpaque(false);
        downPanel.setPreferredSize(new Dimension(0,down));
        panel.add(downPanel,BorderLayout.SOUTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(left,0));
        panel.add(leftPanel,BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(right,0));
        panel.add(rightPanel,BorderLayout.EAST);

        return panel;
    }

}
