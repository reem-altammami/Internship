package com.reem.internship.model

data class User(
    val name: String="",
    val email: String="",
    val id: String="",
    val university: String ="",
    val major: String="",
    val city: String="",
    val gpa: String="",
    val bookMark: List<BookMark> = listOf(BookMark("","","","","","","","",""))

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
    val description :String
)
