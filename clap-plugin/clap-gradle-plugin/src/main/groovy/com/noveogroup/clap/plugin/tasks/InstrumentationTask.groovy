package com.noveogroup.clap.plugin.tasks

import com.noveogroup.clap.plugin.instrumentation.impl.AddLifecycleLogs
import com.noveogroup.clap.plugin.instrumentation.impl.SendLogsToClap
import com.noveogroup.clap.plugin.instrumentation.impl.SendScreenshots
import com.noveogroup.clap.plugin.instrumentation.utils.InstrumentationUtils
import com.noveogroup.clap.plugin.instrumentation.Instrumentor
import javassist.ClassPool
import javassist.CtClass

/**
 */
class InstrumentationTask {

    Collection<Instrumentor> instrumentors = [new AddLifecycleLogs(),new SendLogsToClap()]
    //Collection<Instrumentor> instrumentors = [new AddLifecycleLogs(),new SendLogsToClap(),new SendScreenshots()]

    InstrumentationTask(Collection<String> functionalityToInstrument) {
        if(functionalityToInstrument != null && !functionalityToInstrument.isEmpty()){
            def toDelete = []
            for(Instrumentor instr:instrumentors){
                if(!functionalityToInstrument.contains(instr.getName())){
                    toDelete << instr
                }
            }
            instrumentors.removeAll(toDelete)
        }
    }

    def instrument(def javaCompile,def logger) {
        logger.lifecycle ":$javaCompile.project.name:instrumentLogger[${javaCompile.name}]"

        ClassPool classPool = new ClassPool()
        classPool.appendClassPath(javaCompile.options.bootClasspath)
        javaCompile.classpath.each {
            classPool.appendClassPath(it.absolutePath)
        }
        classPool.appendClassPath(javaCompile.destinationDir.absolutePath)

        InstrumentationUtils.eachClass(javaCompile.destinationDir) { String className ->
            logger.info "instrument $className"
            CtClass aClass = classPool.getCtClass(className)
            for(Instrumentor instr: instrumentors){
                instr.instrument(classPool, aClass)
            }
            aClass.writeFile(javaCompile.destinationDir.absolutePath)
            aClass.detach()
        }
    }
}
