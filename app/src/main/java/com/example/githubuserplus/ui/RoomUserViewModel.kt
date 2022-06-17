package com.example.githubuserplus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserplus.data.UserRepository
import com.example.githubuserplus.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class RoomUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getRoomUser(query: String) = userRepository.getRoomUser(query)

    fun getStarredUser() = userRepository.getStarredUser()

    fun saveUser(user: UserEntity){
        viewModelScope.launch {
            userRepository.setUserStarred(user, true)
        }
    }

    fun deleteUser(user: UserEntity){
        viewModelScope.launch {
            userRepository.setUserStarred(user, false)
        }
    }
}