package com.noveogroup.clap.plugin.instrumentation.utils

import javassist.CtClass
import javassist.CtMethod
import javassist.NotFoundException

import java.util.zip.ZipFile

/**
 * @author Andrey Sokolov
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
}
