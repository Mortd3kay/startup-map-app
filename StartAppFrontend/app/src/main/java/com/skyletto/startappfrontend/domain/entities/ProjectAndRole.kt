package com.skyletto.startappfrontend.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "projects_roles")
data class ProjectAndRole (
        @PrimaryKey var id: Long = 0,
        var project: Project? = null,
        var role: ProjectRole? = null,
        var user: User? = null,
        @SerializedName("has_salary")
        var isSalary: Boolean = false,
        var salaryType: Char = '%',
        var salaryAmount: Double = 0.0
) {
    constructor(project: Project?, role: ProjectRole?, user: User?, hasSalary: Boolean, salaryType: Char, salaryAmount: Double) : this(0, project, role, user, hasSalary, salaryType, salaryAmount)
    constructor(project: Project?, role: ProjectRole?):this(0, project, role)

    override fun toString(): String {
        return "$role $user $isSalary $salaryAmount $salaryType"
    }
}
