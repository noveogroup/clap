package com.noveogroup.clap.client.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.model.message.Message;
import com.noveogroup.clap.model.request.message.SendMessageRequest;
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

    public ClapService() {
        super("Clap service");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        try {
            BasicHttpParams param = new BasicHttpParams();
            HttpProtocolParams.setVersion(param, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(param, HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(param, false);
            MessagesEndpoint messagesEndpoint = ProxyFactory.create(MessagesEndpoint.class, "http://10.0.14.53:8080/clap-rest", new ApacheHttpClient4Executor(param));
            //TODO auth
            Message messageDTO = new Message();
            messageDTO.setLogCat(intent.getStringExtra("logCat"));
            messageDTO.setStackTraceInfo(intent.getStringExtra("stackTraceInfo"));
            messageDTO.setDeviceInfo(intent.getStringExtra("deviceInfo"));
            messageDTO.setActivityTraceLog(intent.getStringExtra("activityLog"));
            messageDTO.setTimestamp(new Date());
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.setMessage(messageDTO);
            sendMessageRequest.setRevisionHash(intent.getStringExtra("revision"));
            messagesEndpoint.saveMessage(sendMessageRequest);

            Log.d("SERVICE", "SERVICE START");
        } catch (Exception e) {
            Log.e("ClapService", "Error while sending message to server " + e.getMessage(), e);
        }

    }
}
