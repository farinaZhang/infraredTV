package com.sample.inferentdemo.widget;


import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class RecycleviewPagerAdapter extends PagerAdapter {
    private List<PlayDateRecycleView> pageViews;

    public RecycleviewPagerAdapter(List<PlayDateRecycleView> list)
    {
        pageViews = list;
    }
    @Override
    public void destroyItem(View v, int position, Object arg2) {
        ((ViewPager) v).removeView(pageViews.get(position));

    }

    @Override
    public void finishUpdate(View arg0) {

    }

    @Override
    public int getCount() {
        return pageViews.size();
    }

    @Override
    public Object instantiateItem(View v, int position) {
        ((ViewPager) v).addView(pageViews.get(position));
        return pageViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View v, Object arg1) {
        return v == arg1;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
