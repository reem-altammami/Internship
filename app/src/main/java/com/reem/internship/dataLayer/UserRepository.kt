package com.reem.internship.dataLayer

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.reem.internship.data.BookMarkResponse
import com.reem.internship.data.UserResponseModel
import com.reem.internship.model.BookMark
import com.reem.internship.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(private val userDataSource: UserDataSource) {
    private fun getCurrentUserID() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    suspend fun putUserData(user: User) = userDataSource.putUserData(user)

    suspend fun getUserData(): Flow<User> = userDataSource.getUserData(getCurrentUserID())

    suspend fun getBookmark(): List<BookMarkResponse> =
        userDataSource.getBooKmark(getCurrentUserID())

    suspend fun addTrainingToBookmark(training: BookMark) = withContext(Dispatchers.IO) {

        val bookmarkList = async { getBookmark() ?: emptyList() }.await().toMutableList()
        val newBookmark = BookMarkResponse(
            training.image,
            training.field,
            training.major,
            training.city,
            training.name,
            training.description,
            training.location,
            training.id,
            training.info,
            training.email
        )

        if (!bookmarkList.contains(newBookmark)) {
            bookmarkList.add(newBookmark)
            userDataSource.updateBookmark(getCurrentUserID(), bookmarkList)
        }
    }

    suspend fun deleteBookmark(trainingId: String) {
        withContext(Dispatchers.IO) {
            val bookmarkList = getBookmark().toMutableList()
            Log.e("TAG", "deleteBookmark: ${bookmarkList.size}")

            var itrator=bookmarkList.iterator()
            while (itrator.hasNext()){
                var item =itrator.next()
                if (item.id==trainingId){
                    itrator.remove()
                }
            }
                    userDataSource.updateBookmark(getCurrentUserID(), bookmarkList)

        }
    }

    suspend fun isTrainingBookmarked(id: String): Boolean {
        var isMark = false
        return withContext(Dispatchers.IO) {
            var a = getBookmark()

            a.find { it.id == id }?.let {
                return@withContext true
            }
            return@withContext false
        }
    }
}


//       bookmarkedList.find { it.id ==id }.let {
//           return true
//       }
//       return false

//       for (item in bookmarkedList){
//           isMark = item.id == id
//       }
//           return isMark


