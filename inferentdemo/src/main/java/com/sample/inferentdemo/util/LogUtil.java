package com.sample.inferentdemo.util;

import android.util.Log;

/* ============================================================

 * ============================================================
 **/
public class LogUtil {


    private static final boolean ENABLE = true;

    /**
     * 打印一个debug等级的log
     */
    public static void d(String tag, String content) {
        if (ENABLE) {
            int p = 2000;
            long length = content.length();
            if (length < p || length == p)
                Log.d(tag, content);
            else {
                while (content.length() > p) {
                    String logContent = content.substring(0, p);
                    content = content.replace(logContent, "");
                    Log.d(tag, logContent);
                }
                Log.d(tag, content);
            }
        }
    }

    /**
     * 打印一个 error 等级的log
     */
    public static void e(String tag, String content) {
        if (ENABLE) {
            int p = 2000;
            long length = content.length();
            if (length < p || length == p)
                Log.d(tag, content);
            else {
                while (content.length() > p) {
                    String logContent = content.substring(0, p);
                    content = content.replace(logContent, "");
                    Log.d(tag, logContent);
                }
                Log.d(tag, content);
            }
        }
    }

    /**
     * 打印一个 error 等级的log
     */
    public static void e(Class cls, String content) {
        if (ENABLE) {
            int p = 2000;
            long length = content.length();
            if (length < p || length == p)
                Log.d("viash_voice_ola_" + cls.getSimpleName(), content);
            else {
                while (content.length() > p) {
                    String logContent = content.substring(0, p);
                    content = content.replace(logContent, "");
                    Log.d("viash_voice_ola_" + cls.getSimpleName(), logContent);
                }
                Log.d("viash_voice_ola_" + cls.getSimpleName(), content);
            }
        }
    }


}
