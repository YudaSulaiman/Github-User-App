package com.example.githubuserplus.di

import android.content.Context
import com.example.githubuserplus.data.UserRepository
import com.example.githubuserplus.data.local.room.UserDatabase
import com.example.githubuserplus.data.remote.retrofit.ApiConfigRoom
import com.example.githubuserplus.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfigRoom.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}