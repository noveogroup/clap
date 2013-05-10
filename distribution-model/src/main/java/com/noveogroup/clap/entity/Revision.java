package com.noveogroup.clap.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Mikhail Demidov
 */
@Entity
@Table(name = "revisions")
public class Revision extends BaseEntity {

    /**
     * Constructor
     */
    public Revision() {
    }


}
