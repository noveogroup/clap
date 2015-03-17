/*
 * Copyright (c) 2015 Noveo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.noveogroup.android.reporter.library.system;

import com.noveogroup.android.reporter.library.Reporter;

public class Utils {

    private static final String PACKAGE_NAME = Reporter.class.getPackage().getName();

    private static final class CallerResolver extends SecurityManager {
        public Class<?> getCaller() {
            Class[] classContext = getClassContext();
            // sometimes class context is null (usually on new Android devices)
            if (classContext == null || classContext.length <= 0) {
                return null; // if class context is null or empty
            }

            boolean packageFound = false;
            for (Class aClass : classContext) {
                if (!packageFound) {
                    if (aClass.getPackage().getName().startsWith(PACKAGE_NAME)) {
                        packageFound = true;
                    }
                } else {
                    if (!aClass.getPackage().getName().startsWith(PACKAGE_NAME)) {
                        return aClass;
                    }
                }
            }
            return classContext[classContext.length - 1];
        }
    }

    private static final CallerResolver CALLER_RESOLVER = new CallerResolver();

    private static StackTraceElement getCallerStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length <= 0) {
            return null; // if stack trace is null or empty
        }

        boolean packageFound = false;
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (!packageFound) {
                if (stackTraceElement.getClassName().startsWith(PACKAGE_NAME)) {
                    packageFound = true;
                }
            } else {
                if (!stackTraceElement.getClassName().startsWith(PACKAGE_NAME)) {
                    return stackTraceElement;
                }
            }
        }
        return stackTrace[stackTrace.length - 1];
    }

    /**
     * Returns a name of a class that calls logging methods.
     * <p/>
     * Can be much faster than {@link #getCaller()} because
     * this method tries to use {@link SecurityManager} to get
     * caller context.
     *
     * @return the caller's name.
     */
    public static String getCallerClassName() {
        Class<?> caller = CALLER_RESOLVER.getCaller();
        if (caller == null) {
            StackTraceElement callerStackTrace = getCallerStackTrace();
            return callerStackTrace == null ? null : callerStackTrace.getClassName();
        } else {
            return caller.getName();
        }
    }

    /**
     * Returns stack trace element corresponding to a class that calls
     * logging methods.
     * <p/>
     * This method compares names of the packages of stack trace elements
     * with the package of this library to find information about caller.
     *
     * @return the caller stack trace element.
     */
    public static StackTraceElement getCaller() {
        return getCallerStackTrace();
    }

}
