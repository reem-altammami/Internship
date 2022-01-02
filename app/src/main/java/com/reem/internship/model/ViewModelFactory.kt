package com.reem.internship.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reem.internship.provideCompaniesRepo
import java.lang.IllegalArgumentException

class ViewModelFactory (): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
if (modelClass.isAssignableFrom(CompanyViewModel::class.java)) {
    return CompanyViewModel(provideCompaniesRepo()) as T
}
    throw IllegalArgumentException("Unknown ViewModel class")}
}
