package com.task.dicoding.database

import androidx.lifecycle.LiveData

class FavoriteRepository(private val favoriteUserDao: FavoriteUserDao) {

    val favoriteUsers: LiveData<List<FavoriteUser>> = favoriteUserDao.getAllFavoriteUsers()
}