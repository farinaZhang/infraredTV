package com.sample.inferentdemo.imain;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.inferentdemo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OtherSetingsActivity extends BaseActivity implements View.OnClickListener{
    @InjectView(R.id.btn_ok)
    TextView mBtnOk;

    @InjectView(R.id.btn_up)
    ImageView mBtnUp;

    @InjectView(R.id.btn_down)
    ImageView mBtnDown;

    @InjectView(R.id.btn_left)
    ImageView mBtnLeft;

    @InjectView(R.id.btn_right)
    ImageView mBtnRight;

    @InjectView(R.id.control_menu)
    TextView mControlMenu;

    @InjectView(R.id.control_huazhonghua)
    TextView mControlHuazhonghua;

    @InjectView(R.id.control_tvav)
    TextView mControlTvAv;

    @InjectView(R.id.control_pingxian)
    TextView mControlPingxian;

    @InjectView(R.id.column_add)
    ImageView mColumnAdd;

    @InjectView(R.id.column_reduce)
    ImageView mColumnReduce;

    @InjectView(R.id.img_close)
    ImageView mImgClose;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_setings);
        mContext = this.getBaseContext();
        ButterKnife.inject(this);

        InitView();

    }

    private void InitView(){
        mBtnOk.setOnClickListener(this);
        mBtnUp.setOnClickListener(this);
        mBtnDown.setOnClickListener(this);
        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);
        mControlMenu.setOnClickListener(this);
        mControlHuazhonghua.setOnClickListener(this);
        mControlTvAv.setOnClickListener(this);
        mControlPingxian.setOnClickListener(this);
        mColumnAdd.setOnClickListener(this);
        mColumnReduce.setOnClickListener(this);
        mImgClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_ok:{
                //确认按键
                break;
            }
            case R.id.btn_up:{
                //方向上
                break;
            }
            case R.id.btn_down:{
                //方向下
                break;
            }
            case R.id.btn_left:{
                //方向左
                break;
            }
            case R.id.btn_right:{
                //方向右
                break;
            }
            case R.id.control_menu:{
                //按键菜单
                break;
            }
            case R.id.control_huazhonghua:{
                //按键画中画
                break;
            }
            case R.id.control_tvav:{
                //按键 TV/AV
                break;
            }
            case R.id.control_pingxian:{
                //按键 屏显
                break;
            }
            case R.id.column_add:{
                //音量 加
                break;
            }
            case R.id.column_reduce:{
                //音量 减
                break;
            }
            case R.id.img_close:{
                finishActivity();
                break;
            }
        }
    }

    private void finishActivity(){
        this.finish();
        this.overridePendingTransition(R.anim.no_anim,R.anim.translate_up_to_down);
    }
}
