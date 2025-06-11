package com.livenne.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class Button extends JLabel {

    public Button(Icon icon) {
        super(icon);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickAction != null) {
                    clickAction.accept(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (pressAction != null) {
                    pressAction.accept(e);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (releaseAction != null) {
                    releaseAction.accept(e);
                    repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverAction != null) {
                    hoverAction.accept(e);
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (exitedAction != null) {
                    exitedAction.accept(e);
                    repaint();
                }
            }
        });
    }

    public Button() {
        this(null);
    }

    public void setIcon(Icon icon) {
        super.setIcon(icon);
    }

    private Consumer<Graphics2D> painter;
    private Consumer<MouseEvent> hoverAction;
    private Consumer<MouseEvent> pressAction;
    private Consumer<MouseEvent> releaseAction;
    private Consumer<MouseEvent> clickAction;
    private Consumer<MouseEvent> exitedAction;

    public Button onPaint(Consumer<Graphics2D> painter) {
        this.painter = painter;
        return this;
    }
    public Button onMouseHover(Consumer<MouseEvent> hoverAction) {
        this.hoverAction = hoverAction;
        return this;
    }
    public Button onMousePress(Consumer<MouseEvent> pressAction) {
        this.pressAction = pressAction;
        return this;
    }
    public Button onMouseRelease(Consumer<MouseEvent> releaseAction) {
        this.releaseAction = releaseAction;
        return this;
    }
    public Button onMouseClicked(Consumer<MouseEvent> clickAction) {
        this.clickAction = clickAction;
        return this;

    }
    public Button onMouseExited(Consumer<MouseEvent> exitedAction) {
        this.exitedAction = exitedAction;
        return this;
    }
    public Button setPreSize(int x,int y){
        setPreferredSize(new Dimension(x,y));
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
