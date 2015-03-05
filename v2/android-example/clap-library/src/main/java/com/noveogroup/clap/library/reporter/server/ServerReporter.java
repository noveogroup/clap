package com.noveogroup.clap.library.reporter.server;

import com.noveogroup.clap.library.reporter.ClapReporter;
import com.noveogroup.clap.library.reporter.Messages;
import com.noveogroup.clap.library.reporter.server.beans.AbstractRequest;
import com.noveogroup.clap.library.reporter.server.beans.AuthRequest;
import com.noveogroup.clap.library.reporter.server.beans.Request;

import retrofit.RestAdapter;

public class ServerReporter implements ClapReporter {

    private final ClapApiService service;

    private final String projectId;
    private final String revisionHash;
    private final String variantHash;
    private final String random;

    private <R extends AbstractRequest> R prepareAbstractRequest(R request) {
        request.setProjectId(projectId);
        request.setRevisionHash(revisionHash);
        request.setVariantHash(variantHash);
        return request;
    }

    private AuthRequest prepareAuth() {
        AuthRequest authRequest = prepareAbstractRequest(new AuthRequest());
        authRequest.setRandom(random);
        return authRequest;
    }

    private <M extends Messages.Base> Request<M> prepareRequest(String token, M message) {
        Request<M> request = prepareAbstractRequest(new Request<M>());
        request.setToken(token);
        request.setMessage(message);
        return request;
    }

    public ServerReporter(String endpoint, boolean verbose,
                          String projectId, String revisionHash, String variantHash, String random) {
        this.service = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setLogLevel(verbose ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE)
                .build().create(ClapApiService.class);

        this.projectId = projectId;
        this.revisionHash = revisionHash;
        this.variantHash = variantHash;
        this.random = random;
    }

    @Override
    public void reportInfo(Messages.Info message) {
        String token = service.getToken(prepareAuth());
        service.sendInfo(prepareRequest(token, message));
    }

    @Override
    public void reportCrash(Messages.Crash message) {
        String token = service.getToken(prepareAuth());
        service.sendCrash(prepareRequest(token, message));
    }

}
