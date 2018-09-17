/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.saasification.config.Constants;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.corenttech.engine.saasification.model.WorkloadGroup;
import com.corenttech.engine.core.DefaultDAOImpl;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.config.WorkloadUtil;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.queryfilter.FilterQuery;
import com.corenttech.engine.utility.queryfilter.FilterQueryBuilder;
import com.corenttech.engine.utility.queryfilter.db.HibernateCriteriaBuilder;
import com.corenttech.engine.utility.queryfilter.exception.ParserException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saran Kumar
 */
@Service("workloadGroupDAOImpl")
public class WorkloadGroupDAOImpl extends DefaultDAOImpl<WorkloadGroup> implements WorkloadGroupDAO {

    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WorkloadGroupDAOImpl.class);
    @Autowired
    private SASFUtil sASFUtil;

    public List<WorkloadGroup> getWorkloadGroupList(String accountGuid, int limit, int offset, String order, String filter, Map<String, String> varMap, String resourceId) throws ServerError, ClientError {
        loggingUtility.logDebug(Constants.MODULE + "getRegisterAgent");
        List<WorkloadGroup> WorkloadGroup = new ArrayList<WorkloadGroup>();
        DetachedCriteria dataCriteria = DetachedCriteria.forClass(WorkloadGroup.class);
        if (StringUtility.isNotNullOrEmpty(accountGuid)) {
            dataCriteria.add(Restrictions.eq("accountId", accountGuid));
        }
        if (!StringUtility.isNullOrEmpty(resourceId)) {
            dataCriteria.add(Restrictions.eq("resourceId", resourceId));
        }
        dataCriteria.add(Restrictions.ne("status", Constants.TRASH));
        if (limit > 0 && offset >= 0) {
            WorkloadGroup = getHibernateTemplate().findByCriteria(dataCriteria, offset, limit);
        } else {
            WorkloadGroup = getHibernateTemplate().findByCriteria(dataCriteria);
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
        if (!StringUtility.isNullOrEmpty(order)) {
            Order filedOrder = sASFUtil.getOrder(order);
            if (filedOrder != null) {
                dataCriteria.addOrder(filedOrder);
            }
        }
        WorkloadGroup = getHibernateTemplate().findByCriteria(dataCriteria);
        if (!WorkloadGroup.isEmpty()) {
            loggingUtility.logDebug(Constants.MODULE + "getRegisterAgent End");
            return WorkloadGroup;
        } else {
            return WorkloadGroup;
        }
    }
    
    @Override
    public List<WorkloadGroup> getWorkloadGroupList(Map<String,String >workloadMap) {
        List<WorkloadGroup> workloadGroupsList = new ArrayList<WorkloadGroup>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(WorkloadGroup.class);
        String accountId = workloadMap.get("account");
        String resourceId = workloadMap.get("resourceid");
        String resourceType = workloadMap.get("resourcetype");
        String id = workloadMap.get("id");
        if (StringUtility.isNotNullOrEmpty(id)) {
            detachedCriteria.add(Restrictions.eq("id", Integer.valueOf(id)));
        }
        if (StringUtility.isNotNullOrEmpty(accountId)) {
            detachedCriteria.add(Restrictions.eq("accountId", accountId));
        }
        if (StringUtility.isNotNullOrEmpty(resourceId)) {
            detachedCriteria.add(Restrictions.eq("resourceId", resourceId));
        }
        if (StringUtility.isNotNullOrEmpty(resourceType)) {
            detachedCriteria.add(Restrictions.eq("resourcetype", resourceType));
        }
        detachedCriteria.add(Restrictions.ne("status", Constants.TRASH));
        workloadGroupsList = getHibernateTemplate().findByCriteria(detachedCriteria);
        if (!workloadGroupsList.isEmpty()) {
            return workloadGroupsList;
        } else {
            return Collections.emptyList();
        }
        

    }

    @Override
    public WorkloadGroup getWorkloadGroupWithId(String workloadGroupId) {
        List<WorkloadGroup> workloadGroupsList = new ArrayList<WorkloadGroup>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(WorkloadGroup.class);
        if (StringUtility.isNotNullOrEmpty(workloadGroupId)) {
            detachedCriteria.add(Restrictions.eq("id", Integer.valueOf(workloadGroupId)));
        }
        workloadGroupsList = getHibernateTemplate().findByCriteria(detachedCriteria);
        if (!workloadGroupsList.isEmpty()) {
            return workloadGroupsList.get(0);
        } else {
            return null;
        }
        
    }
    

}
