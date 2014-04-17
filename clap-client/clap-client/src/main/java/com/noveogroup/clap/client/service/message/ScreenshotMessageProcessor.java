package com.noveogroup.clap.client.service.message;

import android.content.Intent;
import android.util.Log;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.util.Date;

/**
 * @author Andrey Sokolov
 */
public class ScreenshotMessageProcessor extends BaseMessageProcessor {
    private static final String TAG = "SCREENSHOT_MESSAGE_PROCESSOR";

    @Override
    public void processIntent(final Intent intent) {
        final byte[] bitmap = intent.getByteArrayExtra("bitmap");
        if (bitmap != null) {

            BasicHttpParams param = getBasicHttpParams();
            ScreenshotMessage messageDTO = new ScreenshotMessage();
            messageDTO.setTimestamp(new Date());

            String url = CLAP_HOST + "/message/screenshot";
            String token = getToken(param);
            String revisionHash = getRevisionHash(intent);
            Log.d(TAG, "Trying to send screenshot");

            HttpClient client = new DefaultHttpClient();

            try {
                HttpPost post = new HttpPost(url);
                final MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                ByteArrayBody bitmapBody = new ByteArrayBody(bitmap, "screen.png");
                multipartEntityBuilder.addPart("token", new StringBody(token));
                multipartEntityBuilder.addPart("revisionHash", new StringBody(revisionHash));
                multipartEntityBuilder.addPart("screenshotFile", bitmapBody);
                post.setEntity(multipartEntityBuilder.build());
                client.execute(post);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            Log.d(TAG, "MESSAGE SENT");
        } else {
            Log.d(TAG, "no bitmap");
        }
    }

}
