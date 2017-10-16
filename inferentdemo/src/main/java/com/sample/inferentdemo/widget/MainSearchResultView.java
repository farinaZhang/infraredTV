package com.sample.inferentdemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.HorizontalListViewAdapter;
import com.sample.inferentdemo.adapter.PlayContentRecycleAdapter;
import com.sample.inferentdemo.data.PlayContentEntity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by FarinaZhang on 2017/9/21.
 */
public class MainSearchResultView extends RelativeLayout implements View.OnClickListener{
    @InjectView(R.id.playing)
    TextView mPlayingTab;
    @InjectView(R.id.play_time)
    TextView mPlayingTime;

    @InjectView(R.id.play_recyleview)
    RecyclerView mPlayRecyleView;

    /*@InjectView(R.id.playdataview)
    PlayDateView mSubSearchPlayDate;//搜索结果播放日期 */
    @InjectView(R.id.playdataview)
    PlayDateViewExt mSubSearchPlayDate;//搜索结果播放日期



    private Context mContext;
    private Handler mHandler;
    private HorizontalListViewAdapter mPlayTypeAdapter;
    private PlayContentRecycleAdapter mPlayContentAdapter;
    private String[] mTitle;


    public MainSearchResultView(Context context) {
        this(context, null);
    }

    public MainSearchResultView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainSearchResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        View view = View.inflate(mContext,R.layout.layout_main_search_result_view,null);
        this.addView(view);
        ButterKnife.inject(this);

    }

    public void setHTitleString(String[] title){
        mTitle = title;
    }

    public void InitView(List<PlayContentEntity> data){

        //初始化 tab 栏电视节目种类
        setSelectedTab(0);

        mPlayTypeAdapter = new HorizontalListViewAdapter(mContext, mTitle);

        mPlayingTab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedTab(0);

            }
        });

        mPlayingTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedTab(1);

            }
        });


        //初始化播放节目，搜索结果的列表
        mPlayRecyleView.setLayoutManager(new GridLayoutManager(mContext,4));//打大屏4列
        mPlayRecyleView.addItemDecoration(new SpacesItemDecoration(4,13));
        mPlayContentAdapter = new PlayContentRecycleAdapter(mContext,data);
        mPlayRecyleView.setAdapter(mPlayContentAdapter);

        //初始化播放时间画面
        mSubSearchPlayDate.setVisibility(View.GONE);
        mSubSearchPlayDate.InitView();
    }
    public void setHandler(Handler h){
        mHandler = h;
        mSubSearchPlayDate.setHandler(mHandler);
    }

    @Override
    public void onClick(View view) {

    }

    public void setData(List<PlayContentEntity> data){
        //更新数据，并刷新
        mPlayContentAdapter.setData(data);
    }

    //更新播放日期数据
    public void updataPlayDate(){
        mSubSearchPlayDate.setCurSelPage(0);

        //mSubSearchPlayDate.updateTabTitle();
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        private int spanCount;

        public SpacesItemDecoration(int spanCount, int space) {
            this.spanCount = spanCount;
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);

            if(position==0 || position%spanCount !=0) {
                outRect.right = space;
            }

            // Add top margin only for the first item to avoid double space between items

        }
    }

    public void setSelectedTab(int position){

        if(position == 0){
            mPlayingTab.setTextColor(Color.rgb(0x2a,0xa1,0xde));
            mPlayingTime.setTextColor(Color.rgb(0x33,0x33,0x33));
            mPlayRecyleView.setVisibility(View.VISIBLE);
            mSubSearchPlayDate.setVisibility(View.GONE);
        }else{
            mPlayingTab.setTextColor(Color.rgb(0x33,0x33,0x33));
            mPlayingTime.setTextColor(Color.rgb(0x2a,0xa1,0xde));
            mPlayRecyleView.setVisibility(View.GONE);
            mSubSearchPlayDate.setVisibility(View.VISIBLE);
            mSubSearchPlayDate.updateRecycleView();
        }
    }
}
