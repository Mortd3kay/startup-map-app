package com.skyletto.startappfrontend.common.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.domain.entities.User

data class UserWithTags(
        @Embedded val user:User,
        @Relation(
                parentColumn = "uId",
                entity = Tag::class,
                entityColumn = "tId",
                associateBy = Junction(
                        value = UserTags::class,
                        parentColumn = "userId",
                        entityColumn = "tagId"
                )
        )
        val tags: Set<Tag>
)
