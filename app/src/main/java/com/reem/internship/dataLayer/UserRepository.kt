package com.reem.internship.dataLayer

import com.reem.internship.data.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDataSource: UserDataSource) {
    suspend fun putUserData(): User = User()
    suspend fun  getUserData(): Flow<User> = userDataSource.getUserData()
    suspend fun updateUseDat():User = User()
}