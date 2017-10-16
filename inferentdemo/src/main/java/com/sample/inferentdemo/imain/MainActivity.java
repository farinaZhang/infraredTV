package com.sample.inferentdemo.imain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.sample.inferentdemo.MyApplication;
import com.sample.inferentdemo.R;
import com.sample.inferentdemo.data.MainContentData;
import com.sample.inferentdemo.data.MainViewType;
import com.sample.inferentdemo.data.PlayContentEntity;
import com.sample.inferentdemo.data.PlaySearchData;
import com.sample.inferentdemo.data.PlayTimeDataAll;
import com.sample.inferentdemo.data.PlayTimeEntity;
import com.sample.inferentdemo.message.MessageConst;
import com.sample.inferentdemo.service.VoiceSdkService;
import com.sample.inferentdemo.util.HttpUtil;
import com.sample.inferentdemo.util.PreferenceUtil;
import com.sample.inferentdemo.widget.CustomOlaTipDialog;
import com.sample.inferentdemo.widget.MainSearchResultView;
import com.sample.inferentdemo.widget.MainShowView;
import com.sample.inferentdemo.widget.RotateView;
import com.sample.inferentdemo.widget.searchview.SearchDataHelp;
import com.sample.inferentdemo.widget.searchview.SearchView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by FarinaZhang on 2017/4/21.
 * MainActivity  主画面
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    @InjectView(R.id.menu_icon)
    ImageView mMenuIcon;

    @InjectView(R.id.txt_select_devie)
    TextView mSelectDevice;

    //search lan
    @InjectView(R.id.icn_keyboard)
    ImageView mIconKeyBoard;  //打开数字键盘

    @InjectView(R.id.txt_search_cancel)
    TextView cancelSearch;     //搜索取消

    @InjectView(R.id.search_input_box)
    EditText et_search;// 搜索输入框

    //主内容区
    @InjectView(R.id.submainview)
    MainShowView mSubMainView;   //首页主内容区

    @InjectView(R.id.subSearchview)
    SearchView mSubSearchView;  //搜索页面list

    @InjectView(R.id.subSearchMainview)
    MainSearchResultView mSubSearchResult; //搜索结果主内容区

    //底栏按钮
    @InjectView(R.id.icon_power)
    ImageView mIcnPower;  //开关

    @InjectView(R.id.icon_volumn)
    ImageView mIcnVlumn;  //音量

    @InjectView(R.id.icon_speak)
    ImageView mIcnSpeak;    //话筒

    @InjectView(R.id.voiceLine)
    VoiceLineView mVoiceLineView;    //说话中波形动画

    @InjectView(R.id.icon_change)
    ImageView mIcnSpeedChange;  //快速换台

    @InjectView(R.id.icon_other)
    ImageView mIcnOtherSettings;    //更多设置

    @InjectView(R.id.speak_recording_view)
    RelativeLayout mSpeakRecordingView; //底部录音中view

    @InjectView(R.id.speak_rotate)
    RotateView mRotateview;

    /*@InjectView(R.id.image_recording)
    ImageView mImageRecording; //录音中声波图片*/

    @InjectView(R.id.icon_speak_recording)
    ImageView mIcnSpeakReording;    //录音中话筒图标


    private final String TAG_SPEAK = "TAG_SPEAK";
    private final String TAG_SEARCH = "TAG_SEARCH";
    private Handler mHandler;
    private Context mContext;
    private Handler mInComingHandler;
    private ActivityComAssist mActivityComAssist;
    private ConsumerIrManager mCIR;
    private ArrayList<int[]> pattern;
    private JSONObject dataobj;

    private final int START_KEYBOARD = 1; //require code keyboard


    private int oprateType; //0:看节目，1：换台
    private JSONArray json_SearchResult;

    private int mCurViewType = -1;
    private static String mAppType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.assitActivity = MainActivity.this;
        if (PreferenceUtil.getBoolean("beFirstStart", true)) {
            PreferenceUtil.commitBoolean("beFirstStart", false);
            this.startActivity(new Intent(this, FirstOperatorSettingsActivity.class));
        }

        mContext = this.getApplicationContext();
        setContentView(R.layout.layout_main_activity);
        ButterKnife.inject(this);

        initHandler();
        initInComingHandler();
        initCommunicationAssist();
        mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);

        SearchDataHelp.setContext(mContext);

        InitView();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, VoiceSdkService.class);
        startService(intent);

        readStingFromFile();
        getDeviceInfo();
        StartMainTabSearchFunction(0); //获取首页信息（本地）
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_search_cancel: {
                //取消搜索，返回主页
                changeViewType(MainViewType.TYPE_MAIN);
                break;
            }
            case R.id.icn_keyboard: {
                //打开按键输入
                mHandler.sendEmptyMessage(MessageConst.CLENT_ACTION_START_ACTIVITY_KEY_BOARD);
                break;
            }
            case R.id.search_input_box: {
                mHandler.sendEmptyMessage(MessageConst.CLENT_ACTION_LOAD_HISTORY);
                changeViewType(MainViewType.TYPE_SEARCH);

                //显示软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    et_search.requestFocus();
                    imm.showSoftInput(et_search, 0);
                }
                //进入搜索界面
                break;
            }

            case R.id.icon_power: {
                //开关电源

                ShowToast("为您打开或关闭电视");
                /*if (!mCIR.hasIrEmitter()) {
                    Toast.makeText(MainActivity.this,"未找到红外发生器",Toast.LENGTH_SHORT).show();
                    return;
                }

                mCIR.transmit(37950, pattern.get(0));*/
                break;
            }
            case R.id.icon_volumn: {
                //音量
                /*if (!mCIR.hasIrEmitter()) {
                    Toast.makeText(MainActivity.this,"未找到红外发生器",Toast.LENGTH_SHORT).show();
                    return;
                }
                mCIR.transmit(37950, pattern.get(1));*/
                ShowToast("为您调节音量");
                break;
            }
            case R.id.icon_speak: {
                //mIcnSpeak.setVisibility(View.GONE);
                //mVoiceLineView.setVisibility(View.VISIBLE);
                sendMessageToService(MessageConst.CLIENT_ACTION_START_RECORED, 0, 0, null, null);
                //话筒
                /*if (!mCIR.hasIrEmitter()) {
                    Toast.makeText(MainActivity.this,"未找到红外发生器",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                break;
            }
            case R.id.icon_speak_recording: {
                sendMessageToService(MessageConst.CLIENT_ACTION_STOP_RECORED, 0, 0, null, null);
                break;
            }
            /*case R.id.voiceLine:
                sendMessageToService(MessageConst.CLIENT_ACTION_STOP_RECORED, 0, 0, null, null);
                mIcnSpeak.setVisibility(View.VISIBLE);
                mVoiceLineView.setVisibility(View.GONE);
                break;*/
            case R.id.icon_change: {
                //快速切台

                ShowToast("为您快速切台");
                /*if (!mCIR.hasIrEmitter()) {
                    Toast.makeText(MainActivity.this,"未找到红外发生器",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                break;
            }
            case R.id.icon_other: {
                //更多设置
                /*if (!mCIR.hasIrEmitter()) {
                    Toast.makeText(MainActivity.this,"未找到红外发生器",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                startActivityForResult(new Intent(MainActivity.this, OtherSetingsActivity.class), START_KEYBOARD);
                this.overridePendingTransition(R.anim.translate_down_to_up, R.anim.no_anim);

                break;
            }
            case R.id.menu_icon: {
                //打开设置画面，从左侧滑出
                startActivityForResult(new Intent(MainActivity.this, MainSetingsActivity.class), START_KEYBOARD);
                this.overridePendingTransition(R.anim.translate_left_to_center, R.anim.no_anim);
                break;
            }
            case R.id.txt_select_devie: {
                //切换设备
                /*if (!mCIR.hasIrEmitter()) {
                    Toast.makeText(MainActivity.this,"未找到红外发生器",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                break;
            }

        }
        ;
    }

    private int touchYDownInRecording;
    private int touchYUpInRecording;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.speak_recording_view: {
                //在录音中画面中上滑取消录音
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        touchYDownInRecording = (int) event.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        touchYUpInRecording = (int) event.getY();
                        if (touchYDownInRecording - touchYUpInRecording > 10) {
                            //上滑动作，取消录音
                            sendMessageToService(MessageConst.CLIENT_ACTION_CANCEL_RECORED, 0, 0, null, null);

                            mIcnSpeak.setVisibility(View.VISIBLE);
                            mSpeakRecordingView.setVisibility(View.GONE);
                        }
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    private void InitView() {

        mIconKeyBoard.setOnClickListener(this);

        et_search.setOnClickListener(this); //点击search框
        cancelSearch.setOnClickListener(this);//取消搜索
    /*    *
         * 监听输入键盘更换后的搜索按键
         * 调用时刻：点击键盘上的搜索键时*/

        et_search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    actionSearch(et_search.getText().toString());
                    return true;
                }
                return false;
            }
        });

        mMenuIcon.setOnClickListener(this);
        mSelectDevice.setOnClickListener(this);

        mIcnPower.setOnClickListener(this);
        mIcnVlumn.setOnClickListener(this);
        mIcnSpeak.setOnClickListener(this);
        mIcnSpeakReording.setOnClickListener(this);
        mIcnSpeedChange.setOnClickListener(this);
        mIcnOtherSettings.setOnClickListener(this);

        mIcnSpeak.setVisibility(View.VISIBLE);
        mSpeakRecordingView.setVisibility(View.GONE);
        mSpeakRecordingView.setOnTouchListener(this);
        mRotateview.setVisibility(View.GONE);
        mRotateview.setImageDrawable(R.drawable.voice_loading_circle);

        changeViewType(MainViewType.TYPE_MAIN);

        mSubMainView.setHandler(mHandler);
        mSubMainView.InitView(null);
        mSubMainView.setListener(new MainShowView.OnMyHListClickLister() {
            @Override
            public void onMyHListClick(int position) {
                StartMainTabSearchFunction(position);
            }
        });


        mSubSearchView.setHandler(mHandler);
        mSubSearchView.InitView();
        mHandler.sendEmptyMessage(MessageConst.CLENT_ACTION_LOAD_HISTORY);


        String[] search_result_title = mContext.getResources().getStringArray(R.array.search_type_array);
        mSubSearchResult.setHTitleString(search_result_title);
        mSubSearchResult.setHandler(mHandler);
        mSubSearchResult.InitView(null);
    }

    private void readStingFromFile() {
        InputStream is = null;
        String result = "";
        try {
            is = getAssets().open("test.json");
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            result = new String(buffer, "utf8");
            dataobj = new JSONObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pattern = new ArrayList<int[]>();

        try {
            for (int j = 0; j < dataobj.getJSONArray("datacode").length(); j++) {
                JSONArray DATA = null;
                DATA = dataobj.getJSONArray("datacode").getJSONObject(j).getJSONArray("pulsedata");

                int[] pattern0 = new int[DATA.length()];

                for (int i = 0; i < DATA.length(); i++) {
                    pattern0[i] = DATA.optInt(i);
                }

                pattern.add(pattern0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //activity 的普通handler
    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageConst.CLENT_SHOW_ERROR: {
                        break;
                    }
                    case MessageConst.CLENT_ACTION_START_ACTIVITY_KEY_BOARD: {
                        /*if (!mCIR.hasIrEmitter()) {
                            Toast.makeText(MainActivity.this,"未找到红外发生器",Toast.LENGTH_SHORT).show();
                            return;
                        }*/
                        startActivityForResult(new Intent(MainActivity.this, KeyBoardActivity.class), START_KEYBOARD);
                        MainActivity.this.overridePendingTransition(R.anim.translate_down_to_up, R.anim.no_anim);
                        break;
                    }
                    case MessageConst.CUSTOM_GET_SEARCH_DATA_OK: {
                        String str_obj = (String) msg.obj;
                        JSONObject temp = null;

                        try {
                            temp = new JSONObject(str_obj);

                            if (temp.has("status") && temp.get("status").equals("ok")) {
                                JSONObject json_data = temp.getJSONObject("data").getJSONObject("epg");
                                if (json_data.has("desc_obj")) {
                                    if (json_data.getString("desc_obj").equals("search_program")) {
                                        //看节目
                                        oprateType = 0;
                                    } else {
                                        oprateType = 1;
                                    }
                                }

                                if (json_data.has("data_obj")) {
                                    if (PlaySearchData.playData == null) {
                                        PlaySearchData.playData = new ArrayList<PlayContentEntity>();
                                    } else {
                                        PlaySearchData.playData.clear();
                                    }

                                    if (PlayTimeDataAll.mPlayTimeDataAll == null) {
                                        PlayTimeDataAll.mPlayTimeDataAll = new ArrayList<List<PlayTimeEntity>>();
                                    } else {
                                        PlayTimeDataAll.mPlayTimeDataAll.clear();
                                    }

                                    JSONArray data_data = json_data.getJSONArray("data_obj");

                                    if (data_data.length() < 1) {
                                        ShowToast("没有找到您要搜索的内容");

                                    } else {
                                        for (int k = 0; k < data_data.length(); k++) {
                                            JSONArray data = data_data.getJSONObject(k).getJSONArray("result");


                                            List<PlayTimeEntity> date0 = new ArrayList<PlayTimeEntity>();
                                            for (int i = 0; i < data.length(); i++) {

                                                int isplaying =0; //0:未播放，1：正在播放，2：已播放
                                                JSONObject tmp = data.getJSONObject(i);

                                                String s_time = tmp.getString("start_time");
                                                String e_time = tmp.getString("end_time");
                                                SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //"2017-09-26 17:00:00"
                                                try {
                                                    long ds = dFormat.parse(s_time).getTime();
                                                    long de = dFormat.parse(e_time).getTime();
                                                    long curDate = new Date(System.currentTimeMillis()).getTime();

                                                    if (curDate >= ds && curDate < de) {
                                                        PlayContentEntity entity = new PlayContentEntity();

                                                        entity.name = tmp.getString("channel_title");
                                                        entity.subName = tmp.getString("program_title");
                                                        if (tmp.has("image_id")) {
                                                            entity.str_ImageId = tmp.getString("image_id");
                                                        } else {
                                                            entity.str_ImageId = "0";
                                                        }
                                                        entity.time = (int) (((curDate - ds) * 100) / (de - ds));
                                                        PlaySearchData.playData.add(entity);

                                                        Log.d(TAG_SEARCH, "正在播放 starttime :" + s_time);
                                                        Log.d(TAG_SEARCH, "正在播放 end_time :" + e_time);
                                                        Log.d(TAG_SEARCH, "正在播放 entity.name :" + entity.name);
                                                        Log.d(TAG_SEARCH, "正在播放 entity.subName :" + entity.subName);
                                                        Log.d(TAG_SEARCH, "正在播放 entity.time :" + entity.time);

                                                        isplaying = 1;
                                                    }else if(curDate<ds){
                                                       //0:未播放，1：正在播放，2：已播放
                                                        isplaying = 0;
                                                    }else{
                                                        isplaying = 2;
                                                    }

                                                } catch (Exception e) {
                                                    ShowToast("请输入你要查找的节目或频道");
                                                }


                                            /*
                                            *  "channel_id": "CCTV1",
                                                "channel_title": "CCTV-1综合",
                                                "program_id": "QmRkaClbYQ==",
                                                "program_title": "生活提示2017-162",
                                                "start_time": "2017/07/25 00:14:00"，
                                                program_type："",
                                                program_subtype:["",""],
                                            * */
                                                PlayTimeEntity entityTime = new PlayTimeEntity();
                                                entityTime.channel = tmp.getString("channel_title");
                                                entityTime.name = tmp.getString("program_title");
                                                String str_time = tmp.getString("start_time").substring(11, 16);
                                                entityTime.time = str_time;
                                                entityTime.isplaying = isplaying;

                                                date0.add(entityTime);
                                            }

                                            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            Date time_date = new Date(Long.parseLong(data_data.getJSONObject(k).getString("date")));

                                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间


                                            String date_get = dFormat.format(time_date).toString().substring(0, 10);//获取的数据的日期

                                            String date_cur = dFormat.format(curDate).toString().substring(0, 10);  //获取当前日期

                                            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date_get);
                                            long unixTimestamp1 = date1.getTime();

                                            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date_cur);
                                            long unixTimestamp2 = date2.getTime();


                                            Log.d(TAG_SEARCH, "获取的数据的当前日期时间 :" + date_get);
                                            Log.d(TAG_SEARCH, "系统的日期时间 :" + date_cur);
                                            long m = (unixTimestamp1 - unixTimestamp2) / (24 * 3600 * 1000);
                                            Log.d(TAG_SEARCH, " 获取的数据和今天的日期的差距为" + m);

                                            if (0 == m) {
                                                PlayTimeDataAll.mPlayTimeDataAll.add(0, date0);
                                            } else if (1 == m) {
                                                PlayTimeDataAll.mPlayTimeDataAll.add(1, date0);
                                            } else if (2 == m) {
                                                PlayTimeDataAll.mPlayTimeDataAll.add(2, date0);
                                            } else if (3 == m) {
                                                PlayTimeDataAll.mPlayTimeDataAll.add(3, date0);
                                            } else if (4 == m) {
                                                PlayTimeDataAll.mPlayTimeDataAll.add(4, date0);
                                            } else if (5 == m) {
                                                PlayTimeDataAll.mPlayTimeDataAll.add(5, date0);
                                            } else if (6 == m) {
                                                PlayTimeDataAll.mPlayTimeDataAll.add(6, date0);
                                            }


                                            //PlayTimeDataAll.mPlayTimeDataAll.add(date0);
                                        }

                                    }
                                    mSubSearchResult.setData(PlaySearchData.playData);
                                    mSubSearchResult.updataPlayDate();
                                    changeViewType(MainViewType.TYPE_RESULT_MAIN);
                                    if (PlaySearchData.playData.size() < 1 && PlayTimeDataAll.mPlayTimeDataAll.size() > 0) {
                                        //没有正在播放的
                                        mSubSearchResult.setSelectedTab(1);
                                    } else {
                                        mSubSearchResult.setSelectedTab(0);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowToast("请输入你要查找的节目或频道");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            ShowToast("请输入你要查找的节目或频道");
                        }catch(IndexOutOfBoundsException e){
                            e.printStackTrace();
                            ShowToast("请输入你要查找的节目或频道");
                        }
                        break;
                    }
                    case MessageConst.CLENT_ACTION_USER_DO_SEARCH: {
                        actionSearch((String) msg.obj);
                        break;
                    }
                    case MessageConst.CUSTOM_GET_MAIN_DATA_OK: {
                        updataMainViewData(msg.obj);
                        break;
                    }
                    case MessageConst.CLENT_ACTION_LOAD_HISTORY: {
                        List<String> strHistory = new ArrayList<String>();
                        Cursor cursor = SearchDataHelp.getAllData();

                        int index = cursor.getColumnIndex("name");
                        cursor.moveToFirst();
                        for (int i = 0; i < cursor.getCount(); i++) {
                            String temp = cursor.getString(index);
                            strHistory.add(temp);
                            cursor.moveToNext();
                        }

                        Message msgsend = new Message();
                        msgsend.what = MessageConst.CLENT_ACTION_UPDATE_HISTORY_VIEW;
                        msgsend.obj = strHistory;
                        mHandler.sendMessage(msgsend);
                        break;
                    }
                    case MessageConst.CLENT_ACTION_UPDATE_HISTORY_VIEW: {
                        List<String> strHistory = (List<String>) msg.obj;
                        mSubSearchView.reloadHistory(strHistory);
                        break;
                    }
                }
            }
        };
    }

    //service  语音识别的handler
    private void initInComingHandler() {
        mInComingHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageConst.CLENT_START_SPEECH_RECORDING:
                        mSpeakRecordingView.setVisibility(View.VISIBLE);
                        mIcnSpeak.setVisibility(View.GONE);

//                        mImageRecording.clearAnimation();
//                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.cycle_translate_anim);
//                        mImageRecording.startAnimation(anim);

                        //录音中
                        break;
                    case MessageConst.CLENT_END_SPEECH_RECORDING:
                        mIcnSpeak.setVisibility(View.VISIBLE);
                        //mVoiceLineView.setVisibility(View.GONE);
                        mSpeakRecordingView.setVisibility(View.GONE);
                        //mImageRecording.clearAnimation();
                        //录音结束

                        //启动选转动画
                        mRotateview.setVisibility(View.VISIBLE);
                        mRotateview.startRotate();

                        break;
                    case MessageConst.CLENT_END_RECOGNIZER: {
                        //识别结束，停止旋转动画
                        mRotateview.stopRotate();
                        mRotateview.setVisibility(View.GONE);
                        break;
                    }
                    case MessageConst.CLENT_ACTION_CANCEL_TO_ACTIVITY: {
                        mInComingHandler.sendEmptyMessageDelayed(MessageConst.CLENT_END_RECOGNIZER, 800);
                        break;
                    }
                    case MessageConst.CLENT_SHOW_INPUT: {
                        //显示录音结果
                        String temp = (String) msg.obj;
                        et_search.setText(temp);

                        if (temp == null || (temp.length()) < 1 || (temp.equals(""))) return;

                        boolean hasData = SearchDataHelp.hasData(temp.trim());
                        // 3. 若存在，则不保存；若不存在，则将该搜索字段保存（插入）到数据库，并作为历史搜索记录
                        if (!hasData) {
                            SearchDataHelp.insertData(temp.trim());
                        }
                        Log.d(TAG_SPEAK, "INPUT string :" + msg.obj);
                        break;
                    }
                    case MessageConst.SERVER_ACTION_RETURN_RESULT: {
                        //显示语义识别结果
                        JSONObject obj_result = (JSONObject) msg.obj;
                        Log.d(TAG_SPEAK, "INPUT json result:" + obj_result.toString());
                        //{"desc_obj":{"status":0},"semantic":[{"app":"tvprogram","input":"中央一套","slots":[{"name":"name","value":"CCTV1"}],"modifier":["watch_channel"],"customer":"58df54a484ae11f0bb7b488b"}],"type":"tvprogram"}
                        try {
                            JSONObject json_semantic = obj_result.getJSONArray("semantic").getJSONObject(0);
                            JSONArray slots, modifier;
                            String str_search = "", str_type = "";
                            mAppType = obj_result.getString("type");
                            slots = json_semantic.getJSONArray("slots");
                            modifier = json_semantic.getJSONArray("modifier");
                            if (slots != null && slots.length() > 0)
                                str_search = slots.getJSONObject(0).getString("value");
                            if (modifier != null && modifier.length() > 0)
                                str_type = modifier.getString(0);
                            int type = getSearchType(str_type, slots);
                            if (type == 10) {
                                controlOrder(str_search, str_type);
                                return;
                            }

                            if ("tvplay".equals(mAppType))
                                mAppType = "电视剧";
                            else if ("movie".equals(mAppType))
                                mAppType = "电影";

                            StartSearchFunction(mAppType, str_search, type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case MessageConst.CLIENT_ACTION_RETURN_OTHER_RESULT: {
                        String answer = (String) msg.obj;
                        ShowToast(answer);

                        break;
                    }
                    case MessageConst.CLIENT_ACTION_SPEAK_ERROR: {
                        ShowToast("请换个说法，输入您想查询的内容");
                        break;
                    }
                    case MessageConst.CLENT_ACTION_ON_VOLUME_UPDATE:
                        double db = msg.arg1 * 50 / 12;
                        mVoiceLineView.setVolume((int) (db));
                        break;
                }
            }
        };
    }

    private void initCommunicationAssist() {
        mActivityComAssist = new ActivityComAssist();
        MyApplication.getInstance().setServiceToActivityListener(mActivityComAssist);
    }

    private void sendMessageToService(int what, int arg1, int arg2, Bundle data, Object obj) {
        if (MyApplication.getInstance().getActivityToServiceListener() != null)
            MyApplication.getInstance().getActivityToServiceListener().callBack(what, arg1, arg2, data, obj);
    }

    private class ActivityComAssist implements CommunicationAssist {

        @Override
        public void callBack(int what, int arg1, int arg2, Bundle data, Object obj) {
            Message msg = Message.obtain(null, what);
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            if (data != null)
                msg.setData(data);
            if (obj != null)
                msg.obj = obj;
            mInComingHandler.sendMessage(msg);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, VoiceSdkService.class);
        stopService(intent);

        SearchDataHelp.closeDB();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == START_KEYBOARD) {
                Bundle bundle = data.getExtras();
                String num = bundle.getString("NUMBER");

                //mSearchInput.setText(num);
            }
        }
    }

    //type:0:频道，1：具体节目，2：节目大分类：例如【电视剧】【电视节目】【电影】【体育】 ，3：节目小分类：例如【动作】【战争】【谍战】【革命】【喜剧等】
    private void StartSearchFunction(final String programType, final String str, final int type) {
        //开始搜索节目或频道，进入搜索页面更新tab栏，更新节目列表显示
        Log.d(TAG_SEARCH, " search str :" + str + "   --- search type:" + type);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String appSecret = "a72966d006c04442a52409019ffd769a";
                String appKey = "879f09a3c19142458703467db7f0449a";
                String api = "epg";

                String base_url = "https://cn.olami.ai/cloudservice/api/epg";
                //具体的接口访问url
                String url = base_url + "/program";
                long time = System.currentTimeMillis();
                String sign = sign(appKey, api, time, appSecret);

                List<NameValuePair> formData = new ArrayList<NameValuePair>();

                //鉴权所需参数
                formData.add(new BasicNameValuePair("appkey", appKey));
                formData.add(new BasicNameValuePair("api", api));
                formData.add(new BasicNameValuePair("sign", sign));
                formData.add(new BasicNameValuePair("timestamp", String.valueOf(time)));
                //发送参数
                if (type == 0) {
                    formData.add(new BasicNameValuePair("channel", str));
                } else if (type == 1) {
                    formData.add(new BasicNameValuePair("program_title", str));
                } else if (type == 2) {
                    formData.add(new BasicNameValuePair("program_type", programType));
                    if (str != null && !"".equals(str))
                        formData.add(new BasicNameValuePair("program_subtype", str));
                } else if (type == 3) {
                    formData.add(new BasicNameValuePair("program_subtype", str));
                }

                String result = HttpUtil.sendPostCommand(mContext, url, formData);
                System.out.println("testResource:" + result);

                Message msg = new Message();
                msg.what = MessageConst.CUSTOM_GET_SEARCH_DATA_OK;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        }).start();

    }

    private String sign(String appKey, String api, long timestamp, String appSecret) {
        StringBuilder ss = new StringBuilder();
        ss.append(appSecret);
        ss.append("api=").append(api);
        ss.append("appkey=").append(appKey);
        ss.append("timestamp=").append(timestamp);
        ss.append(appSecret);
        return MD5(ss.toString());
    }

    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    //type:0:频道，
    // 1：具体节目，
    // 2：节目大分类：例如【电视剧】【电视节目】【电影】【体育】 ，
    // 3：节目小分类：例如【动作】【战争】【谍战】【革命】【喜剧等】
    // 10:控制类命令
    private int getSearchType(String name, JSONArray slots) {
        int type = 10;

        if (name.equals("watch_channel")) {
            type = 0;
            return type;
        }
        if (name.equals("watch_a_program")) {
            type = 1;
            return type;
        }
        if (name.equals("find_spacial_program_type")) {
            type = 2;
            return type;
        }
        if (name.equals("query_parade_name")) {
            type = 1;
            return type;
        }
        if (name.equals("query_parade_name_time")) {
            type = 1;
            return type;
        }
        if (name.equals("recommend_tvplay") || name.equals("recommend_new_tvplay")) {
            type = 2;
            return type;
        }
        if (name.equals("recommend_tvshow") || name.equals("can_tvplay") || name.equals("can_tvshow")) {
            if (slots != null && slots.length() > 0) {
                type = 3;
            } else {
                type = 2;
            }
            return type;
        }
        if (name.equals("recommend_new_tvshow")) {
            type = 3;
            return type;
        }
        if (name.equals("query_tvplay") || name.equals("query_tvshow") || name.equals("query_tvplay_play") || name.equals("query_tvshow_play")) {
            if (slots != null && slots.length() > 0) {
                try {
                    JSONObject obj = slots.getJSONObject(0);
                    if (obj.toString().contains("type"))
                        type = 3;
                    if (obj.toString().contains("name"))
                        if (obj.toString().contains("program_classify"))
                            type = 1;
                    type = 3;
                } catch (Exception e) {
                    type = 2;
                    e.printStackTrace();
                }
            } else
                type = 2;
            return type;
        }
        if (name.equals("query_movie") || name.equals("recommend_new") || name.equals("recommend") || name.equals("can")) {
            if (slots != null && slots.length() > 0) {
                try {
                    JSONObject obj = slots.getJSONObject(0);
                    if (obj.toString().contains("s_type"))
                        type = 3;
                    if (obj.toString().contains("s_name"))
                        type = 1;
                } catch (Exception e) {
                    type = 2;
                    e.printStackTrace();
                }
            } else
                type = 2;
            return type;
        }
        return type;
    }

    private void controlOrder(String str_search, String str_type) {
        if ("flip_channel".equals(str_search)) {

        } else if ("flip_channel_last".equals(str_search)) {

        } else if ("flip_channel_next".equals(str_search)) {

        } else if ("flip_number".equals(str_search)) {

        } else if ("turn_to_volume".equals(str_search)) {

        } else if ("turn_volume_muteoff".equals(str_search)) {

        } else if ("turn_volume_up".equals(str_search)) {

        } else if ("turn_volume_down".equals(str_search)) {

        } else if ("turn_volume_muteon".equals(str_search)) {

        } else if ("function".equals(str_search)) {

        } else if ("tv_open".equals(str_search)) {

        } else if ("tv_close".equals(str_search)) {

        }
        showDialog(str_search, str_type);
    }

    private void showDialog(String str_search, String str_type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("正在执行指令");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        TextView tv = new TextView(mContext);
        tv.setText(str_search + "   " + str_type);
        builder.setView(tv);
        builder.show();
    }

    //点击主页tab 标题页各项
    private void StartMainTabSearchFunction(final int tab_index) {
        if (1 == tab_index) {
            quaryEaraData();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String appSecret = "a72966d006c04442a52409019ffd769a";
                String appKey = "879f09a3c19142458703467db7f0449a";
                String api = "epg";

                String base_url = "https://cn.olami.ai/cloudservice/api/epg";
                //具体的接口访问url
                String url = base_url + "/program";
                long time = System.currentTimeMillis();
                String sign = sign(appKey, api, time, appSecret);

                List<NameValuePair> formData = new ArrayList<NameValuePair>();

                //鉴权所需参数
                formData.add(new BasicNameValuePair("appkey", appKey));
                formData.add(new BasicNameValuePair("api", api));
                formData.add(new BasicNameValuePair("sign", sign));
                formData.add(new BasicNameValuePair("timestamp", String.valueOf(time)));

                //发送参数

                long currtime = System.currentTimeMillis();

                String str_start = String.valueOf(currtime);
                String str_end = String.valueOf(currtime+60000);

                String str_send = mContext.getResources().getStringArray(R.array.play_type_array)[tab_index];
                formData.add(new BasicNameValuePair("program_subtype", str_send));
                /*formData.add(new BasicNameValuePair("start_time", str_start));
                formData.add(new BasicNameValuePair("end_time", str_end));*/

                String result = HttpUtil.sendPostCommand(mContext, url, formData);
                System.out.println("testResource :" + result);

                Message msg = new Message();
                msg.what = MessageConst.CUSTOM_GET_MAIN_DATA_OK;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    //获取本地首页
    private void quaryEaraData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String appSecret = "a72966d006c04442a52409019ffd769a";
                String appKey = "879f09a3c19142458703467db7f0449a";
                String api = "epg";

                String base_url = "https://cn.olami.ai/cloudservice/api/epg";
                //具体的接口访问url
                String url = base_url + "/area_program";
                long time = System.currentTimeMillis();
                String sign = sign(appKey, api, time, appSecret);

                List<NameValuePair> formData = new ArrayList<NameValuePair>();

                //鉴权所需参数
                formData.add(new BasicNameValuePair("appkey", appKey));
                formData.add(new BasicNameValuePair("api", api));
                formData.add(new BasicNameValuePair("sign", sign));
                formData.add(new BasicNameValuePair("timestamp", String.valueOf(time)));

                //发送参数
                long currtime = System.currentTimeMillis();

                String str_start = String.valueOf(currtime);
                String str_end = String.valueOf(currtime+60000);

                String str_send = "上海";
                formData.add(new BasicNameValuePair("area", str_send));
                //formData.add(new BasicNameValuePair("start_time", str_start));
                //formData.add(new BasicNameValuePair("end_time", str_end));

                String result = HttpUtil.sendPostCommand(mContext, url, formData);
                System.out.println("testResource:" + result);

                Message msg = new Message();
                msg.what = MessageConst.CUSTOM_GET_MAIN_DATA_OK;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        }).start();
    }

    private void getDeviceInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String appSecret = "a72966d006c04442a52409019ffd769a";
                String appKey = "879f09a3c19142458703467db7f0449a";
                String api = "epg";

                String base_url = "https://cn.olami.ai/cloudservice/api/epg";
                //具体的接口访问url
                String url = base_url + "/support";
                long time = System.currentTimeMillis();
                String sign = sign(appKey, api, time, appSecret);

                List<NameValuePair> formData = new ArrayList<NameValuePair>();

                //鉴权所需参数
                formData.add(new BasicNameValuePair("appkey", appKey));
                formData.add(new BasicNameValuePair("api", api));
                formData.add(new BasicNameValuePair("sign", sign));
                formData.add(new BasicNameValuePair("timestamp", String.valueOf(time)));
                //api接口所需参数
                formData.add(new BasicNameValuePair("province", "上海"));
                formData.add(new BasicNameValuePair("controller_type", "top_box_remote_controller"));
                /*formData.add(new BasicNameValuePair("company", "东方有线"));
                formData.add(new BasicNameValuePair("product_model", "龙晶一体机"));*/
                // HttpEntity reqEntity = new UrlEncodedFormEntity(formData, StandardCharsets.UTF_8);
                String result = HttpUtil.sendPostCommand(mContext, url, formData);
                System.out.println("testResource:" + result);
            }
        }).start();
    }

    private void actionSearch(String temp) {
        sendMessageToService(MessageConst.CLIENT_ACTION_SENT_TEXT, 0, 0, null, temp);
    }


    private void changeViewType(int type) {
        if (mCurViewType == type) return;

        mCurViewType = type;
        mSubMainView.setVisibility(View.GONE);
        mSubSearchView.setVisibility(View.GONE);
        mSubSearchResult.setVisibility(View.GONE);

        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        }

        if (0 == type) {
            cancelSearch.setVisibility(View.GONE);
        } else {
            cancelSearch.setVisibility(View.VISIBLE);
        }

        if (0 == type) {
            mSubMainView.setVisibility(View.VISIBLE);
        } else if (1 == type) {
            mSubSearchView.setVisibility(View.VISIBLE);
        } else if (2 == type) {
            mSubSearchResult.setVisibility(View.VISIBLE);
        } else {
            mSubMainView.setVisibility(View.VISIBLE);
        }
    }

    private void updataMainViewData(Object obj) {
        String str_obj = (String) obj;
        JSONObject temp = null;

        try {
            temp = new JSONObject(str_obj);

            if (temp.has("status") && temp.get("status").equals("ok")) {
                JSONObject json_data = temp.getJSONObject("data").getJSONObject("epg");

                if (json_data.has("data_obj")) {
                    if (MainContentData.playData == null) {
                        MainContentData.playData = new ArrayList<PlayContentEntity>();
                    } else {
                        MainContentData.playData.clear();
                    }

                    long systime = System.currentTimeMillis();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date d1 = new Date(systime);
                    String str_cur = format.format(d1);

                    JSONArray data_data = json_data.getJSONArray("data_obj");

                    if (data_data.length() < 1) {
                        ShowToast("没有找到您要搜索的内容");

                    } else {
                        for (int k = 0; k < data_data.length(); k++) {
                            JSONArray data = data_data.getJSONObject(k).getJSONArray("result");

                            for (int i = 0; i < data.length(); i++) {
                            /*
                            *  "channel_id": "CCTV1",
                                "channel_title": "CCTV-1综合",
                                "program_id": "QmRkaClbYQ==",
                                "program_title": "生活提示2017-162",
                                "start_time": "2017/07/25 00:14:00"，
                                program_type："",
                                program_subtype:["",""],
                            * */
                                PlayContentEntity entity = new PlayContentEntity();
                                JSONObject tmp = data.getJSONObject(i);
                                entity.name = tmp.getString("channel_title");
                                entity.subName = tmp.getString("program_title");
                                if (tmp.has("image_id")) {
                                    entity.str_ImageId = tmp.getString("image_id");
                                } else {
                                    entity.str_ImageId = "0";
                                }

                                String s_time = tmp.getString("start_time");
                                String e_time = tmp.getString("end_time");
                                SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //"2017-09-26 17:00:00"
                                try {
                                    long ds = dFormat.parse(s_time).getTime();
                                    long de = dFormat.parse(e_time).getTime();
                                    long curDate = new Date(System.currentTimeMillis()).getTime();
                                    entity.time = (int) (((curDate - ds) * 100) / (de - ds));
                                } catch (Exception e) {

                                }
                                MainContentData.playData.add(entity);
                            }
                        }
                    }
                    mSubMainView.setData(MainContentData.playData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void ShowToast(final String message) {

        /*Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();*/
        CustomOlaTipDialog dialog = CustomOlaTipDialog.createDialog(MainActivity.this);
        dialog.setMessage(message);
        dialog.show();
    }


}
