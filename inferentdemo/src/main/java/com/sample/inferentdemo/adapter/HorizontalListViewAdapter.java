package com.sample.inferentdemo.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sample.inferentdemo.R;

public class HorizontalListViewAdapter extends BaseAdapter{
	private int[] mIconIDs;
	private String[] mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	private int selectIndex = 0;
	private int type =0; //0:首页 1：搜索

	public HorizontalListViewAdapter(Context context, String[] titles){
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
			convertView = mInflater.inflate(R.layout.layout_title_horizontal_list_item, null);
			holder.mTitle=(TextView)convertView.findViewById(R.id.text_list_item);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			holder.mTitle.setTextColor(Color.rgb(0x2a,0xa1,0xde));
		}else{
			holder.mTitle.setTextColor(Color.rgb(0x33,0x33,0x33));
		}
		
		holder.mTitle.setText(mTitles[position]);
		/*RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)holder.mTitle.getLayoutParams();
		if(type == 0){
			int paddingR = (int)DensityUtil.dip2px(mContext, 29.4f);
			holder.mTitle.setPadding(0,0,paddingR,0);
		}else if(type == 1){
			int paddingR = (int)DensityUtil.dip2px(mContext, 33.4f);
			holder.mTitle.setPadding(0,0,paddingR,0);
		}*/

		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle ;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
		notifyDataSetChanged();
	}
	public void setTitle(int type, String[] title){
		this.type = type;
		this.mTitles = title;
		notifyDataSetChanged();
	}
}