package com.noveogroup.clap.client.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.message.Message;
import com.noveogroup.clap.model.request.message.SendMessageRequest;
import com.noveogroup.clap.rest.AuthenticationEndpoint;
import com.noveogroup.clap.rest.MessagesEndpoint;
import org.apache.http.HttpVersion;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import java.util.Date;

/**
 * @author Mikhail Demidov
 */
public class ClapService extends IntentService {

    public static final String CLAP_HOST = "http://10.0.14.53:8080/clap-rest";

    public ClapService() {
        super("Clap service");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d("SERVICE", "SERVICE START");
        try {
            BasicHttpParams param = new BasicHttpParams();
            HttpProtocolParams.setVersion(param, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(param, HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(param, false);
            AuthenticationEndpoint authenticationEndpoint = ProxyFactory.create(AuthenticationEndpoint.class, CLAP_HOST, new ApacheHttpClient4Executor(param));
            Authentication authentication = new Authentication();
            //TODO think about convenient acquiring auth token
            authentication.setLogin("test");
            authentication.setPassword("123");
            String token = authenticationEndpoint.getToken(authentication);
            Log.d("SERVICE", "TOKEN "+token);

            MessagesEndpoint messagesEndpoint = ProxyFactory.create(MessagesEndpoint.class, CLAP_HOST, new ApacheHttpClient4Executor(param));

            Message messageDTO = new Message();
            messageDTO.setLogCat(intent.getStringExtra("logCat"));
            messageDTO.setStackTraceInfo(intent.getStringExtra("stackTraceInfo"));
            messageDTO.setDeviceInfo(intent.getStringExtra("deviceInfo"));
            messageDTO.setActivityTraceLog(intent.getStringExtra("activityLog"));
            messageDTO.setTimestamp(new Date());
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.setMessage(messageDTO);
            sendMessageRequest.setRevisionHash(intent.getStringExtra("revision"));
            sendMessageRequest.setToken(token);
            Log.d("SERVICE", "Trying to send: " + sendMessageRequest);
            messagesEndpoint.saveMessage(sendMessageRequest);

            Log.d("SERVICE", "MESSAGE SENT");
        } catch (Throwable e) {
            Log.e("ClapService", "Error while sending message to server " + e.getMessage(), e);
        }

    }
}
