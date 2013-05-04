package com.noveogroup.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: mihaildemidoff
 * Date: 4/5/13
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    private static final long serialVersionUID = 8306757495649843962L;

}
