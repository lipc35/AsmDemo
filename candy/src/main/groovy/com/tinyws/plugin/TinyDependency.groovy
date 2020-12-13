package com.tinyws.plugin

import com.tinyws.PluginConfig
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.ResolvableDependencies


class TinyDependency implements DependencyResolutionListener {
    Project mProject;

    public TinyDependency(Project project) {
        mProject = project;
    }

    @Override
    void beforeResolve(ResolvableDependencies resolvableDependencies) {
        if (PluginConfig.config.enable && PluginConfig.config.lollipops != null && PluginConfig.config.lollipops.enable) {
            mProject.dependencies.add("api", "com.tinyws.helper:lollipops:1.0.0")
        }
        mProject.gradle.removeListener(this)
    }

    @Override
    void afterResolve(ResolvableDependencies resolvableDependencies) {

    }
}