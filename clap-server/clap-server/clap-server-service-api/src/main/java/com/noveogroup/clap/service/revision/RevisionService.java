package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.model.request.revision.CreateRevisionVariantRequest;
import com.noveogroup.clap.model.revision.ApplicationFile;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
import com.noveogroup.clap.model.user.User;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Mikhail Demidov
 */
public interface RevisionService {

    /**
     * @param request
     * @return true if new created
     */
    boolean createRevisionVariant(@NotNull CreateRevisionVariantRequest request);

    void updateRevisionData(@NotNull Revision revision);

    ApplicationFile getApplication(Long revId, Long variantId);

    Revision getRevision(Long revisionId);

    void deleteRevision(Long id);

    Set<RevisionType> getAvailableTypesToChange(User user, Revision revision);

    RevisionVariantWithApkStructure getRevisionVariantWithApkStructure(Long variantId);

    RevisionVariantWithApkStructure getRevisionVariantWithApkStructureByMessageId(Long messageId);

    boolean checkRevisionVariantRandom(String variantHash, String random);
}
