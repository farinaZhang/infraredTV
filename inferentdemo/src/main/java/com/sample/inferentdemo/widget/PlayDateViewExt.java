package com.sample.inferentdemo.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.PlayTimeListAdapter;
import com.sample.inferentdemo.adapter.PlayTimeTabAdapter;
import com.sample.inferentdemo.data.PlayTimeData;
import com.sample.inferentdemo.data.PlayTimeEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by FarinaZhang on 2017/9/1.
 */
public class PlayDateViewExt extends RelativeLayout {
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    @InjectView(R.id.slideTabView)
    SlideTabView mslideTabView;

    private Handler mHandler;
    private Context mContext;
    private PlayTimeTabAdapter mTabAdapter;

    private PlayTimeListAdapter mListAdapter;
    private int mSelectedPage = 0;
    private List<String> mStrings;
    private List<PlayDateRecycleView> mList;
    private PagerAdapter mAdapter;
    private PlayDateRecycleView mPlayDateRecycleView1;
    private PlayDateRecycleView mPlayDateRecycleView2;
    private PlayDateRecycleView mPlayDateRecycleView3;
    private PlayDateRecycleView mPlayDateRecycleView4;
    private PlayDateRecycleView mPlayDateRecycleView5;
    private PlayDateRecycleView mPlayDateRecycleView6;
    private PlayDateRecycleView mPlayDateRecycleView7;

    public PlayDateViewExt(Context context) {
        this(context, null);
    }

    public PlayDateViewExt(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayDateViewExt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        View view = View.inflate(mContext, R.layout.layout_play_date_view_ext, null);
        this.addView(view);

        ButterKnife.inject(this);

    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void InitView() {
        mslideTabView = (SlideTabView) findViewById(R.id.slideTabView);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mStrings = new ArrayList<>();
        mList = new ArrayList<>();
        mPlayDateRecycleView1 = new PlayDateRecycleView(mContext);
        mPlayDateRecycleView2 = new PlayDateRecycleView(mContext);
        mPlayDateRecycleView3 = new PlayDateRecycleView(mContext);
        mPlayDateRecycleView4 = new PlayDateRecycleView(mContext);
        mPlayDateRecycleView5 = new PlayDateRecycleView(mContext);
        mPlayDateRecycleView6 = new PlayDateRecycleView(mContext);
        mPlayDateRecycleView7 = new PlayDateRecycleView(mContext);
        mPlayDateRecycleView1.initView();
        mPlayDateRecycleView2.initView();
        mPlayDateRecycleView3.initView();
        mPlayDateRecycleView4.initView();
        mPlayDateRecycleView5.initView();
        mPlayDateRecycleView6.initView();
        mPlayDateRecycleView7.initView();

        mList.add(mPlayDateRecycleView1);
        mList.add(mPlayDateRecycleView2);
        mList.add(mPlayDateRecycleView3);
        mList.add(mPlayDateRecycleView4);
        mList.add(mPlayDateRecycleView5);
        mList.add(mPlayDateRecycleView6);
        mList.add(mPlayDateRecycleView7);
        mAdapter = new RecycleviewPagerAdapter(mList);
        mViewPager.setAdapter(mAdapter);
        String [] dataArray = getCurPlayDateTitle();
        for(int i=0,size=dataArray.length; i<size; i++ )
            mStrings.add(dataArray[i]);
        mslideTabView.initTab(mStrings, mViewPager);

    }


    private void ItemClickFunction(View view, int pos){
        PlayTimeEntity entity= PlayTimeData.mPlayTimeData.get(pos);
    }

    public void setCurSelPage(int index){
        mSelectedPage = index;
    }

    public void updateRecycleView()
    {
        mPlayDateRecycleView1.updateView();
        mPlayDateRecycleView2.updateView();
        mPlayDateRecycleView3.updateView();
        mPlayDateRecycleView4.updateView();
        mPlayDateRecycleView5.updateView();
        mPlayDateRecycleView6.updateView();
        mPlayDateRecycleView7.updateView();
    }

    private String[] getCurPlayDateTitle(){
        Calendar c =  Calendar.getInstance();
        String weak = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        if(weak.equals("1")){
            String result[]={"今天","周一","周二","周三","周四","周五","周六"};
            return result;
        }else if(weak.equals("2")){
            String result[]={"今天","周二","周三","周四","周五","周六","周日"};
            return result;
        }else if(weak.equals("3")){
            String result[]={"今天","周三","周四","周五","周六","周日","周一"};
            return result;
        }else if(weak.equals("4")){
            String result[]={"今天","周四","周五","周六","周日","周一","周二"};
            return result;
        }else if(weak.equals("5")){
            String result[]={"今天","周五","周六","周日","周一","周二","周三"};
            return result;
        }else if(weak.equals("6")){
            String result[]={"今天","周六","周日","周一","周二","周三","周四"};
            return result;
        }else if(weak.equals("7")){
            String result[]={"今天","周日","周一","周二","周三","周四","周五"};
            return result;
        }

        String result[]={"今天","周一","周二","周三","周四","周五","周六"};
        return result;

    }

    private void ShowToast(String message){
        //Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();

        CustomOlaTipDialog dialog = CustomOlaTipDialog.createDialog(mContext);
        dialog.setMessage(message);
        dialog.show();
    }

}
