package com.noveogroup.clap.plugin

import com.noveogroup.clap.plugin.tasks.UploadTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author Andrey Sokolov
 */
class ClapPlugin implements Plugin<Project>{

    @Override
    void apply(final Project project) {
        project.task('upload',type:UploadTask)
    }
}
