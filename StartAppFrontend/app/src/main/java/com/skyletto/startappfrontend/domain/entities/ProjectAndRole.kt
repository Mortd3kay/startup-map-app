package com.skyletto.startappfrontend.domain.entities

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "projects_roles")
data class ProjectAndRole (
        @ColumnInfo(name = "prId")
        @PrimaryKey var id: Long = 0,
        @Ignore var project: Project? = null,
        @Embedded var role: Role? = null,
        @Embedded var user: User? = null,
        @SerializedName("has_salary")
        var isSalary: Boolean = false,
        var salaryType: Char = '%',
        var salaryAmount: Double = 0.0
) {
    constructor(project: Project?, role: Role?, user: User?, hasSalary: Boolean, salaryType: Char, salaryAmount: Double) : this(0, project, role, user, hasSalary, salaryType, salaryAmount)
    constructor(project: Project?, role: Role?):this(0, project, role)
    constructor(id: Long, role: Role?, isSalary: Boolean, salaryType: Char, salaryAmount: Double):this(id, null, role, null, isSalary, salaryType, salaryAmount)

    override fun toString(): String {
        return "$role $user $isSalary $salaryAmount $salaryType"
    }
}
