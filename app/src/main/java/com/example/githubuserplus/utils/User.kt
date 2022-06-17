package com.example.githubuserplus.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String,
    var name: String,
    var photo: String,
    var repository: String,
    var followers: String,
    var following: String,
    var company: String,
    var location: String,
    var url: String,
    var isBookmarked: Boolean
) : Parcelable
