package com.reem.internship.ui

import com.reem.internship.TrainingItemUiState
import com.reem.internship.model.TrainingApiStatus

data class UserUiState(
    val userItem: UserItemUiState =UserItemUiState(),
    val status: TrainingApiStatus = TrainingApiStatus.EMPTY
)

data class UserItemUiState(
    val userName: String ="",
    val email: String="",
    val userId: String="",
    val university: String="",
    val major: String="",
    val city: String="",
    val gpa: String=""
)
