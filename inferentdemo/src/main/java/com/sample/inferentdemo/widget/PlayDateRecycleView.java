package com.sample.inferentdemo.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.PlayTimeListAdapter;
import com.sample.inferentdemo.adapter.PlayTimeTabAdapter;
import com.sample.inferentdemo.data.PlayTimeData;
import com.sample.inferentdemo.data.PlayTimeDataAll;
import com.sample.inferentdemo.data.PlayTimeEntity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by FarinaZhang on 2017/9/1.
 */
public class PlayDateRecycleView extends RelativeLayout {


    @InjectView(R.id.play_time_list)
    RecyclerView mPlayTimeList;

    @InjectView(R.id.tv_info)
    TextView mTextViewInfo;

    private Handler mHandler;
    private Context mContext;
    private PlayTimeTabAdapter mTabAdapter;

    private PlayTimeListAdapter mListAdapter;
    private int mSelectedPage = 0;


    public PlayDateRecycleView(Context context) {
        this(context, null);
    }

    public PlayDateRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayDateRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        View view = View.inflate(mContext, R.layout.layout_play_date_recycleview, null);
        this.addView(view);

        ButterKnife.inject(this);

    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void initView()
    {
        mPlayTimeList.setLayoutManager(new LinearLayoutManager(mContext));
        if(PlayTimeDataAll.mPlayTimeDataAll == null ||PlayTimeDataAll.mPlayTimeDataAll.size()<1){
            PlayTimeData.mPlayTimeData = null;
        }else{
            PlayTimeData.mPlayTimeData = PlayTimeDataAll.mPlayTimeDataAll.get(0);
        }
        mListAdapter = new PlayTimeListAdapter(mContext, PlayTimeData.mPlayTimeData);
        mPlayTimeList.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickLitener(new PlayTimeListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                ItemClickFunction(view, pos);
            }

            @Override
            public void OnItemLongClick(View view, int pos) {
                ItemClickFunction(view, pos);
            }
        });

    }

    public void updateView()
    {
        if(PlayTimeDataAll.mPlayTimeDataAll == null ||PlayTimeDataAll.mPlayTimeDataAll.size()<1){
            PlayTimeData.mPlayTimeData = null;
            mTextViewInfo.setVisibility(View.VISIBLE);
        }else{
            PlayTimeData.mPlayTimeData = PlayTimeDataAll.mPlayTimeDataAll.get(mSelectedPage);
                mTextViewInfo.setVisibility(View.GONE);
        }
        mListAdapter.setData(PlayTimeData.mPlayTimeData);
        mListAdapter.notifyDataSetChanged();
    }

    private void ItemClickFunction(View view, int pos){
        PlayTimeEntity entity= PlayTimeData.mPlayTimeData.get(pos);
    }
}