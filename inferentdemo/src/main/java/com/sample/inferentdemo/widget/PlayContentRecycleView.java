package com.sample.inferentdemo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.PlayContentRecycleAdapter;
import com.sample.inferentdemo.data.PlayContentEntity;

import java.util.List;

/**
 * Created by FarinaZhang on 2017/10/9.
 */
public class PlayContentRecycleView extends RelativeLayout{
    private Context mContext;
    private PlayContentRecycleAdapter mPlayContentAdapter;
    private RecyclerView mRecyclerView;

    public PlayContentRecycleView(Context context) {
        this(context,null);
    }

    public PlayContentRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayContentRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        View view = View.inflate(mContext, R.layout.layout_play_content_recycle_view, null);
        addView(view);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.play_recyleview);

    }
    public void initView(){

        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));//竖屏2列
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(2,12));
        mPlayContentAdapter = new PlayContentRecycleAdapter(mContext,null);
        mRecyclerView.setAdapter(mPlayContentAdapter);
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
