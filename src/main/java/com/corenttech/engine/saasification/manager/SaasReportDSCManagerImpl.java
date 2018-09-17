/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.coe.exceptions.ErrorCodeList;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SaaSifyUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.integration.Integration;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.RESTUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.tags.tagexception.ErrorCodes;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Callback;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

/**
 *
 * @author Shagul Hameed S
 */
@Service("saasReportDSCManagerImpl")
public class SaasReportDSCManagerImpl implements SaasReportDSCManager {

    @Autowired
    Integration integration;
    @Autowired
    SaaSifyUtil saaSifyUtil;
    LoggingUtility log = LoggingUtility.getInstance(SaasReportDSCManagerImpl.class);

    @Override
    public String getSaaSReport(String topologyNodeInput, String resource_type) throws ClientError, ServerError {
        log.logMethodEntry();
        String message = "";
        String saasReportXml = null;
        String sharabilityServiceUrl = null;
        sharabilityServiceUrl = integration.getShareabilityServiceUrl();
        if (StringUtility.isNullOrEmpty(sharabilityServiceUrl)) {
            throw new ServerError("Unable to communicate with Common_Engine API", "SFY100011");
        }
        if (resource_type.equalsIgnoreCase(SaasificationConsts.WORKLOADREPORT)) {
            String saasWorkloadUrl = sharabilityServiceUrl + SaasificationConsts.URLs.SHARABILITY_WORKLOAD_URL.getUrl();
            saasReportXml = integration.restCallAPI(saasWorkloadUrl, RESTUtility.POST, topologyNodeInput);
        } else {
            String saasTopologyUrl = sharabilityServiceUrl + SaasificationConsts.URLs.SHARABILITY_TOPOLOGY_URL.getUrl();
            saasReportXml = integration.restCallAPI(saasTopologyUrl, RESTUtility.POST, topologyNodeInput);
        }
        log.logDebug("saasReportXml ::: " + saasReportXml);
        if (StringUtility.isNullOrEmpty(saasReportXml)) {
            throw new ServerError("Unable to communicate with Shareabilityservice API", "SFY100012");
        }
        try {
            message = saaSifyUtil.frameWorkloadReport(saasReportXml, resource_type);
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            log.logDebug("Exception in frameWorkloadReport " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
        log.logMethodExit();
        return message;
    }

}
