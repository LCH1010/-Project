package com.example.project1.api;

import com.example.project1.data.PostTimeData;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiTime {
    @POST("rest/rose/IF_CURRENTTIME_001")
    Call<PostTimeData> PostTime();
}
