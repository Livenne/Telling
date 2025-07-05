package com.livenne;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livenne.model.CustomerProfile;
import com.livenne.model.Message;
import com.livenne.model.RequestEntity;
import com.livenne.model.ResponseEntity;
import com.livenne.panel.MessagePanel;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.*;

public class Application {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;
    private static InputStream in;
    private static OutputStream out;
    private static final ConcurrentHashMap<String, BlockingQueue<ResponseEntity>> responsePool = new ConcurrentHashMap<>();

    private static String DATABASE_PATH = "E:\\Code\\Learn\\Telling\\src\\main\\resources\\data.db";
    private static final String DATABASE_SQL = "E:\\Code\\Learn\\Telling\\src\\main\\resources\\data.sql";
    public static Connection connection;

    private static final int HEARTBEAT_INTERVAL = 15000;
    private static final String HEARTBEAT_PATH = "/sys/heartbeat";

    public static void run(String databasePath) {
        DATABASE_PATH = databasePath;
        internetService();
        databaseService();
        heartbeatService();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getOfflineMessage();

    }

    private static void internetService() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Socket socket = new Socket(ADDRESS, PORT);
                    in = socket.getInputStream();
                    out = socket.getOutputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        ResponseEntity response = ResponseEntity.json(line);
                        String type = response.getType();
                        String requestId = response.getRequestId();

                        if ("response".equals(type)) {
                            BlockingQueue<ResponseEntity> queue = responsePool.get(requestId);
                            if (queue != null) {
                                queue.put(response);
                                responsePool.remove(requestId);
                            }
                        } else if ("push".equals(type)) {
                            receive(response);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        System.err.println("Failed to connect to server...");
                        Thread.sleep(3000);
                        System.err.println("Reconnect server...");
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        thread.setDaemon(false);
        thread.start();
    }
    private static void databaseService() {
        Thread thread = new Thread(() -> {
            try {
                connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", DATABASE_PATH));
                try (Scanner scanner = new Scanner(Files.newInputStream(Path.of(DATABASE_SQL)),StandardCharsets.UTF_8)) {
                    scanner.useDelimiter(";\\s*");
                    while (scanner.hasNext()) {
                        String sql = scanner.next().trim();
                        if (!sql.isEmpty()) {
                            try (Statement stmt = connection.createStatement()) {
                                stmt.execute(sql);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static void heartbeatService() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                send(new RequestEntity(HEARTBEAT_PATH,null,null));
            }
        }, HEARTBEAT_INTERVAL);
    }

    public static void getOfflineMessage() {
        if (CustomerProfile.getToken() == null) return;
        ResponseEntity res = send(new RequestEntity("/chat/offline", null, CustomerProfile.getToken()));
        if (new ObjectMapper().valueToTree(res.getPayload()).has("messages")) {
            String json = new ObjectMapper().valueToTree(res.getPayload()).get("messages").toString();
            try {
                List<Message> messageList = new ObjectMapper().readValue(json, new TypeReference<>() {});
                messageList.stream().forEach(CustomerProfile::addMessage);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ResponseEntity send(RequestEntity request){
        try {String requestId = request.getRequestId();

            responsePool.put(requestId, new LinkedBlockingQueue<>());

            synchronized (out) {
                out.write((request + "\n").getBytes(StandardCharsets.UTF_8));
                out.flush();
            }

            ResponseEntity response = responsePool.get(requestId).poll(5, TimeUnit.SECONDS);
            responsePool.remove(requestId);
            if (response == null) {
                System.err.println("No response to push");
            }

            return response;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseEntity send(String url, Object data, String token) {
        return send(new RequestEntity(url,data,token));
    }

    public static ResponseEntity send(String url, Object data) {
        return send(url,data,CustomerProfile.getToken());
    }

    public static void receive(ResponseEntity response) {
        try {
            Message message = new ObjectMapper().treeToValue(new ObjectMapper().valueToTree(response.getPayload()).get("message"), Message.class);
            CustomerProfile.addMessage(message);
            if (CustomerProfile.messageObject != null && CustomerProfile.messageObject == message.getSender()) {
                MessagePanel.getInstance().loadMessage();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
