package com.reem.internship.dataLayer

import com.reem.internship.data.BookMarkResponse
import com.reem.internship.data.UserResponseModel
import com.reem.internship.model.BookMark
import com.reem.internship.model.User
import kotlinx.coroutines.flow.Flow


interface UserDataSource {


    suspend fun getUserData(id:String): Flow<User>
    suspend fun putUserData(user: User)
    suspend fun addTrainingToBookmark(userId:String,training: List<BookMark>)
suspend fun getBooKmark(userId: String) :List<BookMarkResponse>
}