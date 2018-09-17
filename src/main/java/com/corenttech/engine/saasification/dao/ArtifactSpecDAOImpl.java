/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DefaultDAOImpl;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.queryfilter.FilterQuery;
import com.corenttech.engine.utility.queryfilter.FilterQueryBuilder;
import com.corenttech.engine.utility.queryfilter.db.HibernateCriteriaBuilder;
import com.corenttech.engine.utility.queryfilter.exception.ParserException;
import com.corenttech.tags.tagexception.ErrorCodes;
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
@Repository("artifactSpecDAOImpl")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class ArtifactSpecDAOImpl extends DefaultDAOImpl<ArtifactSpec> implements ArtifactSpecDAO {

    @Autowired
    private transient SASFUtil sASFUtil;
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(ArtifactSpecDAOImpl.class);

    
    @Override
    public List<ArtifactSpec> getArtifactSpecList(String id, String accountid) throws VerifyError, ServerError {
        List<ArtifactSpec> artifactSpecList = new ArrayList<>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ArtifactSpec.class);
        detachedCriteria.add(Restrictions.ne("status", "trash"));
        if (StringUtility.isNotNullOrEmpty(accountid)) {
            detachedCriteria.add(Restrictions.eq("accountid", accountid));
        }
        if (StringUtility.isNotNullOrEmpty(id)) {
            detachedCriteria.add(Restrictions.eq("artifactidentity", id));
        }
        try {
            artifactSpecList = getHibernateTemplate().findByCriteria(detachedCriteria);
            if (!artifactSpecList.isEmpty()) {
                return artifactSpecList;
            } else {
                throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
            }
        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
            throw new ServerError(dae.getMessage(), "");
        }
    }

    @Override
    public List<ArtifactSpec> getArtifactSpecList(String templateAccount, String accountId, int limit, int offset, String order, String filter, Map<String, String> varmap, String id) throws ServerError, ClientError {
        loggingUtility.logDebug(Constants.MODULE + "getArtifactSpecList start");
        List<ArtifactSpec> artifactSpecs = new ArrayList<ArtifactSpec>();
        DetachedCriteria dataCriteria = DetachedCriteria.forClass(ArtifactSpec.class);
        dataCriteria.add(Restrictions.ne("status", "trash"));
        if (StringUtility.isNotNullOrEmpty(varmap.get("accountname"))) {
            loggingUtility.logDebug(Constants.MODULE + "accountname ::: " + varmap.get("accountname") + " accountId ::: " + accountId);
            accountId = sASFUtil.getAccountId(varmap);
            if (StringUtility.isNullOrEmpty(accountId)) {
                varmap.put("account", varmap.get("accountname"));
            } else {
                varmap.put("account", accountId);
                varmap.put("parentid", templateAccount);
            }
        } else if (StringUtility.isNotNullOrEmpty(accountId)) {
            if (StringUtility.isNotNullOrEmpty(templateAccount) && !varmap.get("account").equalsIgnoreCase(templateAccount)) {
                varmap.put("parentid", templateAccount);
            }
        }
        if (StringUtility.isNotNullOrEmpty(accountId)) {
            dataCriteria.add(Restrictions.eq("accountid", accountId));
        }
        if (!StringUtility.isNullOrEmpty(id)) {
            dataCriteria.add(Restrictions.eq("id", id));
        }
        String toversionid = varmap.get("toversionid");
        if (!StringUtility.isNullOrEmpty(toversionid)) {
            dataCriteria.add(Restrictions.eq("toversion", toversionid));
        }
        String action = varmap.get("action");
        if (!StringUtility.isNullOrEmpty(action)) {
            dataCriteria.add(Restrictions.eq("action", action));
        }
        String resourceid = StringUtility.isNotNullOrEmpty(varmap.get("resourceid")) ? varmap.get("resourceid") : varmap.get("deploymentid");

        if (StringUtility.isNotNullOrEmpty(resourceid)) {
            if (StringUtility.isNotNullOrEmpty(accountId) && accountId.equalsIgnoreCase(templateAccount)) {
                dataCriteria.add(Restrictions.eq("resourceid", resourceid));
            } else if (StringUtility.isNullOrEmpty(accountId)) {
                dataCriteria.add(Restrictions.eq("resourceid", resourceid));
            }
        }

        if (!StringUtility.isNullOrEmpty(varmap.get("fromresourceid"))) {
            dataCriteria.add(Restrictions.eq("fromresourceid", varmap.get("fromresourceid")));
        }
        if (!StringUtility.isNullOrEmpty(varmap.get("nodeid"))) {
            dataCriteria.add(Restrictions.eq("nodeid", varmap.get("nodeid")));
        }
        if (limit > 0 && offset >= 0) {
            artifactSpecs = getHibernateTemplate().findByCriteria(dataCriteria, offset, limit);
        } else {
            artifactSpecs = getHibernateTemplate().findByCriteria(dataCriteria);
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
        artifactSpecs = getHibernateTemplate().findByCriteria(dataCriteria);
        if (!artifactSpecs.isEmpty()) {
            loggingUtility.logDebug(Constants.MODULE + "getArtifactSpecList End");
            return artifactSpecs;
        } else {
            return artifactSpecs;
        }
    }

    public FilterQuery getQueryCondition(String colum, String value) throws ServerError {

        loggingUtility.logDebug("getQueryCondition Start..");
        FilterQueryBuilder builderFilterQuery = new FilterQueryBuilder();
        FilterQuery query = null;
        try {
            String jsonQuery =
                    "{ \"fl\" : "
                    + "{"
                    + "\"fc1\": {"
                    + "\"n\" : \"" + colum + "\","
                    + "\"op\": \"EQ\","
                    + "\"vl\" : [\"" + value + "\"]"
                    + "}"
                    + "}"
                    + "}";

            query = builderFilterQuery.buildQuery(jsonQuery);
            loggingUtility.logDebug("getQueryCondition End");
        } catch (ParserException ex) {
            throw new ServerError(ex.getMessage(), ErrorCodes.ERR_UNKOWN);
        }
        return query;
    }

    @Override
    public List<ArtifactSpec> getArtifactDetailsForClone(String accountid, String destinationid, String destinationnodeid) throws ServerError, VerifyError {
        List<ArtifactSpec> artifactSpecList = new ArrayList<>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ArtifactSpec.class);
        if (StringUtility.isNotNullOrEmpty(accountid)) {
            detachedCriteria.add(Restrictions.eq("accountid", accountid));
        }
        if (StringUtility.isNotNullOrEmpty(destinationid)) {
            detachedCriteria.add(Restrictions.eq("nodeid", destinationid));
        }
        if (StringUtility.isNotNullOrEmpty(destinationnodeid)) {
            detachedCriteria.add(Restrictions.eq("resourceid", destinationnodeid));
        }
        try {
            artifactSpecList = getHibernateTemplate().findByCriteria(detachedCriteria);

            return artifactSpecList;
        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
            throw new ServerError(dae.getMessage(), "");
        }
    }

    @Override
    public List<ArtifactSpec> getArtifactDetailsWithResource(String resourceid, String id, String accountid) throws ServerError {
        List<ArtifactSpec> artifactSpecList = new ArrayList<>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ArtifactSpec.class);
        if (StringUtility.isNotNullOrEmpty(accountid)) {
            detachedCriteria.add(Restrictions.eq("resourceid", resourceid));
        }
        if (StringUtility.isNotNullOrEmpty(resourceid)) {
            detachedCriteria.add(Restrictions.eq("artifactidentity", id));
        }
        if (StringUtility.isNotNullOrEmpty(accountid)) {
            detachedCriteria.add(Restrictions.eq("accountid", accountid));
        }
        try {
            artifactSpecList = getHibernateTemplate().findByCriteria(detachedCriteria);
            if (!artifactSpecList.isEmpty()) {
                return artifactSpecList;
            } else {
                throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
            }
        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
            throw new ServerError(dae.getMessage(), "");
        }

    }

    public List<ArtifactSpec> getSourceArtifactSpecList(String id, String parentId, String action) throws VerifyError, ServerError {
        List<ArtifactSpec> artifactSpecList = new ArrayList<>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ArtifactSpec.class);
        if (StringUtility.isNotNullOrEmpty(parentId)) {
            detachedCriteria.add(Restrictions.eq("accountid", parentId));
        }
        if (StringUtility.isNotNullOrEmpty(id)) {
            detachedCriteria.add(Restrictions.eq("artifactidentity", id));
        }
        if (StringUtility.isNotNullOrEmpty(action)) {
            detachedCriteria.add(Restrictions.eq("action", action));
        }
        try {
            artifactSpecList = getHibernateTemplate().findByCriteria(detachedCriteria);

        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
            throw new ServerError(dae.getMessage(), "");
        }
        return artifactSpecList;
    }

    @Override
    public List<ArtifactSpec> getArtifactSpecListAgent(String accountId, String filter, Map<String, String> varmap, String id) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "getArtifactSpecListAgent start");
        List<ArtifactSpec> artifactSpecs = new ArrayList<ArtifactSpec>();
        DetachedCriteria dataCriteria = DetachedCriteria.forClass(ArtifactSpec.class);
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
        if (!StringUtility.isNullOrEmpty(varmap.get("fromresourceid"))) {
            dataCriteria.add(Restrictions.eq("fromresourceid", varmap.get("fromresourceid")));
        }
        if (!StringUtility.isNullOrEmpty(varmap.get("nodeid"))) {
            dataCriteria.add(Restrictions.eq("nodeid", varmap.get("nodeid")));
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
        artifactSpecs = getHibernateTemplate().findByCriteria(dataCriteria);
        if (!artifactSpecs.isEmpty()) {
            loggingUtility.logDebug(Constants.MODULE + "getArtifactSpecListAgent End");
            return artifactSpecs;
        } else {
            return artifactSpecs;
        }
    }
    @Override
    public List<ArtifactSpec> getArtifactSpecList(String id, String accountid,Map<String,String>varMap) throws VerifyError, ServerError {
        List<ArtifactSpec> artifactSpecList = new ArrayList<>();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ArtifactSpec.class);
        detachedCriteria.add(Restrictions.ne("status", "trash"));
        String toversionid=varMap.get("toversionid");
        String resourceid=varMap.get("resourceid");
        String action=varMap.get("action");
        if (StringUtility.isNotNullOrEmpty(accountid)) {
            detachedCriteria.add(Restrictions.eq("accountid", accountid));
        }
        if (StringUtility.isNotNullOrEmpty(id)) {
            detachedCriteria.add(Restrictions.eq("artifactidentity", id));
        }
        if (StringUtility.isNotNullOrEmpty(toversionid)) {
            detachedCriteria.add(Restrictions.eq("toversion", toversionid));
        }
        if (StringUtility.isNotNullOrEmpty(resourceid)) {
            detachedCriteria.add(Restrictions.eq("resourceid", resourceid));
        }
        if (StringUtility.isNotNullOrEmpty(action)) {
            detachedCriteria.add(Restrictions.eq("action", action));
        }
        try {
            artifactSpecList = getHibernateTemplate().findByCriteria(detachedCriteria);
            if (!artifactSpecList.isEmpty()) {
                return artifactSpecList;
            } else {
                throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
            }
        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
            throw new ServerError(dae.getMessage(), "");
        }
    }
}
