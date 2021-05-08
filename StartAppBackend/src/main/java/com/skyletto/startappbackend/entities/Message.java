package com.skyletto.startappbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Time;
import java.time.*;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    private String time;
    private boolean checked;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id",nullable = false)
    private User senderId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiverId;

    public Message(String text, String time, User senderId, User receiverId) {
        this.text = text;
        this.time = time;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public Message() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @JsonProperty("time")
    public String getTime() {
        if (time!=null)
            return time;
        return "";
    }

    @JsonProperty("time")
    public void setTime(String time) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, zoneId);
        this.time = ldt.toString();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setSenderId(User senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(User receiverId) {
        this.receiverId = receiverId;
    }

    @JsonProperty("sender_id")
    public void setSenderId(long id){
        senderId = new User();
        senderId.setId(id);
    }

    @JsonProperty("sender_id")
    public long getSenderId(){
        return senderId.getId();
    }

    @JsonProperty("receiver_id")
    public void setReceiverId(long id){
        receiverId = new User();
        receiverId.setId(id);
    }

    @JsonProperty("receiver_id")
    public long getReceiverId(){
        return receiverId.getId();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", time=" + time +
                ", checked=" + checked +
                ", senderId=" + senderId.getId() +
                ", receiverId=" + receiverId.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id && checked == message.checked && Objects.equals(text, message.text) && Objects.equals(time, message.time) && Objects.equals(senderId, message.senderId) && Objects.equals(receiverId, message.receiverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, time, checked, senderId, receiverId);
    }
}
