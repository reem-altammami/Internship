package com.reem.internship.model

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

class CompanyViewModel(var companiesRepo: CompaniesRepo) : ViewModel() {
    private val _companies = MutableLiveData<List<CompanyResponse>>()
    var companies: MutableLiveData<List<CompanyResponse>> = _companies
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()
    private val _trainingDetails = MutableLiveData<TrainingItemUiState>()
    var trainingDetails: MutableLiveData<TrainingItemUiState> = _trainingDetails

    init {
        getCompany()
    }

     fun getCompany() {
        viewModelScope.launch {
            try {
                val listResult = companiesRepo.getCompanies()
                var list: MutableList<TrainingItemUiState> = mutableListOf()
                listResult.forEach { company ->
                  var companyTraning=  company.training?.map { training ->
                        training?.let {

                                TrainingItemUiState(
                                    id = it.id!!,
                                    image = company.image!!,
                                    name = company.name!!,
                                    info = company.info!!,
                                    location = company.location!!.cityname!!,
                                    major = it.major!!.majorName!!,
                                    field = training.field!!,
                                    city = training.city!!.cityName!!
                                )

                        }
                    }

                    companyTraning?.let { list?.addAll(it) }
                }
                _uiState.update {
                    it.copy(trainingItemList = list.toList())
                }
//                companies.value = listResult
                _status.value = "Success: ${listResult} "
            } catch (e: Exception) {

                _status.value = "Failure: ${e.message}"
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

    fun getTrainingFilteredByCity(filterByCity: String){
        var filteredList = uiState.value.trainingItemList.filter { it.city.equals(filterByCity) }
        _uiState.update { it.copy(trainingItemList = filteredList) }
    }


}