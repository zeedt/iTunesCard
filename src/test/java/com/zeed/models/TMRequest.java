package com.zeed.models;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@JsonFilter("sensitiveDataFilter")
public class TMRequest {

    private static final Logger logger = Logger.getLogger(TMRequest.class.getName());

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String request;
    private String pan;
    private RoutingInformation routingInformation;

    static {
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public RoutingInformation getRoutingInformation() {
        return routingInformation;
    }

    public void setRoutingInformation(RoutingInformation routingInformation) {
        this.routingInformation = routingInformation;
    }

    public <T> T getRequest(Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(request, clazz);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
