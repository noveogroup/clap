package com.noveogroup.clap.plugin.instrumentation.impl

import com.noveogroup.clap.plugin.instrumentation.Instrumentor
import com.noveogroup.clap.plugin.instrumentation.utils.InstrumentationUtils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtMethod

/**
 * @author Andrey Sokolov
 */
class SendLogsToClap implements Instrumentor{
    @Override
    String getName() {
        return "sendLogs"
    }

    @Override
    void instrument(final ClassPool classPool, final CtClass aClass) {
        if (InstrumentationUtils.findSuperclass(aClass, classPool.getCtClass("android.app.Activity"))) {
            modifyExceptionHandler(classPool, aClass)

            CtClass voidClass = classPool.getCtClass("void")
            CtClass bundleClass = classPool.getCtClass("android.os.Bundle")
            CtMethod method = InstrumentationUtils.getMethodToInstrument(classPool,aClass,true,"onCreate",voidClass, [bundleClass] as CtClass[])
            method.insertBefore("com.noveogroup.clap.ActivityTraceLogger.getInstance().setLastContext(this);")
        }

        if (InstrumentationUtils.findSuperclass(aClass, classPool.getCtClass("android.app.Service"))) {
            modifyExceptionHandler(classPool, aClass)
        }
    }

    private static void modifyExceptionHandler(final ClassPool classPool, final CtClass aClass){
        CtConstructor initializer = aClass.classInitializer
        if (!initializer) {
            initializer = aClass.makeClassInitializer()
        }
        initializer.insertBefore("com.noveogroup.clap.ExceptionHandler.replaceHandler();")
    }
}
