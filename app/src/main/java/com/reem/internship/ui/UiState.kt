package com.reem.internship

data class TrainingUiState(
    val trainingItemList: List<TrainingItemUiState> = listOf()
)


data class TrainingItemUiState(
    val id : String,
    val image: String,
    val name: String,
    val info : String,
    val location : String,
    val major: String,
    val field: String,
    val city: String
)