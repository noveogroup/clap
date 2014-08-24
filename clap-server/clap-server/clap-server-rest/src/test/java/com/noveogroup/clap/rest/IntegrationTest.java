package com.noveogroup.clap.rest;

import com.noveogroup.clap.model.auth.Authentication;
import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.response.ClapResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.junit.Test;

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
        request.setProjectExternalId("testProjectExternalId");
        request.setRandom("random");
        request.setRevisionHash("testRevisionHash");
        request.setVariantHash("testVariantHash");
        request.setVariantName("testVariantName");
        request.setToken(token);
        final ClapResponse response = uploadFileEndpoint.createOrUpdateRevision(request);
        assertEquals(0, response.getCode());
    }
}
