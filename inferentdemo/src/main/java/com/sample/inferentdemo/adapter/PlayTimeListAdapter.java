package com.sample.inferentdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.data.PlayTimeEntity;

import java.util.List;

/**
 * Created by FarinaZhang on 2017/9/4.
 */
public class PlayTimeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<PlayTimeEntity> mListData;

    public interface OnItemClickListener{
        public void OnItemClick(View view, int pos);
        public void OnItemLongClick(View view, int pos);
    }

    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public PlayTimeListAdapter(Context context, List<PlayTimeEntity> data){
        mContext = context;
        mListData = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.layout_adapter_play_time_list, viewGroup,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PlayTimeEntity entity = mListData.get(i);
        final MyViewHolder holder = (MyViewHolder)viewHolder;
        holder.time.setText(entity.time);
        holder.channel.setText(entity.channel);
        holder.name.setText(entity.name);

        //isplaying =0; //0:未播放，1：正在播放，2：已播放
        if(1 == entity.isplaying ){
            holder.time.setTextColor(Color.rgb(0x2a,0xa1,0xde));
            holder.channel.setTextColor(Color.rgb(0x2a,0xa1,0xde));
            holder.name.setTextColor(Color.rgb(0x2a,0xa1,0xde));
        }else if(2 == entity.isplaying ){
            holder.time.setTextColor(Color.rgb(0x99,0x99,0x99));
            holder.channel.setTextColor(Color.rgb(0x99,0x99,0x99));
            holder.name.setTextColor(Color.rgb(0x99,0x99,0x99));
        }else{
            holder.time.setTextColor(Color.rgb(0x33,0x33,0x33));
            holder.channel.setTextColor(Color.rgb(0x33,0x33,0x33));
            holder.name.setTextColor(Color.rgb(0x33,0x33,0x33));
        }

        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.OnItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.OnItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(mListData == null)return 0;

        return mListData.size();
    }

    public void setData(List<PlayTimeEntity> data){
        mListData = data;
        notifyDataSetChanged();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView time;
        public TextView channel;
        public TextView name;
        public View item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            time = (TextView)itemView.findViewById(R.id.time);
            channel = (TextView)itemView.findViewById(R.id.channel);
            name = (TextView)itemView.findViewById(R.id.name);
        }
    }
}
