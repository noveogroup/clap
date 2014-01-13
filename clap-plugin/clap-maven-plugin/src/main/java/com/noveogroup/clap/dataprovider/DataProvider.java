package com.noveogroup.clap.dataprovider;

import com.noveogroup.clap.ProjectInfo;
import com.sun.codemodel.JExpression;

/**
 * @author Mikhail Demidov
 */
public interface DataProvider {

    JExpression getData(ProjectInfo projectInfo);

}
