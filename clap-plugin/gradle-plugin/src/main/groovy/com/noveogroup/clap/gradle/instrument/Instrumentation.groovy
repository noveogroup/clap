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
import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class Instrumentation {

    private static Map<String, Module> MODULES = [
            'crash-spy': new CrashSpy(dependencies: ['com.noveogroup.clap:module-crashSpy:0.1']),
            'info-spy' : new InfoSpy(dependencies: ['com.noveogroup.clap:module-infoSpy:0.1']),
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
