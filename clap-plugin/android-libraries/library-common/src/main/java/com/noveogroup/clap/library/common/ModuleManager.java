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

package com.noveogroup.clap.library.common;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {

    private static final ModuleManager INSTANCE = new ModuleManager();

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    private final Object lock = new Object();
    private final Map<String, Module> modules = new HashMap<String, Module>();
    private volatile Context applicationContext = null;
    private volatile boolean initStaticDone = false;
    private volatile boolean initContextDone = false;

    private ModuleManager() {
    }

    public void registerModule(String moduleName, String moduleClassName) {
        try {
            synchronized (lock) {
                Module module = modules.get(moduleName);
                if (module != null) {
                    String className = module.getClass().getName();
                    if (!className.equals(moduleClassName)) {
                        throw new RuntimeException(String.format("cannot module '%s' from %s. it is already registered from %s", moduleName, moduleClassName, className));
                    }
                } else {
                    Class<?> moduleClass = Class.forName(moduleClassName);
                    module = (Module) moduleClass.newInstance();

                    if (initStaticDone) {
                        moduleInitStatic(moduleName, module);
                    }
                    if (initContextDone) {
                        moduleInitContext(moduleName, module);
                    }
                    modules.put(moduleName, module);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("cannot register module '%s' from %s", moduleName, moduleClassName), e);
        }
    }

    private void moduleInitStatic(String moduleName, Module module) {
        module.initStatic(new StaticContext(moduleName));
    }

    private void moduleInitContext(String moduleName, Module module) {
        module.initContext(new AndroidContext(applicationContext, moduleName));
    }

    public void initStatic() {
        synchronized (lock) {
            if (!initStaticDone) {
                initStaticDone = true;

                for (Map.Entry<String, Module> entry : modules.entrySet()) {
                    try {
                        moduleInitStatic(entry.getKey(), entry.getValue());
                    } catch (Throwable ignored) {
                    }
                }
            }
        }
    }

    public void initContext(Context context) {
        synchronized (lock) {
            if (!initContextDone) {
                initContextDone = true;
                applicationContext = context.getApplicationContext();

                for (Map.Entry<String, Module> entry : modules.entrySet()) {
                    try {
                        moduleInitContext(entry.getKey(), entry.getValue());
                    } catch (Throwable ignored) {
                    }
                }
            }
        }
    }

    public void reportStatus(String message) {
        Log.i("CLAP", message);
    }

    public void reportException(String message, Throwable cause) {
        Log.e("CLAP", message, cause);
    }

}
