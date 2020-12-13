package com.tinyws.lollipops;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : tiny
 * @version :
 * @description : MethodConsumeHelper
 * @date : 2020/11/15 11:06 PM
 */
public class LollipopsHelper {
    public static Map<String, Long> consumeMaps = new HashMap<>();
    private static boolean onlyPrintMain;
    private static int edge = -1;
    private static String logTag = "lollipops";

    public static void init(LollipopsParams params) {
        onlyPrintMain = params.isOnlyPrintMain();
        edge = params.getEdge();
        if (!TextUtils.isEmpty(params.getLogTag())) {
            logTag = params.getLogTag();
        }
    }

    public static void start(String className, String methodName) {
        long start = System.currentTimeMillis();
        consumeMaps.put(className + methodName, start);
    }

    public static void end(String className, String methodName) {
        long start = consumeMaps.get(className + methodName);
        long gap = System.currentTimeMillis() - start;
        if (onlyPrintMain) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                if (gap > edge) {
                    Log.e(logTag, className + ":" + methodName + ":[" + gap + "]");
                } else {
                    Log.d(logTag, className + ":" + methodName + ":[" + gap + "]");
                }
            }
        } else {
            if (gap > edge) {
                Log.e(logTag, className + ":" + methodName + ":[" + gap + "]");
            } else {
                Log.d(logTag, className + ":" + methodName + ":[" + gap + "]");
            }
        }
    }
}
