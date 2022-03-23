package com.example.project1.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiPodClient {

    private static final String BASE_URL = "https://release.roseaudio.kr:18081/";

    public static ApiPodcast getApiTopChart() {
        return getInstance().create(ApiPodcast.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
