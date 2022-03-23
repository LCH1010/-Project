package com.example.project1.api;

import com.example.project1.data.TopChartData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiPodcast {

    @POST("v2/rest/rose/IF_ROSEPODCAST_007")
    Call<TopChartData> ApiPodcast(@Body HashMap<String, Object> Body);
}
