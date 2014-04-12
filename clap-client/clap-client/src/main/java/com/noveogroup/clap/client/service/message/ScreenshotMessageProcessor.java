package com.noveogroup.clap.client.service.message;

import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import com.noveogroup.clap.model.request.message.ScreenshotMessageRequest;
import com.noveogroup.clap.model.request.message.SendMessageRequest;
import com.noveogroup.clap.rest.MessagesEndpoint;
import org.apache.http.params.BasicHttpParams;

import java.io.ByteArrayInputStream;
import java.util.Date;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessageProcessor extends BaseMessageProcessor {
    private static final String TAG = "SCREENSHOT_MESSAGE_PROCESSOR";
    @Override
    public void processIntent(final Intent intent) {
        final byte[] bitmap = intent.getByteArrayExtra("bitmap");
        if(bitmap != null){
            BasicHttpParams param = getBasicHttpParams();
            MessagesEndpoint messagesEndpoint = getMessagesEndpoint(param);
            ScreenshotMessage messageDTO = new ScreenshotMessage();
            messageDTO.setTimestamp(new Date());
            ScreenshotMessageRequest sendMessageRequest = new ScreenshotMessageRequest();
            sendMessageRequest.setScreenshotFileStream(new ByteArrayInputStream(bitmap));
            fillSendMessageRequest(intent, param, sendMessageRequest);
            sendMessageRequest.setMessage(messageDTO);
            Log.d(TAG, "Trying to send: " + sendMessageRequest);
            messagesEndpoint.saveScreenshot(sendMessageRequest);
            Log.d(TAG, "MESSAGE SENT");
        } else {
            Log.d(TAG, "no bitmap");
        }
    }
}
