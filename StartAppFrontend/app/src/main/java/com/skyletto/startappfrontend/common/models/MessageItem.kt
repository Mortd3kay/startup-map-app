package com.skyletto.startappfrontend.common.models

data class MessageItem(
        val text: String,
        val time: String,
        val isChecked: Boolean,
        val senderId: Long,
        val receiverId: Long,
        val outcoming: Boolean
)