package com.task.dicoding.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.dicoding.data.response.User
import com.task.dicoding.database.AppDatabase
import com.task.dicoding.database.FavoriteUser
import com.task.dicoding.database.FavoriteUserDao
import com.task.dicoding.ui.DetailUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteUserDao: FavoriteUserDao
    private val appDatabase: AppDatabase = AppDatabase.getDatabase(application)

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    private val _isLoading = MutableLiveData<Boolean>() // Tambahkan LiveData isLoading
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()

    private val apiService = DetailUser.ApiServiceFactory.create()

    fun fetchUserDetail(username: String) {
        _isLoading.value = true

        apiService.getUserDetail(username).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        _user.value = user
                    }
                    Log.d("USER", "$user")
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                    Log.d("USER", "Error : ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _errorMessage.value = "Error: ${t.message}"
                _isLoading.value = false
                Log.d("USER", "Error : ${t.message}")
            }
        })
    }

    init {
        favoriteUserDao = appDatabase.favoriteUserDao()
    }

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteUserDao.insertFavoriteUser(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteUserDao.deleteFavoriteUser(favoriteUser)
        }
    }

    fun getFavoriteUser(username: String): LiveData<FavoriteUser?> {
        return favoriteUserDao.getFavoriteUser(username)
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getAllFavoriteUsers()
    }
}