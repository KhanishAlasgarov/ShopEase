package com.khanish.shopease.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search")
data class RecentSearchItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String
)