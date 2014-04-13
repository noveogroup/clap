package com.noveogroup.clap.client.service.message;

import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.request.message.BaseMessageRequest;
import com.noveogroup.clap.rest.AuthenticationEndpoint;
import com.noveogroup.clap.rest.MessagesEndpoint;
import org.apache.http.HttpVersion;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseMessageProcessor implements MessageProcessor{

    public static final String CLAP_HOST = "http://10.0.14.53:8080/clap-rest";


    protected void fillSendMessageRequest(final Intent intent, final BasicHttpParams param, final BaseMessageRequest sendMessageRequest) {
        String token = getToken(param);
        sendMessageRequest.setRevisionHash(intent.getStringExtra("revision"));
        sendMessageRequest.setToken(token);
    }

    protected MessagesEndpoint getMessagesEndpoint(final BasicHttpParams param) {
        return ProxyFactory.create(MessagesEndpoint.class, CLAP_HOST, new ApacheHttpClient4Executor(param));
    }

    protected BasicHttpParams getBasicHttpParams() {
        BasicHttpParams param = new BasicHttpParams();
        HttpProtocolParams.setVersion(param, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(param, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(param, false);
        return param;
    }

    protected String getToken(final BasicHttpParams param) {
        AuthenticationEndpoint authenticationEndpoint = ProxyFactory.create(AuthenticationEndpoint.class, CLAP_HOST, new ApacheHttpClient4Executor(param));
        Authentication authentication = new Authentication();
        //TODO think about convenient acquiring auth token
        authentication.setLogin("test");
        authentication.setPassword("123");
        String token = authenticationEndpoint.getToken(authentication);
        Log.d("TAG", "TOKEN " + token);
        return token;
    }
}
