package com.sample.inferentdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.inferentdemo.R;

import java.util.List;

/**
 * Created by FarinaZhang on 2017/8/23.
 */
public class SearchhistoryAdapter extends RecyclerView.Adapter<SearchhistoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> values;
    private OnRecyclerViewItemClickListener mItemClickListener;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public SearchhistoryAdapter(Context context, List<String> values){
        mContext = context;
        this.values = values;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_search_history,viewGroup,false);
        MyViewHolder  vh= new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        viewHolder.txt_name.setText(values.get(i));

        final int position = i;
        viewHolder.txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    mItemClickListener.onItemClick(v,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setHistoryData(List<String> data){
        values = data;
        notifyDataSetChanged();

    }

    public List<String> getHistoryData(){
        return values;
    }

    public class MyViewHolder extends ViewHolder {
        public TextView txt_name;
        public MyViewHolder(View itemView) {

            super(itemView);
            txt_name = (TextView)itemView.findViewById(R.id.str_text);
        }
    }
}
