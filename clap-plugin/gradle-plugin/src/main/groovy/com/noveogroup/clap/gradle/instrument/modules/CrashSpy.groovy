/*
 * Copyright (c) 2014 Noveo Group
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

package com.noveogroup.clap.gradle.instrument.modules

import com.noveogroup.clap.gradle.instrument.Module
import javassist.CannotCompileException
import javassist.ClassPool
import javassist.CtClass
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

class CrashSpy extends Module {

    private static final String LOG = "com.noveogroup.clap.module.crash_spy.AndroidLog";

    @Override
    void instrumentClass(ClassPool classPool, CtClass aClass) {
        aClass.instrument(new ExprEditor() {
            @Override
            void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName() == 'android.util.Log') {
                    String methodName = m.getMethodName() as String

                    if (methodName == 'println') {
                        m.replace('{ ' + LOG + '.log($$); $_ = $proceed($$); }')
                    } else if (methodName == 'wtf' || methodName.length() == 1) {
                        m.replace('{ ' + LOG + '.log(\"' + methodName + '\", $$); $_ = $proceed($$); }')
                    }
                }
            }
        })
    }

}
