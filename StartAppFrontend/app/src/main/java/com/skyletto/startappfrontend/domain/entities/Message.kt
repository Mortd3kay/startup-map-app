package com.skyletto.startappfrontend.domain.entities

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Message (
        val id: Long,
        val text: String,
        val time: String,
        @SerializedName("checked")
        val isChecked: Boolean,
        @SerializedName("sender_id")
        val senderId: Long,
        @SerializedName("receiver_id")
        val receiverId: Long
)