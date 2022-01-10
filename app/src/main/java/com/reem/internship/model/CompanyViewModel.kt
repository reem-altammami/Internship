package com.reem.internship.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reem.internship.TrainingItemUiState
import com.reem.internship.TrainingUiState
import com.reem.internship.data.CompanyResponse
import com.reem.internship.dataLayer.CompaniesRepo
import com.reem.internship.dataLayer.UserRepository

import com.reem.internship.domainLayer.GetTrainingListWithBookMarksUseCase
import com.reem.internship.ui.BookmarkItemUiState
import com.reem.internship.ui.BookmarkUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TrainingApiStatus {
    LOADING, ERROR, DONE, EMPTY
}

class CompanyViewModel(var companiesRepo: CompaniesRepo, private val userRepo: UserRepository,private val getTrainingListWithBookMarks: GetTrainingListWithBookMarksUseCase) :
    ViewModel() {
    private val _companies = MutableLiveData<List<CompanyResponse>>()
    var companies: MutableLiveData<List<CompanyResponse>> = _companies
    private val _status = MutableLiveData<TrainingApiStatus>()
    val status: LiveData<TrainingApiStatus> = _status

    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()
    private val _trainingDetails = MutableLiveData<TrainingItemUiState>()
    var trainingDetails: MutableLiveData<TrainingItemUiState> = _trainingDetails

    private val _bookmarkDetails = MutableLiveData<BookmarkItemUiState>()
    var bookmarkDetails: MutableLiveData<BookmarkItemUiState> = _bookmarkDetails

    private val _bookMarkUiState = MutableStateFlow(BookmarkUiState())
    val bookMarkUiState: StateFlow<BookmarkUiState> = _bookMarkUiState.asStateFlow()

    private val _profileDetails = MutableLiveData<User>()
    var profileDetails: MutableLiveData<User> = _profileDetails

    private val _isMarked = MutableLiveData<Boolean>()
    var isMarked: LiveData<Boolean> = _isMarked

    init {
        getItemTraingListWithBookMArks()
        getProfileDetails()
    }
fun getItemTraingListWithBookMArks(major: String = "", city: String = ""){
    _uiState.update {
        it.copy(trainingItemList = emptyList(), status = TrainingApiStatus.LOADING)
    }
    viewModelScope.launch {

       val s= async {  getTrainingListWithBookMarks(city, major)}.await()
        if(s.isEmpty()) {
            _uiState.update { it.copy(trainingItemList = emptyList(), status = TrainingApiStatus.EMPTY) }

        }else{
            _uiState.update { it.copy(trainingItemList = s, status = TrainingApiStatus.DONE) }

        }
    }
}


    fun getTrainingDetails(id: Int, source: Int) {
        val trainingList = uiState.value.trainingItemList
        if (source == 0) {
            trainingDetails.value = trainingList[id]
        } else if (source == 1) {
            val itemBookmarkDetails = bookMarkUiState.value.bookmarkItemList[id]
            for (item in trainingList) {
                if (item.id == itemBookmarkDetails.id)
                    trainingDetails.value = item
            }
        }
    }

    fun addBooKmark(bookmark: BookMark) {

        viewModelScope.launch {
            userRepo.addTrainingToBookmark(bookmark)
        }
    }

    fun getMarkBook() {

        viewModelScope.launch {
            _bookMarkUiState.update {
                it.copy(status = TrainingApiStatus.LOADING)
            }
            try {
                val listResult = userRepo.getBookmark() ?: emptyList()
                val list: MutableList<BookmarkItemUiState> = mutableListOf()
                listResult.forEach { it ->
                    if (!it.id.isNullOrEmpty()) {
                        val bookmark =
                            it.let {

                                BookmarkItemUiState(
                                    id = it.id!!,
                                    image = it.image!!,
                                    name = it.name!!,
                                    info = it.info!!,
                                    location = it.location!!,
                                    major = it.major!!,
                                    field = it.field!!,
                                    city = it.city!!,
                                    description = it.description!!,
                                    email = it.email!!
                                )

                            }
                        list.add(bookmark)
                    }
                }
                if (list.isEmpty()) {
                    _bookMarkUiState.update {
                        Log.e("TAG", "getMarkBook: empty")
                        it.copy(bookmarkItemList = list.toList(), status = TrainingApiStatus.EMPTY)
                    }
                } else {
                    _bookMarkUiState.update {
                        Log.e("TAG", "getMarkBook: Done")
                        it.copy(bookmarkItemList = list.toList(), status = TrainingApiStatus.DONE)

                    }
                    Log.d("size", "size: ${bookMarkUiState.value.bookmarkItemList.size}")
                }

            } catch (e: java.lang.Exception) {
                Log.d("ffff", "getMarkBook: ${e.toString()}")
                _bookMarkUiState.update {
                    it.copy(status = TrainingApiStatus.ERROR)
                }


            }
        }

    }
    fun unBookMarkTraining(trainingId: String) {


        viewModelScope.launch {
            userRepo.deleteBookmark(trainingId)
        }
    }
    fun isTrainingBookmarked(id: String) {
        viewModelScope.launch {
            val result = userRepo.isTrainingBookmarked(id)
            _isMarked.value = result
            Log.d("ismark", "is marked or not: ${isMarked}")

        }


    }

    fun getProfileDetails() {

        viewModelScope.launch {


          userRepo.getUserData().collect {

                val useProfile = it.let {


                    User(
                        it.name,
                        it.email,
                        it.id,
                        it.university,
                        it.major,
                        it.city,
                        it.gpa,
                        it.bookMark

                    )
                }
                Log.d("profile", "profile: ${useProfile}")

                profileDetails.value = useProfile
            }

        }

    }
}



