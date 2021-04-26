package com.skyletto.startappfrontend.data.network;

import com.skyletto.startappfrontend.data.requests.LoginDataRequest;
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest;
import com.skyletto.startappfrontend.data.responses.ProfileResponse;
import com.skyletto.startappfrontend.domain.entities.Tag;
import com.skyletto.startappfrontend.domain.entities.User;

import java.util.Set;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("register")
    Single<ProfileResponse> register(@Body RegisterDataRequest data);

    @POST("auth")
    Single<ProfileResponse> login(@Body LoginDataRequest data);

    @GET("user/get")
    Single<User> getUserByToken(@Header("Authorization") String auth);

    @GET("tags/random")
    Single<Set<Tag>> getRandomTags();

    @GET("tags")
    Single<Set<Tag>> getSimilarTags(@Query("string") String str);
}
