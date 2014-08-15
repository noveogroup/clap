package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.model.request.revision.CreateOrUpdateRevisionRequest;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.ApplicationType;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import com.noveogroup.clap.model.user.User;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Mikhail Demidov
 */
public interface RevisionService {

    /**
     *
     * @param request
     * @return true if new created
     */
    boolean addOrGetRevision(@NotNull CreateOrUpdateRevisionRequest request);

    void updateRevisionData(Revision revision);

    ApplicationFile getApplication(Long revisionId, ApplicationType applicationType);

    Revision getRevision(Long revisionId);

    RevisionWithApkStructure getRevisionWithApkStructure(Long revisionId);

    void deleteRevision(Long id);

    Set<RevisionType> getAvailableTypesToChange(User user,Revision revision);
}
