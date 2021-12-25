package com.reem.internship.dataLayer

import com.reem.internship.data.CompanyResponse

class CompaniesRepo ( private var companiesDataSource: CompaniesDataSource) {
    suspend fun getCompanies(): List<CompanyResponse> =
        companiesDataSource.getCompanies()
}