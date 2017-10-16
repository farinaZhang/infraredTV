package com.sample.inferentdemo.imain;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.util.PreferenceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OperatorSettingsActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.icn_close)
    ImageView mIcnClose;

    @InjectView(R.id.dongfangyouxian)
    ImageView mIcnDongfang;

    @InjectView(R.id.baishitong)
    ImageView mIcnBaushitong;

    private Context mContext;
    private int mOperatorType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_operator_settings);


        ButterKnife.inject(this);
        mContext = this.getBaseContext();

        mOperatorType = PreferenceUtil.getInt("OPERATOR_TYPE", 0);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icn_close: {
                activityfinish();
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

    private void activityfinish() {
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.translate_center_to_right);
    }

    private void initView() {
        mIcnClose.setOnClickListener(this);
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
