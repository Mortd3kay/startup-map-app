package com.skyletto.startappfrontend.ui.main.fragments

import com.skyletto.startappfrontend.common.models.RecommendationItem

interface RecommendationCallback {
    fun callback(list:List<RecommendationItem>)
}