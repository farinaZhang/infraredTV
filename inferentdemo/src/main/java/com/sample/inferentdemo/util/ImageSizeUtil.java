package com.sample.inferentdemo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * 仙
 * <p/>
 * 桃花坞里桃花庵，桃花庵下桃花仙。
 * 桃花仙人种桃树，又摘桃花换酒钱
 * <p/>
 * 酒醒只在花前坐，酒醉还来花下眠。
 * 半醒半醉日复日，花开花落年复年。
 * <p/>
 * 但愿老死花酒间，不愿鞠躬马车前。
 * 车尘马足富者趣，酒盏花枝贫者缘。
 * <p/>
 * 若将富贵比贫贱，一在平地一在天。
 * 若将贫贱比马车，他得驱驰我得闲。
 * <p/>
 * 别人笑我太疯癫，我笑他人看不穿。
 * 不见五陵豪杰墓，无花无酒锄作田。
 * <p/>
 * ============================================================
 * <p/>
 * 版      权 ： SHSF集团 版权所有 (c) 2016
 * <p/>
 * 作      者  :Canislupus
 * <p/>
 * 版      本 ： 1.0
 * <p/>
 * 创建日期 ： 2016/8/4  13:35
 * <p/>
 * 描      述 ：图片处理类
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/
public class ImageSizeUtil {
    private static final String TAG = "ImageSizeUtil";

    /**
     * 获取图片的尺寸
     *
     * @param imageView 容器
     * @return 图片尺寸
     */
    public static ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        int width = imageView.getWidth();
        if (width <= 0) {
            width = layoutParams.width;
        }
        if (width <= 0) {
            width = getImageViewFieldValue(imageView,"mMaxWidth");
        }
        if (width <= 0){
            width = displayMetrics.widthPixels;
        }
        int height = imageView.getHeight();
        if (height <= 0) {
            height = layoutParams.height;
        }
        if (height <= 0) {
            height = getImageViewFieldValue(imageView,"mMaxHeight");
        }

        if (height <= 0){
            height = displayMetrics.heightPixels;
        }
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    /**
     * 通过反射获取imageView某个属性的值
     * @param object imageview
     * @param fieldName 属性名
     * @return 属性值
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue< Integer.MAX_VALUE){
                value = fieldValue;
            }
        } catch (Exception e) {
            Log.e(TAG,"反射获取imageview某个属性的值出现异常");
        }
        return value;
    }

    /**
     * 根据图片要显示的宽高对图片进行压缩
     *
     * @param path   图片地址
     * @param height 目标高度
     * @param width  目标宽度
     * @return       压缩后的图片
     */
    public static Bitmap decodeBitmapFromPath(String path, int height, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize = calculateInSampleSize(options,width,height);
        options.inJustDecodeBounds = false;







        return BitmapFactory.decodeFile(path, options);

    }

    /**
     * 根据需求的宽高计算inSampleSize
     * @param options o
     * @param width 需求宽
     * @param height 需求高
     * @return  inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int inSampleSize = 1;
        if (outHeight > height || outWidth > width ){
            int heightRound = Math.round(outHeight * 1.0f / height);
            int widthRound = Math.round(outWidth * 1.0f / width);
            inSampleSize = Math.max(heightRound,widthRound);
        }
        return inSampleSize;
    }



    /**
     * 定义图片
     */
    public static class ImageSize {
        int height;
        int width;
    }
}
