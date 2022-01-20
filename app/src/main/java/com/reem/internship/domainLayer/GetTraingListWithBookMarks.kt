package com.reem.internship.domainLayer

import com.reem.internship.TrainingItemUiState
import com.reem.internship.dataLayer.CompaniesRepo
import com.reem.internship.dataLayer.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.concurrent.Flow

class GetTrainingListWithBookMarksUseCase(
    private var userRepository: UserRepository,
    private var companiesRepo: CompaniesRepo
) {
    suspend operator fun invoke(city: String, major: String): List<TrainingItemUiState> =
        withContext(Dispatchers.IO) {
            val listResult = async { companiesRepo.getCompanies() }

            val list: MutableList<TrainingItemUiState> = mutableListOf()
            listResult.await().forEach { company ->
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
                            description = it.description!!,
                            email = company.email!!,
                            isMark = false,
                            publishDate = it.date!!

                        )

                    }
                }
                list.addAll(filter(companyTraining, city = city, major = major))

            }

            val listOfbookMark = async { userRepository.getBookmark() }.await()
            val listWithBookMArks = list.map { training ->
                val bookmark = listOfbookMark.find { it.id == training.id }
               bookmark ?.let {
                   return@map     training.copy(isMark = true)
                    }

                    training.copy()

            }

            return@withContext listWithBookMArks
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