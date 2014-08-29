package com.noveogroup.clap.rest;

import com.google.common.collect.Lists;
import com.noveogroup.clap.model.auth.ApkAuthentication;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import com.noveogroup.clap.model.message.StackTraceEntry;
import com.noveogroup.clap.model.message.ThreadInfo;
import com.noveogroup.clap.model.message.log.LogEntry;
import com.noveogroup.clap.model.request.message.BaseMessageRequest;
import com.noveogroup.clap.model.request.message.CrashMessageRequest;
import com.noveogroup.clap.model.request.message.LogsBunchMessageRequest;
import com.noveogroup.clap.model.request.message.ScreenshotMessageRequest;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.response.ClapResponse;
import junit.framework.Assert;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Andrey Sokolov
 */
public class IntegrationTest {

    public static final String BASE = "http://localhost:8080/clap-rest/v1/";
    private String random = "5509B4B3828FB584374EF35555266DF98ACF9FD60F1270069AB98498CEF5D2E4";
    private String testRevisionHash = "aa9c4fa9f09e7e2ac477af717e57864cc7175611";
    private String testVariantHash = "aa9c4fa9f09e7e2ac477af717e57864cc7175611";
    private String testVariantName = "testVariantName";
    private String testProjectExternalId = "testProjectExternalId";

    @Test
    public void testAll() throws Exception {

        final AuthenticationEndpoint authenticationEndpoint = ProxyFactory.create(AuthenticationEndpoint.class, BASE);
        final Authentication authentication = new Authentication();
        authentication.setLogin("unnamed");
        authentication.setPassword("unnamed_password");
        final String token = authenticationEndpoint.getToken(authentication);
        final UploadFileEndpoint uploadFileEndpoint = ProxyFactory.create(UploadFileEndpoint.class,BASE);
        final CreateOrUpdateRevisionRequest request = new CreateOrUpdateRevisionRequest();
        request.setPackageStream(getClass().getResourceAsStream("/clap_test.apk"));
        request.setProjectExternalId(testProjectExternalId);
        request.setRandom(random);
        request.setRevisionHash(testRevisionHash);
        request.setVariantHash(testVariantHash);
        request.setVariantName(testVariantName);
        request.setToken(token);
        final ClapResponse response = uploadFileEndpoint.createOrUpdateRevision(request);
        assertEquals(0, response.getCode());

        final ApkAuthentication apkAuthentication = getApkAuthentication();
        final String token2 = authenticationEndpoint.getToken(apkAuthentication);

        MessagesEndpoint messagesEndpoint = ProxyFactory.create(MessagesEndpoint.class,BASE);
        final CrashMessageRequest crashMessageRequest = new CrashMessageRequest();
        fillMessageRequest(token2, crashMessageRequest);
        final CrashMessage message = new CrashMessage();
        fillMessage(message);

        message.setException("some exception");
        final ArrayList<String> logCat = new ArrayList<String>();
        logCat.add("some logcat1");
        logCat.add("some logcat2");
        logCat.add("some logcat3");
        logCat.add("some logcat4");
        logCat.add("some logcat5");
        message.setLogCat(logCat);
        final LogEntry logEntry = new LogEntry();
        logEntry.setLevel(1);
        logEntry.setLoggerName("logger");
        logEntry.setMessage("message1");
        logEntry.setThreadName("thread1");
        logEntry.setTimestamp(new Date().getTime());
        final LogEntry logEntry2 = new LogEntry();
        logEntry2.setLevel(1);
        logEntry2.setLoggerName("logger2");
        logEntry2.setMessage("message2");
        logEntry2.setThreadName("thread2");
        logEntry2.setTimestamp(new Date().getTime());
        List<LogEntry> logs = Lists.newArrayList();   /*
        logs.add(logEntry);
        logs.add(logEntry2);
        logs.add(logEntry);
        logs.add(logEntry2);
        logs.add(logEntry);
        logs.add(logEntry2); */
        message.setLogs(logs);
        message.setThreads(new ArrayList<ThreadInfo>());
        message.setThreadId(124124);
        final ThreadInfo threadInfo = new ThreadInfo();
        threadInfo.setId(124);
        threadInfo.setName("some thread");
        threadInfo.setState("some state");
        final ArrayList<StackTraceEntry> stackTrace = new ArrayList<StackTraceEntry>();
        final StackTraceEntry stackTraceEntry = new StackTraceEntry();
        stackTraceEntry.setClassName("some Class");
        stackTraceEntry.setLineNumber(124515);
        stackTraceEntry.setMethodName("some method");
        stackTrace.add(stackTraceEntry);
        threadInfo.setStackTrace(stackTrace);
        message.getThreads().add(threadInfo);
        crashMessageRequest.setMessage(message);
        final ClapResponse clapResponse = messagesEndpoint.saveCrashMessage(crashMessageRequest);
        assertEquals(0, clapResponse.getCode());

        final String token3 = authenticationEndpoint.getToken(apkAuthentication);
        final LogsBunchMessageRequest logsBunchMessageRequest = new LogsBunchMessageRequest();
        fillMessageRequest(token3, logsBunchMessageRequest);
        final LogsBunchMessage logsBunchMessage = new LogsBunchMessage();
        fillMessage(logsBunchMessage);
        logsBunchMessage.setLogCat(logCat);
        logsBunchMessage.setLogs(logs);
        logsBunchMessageRequest.setMessage(logsBunchMessage);
        final ClapResponse clapResponse1 = messagesEndpoint.saveLogsBunchMessage(logsBunchMessageRequest);
        assertEquals(0, clapResponse1.getCode());

        apkAuthentication.setRandom("badRandom");
        try {
            final String token4 = authenticationEndpoint.getToken(apkAuthentication);
        } catch (ClientResponseFailure e){
            assertEquals(403,e.getResponse().getStatus());
        } catch (Exception e){
            Assert.fail();
        }
    }

    private ApkAuthentication getApkAuthentication() {
        final ApkAuthentication apkAuthentication = new ApkAuthentication();
        apkAuthentication.setProjectId(testProjectExternalId);
        apkAuthentication.setRevisionHash(testRevisionHash);
        apkAuthentication.setVariantHash(testVariantHash);
        apkAuthentication.setRandom(random);
        return apkAuthentication;
    }

    @Test
    public void testScreenshot() throws Exception {
        final AuthenticationEndpoint authenticationEndpoint = ProxyFactory.create(AuthenticationEndpoint.class, BASE);
        final String token = authenticationEndpoint.getToken(getApkAuthentication());
        final UploadFileEndpoint uploadFileEndpoint = ProxyFactory.create(UploadFileEndpoint.class, BASE);
        final ScreenshotMessageRequest request = new ScreenshotMessageRequest();
        request.setRevisionHash(testRevisionHash);
        request.setVariantHash(testVariantHash);
        request.setToken(token);
        request.setScreenshotFileStream(getClass().getResourceAsStream("/clap_8972227388431348519.png"));
        final ScreenshotMessage message = new ScreenshotMessage();
        fillMessage(message);
        request.setMessage(message);
        final ClapResponse clapResponse = uploadFileEndpoint.saveScreenshot(request);
        assertEquals(0, clapResponse.getCode());
    }

    private void fillMessage(final BaseMessage message) {
        message.setTimestamp(new Date().getTime());
        message.setDeviceId("someDeviceId");
        final HashMap<String, String> deviceInfo = new HashMap<String, String>();
        deviceInfo.put("somDeviceInfoKey","someDeviceInfoValue");
        deviceInfo.put("somDeviceInfoKey2","someDeviceInfoValue2");
        deviceInfo.put("somDeviceInfoKey3","someDeviceInfoValue3");
        message.setDeviceInfo(deviceInfo);
    }

    private void fillMessageRequest(final String token, final BaseMessageRequest crashMessageRequest) {
        crashMessageRequest.setProjectId(testProjectExternalId);
        crashMessageRequest.setRevisionHash(testRevisionHash);
        crashMessageRequest.setVariantHash(testVariantHash);
        crashMessageRequest.setToken(token);
    }
}
