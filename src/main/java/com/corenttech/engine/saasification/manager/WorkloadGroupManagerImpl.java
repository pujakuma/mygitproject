/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.dao.WorkloadGroupDAO;
import com.corenttech.engine.saasification.model.WorkloadGroup;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.config.WorkloadUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author optima
 */
@Service("workloadGroupManagerImpl")
public class WorkloadGroupManagerImpl implements WorkloadGroupManager {

    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WorkloadGroupManagerImpl.class);
    LoggingUtility log = LoggingUtility.getInstance(WorkloadGroupManagerImpl.class);

    @Autowired
    WorkloadGroupDAO workloadGroupDAO;
    @Autowired
    private transient WorkloadUtil workloadUtil;
    @Autowired
    private transient SASFUtil sASFUtil;

    @Override
    public String createWorkloadGroup(List<WorkloadGroup> workloadlist) throws ServerError, ClientError {
        loggingUtility.logDebug(Constants.MODULE + "<!--WorkloadGroup createWorkloadGroup() start --!>");
        String MESSAGE = "";

        if (null != workloadlist) {
            try {
                for (WorkloadGroup workloadGroup : workloadlist) {
                    workloadGroupDAO.create(workloadGroup);
                }
                if (workloadlist.size() > 1) {
                    MESSAGE = workloadUtil.createSuccessResponse();
                } else {
                    WorkloadGroup workloadGroups = workloadlist.get(0);
                    MESSAGE = workloadUtil.createSingleSuccessResponse(workloadGroups);
                }
            } catch (ServerError ex) {
                log.logException(ex);
                log.logError("Exception in createWorkloadGroup - ManagerImpl" + ex.getMessage() + ex.getCause().toString());
                throw new ServerError(ex.getMessage(), ex.getCode());
            }
        } else {
            throw new ClientError("Could Not Create Workload Group", SaasificationConsts.UNABLETO_CREATE);
        }
        loggingUtility.logDebug(Constants.MODULE + "<!--WorkloadGroup createWorkloadGroup() end --!>");
        return MESSAGE;
    }

    @Override
    public String getWorkloadGroup(Map<String, String> varmap) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "WorkloadGroupManagerImpl getWorkloadGroup() start");
        try {
            StringBuilder builder = new StringBuilder();
            String getWorkload = "";
            int limit = 0;
            int offset = 0;
            String filter = "";
            String order = "";
            String accountId = varmap.get("account");
            String resourceId = varmap.get("resourceid");
            if (varmap.containsKey("limit") && varmap.containsKey("offset")) {
                limit = Integer.parseInt(varmap.get("limit").equalsIgnoreCase("") ? "10" : varmap.get("limit"));
                offset = Integer.parseInt(varmap.get("offset").equalsIgnoreCase("") ? "0" : varmap.get("offset"));
            }
            if (varmap.containsKey("order") && !varmap.get("offset").isEmpty()) {
                order = URLDecoder.decode(varmap.get("order"));
            }
            if (varmap.containsKey("q") && !varmap.get("q").isEmpty()) {
                filter = URLDecoder.decode(varmap.get("q"));
            }
            loggingUtility.logDebug(Constants.MODULE + "WorkloadGroupManagerImpl getWorkloadGroup() accountId " + accountId);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadGroupManagerImpl getWorkloadGroup() resourceId " + resourceId);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadGroupManagerImpl getWorkloadGroup() limit " + limit);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadGroupManagerImpl getWorkloadGroup() offset " + offset);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadGroupManagerImpl getWorkloadGroup() order " + order);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadGroupManagerImpl getWorkloadGroup() filter " + filter);
            List<WorkloadGroup> workloadList = workloadGroupDAO.getWorkloadGroupList(accountId, limit, offset, order, filter, varmap, resourceId);
            if (!workloadList.isEmpty()) {
                builder.append(Constants.XML_HEADER);
                builder.append(Constants.GET_SUCCESS);
                builder.append("<workloadgroup totalrecords=\"").append(workloadList.size()).append("\" limit=\"").append(limit).append("\" offset=\"").append(offset).append("\">");
                for (WorkloadGroup workloadgroupdetails : workloadList) {
                    builder.append("<workload id=\"").append(workloadgroupdetails.getId()).append("\" name=\"").append(StringUtility.isNotNullOrEmpty(workloadgroupdetails.getName()) ? workloadgroupdetails.getName() : "").append("\" resourceid=\"").append(workloadgroupdetails.getResourceId()).append("\" nodeid=\"").append(StringUtility.isNotNullOrEmpty(workloadgroupdetails.getNodeId()) ? workloadgroupdetails.getNodeId() : "").append("\">");
                    String type = workloadgroupdetails.getType();
                    if (StringUtility.isNotNullOrEmpty(type)) {
                        builder.append("<type>").append(type).append("</type>");
                    }
                    String issharable = "";
                    if (StringUtility.isNotNullOrEmpty(issharable)) {
                        builder.append("<issharable>").append(workloadgroupdetails.getIssharable()).append("</issharable>");
                    }
                    String version = workloadgroupdetails.getVersion();
                    if (StringUtility.isNotNullOrEmpty(version)) {
                        builder.append("<version>").append(version).append("</version>");
                    }
                    builder.append("<appversion>").append(workloadgroupdetails.getAppversion()).append("</appversion>");

                    builder.append("<resourcetype>").append(workloadgroupdetails.getResourcetype()).append("</resourcetype>");
                    builder.append("<status>").append(workloadgroupdetails.getStatus()).append("</status>");
                    String metadata = workloadgroupdetails.getMetaData();
                    if (StringUtility.isNotNullOrEmpty(metadata)) {
                        if (StringUtility.isNotNullOrEmpty(workloadgroupdetails.getResourcetype()) && (workloadgroupdetails.getResourcetype().equalsIgnoreCase("SCAN") || workloadgroupdetails.getResourcetype().equalsIgnoreCase("SHAREABILITY") || workloadgroupdetails.getResourcetype().equalsIgnoreCase("COSTREPORT"))) {
                            builder.append("<metadata> <![CDATA[").append(metadata).append("]]></metadata>");
                        } else {
                            builder.append("<metadata>").append(metadata).append("</metadata>");
                        }
                    }
                    String properties = workloadgroupdetails.getProperties();
                    if (StringUtility.isNotNullOrEmpty(properties)) {
                        builder.append(properties);
                    } else {
                        builder.append("<properties/>");
                    }
                    String connection = workloadgroupdetails.getConnections();
                    if (StringUtility.isNotNullOrEmpty(connection)) {
                        builder.append(connection);
                    } else {
                        builder.append("<connection/>");
                    }
                    String firewall = workloadgroupdetails.getFirewall();
                    if (StringUtility.isNotNullOrEmpty(firewall)) {
                        builder.append(firewall);
                    } else {
                        builder.append("<firewall/>");
                    }

                    String device = workloadgroupdetails.getDevices();
                    if (StringUtility.isNotNullOrEmpty(device)) {
                        builder.append(device);
                    } else {
                        builder.append("<device/>");
                    }
                    builder.append("</workload>");
                }
                builder.append("</workloadgroup>");
                builder.append(Constants.END_RESPONSE);
                getWorkload = builder.toString();
                return getWorkload;
            } else {
                return sASFUtil.getNoRecoredFrame("workloadgroups");
            }
        } catch (ClientError ex) {
            throw new ClientError(ex.getMessage(), ex.getCode());
        } catch (ServerError | NumberFormatException ex) {
            loggingUtility.logError("Exception in getWorkloadGroup ::::" + ex.getMessage());
            loggingUtility.logException(ex);
            throw new ServerError(ex.getCause().toString(), SaasificationConsts.DB_ERROR);
        }
    }

    @Override
    public void updateWorkloadGroup(Map<String, String> workloadMap, WorkloadGroup workloadGroup) throws ServerError, ClientError {
        List<WorkloadGroup> workloadList = workloadGroupDAO.getWorkloadGroupList(workloadMap);
        WorkloadGroup oldWorkloadGroup = new WorkloadGroup();
        if (workloadList != null && workloadList.size() > 0) {
            oldWorkloadGroup = workloadList.get(0);
            if (oldWorkloadGroup != null) {
                if (StringUtility.isNotNullOrEmpty(workloadGroup.getStatus())) {
                    oldWorkloadGroup.setStatus(workloadGroup.getStatus());
                }
                if (StringUtility.isNotNullOrEmpty(workloadGroup.getMetaData())) {
                    oldWorkloadGroup.setMetaData(workloadGroup.getMetaData());
                }
                if (StringUtility.isNotNullOrEmpty(workloadGroup.getAppversion())) {
                    oldWorkloadGroup.setAppversion(workloadGroup.getAppversion());
                }
                if (StringUtility.isNotNullOrEmpty(workloadGroup.getConnections())) {
                    oldWorkloadGroup.setConnections(workloadGroup.getConnections());
                }
                if(StringUtility.isNotNullOrEmpty(workloadGroup.getFirewall())){
                    oldWorkloadGroup.setFirewall(workloadGroup.getFirewall());
                }
                if(StringUtility.isNotNullOrEmpty(workloadGroup.getIssharable())){
                    oldWorkloadGroup.setIssharable(workloadGroup.getIssharable());
                }
                if(StringUtility.isNotNullOrEmpty(workloadGroup.getProperties())){
                    oldWorkloadGroup.setProperties(workloadGroup.getProperties());
                }
                if(StringUtility.isNotNullOrEmpty(workloadGroup.getName())){
                    oldWorkloadGroup.setName(workloadGroup.getName());
                }
                if(StringUtility.isNotNullOrEmpty(workloadGroup.getType())){
                    oldWorkloadGroup.setType(workloadGroup.getType());
                }
                if(StringUtility.isNotNullOrEmpty(workloadGroup.getVersion())){
                    oldWorkloadGroup.setVersion(workloadGroup.getVersion());
                }
                oldWorkloadGroup.setUpdatedDate(new Date());
                workloadGroupDAO.update(oldWorkloadGroup);
            } else {
                throw new ClientError("could not update the WorkloadGroup", "WKL00001");
            }
        } else {
            throw new ClientError("the WorkloadGroup cannot be updated,no record found", "WKL00002");

        }
    }  
    
    @Override
    public void deleteWorkloadGroup(String workloadId) throws ServerError, ClientError {
        WorkloadGroup workloadGroupObj = workloadGroupDAO.getWorkloadGroupWithId(workloadId);
        if (workloadGroupObj != null) {
            workloadGroupObj.setStatus(Constants.TRASH);
            workloadGroupObj.setDeletedDate(new Date());
            workloadGroupDAO.update(workloadGroupObj);
        } else {
            throw new ClientError("The WorkloadGroup cannot be deleted,no record found", "WKL00003");
        }
    }  

}
