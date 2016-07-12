package com.bakatumu.bakatumu.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class User implements Serializable {
    String id, name, email, phone, apiKey, lastMessage, timestamp;
    int unreadCount;

    public User() {
    }

    public User(String id, String name, String email, String phone, String apiKey, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.apiKey = apiKey;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApiKey() { return apiKey; }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getLastMessage() { return lastMessage; }

    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
