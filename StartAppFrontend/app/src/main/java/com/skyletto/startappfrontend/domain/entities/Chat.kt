package com.skyletto.startappfrontend.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.domain.entities.Message

@Entity(tableName = "chats")
data class Chat(
        @ColumnInfo(name = "avatar_id") val avatarId: Int = R.drawable.ic_profile_image,
        @Embedded val message: Message,
        @PrimaryKey
        @ColumnInfo(name = "chat_id")
        val chatId: Long,
        @ColumnInfo(name = "chat_name") val chatName: String
)
