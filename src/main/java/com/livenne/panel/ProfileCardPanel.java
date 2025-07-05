package com.livenne.panel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.livenne.Application;
import com.livenne.model.CustomerProfile;
import com.livenne.model.RequestEntity;
import com.livenne.model.ResponseEntity;
import com.livenne.utils.Settings;
import com.livenne.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ProfileCardPanel extends JPanel {

    private static ProfileCardPanel instance;

    public static ProfileCardPanel getInstance() {
        if (instance == null) {
            instance = new ProfileCardPanel();
        }
        return instance;
    }

    private final int gap = 8;
    private final int arc = 15;
    private JButton avatar;

    public ProfileCardPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 65));
        {
            JPanel northPanel = new JPanel();
            northPanel.setBackground(Settings.COLOR_TRANSPARENT);
            northPanel.setPreferredSize(new Dimension(0, gap));
            add(northPanel, BorderLayout.NORTH);
            JPanel southPanel = new JPanel();
            southPanel.setBackground(Settings.COLOR_TRANSPARENT);
            southPanel.setPreferredSize(new Dimension(0, gap*2));
            add(southPanel, BorderLayout.SOUTH);
            JPanel westPanel = new JPanel();
            westPanel.setBackground(Settings.COLOR_TRANSPARENT);
            westPanel.setPreferredSize(new Dimension(gap*2,0));
            add(westPanel, BorderLayout.WEST);
            JPanel eastPanel = new JPanel();
            eastPanel.setBackground(Settings.COLOR_TRANSPARENT);
            eastPanel.setPreferredSize(new Dimension(gap*2,0));
            add(eastPanel, BorderLayout.EAST);
        }
        {
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(Settings.COLOR_BACKGROUND_4);
            add(infoPanel, BorderLayout.CENTER);
            avatar = avatar();
            infoPanel.add(avatar, BorderLayout.WEST);
            {
                JPanel msg = new JPanel();
                msg.setBackground(Settings.COLOR_TRANSPARENT);
                msg.setLayout(new BoxLayout(msg, BoxLayout.Y_AXIS));
                msg.add(Box.createVerticalStrut(5));
                JLabel name = new JLabel(CustomerProfile.getAttribute("username"));
                name.setForeground(Color.WHITE);
                name.setFont(Settings.FONT_MICROSOFT_YaHei_BOLD.deriveFont(11.5f));
                JLabel status = new JLabel("在线");
                status.setFont(Settings.FONT_MICROSOFT_YaHei.deriveFont(12f));
                msg.add(name);
                msg.add(status);
                infoPanel.add(msg, BorderLayout.CENTER);
            }
            {
                JPanel featurePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                featurePanel.setBackground(Settings.COLOR_BACKGROUND_4);
                int size = 18;
                JButton settingButton = new JButton(new FlatSVGIcon("static/settings.svg",size,size));
                settingButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                settingButton.setPreferredSize(new Dimension(30, 30));
                settingButton.setBackground(Settings.COLOR_BACKGROUND_4);
                settingButton.setContentAreaFilled(true);
                settingButton.setBorderPainted(false);
                settingButton.setFocusPainted(false);
                settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                settingButton.putClientProperty("FlatLaf.style", "arc:18");
                settingButton.setAlignmentY(Component.CENTER_ALIGNMENT);
                featurePanel.add(settingButton);

                settingButton.addActionListener(e -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("选择文件");
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int result = chooser.showOpenDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String path = chooser.getSelectedFile().getAbsolutePath();
                        String[] fileSuffix = chooser.getSelectedFile().getName().split("\\.");
                        Map<String,Object> map = new HashMap<>();
                        map.put("suffix",fileSuffix[fileSuffix.length-1]);
                        map.put("image", StringUtils.imageToString(path));
                        map.put("userId", CustomerProfile.getUserId());
                        RequestEntity request = new RequestEntity("/image/upload", map, CustomerProfile.getToken());
                        ResponseEntity image = Application.send(request);
                        JsonNode node = new ObjectMapper().valueToTree(image.getPayload());
                        String fileName = node.get("fileName").asText();
                        CustomerProfile.setAttribute("avatar",fileName);
                        //刷新头像
                        infoPanel.remove(avatar);
                        avatar = avatar();
                        infoPanel.add(avatar, BorderLayout.WEST);
                        infoPanel.revalidate();
                        infoPanel.repaint();
                    }
                });

                infoPanel.add(featurePanel, BorderLayout.EAST);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Settings.COLOR_BACKGROUND_4);
        g2d.fillRoundRect(gap, 0, getWidth()-2*gap, getHeight()-gap, arc, arc);
        g2d.setColor(Settings.COLOR_BACKGROUND_5);
        g2d.setStroke(new BasicStroke(0.4f));
        g2d.drawRoundRect(gap, 0, getWidth()-2*gap, getHeight()-gap, arc, arc);
    }



    public JButton avatar() {

        ResponseEntity res = Application.send(new RequestEntity("/image/download", StringUtils.map("fileName", CustomerProfile.getAttribute("avatar")), CustomerProfile.getToken()));
        JsonNode node = new ObjectMapper().valueToTree(res.getPayload());
        BufferedImage img = StringUtils.stringToImage(node.get("image").asText());
        int size = 40;
        Image image = new ImageIcon(img)
                .getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);


        JButton avatar = new JButton(new ImageIcon(image)){
            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.fill(new Ellipse2D.Float(0, 0, size, size));
                Shape circle = new Ellipse2D.Double(1, 1, size-2, size-2);
                g2.setClip(circle);
                g2.drawImage(image, 1, 1, size-2, size-2, this);
                g2.dispose();
            }
        };
        avatar.setContentAreaFilled(false);
        avatar.setBorderPainted(false);
        avatar.setFocusPainted(false);
        avatar.setOpaque(false);
        return avatar;
    }

}
