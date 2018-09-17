/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.dao.ArtifactSpecDAO;
import com.corenttech.engine.saasification.integration.Integration;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.saasification.model.TaskUpdate;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.XMLUtility;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author puja
 */
@Service("taskUpdateManagerImpl")
public class TaskUpdateManagerImpl implements TaskUpdateManager {

    @Autowired
    Integration integration;
    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient ArtifactSpecDAO artifactSpecDAO;
    @Autowired
    private transient ArtifactSpecManager artifactSpecManager;
    private LoggingUtility loggingUtility = LoggingUtility.getInstance(TaskUpdateManagerImpl.class);
    transient protected LoggingUtility log = LoggingUtility.getInstance(TaskUpdateManagerImpl.class);

    @Override
    public String updateTaskUpdate(Map<String, String> varmap) throws ClientError, ServerError, Exception {
        loggingUtility.logDebug("<--TaskUpdateManagerImpl-->updateTaskUpdate Start--->");
        String agentTaskGetXml = "";
        String resourcecdata = "";
        String artifactUpdateXml = "";
        String clonePlanUpdateXml = "";
        String message = "";
        agentTaskGetXml = integration.getAgenttask(varmap);
        loggingUtility.logDebug("<--TaskUpdateManagerImpl--> updateTaskUpdate:AgentTaskXml" + agentTaskGetXml);
        String totalRecords = XPathUtil.getXPathValue("//tasks /@totalrecord", agentTaskGetXml);

        if (StringUtility.isNotNullOrEmpty(agentTaskGetXml) && XMLUtility.isValidXMLString(agentTaskGetXml) && StringUtility.isNotNullOrEmpty(totalRecords) && !totalRecords.equalsIgnoreCase("0")) {

            varmap.put("cloudresourceinfo", XPathUtil.getXPathValue("//cloudresourceinfo/text()", agentTaskGetXml));
            resourcecdata = varmap.get("cloudresourceinfo");
            if (StringUtility.isNotNullOrEmpty(resourcecdata)) {
                varmap.put("artifactid", XPathUtil.getXPathValue("//request/artifactid/text()", resourcecdata));
                varmap.put("coeplanid", XPathUtil.getXPathValue("//coeplanid/text()", resourcecdata));
                varmap.put("coetaskid", XPathUtil.getXPathValue("//coetaskid/text()", resourcecdata));
                varmap.put("resourceid", XPathUtil.getXPathValue("//resourceid/text()", resourcecdata));
                varmap.put("toversionid", XPathUtil.getXPathValue("//toversionid/text()", resourcecdata));
            }
            String status = XPathUtil.getXPathValue("//status/text()", agentTaskGetXml);
            if (StringUtility.isNotNullOrEmpty(status)) {
                varmap.put(status, "status");
            }
            message = XPathUtil.getXPathValue("//message/text()", agentTaskGetXml);
            if (StringUtility.isNotNullOrEmpty(message)) {
                varmap.put(message, "message");
            }

        } else {
            throw new ClientError("The given account or xmltask is invalid");
        }
        String account = varmap.get("account");
        String artifactid = varmap.get("artifactid");
        ArtifactSpec artifactSpec = new ArtifactSpec();
        artifactSpec = artifactSpecManager.updateArtifactSpec(account, artifactid, artifactSpec, varmap);
        loggingUtility.logDebug("<--TaskUpdateManagerImpl--> updateTaskUpdate:ArtifactUpdateXml" + artifactUpdateXml);
        if (artifactSpec != null) {
            clonePlanUpdateXml = integration.updateClonePlan(varmap);
            loggingUtility.logDebug("<--TaskUpdateManagerImpl--> updateTaskUpdate:ClonePlanUpdateXml" + clonePlanUpdateXml);
            message = sASFUtil.updateSuccessTaskUpdate();
        } else {
            throw new ClientError("artifactUpdateXml is not updated");
        }
        loggingUtility.logDebug("<--TaskUpdateManagerImpl-->updateTaskUpdate end--->");
        return message;
    }

    @Override
    public String upgradeTaskUpdate(Map<String, String> varmap, TaskUpdate taskUpdate) throws ClientError, ServerError, Exception {
        loggingUtility.logDebug("<--TaskUpdateManagerImpl-->updateTaskUpdate Start--->");
        String agentTaskGetXml = "";
        String resourcecdata = "";
        String artifactUpdateXml = "";
        String clonePlanUpdateXml = "";
        String message = "";

        String taskid = varmap.get("taskid");
        String account = varmap.get("account");
        ArtifactSpec artifactSpec = new ArtifactSpec();
        if (StringUtility.isNullOrEmpty(taskid)) {
            String artifactid = taskUpdate.getArtifactid();
            String status=taskUpdate.getStatus();
            artifactSpec.setStatus(status);
            varmap.put("status", artifactSpec.getStatus());
            message=taskUpdate.getMessage();
            artifactSpec.setMessage(message);
            varmap.put("message", artifactSpec.getMessage());
            varmap.put("resourceid", taskUpdate.getResourceid());
            varmap.put("toversionid", taskUpdate.getToversionid());
            List<ArtifactSpec> artifactSpecList = artifactSpecDAO.getArtifactSpecList(artifactid, account, varmap);
           // ArtifactSpec artifactSpec = new ArtifactSpec();
            artifactSpec = artifactSpecList.get(0);
            if (artifactSpec != null) {
                varmap.put("taskid", artifactSpec.getTaskid());
                agentTaskGetXml = getAgent(varmap);
                loggingUtility.logDebug("<--TaskUpdateManagerImpl--> upgradeTaskUpdate:AgentTaskXml" + agentTaskGetXml);
                String totalRecords = XPathUtil.getXPathValue("//tasks /@totalrecord", agentTaskGetXml);

                if (StringUtility.isNotNullOrEmpty(agentTaskGetXml) && XMLUtility.isValidXMLString(agentTaskGetXml) && StringUtility.isNotNullOrEmpty(totalRecords) && !totalRecords.equalsIgnoreCase("0")) {

                    varmap.put("cloudresourceinfo", XPathUtil.getXPathValue("//cloudresourceinfo/text()", agentTaskGetXml));
                    resourcecdata = varmap.get("cloudresourceinfo");
                    if (StringUtility.isNotNullOrEmpty(resourcecdata)) {
                        varmap.put("artifactid", XPathUtil.getXPathValue("//request/artifactid/text()", resourcecdata));
                        varmap.put("coeplanid", XPathUtil.getXPathValue("//coeplanid/text()", resourcecdata));
                        varmap.put("coetaskid", XPathUtil.getXPathValue("//coetaskid/text()", resourcecdata));
                    }
                } else {
                    throw new ClientError("The given account or xmltask is invalid");
                }

            }
        } else {
            agentTaskGetXml = getAgent(varmap);
            loggingUtility.logDebug("<--TaskUpdateManagerImpl--> updateTaskUpdate:AgentTaskXml" + agentTaskGetXml);
            String totalRecords = XPathUtil.getXPathValue("//tasks /@totalrecord", agentTaskGetXml);

            if (StringUtility.isNotNullOrEmpty(agentTaskGetXml) && XMLUtility.isValidXMLString(agentTaskGetXml) && StringUtility.isNotNullOrEmpty(totalRecords) && !totalRecords.equalsIgnoreCase("0")) {

                varmap.put("cloudresourceinfo", XPathUtil.getXPathValue("//cloudresourceinfo/text()", agentTaskGetXml));
                resourcecdata = varmap.get("cloudresourceinfo");
                if (StringUtility.isNotNullOrEmpty(resourcecdata)) {
                    varmap.put("artifactid", XPathUtil.getXPathValue("//request/artifactid/text()", resourcecdata));
                    varmap.put("coeplanid", XPathUtil.getXPathValue("//coeplanid/text()", resourcecdata));
                    varmap.put("coetaskid", XPathUtil.getXPathValue("//coetaskid/text()", resourcecdata));
                    varmap.put("resourceid", XPathUtil.getXPathValue("//resourceid/text()", resourcecdata));
                    varmap.put("toversionid", XPathUtil.getXPathValue("//toversionid/text()", resourcecdata));
                }
                String status = XPathUtil.getXPathValue("//status/text()", agentTaskGetXml);
                if (StringUtility.isNotNullOrEmpty(status)) {
                    varmap.put("status", status);
                }
                message = XPathUtil.getXPathValue("//message/text()", agentTaskGetXml);
                if (StringUtility.isNotNullOrEmpty(message)) {
                    varmap.put("message",message );
                }
            } else {
                throw new ClientError("The given account or xmltask is invalid");
            }
        }
        String artifactid = varmap.get("artifactid");
        
        artifactSpec = artifactSpecManager.updateArtifactSpec(account, artifactid, artifactSpec, varmap);
        loggingUtility.logDebug("<--TaskUpdateManagerImpl--> updateTaskUpdate:ArtifactUpdateXml" + artifactUpdateXml);
        if (artifactSpec != null) {

            List<ArtifactSpec> artifactSpecList = artifactSpecDAO.getArtifactSpecList(artifactid, "", varmap);

            loggingUtility.logDebug("<--TaskUpdateManagerImpl--> updateTaskUpdate:ArtifactGETXml" + artifactSpecList);
            boolean stausFlag = false;
            boolean inProgressFlag = false;
            boolean failedFlag = false;
            for (ArtifactSpec artifactstatus : artifactSpecList) {
                if ((artifactstatus.getStatus().equalsIgnoreCase("In progress"))) {
                    log.logDebug("TaskUpdateManagerImpl upgradeTaskUpdate() InProgress...");
                    inProgressFlag = true;
                    break;
                } else if ((artifactstatus.getStatus().equalsIgnoreCase("Failed"))) {
                    failedFlag = true;
                }
            }
            if ((!inProgressFlag && !failedFlag)) {
                varmap.put("status", "Success");
                log.logDebug("TaskUpdateManagerImpl updatePlanStatus() Success...");
                clonePlanUpdateXml = integration.updateClonePlan(varmap);
                stausFlag = true;
            } else if ((failedFlag && !inProgressFlag)) {
                varmap.put("status", "Failed");
                varmap.put("message", "Artifact upgrade failed");
                log.logDebug("TaskUpdateManagerImpl updatePlanStatus() Failed...");
                clonePlanUpdateXml = integration.updateClonePlan(varmap);
            }
            loggingUtility.logDebug("<--TaskUpdateManagerImpl--> updateTaskUpdate:ClonePlanUpdateXml" + clonePlanUpdateXml);
            message = sASFUtil.updateSuccessTaskUpdate();
        } else {
            throw new ClientError("artifactUpdateXml is not updated");
        }
        loggingUtility.logDebug("<--TaskUpdateManagerImpl-->updateTaskUpdate end--->");
        return message;
    }

    public String getAgent(Map<String, String> varmap) throws ClientError {
        String agentTaskGetXml = integration.getAgenttask(varmap);
        loggingUtility.logDebug("<--TaskUpdateManagerImpl--> updateTaskUpdate:AgentTaskXml" + agentTaskGetXml);
        return agentTaskGetXml;
    }

}
