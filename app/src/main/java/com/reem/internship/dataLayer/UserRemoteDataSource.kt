package com.reem.internship.dataLayer

import android.util.Log
import com.reem.internship.data.BookMarkResponse
import com.reem.internship.data.UserResponseModel
import com.reem.internship.model.BookMark
import com.reem.internship.model.User

import com.reem.internship.network.CompanyApi
import com.reem.internship.network.CompanyApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Flow
import kotlin.io.path.Path

class UserRemoteDataSource(var api: CompanyApiService, var ioDispatcher: CoroutineDispatcher) :
    UserDataSource {
    override suspend fun putUserData(user: User) {
        Log.d("TAG", "putUserData: ${user.toString()}")
        CompanyApi.retrofitService.putUserData(user.id, user)

    }


    override suspend fun getUserData(id: String) = flow {

      emit(CompanyApi.retrofitService.getUserApi(id).body()?:User())

    }

    override suspend fun updateBookmark(userId: String, training: List<BookMarkResponse>) {

        CompanyApi.retrofitService.updateBookmark(userId, training)
    }

    override suspend fun getBooKmark(userId: String): List<BookMarkResponse> =
        withContext(ioDispatcher) {
            api.getBookMark(userId).body()?: emptyList()
        }

    override suspend fun deleteBookmark(userId: String, training: List<BookMarkResponse>) {
        CompanyApi.retrofitService.deleteTrainingFromBookmark(userId,training)
    }
}




