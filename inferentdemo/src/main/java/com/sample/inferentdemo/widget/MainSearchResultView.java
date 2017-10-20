package com.sample.inferentdemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.data.PlayContentEntity;

import java.util.ArrayList;
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

   

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;

    private Context mContext;
    private Handler mHandler;

    private List<View> mList;
    private PlayContentRecycleView mPlayContentView;
    private PlayDateView mPlayDataView;
    private MyPagerAdapter mAdapter;


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

    public void InitView(List<PlayContentEntity> data){

        //初始化 tab 栏电视节目种类


        mList = new ArrayList<View>();
        mPlayContentView = new PlayContentRecycleView(mContext);
        mPlayContentView.initView();
        mList.add(mPlayContentView);
        mPlayDataView = new PlayDateView(mContext);
        mPlayDataView.InitView();
        mList.add(mPlayDataView);

        PagerAdapter mAdapter= new MyPagerAdapter();
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(MyPagerChangeListener);

        setSelectedTab(0);
        mViewPager.setCurrentItem(0);

        mPlayingTab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);

            }
        });

        mPlayingTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);

            }
        });
    }
    public void setHandler(Handler h){
        mHandler = h;
        mPlayDataView.setHandler(mHandler);
    }

    @Override
    public void onClick(View view) {

    }

    public void setData(List<PlayContentEntity> data){
        //更新数据，并刷新
        mPlayContentView.setData(data);
    }

    //更新播放日期数据
    public void updataPlayDate(){
        mPlayDataView.setCurSelPage(0);
    }
    public void setSelectedTab(int position){

        if(position == 0){
            mPlayingTab.setTextColor(Color.rgb(0x2a,0xa1,0xde));
            mPlayingTime.setTextColor(Color.rgb(0x33,0x33,0x33));

        }else{
            mPlayingTab.setTextColor(Color.rgb(0x33,0x33,0x33));
            mPlayingTime.setTextColor(Color.rgb(0x2a,0xa1,0xde));
        }
    }

    private ViewPager.OnPageChangeListener MyPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            setSelectedTab(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private class MyPagerAdapter extends PagerAdapter{

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
        return mList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                 Object object) {
            // TODO Auto-generated method stub
             container.removeView(mList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
             // TODO Auto-generated method stub
            container.addView(mList.get(position));

            return mList.get(position);
        }

    }
}
