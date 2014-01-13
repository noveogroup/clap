package com.noveogroup.clap.generator.method;

import com.noveogroup.clap.ProjectInfo;
import com.noveogroup.clap.annotation.Parameter;
import com.noveogroup.clap.dataprovider.DataProvider;
import com.noveogroup.clap.dataprovider.impl.ProjectIdProvider;
import com.noveogroup.clap.dataprovider.impl.RevisionIdProvider;
import com.noveogroup.clap.generator.method.impl.EmptyMethodGenerator;
import com.noveogroup.clap.generator.method.impl.ProjectIdMethodGenerator;
import com.noveogroup.clap.generator.method.impl.RevisionIdMethodGenerator;
import com.sun.codemodel.JDefinedClass;

import java.lang.reflect.Method;

/**
 * @author Mikhail Demidov
 */
public class MethodGeneratorFactory {

    private JDefinedClass jDefinedClass;

    public MethodGeneratorFactory(final JDefinedClass jDefinedClass) {
        this.jDefinedClass = jDefinedClass;
    }

    public MethodGenerator getMethodGenerator(final Method method, final Parameter annotation, final ProjectInfo projectInfo) {
        String parameterName = annotation.parameterName();
        MethodGenerator methodGenerator = null;
        DataProvider dataProvider = null;
        if (annotation != null && parameterName.equalsIgnoreCase("projectId")) {
            dataProvider = new ProjectIdProvider();
            methodGenerator = new ProjectIdMethodGenerator(dataProvider, jDefinedClass, method, annotation, projectInfo);
        } else if (annotation != null && parameterName.equalsIgnoreCase("revisionId")) {
            dataProvider = new RevisionIdProvider();
            methodGenerator = new RevisionIdMethodGenerator(dataProvider, jDefinedClass, method, annotation, projectInfo);
        } else {
            methodGenerator = new EmptyMethodGenerator(dataProvider, jDefinedClass, method, annotation, projectInfo);
        }
        return methodGenerator;
    }
}
