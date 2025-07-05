package com.livenne.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.livenne.Application;
import com.livenne.component.HintTextField;
import com.livenne.model.CustomerProfile;
import com.livenne.model.Message;
import com.livenne.model.RequestEntity;
import com.livenne.utils.AiUtils;
import com.livenne.utils.Settings;
import com.livenne.utils.StringUtils;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {

    private static InputPanel instance;

    public static InputPanel getInstance() {
        if (instance == null) {
            instance = new InputPanel();
        }
        return instance;
    }


    public InputPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 80));
        setBackground(Settings.COLOR_BACKGROUND_6);
        {
            JPanel panel = new JPanel(new BorderLayout()){
                @Override
                protected void paintComponent(Graphics g) {
                    int gap = 7;
                    int arc = 20;
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(Settings.COLOR_BACKGROUND_3);
                    g2d.fillRoundRect(gap, 0, getWidth()-2*gap, getHeight()-gap, arc, arc);

                    g2d.setColor(Settings.COLOR_BACKGROUND_5);
                    g2d.setStroke(new BasicStroke(0.4f));
                    g2d.drawRoundRect(gap, 0, getWidth()-2*gap, getHeight()-gap, arc, arc);
                }
            };
            add(panel, BorderLayout.CENTER);
            int gap = 15;
            panel.setBorder(BorderFactory.createEmptyBorder(gap, gap+7, gap+7, gap+7));

            JButton button = new JButton(new FlatSVGIcon("static/more.svg",20,20));
            button.setPreferredSize(new Dimension(30,0));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            panel.add(button, BorderLayout.WEST);

            HintTextField hintTextField = new HintTextField("发送消息");
            hintTextField.setBackground(Settings.COLOR_BACKGROUND_3);
            hintTextField.setFont(Settings.FONT_MICROSOFT_YaHei.deriveFont(14f));
            hintTextField.setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
            hintTextField.addActionListener(e->{
                if (hintTextField.getText().isEmpty() || CustomerProfile.messageObject == null) {
                    return;
                }
                Message message = new Message(hintTextField.getText(), CustomerProfile.getUserId(),CustomerProfile.messageObject);
                Application.send(new RequestEntity("/chat/message", StringUtils.map("message", message), CustomerProfile.getToken()));
                hintTextField.setText("");
                MessagePanel.getInstance().loadMessage();
            });
            panel.add(hintTextField, BorderLayout.CENTER);

            button.addActionListener(e->{
                String s = AiUtils.enrichTextEmotionally(hintTextField.getText());
                System.out.println(s);
                hintTextField.setText(s);
            });
        }
        {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(0, 10));
            panel.setBackground(Settings.COLOR_BACKGROUND_6);
            add(panel, BorderLayout.SOUTH);
        }
    }

}
