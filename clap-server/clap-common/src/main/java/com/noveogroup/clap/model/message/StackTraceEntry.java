package com.noveogroup.clap.model.message;

/**
 * @author Andrey Sokolov
 */
public class StackTraceEntry {
    private String className;
    private String methodName;
    private int line;

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

    public int getLine() {
        return line;
    }

    public void setLine(final int line) {
        this.line = line;
    }
}
