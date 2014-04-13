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
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
            ScreenshotMessage messageDTO = new ScreenshotMessage();
            messageDTO.setTimestamp(new Date());
            ScreenshotMessageRequest sendMessageRequest = new ScreenshotMessageRequest();
            sendMessageRequest.setScreenshotFileStream(new ByteArrayInputStream(bitmap));
            fillSendMessageRequest(intent, param, sendMessageRequest);

            Log.d(TAG, "Trying to send: " + sendMessageRequest);

            String url = CLAP_HOST+"/message/screenshot";
            String token = sendMessageRequest.getToken();
            String revisionHash = sendMessageRequest.getRevisionHash();
            try {
                HttpClient client = new HttpClient(url);
                client.connectForMultipart();
                client.addFormPart("token", token);
                client.addFormPart("revisionHash", revisionHash);
                client.addFilePart("screenshotFile", "screen.png", bitmap);
                client.finishMultipart();
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            Log.d(TAG, "MESSAGE SENT");
        } else {
            Log.d(TAG, "no bitmap");
        }
    }

    private class HttpClient {

        private final String delimiter = "--";
        private final String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";

        private final String url;
        private HttpURLConnection con;
        private OutputStream os;

        private HttpClient(final String url) {
            this.url = url;
        }

        public void addFormPart(String paramName, String value) throws Exception {
            writeParamData(paramName, value);
        }

        private void writeParamData(String paramName, String value) throws Exception {
            os.write( (delimiter + boundary + "\r\n").getBytes());
            os.write( "Content-Type: text/plain\r\n".getBytes());
            os.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
            os.write( ("\r\n" + value + "\r\n").getBytes());

        }

        public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
            os.write( (delimiter + boundary + "\r\n").getBytes());
            os.write( ("Content-Disposition: form-data; name=\"" + paramName +  "\"; filename=\"" + fileName + "\"\r\n"  ).getBytes());
            os.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
            os.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
            os.write("\r\n".getBytes());

            os.write(data);

            os.write("\r\n".getBytes());
        }

        public void connectForMultipart() throws Exception {
            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.connect();
            os = con.getOutputStream();
        }

        public void finishMultipart() throws IOException {
            os.flush();
            os.close();
            con.disconnect();
        }
    }
}
