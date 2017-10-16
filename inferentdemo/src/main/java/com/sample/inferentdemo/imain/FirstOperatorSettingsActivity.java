package com.sample.inferentdemo.imain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.message.MessageConst;
import com.sample.inferentdemo.util.PreferenceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FirstOperatorSettingsActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.btn_go)
    ImageView mIcnGo;

    @InjectView(R.id.dongfangyouxian)
    ImageView mIcnDongfang;

    @InjectView(R.id.baishitong)
    ImageView mIcnBaushitong;

    private Context mContext;
    private int mOperatorType = 0;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_first_operator_settings);


        ButterKnife.inject(this);
        mContext = this.getBaseContext();

        mOperatorType = PreferenceUtil.getInt("OPERATOR_TYPE", 0);
        initView();
        InitHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go: {
                mHandler.sendEmptyMessage(MessageConst.CLENT_ACTION_ENTER_NEW_MAIN_PAGE);
                break;
            }
            case R.id.dongfangyouxian: {
                setOperatorType(0);
                break;
            }
            case R.id.baishitong: {
                setOperatorType(1);
                break;
            }
        }
    }

    private void InitHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case MessageConst.CLENT_ACTION_ENTER_NEW_MAIN_PAGE:{
                        Intent newActivity = new Intent(mContext,MainActivity.class);
                        newActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(newActivity);
                        mHandler.sendEmptyMessageDelayed(MessageConst.CLENT_ACTION_FINISH_WELCOME_VIEW,300);
                        break;
                    }
                    case MessageConst.CLENT_ACTION_FINISH_WELCOME_VIEW:{
                        finish();
                        break;
                    }

                }
            }

        };
    }

    private void initView() {
        mIcnGo.setOnClickListener(this);
        mIcnDongfang.setOnClickListener(this);
        mIcnBaushitong.setOnClickListener(this);

        if (mOperatorType == 0) {
            mIcnDongfang.setImageResource(R.drawable.icn_choose);
            mIcnBaushitong.setImageResource(R.drawable.icn_unchoose);
        } else {
            mIcnDongfang.setImageResource(R.drawable.icn_unchoose);
            mIcnBaushitong.setImageResource(R.drawable.icn_choose);
        }
    }

    private void setOperatorType(int type) {
        if (type == mOperatorType) return;

        mOperatorType = type;
        PreferenceUtil.commitInt("OPERATOR_TYPE", type);
        if (mOperatorType == 0) {
            mIcnDongfang.setImageResource(R.drawable.icn_choose);
            mIcnBaushitong.setImageResource(R.drawable.icn_unchoose);
        } else {
            mIcnDongfang.setImageResource(R.drawable.icn_unchoose);
            mIcnBaushitong.setImageResource(R.drawable.icn_choose);
        }
    }

}
