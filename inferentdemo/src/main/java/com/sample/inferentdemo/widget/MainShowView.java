package com.sample.inferentdemo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.HorizontalListViewAdapter;
import com.sample.inferentdemo.adapter.PlayContentRecycleAdapter;
import com.sample.inferentdemo.data.MainContentData;
import com.sample.inferentdemo.data.PlayContentEntity;
import com.sample.inferentdemo.message.MessageConst;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by FarinaZhang on 2017/9/18.
 */
public class MainShowView extends RelativeLayout {


    @InjectView(R.id.horizontalScrollView)
    HorizontalScrollView mHScrollView;

    @InjectView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    @InjectView(R.id.btn1)
    RadioButton mBtn1;

    @InjectView(R.id.btn2)
    RadioButton mBtn2;

    @InjectView(R.id.btn3)
    RadioButton mBtn3;

    @InjectView(R.id.btn4)
    RadioButton mBtn4;

    @InjectView(R.id.btn5)
    RadioButton mBtn5;

    @InjectView(R.id.btn6)
    RadioButton mBtn6;

    @InjectView(R.id.btn7)
    RadioButton mBtn7;

    @InjectView(R.id.btn8)
    RadioButton mBtn8;

    @InjectView(R.id.btn9)
    RadioButton mBtn9;

    @InjectView(R.id.btn10)
    RadioButton mBtn10;

    @InjectView(R.id.btn11)
    RadioButton mBtn11;

    @InjectView(R.id.btn12)
    RadioButton mBtn12;

    @InjectView(R.id.btn13)
    RadioButton mBtn13;


    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    private Context mContext;
    private Handler mHandler;
    private final String TAG = "MainShowView";
    private final int PAGE_COUNT=13;

    private List<String> mStrings;
    private List<PlayContentRecycleView> mList;
    private PagerAdapter mAdapter;

    private HorizontalListViewAdapter mPlayTypeAdapter;
    private PlayContentRecycleAdapter mPlayContentAdapter;
    private int curpage;

    private int mCurrentCheckedRadioLeft;
    private int win_width ;


    public MainShowView(Context context) {
        this(context, null);
    }

    public MainShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        View view = View.inflate(mContext,R.layout.layout_main_show_view,null);
        this.addView(view);
        ButterKnife.inject(this);
    }

    public void InitView(List<PlayContentEntity> data){
        curpage = 0;

        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        win_width = dm.widthPixels;
        //初始化 tab 栏电视节目种类
        

        mBtn1.setChecked(true);
        mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft(0);

        mRadioGroup.setOnCheckedChangeListener(MyTabCheckChangeListener);


        //初始化播放节目，搜索结果的列表
        mStrings = new ArrayList<String>();
        mList = new ArrayList<PlayContentRecycleView>();
        for(int i=0; i<PAGE_COUNT; i++){
            PlayContentRecycleView mPlayContentRecycleView = new PlayContentRecycleView(mContext);
            mPlayContentRecycleView.initView();
            mList.add(mPlayContentRecycleView);
        }
        mAdapter = new RecycleviewPagerAdapter(mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);

        mViewPager.addOnPageChangeListener(MyPageChangeListener);


        //初始化播放节目，搜索结果的列表


    }
    public void setHandler(Handler h){
        mHandler = h;
    }


    /**
     * 获得当前被选中的RadioButton距离左侧的距离
     */
    private int getCurrentCheckedRadioLeft(int i){
        int value = 0;

        value =  (int)(i* getResources().getDimension(R.dimen.main_show_tab_item_width));

        return value;
    }

    //tab item 选中变化时，滑动外层的scroolView，
    private RadioGroup.OnCheckedChangeListener MyTabCheckChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            Log.d(TAG,"RadioGroup  onCheckedChanged check item:"+checkedId);
            int i=1;
            switch(checkedId){
                case R.id.btn1:{
                    i=1;
                    break;
                }
                case R.id.btn2:{
                    i=2;
                    break;
                }
                case R.id.btn3:{
                    i=3;
                    break;
                }
                case R.id.btn4:{
                    i=4;
                    break;
                }
                case R.id.btn5:{
                    i=5;
                    break;
                }
                case R.id.btn6:{
                    i=6;
                    break;
                }
                case R.id.btn7:{
                    i=7;
                    break;
                }
                case R.id.btn8:{
                    i=8;
                    break;
                }
                case R.id.btn9:{
                    i=9;
                    break;
                }
                case R.id.btn10:{
                    i=10;
                    break;
                }
                case R.id.btn11:{
                    i=11;
                    break;
                }
                case R.id.btn12:{
                    i=12;
                    break;
                }
                case R.id.btn13:{
                    i=13;
                    break;
                }

            }
            mCurrentCheckedRadioLeft =getCurrentCheckedRadioLeft(i);

            if(mCurrentCheckedRadioLeft > win_width){
                mHScrollView.scrollTo((mCurrentCheckedRadioLeft - win_width),0);
            }

            if(mCurrentCheckedRadioLeft == 0){
                mHScrollView.scrollTo(0, 0);
            }

            Message msg = new Message();
            msg.what = MessageConst.CLENT_ACTION_GET_MAIN_DATA;
            msg.arg1 = i-1;
            curpage = i-1;
            mHandler.sendMessage(msg);
        }
    };

    private ViewPager.OnPageChangeListener MyPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            Log.d(TAG,"onPageSelected the selected page: "+i);
            switch(i){
                case 0:{
                    mBtn1.performClick();
                    break;
                }
                case 1:{
                    mBtn2.performClick();
                    break;
                }
                case 2:{
                    mBtn3.performClick();
                    break;
                }
                case 3:{
                    mBtn4.performClick();
                    break;
                }
                case 4:{
                    mBtn5.performClick();
                    break;
                }
                case 5:{
                    mBtn6.performClick();
                    break;
                }
                case 6:{
                    mBtn7.performClick();
                    break;
                }
                case 7:{
                    mBtn8.performClick();
                    break;
                }
                case 8:{
                    mBtn9.performClick();
                    break;
                }
                case 9:{
                    mBtn10.performClick();
                    break;
                }
                case 10:{
                    mBtn11.performClick();
                    break;
                }
                case 11:{
                    mBtn12.performClick();
                    break;
                }
                case 12:{
                    mBtn13.performClick();
                    break;
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void changeView(){
        //更新数据，并刷新,更新tab 及切换主页面
        //更新下一个的数据
        List<PlayContentEntity>  data=null;

        int size = MainContentData.playData.size();
        for(int i=0;i<size;i++){
            if(curpage == MainContentData.playData.get(i).index){
                data = MainContentData.playData.get(i).ContentData;
            }
        }
        mList.get(curpage).setData(data);
        mViewPager.setCurrentItem(curpage);
        Log.d("MainShowView"," the current page is:"+curpage);

    }

    private void setCurTabItem(int index){
        mPlayTypeAdapter.setSelectIndex(index);
    }


}
