package com.reem.internship.network

import com.reem.internship.data.BookMarkResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.reem.internship.data.CompanyResponse
import com.reem.internship.data.UserResponseModel
import com.reem.internship.model.BookMark
import com.reem.internship.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


private const val BASE_URL = "https://internship-407c0-default-rtdb.firebaseio.com/"
fun getInterceptor(): Interceptor {
    var interceptor = HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return interceptor
}

val client = OkHttpClient.Builder().addInterceptor(getInterceptor()).build();


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)

    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


object CompanyApi {
    val retrofitService: CompanyApiService by lazy {
        retrofit.create(CompanyApiService::class.java)
    }
}

interface CompanyApiService {

    @GET("data/companies.json")
    suspend fun getCompanyApi(): List<CompanyResponse>


    @GET("data/users/{id}.json")
    suspend fun getUserApi(@Path("id") id: String): Response<User?>

    @PUT("data/users/{id}.json")
    suspend fun putUserData(@Path("id") id: String, @Body user: User): User

    @PUT("data/users/{id}/bookMark.json")
    suspend fun updateBookmark(@Path("id") id: String, @Body bookMark: List<BookMarkResponse>)

    @GET("data/users/{id}/bookMark.json")
    suspend fun getBookMark(@Path("id") id: String): Response<List<BookMarkResponse>?>

    @DELETE("data/users/{id}/bookMark/{id}.json")
    suspend fun deleteTrainingFromBookmark(
        @Path("id") id: String,
        @Body bookMark: List<BookMarkResponse>
    )


}

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}  // for your case might be simplify to use only sealed class
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if(response.isSuccessful) {
                val body = response.body()
                // Empty body
                if (body == null || response.code() == 204) {
                    ApiSuccessEmptyResponse()
                } else {
                    ApiSuccessResponse(body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMessage = if(msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMessage ?: "Unknown error")
            }
        }
    }
}

class ApiSuccessResponse<T>(val data: T): ApiResponse<T>()
class ApiSuccessEmptyResponse<T>: ApiResponse<T>()
class ApiErrorResponse<T>(val errorMessage: String): ApiResponse<T>()

class FlowCallAdapterFactory : CallAdapter.Factory() {
    companion object {
        fun create() = FlowCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Flow::class.java != getRawType(returnType)) {
            return null
        }
        check(returnType is ParameterizedType) {
            "Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>"
        }
        val responseType = getParameterUpperBound(0, returnType)

        val rawDeferredType = getRawType(responseType)
        return if (rawDeferredType == Response::class.java) {
            check(responseType is ParameterizedType) {
                "Response must be parameterized as Response<Foo> or Response<out Foo>"
            }
            ResponseCallAdapter<Any>(getParameterUpperBound(0, responseType))
        } else {
            BodyCallAdapter<Any>(responseType)
        }
    }

    private class BodyCallAdapter<T : Any>(
        private val responseType: Type
    ) : CallAdapter<T, Flow<T>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Flow<T> = flow { emit(call.await()) }
    }

    private class ResponseCallAdapter<T : Any>(
        private val responseType: Type
    ) : CallAdapter<T, Flow<Response<T>>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Flow<Response<T>> = flow { emit(call.awaitResponse()) }
    }
}



