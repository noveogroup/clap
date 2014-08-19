package com.noveogroup.clap.gradle.instrument

import javassist.*

class InstrumentationUtils {

    static CtClass getClass(ClassPool classPool, String className) {
        try {
            return classPool.getCtClass(className)
        } catch (NotFoundException ignored) {
            return null
        }
    }

    static boolean findSuperclass(ClassPool classPool, CtClass aClass, String className) {
        return findSuperclass(aClass, getClass(classPool, className))
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
        while (aClass != null) {
            try {
                return aClass.getDeclaredMethod(methodName, parameters)
            } catch (NotFoundException ignored) {
            }
            aClass = aClass.superclass
        }
        return null
    }

    static CtMethod getMethodToInstrument(ClassPool classPool, CtClass aClass, boolean override, String methodName, CtClass returnType, CtClass[] parameters) {
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
