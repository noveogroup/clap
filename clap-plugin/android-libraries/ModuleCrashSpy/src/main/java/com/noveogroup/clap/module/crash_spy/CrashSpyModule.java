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

package com.noveogroup.clap.module.crash_spy;

import com.noveogroup.clap.library.common.AndroidContext;
import com.noveogroup.clap.library.common.Module;
import com.noveogroup.clap.library.common.StaticContext;

public final class CrashSpyModule implements Module {

    private class ExceptionHandler implements Thread.UncaughtExceptionHandler {

        private final Thread.UncaughtExceptionHandler previousHandler;

        public ExceptionHandler(Thread.UncaughtExceptionHandler previousHandler) {
            this.previousHandler = previousHandler;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable uncaughtException) {
            try {
                CrashService.saveCrash(context.getContext(), thread, uncaughtException);
            } catch (Throwable ignored) {
            } finally {
                previousHandler.uncaughtException(thread, uncaughtException);
            }
        }
    }

    private AndroidContext context;

    @Override
    public void initStatic(StaticContext context) {
        // do nothing
    }

    @Override
    public void initContext(AndroidContext context) {
        this.context = context;
        Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(handler));
    }

}
