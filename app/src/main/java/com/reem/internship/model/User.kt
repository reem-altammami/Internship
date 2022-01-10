package com.reem.internship.model

import com.reem.internship.ui.BookmarkItemUiState

data class User(
    val name: String="",
    val email: String="",
    val id: String="",
    val university: String ="",
    val major: String="",
    val city: String="",
    val gpa: String="",
    val bookMark: List<BookMark> = listOf(BookMark("","","","","","","","","",""))

)

data class BookMark(
    val id : String,
    val image: String,
    val name: String,
    val info : String,
    val location : String,
    val major: String,
    val field: String,
    val city: String,
    val description :String,
    val email: String
)


fun BookMark.toBookMarkItemUiState(): BookmarkItemUiState {
    return BookmarkItemUiState(this.id,this.image,this.name,this.info,this.location,this.major,this.field,this.city,this.description,this.email)
}