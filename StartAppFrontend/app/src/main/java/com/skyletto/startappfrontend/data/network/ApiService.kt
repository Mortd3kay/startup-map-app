package com.skyletto.startappfrontend.data.network

import com.skyletto.startappfrontend.data.requests.EditProfileDataRequest
import com.skyletto.startappfrontend.data.requests.LatLngRequest
import com.skyletto.startappfrontend.data.requests.LoginDataRequest
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import com.skyletto.startappfrontend.domain.entities.*
import com.skyletto.startappfrontend.domain.entities.Tag
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(@Body data: RegisterDataRequest): Single<ProfileResponse>

    @POST("auth")
    fun login(@Body data: LoginDataRequest): Single<ProfileResponse>

    @GET("user/get")
    fun getUserByToken(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String): Single<User>

    @PUT("user/edit")
    fun editProfile(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body profile:EditProfileDataRequest) : Single<ProfileResponse>

    @GET("email")
    fun findUserByEmail(@Query("email") str: String): Single<Int>

    @GET("tags/random")
    fun getRandomTags(): Single<Set<Tag>>

    @GET("tags")
    fun getSimilarTags(@Query("string") str: String): Single<Set<Tag>>

    @GET("messages/get")
    fun getMessagesFromChat(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Query("chat") id:Long, @Query("last") lastId:Long):Single<List<Message>>

    @POST("messages/add")
    fun addMessage(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body message: Message): Single<Message>

    @GET("messages/chats")
    fun getChats(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String): Single<List<Message>>

    @GET("users/{id}")
    fun getUserById(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Path("id") id:Long):Single<User>

    @POST("users/specific")
    fun getUsersByIds(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body ids: Set<Long>): Single<List<User>>

    @POST("projects/add")
    fun addProject(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body project: Project):Single<Project>

    @GET("roles/all")
    fun getAllRoles(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String):Single<List<Role>>

    @GET("projects/all")
    fun getAllProjects(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String):Single<List<Project>>

    @POST("projects/closest")
    fun getClosestProjects(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body latLng: LatLngRequest):Single<List<Project>>

    @HTTP(method = "DELETE",path = "projects/remove", hasBody = true)
    fun removeProject(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body project:Project):Single<List<Project>>

    @PUT("roles/update")
    fun updateRole(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body role: ProjectAndRole) : Single<ProjectAndRole>

    @POST("user/location")
    fun setLocation(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body latLng: LatLngRequest) : Single<Location>

    @DELETE("user/location/remove")
    fun deleteLocation(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String) : Completable

    @POST("users/locations")
    fun getUserLocations(@Header(ApiRepository.AUTH_HEADER_NAME) auth: String, @Body latLng: LatLngRequest): Single<List<Location>>
}