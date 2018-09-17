/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.controller;

import com.corenttech.engine.annotation.support.ResponseHolder;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SaaSifyUtil;
import com.corenttech.engine.saasification.manager.SaaSifyManager;
import com.corenttech.engine.saasification.model.Saasify;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

/**
 *
 * @author Shagul Hameed S
 */
@Service("saaSifyController")
public class SaaSifyController {

    LoggingUtility log = LoggingUtility.getInstance(SaaSifyController.class);

    @Autowired
    SaaSifyManager saaSifyManager;

    @Autowired
    SaaSifyUtil saaSifyUtil;

    public ResponseHolder createSaaSify(Map<String, String> saasifyMap, ResponseHolder response) {
        log.logDebug("<----- createSaaSify controller start ----->");
        log.logDebug("createSaaSify Entry saasifyMap" + saasifyMap);
        String accountid = saasifyMap.get("account");
        String body = saasifyMap.get("body");
        String message = "";
        try {
            if (StringUtility.isNullOrEmpty(accountid)) {
                throw new ClientError("Account Id shouldn't be empty", "SFY100001");
            }
            Saasify SaasifyPojo = saaSifyUtil.createSaaSifypojo(saasifyMap);
            saaSifyManager.createSaasify(SaasifyPojo);
            message = saaSifyUtil.createSuccessResponse(SaasifyPojo);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ServerError e) {
            message = saaSifyUtil.parseServerException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        } catch (ClientError ce) {
            message = saaSifyUtil.parseClientException(ce);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            message = saaSifyUtil.parseException(e, "SFY100002");
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        }
        log.logDebug("createSaaSify Response" + response.getResponseXml());
        log.logDebug("createSaaSify getstatus code " + response.getStatusCode());
        log.logDebug("<----- createSaaSify controller end ----->");
        return response;
    }

    public ResponseHolder updateSaaSifyRecord(Map<String, String> saasifyRecords, ResponseHolder response) {
        log.logDebug("updateSaaSifyRecord Entry saasifyRecords :::: " + saasifyRecords);
        Saasify saasify = new Saasify();
        String account = saasifyRecords.get("account");
        String saasifyId = saasifyRecords.get("id");
        String body = saasifyRecords.get("body");
        log.logDebug("accountId :::: " + account);
        log.logDebug("body :::: " + body);
        log.logDebug("saasifyId record id :::::" + saasifyId);
        String message = "";
        try {
            if (StringUtility.isNullOrEmpty(account) || StringUtility.isNullOrEmpty(saasifyId)) {
                throw new ClientError("AccountId/SaasifyId shouldn't be empty", "SFY100003");
            }
            saaSifyUtil.parseInputXml(saasifyRecords);
            saaSifyManager.updateSaasifyRecord(saasifyRecords, saasify);
            message = saaSifyUtil.updateSuccessResponse();
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ServerError e) {
            message = saaSifyUtil.parseServerException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        } catch (ClientError ce) {
            message = saaSifyUtil.parseClientException(ce);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            message = saaSifyUtil.parseException(e, "SFY100002");
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        }
        log.logDebug("updateSaaSDensity Exit response" + response.getResponseXml());
        log.logDebug("updateSaaSDensity Exit response getStatusCode" + response.getStatusCode());
        return response;
    }

    public ResponseHolder getSaaSifyList(Map<String, String> saasifyMap, ResponseHolder response) {
        log.logDebug("<----- getSaaSDensity controller start ----->");
        String message = "";
        try {
            log.logDebug("getSaaSifyList saasifyMap :::: " + saasifyMap);
            message = saaSifyManager.getSaaSifyListxml(saasifyMap);
            message = saaSifyUtil.updateSuccessResponse(message);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ClientError ex) {
            message = saaSifyUtil.parseClientException(ex);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (ServerError ex) {
            message = saaSifyUtil.parseServerException(ex);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        }
        log.logDebug("getSaaSDensity end response" + response.getResponseXml());
        log.logDebug("getSaaSDensity end response getStatusCode" + response.getStatusCode());
        log.logDebug("<-----getSaaSDensity controller end----->");
        return response;
    }

    public ResponseHolder deleteSaaSifyRecord(Map<String, String> saasifyRecords, ResponseHolder response) {
        log.logDebug("deleteSaaSifyRecord Entry saasifyRecords :::: " + saasifyRecords);
        String account = saasifyRecords.get("account");
        String saasifyId = saasifyRecords.get("id");
        log.logDebug("accountId :::: " + account);
        log.logDebug("saasifyId record id :::::" + saasifyId);
        String message = "";
        try {
            if (StringUtility.isNullOrEmpty(account) || StringUtility.isNullOrEmpty(saasifyId)) {
                throw new ClientError("AccountId/SaasifyId shouldn't be empty", "SFY100003");
            }
            saaSifyManager.deleteSaasifyRecord(saasifyRecords);
            message = saaSifyUtil.updateSuccessResponse();
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ServerError e) {
            message = saaSifyUtil.parseServerException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        } catch (ClientError ce) {
            message = saaSifyUtil.parseClientException(ce);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (Exception e) {
            message = saaSifyUtil.parseException(e, "SFY100002");
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        }
        log.logDebug("deleteSaaSifyRecord Exit response" + response.getResponseXml());
        log.logDebug("updateSaaSDensity Exit response getStatusCode" + response.getStatusCode());
        return response;
    }

}
