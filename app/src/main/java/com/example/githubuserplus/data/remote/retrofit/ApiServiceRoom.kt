package com.example.githubuserplus.data.remote.retrofit

import com.example.githubuserplus.BuildConfig
import com.example.githubuserplus.data.remote.response.UserResponse
import retrofit2.http.*

interface ApiServiceRoom {
    @Headers("Authorization: ${BuildConfig.Authorization}")
    @GET
    suspend fun getUser(@Url url: String): UserResponse
}