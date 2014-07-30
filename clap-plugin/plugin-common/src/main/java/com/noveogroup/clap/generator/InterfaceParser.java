package com.noveogroup.clap.generator;

import com.noveogroup.clap.ProjectInfo;
import com.noveogroup.clap.annotation.Parameter;
import com.noveogroup.clap.generator.method.MethodGenerator;
import com.noveogroup.clap.generator.method.MethodGeneratorFactory;
import com.sun.codemodel.JDefinedClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Mikhail Demidov
 */
public class InterfaceParser {

    private JDefinedClass jc;

    private Class clazz;

    private ProjectInfo projectInfo;


    public InterfaceParser(JDefinedClass jc, Class clazz, ProjectInfo projectInfo) {
        this.jc = jc;
        this.clazz = clazz;
        this.projectInfo = projectInfo;
    }

    public void createMethods() {

        MethodGeneratorFactory methodGeneratorFactory = new MethodGeneratorFactory(jc);
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            boolean methodFilled = false;
            if (method.isAnnotationPresent(Parameter.class)) {
                MethodGenerator methodGenerator = methodGeneratorFactory.getMethodGenerator(method, method.getAnnotation(Parameter.class), projectInfo);
                methodGenerator.generateMethodBody();
            } else {
                methodGeneratorFactory.getMethodGenerator(method, null, projectInfo);
            }
        }

    }

}
