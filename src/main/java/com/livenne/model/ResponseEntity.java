package com.livenne.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livenne.utils.StringUtils;

public class ResponseEntity {

    private String type;
    private String requestId;
    private Object payload;

    public ResponseEntity(String type, String requestId, Object payload) {
        this.type = type;
        this.requestId = requestId;
        this.payload = payload;
    }
    public ResponseEntity() {}

    public static ResponseEntity json(String json) {
        try {
            return new ObjectMapper().readValue(json, ResponseEntity.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getType() {
        return type;
    }

    public ResponseEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public ResponseEntity setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public Object getPayload() {
        return payload;
    }

    public ResponseEntity setPayload(Object payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public String toString() {
        return StringUtils.toJson(this);
    }
}
