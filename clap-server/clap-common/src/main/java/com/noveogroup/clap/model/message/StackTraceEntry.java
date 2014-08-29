package com.noveogroup.clap.model.message;

import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
public class StackTraceEntry implements Serializable {
    private String className;
    private String methodName;
    private int lineNumber;

    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
