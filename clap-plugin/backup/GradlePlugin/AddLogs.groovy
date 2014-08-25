package com.noveogroup.clap.gradle.instrument.modules

import com.noveogroup.clap.gradle.instrument.InstrumentationUtils
import com.noveogroup.clap.gradle.instrument.Module
import javassist.*
import javassist.bytecode.LocalVariableAttribute
import javassist.bytecode.MethodInfo

class AddLogs extends Module {

    public static final String LOGGER_FIELD_NAME = "COM_NOVEOGROUP_ANDROID_LOGGER"

    @Override
    void instrumentClass(final ClassPool classPool, final CtClass aClass) {
        CtClass loggerClass = classPool.getCtClass("org.slf4j.Logger")
        CtField loggerField = new CtField(loggerClass, LOGGER_FIELD_NAME, aClass)
        loggerField.setModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL)
        aClass.addField(loggerField)

        CtConstructor initializer = aClass.classInitializer
        if (!initializer) {
            initializer = aClass.makeClassInitializer()
        }
        initializer.insertBefore("" +
                "${loggerField.name} = org.slf4j.LoggerFactory.getLogger(\"LOGGER\");" +
                "${loggerField.name}.info(\"load class ${aClass.name}\");")

        if (aClass.interfaces.contains(classPool.getCtClass("android.view.View\$OnClickListener"))) {
            instrumentOnClickListener(classPool, aClass)
        }

        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.app.Activity")) {
            instrumentActivity(classPool, aClass)
        }

        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.app.Fragment")) {
            instrumentFragment(classPool, aClass)
        }

        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.support.v4.app.Fragment")) {
            instrumentFragment(classPool, aClass)
        }

        if (InstrumentationUtils.findSuperclass(classPool, aClass, "android.app.Service")) {
            instrumentService(classPool, aClass)
        }
    }

    static void addLogMessage(ClassPool classPool, CtClass aClass, boolean override, String methodName, CtClass returnType, CtClass[] parameters) {
        addLogMessage(classPool, aClass, override, methodName, returnType, parameters, "\"\"")
    }

    static void addLogMessage(ClassPool classPool, CtClass aClass, boolean override, String methodName, CtClass returnType, CtClass[] parameters, String infoCode) {
        CtMethod method

        try {
            method = aClass.getDeclaredMethod(methodName, parameters)
        } catch (NotFoundException ignored) {
            method = InstrumentationUtils.findMethod(aClass, methodName, parameters)
            if (method != null && (method.modifiers.and(Modifier.FINAL) != 0)) return

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

        method.insertBefore(LOGGER_FIELD_NAME + ".info(\"call $aClass.name[\" + hashCode() + \"]::$methodName \" + $infoCode);")
    }

    static void instrumentOnClickListener(ClassPool classPool, CtClass listenerClass) {
        CtClass voidClass = classPool.getCtClass("void")
        CtClass viewClass = classPool.getCtClass("android.view.View")

        CtMethod method

        try {
            method = listenerClass.getDeclaredMethod("onClick", [viewClass] as CtClass[])
        } catch (NotFoundException ignored) {
            return
        }

        MethodInfo methodInfo = method.getMethodInfo();
        LocalVariableAttribute table = methodInfo.getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
        int frameWithNameAtConstantPool = table.nameIndex(1);
        String variableName = methodInfo.getConstPool().getUtf8Info(frameWithNameAtConstantPool) as String

        method.insertBefore(LOGGER_FIELD_NAME + ".info(\"call ${listenerClass.name}[\" + hashCode() + \"]::onClick \" + ${variableName}.getContext().getResources().getResourceName(${variableName}.getId()));")
    }

    static void instrumentActivity(ClassPool classPool, CtClass activityClass) {
        CtClass voidClass = classPool.getCtClass("void")
        CtClass bundleClass = classPool.getCtClass("android.os.Bundle")
        addLogMessage(classPool, activityClass, true, "onCreate", voidClass, [bundleClass] as CtClass[])
        addLogMessage(classPool, activityClass, true, "onStart", voidClass, [] as CtClass[])
        addLogMessage(classPool, activityClass, true, "onResume", voidClass, [] as CtClass[])
        addLogMessage(classPool, activityClass, true, "onPause", voidClass, [] as CtClass[])
        addLogMessage(classPool, activityClass, true, "onStop", voidClass, [] as CtClass[])
        addLogMessage(classPool, activityClass, true, "onDestroy", voidClass, [] as CtClass[])
    }

    static void instrumentFragment(ClassPool classPool, CtClass fragmentClass) {
        CtClass voidClass = classPool.getCtClass("void")
        CtClass activityClass = classPool.getCtClass("android.app.Activity")
        CtClass bundleClass = classPool.getCtClass("android.os.Bundle")
        CtClass layoutInflaterClass = classPool.getCtClass("android.view.LayoutInflater")
        CtClass viewClass = classPool.getCtClass("android.view.View")
        CtClass viewGroupClass = classPool.getCtClass("android.view.ViewGroup")
        addLogMessage(classPool, fragmentClass, true, "onAttach", voidClass, [activityClass] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onCreate", voidClass, [bundleClass] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onCreateView", viewClass, [layoutInflaterClass, viewGroupClass, bundleClass] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onStart", voidClass, [] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onResume", voidClass, [] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onPause", voidClass, [] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onStop", voidClass, [] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onDestroyView", voidClass, [] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onDestroy", voidClass, [] as CtClass[])
        addLogMessage(classPool, fragmentClass, true, "onDetach", voidClass, [] as CtClass[])
    }

    static void instrumentService(ClassPool classPool, CtClass serviceClass) {
        CtClass voidClass = classPool.getCtClass("void")
        addLogMessage(classPool, serviceClass, true, "onCreate", voidClass, [] as CtClass[])
        addLogMessage(classPool, serviceClass, true, "onDestroy", voidClass, [] as CtClass[])
    }

}
