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

package com.noveogroup.clap.library.api;

import android.content.Context;

import com.noveogroup.clap.library.api.server.ClapApiService;
import com.noveogroup.clap.library.api.server.beans.Auth;
import com.noveogroup.clap.library.api.server.beans.BaseRequest;
import com.noveogroup.clap.library.api.server.beans.CrashRequest;
import com.noveogroup.clap.library.api.server.beans.InfoRequest;
import com.noveogroup.clap.library.api.server.beans.LogEntry;
import com.noveogroup.clap.library.api.utils.SystemUtils;
import com.noveogroup.clap.library.common.BuildConfigHelper;

import java.util.List;

import retrofit.RestAdapter;

public final class ClapApi {

    private ClapApi() {
        throw new UnsupportedOperationException();
    }

    private static final String FIELD_CLAP_SERVER_URL = "CLAP_SERVER_URL";
    private static final String FIELD_CLAP_PROJECT_ID = "CLAP_PROJECT_ID";
    private static final String FIELD_CLAP_REVISION_HASH = "CLAP_REVISION_HASH";
    private static final String FIELD_CLAP_VARIANT_HASH = "CLAP_VARIANT_HASH";
    private static final String FIELD_CLAP_RANDOM = "CLAP_RANDOM";

    public static ClapApiService getApiService(Context context) {
        String serverUrl = BuildConfigHelper.get(context, FIELD_CLAP_SERVER_URL);
        return new RestAdapter.Builder()
                .setEndpoint(serverUrl).build()
                .create(ClapApiService.class);
    }

    public static Auth prepareAuth(Context context) {
        Auth auth = new Auth();
        auth.setProjectId(BuildConfigHelper.get(context, FIELD_CLAP_PROJECT_ID));
        auth.setRevisionHash(BuildConfigHelper.get(context, FIELD_CLAP_REVISION_HASH));
        auth.setVariantHash(BuildConfigHelper.get(context, FIELD_CLAP_VARIANT_HASH));
        auth.setRandom(BuildConfigHelper.get(context, FIELD_CLAP_RANDOM));
        return auth;
    }

    public static <M extends BaseRequest.BaseMessage> M prepareBaseMessage(M message, Context context) {
        message.setTimestamp(System.currentTimeMillis());
        message.setDeviceId(SystemUtils.getDeviceId(context));
        message.setDeviceInfo(SystemUtils.getDeviceInfo(context));
        return message;
    }

    public static InfoRequest.InfoMessage prepareInfoMessage(Context context) {
        return prepareBaseMessage(new InfoRequest.InfoMessage(), context);
    }

    public static CrashRequest.CrashMessage prepareCrashMessage(Context context, Thread thread, Throwable throwable, List<String> logcat, List<LogEntry> logs) {
        CrashRequest.CrashMessage message = prepareBaseMessage(new CrashRequest.CrashMessage(), context);
        message.setThreadId(thread.getId());
        message.setException(SystemUtils.getStackTrace(throwable));
        message.setThreads(SystemUtils.getThreadsInfo());
        message.setLogCat(logcat);
        message.setLogs(logs);
        return message;
    }

    public static <R extends BaseRequest<M>, M extends BaseRequest.BaseMessage> R prepareBaseRequest(R request, Context context, String token, M message) {
        request.setProjectId(BuildConfigHelper.get(context, FIELD_CLAP_PROJECT_ID));
        request.setRevisionHash(BuildConfigHelper.get(context, FIELD_CLAP_REVISION_HASH));
        request.setVariantHash(BuildConfigHelper.get(context, FIELD_CLAP_VARIANT_HASH));
        request.setToken(token);
        request.setMessage(message);
        return request;
    }

}
