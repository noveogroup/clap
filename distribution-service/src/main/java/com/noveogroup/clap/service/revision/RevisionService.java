package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.RevisionDTO;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Mikhail Demidov
 */
public interface RevisionService {

    RevisionDTO addRevision(Long projectId, RevisionDTO revisionDTO, byte[] mainPackage, byte[] specialPackage);

    RevisionDTO updateRevisionPackages(RevisionDTO revisionDTO, byte[] mainPackage, byte[] specialPackage);


    RevisionDTO updateRevisionPackages(Long revisionTimestamp, byte[] mainPackage, byte[] specialPackage);

    /**
     * Change type to enum
     *
     * @param revisionId
     * @param type
     * @return
     */
    ApplicationFile getApplication(Long revisionId, Integer type);

    RevisionDTO getRevision(Long timestamp);

    RevisionDTO findById(Long id);

}
