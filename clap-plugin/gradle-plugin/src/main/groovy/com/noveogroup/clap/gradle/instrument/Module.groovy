package com.noveogroup.clap.gradle.instrument

import javassist.ClassPool
import javassist.CtClass

public class Module {

    List<String> dependencies

    void instrumentClass(ClassPool classPool, CtClass aClass) {}

}
