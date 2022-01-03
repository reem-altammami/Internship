package com.reem.internship.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reem.internship.provideCompaniesRepo
import com.reem.internship.provideUserRepo
import java.lang.IllegalArgumentException

class ViewModelFactory (): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
if (modelClass.isAssignableFrom(CompanyViewModel::class.java)) {
    return CompanyViewModel(provideCompaniesRepo()) as T
}
    throw IllegalArgumentException("Unknown ViewModel class")}
}

class UserViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(provideUserRepo()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
