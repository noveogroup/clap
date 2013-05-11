package com.noveogroup.clap.service.url;

import com.noveogroup.clap.model.revision.RevisionDTO;

public interface UrlService {
    String createUrl(Long revisionId,boolean isMainPackage);
}
