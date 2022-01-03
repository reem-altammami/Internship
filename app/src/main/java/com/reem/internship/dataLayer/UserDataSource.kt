package com.reem.internship.dataLayer

import com.reem.internship.data.User
import kotlinx.coroutines.flow.Flow


interface UserDataSource {

    suspend fun putUserData(): User
    suspend fun getUserData(): Flow<User>
    suspend fun updateUserData(): User
}