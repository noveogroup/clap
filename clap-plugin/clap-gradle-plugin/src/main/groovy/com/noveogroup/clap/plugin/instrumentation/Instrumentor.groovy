package com.noveogroup.clap.plugin.instrumentation

import javassist.ClassPool
import javassist.CtClass

/**
 * @author Andrey Sokolov
 */
public interface Instrumentor {

    String getName()

    void instrument(ClassPool classPool, CtClass aClass)

}