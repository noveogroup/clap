package com.noveogroup.clap.plugin.instrumentation.impl

import com.noveogroup.clap.plugin.instrumentation.Instrumentor
import javassist.ClassPool
import javassist.CtClass

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

        //TODO
    }
}
