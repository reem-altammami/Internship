package com.reem.internship.dataLayer

import android.util.Log
import com.reem.internship.data.UserResponseModel
import com.reem.internship.model.BookMark
import com.reem.internship.model.User

import com.reem.internship.network.CompanyApi
import com.reem.internship.network.CompanyApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import kotlin.io.path.Path

class UserRemoteDataSource(var api: CompanyApiService, var ioDispatcher: CoroutineDispatcher) :
    UserDataSource {
    override suspend fun putUserData(user: User) {
        Log.d("TAG", "putUserData: ${user.toString()}")
        CompanyApi.retrofitService.putUserData(user.id,user)

    }



    override suspend fun getUserData(id:String) = flow {

        emit(CompanyApi.retrofitService.getUserApi(id))
    }

    override suspend fun addTrainingToBookmark(userId:String,training: BookMark) {
        CompanyApi.retrofitService.addTrainingToBookmark(userId,training)
    }

    override suspend fun getBooKmark(userId: String) = flow {
        emit(CompanyApi.retrofitService.getBookMark(userId))
    }


}