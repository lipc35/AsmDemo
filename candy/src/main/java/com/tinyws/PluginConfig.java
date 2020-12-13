package com.tinyws;

import org.gradle.api.Project;

/**
 * @author : tiny
 * @version :
 * @description : PluginConfig
 * @date : 2020/12/11 12:36 AM
 */
public class PluginConfig {
    private static Project mProject;

    public static void init(Project project) {
        mProject = project;
    }

    public static TinyConfig getConfig() {
        return mProject.getExtensions().getByType(TinyConfig.class);
    }
}
