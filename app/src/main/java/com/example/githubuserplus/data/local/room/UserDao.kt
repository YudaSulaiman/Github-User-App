package com.example.githubuserplus.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuserplus.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username LIKE :query")
    fun getUser(query: String): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user where starred = 1")
    fun getStarredUser(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM user WHERE starred = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username AND starred = 1)")
    suspend fun isUserStarred(username: String): Boolean
}