package com.noveogroup.clap.client.service.message;

import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.request.message.SendMessageRequest;
import com.noveogroup.clap.rest.MessagesEndpoint;
import org.apache.http.params.BasicHttpParams;

import java.util.Date;

/**
 * @author Andrey Sokolov
 */
public class CrashMessageProcessor extends BaseMessageProcessor {
    private static final String TAG = "CRASH_MESSAGE_PROCESSOR";

    @Override
    public void processIntent(final Intent intent) {


        BasicHttpParams param = getBasicHttpParams();

        MessagesEndpoint messagesEndpoint = getMessagesEndpoint(param);

        CrashMessage messageDTO = new CrashMessage();
        messageDTO.setLogCat(intent.getStringExtra("logCat"));
        messageDTO.setStackTraceInfo(intent.getStringExtra("stackTraceInfo"));
        messageDTO.setDeviceInfo(intent.getStringExtra("deviceInfo"));
        messageDTO.setActivityTraceLog(intent.getStringExtra("activityLog"));
        messageDTO.setTimestamp(new Date());
        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        fillSendMessageRequest(intent, param, sendMessageRequest);
        sendMessageRequest.setMessage(messageDTO);
        Log.d(TAG, "Trying to send: " + sendMessageRequest);
        messagesEndpoint.saveCrashMessage(sendMessageRequest);

        Log.d(TAG, "MESSAGE SENT");
    }
}
