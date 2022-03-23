package com.example.project1.api;

import android.util.Log;

import com.example.project1.data.TopChartData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiPodMain {

    public interface onPodListener {
        void postpodcast(TopChartData Data);
    }

    public boolean podcastCheck(onPodListener listener) {
        boolean isRight = false;

        //Retrofit 호출
        // ★놓친부분 다시 공부하기★
        HashMap<String, Object> map = new HashMap<>();
        map.put("roseToken", "ROSE02049AABCD");
        map.put("countryID", "2");

        Call<TopChartData> call = ApiPodClient.getApiTopChart().ApiPodcast(map);
        call.enqueue(new Callback<TopChartData>() {
            @Override
            public void onResponse(Call<TopChartData> call, Response<TopChartData> response) {
                if (!response.isSuccessful()) {
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    return;
                }
                TopChartData check = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());
                listener.postpodcast(check);
            }

            @Override
            public void onFailure(Call<TopChartData> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
        return isRight;
    }
}