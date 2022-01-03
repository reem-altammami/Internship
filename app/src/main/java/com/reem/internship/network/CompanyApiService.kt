package com.reem.internship.network

import com.reem.internship.data.BookMark
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.reem.internship.data.CompanyResponse
import com.reem.internship.model.User
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


private const val BASE_URL = "https://internship-407c0-default-rtdb.firebaseio.com/"
fun getInterceptor(): Interceptor {
    var interceptor = HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return  interceptor
}
val client =  OkHttpClient.Builder().addInterceptor(getInterceptor()).build();


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


object CompanyApi {
    val retrofitService : CompanyApiService by lazy {
        retrofit.create(CompanyApiService::class.java) }
}

interface CompanyApiService {

    @GET("data/companies.json")
    suspend fun getCompanyApi(): List<CompanyResponse>

  @PUT("data/users.json")
  suspend fun putUserData (@Path("id")id:String,@Body user:User):User

    @GET("data/users/{id}.json")
    suspend fun getUserApi(@Path("id")id:String): User

    @PUT("data/users/{id}.json")
    suspend fun updateUserData (@Path("id")id:String,@Body user:User):User

    @PUT("data/users/{id}/bookMark.json")
    suspend fun addTraining (@Path("id")id:String,@Body user:User):User

    @GET("data/users/{id}/bookMark.json")
    suspend fun getUserBookMark(@Path("id")bookMark: BookMark):List<BookMark>

    @DELETE("data/users/{id}/bookMark/{id}.json")
    suspend fun deleteTraining (@Path("id")id:String, @Path("id")idBookmark: String, @Body bookMark: BookMark)

//    @GET("data/users.json")
//    suspend fun getUserApi(@Query("users")userId:String): User


}