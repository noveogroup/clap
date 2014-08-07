package com.noveogroup.clap.plugin.tasks

import com.noveogroup.clap.plugin.ClapPlugin
import com.noveogroup.clap.plugin.config.Clap
import com.noveogroup.clap.utils.Constants
import com.noveogroup.clap.utils.HashCalculator
import org.apache.commons.lang.StringUtils
import org.gradle.api.DefaultTask

/**
 */
abstract class AbstractClapTask  extends DefaultTask {

    protected String getProjectExternalId() {
        String projectId = (String) ClapPlugin.PLUGIN_CONTEXT.get(Constants.PROJECT_ID);
        if (StringUtils.isEmpty(projectId)) {
            def clap = project.extensions.getByType(Clap)
            def projectExternalId = clap.clapProjectId
            ClapPlugin.PLUGIN_CONTEXT.put(Constants.PROJECT_ID,projectExternalId)
            return projectExternalId;
        } else {
            return projectId;
        }
    }


    protected String getRevisionId() {
        String revisionId = (String) ClapPlugin.PLUGIN_CONTEXT.get(Constants.REVISION_ID);
        if (StringUtils.isEmpty(revisionId)) {
            def clap = project.extensions.getByType(Clap)
            def config = clap.hashCalculatorConfig
            HashCalculator hashCalculator = new HashCalculator(config.baseDir, config.folders, config.excludes);
            def calculateHash = hashCalculator.calculateHash(config.checksumAlgorithm)
            ClapPlugin.PLUGIN_CONTEXT.put(Constants.REVISION_ID,calculateHash);
            return calculateHash;
        } else {
            return revisionId;
        }
    }
}
