package com.sample.inferentdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.data.ChannelPlayListData;
import com.sample.inferentdemo.data.PlayTimeEntity;
import com.sample.inferentdemo.message.MessageConst;
import com.sample.inferentdemo.util.HttpUtil;
import com.sample.inferentdemo.util.ImageLoaderUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by FarinaZhang on 2017/7/26.
 */
public class TvProgramReview extends RelativeLayout {
    private final String TAG_SEARCH = "TAG_SEARCH";
    private ImageView mImageReview;
    private TextView mProgramName;
    private ProgressBar mProgressBar;
    private TextView mPlayName;
    private Context mContext;
    private int mImageWidth = 200;
    private int mImageHeight = 200;
    private int mImageId = 0;
    private Handler mHandler;
    private int curIndex = 0;


    public TvProgramReview(Context context) {
        this(context, null);
    }

    public TvProgramReview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvProgramReview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        View myView = LayoutInflater.from(context).inflate(R.layout.layout_tvtrogram_review, null);
        myView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.addView(myView);

        mImageReview = (ImageView) myView.findViewById(R.id.image_review);
        mProgramName = (TextView) myView.findViewById(R.id.program_name);
        mProgressBar = (ProgressBar) myView.findViewById(R.id.play_progress);
        mPlayName = (TextView) myView.findViewById(R.id.play_name);

        TypedArray mTypes = context.obtainStyledAttributes(attrs, R.styleable.playImg);

        mImageWidth = mTypes.getDimensionPixelSize(R.styleable.playImg_imageWidth, 200);
        mImageHeight = mTypes.getDimensionPixelSize(R.styleable.playImg_imageHeight, 200);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mImageReview.getLayoutParams();
        lp.width = mImageWidth;
        lp.height = mImageHeight;
        mImageReview.setLayoutParams(lp);

        mImageReview.setImageResource(mImageId);

        initHandler();

        mImageReview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //play the select progress
                //ShowToast("开始播放" + mPlayName.getText().toString());

                if (true) {
                    //打开弹窗功能
                    String sendText = mProgramName.getText().toString();

                    //sendText = "CCTV";
                    GetDataFromServer(sendText);

                }
            }
        });

        /*mImageReview.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (true) {
                    //打开弹窗功能
                    String sendText = mProgramName.getText().toString();
                    sendText = "湖南卫视";
                    GetDataFromServer(sendText);
                    return true;
                }
                return false;
            }
        });*/

    }

    //设置节目预览图片
    public void setImageReviewPictrue(String str_ImageId, int defImg) {
        Drawable imgdef = mContext.getResources().getDrawable(defImg);


        if (str_ImageId.equals("")) {
            mImageReview.setImageDrawable(imgdef);
        } else {
            String str_imgUrl = "http://cn.olami.ai/ImageService/epgprogram_get_image?id=" + str_ImageId;
            ImageLoaderUtil.loadImageAsync(mImageReview, str_imgUrl, null, imgdef, mImageWidth);
        }

    }

    //设置台名及台号
    public void setProgramName(String name) {
        mProgramName.setText(name);
    }

    //设置节目播放进度
    public void setPlayProgress(int value) {
        mProgressBar.setProgress(value);
    }

    //设置节目名
    public void setPlayName(String name) {
        mPlayName.setText(name);
    }

    private void ShowToast(String message) {
        //Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();

        CustomOlaTipDialog dialog = CustomOlaTipDialog.createDialog(mContext);
        dialog.setMessage(message);
        dialog.show();
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageConst.CUSTOM_GET_SEARCH_DATA_OK: {
                        String str_obj = (String) msg.obj;
                        JSONObject temp = null;

                        try {
                            temp = new JSONObject(str_obj);

                            if (temp.has("status") && temp.get("status").equals("ok")) {
                                JSONObject json_data = temp.getJSONObject("data").getJSONObject("epg");

                                if (json_data.has("data_obj")) {
                                    if (ChannelPlayListData.playData == null) {
                                        ChannelPlayListData.playData = new ArrayList<PlayTimeEntity>();
                                    } else {
                                        ChannelPlayListData.playData.clear();
                                    }

                                    JSONArray data_data = json_data.getJSONArray("data_obj");

                                    String channel_name = "";
                                    if (data_data==null ||data_data.length() < 1) {
                                        ShowToast("没有找到您要搜索的内容");

                                    } else {
                                        for (int k = 0; k < data_data.length(); k++) {
                                            SimpleDateFormat dFormat_s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //"2017-09-26 17:00:00"
                                            Date time_date = new Date(Long.parseLong(data_data.getJSONObject(k).getString("date")));

                                            Date curDate_s = new Date(System.currentTimeMillis());//获取当前时间


                                            String date_get = dFormat_s.format(time_date).toString().substring(0, 10);//获取的数据的日期

                                            String date_cur = dFormat_s.format(curDate_s).toString().substring(0, 10);  //获取当前日期
                                            try {
                                                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date_get);
                                                long unixTimestamp1 = date1.getTime();

                                                Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date_cur);
                                                long unixTimestamp2 = date2.getTime();

                                                Log.d(TAG_SEARCH, "获取的数据的当前日期时间 :" + date_get);
                                                Log.d(TAG_SEARCH, "系统的日期时间 :" + date_cur);
                                                long m = (unixTimestamp1 - unixTimestamp2) / (24 * 3600 * 1000);
                                                Log.d(TAG_SEARCH, " 获取的数据和今天的日期的差距为" + m);

                                                if(m!=0){
                                                    //不是当天的节目
                                                    break;
                                                }
                                            //try {
                                                JSONArray data = data_data.getJSONObject(k).getJSONArray("result");
                                                for (int i = 0; i < data.length(); i++) {

                                                    int isplaying = 0; //0:未播放，1：正在播放，2：已播放
                                                    JSONObject tmp = data.getJSONObject(i);

                                                    String s_time = tmp.getString("start_time");
                                                    String e_time = tmp.getString("end_time");
                                                    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //"2017-09-26 17:00:00"
                                                    try {
                                                        long ds = dFormat.parse(s_time).getTime();
                                                        long de = dFormat.parse(e_time).getTime();
                                                        long curDate = new Date(System.currentTimeMillis()).getTime();

                                                        if (curDate >= ds && curDate < de) {
                                                            isplaying = 1;
                                                            curIndex = i;
                                                        } else if (curDate < ds) {
                                                            //0:未播放，1：正在播放，2：已播放
                                                            isplaying = 0;
                                                        } else {
                                                            isplaying = 2;
                                                        }
                                                    } catch (Exception e) {
                                                        ShowToast("请输入你要查找的节目或频道");
                                                    }

                                                    PlayTimeEntity entityTime = new PlayTimeEntity();
                                                    entityTime.channel = tmp.getString("channel_title");
                                                    entityTime.name = tmp.getString("program_title");
                                                    String str_time = tmp.getString("start_time").substring(11, 16);
                                                    entityTime.time = str_time;
                                                    entityTime.isplaying = isplaying;

                                                    channel_name = entityTime.channel;
                                                    ChannelPlayListData.playData.add(entityTime);
                                                }

                                            }catch(Exception e) {
                                                ShowToast("请输入你要查找的节目或频道");
                                            }
                                        }
                                    }

                                    //更新list 列表
                                    if (ChannelPlayListData.playData.size() > 0) {
                                        PlayListPopDialog dialog = PlayListPopDialog.createDialog(mContext, ChannelPlayListData.playData);
                                        String title = channel_name + "今日节目单";
                                        dialog.setTitle(title);
                                        dialog.setSelectIndex(curIndex);

                                        dialog.setListener(new PlayListPopDialog.PlayListPopDialogListener() {
                                            @Override
                                            public void onItemClick(int positon) {
                                                String name = ChannelPlayListData.playData.get(positon).name;
                                                ShowToast("马上为您播放："+name);
                                            }
                                        });
                                        dialog.show();
                                    }else{
                                        ShowToast("没有找到今日节目单");
                                    }
                                }else{
                                    ShowToast("数据为空，请检查网络");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowToast("请输入你要查找的节目或频道");
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            ShowToast("请输入你要查找的节目或频道");
                        }
                        break;
                    }
                }
            }
        };
    }

    private void GetDataFromServer(final String temp) {

        //开始搜索该频道的节目列表显示
        new Thread(new Runnable() {
            @Override
            public void run() {
                String appSecret = "a72966d006c04442a52409019ffd769a";
                String appKey = "879f09a3c19142458703467db7f0449a";
                String api = "epg";

                String base_url = "https://cn.olami.ai/cloudservice/api/epg";
                //具体的接口访问url
                String url = base_url + "/program";
                long curtime = System.currentTimeMillis();
                String sign = sign(appKey, api, curtime, appSecret);

                List<NameValuePair> formData = new ArrayList<NameValuePair>();

                //鉴权所需参数
                formData.add(new BasicNameValuePair("appkey", appKey));
                formData.add(new BasicNameValuePair("api", api));
                formData.add(new BasicNameValuePair("sign", sign));
                formData.add(new BasicNameValuePair("timestamp", String.valueOf(curtime)));
                //发送参数
                //获取当前频道当天的节目，正在播出和未播出

                /*Calendar cal_s = Calendar.getInstance();
                cal_s.set(Calendar.HOUR_OF_DAY, 0);
                cal_s.set(Calendar.SECOND, 0);
                cal_s.set(Calendar.MINUTE, 0);
                cal_s.set(Calendar.MILLISECOND, 0);
                long s_time = cal_s.getTimeInMillis();

                Calendar cal_e = Calendar.getInstance();
                cal_e.set(Calendar.HOUR_OF_DAY, 23);
                cal_e.set(Calendar.SECOND, 59);
                cal_e.set(Calendar.MINUTE, 59);
                cal_e.set(Calendar.MILLISECOND, 0);
                long e_time = cal_e.getTimeInMillis();*/

                formData.add(new BasicNameValuePair("channel", temp));
                /*formData.add(new BasicNameValuePair("start_time", String.valueOf(s_time)));
                formData.add(new BasicNameValuePair("end_time", String.valueOf(e_time)));*/

                String result = HttpUtil.sendPostCommand(mContext, url, formData);
                //Log.d("TVprogramReview", "获取当前频道当天的节目: channl:" + temp + "  start_time:" + s_time + "  end_time:" + e_time);
                System.out.println("testResource:" + result);
                if(result==null||result.length()<1){
                    mHandler.sendEmptyMessage(MessageConst.CLENT_ACTION_NO_CONNECT);
                    return;
                }
                Message msg = new Message();
                msg.what = MessageConst.CUSTOM_GET_SEARCH_DATA_OK;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        }

        ).start();

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
}
