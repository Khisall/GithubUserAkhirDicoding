package com.task.dicoding.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_users")
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    val username: String = "",

    val name: String? = null,

    val avatarUrl: String? = null,

)