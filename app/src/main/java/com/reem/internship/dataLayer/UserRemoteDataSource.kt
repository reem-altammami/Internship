package com.reem.internship.dataLayer

import com.reem.internship.data.User
import com.reem.internship.network.CompanyApi

class UserRemoteDataSource (): UserDataSource{
    override suspend fun putUserData(): User {
//CompanyApi.retrofitService.putUserData()
        return User()
    }
}