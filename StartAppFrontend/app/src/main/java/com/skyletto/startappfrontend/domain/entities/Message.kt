package com.skyletto.startappfrontend.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity(tableName = "messages")
data class Message (
        @PrimaryKey val id: Long?,
        val text: String,
        val time: String,
        @SerializedName("checked")
        val isChecked: Boolean,
        @SerializedName("sender_id")
        val senderId: Long,
        @SerializedName("receiver_id")
        val receiverId: Long
){
        constructor(text: String, time: String, senderId: Long, receiverId: Long): this(null, text, time, false, senderId, receiverId)
}