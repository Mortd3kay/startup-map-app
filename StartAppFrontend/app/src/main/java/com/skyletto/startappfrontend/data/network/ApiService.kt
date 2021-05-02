package com.skyletto.startappfrontend.data.network

import com.skyletto.startappfrontend.data.requests.LoginDataRequest
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.domain.entities.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(@Body data: RegisterDataRequest): Response<ProfileResponse>

    @POST("auth")
    suspend fun login(@Body data: LoginDataRequest): Response<ProfileResponse>

    @GET("user/get")
    suspend fun getUserByToken(@Header("Authorization") auth: String): Response<User>

    @GET("tags/random")
    suspend fun getRandomTags(): Response<Set<Tag>>

    @GET("tags")
    suspend fun getSimilarTags(@Query("string") str: String): Response<Set<Tag>>
}