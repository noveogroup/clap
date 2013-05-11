package com.noveogroup.clap.dao.impl;

import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.revision.Revision;

import javax.ejb.Stateless;

/**
 * @author Mikhail Demidov
 */
@Stateless
public class RevisionDAOImpl extends GenericHibernateDAOImpl<Revision, Long> implements RevisionDAO {
}
