package com.skyletto.startappfrontend.common.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.ProjectAndRole
import com.skyletto.startappfrontend.domain.entities.Tag

data class ProjectWithTagsAndRoles(
        @Embedded val project: Project,
        @Relation(
                parentColumn = "pId",
                entity = Tag::class,
                entityColumn = "tId",
                associateBy = Junction(
                        value = ProjectTags::class,
                        parentColumn = "projectId",
                        entityColumn = "tagId"
                )
        )
        val tags: Set<Tag>? = null,
        @Relation(
                parentColumn = "pId",
                entity = ProjectAndRole::class,
                entityColumn = "prId",
                associateBy = Junction(
                        value = ProjectRoles::class,
                        parentColumn = "projectId",
                        entityColumn = "roleId"
                )
        )
        val roles: List<ProjectAndRole>? = null
)
