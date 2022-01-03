package com.reem.internship

import com.reem.internship.dataLayer.*
import com.reem.internship.network.CompanyApi
import com.reem.internship.network.CompanyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle


fun providerCompanyApi(): CompanyApiService {
    return CompanyApi.retrofitService
}

fun provideCompanyRemoteDataSource(): CompaniesDataSource{
    return CompaniesRemoteDataSource(providerCompanyApi(), Dispatchers.IO)
}

fun provideCompaniesRepo():CompaniesRepo = CompaniesRepo(provideCompanyRemoteDataSource())


fun provideUserRemoteDataSource () :UserDataSource {
    return UserRemoteDataSource(providerCompanyApi(),Dispatchers.IO)
}

fun provideUserRepo():UserRepository = UserRepository(provideUserRemoteDataSource())