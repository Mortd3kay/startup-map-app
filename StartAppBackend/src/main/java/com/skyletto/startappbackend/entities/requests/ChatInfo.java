package com.skyletto.startappbackend.entities.requests;

public class ChatInfo {
    private long friendId;

    public ChatInfo() {
    }

    public ChatInfo(long friendId) {
        this.friendId = friendId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }
}
