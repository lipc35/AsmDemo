package com.tinyws.lollipops;

import java.util.Arrays;
import java.util.List;

/**
 * @author : tiny
 * @version :
 * @description : LollipopsConfig
 * @date : 2020/12/11 12:31 AM
 */
public class LollipopsConfig {
    public boolean enable;
    public List<String> includes;

    public void enable(boolean enable) {
        this.enable = enable;
    }

    public void includes(String... params) {
        this.includes = Arrays.asList(params);
    }
}
