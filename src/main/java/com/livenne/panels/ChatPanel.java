package com.livenne.panels;

import com.livenne.Settings;
import com.livenne.component.TextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChatPanel extends JPanel {
    public ChatPanel() {
        setBackground(Settings.COLOR_TRANSPARENT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(Settings.COLOR_WINDOW_BORDER);
                panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));
                panel.setMinimumSize(new Dimension(0, 45));
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
                TextField tf = new TextField();
                add(tf, BorderLayout.WEST);
                add(panel);
                add(Box.createVerticalStrut(16));
                revalidate();
                JViewport viewport = (JViewport) getParent();
                Rectangle rect = getBounds();
                viewport.scrollRectToVisible(new Rectangle(0, rect.height - 1, 1, 1));
            }
        });

        {
//            add(panel);
        }
    }
}
