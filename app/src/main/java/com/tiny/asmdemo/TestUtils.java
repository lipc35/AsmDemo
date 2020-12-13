package com.tiny.asmdemo;

/**
 * @author : tiny
 * @version :
 * @description : TestUtils
 * @date : 2020/12/13 6:00 PM
 */
public class TestUtils {
    public static void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
