package com.noveogroup.clap.generator.method.impl;

import com.noveogroup.clap.ProjectInfo;
import com.noveogroup.clap.annotation.Parameter;
import com.noveogroup.clap.dataprovider.DataProvider;
import com.noveogroup.clap.generator.method.MethodGenerator;
import com.sun.codemodel.JDefinedClass;

import java.lang.reflect.Method;

/**
 * @author Mikhail Demidov
 */
public class RevisionIdMethodGenerator extends MethodGenerator {


    public RevisionIdMethodGenerator(final DataProvider dataProvider, final JDefinedClass jDefinedClass, final Method method, final Parameter annotation, final ProjectInfo projectInfo) {
        super(dataProvider, jDefinedClass, method, annotation, projectInfo);
    }

    @Override
    public void generateMethodBody() {
        generateDefaultVersion();
    }
}
