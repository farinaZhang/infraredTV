package com.sample.inferentdemo.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.PlayTimeListAdapter;
import com.sample.inferentdemo.adapter.PlayTimeTabAdapter;
import com.sample.inferentdemo.data.PlayTimeData;
import com.sample.inferentdemo.data.PlayTimeDataAll;
import com.sample.inferentdemo.data.PlayTimeEntity;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by FarinaZhang on 2017/9/1.
 */
public class PlayDateView extends RelativeLayout {
    @InjectView(R.id.play_time_list)
    RecyclerView mPlayTimeList;

    @InjectView(R.id.title_tab_item)
    HorizontalListView mDateTab;

    private Handler mHandler;
    private Context mContext;
    private PlayTimeTabAdapter mTabAdapter;

    private PlayTimeListAdapter mListAdapter;
    private int mSelectedPage = 0;

    public PlayDateView(Context context) {
        this(context, null);
    }

    public PlayDateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        View view = View.inflate(mContext, R.layout.layout_play_date_view, null);
        this.addView(view);

        ButterKnife.inject(this);

    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void InitView() {
        //tab
        String temp[] = getCurPlayDateTitle();
        mTabAdapter = new PlayTimeTabAdapter(mContext, temp);
        mDateTab.setAdapter(mTabAdapter);
        mDateTab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mDateTab.getSelectedItemPosition() == position) return;

                if(PlayTimeDataAll.mPlayTimeDataAll.size() > position) {
                    mTabAdapter.setSelectIndex(position);
                    //跟新list 信息
                    setCurSelPage(position);
                }else{
                    ShowToast("缺少需要的数据");
                }
            }
        });
        //list

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

    private void ItemClickFunction(View view, int pos){
        PlayTimeEntity entity= PlayTimeData.mPlayTimeData.get(pos);
    }

    public void setCurSelPage(int index){
        mSelectedPage = index;
        updateData();
    }
    public void updateData(){

        if(PlayTimeDataAll.mPlayTimeDataAll == null ||PlayTimeDataAll.mPlayTimeDataAll.size()<1){
            PlayTimeData.mPlayTimeData = null;
        }else{
            PlayTimeData.mPlayTimeData = PlayTimeDataAll.mPlayTimeDataAll.get(mSelectedPage);
        }
        mListAdapter.setData(PlayTimeData.mPlayTimeData);

    }

    public void updateTabTitle(){
        String[] title = getCurPlayDateTitle();
        mTabAdapter.resetData(title);
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
