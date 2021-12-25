package com.reem.internship.dataLayer

import com.reem.internship.data.CompanyResponse
import com.reem.internship.network.CompanyApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CompaniesRemoteDataSource (var api:CompanyApiService, var ioDispatcher: CoroutineDispatcher) : CompaniesDataSource {
    override suspend fun getCompanies(): List<CompanyResponse> =
      withContext(ioDispatcher) {
          api.getCompanyApi()
      }

}