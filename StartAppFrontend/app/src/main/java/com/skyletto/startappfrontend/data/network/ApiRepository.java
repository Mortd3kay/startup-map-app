package com.skyletto.startappfrontend.data.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRepository {

    public static final String ERR_500 = "HTTP 500";

    private static final String BASE_URL = "http://192.168.0.11:8080/api/";

    private static ApiRepository instance;

    private Retrofit retrofit;

    public ApiService apiService;

    private ApiRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiRepository getInstance(){
        if (instance == null){
            synchronized (ApiRepository.class){
                if (instance == null){
                    instance = new ApiRepository();
                }
            }
        }
        return instance;
    }
}
