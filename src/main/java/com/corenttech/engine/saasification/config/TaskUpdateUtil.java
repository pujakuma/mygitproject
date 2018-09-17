/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.config;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.TaskUpdate;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.XMLUtility;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author veyron
 */
@Service("taskupdateutil")
public class TaskUpdateUtil {
  
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(ProvisioningDscUtil.class);

    public TaskUpdate createTaskUpgradeDSCPojo(Map<String, String> varmap, TaskUpdate taskUpdate) throws ClientError, ServerError, Exception {
        String body = varmap.get("body");
        loggingUtility.logInfo("TaskUpdateXMl xml :::: " + body);
        Document document = XMLUtility.getXMLDocument(body, false);
        NodeList nodelist = document.getElementsByTagName("input");
        Node node = nodelist.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            String artifactid = XPathUtil.getXPathValue("//artifactid/text()", body);
            if (StringUtility.isNotNullOrEmpty(artifactid)) {
                taskUpdate.setArtifactid(artifactid);
            }
            String status = XPathUtil.getXPathValue("//status/text()", body);
            if (StringUtility.isNotNullOrEmpty(status)) {
                taskUpdate.setStatus(status);
            }
            String message = XPathUtil.getXPathValue("//message/text()", body);
            if (StringUtility.isNotNullOrEmpty(message)) {
                taskUpdate.setMessage(message);
            }
            String resourceid = XPathUtil.getXPathValue("//resourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(resourceid)) {
                taskUpdate.setResourceid(resourceid);
            }
            String toversionid=XPathUtil.getXPathValue("//toversionid/text()", body);
            if (StringUtility.isNotNullOrEmpty(toversionid)) {
                taskUpdate.setToversionid(toversionid);
            }
        }
        return taskUpdate;
    }
}
