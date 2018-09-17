/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.config;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.ProvisioningDSC;
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
 * @author puja
 */
@Service("provisioningDscUtil")
public class ProvisioningDscUtil {

    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(ProvisioningDscUtil.class);

    public ProvisioningDSC createProvisionDSCPojo(Map<String, String> varmap, ProvisioningDSC provisioningDSC) throws ClientError, ServerError, Exception {
        String body = varmap.get("body");
        loggingUtility.logInfo("ProvisionDscXMl xml :::: " + body);
        Document document = XMLUtility.getXMLDocument(body, false);
        NodeList nodelist = document.getElementsByTagName("provisioningdsc");
        Node node = nodelist.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            String saasvariantid = XPathUtil.getXPathValue("//saasvariantid/text()", body);
            if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
                provisioningDSC.setSaasvariantid(saasvariantid);
            }
            String sourcenodeid = XPathUtil.getXPathValue("//sourcenodeid/text()", body);
            if (StringUtility.isNotNullOrEmpty(sourcenodeid)) {
                provisioningDSC.setSourcenodeid(sourcenodeid);
            }
            String destinationdepid = XPathUtil.getXPathValue("//destinationdepid/text()", body);
            if (StringUtility.isNotNullOrEmpty(destinationdepid)) {
                provisioningDSC.setDestinationdepid(destinationdepid);
            }
            String destnodeid = XPathUtil.getXPathValue("//destnodeid/text()", body);
            if (StringUtility.isNotNullOrEmpty(destnodeid)) {
                provisioningDSC.setDestnodeidd(destnodeid);
            }
            String action = XPathUtil.getXPathValue("//action/text()", body);
            if (StringUtility.isNotNullOrEmpty(action)) {
                provisioningDSC.setAction(action);
            }
            String parentid = XPathUtil.getXPathValue("//parentid", body);
            if (StringUtility.isNotNullOrEmpty(parentid)) {
                provisioningDSC.setParentid(parentid);
            }
            String ispaas = XPathUtil.getXPathValue("//ispaas", body);
            if (StringUtility.isNotNullOrEmpty(ispaas)) {
                provisioningDSC.setIspaas(ispaas);
            }
            String planid = XPathUtil.getXPathValue("//coeplanid", body);
            if (StringUtility.isNotNullOrEmpty(planid)) {
                provisioningDSC.setCoeplanid(planid);
            }
            String saasboxid = XPathUtil.getXPathValue("//saasboxid", body);
            if (StringUtility.isNotNullOrEmpty(saasboxid)) {
                provisioningDSC.setSaasboxid(saasboxid);
            }
            String cloudproviderid = XPathUtil.getXPathValue("//cloudproviderid", body);
            if (StringUtility.isNotNullOrEmpty(cloudproviderid)) {
                provisioningDSC.setCloudproviderid(cloudproviderid);
            }
            String executionnode = XPathUtil.getXPathValue("//executionnode", body);
            if (StringUtility.isNotNullOrEmpty(cloudproviderid)) {
                provisioningDSC.setExecutionnode(executionnode);
            }

        }
        return provisioningDSC;
    }
}
