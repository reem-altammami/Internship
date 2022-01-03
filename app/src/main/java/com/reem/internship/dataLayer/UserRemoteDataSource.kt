package com.reem.internship.dataLayer

import com.reem.internship.data.City
import com.reem.internship.data.Major
import com.reem.internship.data.User
import com.reem.internship.network.CompanyApi
import com.reem.internship.network.CompanyApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.concurrent.Flow

class UserRemoteDataSource(var api: CompanyApiService, var ioDispatcher: CoroutineDispatcher) :
    UserDataSource {
    override suspend fun putUserData(): User {
//CompanyApi.retrofitService.putUserData()
        return User()
    }

    override suspend fun getUserData(): kotlinx.coroutines.flow.Flow<User> = flow {
 com.reem.internship.network.CompanyApi.retrofitService.getUserApi("0") }
        emit(g.await())
    }


    override suspend fun updateUserData(): User {
        TODO("Not yet implemented")
    }
}