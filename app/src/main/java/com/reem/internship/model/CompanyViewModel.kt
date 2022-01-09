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
import com.reem.internship.ui.UserItemUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

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

    private val _profileDetails = MutableLiveData<User>()
    var profileDetails: MutableLiveData<User> = _profileDetails

    private val _isMarked = MutableLiveData<Boolean>()
     var  isMarked : LiveData<Boolean> =_isMarked
//var isMarked by Delegates.notNull<Boolean>()

    init {
        getTrainingList()
        getMarkBook()
        getProfileDetails()
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

                        // fun

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
                                email = company.email!!,
                                isMark = true

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

        Log.e("TAG", "getBookmarkDetails: ${bookmarkDetails.value} ", )

    }

    fun getTrainingDetails(id: Int, source: Int){
        val trainingList = uiState.value.trainingItemList

    if(source == 0) {

        trainingDetails.value = trainingList[id]

    } else if (source == 1) {
        val itemBookmarkDetails = bookMarkUiState.value.bookmarkItemList[id]
        for (item in trainingList){
             if (item.id == itemBookmarkDetails.id)
               trainingDetails.value = item
        }

    }

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
            trainingDetails.value?.email!!
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
                                    description = it.description!!,
                                    email = it.email!!
                                )

                            }
                        list.add(bookmark)
                    }
                }
                if (list.isEmpty()) {
                    _bookMarkUiState.update {
                        Log.e("TAG", "getMarkBook: empty", )
                        it.copy(bookmarkItemList = list.toList(), status = TrainingApiStatus.EMPTY)
                    }
                } else {
                    _bookMarkUiState.update {
                        Log.e("TAG", "getMarkBook: Done", )
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

    fun unBookMarkTraining(){
        Log.e("TAG", "unBookMarkTraining: id  ${trainingDetails.value?.id}", )
        Log.e("TAG", "unBookMarkTraining: name ${trainingDetails.value?.name}", )

        bookmarkDetails
        val trainingId = trainingDetails.value?.id
        viewModelScope.launch {
            userRepo.deleteBookmark(trainingId!!)
        }
    }

    fun isTrainingBookmarked(id: String)  {
            viewModelScope.launch {
               val result = userRepo.isTrainingBookmarked(id)
                _isMarked.value = result
                Log.d("ismark", "is marked or not: ${isMarked}")

            }



    }

    fun getProfileDetails() {

        viewModelScope.launch {


            var user = userRepo.getUserData().collect {

                val useProfile = it.let{


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



