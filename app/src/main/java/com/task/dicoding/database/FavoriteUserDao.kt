package com.task.dicoding.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteUser(user: FavoriteUser)

    @Delete
    suspend fun deleteFavoriteUser(user: FavoriteUser)

    @Query("SELECT * FROM favorite_users WHERE username = :username")
    fun getFavoriteUser(username: String): LiveData<FavoriteUser?>

    @Query("SELECT * FROM favorite_users")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_users WHERE username = :username)")
    fun isUserFavorite(username: String): LiveData<Boolean>

}
