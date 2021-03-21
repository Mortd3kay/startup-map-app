package com.skyletto.startappfrontend.data.responses;

import com.skyletto.startappfrontend.domain.entities.User;

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
