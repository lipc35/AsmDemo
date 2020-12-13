package com.tinyws;

import com.tinyws.lollipops.LollipopsConfig;

import org.gradle.api.Action;

import groovy.lang.Closure;

/**
 * @author : tiny
 * @version :
 * @description : TinyConfig
 * @date : 2020/12/11 12:24 AM
 */
public class TinyConfig {
    public boolean enable;
    public LollipopsConfig lollipops = new LollipopsConfig();

    public void enable(boolean enable) {
        this.enable = enable;
    }

    void lollipops(Action<LollipopsConfig> action) {
        action.execute(lollipops);
    }

    void lollipops(Closure c) {
        org.gradle.util.ConfigureUtil.configure(c, lollipops);
    }
}
