package com.noveogroup.clap.gradle

import org.gradle.api.Project
import org.gradle.api.file.FileTree

import java.security.MessageDigest

class Utils {

    static String calculateHash(FileTree fileTree) {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1")

        fileTree.each {
            it.eachByte(1024) { byte[] buffer, int length ->
                messageDigest.update(buffer, 0, length)
            }
        }

        return messageDigest.digest().encodeHex() as String
    }

    static FileTree getFileTree(Project project, def variant) {
        List<File> directories = []

        Closure sum = { set, i -> set + i }
        directories += variant.sourceSets*.assetsDirectories.inject([], sum)
        directories += variant.sourceSets*.resDirectories.inject([], sum)
        directories += variant.sourceSets*.jniDirectories.inject([], sum)
        directories += variant.sourceSets*.renderscriptDirectories.inject([], sum)
        directories += variant.sourceSets*.aidlDirectories.inject([], sum)
        directories += variant.sourceSets*.javaDirectories.inject([], sum)
        directories += variant.sourceSets*.resourcesDirectories.inject([], sum)
        directories += variant.sourceSets*.manifestFile
        directories += variant.variantData.processResourcesTask.getSourceOutputDir()
        directories += variant.variantData.generateBuildConfigTask.getSourceOutputDir()

        FileTree sourcesTree = project.fileTree(dir: project.projectDir, excludes: ['**/*'])
        directories.each {
            sourcesTree += project.fileTree(dir: it)
        }
        return sourcesTree
    }

}
