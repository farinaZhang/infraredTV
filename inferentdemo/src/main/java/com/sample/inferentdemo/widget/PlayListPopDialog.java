package com.sample.inferentdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.data.PlayTimeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FarinaZhang on 2017/9/29.
 * 长按首页某项弹出节目列表
 */
public class PlayListPopDialog extends Dialog {
    public interface PlayListPopDialogListener {
        public void onItemClick(int positon);
    }

    private PlayListPopDialogListener listener = null;
    private static ListView mListView;
    private static TextView mTitle;

    private static PlayListPopDialog dialog;
    private static List<PlayTimeEntity> mlistData = new ArrayList<PlayTimeEntity>();
    private static boolean mHasTitle = false;
    private static int mListColor = Color.rgb(0x33, 0xad, 0xce);
    private static Context mContext;


    public PlayListPopDialog(Context context) {
        super(context);
    }

    public PlayListPopDialog(Context context, int theme) {
        super(context, theme);
    }

    public static PlayListPopDialog createDialog(Context context,
                                                 List<PlayTimeEntity> listData) {

        mContext = context;
        mlistData = listData;
        dialog = new PlayListPopDialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.play_list_pop_dialog);

        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_box_content));
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(20, 20, 20, 20);

        //系统屏幕的宽高
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        int win_width = dm.widthPixels;
        int win_height = dm.heightPixels;

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = win_width;
        lp.height = win_height / 2;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.style_dialog_anim);

        mTitle = (TextView) dialog.findViewById(R.id.title);
        mListView = (ListView) dialog.findViewById(R.id.view_list);

        myListAdapter adapter = dialog.new myListAdapter(dialog.getContext());
        mListView.setAdapter(adapter);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (dialog == null)
            return;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.onItemClick(position);
                }
                dialog.cancel();
            }

        });
    }

    public void setListener(final PlayListPopDialogListener listener) {
        this.listener = listener;
    }

    public void setTitle(String title){
        if(mTitle==null){
            mTitle = (TextView)dialog.findViewById(R.id.title);
        }
        mTitle.setText(title);
    }

    public void setSelectIndex(int index){
        mListView.setSelection(index);
    }

    private class myListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public myListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mlistData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mlistData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.layout_adapter_selected_dialog_item, null);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.state = (TextView) convertView.findViewById(R.id.state);
                holder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PlayTimeEntity entity = mlistData.get(position);

            holder.time.setText(entity.time);
            holder.name.setText(entity.name);

            //0:未播放，1：正在播放，2：已播放
            if (0 == entity.isplaying) {
                holder.state.setText("即将播出");

                holder.time.setTextColor(Color.rgb(0x33, 0x33, 0x33));
                holder.state.setTextColor(Color.rgb(0x33, 0x33, 0x33));
                holder.name.setTextColor(Color.rgb(0x33, 0x33, 0x33));
            } else if (1 == entity.isplaying) {
                holder.state.setText("正在播出");

                holder.time.setTextColor(Color.rgb(0x2a, 0xa1, 0xde));
                holder.state.setTextColor(Color.rgb(0x2a, 0xa1, 0xde));
                holder.name.setTextColor(Color.rgb(0x2a, 0xa1, 0xde));
            } else {
                holder.state.setText("已播出");

                holder.time.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                holder.state.setTextColor(Color.rgb(0x99, 0x99, 0x99));
                holder.name.setTextColor(Color.rgb(0x99, 0x99, 0x99));
            }

            return convertView;
        }

        public class ViewHolder {
            public TextView time;
            public TextView state;
            public TextView name;
        }
    }



}
