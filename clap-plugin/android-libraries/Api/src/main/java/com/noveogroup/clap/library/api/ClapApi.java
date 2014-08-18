package com.noveogroup.clap.library.api;

import android.content.Context;
import android.graphics.Bitmap;

import com.noveogroup.clap.api.BuildConfigHelper;
import com.noveogroup.clap.library.api.model.auth.Authentication;
import com.noveogroup.clap.library.api.model.message.CrashMessage;
import com.noveogroup.clap.library.api.model.message.LogsBunchMessage;
import com.noveogroup.clap.library.api.model.message.log.LogEntry;
import com.noveogroup.clap.library.api.model.request.message.BaseMessageRequest;
import com.noveogroup.clap.library.api.model.request.message.LogsBunchMessageRequest;
import com.noveogroup.clap.library.api.model.request.message.SendMessageRequest;
import com.noveogroup.clap.library.api.rest.AuthenticationEndpoint;
import com.noveogroup.clap.library.api.rest.MessagesEndpoint;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ClapApi {

    private final Context applicationContext;

    public ClapApi(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    public String getBuildConfigField(String field) {
        return BuildConfigHelper.get(applicationContext.getPackageName(), field);
    }

    private BasicHttpParams getBasicHttpParams() {
        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, false);
        return params;
    }

    private AuthenticationEndpoint getAuthenticationEndpoint(BasicHttpParams params) {
        String url = getBuildConfigField(BuildConfigHelper.FIELD_CLAP_SERVER_URL);
        return ProxyFactory.create(AuthenticationEndpoint.class, url, new ApacheHttpClient4Executor(params));
    }

    private MessagesEndpoint getMessagesEndpoint(BasicHttpParams params) {
        String url = getBuildConfigField(BuildConfigHelper.FIELD_CLAP_SERVER_URL);
        return ProxyFactory.create(MessagesEndpoint.class, url, new ApacheHttpClient4Executor(params));
    }

    private String getToken(BasicHttpParams params) {
        AuthenticationEndpoint authenticationEndpoint = getAuthenticationEndpoint(params);
        Authentication authentication = new Authentication();
        authentication.setLogin(getBuildConfigField(BuildConfigHelper.FIELD_CLAP_USERNAME));
        authentication.setPassword(getBuildConfigField(BuildConfigHelper.FIELD_CLAP_PASSWORD));
        return authenticationEndpoint.getToken(authentication);
    }

    private void fillSendMessageRequest(BasicHttpParams params, BaseMessageRequest request) {
        String token = getToken(params);
        request.setToken(token);
        request.setRevisionHash(getBuildConfigField(BuildConfigHelper.FIELD_CLAP_SOURCE_HASH));
    }

    public void saveCrashMessage(String deviceInfo, String stackTraceInfo, String logCat, String activityTraceLog) {
        BasicHttpParams params = getBasicHttpParams();

        CrashMessage message = new CrashMessage();
        message.setTimestamp(new Date());
        message.setDeviceInfo(deviceInfo);
        message.setStackTraceInfo(stackTraceInfo);
        message.setLogCat(logCat);
        message.setActivityTraceLog(activityTraceLog);

        SendMessageRequest request = new SendMessageRequest();
        fillSendMessageRequest(params, request);
        request.setMessage(message);

        MessagesEndpoint messagesEndpoint = getMessagesEndpoint(params);
        messagesEndpoint.saveCrashMessage(request);
    }

    public void saveLogsBunchMessage(List<LogEntry> logs) {
        BasicHttpParams params = getBasicHttpParams();

        LogsBunchMessage message = new LogsBunchMessage();
        message.setTimestamp(new Date());
        message.setLogEntries(logs);

        LogsBunchMessageRequest request = new LogsBunchMessageRequest();
        fillSendMessageRequest(params, request);
        request.setMessage(message);

        MessagesEndpoint messagesEndpoint = getMessagesEndpoint(params);
        messagesEndpoint.saveLogsBunchMessage(request);
    }

    public void saveScreenShot(Bitmap bitmap) throws IOException {
        BasicHttpParams params = getBasicHttpParams();

        String url = getBuildConfigField(BuildConfigHelper.FIELD_CLAP_SERVER_URL) + "/upload/screenshot";
        String token = getToken(params);
        String revisionHash = getBuildConfigField(BuildConfigHelper.FIELD_CLAP_SOURCE_HASH);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, output);
        byte[] bitmapBytes = output.toByteArray();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        MultipartEntity multipartEntity = new MultipartEntity();
        multipartEntity.addPart("token", new StringBody(token));
        multipartEntity.addPart("revisionHash", new StringBody(revisionHash));
        multipartEntity.addPart("screenshotFile", new ByteArrayBody(bitmapBytes, "screen.png"));
        post.setEntity(multipartEntity);
        client.execute(post);
    }

}
