package com.skyletto.startappbackend.entities.responses;

import com.skyletto.startappbackend.entities.User;

public class ProfileResponse {
    private String token;
    private User user;

    public ProfileResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
