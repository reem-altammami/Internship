package com.reem.internship.ui

import com.reem.internship.TrainingItemUiState
import com.reem.internship.model.BookMark
import com.reem.internship.model.TrainingApiStatus

data class BookmarkUiState(
    val bookmarkItemList: List<BookmarkItemUiState> = listOf(),
    val status: TrainingApiStatus = TrainingApiStatus.EMPTY
)


data class BookmarkItemUiState(
    val id : String,
    val image: String,
    val name: String,
    val info : String,
    val location : String,
    val major: String,
    val field: String,
    val city: String,
    val description :String,
    val email:String
)


fun TrainingItemUiState.toBookMark():BookMark{

    return  BookMark(this.id,this.image,this.name,this.info,
        this.location,this.major,this.field,this.city,
        this.description,this.email)
}