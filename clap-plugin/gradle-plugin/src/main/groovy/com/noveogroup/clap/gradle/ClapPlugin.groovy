/*
 * Copyright (c) 2014 Noveo Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.noveogroup.clap.gradle

import com.noveogroup.clap.api.BuildConfigHelper
import com.noveogroup.clap.gradle.config.ClapOptions
import com.noveogroup.clap.gradle.config.CustomOptions
import com.noveogroup.clap.gradle.config.Options
import com.noveogroup.clap.gradle.instrument.Instrumentation
import javassist.ClassPool
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.compile.JavaCompile

class ClapPlugin implements Plugin<Project> {

    private static void addBuildConfigFields(def config, Options options, boolean isDefault) {
        Closure set = { String field, String value ->
            if (value) {
                config.buildConfigField("String", field, "\"$value\"")
            } else {
                if (isDefault) {
                    if (field in [BuildConfigHelper.FIELD_CLAP_PROJECT_ID, BuildConfigHelper.FIELD_CLAP_SERVER_URL]) {
                        throw new GradleException("default value of field '$field' should be set")
                    }
                    config.buildConfigField("String", field, "null")
                }
            }
        }
        set BuildConfigHelper.FIELD_CLAP_PROJECT_ID, options.projectId
        set BuildConfigHelper.FIELD_CLAP_SERVER_URL, options.serverUrl
        set BuildConfigHelper.FIELD_CLAP_USERNAME, options.username
        set BuildConfigHelper.FIELD_CLAP_PASSWORD, options.password
    }

    @Override
    void apply(Project project) {
        project.extensions.create('clap', ClapOptions, project)

        project.gradle.afterProject {
            def android = project.extensions.findByName('android')
            ClapOptions clap = project.extensions.findByType(ClapOptions)

            // add default build config fields
            addBuildConfigFields(android.defaultConfig, clap, true)

            clap.custom.each { CustomOptions customOptions ->
                // check names of custom clap options
                List<String> allowedNames = android.buildTypes*.name
                if (!allowedNames.contains(customOptions.name)) {
                    throw new GradleException("clap cannot configure '${it.name}'. only build types allowed: $allowedNames")
                }

                // add build config fields for build type
                addBuildConfigFields(android.buildTypes[customOptions.name], customOptions, false)

                // add dependencies
                project.dependencies.add("${customOptions.name}Compile", 'com.noveogroup.clap:clap-api:0.1')
                Set<String> instrument = clap.instrument + customOptions.instrument
                Instrumentation.getDependencies(instrument).each {
                    project.dependencies.add("${customOptions.name}Compile", it)
                }
            }
        }

        project.afterEvaluate {
            def android = project.extensions.findByName('android')
            ClapOptions clap = project.extensions.findByType(ClapOptions)

            android.applicationVariants.each { variant ->
                Options options = clap.custom[variant.buildType.name as String]
                JavaCompile javaCompileTask = variant.variantData.javaCompileTask

                javaCompileTask.doLast {
                    logger.lifecycle ":$javaCompileTask.project.name:instrument${variant.name.capitalize()}"

                    // prepare class pool
                    ClassPool classPool = Utils.prepareClassPool(javaCompileTask)

                    // calculate and update hash code of sources
                    FileTree fileTree = Utils.getFileTree(project, variant)
                    String hash = Utils.calculateHash(fileTree) as String
                    Utils.setHashField(classPool, javaCompileTask.destinationDir, variant, hash)

                    // instrument classes
                    Set<String> instrument = clap.instrument + options.instrument
                    Instrumentation.instrument(project, javaCompileTask, classPool, instrument)
                }
            }
        }
    }

}
