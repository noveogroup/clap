package com.noveogroup.clap.dataprovider.impl;

import com.noveogroup.clap.ProjectInfo;
import com.noveogroup.clap.dataprovider.DataProvider;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;

/**
 * @author Mikhail Demidov
 */
public class ProjectIdProvider implements DataProvider {


    @Override
    public JExpression getData(final ProjectInfo projectInfo) {
        return JExpr.lit(projectInfo.getProjectId());
    }
}
