package com.skyletto.startappfrontend.data.network;


import com.skyletto.startappfrontend.data.requests.LoginDataRequest;
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest;
import com.skyletto.startappfrontend.data.responses.ProfileResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("register")
    Single<ProfileResponse> register(@Body RegisterDataRequest data);

    @POST("auth")
    Single<ProfileResponse> login(@Body LoginDataRequest data);

}
