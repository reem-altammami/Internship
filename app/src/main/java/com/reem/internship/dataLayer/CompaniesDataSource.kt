package com.reem.internship.dataLayer

import com.reem.internship.data.CompanyResponse

interface CompaniesDataSource {

    suspend fun getCompanies():List<CompanyResponse>


}