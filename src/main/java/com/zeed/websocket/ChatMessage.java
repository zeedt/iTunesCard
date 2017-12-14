package com.zeed.websocket;

/**
 * Created by longbridge on 12/11/17.
 */
public class ChatMessage {
    public String message;
    public String cardId;
    public String userRole;

    public ChatMessage() {
    }

    public ChatMessage(String message, String cardId, String userRole) {
        this.message = message;
        this.cardId = cardId;
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", cardId='" + cardId + '\'' +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}
