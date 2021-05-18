package com.skyletto.startappfrontend.common.adapters

import com.skyletto.startappfrontend.domain.entities.Project

interface OnDeleteProjectListener {
    fun delete(project: Project)
}