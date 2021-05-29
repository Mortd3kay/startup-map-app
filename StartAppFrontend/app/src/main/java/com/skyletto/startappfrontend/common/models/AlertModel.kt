package com.skyletto.startappfrontend.common.models

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.domain.entities.Tag

data class AlertModel(
        var title:String = "",
        var subtitle:String = "",
        var subsubtitle:String = "",
        var description:String = "",
        var id: Long,
        var tags: Set<Tag>? = null,
        var isProject: Boolean,
        var chatId: Long? = null
){
    constructor(id: Long, isProject: Boolean):this("","", "", "", id, null, isProject, null)

}
