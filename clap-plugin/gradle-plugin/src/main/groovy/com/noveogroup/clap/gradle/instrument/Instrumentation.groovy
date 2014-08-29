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

package com.noveogroup.clap.gradle.instrument

import com.noveogroup.clap.gradle.Utils
import com.noveogroup.clap.gradle.instrument.modules.CrashSpy
import com.noveogroup.clap.gradle.instrument.modules.InfoSpy
import javassist.*
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class Instrumentation {

    private static Map<String, Module> MODULES = [
            'crash-spy': new CrashSpy(moduleClassName: 'com.noveogroup.clap.module.crash_spy.CrashSpyModule',
                    dependencies: ['com.noveogroup.clap:module-crashSpy:0.1']),
            'info-spy' : new InfoSpy(moduleClassName: 'com.noveogroup.clap.module.info_spy.InfoSpyModule',
                    dependencies: ['com.noveogroup.clap:module-infoSpy:0.1']),
    ]

    private static final String MODULE_MANAGER = "com.noveogroup.clap.library.common.ModuleManager"

    static Module getModule(String name) {
        if (!MODULES.containsKey(name)) {
            throw new GradleException("unknown instrumentation module '$name'. modules can be: ${MODULES.keySet()}")
        }
        return MODULES.get(name)
    }

    static List<String> getDependencies(Set<String> names) {
        names.collect { getModule(it).dependencies }.sum()
    }

    private static void instrumentCommon(ClassPool classPool, CtClass aClass, Set<String> names) {
        if (aClass.name == MODULE_MANAGER) return

        CtConstructor initializer = aClass.classInitializer ?: aClass.makeClassInitializer()

        // add static initialization
        initializer.insertBefore("${MODULE_MANAGER}.getInstance().initStatic();")

        // register modules
        names.each {
            initializer.insertBefore("${MODULE_MANAGER}.getInstance().registerModule(\"${it}\", \"${getModule(it).moduleClassName}\");")
        }

        // add context initialization for Application
        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.app.Application")) {
            CtMethod method = InstrumentationUtils.getMethodToInstrument(classPool, aClass, 'onCreate', 'void', [])
            method.insertBefore("${MODULE_MANAGER}.getInstance().initContext(this);")
        }

        // add context initialization for Activity
        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.app.Activity")) {
            CtMethod method = InstrumentationUtils.getMethodToInstrument(classPool, aClass, 'onCreate', 'void', ['android.os.Bundle'])
            method.insertBefore("${MODULE_MANAGER}.getInstance().initContext(this);")
        }

        // add context initialization for Service
        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.app.Service")) {
            CtMethod method = InstrumentationUtils.getMethodToInstrument(classPool, aClass, 'onCreate', 'void', [])
            method.insertBefore("${MODULE_MANAGER}.getInstance().initContext(this);")
        }

        // add context initialization for ContentProvider
        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.content.ContentProvider")) {
            CtMethod method = InstrumentationUtils.getMethodToInstrument(classPool, aClass, 'onCreate', 'boolean', [])
            method.insertBefore("${MODULE_MANAGER}.getInstance().initContext(getContext());")
        }

        // add context initialization for BroadcastReceiver
        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.content.BroadcastReceiver")) {
            CtMethod method = InstrumentationUtils.getMethodToInstrument(classPool, aClass, 'onReceive', 'boolean', ['android.content.Context', 'android.content.Intent'])
            method.insertBefore("${MODULE_MANAGER}.getInstance().initContext(${InstrumentationUtils.getParameterName(method, 1)});")
        }
    }

    static void instrument(Project project, JavaCompile javaCompileTask, ClassPool classPool, Set<String> names) {
        Utils.getClassNames(javaCompileTask.destinationDir)
                .each { String className ->
            CtClass aClass = classPool.getCtClass(className)

            project.logger.info "instrument $className"
            instrumentCommon(classPool, aClass, names)
            names.each { getModule(it).instrumentClass(classPool, aClass) }

            aClass.writeFile(javaCompileTask.destinationDir.absolutePath)
            aClass.detach()
        }

        javaCompileTask.classpath
                .inject([]) { classNames, file -> classNames + Utils.getClassNames(file) }
                .each { String className ->
            CtClass aClass = classPool.getCtClass(className)

            try {
                project.logger.info "instrument $className"
                instrumentCommon(classPool, aClass, names)
                names.each { getModule(it).instrumentClass(classPool, aClass) }
            } catch (NotFoundException ignored) {
                // classpath can contain classes with missed dependencies
            }

            aClass.writeFile(javaCompileTask.destinationDir.absolutePath)
            aClass.detach()
        }

        javaCompileTask.classpath = project.files()
    }

}
