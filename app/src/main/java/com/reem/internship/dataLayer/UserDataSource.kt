package com.reem.internship.dataLayer

import com.reem.internship.data.User


interface UserDataSource {

    suspend fun putUserData(): User
    suspend fun getUserData(): User
}