package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.model.revision.RevisionDTO;

import javax.inject.Inject;

/**
 * @author Mikhail Demidov
 */
public interface RevisionService {

    RevisionDTO addRevision(Long projectId, RevisionDTO revisionDTO, byte[] mainPackage, byte[] specialPackage);

    /**
     * Change type to enum
     *
     * @param revisionId
     * @param type
     * @return
     */
    byte[] getApplication(Long revisionId, Integer type);


}
