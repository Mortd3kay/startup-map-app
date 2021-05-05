package com.skyletto.startappbackend.entities.requests;

public class ChatRequest {
    private long friendId;

    public ChatRequest() {
    }

    public ChatRequest(long friendId) {
        this.friendId = friendId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }
}
