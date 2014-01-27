package com.noveogroup.clap.converter;

import com.noveogroup.clap.entity.BaseEntity;
import com.noveogroup.clap.model.BaseModel;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseConverter {

    /**
     * map common from entity to model
     * @param model
     * @param entity
     * @param <T>
     * @param <V>
     */
    public <T extends BaseModel, V extends BaseEntity> void map(T model,V entity){
        model.setId(entity.getId());
    }

    /**
     * map common from model to entity
     *
     * @param entity
     * @param model
     * @param <T>
     * @param <V>
     */
    public <T extends BaseModel, V extends BaseEntity> void map(V entity,T model){
        entity.setId(model.getId());
    }
}
