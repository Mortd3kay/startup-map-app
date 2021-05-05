package com.skyletto.startappfrontend.domain.entities

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Message (
    p_id: Long,
    p_text: String,
    p_time: String,
    p_checked: Boolean,
    p_sender: Long,
    p_receiver: Long,

){
    val id: Long = p_id
    val text: String = p_text
    val time: String = LocalDateTime.parse(p_time).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
    @SerializedName("checked")
    val isChecked: Boolean = p_checked
    @SerializedName("sender_id")
    val senderId: Long = p_sender
    @SerializedName("receiver_id")
    val receiverId: Long = p_receiver
}