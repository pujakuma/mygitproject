/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.exception;

import com.corenttech.engine.saasification.config.NodeSpecUtil;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.utility.parsers.PropertiesUtility;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Service;

/**
 *
 * @author fiesta
 */
@Service("saasificationErrorCodeList")
public final class SaasificationErrorCodeList {

    LoggingUtility log = LoggingUtility.getInstance(NodeSpecUtil.class);
    public boolean flag = false;
    public static Properties errorProperties = null;

    public SaasificationErrorCodeList(boolean flag) {
        this.flag = flag;
        initializeErrorCodes();
    }

    public void initializeErrorCodes() {
        String propertyFilePath = "";
        try {
            log.logDebug("initializeErrorCodes flag : " + flag);
            if (!flag) {
                propertyFilePath = ".";
            } else {
                propertyFilePath = PropertiesUtility.classLoaderfromConfig(Thread.currentThread().getContextClassLoader());
            }
            log.logDebug("propertyFilePath>>>" + propertyFilePath);
            errorProperties = PropertiesUtility.readProperties(propertyFilePath, "saasificationerrorcodes.properties");
        } catch (IOException ie) {
            log.logException(ie);
        }
    }

    public static String getErrorResponse(String errorCode) {
        return errorProperties.getProperty(errorCode) == null ? "" : errorProperties.getProperty(errorCode);
    }
}
