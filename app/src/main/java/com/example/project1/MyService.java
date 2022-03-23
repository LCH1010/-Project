package com.example.project1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.project1.adapter.PersonAdapter;
import com.example.project1.data.PostTimeData;
import com.example.project1.fragment.RadioFragment;

public class MyService extends Service {
    private String Tag = MyService.class.getSimpleName();

    MediaPlayer mMPlayer;
    private String mUrl = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mUrl = intent.getStringExtra("url");
        Log.d(Tag, "chlee" + mUrl);

        //서비스가 실행될 때 실행
        mMPlayer = MediaPlayer.create(this, Uri.parse(mUrl));  //mp3시작 및 반복
        mMPlayer.setLooping(true);
        mMPlayer.start(); //음악 시작
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //서비스가 종료될 때 실행
        mMPlayer.stop(); //음악 종료
        super.onDestroy();
    }
}