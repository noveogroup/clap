package com.noveogroup.clap.plugin.instrumentation.impl

import com.noveogroup.clap.plugin.instrumentation.Instrumentor
import com.noveogroup.clap.plugin.instrumentation.utils.InstrumentationUtils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.CtNewMethod
import javassist.Modifier
import javassist.NotFoundException
import javassist.bytecode.LocalVariableAttribute
import javassist.bytecode.MethodInfo;

/**
 * @author Andrey Sokolov
 */
class SendScreenshots implements Instrumentor {
    @Override
    String getName() {
        return "sendScreenshots"
    }

    @Override
    void instrument(final ClassPool classPool, final CtClass aClass) {
        if (InstrumentationUtils.findSuperclass(aClass, classPool.getCtClass("android.app.Activity"))) {
            CtClass retClass = classPool.getCtClass("boolean")
            CtClass paramClass = classPool.getCtClass("android.view.KeyEvent")
            instr(classPool,aClass,true,"dispatchKeyEvent",retClass, [paramClass] as CtClass[])
        }
    }


    private static CtMethod instr(ClassPool classPool, CtClass aClass, boolean override, String methodName, CtClass returnType, CtClass[] parameters){
        CtMethod method

        try {
            method = aClass.getDeclaredMethod(methodName, parameters)


            MethodInfo methodInfo = method.getMethodInfo();
            LocalVariableAttribute table = methodInfo.getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
            int frameWithNameAtConstantPool = table.nameIndex(1);
            String variableName = methodInfo.getConstPool().getUtf8Info(frameWithNameAtConstantPool)
            method.insertBefore("com.noveogroup.clap.aspect.dispatchkey.DispatchKeyHandler.getInstance().dispatchKey("+variableName+",this);")

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

           /* if (override) {
                if (returnType.name != "void") {
                    builder.append("return ")
                }
                builder.append("super.$methodName(")
                parameters.size().times { int i ->
                    if (i > 0) builder.append(",")
                    builder.append("arg$i")
                }
                builder.append(");")
            }*/



            //WARNING!!! SO HARDCODE!
            builder.append("com.noveogroup.clap.aspect.dispatchkey.DispatchKeyHandler.getInstance().dispatchKey(arg0,this); return super.dispatchKeyEvent(arg0);")






            builder.append(" }")

            method = CtNewMethod.make(builder.toString(), aClass)
            aClass.addMethod(method)
        }
        return method
    }
}
