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

import com.noveogroup.clap.gradle.config.ClapOptions
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class ClapPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('clap', ClapOptions, project)

        project.gradle.afterProject {
            ClapOptions clap = project.extensions.findByType(ClapOptions)

            // modify dependencies
            clap.custom.each {
                project.dependencies.add("${it.name}Compile", 'com.noveogroup.clap:library:0.1')
            }
        }

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
