package com.noveogroup.clap.gradle

import com.noveogroup.clap.api.BuildConfigHelper
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.Modifier
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.compile.JavaCompile

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

    static ClassPool prepareClassPool(JavaCompile javaCompileTask) {
        ClassPool classPool = new ClassPool()
        classPool.appendClassPath(javaCompileTask.options.bootClasspath)
        javaCompileTask.classpath.each { classPool.appendClassPath(it.absolutePath) }
        classPool.appendClassPath(javaCompileTask.destinationDir.absolutePath)
        return classPool
    }

    static void setHashField(ClassPool classPool, File destinationDir, def variant, String hashValue) {
        CtClass buildConfigClass = classPool.getCtClass("${variant.packageName}.BuildConfig")

        CtClass stringClass = classPool.getCtClass("java.lang.String")
        CtField hashField = new CtField(stringClass, BuildConfigHelper.FIELD_CLAP_SOURCE_HASH, buildConfigClass)
        hashField.setModifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)
        buildConfigClass.addField(hashField, CtField.Initializer.constant(hashValue))

        buildConfigClass.writeFile(destinationDir.absolutePath)
        buildConfigClass.detach()
    }

    static List<String> getClassNames(File directory) {
        List<String> list = []
        directory.eachFileRecurse {
            String path = it.absolutePath as String
            def matcher = (path =~ /(.*)\.class/)
            if (it.isFile() && matcher.matches()) {
                String className = matcher.group(1)
                        .substring(directory.absolutePath.length() + 1)
                        .replaceAll('/', '.')
                        .replaceAll('\\\\', '.') as String
                list << className
            }
        }
        return list
    }

}
