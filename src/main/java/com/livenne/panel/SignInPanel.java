package com.livenne.panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livenne.Application;
import com.livenne.Main;
import com.livenne.component.HintTextField;
import com.livenne.component.HintTextPasswordField;
import com.livenne.model.CustomerProfile;
import com.livenne.model.RequestEntity;
import com.livenne.model.ResponseEntity;
import com.livenne.model.User;
import com.livenne.utils.Settings;
import com.livenne.utils.StringUtils;
import com.livenne.window.LoginWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SignInPanel extends JPanel {

    private static SignInPanel instance;
    public static SignInPanel getInstance(JFrame frame) {
        if (instance == null) {
            instance = new SignInPanel(frame);
        }
        return instance;
    }

    public SignInPanel(JFrame frame) {
        setLayout(new BorderLayout());
        setOpaque(false);
        {
            JPanel panel = new JPanel();
            add(panel, BorderLayout.EAST);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Settings.COLOR_TRANSPARENT);
            panel.setPreferredSize(new Dimension(500, 0));
            panel.add(Box.createVerticalStrut(70));
            {
                JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
                temp.setBackground(Settings.COLOR_TRANSPARENT);
                JLabel label = new JLabel("LOGIN");
                Font font = new Font("Microsoft YaHei", Font.BOLD, 46);
                label.setFont(font);
                label.setForeground(Color.WHITE);
                temp.add(label);

                JButton button = new JButton("REGISTER");
                button.setBackground(new Color(64,64,72));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.setFont(new Font("Microsoft YaHei", Font.BOLD, 8));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setPreferredSize(new Dimension(55, 20));
                temp.add(button);


                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        frame.setContentPane(SignUpPanel.getInstance(frame));
                        frame.revalidate();
                        frame.repaint();
                    }
                });

                panel.add(temp);
            }
            HintTextField username;
            {
                JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
                temp.setBackground(Settings.COLOR_TRANSPARENT);
                username = new HintTextField("USERNAME");
                username.setPreferredSize(new Dimension(290,55));

                username.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
                temp.add(username);
                panel.add(temp);
            }

            HintTextPasswordField password;
            {
                JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
                temp.setBackground(Settings.COLOR_TRANSPARENT);

                password = new HintTextPasswordField("PASSWORD");
                password.setPreferredSize(new Dimension(290,55));
                password.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
                temp.add(password);
                panel.add(temp);
            }

            {
                JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
                temp.setBackground(Settings.COLOR_TRANSPARENT);
                JButton button = new JButton("SIGN IN");
                button.setBackground(new Color(50, 70, 255));
                button.setForeground(Color.WHITE);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.setFocusPainted(false);
                button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setPreferredSize(new Dimension(230, 45));
                temp.add(button);
                panel.add(temp);

                button.addActionListener(e -> {
                    Map<String,String> data = new HashMap<>();
                    data.put("username", username.getText());
                    data.put("password", password.getText());
                    ResponseEntity res = Application.send(new RequestEntity("/account/login", data, null));
                    JsonNode node = new ObjectMapper().valueToTree(res.getPayload());
                    if (node != null && node.has("token")) {
                        String token = node.get("token").asText();
                        CustomerProfile.setToken(token);
                        ResponseEntity userRes = Application.send(new RequestEntity("/user/get", StringUtils
                                .map("id", CustomerProfile.getUserId())
                                , CustomerProfile.getToken())
                        );
                        try {
                            User user = new ObjectMapper().treeToValue(new ObjectMapper().valueToTree(userRes.getPayload()).get("user"),User.class);
                            CustomerProfile.createUser(user);
                        } catch (JsonProcessingException _){}
                        ((LoginWindow) frame).jumpToMain();
                    }else {
                        //
                    }
                });


            }

            panel.add(Box.createVerticalStrut(150));

        }
    }

}
