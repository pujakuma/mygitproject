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
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.manager.SaasReportDSCManager;
import com.corenttech.engine.saasification.manager.SaasReportDSCManagerImpl;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Shagul Hameed S
 */
@Service("saaSReportDSCController")
public class SaaSReportDSCController {
    
    @Autowired
    SASFUtil sASFUtil;
    @Autowired
    SaasReportDSCManager saasReportDSCManager;
    
    LoggingUtility log = LoggingUtility.getInstance(SaaSReportDSCController.class);
    
    public ResponseHolder getSaasReportDsc(Map<String, String> varmap, ResponseHolder response) throws ClientError, ServerError, Exception {
        log.logMethodEntry();
        String message = "";
        log.logDebug(Constants.MODULE + "varmap : " + varmap);
        try {
            String body = varmap.get("body");
            String resource_type = varmap.get("type");
            if (StringUtility.isNullOrEmpty(body)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_XML, true);
                throw new VerifyError(message);
            }
            message = saasReportDSCManager.getSaaSReport(body, resource_type);
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
        log.logDebug("getSaasReportDsc Response" + response.getResponseXml());
        log.logDebug("getSaasReportDsc getstatus code " + response.getStatusCode());
        log.logMethodExit();
        return response;
    }
}
