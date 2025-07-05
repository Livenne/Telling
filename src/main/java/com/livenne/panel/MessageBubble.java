package com.livenne.panel;

import com.livenne.utils.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MessageBubble extends JPanel {

    public MessageBubble(String nickname, String content, BufferedImage avatarImage) {
        setLayout(new BorderLayout());
        setBackground(Settings.COLOR_BACKGROUND_6);
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Settings.COLOR_BACKGROUND_7);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Settings.COLOR_BACKGROUND_6);
            }
        });

        int avatarSize = 40;
        AvatarButton avatar = new AvatarButton(avatarImage, avatarSize);

        add(avatar, BorderLayout.WEST);

        // 中间部分：昵称 + 内容
        JPanel messageBox = new JPanel();
        messageBox.setLayout(new BoxLayout(messageBox, BoxLayout.Y_AXIS));
        messageBox.setOpaque(false);
        messageBox.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        // 昵称
        JLabel nameLabel = new JLabel(nickname);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(Settings.FONT_MICROSOFT_YaHei.deriveFont(15f));

        messageBox.add(nameLabel);
        messageBox.add(Box.createVerticalStrut(4));

        // 消息内容

        JTextArea textArea = new JTextArea(content);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setFont(Settings.FONT_MICROSOFT_YaHei);
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(Settings.COLOR_TRANSPARENT);
        textArea.setMinimumSize(new Dimension(0,0));
        textArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        messageBox.add(textArea);


        add(messageBox, BorderLayout.CENTER);
    }
}
