package com.skyletto.startappfrontend.common.models

import androidx.databinding.ObservableField
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.ProjectRole

class ProjectRoleItem {
    var project: Project? = null
    val role: ObservableField<ProjectRole> = ObservableField()
    val isSalary: ObservableField<Boolean> = ObservableField(false)
    val salaryType: ObservableField<Char> = ObservableField('%')
    val salaryAmount: ObservableField<Double> = ObservableField(0.0)
}
