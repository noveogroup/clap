package com.noveogroup.clap.gradle.instrument

import com.noveogroup.clap.gradle.Utils
import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class Instrumentation {

    private static Map<String, Module> MODULES = [
            addLogs : new Module(dependencies: []),
            sendLogs: new Module(dependencies: ['com.noveogroup.clap:library:0.1']),
    ]

    static Module getModule(String name) {
        if (!MODULES.containsKey(name)) {
            throw new GradleException("unknown instrumentation module '$name'. modules can be: ${MODULES.keySet()}")
        }
        return MODULES.get(name)
    }

    static List<String> getDependencies(Set<String> names) {
        names.collect { getModule(it).dependencies }.sum()
    }

    static void instrument(Project project, JavaCompile javaCompileTask, ClassPool classPool, Set<String> names) {
        List<String> classNames = Utils.getClassNames(javaCompileTask.destinationDir)
        classNames.each { String className ->
            CtClass aClass = classPool.getCtClass(className)

            project.logger.info "instrument $className"
            names.each { getModule(it).instrumentClass(classPool, aClass) }

            aClass.writeFile(javaCompileTask.destinationDir.absolutePath)
            aClass.detach()
        }
        javaCompileTask.classpath = project.files()
    }

}
