package com.livenne.panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livenne.Application;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class SignUpPanel extends JPanel {

    private static SignUpPanel instance;
    public static SignUpPanel getInstance(JFrame frame) {
        if (instance == null) {
            instance = new SignUpPanel(frame);
        }
        return instance;
    }

    public SignUpPanel(JFrame frame) {
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

                JLabel label = new JLabel("REGISTER");
                Font font = new Font("Microsoft YaHei", Font.BOLD, 46);
                label.setFont(font);
                label.setForeground(Color.WHITE);
                temp.add(label);

                JButton button = new JButton("LOGIN");
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
                        frame.setContentPane(SignInPanel.getInstance(frame));
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
            HintTextPasswordField passwordConfirm;
            {
                JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
                temp.setBackground(Settings.COLOR_TRANSPARENT);

                passwordConfirm = new HintTextPasswordField("CONFIRM PASSWORD");
                passwordConfirm.setPreferredSize(new Dimension(290,55));
                passwordConfirm.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
                temp.add(passwordConfirm);
                panel.add(temp);
            }

            {
                JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
                temp.setBackground(Settings.COLOR_TRANSPARENT);
                JButton button = new JButton("SIGN UP");
                button.setBackground(new Color(50, 70, 255));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setPreferredSize(new Dimension(230, 45));
                temp.add(button);
                panel.add(temp);


                button.addActionListener(e -> {
                    Map<String,String> data = new HashMap<>();
                    data.put("username", username.getText());
                    data.put("password", password.getText());
                    data.put("passwordConfirm", passwordConfirm.getText());
                    ResponseEntity res = Application.send(new RequestEntity("/account/register", data, null));
                    JsonNode node = new ObjectMapper().valueToTree(res.getPayload());
                    if (node != null && node.has("token")) {
                        CustomerProfile.setToken(node.get("token").asText());
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


            panel.add(Box.createVerticalStrut(70));

        }
    }
    public void jumpToWindow(JFrame currentFrame) {
        currentFrame.dispose();
    }

}
