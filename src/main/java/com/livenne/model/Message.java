package com.livenne.model;


import com.livenne.utils.StringUtils;

public class Message {
    private String message;
    private int sender;
    private int receiver;
    private long timestamp;
    public Message(String message, int sender, int receiver, long timestamp) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }
    public Message(String message, int sender, int receiver) {
        this(message, sender, receiver, System.currentTimeMillis());
    }
    public Message() {
        this(null, -1, -1);
    }

    public String getMessage() {
        return message;
    }
    public Message setMessage(String message) {
        this.message = message;
        return this;
    }
    public int getSender() {
        return sender;
    }
    public Message setSender(int sender) {
        this.sender = sender;
        return this;
    }
    public int getReceiver() {
        return receiver;
    }
    public Message setReceiver(int receiver) {
        this.receiver = receiver;
        return this;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public Message setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public String toString() {
        return StringUtils.toJson(this);
    }
}
