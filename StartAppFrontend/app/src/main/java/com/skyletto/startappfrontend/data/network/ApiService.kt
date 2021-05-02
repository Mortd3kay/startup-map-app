package com.skyletto.startappfrontend.data.network

import com.skyletto.startappfrontend.data.requests.LoginDataRequest
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.domain.entities.User
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(@Body data: RegisterDataRequest): Single<ProfileResponse>

    @POST("auth")
    fun login(@Body data: LoginDataRequest): Single<ProfileResponse>

    @GET("user/get")
    fun getUserByToken(@Header("Authorization") auth: String): Single<User>

    @GET("email")
    fun findUserByEmail(@Query("email") str: String): Single<Int>

    @GET("tags/random")
    fun getRandomTags(): Single<Set<Tag>>

    @GET("tags")
    fun getSimilarTags(@Query("string") str: String): Single<Set<Tag>>
}