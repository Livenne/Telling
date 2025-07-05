package com.livenne.model;

import com.livenne.utils.StringUtils;

public class User {
    private int id;
    private String username;
    private String nickname;
    private String gender;
    private String signature;
    private String avatar;

    public User() {}

    public User(String username, String nickname, String gender, String signature, String avatar) {
        this.username = username;
        this.nickname = nickname;
        this.gender = gender;
        this.signature = signature;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return StringUtils.toJson(this);
    }
}
