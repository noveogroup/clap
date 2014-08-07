package com.noveogroup.clap.client.service.message;

import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import com.noveogroup.clap.model.message.log.LogEntry;
import com.noveogroup.clap.model.request.message.LogsBunchMessageRequest;
import com.noveogroup.clap.rest.MessagesEndpoint;
import org.apache.http.params.BasicHttpParams;

import java.util.Date;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
public class LogsBunchMessageProcessor extends BaseMessageProcessor{
    private static final String TAG = "LOGS_BUNCH_MESSAGE_PROCESSOR";

    @Override
    public void processIntent(final Intent intent) {
        Log.d(TAG, "START LOGS_BUNCH MESSAGE SENT");
        BasicHttpParams param = getBasicHttpParams();
        MessagesEndpoint messagesEndpoint = getMessagesEndpoint(param);
        LogsBunchMessage messageDTO = new LogsBunchMessage();
        messageDTO.setLogEntries((List<LogEntry>) intent.getSerializableExtra("logs"));
        messageDTO.setTimestamp(new Date());
        LogsBunchMessageRequest sendMessageRequest = new LogsBunchMessageRequest();
        fillSendMessageRequest(intent, param, sendMessageRequest);
        sendMessageRequest.setMessage(messageDTO);
        Log.d(TAG, "Trying to send: " + sendMessageRequest);
        messagesEndpoint.saveLogsBunchMessage(sendMessageRequest);
        Log.d(TAG, "MESSAGE SENT");
    }
}
