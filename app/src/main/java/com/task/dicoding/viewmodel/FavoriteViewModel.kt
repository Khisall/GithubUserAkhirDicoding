package com.task.dicoding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.task.dicoding.database.FavoriteRepository
import com.task.dicoding.database.FavoriteUser

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    val favoriteUsers: LiveData<List<FavoriteUser>> = repository.favoriteUsers

}