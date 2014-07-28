package com.noveogroup.clap.generator;

import com.noveogroup.clap.ProjectInfo;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;

import java.io.File;
import java.io.IOException;

/**
 * @author Mikhail Demidov
 */
public class RevisionGenerator {

    public RevisionGenerator() {
    }

    public void generate(final File generatedSourcesDirectory, final String className, final String packageName, final ProjectInfo projectInfo) throws JClassAlreadyExistsException, IOException {
        JCodeModel jCodeModel = new JCodeModel();
        JPackage jp = jCodeModel._package(packageName);
        JDefinedClass jc = jp._class(className);
        jc._implements(ProjectInfo.class);
        InterfaceParser interfaceParser = new InterfaceParser(jc, ProjectInfo.class, projectInfo);
        interfaceParser.createMethods();


        createDirectory(generatedSourcesDirectory);
        jCodeModel.build(generatedSourcesDirectory);
    }

    private void createDirectory(final File file) {
        if (!file.exists()) {
            createDirectory(file.getParentFile());
            file.mkdir();
        }
    }


}
