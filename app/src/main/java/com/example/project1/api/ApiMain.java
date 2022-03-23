package com.example.project1.api;

import android.util.Log;

import com.example.project1.data.PostTimeData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiMain {

    public interface onTimeListener {
        void PostTime(PostTimeData data);
    }

    public boolean ApiAlreadyCheck(onTimeListener listener){
        boolean isRight = false;

        //Retrofit 호출
        Call<PostTimeData> call = ApiClient.getApiService().PostTime();
        call.enqueue(new Callback<PostTimeData>() {
            @Override
            public void onResponse(Call<PostTimeData> call, Response<PostTimeData> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    return;
                }
                PostTimeData checkAlready = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());
                listener.PostTime(checkAlready);

            }
            @Override
            public void onFailure(Call<PostTimeData> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        return isRight;
    }

}
