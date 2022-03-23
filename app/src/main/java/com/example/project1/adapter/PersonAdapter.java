package com.example.project1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.data.personArrData;
import com.example.project1.fragment.ExoPodcastFragment;

import java.util.ArrayList;

// 어댑터는 데이터와 뷰객체 관리 기능

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    // 회사 규약 로그 찍는법
    private String Tag = PersonAdapter.class.getSimpleName();

    ArrayList<personArrData> mPersonArrData = new ArrayList<personArrData>();
    Context mContext;
    int mPos;
    private SampleEventListener mSampleEventListener;

    public PersonAdapter(Context pContext, SampleEventListener listener) {
        mContext = pContext;
        mSampleEventListener = listener;
    }

    public interface SampleEventListener {
        void onReceivedEvent(String url,int pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.main_holder_view, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.setItem(mPersonArrData.get(position));
    }

    @Override
    public int getItemCount() {
        return mPersonArrData.size();
    }

    public void addItem(ArrayList<personArrData> arr) {
        mPersonArrData = arr;
    }

    // 리스트형태의 아이템을 뷰홀더에 담아둠
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        // 뷰홀더 생성자로 전달되는 뷰 객체 참조
        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPos = getAdapterPosition();
                    String item = null;
                    if (mPos != RecyclerView.NO_POSITION) {
                        Log.d(Tag, "chlee pos = " + mPos);
                        mSampleEventListener.onReceivedEvent(mPersonArrData.get(mPos).getUrl(),mPos);
                    }
                }
            });

            // 뷰 객체에 들어있는 텍스트뷰 참조
            textView = itemView.findViewById(R.id.recycler_textView);
        }
        ExoPodcastFragment.PrevListener testListener = new ExoPodcastFragment.PrevListener() {

            @Override
            public void onPrevEvent(int position) {
                mPos=position;
                mSampleEventListener.onReceivedEvent(mPersonArrData.get(mPos).getUrl(),mPos);
            }
        };

        public void setItem(personArrData item) {
            for (int pos = 0; pos<= mPersonArrData.size(); pos++ ) {
                textView.setText(item.getName());
            }
        }
    }
}
