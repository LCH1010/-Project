package com.example.project1.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.adapter.PersonAdapter;
import com.example.project1.data.personArrData;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;


public class ExoPodcastFragment extends Fragment {
    // 회사규약 Log찍는 선언
    private String mTag = ExoPodcastFragment.class.getSimpleName();
    View.OnClickListener mOnClickListener;

    private int mArrPos = 0;

    private PlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;

    Context mContext;
    PersonAdapter mAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MediaItem mediaItem;

    ImageButton mIb_exo_prev;
    ImageButton mIb_exo_next;

    public interface PrevListener {
        void onPrevEvent(int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mContext = getContext();

        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.exo_fragment_podcast, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mExoPlayer = new SimpleExoPlayer.Builder(mContext).build();

        mExoPlayerView = mView.findViewById(R.id.video_view);
        mIb_exo_prev = mView.findViewById(R.id.exo_prev);
        mIb_exo_next = mView.findViewById(R.id.exo_next);

        mExoPlayerView.setPlayer(mExoPlayer);

        // 커스텀을 이용해 next, prev 부분 완료하지 못하여 주석 처리 했습니다.
//        mOnClickListener = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                switch (v.getId()) {
//
//                    case R.id.exo_prev:
//                        Log.d(mTag, "chlee = " + "exo_prev");
//                        if (mExoPlayer != null) {
//                            if (mArrPos > -1) {
//                                Log.d(mTag, "chlee = " + "mArrPos");
//                                mArrPos--;
//                                Log.d(mTag, "chlee = " + mArrPos);
//
//                                mPrevListener.onPrevEvent(mArrPos);
//
//                            }
//                        }
//                        break;
//
//                    case R.id.exo_next:
//                        Log.d(mTag, "chlee = " + "exo_next");
//                        if (mExoPlayer != null) {
//                            if (mArrPos < -1) {
//                                mArrPos++;
//                                mPrevListener.onPrevEvent(mArrPos);
//                            }
//                        }
//                        break;
//                }
//            }
//        };
//
//        exo_prev.setOnClickListener(mOnClickListener);
//        exo_next.setOnClickListener(mOnClickListener);

        PersonAdapter.SampleEventListener Adapterlistener = new PersonAdapter.SampleEventListener() {
            @Override
            public void onReceivedEvent(String pUrl, int pPosition) {
                Log.d(mTag, "chlee2 url = " + pUrl);
                Log.d(mTag, "chlee2 pPosition = " + pPosition);
                mArrPos = pPosition;
                Log.d(mTag, "chlee2 mArrPos = " + mArrPos);
                exoplay(pUrl);
            }
        };

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PersonAdapter(mContext, Adapterlistener);

        ArrayList<personArrData> mPersonArrData = new ArrayList<personArrData>();

        mPersonArrData.add(new personArrData("컬투쇼 레전드 사연", "https://podcastfile2.sbs.co.kr/powerfm/2019/01/podcast-v0000364436-20190121-2.mp3?vod_id=V0000364436&podcast_id=P0000000300"));
        mPersonArrData.add(new personArrData("2시의 데이트 지석진입니다", "http://podcastfile.imbc.gscdn.com/merge/date/DATE_20210309.mp3/1070_$/DATE_20210309.mp3"));
        mPersonArrData.add(new personArrData("씨네 마운틴", "https://rss.art19.com/episodes/62e2d558-7bde-44c7-bc97-b5d6e23c03af.mp3"));

        mAdapter.addItem(mPersonArrData);

        mRecyclerView.setAdapter(mAdapter);
        return mView;
    }

    private void exoplay(String url) {
        mediaItem = MediaItem.fromUri(url);
        mExoPlayer.setMediaItem(mediaItem);

        MediaItem firstItem = MediaItem.fromUri("https://podcastfile2.sbs.co.kr/powerfm/2019/01/podcast-v0000364436-20190121-2.mp3?vod_id=V0000364436&podcast_id=P0000000300");
        MediaItem secondItem = MediaItem.fromUri("http://podcastfile.imbc.gscdn.com/merge/date/DATE_20210309.mp3/1070_$/DATE_20210309.mp3");
        MediaItem thirdItem = MediaItem.fromUri("https://rss.art19.com/episodes/62e2d558-7bde-44c7-bc97-b5d6e23c03af.mp3");
        mExoPlayer.addMediaItem(firstItem);
        mExoPlayer.addMediaItem(secondItem);
        mExoPlayer.addMediaItem(thirdItem);

        mExoPlayer.prepare();
        // Start the playback.
        mExoPlayer.play();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mExoPlayer.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}