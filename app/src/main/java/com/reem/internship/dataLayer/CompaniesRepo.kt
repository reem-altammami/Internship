package com.reem.internship.dataLayer

import com.google.firebase.auth.FirebaseAuth
import com.reem.internship.data.CompanyResponse

class CompaniesRepo ( private var companiesDataSource: CompaniesDataSource) {
    private fun getCurrentUserID()= FirebaseAuth.getInstance().currentUser?.uid?:""

    suspend fun getCompanies(): List<CompanyResponse> =
        companiesDataSource.getCompanies()


}