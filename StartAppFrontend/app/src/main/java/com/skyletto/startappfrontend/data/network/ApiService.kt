package com.skyletto.startappfrontend.data.network

import com.skyletto.startappfrontend.data.requests.LoginDataRequest
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import com.skyletto.startappfrontend.domain.entities.Message
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
    fun getUserByToken(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String): Single<User>

    @GET("email")
    fun findUserByEmail(@Query("email") str: String): Single<Int>

    @GET("tags/random")
    fun getRandomTags(): Single<Set<Tag>>

    @GET("tags")
    fun getSimilarTags(@Query("string") str: String): Single<Set<Tag>>

    @GET("messages/chats")
    fun getChats(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String): Single<List<Message>>

    @GET("users/{id}")
    fun getUserById(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Path("id") id:Long):Single<User>

    @POST("users/specific")
    fun getUsersByIds(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body ids: Set<Long>): Single<List<User>>
}