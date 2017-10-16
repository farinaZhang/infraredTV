package com.sample.inferentdemo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.HorizontalListViewAdapter;
import com.sample.inferentdemo.adapter.PlayContentRecycleAdapter;
import com.sample.inferentdemo.data.PlayContentEntity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by FarinaZhang on 2017/9/18.
 */
public class MainShowView extends RelativeLayout implements View.OnClickListener{
    @InjectView(R.id.title_tab_item)
    HorizontalListView mTitleTabHListView;

    @InjectView(R.id.play_recyleview)
    RecyclerView mPlayRecyleView;

    private Context mContext;
    private Handler mHandler;
    private HorizontalListViewAdapter mPlayTypeAdapter;
    private PlayContentRecycleAdapter mPlayContentAdapter;
    private final int  COLUMN_COUNT = 2; //横屏4，竖屏2；

    public interface  OnMyHListClickLister{
        public void onMyHListClick(int position);
    }
    private OnMyHListClickLister mClickListener;

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

        //初始化 tab 栏电视节目种类
        String[] title = mContext.getResources().getStringArray(R.array.play_type_array);
        mPlayTypeAdapter = new HorizontalListViewAdapter(mContext, title);
        mTitleTabHListView.setAdapter(mPlayTypeAdapter);
        mTitleTabHListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mTitleTabHListView.getSelectedItemPosition() == position) return;

                if(mPlayRecyleView.getScrollState() != mPlayRecyleView.SCROLL_STATE_IDLE){
                    mPlayRecyleView.stopScroll();
                }
                mPlayTypeAdapter.setSelectIndex(position);
                if(mClickListener!=null){
                    mClickListener.onMyHListClick(position);
                }
            }
        });

        //初始化播放节目，搜索结果的列表

        mPlayRecyleView.setLayoutManager(new GridLayoutManager(mContext, COLUMN_COUNT));//打大屏4列
        mPlayRecyleView.addItemDecoration(new SpacesItemDecoration(COLUMN_COUNT, 12));
        mPlayContentAdapter = new PlayContentRecycleAdapter(mContext,data);
        mPlayRecyleView.setAdapter(mPlayContentAdapter);

    }
    public void setHandler(Handler h){
        mHandler = h;
    }

    @Override
    public void onClick(View view) {

    }

    public void setListener(OnMyHListClickLister listener){
        mClickListener = listener;
    }

    public void setData(List<PlayContentEntity> data){
        //更新数据，并刷新

        mPlayContentAdapter.setData(data);

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

}
