/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.controller;

import com.corenttech.engine.annotation.support.ResponseHolder;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.WorkloadUtil;
import com.corenttech.engine.saasification.manager.WorkloadGroupManager;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.WorkloadGroup;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saran Kumar
 */
@Service("workloadGroupController")
public class WorkloadGroupController {

    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WorkloadGroupController.class);

    @Autowired
    private transient WorkloadUtil workloadUtil;
    @Autowired
    private transient WorkloadGroupManager workloadGroupManager;
    @Autowired
    private transient SASFUtil sASFUtil;

    public ResponseHolder createWorkloadGroup(Map<String, String> varMap, ResponseHolder response) {
        loggingUtility.logDebug(Constants.MODULE + "<!--WorkloadGroupController createWorkloadGroup() start --!>");
        String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "varmap : " + varMap);
        try {
            List<WorkloadGroup> workloadlist = new ArrayList<>();
            //WorkloadGroup workloadGroup=new WorkloadGroup();
            workloadlist = workloadUtil.createWorkloadgroupPojo(varMap);
            MESSAGE = workloadGroupManager.createWorkloadGroup(workloadlist);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ClientError clientError) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            MESSAGE = sASFUtil.getErrorMessage(clientError.getCode(), Boolean.TRUE);
            loggingUtility.logException(clientError);
        } catch (ServerError serverError) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            MESSAGE = sASFUtil.parseServerException(serverError);
            loggingUtility.logException(serverError);
        } catch (Exception exception) {
            MESSAGE = sASFUtil.parseException(new ServerError(exception.getMessage(), "SAAS001000"));
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            loggingUtility.logException(exception);
        }
        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[createAvmDeploy] : " + MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!--WorkloadGroupController createWorkloadGroup() End --!>");
        response.setResponseXml(MESSAGE);
        return response;
    }

    public ResponseHolder updateWorkloadGroup(Map<String, String> varmap, ResponseHolder response) {
        String MESSAGE = "";
        String accountId = varmap.get("account");
        String workloadId = varmap.get("id");
        String body = varmap.get("body");
        String resourceId = varmap.get("resourceid");
        String resourceType = varmap.get("resourcetype");
        loggingUtility.logDebug(Constants.MODULE + "<!------ Update WorkLoadGroup ------!>");
        loggingUtility.logDebug(Constants.MODULE + "ResourceId      : " + resourceId);
        loggingUtility.logDebug(Constants.MODULE + "AccountId : " + accountId);
        loggingUtility.logDebug(Constants.MODULE + "Body : " + body);
        loggingUtility.logDebug(Constants.MODULE + "Type : " + resourceType);
        loggingUtility.logDebug(Constants.MODULE + "<!------ Update WorkLoadGroup ------!>");
        WorkloadGroup workloadGroup = new WorkloadGroup();
        try {
            workloadGroup.setAccountId(accountId);
            if (StringUtility.isNotNullOrEmpty(workloadId)) {
                List<WorkloadGroup> createWorkloadgroupPojo = workloadUtil.createWorkloadgroupPojo(varmap);
                if (!createWorkloadgroupPojo.isEmpty()) {
                    workloadGroupManager.updateWorkloadGroup(varmap, createWorkloadgroupPojo.get(0));
                }
            } else {
                throw new ClientError("Workloadgroup is not found without id","SFY100009");
            }
            if (workloadGroup != null) {
                MESSAGE = sASFUtil.updateSuccessResponse();
                response.setStatusCode(HttpStatus.OK.value());
            }
            loggingUtility.logInfo(Constants.MODULE + "<---WorkLoadGroupController--->" + MESSAGE);

        } catch (com.corenttech.engine.core.exception.ServerError serverError) {
            loggingUtility.logException(serverError);
            MESSAGE = sASFUtil.parseServerException(serverError);
        } catch (ClientError clientError) {
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
            MESSAGE = sASFUtil.parseClientException(clientError);

        } catch (Exception ex) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            loggingUtility.logDebug("<<Exeption in ProcessingSpec update>>" + ex.getCause());
            MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.COULDNOT_UPDATE, true);
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[UpdateWorkLoadGroup] : " + MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!--WorkloadGroupController updateWorkloadGroup() End --!>");
        response.setResponseXml(MESSAGE);
        return response;

    }
    
    public ResponseHolder deleteWorkloadGroup(String workloadId, ResponseHolder response) {
        String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "<!------ Delete WorkLoadGroup ------!>");
        loggingUtility.logDebug(Constants.MODULE + "workloadId      : " + workloadId);
        loggingUtility.logDebug(Constants.MODULE + "<!------ Delete WorkLoadGroup ------!>");
        try {
            if (StringUtility.isNotNullOrEmpty(workloadId)) {
                workloadGroupManager.deleteWorkloadGroup(workloadId);
            } else {
                throw new ClientError("Workloadgroup is not found without id", "SFY100009");
            }
            MESSAGE = sASFUtil.updateSuccessResponse();
            response.setStatusCode(HttpStatus.OK.value());
            loggingUtility.logInfo(Constants.MODULE + "<---WorkLoadGroupController--->" + MESSAGE);

        } catch (ServerError serverError) {
            loggingUtility.logException(serverError);
            MESSAGE = sASFUtil.parseServerException(serverError);
        } catch (ClientError clientError) {
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
            MESSAGE = sASFUtil.parseClientException(clientError);

        } catch (Exception ex) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            loggingUtility.logDebug("<<Exeption in ProcessingSpec update>>" + ex.getCause());
            MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_INPUT, true);
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[DeleteWorkLoadGroup] : " + MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!--WorkloadGroupController DeleteWorkloadGroup() End --!>");
        response.setResponseXml(MESSAGE);
        return response;

    }

    public ResponseHolder getWorkloadGroup(Map<String, String> varMap, ResponseHolder response) {
        String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "<!------ getWorkloadGroup start ------!>");
        loggingUtility.logDebug(Constants.MODULE + "varMap      : " + varMap);
        try {
            MESSAGE = workloadGroupManager.getWorkloadGroup(varMap);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ClientError ex) {
            if (!StringUtility.isNullOrEmpty(ex.getCode())) {
                response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
                MESSAGE = sASFUtil.parseClientException(ex);
            } else {
                response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
                MESSAGE = sASFUtil.parseClientException(new ClientError("Could not get the WorkloadGroup", "SAAS0003000"));
            }

        } catch (ServerError ser) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            MESSAGE = sASFUtil.parseServerException(ser);
        } catch (Exception ex) {
            loggingUtility.logException(ex, Constants.MODULE);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            MESSAGE = sASFUtil.parseClientException(new ClientError("Could not get the WorkloadGroup", "SAAS0004000"));
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[updateAvmDeploy] : " + MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getStatusCode()] : " + response.getStatusCode());
        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getResponseXml()] : " + response.getResponseXml());
        response.setResponseXml(MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!------ getWorkloadGroup End ------!>");
        return response;
    }
}
