package com.noveogroup.clap.gradle.instrument.modules

import com.noveogroup.clap.gradle.instrument.InstrumentationUtils
import com.noveogroup.clap.gradle.instrument.Module
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtMethod

class SendLogs extends Module {

    @Override
    void instrumentClass(final ClassPool classPool, final CtClass aClass) {
        if (InstrumentationUtils.findSuperclass(aClass, classPool.getCtClass("android.app.Activity"))) {
            modifyExceptionHandler(classPool, aClass)
            adjustLogger(classPool, aClass)

            CtClass voidClass = classPool.getCtClass("void")
            CtClass bundleClass = classPool.getCtClass("android.os.Bundle")
            CtMethod method = InstrumentationUtils.getMethodToInstrument(classPool, aClass, true, "onCreate", voidClass, [bundleClass] as CtClass[])
            method.insertBefore("com.noveogroup.clap.library.logs.ActivityTraceLogger.getInstance().setLastContext(this);")
        }

        if (InstrumentationUtils.findSuperclass(aClass, classPool.getCtClass("android.app.Service"))) {
            modifyExceptionHandler(classPool, aClass)
        }
    }

    private static void adjustLogger(final ClassPool classPool, final CtClass aClass) {
        CtConstructor initializer = aClass.classInitializer
        if (!initializer) {
            initializer = aClass.makeClassInitializer()
        }
        initializer.insertBefore("com.noveogroup.clap.library.logs.LoggerAdjuster.adjustLogger();")
    }

    private static void modifyExceptionHandler(final ClassPool classPool, final CtClass aClass) {
        CtConstructor initializer = aClass.classInitializer
        if (!initializer) {
            initializer = aClass.makeClassInitializer()
        }
        initializer.insertBefore("com.noveogroup.clap.library.logs.ExceptionHandler.replaceHandler();")
    }

}
