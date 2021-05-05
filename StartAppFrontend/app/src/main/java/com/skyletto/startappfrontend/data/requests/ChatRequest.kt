package com.skyletto.startappfrontend.data.requests

data class ChatRequest(val friendId: Long) {
    constructor() : this(0)
}