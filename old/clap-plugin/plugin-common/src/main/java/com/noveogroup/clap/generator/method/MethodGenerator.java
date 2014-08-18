package com.noveogroup.clap.generator.method;

import com.noveogroup.clap.ProjectInfo;
import com.noveogroup.clap.annotation.Parameter;
import com.noveogroup.clap.dataprovider.DataProvider;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import java.lang.reflect.Method;

/**
 * @author Mikhail Demidov
 */
public abstract class MethodGenerator {

    protected JDefinedClass jDefinedClass;
    protected Method method;
    protected Parameter annotation;
    protected DataProvider dataProvider;
    protected ProjectInfo projectInfo;


    protected MethodGenerator(final DataProvider dataProvider, final JDefinedClass jDefinedClass, final Method method, final Parameter annotation, final ProjectInfo projectInfo) {
        this.jDefinedClass = jDefinedClass;
        this.annotation = annotation;
        this.method = method;
        this.dataProvider = dataProvider;
        this.projectInfo = projectInfo;
    }

    public abstract void generateMethodBody();

    protected void generateDefaultImplementation() {
        JFieldVar field = jDefinedClass.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, method.getReturnType(), annotation.parameterName(), dataProvider.getData(projectInfo));
        JMethod jMethod = jDefinedClass.method(JMod.PUBLIC, method.getReturnType(), method.getName());
        jMethod.annotate(Override.class);
        jMethod.body()._return(JExpr.ref(field.name()));
    }

}
