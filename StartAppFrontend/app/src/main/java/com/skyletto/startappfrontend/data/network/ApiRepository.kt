package com.skyletto.startappfrontend.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiRepository {
    const val ERR_500 = "HTTP 500"
    private const val BASE_URL = "http://192.168.0.11:8080/api/"

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json;charset=UTF-8")
                        .build()
                chain.proceed(request)
            }

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient.build())
            .build()

    @JvmField
    var apiService: ApiService = retrofit.create(ApiService::class.java)

    @JvmStatic
    fun makeToken(token: String): String {
        return "Bearer $token"
    }
}