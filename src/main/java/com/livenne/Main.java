package com.livenne;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.livenne.model.CustomerProfile;
import com.livenne.model.ResponseEntity;
import com.livenne.panel.SignInPanel;
import com.livenne.utils.Settings;
import com.livenne.utils.StringUtils;
import com.livenne.window.LoginWindow;
import com.livenne.window.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Application.run("E:\\Code\\Learn\\Telling\\src\\main\\resources\\data.db");
        FlatLaf.setup(new FlatDarkLaf());
        UIManager.put("Panel.background", Settings.COLOR_BACKGROUND_1);
        SwingUtilities.invokeLater(() -> {
            ResponseEntity res = Application.send("/account/verify", StringUtils.map("token", CustomerProfile.getToken()), null);
            JsonNode map = new ObjectMapper().valueToTree(res.getPayload());
            if (map.has("verify") && map.get("verify").asBoolean()) MainWindow.getInstance();
            else LoginWindow.getInstance();
        });


    }

}