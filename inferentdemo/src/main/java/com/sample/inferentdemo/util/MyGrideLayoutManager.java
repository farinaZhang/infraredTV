package com.sample.inferentdemo.util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by FarinaZhang on 2017/8/31.
 */
public class MyGrideLayoutManager extends GridLayoutManager{
    private int mLineCount ;
    private int mLineSpace;

    public MyGrideLayoutManager(Context context, int spanCount, int lineCount, int LineSpace) {
        super(context, spanCount);
        mLineCount = lineCount;
        mLineSpace = LineSpace;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        try {
            View view = recycler.getViewForPosition(0);
            if (view != null) {
                measureChild(view, widthSpec, heightSpec);
                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                int measuredHeight;
                if(mLineSpace>0) {
                     measuredHeight = view.getMeasuredHeight() * mLineCount + ((mLineCount > 0) ? (mLineSpace * mLineCount) : 0);
                }else{
                     measuredHeight = view.getMeasuredHeight() * mLineCount;
                }

                setMeasuredDimension(measuredWidth, measuredHeight);
            }
        }catch (Exception e){
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }

    public void updataLineCount(int lineCount, int LineSpace){
        mLineCount = lineCount;
        mLineSpace = LineSpace;
    }
}
