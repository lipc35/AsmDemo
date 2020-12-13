package com.tinyws.plugin

import com.android.build.gradle.AppExtension
import com.tinyws.PluginConfig
import com.tinyws.TinyConfig
import com.tinyws.TinyConstant
import org.gradle.api.Plugin
import org.gradle.api.Project

class TinyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create(TinyConstant.CONFIG_NAME, TinyConfig.class)
        PluginConfig.init(project)
        project.gradle.addListener(new TinyDependency(project))
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        // 注册Transform
        appExtension.registerTransform(new LollipopsTransform());
    }
}