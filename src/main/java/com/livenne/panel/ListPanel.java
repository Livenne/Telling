package com.livenne.panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.livenne.Application;
import com.livenne.component.HintTextField;
import com.livenne.model.CustomerProfile;
import com.livenne.model.RequestEntity;
import com.livenne.model.ResponseEntity;
import com.livenne.model.User;
import com.livenne.utils.Settings;
import com.livenne.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ListPanel extends JPanel {

    private static ListPanel instance;

    public static ListPanel getInstance() {
        if (instance == null) {
            instance = new ListPanel();
        }
        return instance;
    }

    private JPanel listPanel;
    private JScrollPane scrollPane;
    private List<JPanel> childPanels = new ArrayList<>();
    public ListPanel() {
        setLayout(new BorderLayout());
        {
            JPanel searchPanel = new JPanel(new BorderLayout()){
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
            };
            searchPanel.setPreferredSize(new Dimension(0, 50));
            searchPanel.setBackground(Settings.COLOR_TRANSPARENT);

            {
                int gap = 9;
                JPanel backPanel = new JPanel(new BorderLayout());
                backPanel.setOpaque(false);
                backPanel.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
                searchPanel.add(backPanel, BorderLayout.CENTER);
                JPanel colorPanel = new JPanel(new BorderLayout());
                backPanel.add(colorPanel, BorderLayout.CENTER);
                {
                    HintTextField searchField = new HintTextField("搜索用户/添加好友");
                    searchField.putClientProperty("JComponent.roundRect", true);
                    searchField.setForeground(Color.WHITE);
                    searchField.setFont(Settings.FONT_MICROSOFT_YaHei.deriveFont(12f));
                    searchField.setOpaque(true);
                    searchField.setBackground(Settings.COLOR_BACKGROUND_3);
                    searchField.addActionListener(e -> {
                        ResponseEntity res = Application.send(new RequestEntity("/user/friend", StringUtils.map(
                                "id",CustomerProfile.getUserId(), "username",searchField.getText()
                        ), CustomerProfile.getToken()));
                        loadList();
                        searchField.setText("");
                    });
                    colorPanel.add(searchField, BorderLayout.CENTER);
                }
            }

            add(searchPanel, BorderLayout.NORTH);
        }
        {

            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.add(Box.createVerticalGlue());

            this.listPanel = listPanel;

            JScrollPane scrollPane = new JScrollPane(listPanel,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getVerticalScrollBar().setBackground(Settings.COLOR_BACKGROUND_1);
            scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, Integer.MAX_VALUE));
            add(scrollPane, BorderLayout.CENTER);

            this.scrollPane = scrollPane;


            List<Integer> friendList = new ArrayList<>();

            ResponseEntity res = Application.send(new RequestEntity("/user/friendlist", StringUtils.map("id",CustomerProfile.getUserId()), CustomerProfile.getToken()));

            ArrayNode node = (ArrayNode) new ObjectMapper().valueToTree(res.getPayload()).get("list");
            node.forEach(friend->{
                friendList.add(friend.get("friendId").asInt());
            });

            friendList.forEach(friend->{
                addListItem(friend);
            });

        }
        {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(1, 0));
            panel.setBackground(Settings.COLOR_TRANSPARENT);
            add(panel, BorderLayout.WEST);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Settings.COLOR_BACKGROUND_5);
        g2d.setStroke(new BasicStroke(0.4f));
        int arc = 15;
        int x = 0;
        int y = 0;
        int w = getWidth();
        int h = getHeight();
        g2d.drawArc(x, y, arc * 2, arc * 2, 90, 90);
        g2d.drawLine(x + arc, y, w, y);
        g2d.drawLine(x, y + arc, x, h);
    }

    private JPanel getListChildPanel(int userId) {
        ListComponent childPanel = new ListComponent(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (CustomerProfile.messageObject != null && CustomerProfile.messageObject == userId){
                    setBackground(Settings.COLOR_BACKGROUND_7);
                }else if (hover)setBackground(Settings.COLOR_BACKGROUND_4);
                else setBackground(Settings.COLOR_BACKGROUND_1);
            }
        };

        childPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        childPanels.add(childPanel);

        childPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CustomerProfile.messageObject = userId;
                refreshAllChildPanels();
                TitlePanel.getInstance().loadUsernameAsync();
                MessagePanel.getInstance().loadMessage();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                childPanel.hover = true;
                refreshAllChildPanels();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                childPanel.hover = false;
                refreshAllChildPanels();
            }
        });
        childPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        childPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        User user;
        {
            ResponseEntity response = Application.send(new RequestEntity("/user/get",StringUtils.map("id",userId), CustomerProfile.getToken()));
            JsonNode n = new ObjectMapper().valueToTree(response.getPayload());
            try {
                user = new ObjectMapper().treeToValue(n.get("user"),User.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            ResponseEntity res = Application.send(new RequestEntity("/image/download", StringUtils.map("fileName", user.getAvatar()), CustomerProfile.getToken()));
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
                    g2.fill(new RoundRectangle2D.Float(0, 0, size, size, 50, 50));
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
            childPanel.add(avatar, BorderLayout.WEST);
        }
        {
            JPanel msg = new JPanel();
            msg.setBackground(Settings.COLOR_TRANSPARENT);
            msg.setLayout(new BoxLayout(msg, BoxLayout.Y_AXIS));
            msg.add(Box.createVerticalStrut(8));
            JLabel name = new JLabel(user.getNickname());
            name.setForeground(Color.WHITE);
            name.setFont(Settings.FONT_MICROSOFT_YaHei.deriveFont(15f));
            msg.add(name);
            childPanel.add(msg, BorderLayout.CENTER);
        }
        return childPanel;
    }
    private void addListItem(int userId) {
        int insertPosition = listPanel.getComponentCount() - 1;
        listPanel.add(getListChildPanel(userId), insertPosition);
        refreshLayout();
    }

    public void loadList() {
        List<Integer> friendList = new ArrayList<>();

        ResponseEntity res = Application.send(new RequestEntity("/user/friendlist", StringUtils.map("id",CustomerProfile.getUserId()), CustomerProfile.getToken()));

        ArrayNode node = (ArrayNode) new ObjectMapper().valueToTree(res.getPayload()).get("list");
        node.forEach(friend->{
            friendList.add(friend.get("friendId").asInt());
        });

        friendList.forEach(friend->{
            addListItem(friend);
        });

        listPanel.removeAll();
        listPanel.add(Box.createVerticalGlue());

        friendList.forEach(friend->{
            addListItem(friend);
        });

    }

    private void refreshLayout() {
        listPanel.revalidate();
        listPanel.repaint();
        scrollPane.revalidate();
    }
    private void refreshAllChildPanels() {
        for (JPanel panel : childPanels) {
            panel.repaint();
        }
    }

    class ListComponent extends JPanel {
        public ListComponent(LayoutManager layout) {
            super(layout);
        }
        public boolean hover = false;
    }

}
