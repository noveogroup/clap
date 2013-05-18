package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;

/**
 * @author Mikhail Demidov
 */
public interface RevisionService {

    Revision addRevision(Long projectId, Revision revision, byte[] mainPackage, byte[] specialPackage);

    Revision updateRevisionPackages(Revision revision, byte[] mainPackage, byte[] specialPackage);


    Revision updateRevisionPackages(Long revisionTimestamp, byte[] mainPackage, byte[] specialPackage);

    /**
     * Change type to enum
     *
     * @param revisionId
     * @param type
     * @return
     */
    ApplicationFile getApplication(Long revisionId, Integer type);

    Revision getRevision(Long timestamp);

    Revision findById(Long id);

}
