package com.reem.internship.dataLayer

import com.reem.internship.data.User

class UserRepository(private val userDataSource: UserDataSource) {
    suspend fun putUserData(): User = User()
    suspend fun  getUserData(): User = userDataSource.getUserData()
    suspend fun updateUseDat():User = User()
}