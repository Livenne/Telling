package com.livenne.panel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livenne.Application;
import com.livenne.model.*;
import com.livenne.utils.Settings;
import com.livenne.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MessagePanel extends JPanel {

    private static MessagePanel instance;
    public static MessagePanel getInstance() {
        if (instance == null) {
            instance = new MessagePanel();
        }
        return instance;
    }

    private final JPanel listPanel;
    private final JScrollPane scrollPane;
    public static List<Message> messages = new ArrayList<>();

    public MessagePanel() {
        setLayout(new BorderLayout());
        setBackground(Settings.COLOR_BACKGROUND_6);

        listPanel = new JPanel();
        listPanel.setBackground(Settings.COLOR_BACKGROUND_6);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Settings.COLOR_BACKGROUND_6);
        container.add(listPanel, BorderLayout.NORTH);
        scrollPane = new JScrollPane(container,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Settings.COLOR_BACKGROUND_6);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, Integer.MAX_VALUE));
        scrollPane.getViewport().setBackground(Settings.COLOR_BACKGROUND_6);

        // 监听窗口拉伸时重绘子项
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                listPanel.revalidate();
            }
        });

        add(scrollPane, BorderLayout.CENTER);
    }

    public void addListItem(Message message) {
        User user = loadUser(message.getSender());
        BufferedImage avatarImg = loadAvatarImage(user.getAvatar());

        MessageBubble bubble = new MessageBubble(user.getNickname(), message.getMessage(), avatarImg);
        listPanel.add(bubble, listPanel.getComponentCount());
        listPanel.add(Box.createVerticalStrut(12));
        listPanel.revalidate();

        // 滚动到底部
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    public void loadMessage() {
        SwingUtilities.invokeLater(()->{
            if (CustomerProfile.messageObject == null) return;
            listPanel.removeAll();
            messages = CustomerProfile.getMessage(CustomerProfile.messageObject);
            messages.forEach(this::addListItem);
            listPanel.revalidate();
        });
    }

    private User loadUser(int id) {
        ResponseEntity response = Application.send(
                new RequestEntity("/user/get", StringUtils.map("id", id), CustomerProfile.getToken()));
        JsonNode node = new ObjectMapper().valueToTree(response.getPayload());
        try {
            return new ObjectMapper().treeToValue(node.get("user"), User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage loadAvatarImage(String fileName) {
        ResponseEntity res = Application.send(
                new RequestEntity("/image/download", StringUtils.map("fileName", fileName), CustomerProfile.getToken()));
        JsonNode node = new ObjectMapper().valueToTree(res.getPayload());
        return StringUtils.stringToImage(node.get("image").asText());
    }
}
