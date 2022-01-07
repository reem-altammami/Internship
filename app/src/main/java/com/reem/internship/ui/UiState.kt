package com.reem.internship

import com.reem.internship.model.TrainingApiStatus

data class TrainingUiState(
    val trainingItemList: List<TrainingItemUiState> = listOf(),
val status: TrainingApiStatus=TrainingApiStatus.EMPTY
)


data class TrainingItemUiState(
    val id : String,
    val image: String,
    val name: String,
    val info : String,
    val location : String,
    val major: String,
    val field: String,
    val city: String,
    val description :String,
    val email : String
)