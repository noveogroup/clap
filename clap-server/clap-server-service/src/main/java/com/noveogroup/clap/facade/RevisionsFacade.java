package com.noveogroup.clap.facade;

import com.noveogroup.clap.auth.AuthenticationRequired;
import com.noveogroup.clap.interceptor.ClapMainInterceptor;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.GetApplicationRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.transaction.Transactional;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * @author Andrey Sokolov
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({ClapMainInterceptor.class})
public class RevisionsFacade {

    @Inject
    private RevisionService revisionService;

    @Transactional
    public Revision addOrGetRevision(final AddOrGetRevisionRequest request){
        return revisionService.addOrGetRevision(request);
    }

    @Transactional
    public Revision updateRevisionPackages(final UpdateRevisionPackagesRequest request){
        return revisionService.updateRevisionPackages(request);
    }

    @Transactional
    @AuthenticationRequired
    public ApplicationFile getApplication(final GetApplicationRequest request){
        return revisionService.getApplication(request);
    }

    @Transactional
    @AuthenticationRequired
    public Revision getRevision(final RevisionRequest request){
        return revisionService.getRevision(request);
    }

    @Transactional
    @AuthenticationRequired
    public RevisionWithApkStructure getRevisionWithApkStructure(final RevisionRequest request){
        return revisionService.getRevisionWithApkStructure(request);
    }
}
