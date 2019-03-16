package com.github.watabee.hackernews.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "stories"
)
data class Story(
    @PrimaryKey val id: Long,
    val by: String,
    val time: Long,
    val title: String,
    val url: String?,
    val text: String?,
    val score: Int,
    val updatedAt: Long
)