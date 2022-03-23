package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.project1.api.ApiMain;
import com.example.project1.data.PostTimeData;
import com.example.project1.data.mainthumbnailData;
import com.example.project1.fragment.ExoPodcastFragment;
import com.example.project1.fragment.PodcastFragment;
import com.example.project1.fragment.RadioFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 멤버변수
    Context mContext;
    View.OnClickListener mOnClickListener;

    Gson gson = new Gson();

    ImageButton mIbHome;

    Button mBPodcast;
    Button mBexoPodcast;
    Button mBradio;
    Button mBtime;

    ImageView mIv_main_Thumbnail;
    TextView mTv_main_Thumbnail;

    TextView mTv_CurrentTime;

    private String mthumbnailName = null;
    private String mThumbimage = null;
    private ArrayList<mainthumbnailData> mthumbnailArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentView(1);
        Log.d("chlee", "onCreate");
        mIbHome = findViewById(R.id.ib_home);
        mBtime = findViewById(R.id.b_time);
        mBPodcast = findViewById(R.id.b_podcast);
        mBexoPodcast = findViewById(R.id.b_exo_podcast);
        mBradio = findViewById(R.id.b_radio);
        mTv_CurrentTime = findViewById(R.id.tv_current_time);
        mTv_main_Thumbnail = findViewById(R.id.tv_main_thumbnail);
        mIv_main_Thumbnail = findViewById(R.id.iv_main_thumbnail);

        mContext = this;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // 홈 버튼 클릭시 홈화면 으로 이동 (intent로 연결)
                    case R.id.ib_home:
                        Intent intent = new Intent("com.citech.common.ACTION_ROSE_SERVICE");
                        intent.putExtra("BR_KEY", "com.citech.common.ACTION_HOME_COMMAND_EVENT");
                        sendBroadcast(intent);
                        break;
                    // 현재시간 업테이트 버튼을 눌렀을때의 이벤트
                    case R.id.b_time:
                        new ApiMain().ApiAlreadyCheck(timeListener);
                        break;

                    //프래그먼트
                    case R.id.b_radio:
                        fragmentView(1);
                        channel2(0);
                        break;

                    case R.id.b_exo_podcast:
                        fragmentView(2);
                        channel2(1);
                        break;

                    case R.id.b_podcast:
                        fragmentView(3);
                        channel2(2);
                        break;
                }
            }
        };

        mIbHome.setOnClickListener(mOnClickListener);
        mBexoPodcast.setOnClickListener(mOnClickListener);
        mBradio.setOnClickListener(mOnClickListener);
        mBtime.setOnClickListener(mOnClickListener);
        mBPodcast.setOnClickListener(mOnClickListener);

        ExoPodcastFragment.PrevListener prevListener;
    }

    //json
    private void jsonData2() {
        try {
            InputStream is = getResources().getAssets().open("mainthumbnail.json");
            int fileSize = is.available();
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray mainthumbnailArray = jsonObject.getJSONArray("mainthumbnail");
            // 방어코드를 add전에 선언함으로써 데이터를 초기화 해서 중복 add를 막는 역할
            mthumbnailArr.clear();
            for (int i = 0; i < mainthumbnailArray.length(); i++) {
                mainthumbnailData mainthumbnail = gson.fromJson(mainthumbnailArray.get(i).toString(), mainthumbnailData.class);
                mthumbnailArr.add(mainthumbnail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Json을 동적으로 가져오기 위한 공통부분 함수
    private void initChannel2(int position) {
        jsonData2();
        mthumbnailName = mthumbnailArr.get(position).getThumbnailText();
        mTv_main_Thumbnail.setText(mthumbnailName);

        mThumbimage = mthumbnailArr.get(position).getGlide();
        Glide.with(this).load(mThumbimage).into(mIv_main_Thumbnail);
    }

    private void channel2(int position) {
        switch (position) {
            case 0:
                initChannel2(position);
                break;
            case 1:
                initChannel2(position);
                break;
            case 2:
                initChannel2(position);
                break;
        }
    }

    ApiMain.onTimeListener timeListener = new ApiMain.onTimeListener() {
        public void PostTime(PostTimeData data) {

            mTv_CurrentTime.setText(data.getData().getCurrentDate());
        }
    };

    // 프래그먼트 연결
    // 메인 XML에 프래그먼트 레이아웃을 지정해 주면서 프래그먼트 공간만 왔다갔다 할수 있도록 함
    private void fragmentView(int fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragment) {
            case 1:
                // 첫번 째 프래그먼트 호출 , 라디오 프래그먼트
                RadioFragment radioFragment = new RadioFragment();
                transaction.replace(R.id.fragment_container, radioFragment);
                transaction.commit();
                break;

            case 2:
                // 두번 째 프래그먼트 호출 , EXO팟캐스트 프래그먼트
                ExoPodcastFragment exoPodcastFragment = new ExoPodcastFragment();
                transaction.replace(R.id.fragment_container, exoPodcastFragment);
                transaction.commit();
                break;

            case 3:
                // 세번 째 프래그먼트 호출
                PodcastFragment podcastFragment = new PodcastFragment();
                transaction.replace(R.id.fragment_container, podcastFragment);
                transaction.commit();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("chlee", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("chlee", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("chlee", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("chlee", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("chlee", "onDestroy");
    }
}