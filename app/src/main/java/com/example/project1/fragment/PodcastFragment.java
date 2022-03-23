package com.example.project1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.adapter.TopChartAdapter;
import com.example.project1.api.ApiPodMain;
import com.example.project1.data.TopChartData;
import com.example.project1.data.TopChartDataList;

import java.util.ArrayList;

public class PodcastFragment extends Fragment {

    // 회사 규약 로그 찍는법
    private String Tag = PodcastFragment.class.getSimpleName();

    TopChartAdapter mTcAdapter;
    RecyclerView mTcRecyclerView;
    LinearLayoutManager mLayoutManager;

    private ArrayList<TopChartDataList> mPTopArr = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.tc_holder_view, container, false);

        View podcastTopChartView = inflater.inflate(R.layout.fragment_podcast, container, false);
        mTcRecyclerView = podcastTopChartView.findViewById(R.id.p_recyclerView);

        //리스트 가로로 보여주기 → LinearLayoutManager.HORIZONTAL
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTcRecyclerView.setLayoutManager(mLayoutManager);
        mTcAdapter = new TopChartAdapter(getContext());


        mTcRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = recyclerView.getAdapter().getItemCount();
                int totalCount = recyclerView.getAdapter().getItemCount();
                if (lastPosition == totalCount) {
                }
            }
        });
        mTcRecyclerView.setAdapter(mTcAdapter);
        initpodcastCheck();
        return podcastTopChartView;
    }

    public void initpodcastCheck() {
        new ApiPodMain().podcastCheck(new ApiPodMain.onPodListener() {
            @Override

            //★놓친부분 다시 공부하기★ 리사이클러뷰 불러오는 부분
            public void postpodcast(TopChartData Data) {
                mTcAdapter.setData(Data.getData());
            }
        });
    }

}