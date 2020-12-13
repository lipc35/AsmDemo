package com.tiny.asmdemo;

import android.app.Application;

import com.tinyws.lollipops.LollipopsHelper;
import com.tinyws.lollipops.LollipopsParams;

/**
 * @author : tiny
 * @version :
 * @description : CandyApp
 * @date : 2020/12/13 12:45 AM
 */
public class CandyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LollipopsParams params = new LollipopsParams();
        LollipopsHelper.init(params);
    }
}
