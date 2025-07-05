package com.livenne.panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livenne.Application;
import com.livenne.model.CustomerProfile;
import com.livenne.model.RequestEntity;
import com.livenne.model.ResponseEntity;
import com.livenne.model.User;
import com.livenne.utils.Settings;
import com.livenne.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TitlePanel extends JPanel {
    public static TitlePanel instance;

    public static TitlePanel getInstance() {
        if (instance == null) {
            instance = new TitlePanel();
        }
        return instance;
    }

    private final JLabel titleLabel;

    public TitlePanel() {
        setLayout(new BorderLayout());
        setBackground(Settings.COLOR_BACKGROUND_6);
        setPreferredSize(new Dimension(0, 50));

        titleLabel = new JLabel("");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0,15,0,15));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(Settings.FONT_MICROSOFT_YaHei_BOLD.deriveFont(16f));
        add(titleLabel, BorderLayout.WEST);
    }

    public void loadUsernameAsync() {
        try {
            int userId = CustomerProfile.messageObject;
            User user = getUserFromServer(userId);
            SwingUtilities.invokeLater(() -> {
                titleLabel.setText(user.getUsername());
                revalidate();
                repaint();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                titleLabel.setText("Username Unavailable");
                revalidate();
                repaint();
            });
        }
    }
    private User getUserFromServer(int userId) throws JsonProcessingException {
        ResponseEntity response = Application.send(new RequestEntity(
                "/user/get",
                StringUtils.map("id", userId),
                CustomerProfile.getToken()
        ));

        JsonNode n = new ObjectMapper().valueToTree(response.getPayload());
        return new ObjectMapper().treeToValue(n.get("user"), User.class);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Settings.COLOR_BACKGROUND_5);
        g2.setStroke(new BasicStroke(0.4f));
        g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        g2.dispose();
    }
}