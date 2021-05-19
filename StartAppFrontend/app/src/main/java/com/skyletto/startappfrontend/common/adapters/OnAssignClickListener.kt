package com.skyletto.startappfrontend.common.adapters

import com.skyletto.startappfrontend.common.models.UserItem
import com.skyletto.startappfrontend.domain.entities.ProjectAndRole

interface OnAssignClickListener {
    fun assign(role: ProjectAndRole, userItem: UserItem?)
}