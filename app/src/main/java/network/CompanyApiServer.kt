package network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import data.CompanyResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://61c5d1dec003e70017b79948.mockapi.io/"
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

interface CompanyApiService {

    @GET("company")
    suspend fun getCompanyApi(): List<CompanyResponse>





}

object CompanyApi {
    val retrofitService : CompanyApiService by lazy {
        retrofit.create(CompanyApiService::class.java) }
}