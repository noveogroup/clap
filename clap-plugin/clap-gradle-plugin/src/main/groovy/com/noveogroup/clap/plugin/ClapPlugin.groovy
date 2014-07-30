package com.noveogroup.clap.plugin

import com.noveogroup.clap.plugin.config.Clap
import com.noveogroup.clap.plugin.tasks.ClapUploadTask
import com.noveogroup.clap.plugin.tasks.GenerateVersionTask
import com.noveogroup.clap.plugin.tasks.InstrumentationTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.compile.JavaCompile

/**
 * @author Andrey Sokolov
 */
class ClapPlugin implements Plugin<Project> {


    public static final String CLAP_AAR_VERSION = '1.0-SNAPSHOT'

    public static final String GENERATED_SOURCES_DIR_KEY = "clapGeneratedSourceDir";
    //TODO check if gradle has plugin context like maven
    public static final Map<String,Object> PLUGIN_CONTEXT = new HashMap<>();

    @Override
    void apply(final Project project) {
        String generatedSourcePath = project.buildDir.absolutePath + '/src-generated/java'
        PLUGIN_CONTEXT.put(GENERATED_SOURCES_DIR_KEY,generatedSourcePath)

        project.task('clapGenerateVersion', type: GenerateVersionTask)
        project.extensions.create("clap", Clap)
        project.dependencies {
            compile 'com.noveogroup.clap:clap-client:'+CLAP_AAR_VERSION
            compile 'com.noveogroup.clap:clap-library-logger:'+CLAP_AAR_VERSION
        }

        //TODO check
        // without following gradle/android throws error connected to jackson-mapper
        project.android {
            packagingOptions {
                exclude 'META-INF/ASL2.0'
                exclude 'META-INF/LICENSE'
                exclude 'META-INF/NOTICE'
            }
            //adding generated version-provider class to apk
            sourceSets {
                main {
                    java {
                        srcDir generatedSourcePath
                    }
                }
            }
        }

        project.afterEvaluate {
            def clap = project.extensions.getByType(Clap)
            project.logger.info("clap = " + clap)
            project.logger.info("enableInstrumenting = " + clap.enableInstrumenting)
            if (clap.enableInstrumenting) {
                project.tasks.each { Task task ->
                    if (task instanceof JavaCompile) {
                        task.dependsOn << 'clapGenerateVersion'
                        JavaCompile javaCompile = task
                        javaCompile.doLast {
                            new InstrumentationTask(clap.instrumenting).instrument(javaCompile, logger)
                        }
                    }
                }
            }
        }

        //project.task('clapUpload', type: ClapUploadTask)
        project.android.applicationVariants.all { variant ->
            def task = project.tasks.create("clapUpload${variant.name}Apk", ClapUploadTask)
            task.artifactFile = variant.outputFile
            task.variantName = variant.name
            task.dependsOn variant.assemble
        }
    }
}
