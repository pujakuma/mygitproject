/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DefaultDAOImpl;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.NodeSpecUtil;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.NodeSpec;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.queryfilter.FilterQuery;
import com.corenttech.engine.utility.queryfilter.FilterQueryBuilder;
import com.corenttech.engine.utility.queryfilter.db.HibernateCriteriaBuilder;
import com.corenttech.engine.utility.queryfilter.exception.ParserException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Naveen Kumar S
 */
@Repository("nodeSpecDAOImpl")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class NodeSpecDAOImpl extends DefaultDAOImpl<NodeSpec> implements NodeSpecDAO {

    @Autowired
    private transient SASFUtil sASFUtil;
    LoggingUtility loggingUtility = LoggingUtility.getInstance(NodeSpecDAOImpl.class);

    @Override
    public NodeSpec getNodeSpecRecord(String nodeSpecId) throws ServerError, VerifyError {
        List<NodeSpec> nodeSpecRecords = new ArrayList<NodeSpec>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NodeSpec.class);
        if (StringUtility.isNotNullOrEmpty(nodeSpecId)) {
            detachedCriteria.add(Restrictions.eq("id", nodeSpecId));
        }
        try {
            nodeSpecRecords = getHibernateTemplate().findByCriteria(detachedCriteria);
            if (!nodeSpecRecords.isEmpty() && null != nodeSpecRecords.get(0)) {
                return nodeSpecRecords.get(0);
            } else {
                return null;
            }
        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
            throw new ServerError(dae.getMessage(), SaasificationConsts.SERVER_ERROR);
        }
    }

    @Override
    public List<NodeSpec> getNodeSpecList(Map<String, String> searchattributes) throws ServerError {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NodeSpec.class);
        List<NodeSpec> nodeSpecs = new ArrayList<NodeSpec>();
        try {
            String accountid = searchattributes.get("account");
            if (StringUtility.isNotNullOrEmpty(accountid)) {
                detachedCriteria.add(Restrictions.eq("accountid", accountid));
            }
            detachedCriteria.add(Restrictions.ne("status", SaasificationConsts.TRASH));
            String id = searchattributes.get("id");
            if (StringUtility.isNotNullOrEmpty(id)) {
                detachedCriteria.add(Restrictions.eq("id", id));
            }
            int limit = StringUtility.isNotNullOrEmpty(searchattributes.get("limit")) ? Integer.parseInt(searchattributes.get("limit")) : 0;
            int offset = StringUtility.isNotNullOrEmpty(searchattributes.get("offset")) ? Integer.parseInt(searchattributes.get("offset")) : 0;
            nodeSpecs = getHibernateTemplate().findByCriteria(detachedCriteria);
            if (!nodeSpecs.isEmpty()) {
                loggingUtility.logDebug("available record size ::: " + nodeSpecs.size());
                searchattributes.put("size", String.valueOf(nodeSpecs.size()));
            }
            String order = searchattributes.get("order");
            if (!StringUtility.isNullOrEmpty(order)) {
                loggingUtility.logDebug("order ::: " + order);
                Order filedOrder = sASFUtil.getOrder(order);
                if (filedOrder != null) {
                    detachedCriteria.addOrder(filedOrder);
                }
            }
            String filter = searchattributes.get("q");
            if (StringUtility.isNotNullOrEmpty(filter)) {
                FilterQueryBuilder filterQueryBuilder = new FilterQueryBuilder();
                FilterQuery filterQuery = filterQueryBuilder.buildQuery(filter);
                HibernateCriteriaBuilder.buildCriteria(filterQuery, detachedCriteria);
            }

            if (limit > 0 && offset >= 0) {
                loggingUtility.logDebug("limit ::: " + limit + " offset ::: " + offset);
                nodeSpecs = getHibernateTemplate().findByCriteria(detachedCriteria, offset, limit);
            } else {
                nodeSpecs = getHibernateTemplate().findByCriteria(detachedCriteria);
            }

            if (null != nodeSpecs && !nodeSpecs.isEmpty()) {
                loggingUtility.logDebug("filtered nodespec record size ::: " + nodeSpecs.size());
                return nodeSpecs;
            } else {
                return null;
            }
        } catch (ParserException ex) {
            ex.printStackTrace(System.err);
            loggingUtility.logException(ex);
            throw new ServerError(ex.getMessage());
        }
    }
}
