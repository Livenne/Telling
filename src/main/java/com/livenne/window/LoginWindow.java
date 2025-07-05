package com.livenne.window;

import com.livenne.Main;
import com.livenne.panel.SignInPanel;
import com.livenne.utils.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LoginWindow extends JFrame {

    private static LoginWindow instance;
    public static LoginWindow getInstance() {
        if (instance == null) {
            instance = new LoginWindow();
        }
        return instance;
    }

    private final int width = 1100;
    private final int height = width * 9/16;
    public LoginWindow() {
        super("Telling - Login");

        UIManager.put("TitlePane.buttonHoverBackground", new Color(100, 100, 100, 150));
        UIManager.put("TitlePane.buttonPressedBackground", new Color(80, 80, 80, 180));
        getRootPane().putClientProperty("JRootPane.titleBarShowIcon", false);
        getRootPane().putClientProperty("JRootPane.titleBarShowTitle", false);
        getRootPane().putClientProperty("JRootPane.titleBarBackground", Settings.COLOR_TRANSPARENT);
        getRootPane().putClientProperty("JRootPane.titleBarForeground", Settings.COLOR_BACKGROUND_2);

        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(Objects.requireNonNull(Main.class.getResource("/static/icon.png"))).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Image bgImage =  new ImageIcon(Objects.requireNonNull(Main.class.getResource("/static/LoginBackground.png"))).getImage();

        JLayeredPane layeredPane = getLayeredPane();
        JLabel bgLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgLabel.setBounds(0, 0, getWidth(), getHeight());
        bgLabel.setOpaque(false);
        layeredPane.add(bgLabel, Integer.valueOf(Integer.MIN_VALUE));

        setVisible(true);
        setContentPane(SignInPanel.getInstance(this));
    }
    public void jumpToMain() {
        dispose();
        MainWindow.getInstance();
    }
}
