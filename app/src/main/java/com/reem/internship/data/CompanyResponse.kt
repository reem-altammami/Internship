package com.reem.internship.data

import com.squareup.moshi.Json

data class CompanyResponse(

	@Json(name="image")
	val image: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="location")
	val location: Location? = null,

	@Json(name="training")
	val training: List<TrainingItem> = listOf(),

	@Json(name="id")
	val id: String? = null,

	@Json(name="email")
	val email: String? = null,

	@Json(name="info")
	val info: String? = null
)

data class Major(

	@Json(name="majorId")
	val majorId: String? = null,

	@Json(name="majorName")
	val majorName: String? = null
)

data class TrainingItem(

	@Json(name="date")
	val date: String? = null,

	@Json(name="companyId")
	val companyId: String? = null,

	@Json(name="field")
	val field: String? = null,

	@Json(name="major")
	val major: Major? = null,

	@Json(name="city")
	val city: City? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="id")
	val id: String? = null
)

data class Location(

	@Json(name="cityname")
	val cityname: String? = null,

	@Json(name="cityID")
	val cityID: Int? = null
)

data class City(

	@Json(name="cityName")
	val cityName: String? = null,

	@Json(name="cityId")
	val cityId: String? = null
)

data class User(
	@Json(name="userId")
	val userId: String? = null,

	@Json(name="name")
	val userName: String? = null,


	@Json(name="email")
	val email: String? = null,

	@Json(name="university")
	val university: String? = null,

	@Json(name="major")
	val major: Major? = null,

	@Json(name="city")
	val city: City? = null,

	@Json(name="gpa")
	val gpa: String? = null,

	@Json(name="bookMark")
	val bookMark: BookMark? = null
)

data class BookMark(
	@Json(name="bookMarkId")
	val bookMarkId: String? = null,

	@Json(name="training")
	val training: List<TrainingItem> = listOf()
)
