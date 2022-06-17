package com.example.githubuserplus.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName  = "user")
class UserEntity (
    @field:PrimaryKey
    @field:ColumnInfo(name = "username")
    val username: String,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "userUrl")
    val userUrl: String,

    @field:ColumnInfo(name = "photo")
    val photo: String,

    @field:ColumnInfo(name = "followers_url")
    val followers_url: String,

    @field:ColumnInfo(name = "following_url")
    val following_url: String,

    @field:ColumnInfo(name = "starred")
    var isStarred: Boolean
)
