package com.example.test1.model;

import androidx.annotation.NonNull;

import java.util.Date;

public class UserInfo {
    private String email;
    private boolean isEmailConfirmed;
    private Date createdAt;

    public boolean isEmailConfirmed() {
        return isEmailConfirmed;
    }

    public String getEmail() {
        return email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
