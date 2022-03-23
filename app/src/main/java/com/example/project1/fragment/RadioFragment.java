package com.example.project1.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.project1.MyService;
import com.example.project1.R;
import com.example.project1.data.RadioJsonArrData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RadioFragment extends Fragment {

    // 멤버변수 선언
    View.OnClickListener mOnClickListener;
    Context mContext;

    // 멤버변수 선언할때 단독으로 쓰일거면 privatr 붙여주기
    // 회사규약 Log찍는 선언
    private String Tag = RadioFragment.class.getSimpleName();
    private final String PREF_NAME = "spPage";

    private String mUrl = null;
    private String mChannelName = null;
    private String mThumbUrl = null;
    private String mFrequencyNum = null;

    private int mPosition = -1;
    private int mCurPosition = 0;
    private int mOn = 1;
    private int mOff = 0;
    private Intent intent;

    private static final int MESSAGE_TIMER = 100;

    private ArrayList<RadioJsonArrData> mRadioArr = new ArrayList<>();

    // 사용할 MediaPlayer 선언 (라이브러리)
    MediaPlayer mMPlayer;

    // SharedPreferences = 간단한 데이터를 파일 형식으로 저장할수 있는 라이브러리
    private SharedPreferences sp;
    // 사용할 SharedPreferences 에 데이터를 넣기위한 에디터
    SharedPreferences.Editor editor;

    // 사용할 Gson 선언(라이브러리)
    Gson gson = new Gson();

    TextView mTvThumbnail;
    TextView mTvFrequency;
    TextView mTvCurrentTime;

    RadioGroup mRGroup;
    RadioButton mRbOne;
    RadioButton mRbTwo;
    RadioButton mRbThree;
    RadioButton mRbFour;
    RadioButton mRbFive;
    RadioButton mRbSix;

    ImageButton mIbPlayPause;
    ImageButton mIbPrev;
    ImageButton mIbNext;

    Button mBHandler;
    Button mBstart;
    Button mBstop;

    ImageView mIvHeart;
    ImageView mIvThumbnail;

    RelativeLayout mRlLoadingPanel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.fragment_radio, container, false);

        mRGroup = mView.findViewById(R.id.radioGroup);

        mRbOne = mView.findViewById(R.id.rb_btn_one);
        mRbTwo = mView.findViewById(R.id.rb_btn_two);
        mRbThree = mView.findViewById(R.id.rb_btn_three);
        mRbFour = mView.findViewById(R.id.rb_btn_four);
        mRbFive = mView.findViewById(R.id.rb_btn_five);
        mRbSix = mView.findViewById(R.id.rb_btn_six);

        mIbPlayPause = mView.findViewById(R.id.ib_PlayPause);
        mIbPrev = mView.findViewById(R.id.ib_prev);
        mIbNext = mView.findViewById(R.id.ib_next);
        mIvHeart = mView.findViewById(R.id.ib_Heart);

        mBHandler = mView.findViewById(R.id.b_handler);
        mBstart = mView.findViewById(R.id.start);
        mBstop = mView.findViewById(R.id.stop);

        mTvFrequency = mView.findViewById(R.id.tv_frequency);
        mTvThumbnail = mView.findViewById(R.id.tv_thumbnail);
        mIvThumbnail = mView.findViewById(R.id.iv_thumbnail);
        mTvCurrentTime = mView.findViewById(R.id.tv_current_time);

        mRlLoadingPanel = mView.findViewById(R.id.rl_loading_panel);
        mContext = getContext();

        sp = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        intent = new Intent(mContext, MyService.class);

        isServiceRunning(mContext);
        //로딩화면 제거
        mRlLoadingPanel.setVisibility(View.GONE);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRlLoadingPanel.getVisibility() != View.VISIBLE) {
                    handler.removeMessages(MESSAGE_TIMER);
                    if (isServiceRunning(mContext)==true){
                        mContext.stopService(intent);
                    }
                    switch (v.getId()) {
                        // PlayPause 버튼 눌렀을때의 이벤트
                        case R.id.ib_PlayPause:
                            if (mMPlayer != null) {
                                if (mMPlayer.isPlaying()) {
                                    mIbPlayPause.setSelected(true);
                                    mMPlayer.pause();
                                } else {
                                    mIbPlayPause.setSelected(false);
                                    mMPlayer.start();
                                    mMPlayer.setLooping(true);
                                }
                            }
                            break;
                        // Prev 버튼 눌렀을때의 이벤트
                        case R.id.ib_prev:
                            if (mMPlayer != null) {
                                mPosition--;
                                channel(mPosition);
                            }
                            break;
                        // Next 버튼 눌렀을때의 이벤트
                        case R.id.ib_next:
                            if (mMPlayer != null) {
                                mPosition++;
                                channel(mPosition);
                            }
                            break;
                        // handler_button 눌렀을때의 이벤트
                        case R.id.b_handler:
                            if (mMPlayer != null) {
                                Log.d(Tag, "chlee" + mBHandler);
                                handler.sendEmptyMessage(MESSAGE_TIMER);
                            }
                            break;
                        // 즐겨찾기 heart 버튼 눌렀을때의 이벤트
                        case R.id.ib_Heart:
                            Log.d("Tag", "chlee" + mIvHeart);
                            if (intgetPre(String.valueOf(mPosition)) == mOff) {
                                mIvHeart.setBackgroundResource(R.drawable.music_ico_heart_on);
                                setPre(String.valueOf(mPosition), mOn);

                            } else {
                                mIvHeart.setBackgroundResource(R.drawable.music_ico_heart_off);
                                setPre(String.valueOf(mPosition), mOff);
                            }
                            break;
                        case R.id.start:
                            intent.putExtra("url", mUrl);
                            mContext.startService(intent);
                            break;

                      /*  case R.id.stop:
                            mContext.stopService(intent);
                            break;*/
                    }
                }
            }
        };

        mIbPlayPause.setOnClickListener(mOnClickListener);
        mIbPrev.setOnClickListener(mOnClickListener);
        mIbNext.setOnClickListener(mOnClickListener);
        mBHandler.setOnClickListener(mOnClickListener);
        mIvHeart.setOnClickListener(mOnClickListener);
        mBstart.setOnClickListener(mOnClickListener);
        mBstop.setOnClickListener(mOnClickListener);

        mRGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // 클릭되는 값마다 다른 값이 출력 될수 있도록 switch 사용
                if (mRlLoadingPanel.getVisibility() != View.VISIBLE) {
                    // 타이머 기능
                    handler.removeMessages(MESSAGE_TIMER);
                    // 버튼 클릭시 로딩 기능 종료

                    mRbOne.setEnabled(false);
                    mRbTwo.setEnabled(false);
                    mRbThree.setEnabled(false);
                    mRbFour.setEnabled(false);
                    mRbFive.setEnabled(false);
                    mRbSix.setEnabled(false);

                    if (isServiceRunning(mContext)==true){
                        mContext.stopService(intent);
                    }

                    switch (checkedId) {

                        case R.id.rb_btn_one:
                            mPosition = 0;
                            mCurPosition = 0;
                            channel(mPosition);
                            mIbPlayPause.setSelected(false);
                            //로딩화면 구현
                            mView.findViewById(R.id.rl_loading_panel).setVisibility(View.VISIBLE);

                            break;

                        case R.id.rb_btn_two:
                            mPosition = 1;
                            mCurPosition = 1;
                            channel(mPosition);
                            mIbPlayPause.setSelected(false);
                            mView.findViewById(R.id.rl_loading_panel).setVisibility(View.VISIBLE);
                            break;

                        case R.id.rb_btn_three:
                            mPosition = 2;
                            mCurPosition = 2;
                            channel(mPosition);
                            mIbPlayPause.setSelected(false);
                            mView.findViewById(R.id.rl_loading_panel).setVisibility(View.VISIBLE);
                            break;

                        case R.id.rb_btn_four:
                            mPosition = 3;
                            mCurPosition = 3;
                            channel(mPosition);
                            mIbPlayPause.setSelected(false);
                            mView.findViewById(R.id.rl_loading_panel).setVisibility(View.VISIBLE);
                            break;

                        case R.id.rb_btn_five:
                            mPosition = 4;
                            mCurPosition = 4;
                            channel(mPosition);
                            mIbPlayPause.setSelected(false);
                            mView.findViewById(R.id.rl_loading_panel).setVisibility(View.VISIBLE);
                            break;

                        case R.id.rb_btn_six:
                            mPosition = 5;
                            mCurPosition = 5;
                            channel(mPosition);
                            mIbPlayPause.setSelected(false);
                            mView.findViewById(R.id.rl_loading_panel).setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });
        return mView;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_TIMER:
                    if (mPosition < 6) {
                        Log.d(Tag, "chlee mCurPosition = " + mCurPosition);
                        Log.d(Tag, "chlee position = " + mPosition);
                        // 초기 버튼 클릭시 position 과 mCurPosition 같아서 채널 함수를 타지 않음
                        // 그렇기 때문에 채널 버튼 클릭후 자동 NEXT 버튼 클릭했을때 끊김 없이 진행된다.
                        if (mPosition != mCurPosition) {
                            channel(mPosition);
                        }
                        // 포지션 값을 1씩 증가시키고,
                        // 채널 함수 밑에 위치 함으로써 바로 다음 채널로 넘어감을 막는다.
                        mPosition++;
                        // 메세지를 5초씩 딜레이를 시켜 핸들러에게 전달함으로써 해당 채널이 5초 뒤에 다음 채널로 증가한다.
                        handler.sendEmptyMessageDelayed(MESSAGE_TIMER, 5000);
                        Log.d(Tag, "chlee sendEmptyMessageDelayed mCurPosition = " + mCurPosition);
                        Log.d(Tag, "chlee sendEmptyMessageDelayed position = " + mPosition);
                    } else {
                        Log.d(Tag, "chlee else" + mPosition);
                        // 포지션이 7이상일 경우 포지션을 0으로 초기화 시키고, 핸들러 메세지를 5초 후에 보낸다.
                        // 6채널 이후로 1번으로 돌아가 반복 재생
                        mPosition = 0;
                        handler.sendEmptyMessageDelayed(MESSAGE_TIMER, 5000);

                        // 마지막채널이 계속 재생되면서 핸들러를 삭제시킨다.
//                        handler.removeMessages(MESSAGE_TIMER);
                        // 토스트 메세지를 화면에 띄우면서 알려준다.
//                        Toast.makeText(getContext().getApplicationContext(), "마지막 채널입니다.", Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }
    };

    private void playAudio(String url) {

        // 미디어 플레이어가 사용되고 있을경우
        if (mMPlayer != null) {
            //미디어 플레이어 리소스 초기화
            mMPlayer.reset();

        }
        try {
            //재생 대상 설정
            mMPlayer.setDataSource(url);

            // 재생 준비
            mMPlayer.prepareAsync();
            mMPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    // 재생직전 로딩 기능 멈춤
                    mRbOne.setEnabled(true);
                    mRbTwo.setEnabled(true);
                    mRbThree.setEnabled(true);
                    mRbFour.setEnabled(true);
                    mRbFive.setEnabled(true);
                    mRbSix.setEnabled(true);
//                    로딩제거
                    mRlLoadingPanel.setVisibility(View.GONE);

                    Log.d("chlee", "setOnPreparedListener");
                    mp.start();
                }
            });

            // 재생 시작
            mMPlayer.start();

            //에러가 발생한다면 근원지를 찾아서 단계별로 에러를 출력
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playPause() {
        if (mMPlayer != null && mMPlayer.isPlaying()) {
            mIbPlayPause.setSelected(false);
            // Position 변수에 재생중인 시점을 기억하고 멈춘다.
            mPosition = mMPlayer.getCurrentPosition();
            mMPlayer.pause();
        } else {
            mIbPlayPause.setSelected(true);
        }
    }

    // Json
    private void jsonData() {
        Log.d(Tag, "chlee JasonData check ");
        try {

            InputStream is = getResources().getAssets().open("Radio.json");
            int fileSize = is.available();
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray RadioArray = jsonObject.getJSONArray("Radio");
            // 방어코드를 add전에 선언함으로써 데이터를 초기화 해서 중복 add를 막는 역할
            mRadioArr.clear();
            for (int i = 0; i < RadioArray.length(); i++) {
                RadioJsonArrData Radio = gson.fromJson(RadioArray.get(i).toString(), RadioJsonArrData.class);
                mRadioArr.add(Radio);
            }
            Log.d(Tag, "chlee mRadioArr = " + mRadioArr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Json을 동적으로 가져오기 위한 공통부분 함수
    private void initChannel(int position) {
        Log.d(Tag, "chlee initChannel position = " + position);
        jsonData();
        mFrequencyNum = mRadioArr.get(position).getFrequencyText();
        mChannelName = mRadioArr.get(position).getThumbnailText();
        mThumbUrl = mRadioArr.get(position).getGlide();
        mUrl = mRadioArr.get(position).getUrl();

        mTvFrequency.setText(mFrequencyNum);
        mTvThumbnail.setText(mChannelName);
        playAudio(mUrl);
        // 글라이더 활용 이미지 로딩, With : 해당 환경의 context , load : url 경로 , into : 받아온 이미지 공간
        Glide.with(this).load(mThumbUrl).into(mIvThumbnail);

//        채널 버튼을 눌렀을때 intgetPre 함수를 통해 데이터가 저장되어 있는지 확인하고 하트의 이미지를 상태에 맞게 바꿔준다 UI 변경 역할
        Log.d(Tag, "chlee initChannel position = " + intgetPre(String.valueOf(position)));
        if (intgetPre(String.valueOf(position)) == mOff) {
            mIvHeart.setBackgroundResource(R.drawable.music_ico_heart_off);
        } else {
            mIvHeart.setBackgroundResource(R.drawable.music_ico_heart_on);
        }

    }

    private void channel(int position) {
        Log.d(Tag, "chlee Channel position = " + position);

        switch (position) {

            case 0:
                // 옵션객체에 클릭이벤트를 등록 후 옵션버튼 클릭된 것으로 변경
                mRbOne.setChecked(true);
                initChannel(position);
                break;

            case 1:
                mRbTwo.setChecked(true);
                initChannel(position);
                break;

            case 2:
                mRbThree.setChecked(true);
                initChannel(position);
                break;

            case 3:
                mRbFour.setChecked(true);
                initChannel(position);
                break;

            case 4:
                mRbFive.setChecked(true);
                initChannel(position);
                break;

            case 5:
                mRbSix.setChecked(true);
                initChannel(position);
                break;
        }
    }

    public void setPre(String key, int value) {
        editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
        Log.d(Tag, "chlee getAll = " + sp.getAll());
    }

    public int intgetPre(String key) {
        return sp.getInt(key, mOff);
    }

    public static boolean isServiceRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(rsi.service.getClassName()))
                return true;
        }
        return false;

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag, "chlee onResume ");

    }

    @Override
    public void onStart() {
        super.onStart();
        mMPlayer = new MediaPlayer();
        Log.d(Tag, "chlee onStart ");
        //하트 UI 상태 초기화
        if (intgetPre(String.valueOf(mPosition)) == mOff) {
            mIvHeart.setBackgroundResource(R.drawable.music_ico_heart_off);
        } else {
            mIvHeart.setBackgroundResource(R.drawable.music_ico_heart_on);
        }
        playPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Tag, "chlee onStop ");
        if (mMPlayer != null) {
            mMPlayer.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Tag, "chlee onDestroy ");
        if (mMPlayer != null) {
            mMPlayer.release();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Tag, "chlee onPause ");
    }
}