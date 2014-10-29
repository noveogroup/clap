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

import javassist.*
import javassist.bytecode.LocalVariableAttribute
import javassist.bytecode.MethodInfo

class InstrumentationUtils {

    static CtClass getClass(ClassPool classPool, String className) {
        try {
            return classPool.getCtClass(className)
        } catch (NotFoundException ignored) {
            return null
        }
    }

    static CtClass getSuperclass(CtClass aClass) {
        try {
            return aClass.superclass
        } catch (NotFoundException ignored) {
            return null
        }
    }

    static boolean findSuperclass(ClassPool classPool, CtClass aClass, String className) {
        return findSuperclass(aClass, getClass(classPool, className))
    }

    static boolean findSuperclass(CtClass aClass, CtClass superClass) {
        while (!aClass.equals(superClass)) {
            if (getSuperclass(aClass) == null) {
                return false
            } else {
                aClass = getSuperclass(aClass)
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
            aClass = getSuperclass(aClass)
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

    static CtMethod getMethodToInstrument(ClassPool classPool, CtClass aClass, String methodName, String returnType, List<String> parameters) {
        CtClass returnClass = classPool.getCtClass(returnType)
        List<CtClass> parameterClasses = parameters.collect { classPool.getCtClass(it) }
        return getMethodToInstrument(classPool, aClass, true, methodName, returnClass, parameterClasses as CtClass[])
    }

    static String getParameterName(CtMethod method, int index) {
        MethodInfo methodInfo = method.getMethodInfo()
        LocalVariableAttribute table = methodInfo.getCodeAttribute().getAttribute(LocalVariableAttribute.tag)
        int frameWithNameAtConstantPool = table.nameIndex(index);
        return methodInfo.getConstPool().getUtf8Info(frameWithNameAtConstantPool) as String
    }

}
