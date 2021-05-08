package com.skyletto.startappfrontend.common

import com.google.gson.annotations.SerializedName

data class MessageItem(
        val text: String,
        val time: String,
        val isChecked: Boolean,
        val senderId: Long,
        val receiverId: Long,
        val outcoming: Boolean
)