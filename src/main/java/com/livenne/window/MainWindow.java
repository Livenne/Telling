package com.livenne.window;

import com.livenne.Main;
import com.livenne.panel.MainPanel;
import com.livenne.utils.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainWindow extends JFrame {

    private static MainWindow instance;
    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    private final int width = 1250;
    private final int height = width * 9/16;
    public MainWindow() {
        super("Telling");
        getRootPane().putClientProperty("JRootPane.titleBarShowIcon", false);
        getRootPane().putClientProperty("JRootPane.titleBarShowTitle", false);
        getRootPane().putClientProperty("JRootPane.titleBarBackground", Settings.COLOR_BACKGROUND_1);
        getRootPane().putClientProperty("JRootPane.titleBarForeground", Settings.COLOR_BACKGROUND_2);
        setIconImage(new ImageIcon(Objects.requireNonNull(Main.class.getResource("/static/icon.png"))).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(width, height));
        setMinimumSize(new Dimension(590, height));
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(MainPanel.getInstance());
    }
}
