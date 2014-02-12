package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.ApplicationType;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import com.noveogroup.clap.model.user.User;

import java.util.List;

/**
 * @author Mikhail Demidov
 */
public interface RevisionService {

    Revision addOrGetRevision(AddOrGetRevisionRequest request);

    Revision updateRevisionPackages(UpdateRevisionPackagesRequest request);

    ApplicationFile getApplication(Long revisionId, ApplicationType applicationType);

    Revision getRevision(Long revisionId);

    RevisionWithApkStructure getRevisionWithApkStructure(Long revisionId);

    void deleteRevision(Long id);

    List<RevisionType> getAvailableTypesToChange(User user,Revision revision);
}
