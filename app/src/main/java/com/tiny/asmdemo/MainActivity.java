package com.tiny.asmdemo;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tiny.asm1demo.AnnotionUtils;

/**
 * @author : tiny
 * @version :
 * @description : MainActivity
 * @date : 2020/12/8 12:48 AM
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.initStatusBar(this, false);
        test();
        test1();
        TestUtils.test();
        AnnotionUtils.aaa();
    }

    public int test() {
        int i = 10;
        i++;
        if (i > 100) {
            Log.i("lipc", "test: ");
        }
        return i;
    }

    public int test1() {
        try {
            int i = 10;
            i++;
            if (i > 100) {
                Log.i("lipc", "test: ");
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
