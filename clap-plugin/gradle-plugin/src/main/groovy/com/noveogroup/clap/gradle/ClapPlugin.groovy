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
import com.noveogroup.clap.gradle.config.Options
import com.noveogroup.clap.gradle.instrument.Instrumentation
import javassist.ClassPool
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
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
    }

    Map<String, Task> hashTasks = [:]
    Map<String, String> hashValues = [:]
    Map<String, String> randomValues = [:]

    private Task createCalculateRevisionHashTask(Project project) {
        hashTasks[null] = project.task("calculateHash",
                group: "CLAP",
                description: "Calculates hash of all sources of project.")

        hashTasks[null] << {
            // calculate and update hash code of sources
            FileTree fileTree = Utils.getFileTree(project)
            String hash = Utils.calculateHash(fileTree) as String
            hashValues[null] = hash
        }

        return hashTasks[null]
    }

    private Task createCalculateVariantHashTask(Project project, def variant) {
        String name = variant.name as String
        Task hashTask = project.task("calculate${name.capitalize()}Hash",
                group: "CLAP",
                description: "Calculates hash of sources of ${name.capitalize()} build.")
        hashTasks[name] = hashTask

        hashTask << {
            // calculate and update hash code of sources
            FileTree fileTree = Utils.getFileTree(project, variant)
            String hash = Utils.calculateHash(fileTree) as String
            hashValues[name] = hash
        }

        return hashTask
    }

    private Task createUploadTask(Project project, ClapOptions clap, def variant) {
        def zipAlignTask = variant.variantData.zipAlignTask
        if (!zipAlignTask) return null

        String name = variant.name as String

        Task uploadTask = project.task("upload${name.capitalize()}",
                group: "CLAP",
                description: "Uploads ${name.capitalize()} APK on CLAP server.",
                dependsOn: [zipAlignTask, hashTasks[null], hashTasks[name]])

        uploadTask << {
            Options options = clap.resolve(variant.buildType.name as String)
            File apkFile = variant.variantData.outputFile
            Utils.uploadApk(apkFile, options, hashValues[null], hashValues[name], randomValues[name])
        }

        return uploadTask
    }

    @Override
    void apply(Project project) {
        project.extensions.create('clap', ClapOptions, project)

        // todo remove when httpmime will be excluded from dependencies
        project.android {
            packagingOptions {
                exclude 'META-INF/ASL2.0'
                exclude 'META-INF/LICENSE'
                exclude 'META-INF/LICENSE.txt'
                exclude 'META-INF/NOTICE'
                exclude 'META-INF/NOTICE.txt'
            }
        }

        project.gradle.afterProject {
            def android = project.extensions.findByName('android')
            ClapOptions clap = project.extensions.findByType(ClapOptions)

            // add default build config fields
            addBuildConfigFields(android.defaultConfig, clap, true)

            clap.custom.names.each { String customName ->
                Options customOptions = clap.resolve(customName)

                // check names of custom clap options
                List<String> allowedNames = android.buildTypes*.name
                if (!allowedNames.contains(customName)) {
                    throw new GradleException("clap cannot configure '${customName}'. only build types allowed: $allowedNames")
                }

                // add build config fields for build type
                addBuildConfigFields(android.buildTypes[customName], customOptions, false)

                // add dependencies
                // todo add clap-api module (for field constants, clap annotations and configurations)
                // project.dependencies.add("${customName}Compile", 'com.noveogroup.clap:clap-api:0.1')
                Instrumentation.getDependencies(customOptions.instrument).each {
                    project.dependencies.add("${customName}Compile", it)
                }

//                todo Android builder do not support standard Artifact Query API
//                println "----- ===== ***** ===== -----"
//                project.dependencies.components.eachComponent { ComponentMetadataDetails details ->
//                    println "${details.id.group} : ${details.id.name} : ${details.id.version}"
//                    def result = project.dependencies.createArtifactResolutionQuery()
//                            .forComponents(new DefaultModuleComponentIdentifier(details.id.group, details.id.name, details.id.version))
//                            .withArtifacts(JvmLibrary, SourcesArtifact, JavadocArtifact)
//                            .execute()
//
//                    for (component in result.resolvedComponents) {
//                        println component
//                        component.getArtifacts(SourcesArtifact).each {
//                            println "Source artifact for ${component.id}: ${it.file}"
//                        }
//                    }
//                }
//                println "----- ===== ***** ===== -----"
//                project.repositories.each { ArtifactRepository artifactRepository ->
//                    if (artifactRepository instanceof MavenArtifactRepository) {
//                        MavenArtifactRepository repository = artifactRepository
//                        println repository
//                        println repository.url
//                    }
//                }
//                println "----- ===== ***** ===== -----"
//                variant.variantData.variantDependency.libraries.each { println it }
//                variant.variantData.variantDependency.jars.each { println it }
//                variant.variantData.variantDependency.localJars.each { println it }
//                println "----- ===== ***** ===== -----"
//                println project.components
//                println "----- ===== ***** ===== -----"
//                def componentIds = project.configurations.compile.incoming.resolutionResult.allDependencies.collect {
//                    it.selected.id
//                }
//                println componentIds
//                def result = project.dependencies.createArtifactResolutionQuery()
//                        .forComponents(componentIds)
//                        .withArtifacts(JvmLibrary, SourcesArtifact, JavadocArtifact)
//                        .execute()
//                println result
//                println "----- ===== ***** ===== -----"
            }
        }

        project.afterEvaluate {
            def android = project.extensions.findByName('android')
            ClapOptions clap = project.extensions.findByType(ClapOptions)

            // create calculate revision hash task
            createCalculateRevisionHashTask(project)

            android.applicationVariants.each { variant ->
                String name = variant.name as String
                Options customOptions = clap.resolve(variant.buildType.name as String)
                JavaCompile javaCompileTask = variant.variantData.javaCompileTask

                // generate random values
                randomValues[name] = Utils.generateRandom()

                // create calculate hash task
                Task hashTask = createCalculateVariantHashTask(project, variant)
                hashTask.dependsOn javaCompileTask.getDependsOn().clone()
                javaCompileTask.dependsOn hashTasks[null]
                javaCompileTask.dependsOn hashTask

                // instrument classes after compilation
                javaCompileTask << {
                    logger.lifecycle ":$javaCompileTask.project.name:instrument${name.capitalize()}"

                    // prepare class pool
                    ClassPool classPool = Utils.prepareClassPool(javaCompileTask)

                    // calculate and update hash code of sources
                    Utils.setBuildConfigFields(classPool, javaCompileTask.destinationDir, variant,
                            hashValues[null], hashValues[name], randomValues[name])

                    // instrument classes
                    Instrumentation.instrument(project, javaCompileTask, classPool, customOptions.instrument)
                }

                // delete instrumented classes from DEX task
                // todo so preDEX task does useless work now
                variant.variantData.dexTask.libraries = []

                // create upload task
                createUploadTask(project, clap, variant)
            }
        }
    }

}
