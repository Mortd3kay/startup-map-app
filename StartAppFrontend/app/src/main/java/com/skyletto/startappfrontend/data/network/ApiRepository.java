package com.skyletto.startappfrontend.data.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .build();
            return chain.proceed(request);
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
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

    public static String makeToken(String token){
        return "Bearer "+token;
    }
}
