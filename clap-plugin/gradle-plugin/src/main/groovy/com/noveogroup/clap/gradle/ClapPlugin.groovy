package com.noveogroup.clap.gradle

import com.noveogroup.clap.gradle.config.ClapOptions
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class ClapPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('clap', ClapOptions, project)

        project.afterEvaluate {
            ClapOptions clap = project.extensions.findByType(ClapOptions)

            // load android configuration
            def android = project.extensions.findByName('android')

            // check each custom option corresponds to some type, flavor or variant
            List<String> allowedNames = android.applicationVariants*.name +
                    android.buildTypes*.name + android.productFlavors*.name
            clap.custom.each {
                if (!allowedNames.contains(it.name)) {
                    throw new GradleException("clap cannot configure '${it.name}'. allowed configurations are: $allowedNames")
                }
            }
        }
    }

}
