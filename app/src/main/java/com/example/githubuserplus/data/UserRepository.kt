package com.example.githubuserplus.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubuserplus.data.local.entity.UserEntity
import com.example.githubuserplus.data.local.room.UserDao
import com.example.githubuserplus.data.remote.retrofit.ApiServiceRoom
import com.example.githubuserplus.utils.AppExecutors

class UserRepository private constructor(
    private val apiService: ApiServiceRoom,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors
) {

    fun getRoomUser(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUser(USERS + query)
            val items = response.items
            val userList = items.map { user ->
                val isStarred = userDao.isUserStarred(user.login)
                UserEntity(
                    user.login,
                    user.login,
                    user.url,
                    user.avatarUrl,
                    user.followersUrl,
                    user.followingUrl,
                    isStarred
                )
            }
            userDao.deleteAll()
            userDao.insertUser(userList)
        } catch (e: Exception){
            Log.d("UserRepository", "getRoomUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = userDao.getUser("%$query%").map { Result.Success(it) }
        emitSource(localData)
    }

    fun getStarredUser(): LiveData<List<UserEntity>> {
        return userDao.getStarredUser()
    }

    suspend fun setUserStarred(user: UserEntity, starredState: Boolean) {
        user.isStarred = starredState
        userDao.updateUser(user)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiServiceRoom,
            userDao: UserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, appExecutors)
            }.also { instance = it }

        private const val USERS = "search/users?q="
    }
}