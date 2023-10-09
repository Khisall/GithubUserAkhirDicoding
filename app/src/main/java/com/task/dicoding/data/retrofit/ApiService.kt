package com.task.dicoding.data.retrofit

import com.task.dicoding.data.response.ResponseSearch
import com.task.dicoding.data.response.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUsername(
        @Query("q") q: String
    ): Call<ResponseSearch>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<User>
}