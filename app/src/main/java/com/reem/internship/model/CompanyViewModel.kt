package com.reem.internship.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reem.internship.TrainingItemUiState
import com.reem.internship.TrainingUiState
import com.reem.internship.data.CompanyResponse
import com.reem.internship.data.TrainingItem
import com.reem.internship.dataLayer.CompaniesRepo
import com.reem.internship.dataLayer.UserRepository
import com.reem.internship.ui.BookmarkItemUiState
import com.reem.internship.ui.BookmarkUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class TrainingApiStatus {
    LOADING, ERROR, DONE, EMPTY
}

class CompanyViewModel(var companiesRepo: CompaniesRepo, private val userRepo: UserRepository) :
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
    private var bookmarkList = mutableListOf<BookMark>()
    private var userBookmarkList = mutableListOf<BookmarkItemUiState>()

    init {
        getTrainingList()
        getMarkBook()
    }

    fun getTrainingList(major: String = "", city: String = "") {
        viewModelScope.launch {
            _uiState.update {
                it.copy(status = TrainingApiStatus.LOADING)
            }
            try {
                val listResult = companiesRepo.getCompanies()
//                _status.value = TrainingApiStatus.DONE

                val list: MutableList<TrainingItemUiState> = mutableListOf()
                listResult.forEach { company ->
                    val companyTraining = company.training.map { training ->

                        training.let {

                            TrainingItemUiState(
                                id = it.id!!,
                                image = company.image!!,
                                name = company.name!!,
                                info = company.info!!,
                                location = company.location!!.cityname!!,
                                major = it.major!!.majorName!!,
                                field = training.field!!,
                                city = training.city!!.cityName!!,
                                description = it.description!!,
                                mail = company.email!!
                            )

                        }
                    }



                    list.addAll(filter(companyTraining, city = city, major = major))

                }
                if (list.isEmpty()) {
                    _uiState.update {
                        it.copy(trainingItemList = list.toList(), status = TrainingApiStatus.EMPTY)
                    }
                    //     _status.value=TrainingApiStatus.EMPTY
                } else {
                    _uiState.update {
                        it.copy(trainingItemList = list.toList(), status = TrainingApiStatus.DONE)
                    }
                    //    _status.value = TrainingApiStatus.DONE

                }
//                companies.value = listResult
            } catch (e: Exception) {

                _uiState.update {
                    it.copy(status = TrainingApiStatus.ERROR)
                }
            }
        }
    }


    fun getTrainingDetails(id: Int) {

        val itemDetails = uiState.value.trainingItemList[id]
        trainingDetails.value = itemDetails


    }

    fun getBookmarkDetails(id: Int) {

        val itemDetails = bookMarkUiState.value.bookmarkItemList[id]
        bookmarkDetails.value = itemDetails

    }

    fun getTrainingFilteredByMajor(filterByMajor: String) {
        var filteredList = uiState.value.trainingItemList.filter { it.major.equals(filterByMajor) }
        _uiState.update { it.copy(trainingItemList = filteredList) }
    }

    fun getTrainingFilteredByCity(filterByCity: String) {
        var filteredList = uiState.value.trainingItemList.filter { it.city.equals(filterByCity) }
        _uiState.update { it.copy(trainingItemList = filteredList) }
    }

    fun filter(
        list: List<TrainingItemUiState>,
        major: String = "",
        city: String = ""
    ): List<TrainingItemUiState> {
        var
                filterList = if (major.isNotEmpty() && city.isNotEmpty()) {
            list.filter { it.major == major && it.city == city }
        } else if (major.isNotEmpty() && city.isEmpty()) {
            list.filter { it.major == major }
        } else if (major.isEmpty() && city.isNotEmpty()) {
            list.filter { it.city == city }

        } else {
            list
        }
        return filterList
    }

    fun addBooKmark() {
        val bookmark = BookMark(
            trainingDetails.value?.id!!,
            trainingDetails.value?.image!!,
            trainingDetails.value?.name!!,
            trainingDetails.value?.info!!,
            trainingDetails.value?.location!!,
            trainingDetails.value?.major!!,
            trainingDetails.value?.field!!,
            trainingDetails.value?.city!!,
            trainingDetails.value?.description!!,
            trainingDetails.value?.mail!!
        )
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
                                    description = it.description!!
                                )

                            }
                        list.add(bookmark)
                    }
                }
                if (list.isEmpty()) {
                    _bookMarkUiState.update {
                        it.copy(bookmarkItemList = list.toList(), status = TrainingApiStatus.EMPTY)
                    }
                } else {
                    _bookMarkUiState.update {
                        it.copy(bookmarkItemList = list.toList(), status = TrainingApiStatus.DONE)
                    }
                }

            } catch (e: java.lang.Exception) {
                Log.d("ffff", "getMarkBook: ${e.toString()}")
                _bookMarkUiState.update {
                    it.copy(status = TrainingApiStatus.ERROR)
                }


            }
        }

    }

    fun unBookMarkTraining(){
        val trainingId = trainingDetails.value?.id!!
        viewModelScope.launch {
            userRepo.deleteBookmark(trainingId)
        }
    }
}