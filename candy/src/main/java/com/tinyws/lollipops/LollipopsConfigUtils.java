package com.tinyws.lollipops;

import com.tinyws.PluginConfig;

import java.util.List;

/**
 * @author : tiny
 * @version :
 * @description : LollipopsConfigUtils
 * @date : 2020/12/14 12:16 AM
 */
public class LollipopsConfigUtils {
    public static boolean isOpen() {
        if (PluginConfig.getConfig().enable && PluginConfig.getConfig().lollipops != null
                && PluginConfig.getConfig().lollipops.enable) {
            return true;
        }
        return false;
    }

    public static boolean checkCanWave(String name) {
        List<String> lists = PluginConfig.getConfig().lollipops.includes;
        if (lists != null) {
            for (String include : lists) {
                if (name.contains(include)) {
                    return true;
                }
            }
        }
        return false;
    }
}
