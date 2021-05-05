package com.skyletto.startappfrontend.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiRepository {
    const val ERR_500 = "HTTP 500"
    private const val BASE_URL = "http://192.168.0.11:8080/api/"
    private const val HEADER_NAME = "Content-Type"
    const val AUTH_HEADER_NAME = "Authorization"
    private const val HEADER_VALUE = "application/json;charset=UTF-8"

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val request = chain.request().newBuilder()
                        .addHeader(HEADER_NAME, HEADER_VALUE)
                        .build()
                chain.proceed(request)
            }

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()

    @JvmField
    var apiService: ApiService = retrofit.create(ApiService::class.java)

    @JvmStatic
    fun makeToken(token: String): String {
        return "Bearer $token"
    }
}