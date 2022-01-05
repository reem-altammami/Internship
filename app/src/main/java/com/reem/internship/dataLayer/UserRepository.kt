package com.reem.internship.dataLayer

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.reem.internship.data.BookMarkResponse
import com.reem.internship.data.UserResponseModel
import com.reem.internship.model.BookMark
import com.reem.internship.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDataSource: UserDataSource) {
    private fun getCurrentUserID()=FirebaseAuth.getInstance().currentUser?.uid?:""
    suspend fun putUserData(user: User) = userDataSource.putUserData(user)

    suspend fun  getUserData(): Flow<User> = userDataSource.getUserData(getCurrentUserID())

    suspend fun getBookmark():List<BookMarkResponse> = userDataSource.getBooKmark(getCurrentUserID())

    suspend fun addTrainingToBookmark(training:List<BookMark>)= userDataSource.addTrainingToBookmark(getCurrentUserID(),training)


}