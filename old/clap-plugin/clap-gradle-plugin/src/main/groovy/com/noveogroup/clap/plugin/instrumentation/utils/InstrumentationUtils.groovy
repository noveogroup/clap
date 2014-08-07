package com.noveogroup.clap.plugin.instrumentation.utils

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.CtNewMethod
import javassist.Modifier
import javassist.NotFoundException

import java.util.zip.ZipFile

/**
 */
class InstrumentationUtils {
    static void eachClass(File file, Closure closure) {
        if (file.isDirectory()) {
            // it is a classpath directory
            file.eachFileRecurse {
                String path = it.absolutePath
                def matcher = (path =~ /(.*)\.class/)
                if (it.isFile() && matcher.matches()) {
                    String className = matcher.group(1)
                            .substring(file.absolutePath.length() + 1)
                            .replaceAll('/', '.')
                            .replaceAll('\\\\', '.')
                    closure className
                }
            }
        } else {
            // it is a JAR file
            new ZipFile(file).entries().each {
                def matcher = (it.name =~ /(.*)\.class/)
                if (!it.isDirectory() && matcher.matches()) {
                    String className = matcher.group(1).replaceAll('/', '.')
                    closure className
                }
            }
        }
    }

    static boolean findSuperclass(CtClass aClass, CtClass superClass) {
        while (!aClass.equals(superClass)) {
            if (aClass.superclass == null) {
                return false
            } else {
                aClass = aClass.superclass
            }
        }
        return true
    }

    static CtMethod findMethod(CtClass aClass, String methodName, CtClass[] parameters) {
        while(aClass != null) {
            try {
                return aClass.getDeclaredMethod(methodName, parameters)
            } catch (NotFoundException ignored) {
            }
            aClass = aClass.superclass
        }
        return null
    }

    static CtMethod getMethodToInstrument(ClassPool classPool, CtClass aClass, boolean override, String methodName, CtClass returnType, CtClass[] parameters){
        CtMethod method

        try {
            method = aClass.getDeclaredMethod(methodName, parameters)
        } catch (NotFoundException ignored) {
            method = InstrumentationUtils.findMethod(aClass, methodName, parameters)
            if (method != null && (method.modifiers.and(Modifier.FINAL) != 0)) return null

            StringBuilder builder = new StringBuilder()

            builder.append("public $returnType.name $methodName(")
            parameters.size().times { int i ->
                if (i > 0) builder.append(",")
                builder.append("${parameters[i].name} arg$i")
            }
            builder.append(") { ")

            if (override) {
                if (returnType.name != "void") {
                    builder.append("return ")
                }
                builder.append("super.$methodName(")
                parameters.size().times { int i ->
                    if (i > 0) builder.append(",")
                    builder.append("arg$i")
                }
                builder.append(");")
            }
            builder.append(" }")

            method = CtNewMethod.make(builder.toString(), aClass)
            aClass.addMethod(method)
        }
        return method
    }
}
