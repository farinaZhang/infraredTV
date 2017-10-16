package com.sample.inferentdemo.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.data.PlayContentEntity;
import com.sample.inferentdemo.widget.TvProgramReview;

import java.util.List;

/**
 * Created by FarinaZhang on 2017/8/21.
 */
public class PlayContentRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PlayContentEntity> listdata;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private  View mHeaderView;

    interface OnItemClickListener {
        void onItemClick(int position, String data);
    }
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public PlayContentRecycleAdapter(Context cnt , List<PlayContentEntity> data) {
        mContext = cnt;
        listdata = data;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return TYPE_HEADER;

        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if( viewType == TYPE_HEADER){

            mHeaderView = LayoutInflater.from(
                    mContext).inflate(R.layout.layout_head_play_content, parent,
                    false);
            return new HeadViewHolder(mHeaderView);
        }

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.layout_adapter_play_content, parent,
                false));
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) {
            HeadViewHolder holder = (HeadViewHolder)viewHolder;

            PlayContentEntity entity = listdata.get(position);
            holder.item.setProgramName(entity.name);
            holder.item.setImageReviewPictrue(entity.str_ImageId, R.drawable.review_unkown_large);
            holder.item.setPlayName(entity.subName);
            holder.item.setPlayProgress(entity.time);

            return;
        }
        MyViewHolder holder = (MyViewHolder)viewHolder;
        PlayContentEntity entity = listdata.get(position);
        holder.item.setProgramName(entity.name);
        holder.item.setImageReviewPictrue(entity.str_ImageId, R.drawable.review_unkown_large);
        holder.item.setPlayName(entity.subName);
        holder.item.setPlayProgress(entity.time);
    }

    @Override
    public int getItemCount() {
        if(listdata == null)return 0;

        int count = listdata.size();
        return  count;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    public void setData(List<PlayContentEntity> data){
        listdata = data;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TvProgramReview item;
        public MyViewHolder(View view) {
            super(view);
            item = (TvProgramReview) view.findViewById(R.id.item);
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {

        TvProgramReview item;
        public HeadViewHolder(View view) {
            super(view);
            item = (TvProgramReview) view.findViewById(R.id.item);
        }
    }

}
