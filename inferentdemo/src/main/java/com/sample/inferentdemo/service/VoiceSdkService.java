package com.sample.inferentdemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.sample.inferentdemo.MyApplication;
import com.sample.inferentdemo.imain.CommunicationAssist;
import com.sample.inferentdemo.message.MessageConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ai.olami.aiCloudService.sdk.engin.OlamiVoiceRecognizer;
import ai.olami.aiCloudService.sdk.interfaces.IOlamiVoiceRecognizerListener;


/**
 * Created by FarinaZhang on 2017/4/21.
 * MainService
 * <p/>
 * 用于语音识别
 */

public class VoiceSdkService extends Service {

    private String TAG = "VoiceSdkService";
    private Handler mInComingHandler;
    private VoiceSdkComAssist mVoiceSdkComAssist;
    private OlamiVoiceRecognizer mViaVoiceRecognizer;
    private IOlamiVoiceRecognizerListener mViaVoiceRecognizerListener;
    private ConsumerIrManager mCIR;
    private boolean isRecording = false;

    @Override
    public void onCreate() {
        initInComingHandler();
        initCommunicationAssist();
        initViaVoiceRecognizerListener();
        init();
        mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    //设置语音识别相关初始化参数
    public void init() {
        mViaVoiceRecognizer = new OlamiVoiceRecognizer(VoiceSdkService.this);
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.getBaseContext().TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        //mViaVoiceRecognizer.init("12345678");
        mViaVoiceRecognizer.init(imei); //设置用户号，用于区分用户，一般设置为iemi号

        mViaVoiceRecognizer.setListener(mViaVoiceRecognizerListener); //设置语音设备listener，监听语音识别开始，结束，结果，及各种出错。
        mViaVoiceRecognizer.setLocalization(OlamiVoiceRecognizer.LANGUAGE_SIMPLIFIED_CHINESE);// 设置返回结果语言类型，目前支持简体和繁体
        //(String appKey, String api, String secret, String seq)
        mViaVoiceRecognizer.setAuthorization("879f09a3c19142458703467db7f0449a", "asr", "a72966d006c04442a52409019ffd769a", "nli");

        mViaVoiceRecognizer.setVADTailTimeout(3000);

        mViaVoiceRecognizer.setLatitudeAndLongitude(31.155364678184498, 121.34882432933009);

    }

    private void initInComingHandler() {
        mInComingHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageConst.CLIENT_ACTION_START_RECORED:
                        if (mViaVoiceRecognizer != null && (isRecording == false)) {
                            Log.d(TAG,"MESSAGE  开始录音");
                            isRecording = true;
                            mViaVoiceRecognizer.start();
                        }
                        break;
                    case MessageConst.CLIENT_ACTION_STOP_RECORED:
                        if (mViaVoiceRecognizer != null && (isRecording == true)) {
                            //isRecording = false;
                            mViaVoiceRecognizer.stop();
                        }
                        break;
                    case MessageConst.CLIENT_ACTION_CANCEL_RECORED:
                        if (mViaVoiceRecognizer != null && isRecording == true) {
                            //isRecording = false;
                            mViaVoiceRecognizer.cancel();
                        }
                        break;
                    case MessageConst.CLIENT_ACTION_SENT_TEXT:
                        if (mViaVoiceRecognizer != null)
                            mViaVoiceRecognizer.sendText(msg.obj.toString());
                        break;

                }
            }
        };
    }

    private void initViaVoiceRecognizerListener() {
        mViaVoiceRecognizerListener = new olamiVoiceRecognizerListener();
    }

    private class olamiVoiceRecognizerListener implements IOlamiVoiceRecognizerListener {

        @Override
        public void onError(int errCode) {
            Log.d(TAG, "onError ");
            // TODO Auto-generated method stub
            if(isRecording){
                isRecording = false;
            }
            sendMessageToActivity(MessageConst.CLENT_END_RECOGNIZER, 0, 0, null, null);
            sendMessageToActivity(MessageConst.CLIENT_ACTION_SPEAK_ERROR, 0, 0, null, null);
        }

        @Override
        public void onEndOfSpeech() {
            // TODO Auto-generated method stub
            Log.d(TAG, "onEndOfSpeech ");
            sendMessageToActivity(MessageConst.CLENT_END_SPEECH_RECORDING, 0, 0, null, null);

        }

        @Override
        public void onBeginningOfSpeech() {
            // TODO Auto-generated method stub
            Log.d(TAG, "onBeginningOfSpeech ");
            if(false == isRecording){
                isRecording = true;
            }
            sendMessageToActivity(MessageConst.CLENT_START_SPEECH_RECORDING, 0, 0, null, null);

        }

        @Override
        public void onResult(String result, int type) {
            Log.d(TAG, "speak onResult ,result=" + result);
            getValidData(result);

            if(isRecording) {
                isRecording = false;
            }
            sendMessageToActivity(MessageConst.CLENT_END_RECOGNIZER, 0, 0, null, null);
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel ");
            // TODO Auto-generated method stub
            if(isRecording) {
                isRecording = false;
            }
            sendMessageToActivity(MessageConst.CLENT_ACTION_CANCEL_TO_ACTIVITY, 0, 0, null, null);
        }

        @Override
        public void onUpdateVolume(int volume) {
            Log.d(TAG, "onUpdateVolume ");
            // TODO Auto-generated method stub
            sendMessageToActivity(MessageConst.CLENT_ACTION_ON_VOLUME_UPDATE, volume, 0, null, null);
        }

    }

    private void initCommunicationAssist() {
        mVoiceSdkComAssist = new VoiceSdkComAssist();
        MyApplication.getInstance().setActivityToServiceListener(mVoiceSdkComAssist);
    }


    private void sendMessageToActivity(int what, int arg1, int arg2, Bundle data, Object obj) {
        if (MyApplication.getInstance().getServiceToActivityListener() != null)
            MyApplication.getInstance().getServiceToActivityListener().callBack(what, arg1, arg2, data, obj);
    }

    private class VoiceSdkComAssist implements CommunicationAssist {

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
        if (mViaVoiceRecognizer != null)
            mViaVoiceRecognizer.destroy();
    }

    //解析语音结果数据
    private void getValidData(final String ValidData) {

        //{"data":{"asr":{"result":"你好","speech_status":0,"final":true,"status":0},"nli":[{"desc_obj":{"result":"你想说什么呀，说清楚点好不好？","status":"1007"},"type":"ds"}]},"status":"ok"}
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jTemp;
                    JSONObject jDataSpeak;
                    JSONObject jDataAnswer;

                    jTemp = new JSONObject(ValidData);

                    if (jTemp.getString("status").equals("ok")) {
                        //服务器返回结果正确
                        jDataAnswer = jTemp.getJSONObject("data").getJSONArray("nli").getJSONObject(0);


                        JSONObject descObject = jDataAnswer.getJSONObject("desc_obj");

                        if (descObject.has("status") && descObject.getInt("status") == 0) {
                            JSONObject semanticObject = jDataAnswer.getJSONArray("semantic").getJSONObject(0);
                            String stringInput = semanticObject.getString("input");
                            sendMessageToActivity(MessageConst.CLENT_SHOW_INPUT, 0, 0, null, stringInput); //输入
                            String modifystr = semanticObject.getJSONArray("modifier").getString(0);
                            sendMessageToActivity(MessageConst.SERVER_ACTION_RETURN_RESULT, 0, 0, null, jDataAnswer); //输入
                            //SendCommondToDevice(modifystr);
                        } else if (descObject.has("result")) {
                            String answer = descObject.getString("result");

                            sendMessageToActivity(MessageConst.CLIENT_ACTION_RETURN_OTHER_RESULT, 0, 0, null, answer);
                        } else {
                            sendMessageToActivity(MessageConst.CLIENT_ACTION_SPEAK_ERROR, 0, 0, null, null);
                        }


                    } else {
                        sendMessageToActivity(MessageConst.CLIENT_ACTION_SPEAK_ERROR, 0, 0, null, null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void SendCommondToDevice(String str_commond) {
        /*if (!mCIR.hasIrEmitter()) {
            Toast.makeText(this,"未找到红外发生器",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "未找到红外发身器！");
            return;
        }*/

        //[8985,4481,578,555,578,555,578,555,578,555,578,555,578,555,578,555,578,555,578,1688,578,1688,578,1688,578,1688,578,1688,578,1688,578,555,578,1688,578,1688,578,555,578,1688,578,1688,578,555,578,555,578,556,578,555,578,555,578,1688,578,555,578,555,578,1688,578,1688,578,1688,578,1688,578,40734,8985,2242,578,96165]
        if (str_commond.equals("tv_open")) {
            //int[] pattern = { 8985,4481,578,555,578,555,578,555,578,555,578,555,578,555,578,555,578,555,578,1688,578,1688,578,1688,578,1688,578,1688,578,1688,578,555,578,1688,578,1688,578,555,578,1688,578,1688,578,555,578,555,578,556,578,555,578,555,578,1688,578,555,578,555,578,1688,578,1688,578,1688,578,1688,578,40734,8985,2242,578,96165 };
            int[] pattern = {9000, 4500, 560, 565, 560, 565, 560, 565, 560, 565, 560, 565, 560, 565, 560, 565, 560, 565, 560, 1690, 560, 1690, 560, 1690, 560, 1690, 560, 1690, 560, 1690, 560, 565, 560, 1690, 560, 1690, 560, 565, 560, 1690, 560, 1690, 560, 565, 560, 565, 560, 565, 560, 565, 560, 565, 560, 1690, 560, 565, 560, 565, 560, 1690, 560, 1690, 560, 1690, 560, 1690, 560};
            mCIR.transmit(37950, pattern);
        } else if (str_commond.equals("tv_close")) {
            int[] pattern = {8985, 4481, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 555, 578, 1688, 578, 1688, 578, 555, 578, 1688, 578, 1688, 578, 555, 578, 555, 578, 556, 578, 555, 578, 555, 578, 1688, 578, 555, 578, 555, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 40734, 8985, 2242, 578, 96165};
            mCIR.transmit(37950, pattern);
        } else if (str_commond.equals("turn_volume_muteon")) {//静音
            int[] pattern = {8985, 4481, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 579, 555, 578, 555, 578, 555, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 555, 578, 1688, 578, 555, 578, 1688, 578, 1688, 579, 1687, 578, 555, 578, 555, 578, 555, 578, 556, 578, 1688, 578, 555, 578, 555, 578, 555, 578, 1688, 579, 1687, 578, 1688, 578, 1689, 578, 40734, 8985, 2242, 578, 96163};
            mCIR.transmit(37950, pattern);
        } else if (str_commond.equals("turn_volume_up")) {//音量大
            int[] pattern = {8985, 4481, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 555, 578, 1688, 578, 555, 578, 555, 578, 555, 579, 1688, 578, 1688, 578, 555, 578, 555, 578, 555, 578, 1688, 578, 1688, 578, 1688, 579, 554, 578, 555, 578, 1688, 578, 1688, 578, 1688, 578, 40704};
            mCIR.transmit(37950, pattern);
        } else if (str_commond.equals("turn_volume_down")) {//音量小
            int[] pattern = {8985, 4481, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 555, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 1688, 578, 555, 578, 1688, 578, 1688, 578, 555, 578, 555, 579, 1688, 578, 1688, 578, 555, 578, 555, 578, 555, 578, 555, 578, 1688, 578, 1688, 578, 555, 579, 555, 578, 1688, 578, 1688, 578, 1688, 578, 40703};
            mCIR.transmit(37950, pattern);
        }

    }

    private int[] getSendDataFromString(String data) {

        int[] result = new int[]{};
        if (data.length() < 1) return null;

        int len = data.length();
        int count = 0;
        List<Integer> pos = new ArrayList<Integer>();
        for (int i = 0; i < len; i++) {
            if (data.charAt(i) == ',') {
                pos.add(new Integer(i));
                count++;
            }
        }
        pos.add(new Integer(len));
        count++;

        for (int j = 0; j < count; j++) {
            int subdata;
            if (j == 0) {
                subdata = (int) (Integer.valueOf(data.substring(0, pos.get(0))));
            } else {
                subdata = (int) (Integer.valueOf(data.substring(pos.get(j - 1), pos.get(j))));
            }

            result[j] = new Integer(subdata);
        }

        return result;
    }
}
