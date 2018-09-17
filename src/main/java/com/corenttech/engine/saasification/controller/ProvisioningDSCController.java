/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.controller;

import com.corenttech.engine.annotation.support.ResponseHolder;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.ProvisioningDscUtil;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.manager.ProvisioningDSCManager;
import com.corenttech.engine.saasification.model.ProvisioningDSC;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author puja
 */
@Service("provisioningDSCController")
public class ProvisioningDSCController {

    @Autowired
    private transient ProvisioningDscUtil provisioningDscUtil;
    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient ProvisioningDSCManager provisioningDSCManager;
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(ProvisioningDSCController.class);
    LoggingUtility log = LoggingUtility.getInstance(ProvisioningDSCController.class);

    public ResponseHolder createProvisioningDsc(Map<String, String> varmap, ResponseHolder response) throws ClientError, ServerError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--ProvisioningDSCController createProvisioningDsc() start(!--notpersisting in DB--!) --!>");
        String message = "";
        loggingUtility.logDebug(Constants.MODULE + "varmap : " + varmap);
        try {
            String accountid = varmap.get("account");
            String body = varmap.get("body");
            if (StringUtility.isNullOrEmpty(accountid)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_ACCOUNT, true);
                throw new VerifyError(message);
            }
            if (StringUtility.isNullOrEmpty(body)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_XML, true);
                throw new VerifyError(message);
            }
            ProvisioningDSC provisioningDSC = new ProvisioningDSC();
            provisioningDSC = provisioningDscUtil.createProvisionDSCPojo(varmap,provisioningDSC);
            message = provisioningDSCManager.createProvisionDSC(varmap,provisioningDSC);
//            message = sASFUtil.createSuccessResponse(message);
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
        log.logDebug("createProvisioningDsc Response" + response.getResponseXml());
        log.logDebug("createProvisioningDsc getstatus code " + response.getStatusCode());
        loggingUtility.logDebug(Constants.MODULE + "<!--ProvisioningDSCController createProvisioningDsc() end (!--notpersisting in DB--!)--!>");
        return response;
    }

}
