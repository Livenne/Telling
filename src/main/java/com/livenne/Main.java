package com.livenne;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.livenne.component.BorderContainer;
import com.livenne.panels.ContentPanel;
import com.livenne.panels.FunctionPanel;
import com.livenne.panels.MainPanel;
import com.livenne.panels.TitlePanel;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::window);

    }
    private static void window(){
        JFrame frame = new JFrame();
        frame.setSize(1300,720);
        ImageIcon flatSVGIcon = new FlatSVGIcon("static/icon.svg");
        frame.setIconImage(flatSVGIcon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0,0,0,0));
        {
            MainPanel mainPanel = new MainPanel();
            frame.setContentPane(mainPanel);

            mainPanel.add(new TitlePanel(frame),BorderLayout.NORTH);

            mainPanel.add(
                    new BorderContainer(new FunctionPanel(),8)
                            .up(0)
                            .getPanel()
                    , BorderLayout.WEST);

            JPanel tempPanel = new JPanel(new BorderLayout());
            tempPanel.setBackground(Settings.COLOR_TRANSPARENT);
            mainPanel.add(tempPanel, BorderLayout.CENTER);

            tempPanel.add(new ContentPanel(), BorderLayout.CENTER);

        }

        frame.setVisible(true);
    }
}