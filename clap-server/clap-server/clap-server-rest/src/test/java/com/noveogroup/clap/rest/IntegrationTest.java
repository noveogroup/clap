package com.noveogroup.clap.rest;

import com.google.common.collect.Lists;
import com.noveogroup.clap.model.auth.ApkAuthentication;
import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.StackTraceEntry;
import com.noveogroup.clap.model.message.ThreadInfo;
import com.noveogroup.clap.model.message.log.LogEntry;
import com.noveogroup.clap.model.request.message.CrashMessageRequest;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.response.ClapResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Andrey Sokolov
 */
public class IntegrationTest {

    public static final String BASE = "http://localhost:8080/clap-rest/v1/";

    @Test
    public void testAll() throws Exception {

        final AuthenticationEndpoint authenticationEndpoint = ProxyFactory.create(AuthenticationEndpoint.class, BASE);
        final Authentication authentication = new Authentication();
        authentication.setLogin("unnamed");
        authentication.setPassword("unnamed_password");
        final String token = authenticationEndpoint.getToken(authentication);
        final UploadFileEndpoint uploadFileEndpoint = ProxyFactory.create(UploadFileEndpoint.class,BASE);
        final CreateOrUpdateRevisionRequest request = new CreateOrUpdateRevisionRequest();
        request.setPackageStream(getClass().getResourceAsStream("/com.noveogroup.clap-clap-application_hacked.apk"));
        final String testProjectExternalId = "testProjectExternalId";
        request.setProjectExternalId(testProjectExternalId);
        final String random = "random";
        request.setRandom(random);
        final String testRevisionHash = "testRevisionHash";
        request.setRevisionHash(testRevisionHash);
        final String testVariantHash = "testVariantHash";
        request.setVariantHash(testVariantHash);
        final String testVariantName = "testVariantName";
        request.setVariantName(testVariantName);
        request.setToken(token);
        final ClapResponse response = uploadFileEndpoint.createOrUpdateRevision(request);
        assertEquals(0, response.getCode());


        final ApkAuthentication apkAuthentication = new ApkAuthentication();
        apkAuthentication.setProjectId(testProjectExternalId);
        apkAuthentication.setRevisionHash(testRevisionHash);
        apkAuthentication.setVariantHash(testVariantHash);
        apkAuthentication.setRandom(random);
        final String token2 = authenticationEndpoint.getToken(apkAuthentication);

        MessagesEndpoint messagesEndpoint = ProxyFactory.create(MessagesEndpoint.class,BASE);
        final CrashMessageRequest crashMessageRequest = new CrashMessageRequest();
        crashMessageRequest.setProjectId(testProjectExternalId);
        crashMessageRequest.setRevisionHash(testRevisionHash);
        crashMessageRequest.setVariantHash(testVariantHash);
        crashMessageRequest.setToken(token2);
        final CrashMessage message = new CrashMessage();
        message.setTimestamp(new Date().getTime());
        message.setDeviceId("someDeviceId");
        message.setException("some exception");
        message.setLogCat("some logcat");
        final LogEntry logEntry = new LogEntry();
        logEntry.setLevel(1);
        logEntry.setLoggerName("logger");
        logEntry.setMessage("message1");
        logEntry.setThreadName("thread1");
        logEntry.setTimestamp(new Date().getTime());
        final LogEntry logEntry2 = new LogEntry();
        logEntry2.setLevel(1);
        logEntry2.setLoggerName("logger");
        logEntry2.setMessage("message1");
        logEntry2.setThreadName("thread1");
        logEntry2.setTimestamp(new Date().getTime());
        List<LogEntry> logs = Lists.newArrayList();
        logs.add(logEntry);
        logs.add(logEntry2);
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
    }
}
