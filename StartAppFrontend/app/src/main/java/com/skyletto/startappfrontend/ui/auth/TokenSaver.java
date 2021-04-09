package com.skyletto.startappfrontend.ui.auth;

public interface TokenSaver {
    void save(String token, long id);
}
