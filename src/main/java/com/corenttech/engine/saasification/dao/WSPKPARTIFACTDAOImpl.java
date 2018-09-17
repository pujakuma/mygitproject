/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DefaultDAOImpl;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.saasification.model.KbArtifacts;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.queryfilter.FilterQuery;
import com.corenttech.engine.utility.queryfilter.FilterQueryBuilder;
import com.corenttech.engine.utility.queryfilter.db.HibernateCriteriaBuilder;
import com.corenttech.engine.utility.queryfilter.exception.ParserException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saravanan J
 */
@Repository("wSPKPARTIFACTDAOImpl")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class WSPKPARTIFACTDAOImpl extends DefaultDAOImpl<KbArtifacts> implements WSPKPARTIFACTDAO {

    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WSPKPARTIFACTDAOImpl.class);

    @Override
    public List<KbArtifacts> getKpList(String id, String guid, String os, int limit, int offset, String order, String filter) throws ServerError, ClientError {
        loggingUtility.logDebug("WSPKPARTIFACTDAOImpl getKpList Entry >>>>>>");
        List<KbArtifacts> artifactses = new ArrayList<KbArtifacts>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(KbArtifacts.class);
        if (StringUtility.isNotNullOrEmpty(id)) {
            detachedCriteria.add(Restrictions.eq("id", id));
        }
        if (StringUtility.isNotNullOrEmpty(id)) {
            detachedCriteria.add(Restrictions.eq("os", os));
        }
        if (StringUtility.isNotNullOrEmpty(id)) {
            detachedCriteria.add(Restrictions.eq("guid", guid));
        }
        if (limit > 0 && offset >= 0) {
            artifactses = getHibernateTemplate().findByCriteria(detachedCriteria, offset, limit);
        } else {
            artifactses = getHibernateTemplate().findByCriteria(detachedCriteria);
        }
        if (!StringUtility.isNullOrEmpty(filter)) {
            try {
                FilterQueryBuilder builderFilterQuery = new FilterQueryBuilder();
                FilterQuery query = builderFilterQuery.buildQuery(filter);
                HibernateCriteriaBuilder.buildCriteria(query, detachedCriteria);
            } catch (ParserException ex) {
                loggingUtility.logException(ex);
            }
            artifactses = getHibernateTemplate().findByCriteria(detachedCriteria);
        }
        loggingUtility.logDebug("WSPKPARTIFACTDAOImpl getKpList Exit >>>>>>");
        return artifactses;
    }

    @Override
    public KbArtifacts getKpListWithversion(String versionid, String name) throws ServerError, ClientError {
        loggingUtility.logDebug("WSPKPARTIFACTDAOImpl getKpListWithversion Entry >>>>>>");
        List<KbArtifacts> artifactses = new ArrayList<KbArtifacts>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(KbArtifacts.class);
        if (StringUtility.isNotNullOrEmpty(versionid)) {
            detachedCriteria.add(Restrictions.eq("version", versionid));
        }
        if (StringUtility.isNotNullOrEmpty(name)) {
            detachedCriteria.add(Restrictions.eq("workloadname", name));
        }
        artifactses = getHibernateTemplate().findByCriteria(detachedCriteria);
        loggingUtility.logDebug("WSPKPARTIFACTDAOImpl getKpListWithversion Exit >>>>>>");
        return artifactses.get(0);

    }
}
