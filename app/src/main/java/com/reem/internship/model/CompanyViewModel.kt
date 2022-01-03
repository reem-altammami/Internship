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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TrainingApiStatus {
    LOADING, ERROR, DONE, EMPTY
}

class CompanyViewModel(var companiesRepo: CompaniesRepo) : ViewModel() {
    private val _companies = MutableLiveData<List<CompanyResponse>>()
    var companies: MutableLiveData<List<CompanyResponse>> = _companies
    private val _status = MutableLiveData<TrainingApiStatus>()
    val status: LiveData<TrainingApiStatus> = _status

    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()
    private val _trainingDetails = MutableLiveData<TrainingItemUiState>()
    var trainingDetails: MutableLiveData<TrainingItemUiState> = _trainingDetails

    init {
        getTrainingList()
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
                                description = it.description!!
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
                    it.copy(status= TrainingApiStatus.ERROR)
                }
            }
        }
    }

    fun getTrainingDetails(id: Int) {

        val itemDetails = uiState.value.trainingItemList[id]
        trainingDetails.value = itemDetails

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
}
