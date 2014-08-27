package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntityListener;

/**
 * @author Andrey Sokolov
 */
public class RevisionVariantEntityListener extends BaseEntityListener<RevisionVariantEntityListenerDelegate,
        RevisionVariantEntity> {


    @Override
    protected Class<RevisionVariantEntityListenerDelegate> getDelegateClass() {
        return RevisionVariantEntityListenerDelegate.class;
    }
}
