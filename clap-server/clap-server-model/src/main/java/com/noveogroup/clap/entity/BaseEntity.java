package com.noveogroup.clap.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author mdemidov
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 6726259138874256564L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    /**
     * Constructor
     */
    protected BaseEntity() {
    }


    /**
     * Sets new id.
     *
     * @param id New value of id.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return Value of id.
     */
    public Long getId() {
        return id;
    }
}
