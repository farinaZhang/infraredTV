package com.sample.inferentdemo.widget.searchview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.SearchTryAdapter;
import com.sample.inferentdemo.adapter.SearchhistoryAdapter;
import com.sample.inferentdemo.message.MessageConst;
import com.sample.inferentdemo.util.MyGrideLayoutManager;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Farinazhang on 17/8/10.
 */

public class SearchView extends RelativeLayout implements View.OnClickListener{

    @InjectView(R.id.search_content_view)
    LinearLayout mSearchContentView;

    @InjectView(R.id.history_eara)
    LinearLayout mHistoryEara;

    @InjectView(R.id.clear_history)
    TextView clearHistory;  // 删除搜索记录按键

    @InjectView(R.id.history_item)
    RecyclerView SearchlistView;

    @InjectView(R.id.try_item)
    RecyclerView TryListView;

    /**
     * 初始化成员变量
     */

    private Context mContext;
    private Handler mHandler;
    // ListView列表 & 适配器

    private SearchhistoryAdapter mHistoryAdapter;
    private SearchTryAdapter mTryAdapter;
    private MyGrideLayoutManager mHistoryLayoutManager;

    /**
     * 构造函数
     * 作用：对搜索框进行初始化
     */
    public SearchView(Context context) {
        this(context,null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        View view =  View.inflate(mContext,R.layout.layout_search_view,null);
        this.addView(view);

        ButterKnife.inject(this);
    }

    /**
     * 关注b
     * 作用：初始化搜索框
     */
    public void InitView(){

        //搜索历史
        mHistoryLayoutManager = new MyGrideLayoutManager(mContext,2, 0,0);
        SearchlistView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter = new SearchhistoryAdapter(mContext,null);
        // 3. 设置适配器
        SearchlistView.setAdapter(mHistoryAdapter);

        //你可以试试
        String[] strTry= mContext.getResources().getStringArray(R.array.search_try_array);
        int LintCountTry = (strTry.length/3)+((strTry.length%3 >0)?1: 0);
        int spacesTry = mContext.getResources().getDimensionPixelSize(R.dimen.history_spacing);
        MyGrideLayoutManager mTryLayoutManager = new MyGrideLayoutManager(mContext,3,LintCountTry,spacesTry);
        TryListView.setLayoutManager(mTryLayoutManager);
        TryListView.addItemDecoration(new SpacesItemDecoration(spacesTry));
        mTryAdapter = new SearchTryAdapter(mContext,strTry);
        TryListView.setAdapter(mTryAdapter);

       /* *
         * "清空搜索历史"按钮*/
        clearHistory.setOnClickListener(this);

       /* *
         * 搜索记录列表（ListView）监听
         * 即当用户点击搜索历史里的字段后,会直接将结果当作搜索字段进行搜索*/

        mHistoryAdapter.setOnItemClickListener(new SearchhistoryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick( View view, int position) {
                // 获取用户点击列表里的文字,并自动填充到搜索框内
                String name =  mHistoryAdapter.getHistoryData().get(position);

                Message msg = new Message();
                msg.what = MessageConst.CLENT_ACTION_USER_DO_SEARCH;
                msg.obj = name;
                mHandler.sendMessage(msg);
            }
        });

        mTryAdapter.setOnItemClickListener(new SearchTryAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick( View view, int position) {
                // 获取用户点击列表里的文字,并自动填充到搜索框内
                String name =  mTryAdapter.getAdapterData()[position];
                Message msg = new Message();
                msg.what = MessageConst.CLENT_ACTION_USER_DO_SEARCH;
                msg.obj = name;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.clear_history:{
                // 清空数据库->>关注2
                SearchDataHelp.deleteData();
                // 模糊搜索空字符 = 显示所有的搜索历史（此时是没有搜索记录的）
                mHandler.sendEmptyMessage(MessageConst.CLENT_ACTION_LOAD_HISTORY);
                break;
            }

        }
    }

    public void setHandler(Handler handler){
        mHandler = handler;
    }


    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items

        }
    }

    public void reloadHistory(List<String> strHistory){

        int LintCountHistory = (strHistory.size()/2)+(((strHistory.size()%2) >0)?1: 0);

        mHistoryLayoutManager.updataLineCount(LintCountHistory,0);
        mHistoryAdapter.setHistoryData(strHistory);

        if(strHistory!= null && strHistory.size()>0) {
            mHistoryEara.setVisibility(View.VISIBLE);
        }else{
            mHistoryEara.setVisibility(View.GONE);
        }
    }

}
