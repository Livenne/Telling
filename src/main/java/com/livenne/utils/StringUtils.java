package com.livenne.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StringUtils {
    public static String toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static Map.Entry map(String key, Object value) {
        return new AbstractMap.SimpleEntry(key, value);
    }
    public static HashMap<String,Object> map(Object... obj) {
        HashMap<String,Object> map = new HashMap<>();
        for (int i = 0; i < obj.length; i+=2) {
            map.put(obj[i].toString(), obj[i+1]);
        }
        return map;
    }

    public static BufferedImage stringToImage(String base64String) {
        byte[] imageBytes = Base64.getDecoder().decode(base64String);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String imageToString(String imagePath) {
        byte[] imageBytes = null;
        try {
            imageBytes = Files.readAllBytes(Paths.get(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
