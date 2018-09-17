/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DefaultDAOImpl;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.Saasify;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.queryfilter.FilterQuery;
import com.corenttech.engine.utility.queryfilter.FilterQueryBuilder;
import com.corenttech.engine.utility.queryfilter.db.HibernateCriteriaBuilder;
import com.corenttech.engine.utility.queryfilter.exception.ParserException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Shagul Hameed S
 */
@Repository("saaSifyDAOImpl")
public class SaaSifyDAOImpl extends DefaultDAOImpl<Saasify> implements SaaSifyDAO {

    LoggingUtility log = LoggingUtility.getInstance(SaaSifyDAOImpl.class);

    @Override
    public List<Saasify> getSaasifyList(Map<String, String> saasifyMap) throws ServerError {
        List<Saasify> saasifyList = new ArrayList<Saasify>();
        int limit = 0;
        int offset = 0;
        DetachedCriteria dc = DetachedCriteria.forClass(Saasify.class);
        if (saasifyMap.containsKey("limit") && saasifyMap.containsKey("offset")) {
            log.logDebug("limit :::: " + saasifyMap.get("limit"));
            log.logDebug("offset :::: " + saasifyMap.get("offset"));
            limit = StringUtility.isNotNullOrEmpty(saasifyMap.get("limit")) ? Integer.parseInt(saasifyMap.get("limit")) : 20;
            offset = StringUtility.isNotNullOrEmpty(saasifyMap.get("offset")) ? Integer.parseInt(saasifyMap.get("offset")) : 0;
            saasifyMap.put("limit", String.valueOf(limit));
            saasifyMap.put("offset", String.valueOf(offset));
        }
        if (StringUtility.isNotNullOrEmpty(saasifyMap.get("account"))) {
            log.logDebug("accountId :::: " + saasifyMap.get("account"));
            dc.add(Restrictions.eq("accountId", saasifyMap.get("account")));
        }
        if (StringUtility.isNotNullOrEmpty(saasifyMap.get("type"))) {
            log.logDebug("type :::: " + saasifyMap.get("type"));
            dc.add(Restrictions.eq("type", saasifyMap.get("type")));
        }
        dc.add(Restrictions.ne("status", Saasify.Status.TRASH));
        if (StringUtility.isNotNullOrEmpty(saasifyMap.get("q"))) {
            try {
                String filter = URLDecoder.decode(saasifyMap.get("q"));
                log.logDebug("queryFilter :::: " + filter);
                FilterQueryBuilder fqb = new FilterQueryBuilder();
                FilterQuery fq = fqb.buildQuery(filter);
                HibernateCriteriaBuilder.buildCriteria(fq, dc);
            } catch (ParserException ex) {
                log.logException(ex);
                log.logError("BuildCriteria Exception in getSaasifyList" + ex.getMessage());
                throw new ServerError(ex.getMessage(), "SFY100008");
            }
        }
        saasifyList = getHibernateTemplate().findByCriteria(dc);
        if (null != saasifyList) {
            log.logDebug("Size of saasifyList :::: " + saasifyList.size());
            saasifyMap.put("size", String.valueOf(saasifyList.size()));
        }
        if (saasifyMap.containsKey("limit") && saasifyMap.containsKey("offset")) {
            saasifyList = getHibernateTemplate().findByCriteria(dc, offset, limit);
        }
        return saasifyList;
    }

    @Override
    public Saasify getSaasifyRecord(Map<String, String> saasifyMap) throws ServerError {
        List<Saasify> saasifyList = new ArrayList<Saasify>();
        DetachedCriteria dc = DetachedCriteria.forClass(Saasify.class);
        if (StringUtility.isNotNullOrEmpty(saasifyMap.get("account"))) {
            log.logDebug("accountId :::: " + saasifyMap.get("account"));
            dc.add(Restrictions.eq("accountId", saasifyMap.get("account")));
        }
        if (StringUtility.isNotNullOrEmpty(saasifyMap.get("id"))) {
            log.logDebug("id :::: " + saasifyMap.get("id"));
            dc.add(Restrictions.eq("id", saasifyMap.get("id")));
        }
        saasifyList = getHibernateTemplate().findByCriteria(dc);
        if (null != saasifyList && saasifyList.size() > 0) {
            return saasifyList.get(0);
        }
        return null;
    }

}
