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
import com.noveogroup.clap.library.api.utils.BuildConfigHelper;
import com.noveogroup.clap.library.api.utils.SystemUtils;

import java.util.List;

import retrofit.RestAdapter;

public class ClapApi {

    private final Context applicationContext;

    private final ClapApiService apiService;

    private final String projectId;
    private final String revisionHash;
    private final String variantHash;
    private final String random;

    private final String deviceId;

    public ClapApi(Context context) {
        this.applicationContext = context.getApplicationContext();

        String serverUrl = BuildConfigHelper.get(applicationContext, BuildConfigHelper.FIELD_CLAP_SERVER_URL);
        this.apiService = new RestAdapter.Builder()
                .setEndpoint(serverUrl).build()
                .create(ClapApiService.class);

        this.projectId = BuildConfigHelper.get(applicationContext, BuildConfigHelper.FIELD_CLAP_PROJECT_ID);
        this.revisionHash = BuildConfigHelper.get(applicationContext, BuildConfigHelper.FIELD_CLAP_REVISION_HASH);
        this.variantHash = BuildConfigHelper.get(applicationContext, BuildConfigHelper.FIELD_CLAP_VARIANT_HASH);
        this.random = BuildConfigHelper.get(applicationContext, BuildConfigHelper.FIELD_CLAP_RANDOM);

        this.deviceId = SystemUtils.getDeviceId(applicationContext);
    }

    private Auth prepareAuth() {
        Auth auth = new Auth();
        auth.setProjectId(projectId);
        auth.setRevisionHash(revisionHash);
        auth.setVariantHash(variantHash);
        auth.setRandom(random);
        return auth;
    }

    private <R extends BaseRequest<M>, M extends BaseRequest.BaseMessage> R prepare(R request, String token) {
        request.setProjectId(projectId);
        request.setRevisionHash(revisionHash);
        request.setVariantHash(variantHash);
        request.setToken(token);
        request.setMessage(request.createMessage());

        request.getMessage().setTimestamp(System.currentTimeMillis());
        request.getMessage().setDeviceId(deviceId);
        request.getMessage().setDeviceInfo(SystemUtils.getDeviceInfo(applicationContext));

        return request;
    }

    public void sendInfo() {
        String token = apiService.getToken(prepareAuth());
        InfoRequest request = prepare(new InfoRequest(), token);
        apiService.sendInfo(request);
    }

    public void sendCrash(Thread thread, Throwable throwable, String logcat, List<LogEntry> logs) {
        String token = apiService.getToken(prepareAuth());
        CrashRequest request = prepare(new CrashRequest(), token);
        request.getMessage().setThreadId(thread.getId());
        request.getMessage().setException(SystemUtils.getStackTrace(throwable));
        request.getMessage().setLogCat(logcat);
        request.getMessage().setLogs(logs);
        request.getMessage().setThreads(SystemUtils.getThreadsInfo());
        apiService.sendCrash(request);
    }

}
