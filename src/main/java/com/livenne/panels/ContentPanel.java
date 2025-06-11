package com.livenne.panels;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.livenne.Settings;
import com.livenne.component.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

public class ContentPanel extends JPanel {
    public ContentPanel() {
        setBackground(Settings.COLOR_TRANSPARENT);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setLayout(new BorderLayout());
        setFocusable(true);
        add(new AttributePanel(), BorderLayout.WEST);
        add(new InputPanel(), BorderLayout.CENTER);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);



        GeneralPath path = new GeneralPath();

        path.moveTo(0, getHeight());
        path.lineTo(0, Settings.MINI_ARC);
        path.quadTo(0, 0, Settings.MINI_ARC, 0);
        path.lineTo(getWidth(), 0);

        float border = 0.3F;
        g2d.setStroke(new BasicStroke(border));
        g2d.setColor(Settings.COLOR_BACKGROUND_5);
        g2d.draw(path);

        g2d.drawLine(0,Settings.WINDOW_TITLE_HEIGHT,getWidth(),Settings.WINDOW_TITLE_HEIGHT);
    }
}
