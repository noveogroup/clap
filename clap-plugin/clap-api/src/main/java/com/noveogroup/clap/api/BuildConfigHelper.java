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

package com.noveogroup.clap.api;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BuildConfigHelper {

    public static final String FIELD_CLAP_PROJECT_ID = "CLAP_PROJECT_ID";
    public static final String FIELD_CLAP_SERVER_URL = "CLAP_SERVER_URL";
    public static final String FIELD_CLAP_USERNAME = "CLAP_USERNAME";
    public static final String FIELD_CLAP_PASSWORD = "CLAP_PASSWORD";
    public static final String FIELD_CLAP_SOURCE_HASH = "CLAP_SOURCE_HASH";

    private static final Map<String, String> FIELD_CACHE = new HashMap<>();

    private static String getValue(String packageName, String fieldName) {
        String buildConfigClassName = packageName + ".BuildConfig";
        try {
            Class<?> buildConfigClass = Class.forName(buildConfigClassName);
            Field field = buildConfigClass.getField(fieldName);
            return (String) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("wrong " + buildConfigClassName, e);
        }
    }

    public static synchronized String get(String packageName, String fieldName) {
        String key = String.format("%s$%s", packageName, fieldName);

        String value = FIELD_CACHE.get(key);
        if (value == null) {
            value = getValue(packageName, fieldName);
            FIELD_CACHE.put(key, value);
        }

        return value;
    }

}
