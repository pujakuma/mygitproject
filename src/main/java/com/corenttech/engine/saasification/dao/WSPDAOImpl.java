/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DefaultDAOImpl;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.WSP;
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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author puja
 */
@Repository("wSPDAOImpl")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)

public class WSPDAOImpl extends DefaultDAOImpl<WSP> implements WSPDAO {
    
    @Autowired
    private transient SASFUtil sASFUtil;
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WSPDAOImpl.class);

    @Override
    public List<WSP> getWspList(String id, String accountId) throws ServerError, VerifyError {
        List<WSP> wsps = new ArrayList<WSP>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(WSP.class);
        detachedCriteria.add(Restrictions.ne("status", "trash"));
        if (StringUtility.isNotNullOrEmpty(accountId)) {
            detachedCriteria.add(Restrictions.eq("accountid", accountId));
        }
//        if (StringUtility.isNotNullOrEmpty(resourceid)) {
//            if (StringUtility.isNotNullOrEmpty(accountId) && accountId.equalsIgnoreCase(templateAccount)) {
//                detachedCriteria.add(Restrictions.eq("resourceid", resourceid));
//            } else if (StringUtility.isNullOrEmpty(accountId)) {
//                detachedCriteria.add(Restrictions.eq("resourceid", resourceid));
//            }
//        }
        if (StringUtility.isNotNullOrEmpty(id)) {
            detachedCriteria.add(Restrictions.eq("identity", id));
        }
        try {
            wsps = getHibernateTemplate().findByCriteria(detachedCriteria);
            if (!wsps.isEmpty()) {
                return wsps;
            } else {
                throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
            }
        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
            throw new ServerError(dae.getMessage(), "");
        }

    }

    @Override
    public List<WSP> getWspList(String accountId, int limit, int offset, String order, String filter, Map<String, String> varmap, String id) {
      loggingUtility.logDebug(Constants.MODULE + "getWspList start");
        List<WSP> wsps = new ArrayList<WSP>();
        DetachedCriteria dataCriteria = DetachedCriteria.forClass(WSP.class);
        dataCriteria.add(Restrictions.ne("status", "trash"));
        if (StringUtility.isNotNullOrEmpty(accountId)) {
            dataCriteria.add(Restrictions.eq("accountid", accountId));
        }
        if (!StringUtility.isNullOrEmpty(id)) {
            dataCriteria.add(Restrictions.eq("id", id));
        }
        String resourceid = varmap.get("resourceid");
        
        if (!StringUtility.isNullOrEmpty(resourceid)) {
            dataCriteria.add(Restrictions.eq("resourceid", resourceid));
        }
        if (!StringUtility.isNullOrEmpty(varmap.get("fromresourceid"))){
            dataCriteria.add(Restrictions.eq("fromresourceid", varmap.get("fromresourceid")));
        }
        if (!StringUtility.isNullOrEmpty(varmap.get("nodeid"))){
            dataCriteria.add(Restrictions.eq("nodeid", varmap.get("nodeid")));
        }
        if (limit > 0 && offset >= 0) {
            wsps = getHibernateTemplate().findByCriteria(dataCriteria, offset, limit);
        } else {
            wsps = getHibernateTemplate().findByCriteria(dataCriteria);
        }
        if (!StringUtility.isNullOrEmpty(filter)) {
            try {
                FilterQueryBuilder builderFilterQuery = new FilterQueryBuilder();
                FilterQuery query = builderFilterQuery.buildQuery(filter);
                HibernateCriteriaBuilder.buildCriteria(query, dataCriteria);
            } catch (ParserException ex) {
                loggingUtility.logException(ex);
            }
        }
        wsps = getHibernateTemplate().findByCriteria(dataCriteria);
        if (!wsps.isEmpty()) {
            loggingUtility.logDebug(Constants.MODULE + "getWspList End");
            return wsps;
        } else {
            return wsps;
    }   
    }

    @Override
    public List<WSP> getWspDetailsWithResource(String resourceid, String id, String accountid) throws ServerError {
        List<WSP> wspList = new ArrayList<>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(WSP.class);
        if(StringUtility.isNotNullOrEmpty(accountid)){
        detachedCriteria.add(Restrictions.eq("resourceid", resourceid));
        }
        if(StringUtility.isNotNullOrEmpty(resourceid)){
        detachedCriteria.add(Restrictions.eq("identity", id));
        }
        if(StringUtility.isNotNullOrEmpty(accountid)){
        detachedCriteria.add(Restrictions.eq("accountid", accountid));
        }
        try
        {
        wspList = getHibernateTemplate().findByCriteria(detachedCriteria);
        if (!wspList.isEmpty()) {
            return wspList;
        } else {
            throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
        }
        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
            throw new ServerError(dae.getMessage(), "");
        }
    }
    
    
}
