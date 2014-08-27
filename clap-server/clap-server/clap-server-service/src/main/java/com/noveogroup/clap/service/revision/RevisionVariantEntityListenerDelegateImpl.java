package com.noveogroup.clap.service.revision;

import com.noveogroup.clap.entity.revision.RevisionVariantEntity;
import com.noveogroup.clap.entity.revision.RevisionVariantEntityListenerDelegate;
import com.noveogroup.clap.service.file.FileService;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
@Stateless
public class RevisionVariantEntityListenerDelegateImpl implements RevisionVariantEntityListenerDelegate{

    @Inject
    private FileService fileService;

    @Override
    public void postRemove(final RevisionVariantEntity entity) {
        fileService.removeFile(entity.getPackageFileUrl());
    }
}
