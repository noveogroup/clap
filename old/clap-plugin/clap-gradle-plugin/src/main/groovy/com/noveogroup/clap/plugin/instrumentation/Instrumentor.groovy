package com.noveogroup.clap.plugin.instrumentation

import javassist.ClassPool
import javassist.CtClass

/**
 */
public interface Instrumentor {

    String getName()

    void instrument(ClassPool classPool, CtClass aClass)

}