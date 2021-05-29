package com.skyletto.startappfrontend.common.models

data class RecommendationItem(
        val id: Long,
        val userId:Long,
        val username:String,
        val title:String?,
        val subtitle:String?,
        val isProject: Boolean
)