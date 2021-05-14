package com.skyletto.startappfrontend.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "projects_roles")
data class ProjectAndRole (
        @PrimaryKey var id: Long = 0,
        var project: Project? = null,
        var role: ProjectRole? = null,
        var user: User? = null,
        var isSalary: Boolean = false,
        var salaryType: Char = '%',
        var salaryAmount: Double = 0.0
) {
    constructor(project: Project?, role: ProjectRole?, user: User?, hasSalary: Boolean, salaryType: Char, salaryAmount: Double) : this(0, project, role, user, hasSalary, salaryType, salaryAmount)
    constructor(project: Project?, role: ProjectRole?):this(0, project, role)
}
