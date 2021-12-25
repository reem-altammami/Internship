package com.reem.internship.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reem.internship.data.CompanyResponse
import com.reem.internship.dataLayer.CompaniesRepo
import kotlinx.coroutines.launch

class CompanyViewModel(var companiesRepo: CompaniesRepo) :ViewModel() {
    private val _companies = MutableLiveData<List<CompanyResponse>>()
    var companies : MutableLiveData<List<CompanyResponse>> = _companies
private val _status =MutableLiveData<String>()
    val status: LiveData<String> = _status
    init {
        getCompany()
    }

    private fun getCompany() {
        viewModelScope.launch {
            try {
                val listResult = companiesRepo.getCompanies()
                companies.value = listResult
                _status.value = "Success: ${listResult} "
            } catch (e: Exception) {

                _status.value = "Failure: ${e.message}"
            }
        }
    }
}