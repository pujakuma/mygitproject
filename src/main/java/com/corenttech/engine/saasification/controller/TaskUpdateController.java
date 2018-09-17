/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.controller;

import com.corenttech.engine.annotation.support.ResponseHolder;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.config.TaskUpdateUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.manager.TaskUpdateManager;
import com.corenttech.engine.saasification.model.TaskUpdate;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author veyron
 */
@Service("taskUpdateController")
public class TaskUpdateController {

    @Autowired
    private transient TaskUpdateManager taskUpdateManager;
    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient TaskUpdateUtil taskupdateutil;
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(TaskUpdateController.class);
    LoggingUtility log = LoggingUtility.getInstance(ProvisioningDSCController.class);

    public ResponseHolder updateTaskUpdate(Map<String, String> varmap, ResponseHolder response) throws ClientError, ServerError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--TaskUpdateController updateTaskUpdate() start--!>");
        String message = "";
        loggingUtility.logDebug(Constants.MODULE + "varmap : " + varmap);
        try {
            String accountid = varmap.get("account");
            String taskid = varmap.get("taskid");
            String upgrade = varmap.get("upgrade");
            if (StringUtility.isNullOrEmpty(accountid)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_ACCOUNT, true);
                throw new VerifyError(message);
            }
            
            TaskUpdate taskUpdate = new TaskUpdate();
            if (StringUtility.isNullOrEmpty(taskid)) {
                taskUpdate = taskupdateutil.createTaskUpgradeDSCPojo(varmap, taskUpdate);
            }
            if (Boolean.valueOf(upgrade)) {

                message = taskUpdateManager.upgradeTaskUpdate(varmap, taskUpdate);
            } else {
                message = taskUpdateManager.updateTaskUpdate(varmap);

            }

            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ServerError e) {
            message = sASFUtil.parseServerException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        } catch (ClientError ce) {
            message = sASFUtil.parseClientException(ce);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (VerifyError e) {
            response.setResponseXml(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        log.logDebug("updateTaskUpdate Response" + response.getResponseXml());
        log.logDebug("updateTaskUpdate getstatus code " + response.getStatusCode());
        loggingUtility.logDebug(Constants.MODULE + "<!--TaskUpdateController updateTaskUpdate() end --!>");
        return response;
    }

}
