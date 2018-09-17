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
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.manager.SaaSifyClone;
import com.corenttech.engine.utility.LoggingUtility;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author volvo
 */
@Service("saasifyController")
public class SaaSifyCloneController {

    private LoggingUtility loggingUtility = LoggingUtility.getInstance(SaaSifyCloneController.class);
    @Autowired
    private transient SaaSifyClone saaSifyClone;
    @Autowired
    private transient SASFUtil sASFUtil;

    public ResponseHolder cloneSpec(Map<String, String> varMap, ResponseHolder response) {
        String message = "";
        loggingUtility.logDebug(Constants.MODULE + "<!--SaaSifyCloneController  cloneSpec start --!>");
        loggingUtility.logDebug(Constants.MODULE + "<!--SaaSifyCloneController  varMap --!>"+varMap);
        try {
            message = saaSifyClone.cloneSpec(varMap);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ClientError clientError) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            message = sASFUtil.getErrorMessage(clientError.getCode(), Boolean.TRUE);
            loggingUtility.logException(clientError);
        } catch (Exception exception) {
            message = sASFUtil.parseException(new ServerError(exception.getMessage(), "SAAS001000"));
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            loggingUtility.logException(exception);
        }
        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[cloneSpec] : " + message);
        loggingUtility.logDebug(Constants.MODULE + "<!--SaaSifyCloneController  cloneSpec END --!>");
        response.setResponseXml(message);
        return response;

    }
}
