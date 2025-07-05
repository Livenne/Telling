package com.livenne.model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livenne.utils.StringUtils;

import java.util.UUID;

public class RequestEntity {
    private String url;
    private String requestId;
    private Object data;
    private String token;

    public RequestEntity(String url, Object data, String token) {
        this.url = url;
        this.data = data;
        this.token = token;
        this.requestId = UUID.randomUUID().toString();
    }

    public static RequestEntity json(String json) {
        try {
            return new ObjectMapper().readValue(json, RequestEntity.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public RequestEntity() {
        this(null, null, null);
    }

    public String getUrl() {
        return url;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return StringUtils.toJson(this);
    }
}
