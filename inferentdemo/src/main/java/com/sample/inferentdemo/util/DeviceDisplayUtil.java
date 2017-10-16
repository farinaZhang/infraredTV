package com.sample.inferentdemo.util;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.sample.inferentdemo.R;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;



/**
 * Created by FarinaZhang on 2016/12/1.
 */
public class DeviceDisplayUtil {

    Device device;

    public DeviceDisplayUtil(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    // DOC:DETAILS
    public String getDetailsMessage() {
        StringBuilder sb = new StringBuilder();
        if (getDevice().isFullyHydrated()) {
            sb.append(getDevice().getDisplayString());
            sb.append("\n\n");
            for (Service service : getDevice().getServices()) {
                sb.append(service.getServiceType()).append("\n");
            }
        } else {
            sb.append("");//设备没有找到
        }
        return sb.toString();
    }
    // DOC:DETAILS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDisplayUtil that = (DeviceDisplayUtil) o;
        return device.equals(that.device);
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }

    @Override
    public String toString() {
//            String name =
//                getDevice().getDetails() != null && getDevice().getDetails().getFriendlyName() != null
//                    ? getDevice().getDetails().getFriendlyName()
//                    : getDevice().getDisplayString();
        // Display a little star while the device is being loaded (see performance optimization earlier)
       /* String name = getDevice().getDetails().getFriendlyName() + " " + getDevice().getDetails().getModelDetails().getModelName() +
                getDevice().getDetails().getModelDetails().getModelDescription();

        String temp = device.isFullyHydrated() ? name : name;*/

        String name = getDevice().getDetails().getModelDetails().getModelName();
        String temp = name;
        LogUtil.d("DeviceDisplayUtil"," find a device name : "+temp);
        return temp;
    }

    public static void setTranslucentStatus(Activity acticity ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = acticity.getWindow();

            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemStatusManager tintManager = new SystemStatusManager(acticity);
            tintManager.setStatusBarTintEnabled(true);  // 激活状态栏设置
            tintManager.setStatusBarTintResource(0);//状态栏无背景
        }
    }

    public static void setLightStatusBar(Activity acticity){
        String model = Build.MODEL;
        String sdk = Build.VERSION.SDK;
        String release = Build.VERSION.RELEASE;

        String temp = release.substring(0, 1);
        int j = Integer.parseInt(temp);
        if (j == 6) {
            String str = release.substring(release.length() - 1);
            int i = Integer.parseInt(str);
            if (i > 0) {
                LogUtil.d("DeviceDisplayUtil", "setLightStatusBar Android6.0.1以上 白底黑字");
                StatusBarUtil.transparencyBar(acticity);//透明
                StatusBarUtil.setStatusBarColor(acticity, R.color.app_bg);//颜色
                StatusBarUtil.StatusBarLightMode(acticity);//黑字
            }
        } else if (j > 6) {
            LogUtil.d("DeviceDisplayUtil", "setLightStatusBar Android6.0.1以上 白底黑字");
            StatusBarUtil.transparencyBar(acticity);//透明
            StatusBarUtil.setStatusBarColor(acticity, R.color.app_bg);//颜色
            StatusBarUtil.StatusBarLightMode(acticity);//黑字
        }
    }
}
