package com.reem.internship.model

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.reem.internship.TrainingItemUiState
import com.reem.internship.TrainingUiState
import com.reem.internship.dataLayer.UserRepository
import com.reem.internship.network.CompanyApi
import com.reem.internship.provideCompaniesRepo
import com.reem.internship.provideUserRepo
import com.reem.internship.ui.BookmarkUiState
import com.reem.internship.ui.UserItemUiState
import com.reem.internship.ui.UserUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class UserViewModel(private val userRepo: UserRepository) : ViewModel() {

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()


    private val _profileDetails = MutableLiveData<UserItemUiState>()
    var profileDetails: MutableLiveData<UserItemUiState> = _profileDetails

    init {
        getUserData()
    }

    //Get user information from repository
    fun getUserData() {
        viewModelScope.launch {
            var user = userRepo.getUserData().collect { user ->


                _userUiState.update {
                    it.copy(
                        userItem =
                        UserItemUiState(
                            userName = user.name!!,
                            email = user.email!!,
                            userId = user.id!!,
                            university = user.university!!,
                            major = user.major!!,
                            gpa = user.gpa!!,
                            city = user.city!!
                        )
                    )
                }
            }
        }
    }


    fun getProfileDetails() {
        val useProfile = userUiState.value.userItem
        profileDetails.value = useProfile
    }
//add user information to database
    fun addUserToDataBase(user: User) {
        viewModelScope.launch {
            userRepo.putUserData(user)
        }
    }
//Check if all fields are not empty
    fun isEntryValid(
        userName: String,
        email: String,
        major: String,
        city: String,
        university: String,
        gpa: String
    ): Boolean {
        return (userName.trim().isNotEmpty() && email.trim().isNotEmpty() && major.trim()
            .isNotEmpty() && city.trim().isNotEmpty()&& !city.trim().equals("City") && !major.trim().equals("Major") && university.trim()
            .isNotEmpty()&& gpa.trim().isNotEmpty())
    }

}

