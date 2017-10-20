package com.sample.inferentdemo.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sample.inferentdemo.R;

public class PlayTimeTabAdapter extends BaseAdapter{
	private int[] mIconIDs;
	private String[] mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	private int selectIndex = 0;

	public PlayTimeTabAdapter(Context context, String[] titles){
		this.mContext = context;
		this.mTitles = titles;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return mTitles.length;
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.layout_adapter_play_time_tab, null);
			holder.mTitle=(TextView)convertView.findViewById(R.id.text_list_item);
			holder.mFrame = (RelativeLayout)convertView.findViewById(R.id.item_frame);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			holder.mTitle.setBackgroundColor(Color.rgb(0x75,0xc5,0xef));
			holder.mTitle.setTextColor(Color.rgb(0xff,0xff,0xff));
		}else{
			holder.mTitle.setTextColor(Color.rgb(0x99,0x99,0x99));
			holder.mTitle.setBackgroundColor(Color.rgb(0xff,0xff,0xff));
		}
		holder.mTitle.setText(mTitles[position]);

		int win_width = mContext.getResources().getDisplayMetrics().widthPixels;
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)holder.mFrame.getLayoutParams();
		lp.width = win_width/7;
		holder.mFrame.setLayoutParams(lp);

		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle ;
		private RelativeLayout mFrame;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
		notifyDataSetChanged();
	}
	public void resetData(String[] title){
		selectIndex = 0;
		this.mTitles = title;
		notifyDataSetChanged();
	}
}