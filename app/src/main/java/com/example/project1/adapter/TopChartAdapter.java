package com.example.project1.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project1.PodcastDB;
import com.example.project1.R;
import com.example.project1.data.TopChartDataList;

import java.util.ArrayList;

public class TopChartAdapter extends RecyclerView.Adapter<TopChartAdapter.podViewHolder> {
    // 회사 규약 로그 찍는법
    private String Tag = TopChartAdapter.class.getSimpleName();
    View.OnClickListener mOnClickListener;
    private String mImageUrl;
    private ImageView mivTcRecycler;
    private TextView mTvTcRecycler;
    ImageView mIvPcHeart;

    Cursor cur;
    String res;
    String mColName;
    boolean poddb = false;

    Context mContext;

    PodcastDB dbHelper;
    SQLiteDatabase database;

    private ArrayList<TopChartDataList> mPodcastArr = new ArrayList<>();

    public void setData(ArrayList<TopChartDataList> podcastarr) {
        mPodcastArr = podcastarr;
        notifyDataSetChanged();
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public TopChartAdapter(Context pContext) {
        mContext = pContext;
    }

    @NonNull
    @Override
    // viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
    public TopChartAdapter.podViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View podItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tc_holder_view, parent, false);


        dbHelper = new PodcastDB(mContext);
        database = dbHelper.getWritableDatabase();
        mColName = null;

        return new TopChartAdapter.podViewHolder(podItemView);
    }


    @Override
    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    public void onBindViewHolder(@NonNull TopChartAdapter.podViewHolder holder, int position) {
        holder.setItem(mPodcastArr.get(position));
    }


    @Override
    // 전체 아이템 갯수 리턴.
    public int getItemCount() {
        return mPodcastArr.size();
    }

    public class podViewHolder extends RecyclerView.ViewHolder {

        public podViewHolder(View podItemView) {
            super(podItemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mivTcRecycler = podItemView.findViewById(R.id.tc_recycler_imageView);
            mTvTcRecycler = podItemView.findViewById(R.id.tc_recycler_textView);
            mIvPcHeart = podItemView.findViewById(R.id.iv_Pc_Heart);
        }

        public void setItem(TopChartDataList podcast_dataList) {
            mTvTcRecycler.setText(podcast_dataList.getCollectionName());
            mImageUrl = podcast_dataList.getArtworkUrl600();
            Glide.with(mivTcRecycler).load(mImageUrl).into(mivTcRecycler);

            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {

                        case R.id.iv_Pc_Heart:

                            mColName = podcast_dataList.getCollectionName();

                            Log.d(Tag, "chlee" + mColName);

                            try {
                                database = dbHelper.getWritableDatabase();
                                database.execSQL("INSERT INTO podcastdb VALUES ('" + mColName + "');");
                                database.close();
                                heartonoff(true);
                            } catch (Exception e) {
                            }
                            break;

                        case R.id.tc_recycler_imageView:
                            mColName = podcast_dataList.getCollectionName();
                            select(mColName);
                            break;
                    }
                }
            };

            mivTcRecycler.setOnClickListener(mOnClickListener);
            mIvPcHeart.setOnClickListener(mOnClickListener);
        }
    }

    private void podcastdb(Boolean Check) {
        if (Check) {
            try {
                database = dbHelper.getWritableDatabase();
                database.execSQL("DELETE FROM podcastdb WHERE NAME = '" + mColName + "';");
                database.close();
                heartonoff(false);
            } catch (Exception e) {
            }
        } else {
            try {
                database = dbHelper.getWritableDatabase();
                database.execSQL("INSERT INTO podcastdb VALUES ('" + "mColName" + "','" + mColName + "')");
                database.close();
                heartonoff(true);
            } catch (Exception e) {
            }
        }
    }

    private void select(String pName) {
        Log.d(Tag, "select chlee" + pName);
        try {
            database = dbHelper.getReadableDatabase();
            cur = database.rawQuery("SELECT * FROM podcastdb" + mColName, null);
            res = "";
            if (cur != null) {
                while (cur.moveToNext()) {
                    res = res + cur.getString(0) + cur.getString(1) + "\n";
                    Log.d(Tag, "chlee res = " + cur.getString(0));
                    Log.d(Tag, "chlee res = " + cur.getString(1));
                    Log.d(Tag, "chlee res = " + res);
                    if (cur.getString(1).equals(mColName)) {
                        podcastdb(false);
                        Log.d(Tag, "true");
                        break;
                    } else {
                        podcastdb(true);
                        Log.d(Tag, "false");
                    }
                }
            }
            cur.close();
            database.close();
        } catch (Exception e) {
            Log.d(Tag, "데이터 확인실패");
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }

    private void heartonoff(boolean on) {
        if (on) {
            mIvPcHeart.setImageResource(R.drawable.music_ico_heart_on);
        } else {
            mIvPcHeart.setImageResource(R.drawable.music_ico_heart_off);
        }
    }
}