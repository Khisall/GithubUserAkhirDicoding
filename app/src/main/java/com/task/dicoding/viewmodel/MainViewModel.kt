package com.task.dicoding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.task.dicoding.adapter.SettingPreferences
import com.task.dicoding.data.response.ResponseSearch
import com.task.dicoding.data.response.User
import com.task.dicoding.data.retrofit.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    private val apiService: ApiService
    private var lastQuery: String = ""

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun searchUser(query: String) {
        if (query != lastQuery) {
            _loading.value = true
            val call = apiService.searchUsername(query)
            call.enqueue(object : Callback<ResponseSearch> {
                override fun onResponse(
                    call: Call<ResponseSearch>,
                    response: Response<ResponseSearch>
                ) {
                    _loading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val users = responseBody.items
                            _userList.value = users
                        }
                    } else {
                        // Handle kesalahan jika ada
                    }
                }

                override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                    _loading.value = false
                    // Handle kesalahan jika ada
                }
            })
            lastQuery = query
        }
    }

    companion object

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(preferences) as T
    }
}