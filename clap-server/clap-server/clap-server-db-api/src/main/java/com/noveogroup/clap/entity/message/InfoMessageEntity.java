package com.noveogroup.clap.entity.message;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Andrey Sokolov
 */
@Entity(name = "InfoMessageEntity")
@DiscriminatorValue("info")
public class InfoMessageEntity extends BaseMessageEntity {
    @Override
    public Class<? extends BaseMessageEntity> getEntityType() {
        return InfoMessageEntity.class;
    }
}
