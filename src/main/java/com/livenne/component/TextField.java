package com.livenne.component;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class TextField extends JTextField {

    private int length;
    private String placeholder;

    public TextField(String placeholder) {
        this.placeholder = placeholder;
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);
    }

    public TextField() {
        this("");
    }

    private Consumer<Graphics2D> painter;

    public TextField onPaint(Consumer<Graphics2D> painter) {
        this.painter = painter;
        return this;
    }

    public int getLength() {
        return length;
    }
    public TextField setLength(int length) {
        this.length = length;
        return this;
    }


    public String getPlaceholder() {
        return placeholder;
    }
    public TextField setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public TextField setPreSize(int x, int y) {
        setPreferredSize(new Dimension(x, y));
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if (painter != null) painter.accept(g2d);
        super.paintComponent(g);
    }
}
