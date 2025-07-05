package com.livenne.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.livenne.Application.connection;

public class CustomerProfile {

    private static final String SQL_TOKEN_QUERY = "SELECT * FROM token";
    private static final String SQL_USER_QUERY = "SELECT * FROM user";
    private static final String SQL_TOKEN_SET = "INSERT OR REPLACE INTO token (id,token) VALUES (?,?)";
    private static final String SQL_USER_SET = "INSERT OR REPLACE INTO user (id,username,nickname,gender,signature,avatar) VALUES (?,?,?,?,?,?)";
    private static final String SQL_USER_CLEAR = "DELETE FROM user;";
    public static Integer messageObject;

    public static String getToken() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_TOKEN_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("token");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public static void setToken(String token) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_TOKEN_SET);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, token);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setAttribute(String column, String value) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET "+column+"=?");
            preparedStatement.setString(1, value);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getUserId() {
        String token = getToken();

        String json = new String(
                Base64.getUrlDecoder().decode(token.split("\\.")[1]),
                StandardCharsets.UTF_8
        );
        try {
            return new ObjectMapper().readTree(json).get("iss").asInt();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getAttribute(String column) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_USER_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static void createUser(User user) {
        clearUser();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_USER_SET);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getNickname());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getSignature());
            preparedStatement.setString(6, user.getAvatar());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void clearUser() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_USER_CLEAR);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMessage(Message message) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (message,sender,receiver,timestamp) VALUES (?,?,?,?)");
            statement.setString(1, message.getMessage());
            statement.setInt(2, message.getSender());
            statement.setInt(3, message.getReceiver());
            statement.setLong(4, message.getTimestamp());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Message> getMessage(int friendId) {
        List<Message> messageList = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY timestamp ASC;";
        int userId = getUserId();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            statement.setInt(3, friendId);
            statement.setInt(4, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message(resultSet.getString("message"),
                        resultSet.getInt("sender"),
                        resultSet.getInt("receiver"),
                        resultSet.getLong("timestamp")
                );
                messageList.add(message);
            }
            return messageList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
