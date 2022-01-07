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
//            }
                }
            }
        }
    }
//        viewModelScope.launch {
//            val user = CompanyApi.retrofitService.putUserData(userId.value!!,user = _user.value!!)
//        }

//        var s = UserItemUiState("ddd", "dddd@fdfr.efe", "rgfef", "efe")
//        _userUiState.update { it.copy(userItem = s) }
//    }

    fun showProfileDetails() {
        val useProfile = userUiState.value.userItem
//        profileDetails.value = useProfile
    }

    fun addUserToDataBase(user: User) {
        viewModelScope.launch {
            userRepo.putUserData(user)
        }
    }

    fun isEntryValid(
        userName: String,
        email: String,
        major: String,
        city: String,
        university: String,
        gpa: String
    ): Boolean {
        return (userName.trim().isNotEmpty() && email.trim().isNotEmpty() && major.trim()
            .isNotEmpty() && city.trim().isNotEmpty() && university.trim()
            .isNotEmpty()&& gpa.trim().isNotEmpty())
    }

}

