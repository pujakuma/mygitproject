/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SaaSifyUtil;
import com.corenttech.engine.saasification.controller.SaaSifyController;
import com.corenttech.engine.saasification.dao.SaaSifyDAO;
import com.corenttech.engine.saasification.model.Saasify;
import com.corenttech.engine.utility.DateUtility;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Shagul Hameed S
 */
@Service("saaSifyManagerImpl")
public class SaaSifyManagerImpl implements SaaSifyManager {

    LoggingUtility log = LoggingUtility.getInstance(SaaSifyController.class);
    @Autowired
    SaaSifyDAO saaSifyDAO;
    @Autowired
    SaaSifyUtil saaSifyUtil;

    @Override
    public void createSaasify(Saasify SaasifyPojo) throws ClientError, ServerError {
        log.logDebug("::::: createSaasify method started :::::");
        if (null != SaasifyPojo) {
            SaasifyPojo.setCreatedDate(DateUtility.getCurrentDate());
            SaasifyPojo.setStatus(Saasify.Status.INPROGRESS);
            try {
//                saaSifyUtil.amTaskCreate(SaasifyPojo);
                saaSifyDAO.create(SaasifyPojo);
            } catch (ServerError ex) {
                log.logException(ex);
                log.logError("Exception in createSaasify - ManagerImpl" + ex.getMessage());
                throw new ServerError(ex.getMessage(), ex.getCode());
            }
        }
        log.logDebug("::::: createSaasify method end:::::");
    }

    @Override
    public String getSaaSifyListxml(Map<String, String> saasifyMap) throws ClientError, ServerError {
        log.logDebug("::::: getSaaSifyListxml method started :::::");
        StringBuilder sb = new StringBuilder();
        try {
            log.logDebug("accountID ::::" + saasifyMap.get("account"));
            log.logDebug("QueryFilter ::::" + saasifyMap.get("q"));
            List<Saasify> saasifyPojoList = saaSifyDAO.getSaasifyList(saasifyMap);
            sb.append("<saasifys totalrecords=\"").append(saasifyMap.get("size")).append("\" limit=\"").append(saasifyMap.get("limit")).append("\" offset=\"").append(saasifyMap.get("offset")).append("\">");
            for (Saasify saasify : saasifyPojoList) {
                saaSifyUtil.frameSaasifyPojo(saasify, sb);
            }
            sb.append("</saasifys>");
        } catch (ServerError ex) {
            log.logException(ex);
            log.logError("Exception in createSaasify - ManagerImpl" + ex.getMessage());
            throw new ServerError(ex.getMessage(), "SFY100004");
        }
        log.logDebug("::::: Final response in getSaaSifyListxml :::::" + sb.toString());
        log.logDebug("::::: getSaaSifyListxml method end :::::");
        return sb.toString();
    }

    @Override
    public void updateSaasifyRecord(Map<String, String> saasifyMap, Saasify saasify) throws ClientError, ServerError {
        log.logDebug("::::: updateSaasifyRecord method started :::::");
        log.logDebug("::::: updateSaasifyRecord records :::::" + saasifyMap);
        saasify = saaSifyDAO.getSaasifyRecord(saasifyMap);
        if (null != saasify) {
            if (saasify.getIsCallBack().toString().equalsIgnoreCase("true")) {
                saaSifyUtil.updateCallBackUrl(saasify);
            }
            switch (saasifyMap.get("status").toLowerCase()) {
                case "active":
                    saasify.setStatus(Saasify.Status.ACTIVE);
                    break;
                case "inactive":
                    saasify.setStatus(Saasify.Status.INACTIVE);
                    break;
                case "success":
                    saasify.setStatus(Saasify.Status.SUCCESS);
                    break;
                case "inprogress":
                    saasify.setStatus(Saasify.Status.INPROGRESS);
                    break;
                case "failed":
                    saasify.setStatus(Saasify.Status.FAILED);
                    break;
                case "trash":
                    saasify.setStatus(Saasify.Status.TRASH);
                    break;
                default:
                    throw new ClientError("Saasify Status is invalid", "SFY100005");
            }
            if (StringUtility.isNotNullOrEmpty(saasifyMap.get("message"))) {
                saasify.setMessage(saasifyMap.get("message"));
            }
            saasify.setUpdatedDate(DateUtility.getCurrentDate());
            saaSifyDAO.update(saasify);
        } else {
            throw new ClientError("Given invalid id", "SFY100006");
        }

        log.logDebug("::::: updateSaasifyRecord method end :::::");
    }

    @Override
    public void deleteSaasifyRecord(Map<String, String> saasifyMap) throws ClientError, ServerError {
        log.logDebug("::::: updateSaasifyRecord method started :::::");
        log.logDebug("::::: updateSaasifyRecord records :::::" + saasifyMap);
        Saasify saasify = new Saasify();
        saasify = saaSifyDAO.getSaasifyRecord(saasifyMap);
        if (null != saasify) {
            saasify.setStatus(Saasify.Status.TRASH);
            saasify.setDeletedDate(DateUtility.getCurrentDate());
            saaSifyDAO.update(saasify);
        } else {
            throw new ClientError("Given invalid id", "SFY100007");
        }

        log.logDebug("::::: updateSaasifyRecord method end :::::");
    }

}
