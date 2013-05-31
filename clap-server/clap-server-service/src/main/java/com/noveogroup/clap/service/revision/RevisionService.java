package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.request.revision.AddOrGetRevisionRequest;
import com.noveogroup.clap.model.request.revision.GetApplicationRequest;
import com.noveogroup.clap.model.request.revision.RevisionRequest;
import com.noveogroup.clap.model.request.revision.UpdateRevisionPackagesRequest;

/**
 * @author Mikhail Demidov
 */
public interface RevisionService {

    Revision addOrGetRevision(AddOrGetRevisionRequest request);

    Revision updateRevisionPackages(UpdateRevisionPackagesRequest request);

    ApplicationFile getApplication(GetApplicationRequest request);

    Revision getRevision(RevisionRequest request);

}
