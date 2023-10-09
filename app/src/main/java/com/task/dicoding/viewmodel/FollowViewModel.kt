package com.task.dicoding.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.task.dicoding.data.response.User
import com.task.dicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _follow = MutableLiveData<List<User>?>()
    private val _error = MutableLiveData<String>()

    val follow: LiveData<List<User>?> = _follow
    val error: LiveData<String> = _error

    fun fetchFollowers(username: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getUserFollowers(username)

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    Log.e(TAG, "Unsucces")
                    val users = response.body()
                    _follow.postValue(users)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                val errorMessage = "Error: ${t.message}"
                _error.postValue(errorMessage)
                // Tambahkan logging untuk debugging
                Log.e(TAG, "API Error: $errorMessage")
            }
        })
    }

    fun fetchFollowing(username: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getUserFollowing(username)

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    Log.e(TAG, "Unsucces")
                    val users = response.body()
                    _follow.postValue(users)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _error.postValue("Error: ${t.message}")
                Log.e("FollowViewModel", "API Error: ${t.message}", t)
            }
        })
    }
    companion object{
        private val TAG = FollowViewModel::class.java.simpleName
    }
}
