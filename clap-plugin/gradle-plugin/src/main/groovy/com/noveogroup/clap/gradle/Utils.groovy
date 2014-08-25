package com.noveogroup.clap.gradle

import com.noveogroup.clap.gradle.config.Options
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.Modifier
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.compile.JavaCompile

import java.security.MessageDigest

class Utils {

    static String generateRandom() {
        new Random().with { (1..8).collect { String.format('%08X', nextInt()) }.join('') } as String
    }

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
        // todo Google can change generator and add time to the files
        // directories += variant.variantData.processResourcesTask.getSourceOutputDir()
        // directories += variant.variantData.generateBuildConfigTask.getSourceOutputDir()

        FileTree sourcesTree = project.fileTree(dir: project.projectDir, excludes: ['**/*'])
        directories.each {
            sourcesTree += project.fileTree(dir: it)
        }
        return sourcesTree
    }

    static FileTree getFileTree(Project project) {
        FileTree sourcesTree = project.fileTree(dir: project.projectDir, excludes: ['**/*'])

        def android = project.extensions.findByName('android')
        android.applicationVariants.each { variant ->
            sourcesTree += getFileTree(project, variant)
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

    static void setBuildConfigFields(ClassPool classPool, File destinationDir, def variant,
                                     String revisionHashValue, String variantHashValue, String randomValue) {
        CtClass buildConfigClass = classPool.getCtClass("${variant.packageName}.BuildConfig")
        CtClass stringClass = classPool.getCtClass("java.lang.String")

        CtField revisionHashField = new CtField(stringClass, BuildConfigHelper.FIELD_CLAP_REVISION_HASH, buildConfigClass)
        revisionHashField.setModifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)
        buildConfigClass.addField(revisionHashField, CtField.Initializer.constant(revisionHashValue))

        CtField variantHashField = new CtField(stringClass, BuildConfigHelper.FIELD_CLAP_VARIANT_HASH, buildConfigClass)
        variantHashField.setModifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)
        buildConfigClass.addField(variantHashField, CtField.Initializer.constant(variantHashValue))

        CtField randomField = new CtField(stringClass, BuildConfigHelper.FIELD_CLAP_RANDOM, buildConfigClass)
        randomField.setModifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)
        buildConfigClass.addField(randomField, CtField.Initializer.constant(randomValue))

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

    static boolean uploadApk(File apkFile, Options options, String revisionHash, String variantHash, String random) {
        println "revisionHash: $revisionHash"
        println "variantHash : $variantHash"
        println "random      : $random"

        RESTClient clapREST = new RESTClient(options.serverUrl)
        def authResponse = clapREST.post(path: 'auth',
                body: [login: options.username, password: options.password],
                requestContentType: ContentType.JSON)
        String token = authResponse.data.text as String

        HTTPBuilder httpBuilder = new HTTPBuilder("${options.serverUrl}")
        def uploadResponse = httpBuilder.request(Method.POST, ContentType.TEXT) { request ->
            uri.path = 'upload/revision'
            requestContentType: 'multipart/form-data'
            headers = ['Accept': 'application/json']
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
            builder.addPart('projectExternalId', new StringBody(options.projectId))
            builder.addPart('revisionHash', new StringBody(variantHash))
            builder.addPart('token', new StringBody(token))
            builder.addPart('mainPackage', new FileBody(apkFile))
            request.entity = builder.build()
        }
        return uploadResponse.text != "true"
    }

}
