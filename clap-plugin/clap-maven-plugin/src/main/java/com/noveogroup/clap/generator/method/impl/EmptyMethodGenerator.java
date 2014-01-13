package com.noveogroup.clap.generator.method.impl;

import com.noveogroup.clap.ProjectInfo;
import com.noveogroup.clap.annotation.Parameter;
import com.noveogroup.clap.dataprovider.DataProvider;
import com.noveogroup.clap.generator.method.MethodGenerator;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: mdemidov
 * Date: 5/20/13
 * Time: 9:51 AM
 *
 * @since 5/20/13 9:51 AM
 */
public class EmptyMethodGenerator extends MethodGenerator {


    public EmptyMethodGenerator(final DataProvider dataProvider, final JDefinedClass jDefinedClass, final Method method, final Parameter annotation, final ProjectInfo projectInfo) {
        super(dataProvider, jDefinedClass, method, annotation, projectInfo);
    }

    @Override
    public void generateMethodBody() {
        JMethod jMethod = jDefinedClass.method(JMod.PUBLIC, method.getReturnType(), method.getName());
    }
}
