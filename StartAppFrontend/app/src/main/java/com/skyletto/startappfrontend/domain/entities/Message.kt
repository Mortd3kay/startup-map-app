package com.skyletto.startappfrontend.domain.entities

import android.util.Log
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

data class Message (
    val id: Long,
    val text: String,
    var time: String,
    val isChecked: Boolean,
    val senderId: Long,
    val receiverId: Long
){
    init {
        if (time.isNotEmpty()) {
            Log.d("TAG", "$time")
            val ldt = LocalDateTime.parse(time)
            val zoneId = ZoneId.systemDefault()
            time = ldt.atZone(zoneId).toLocalDateTime().format(DateTimeFormatter.ISO_TIME)
            Log.d("TAG", "$time")
        }
    }
    constructor() : this(0, "", "", false, 0, 0)
}