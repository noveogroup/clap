package com.noveogroup.clap.entity.revision;

import com.noveogroup.clap.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Mikhail Demidov
 */
@Entity
@Table(name = "revisions")
public class Revision extends BaseEntity {


    private Long timestamp;

    private RevisionType revisionType;


    /**
     * Constructor
     */
    public Revision() {
    }


}
