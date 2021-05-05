package com.skyletto.startappfrontend.common

import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.domain.entities.Message

data class ChatItem(val avatarId: Int = R.drawable.ic_testing, val message: Message, val chatId: Long)
