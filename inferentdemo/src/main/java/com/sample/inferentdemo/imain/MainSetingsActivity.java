package com.sample.inferentdemo.imain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sample.inferentdemo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainSetingsActivity extends BaseActivity implements View.OnClickListener{
    @InjectView(R.id.icn_close)
    ImageView mIcnClose;

    @InjectView(R.id.shake_settings)
    ImageView mShakeSettings;

    @InjectView(R.id.volumn_settings)
    ImageView mVolumnSettings;

    @InjectView(R.id.operator_settings)
    ImageView mOperatorSettngs;

    @InjectView(R.id.about_settings)
    ImageView mAboatSettings;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_setings);
        ButterKnife.inject(this);
        mContext = this.getBaseContext();

        initView();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.icn_close:{
                activityfinish();
                break;
            }
            case R.id.shake_settings:{
                break;
            }
            case R.id.volumn_settings:{
                break;
            }
            case R.id.operator_settings:{
                startActivity(new Intent(MainSetingsActivity.this, OperatorSettingsActivity.class));
                overridePendingTransition(R.anim.translate_right_to_center,R.anim.no_anim);
                break;
            }
            case R.id.about_settings:{
                break;
            }
        }
    }

    private void activityfinish(){
        finish();
        overridePendingTransition(R.anim.no_anim,R.anim.translate_center_to_left);
    }

    private void initView(){
        mIcnClose.setOnClickListener(this);
        mShakeSettings.setOnClickListener(this);
        mVolumnSettings.setOnClickListener(this);
        mOperatorSettngs.setOnClickListener(this);
        mAboatSettings.setOnClickListener(this);

    }
}
