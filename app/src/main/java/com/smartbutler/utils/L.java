package com.smartbutler.utils;

import android.util.Log;

/**
 * Log工具类封装
 */

public class L {
    // 开关
    public static final boolean DEBUG = true;
    // TAG
    public static final String TAG = "SmartButler";

    // 五个等级
    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (DEBUG) {
            Log.e(TAG, text);
        }
    }

    public static void f(String text) {
        if (DEBUG) {
            Log.wtf(TAG, text);
        }
    }
}
