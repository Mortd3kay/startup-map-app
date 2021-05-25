package com.skyletto.startappfrontend.ui.main.fragments

import com.skyletto.startappfrontend.common.models.AlertModel
import java.util.function.Predicate

interface OnConditionUpdateListener {
    fun update(predicates: Array<Predicate<AlertModel>?>)
}