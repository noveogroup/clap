package com.noveogroup.clap.plugin

import com.noveogroup.clap.plugin.config.Clap
import com.noveogroup.clap.plugin.tasks.ClapUploadTask
import com.noveogroup.clap.plugin.tasks.InstrumentationTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.compile.JavaCompile

/**
 * @author Andrey Sokolov
 */
class ClapPlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        project.task('clapUpload', type: ClapUploadTask)
        project.extensions.create("clap", Clap)
        project.afterEvaluate {
            def clap = project.extensions.getByType(Clap)
            logger.info("enableInstrumenting = " + clap.enableInstrumenting)
            if (clap.enableInstrumenting) {
                project.tasks.each { Task task ->
                    if (task instanceof JavaCompile) {
                        JavaCompile javaCompile = task
                        javaCompile.doLast {
                            new InstrumentationTask(clap.instrumenting).instrument(javaCompile, logger)
                        }
                    }
                }
            }
        }
    }
}
