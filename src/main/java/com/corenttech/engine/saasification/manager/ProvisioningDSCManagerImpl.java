/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.coe.beans.Plan;
import com.corenttech.engine.coe.beans.Task;
import com.corenttech.engine.coe.util.JaxBUtil;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.dao.ArtifactSpecDAO;
import com.corenttech.engine.saasification.integration.Integration;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.saasification.model.ProvisioningDSC;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.RESTUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.URLUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.XMLUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import com.corenttech.engine.saasification.dao.WSPKPARTIFACTDAO;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author veyron
 */
@Service("provisioningDSCManagerImpl")
public class ProvisioningDSCManagerImpl implements ProvisioningDSCManager {

    @Autowired
    private transient Integration integration;
    @Autowired
    private transient ArtifactSpecDAO artifactSpecDAO;
    @Autowired
    private transient WSPKPARTIFACTDAO wspkpartifactdao;
    @Autowired
    private transient SASFUtil sASFUtil;
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(ProvisioningDSCManagerImpl.class);

    @Override
    public String createProvisionDSC(Map<String, String> varmap, ProvisioningDSC provisioningDSC) throws ClientError, ServerError, Exception {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createProvisionDSC() start");
        String message = "";
        //get zone deployment for clone the resource group
        String parentid = provisioningDSC.getParentid();
        String saasvariantid = provisioningDSC.getSaasvariantid();
        String destnodeid = provisioningDSC.getDestnodeid();
        String destinationdepid = provisioningDSC.getDestinationdepid();
        String accountid = varmap.get("account");
        varmap.put("destnodeid", destnodeid);
        varmap.put("destinationdepid", destinationdepid);
        varmap.put("sourcenodeid", provisioningDSC.getSourcenodeid());
        varmap.put("saasvariantid", saasvariantid);
        varmap.put("parentid", parentid);
        varmap.put("coeplanid", provisioningDSC.getCoeplanid());
        varmap.put("cloudproviderid", provisioningDSC.getCloudproviderid());
        varmap.put("saasboxid", provisioningDSC.getSaasboxid());
        varmap.put("executionnode", provisioningDSC.getExecutionnode());
        varmap.put("isspaas", provisioningDSC.getIspaas());
        //get zone deployment 
        String depid = getDeploymentFromSaasBox(varmap);
        varmap.put("depid", depid);
        //get zone node
        String nodeid = getNode(varmap);
        varmap.put("zonenodeid", nodeid);
        if (!accountid.equalsIgnoreCase(varmap.get("templateaccountid"))) {
            //getResource group of zone
            String resourceGroup = getResourceGroup(varmap);
            //getResource group Details of zone
            String resourceGroupDetails = getResourceGroupDetails(varmap);
            //create Resource group of tenant
            createResourceGroup(varmap, resourceGroup);
            //create Resource group details of tenant
            createResourceGroupDetails(varmap, resourceGroupDetails);
            //clone  install agent
            cloneInstallAgent(varmap);
            String tenantname = getaccountxml(varmap);
            varmap.put("tenantname", tenantname);
        } else {
            varmap.put("tenantname", accountid);
            varmap.put("templateaccountid", accountid);
        }
        //get the agentport
        String agentPort = getRegisterAgentPort(varmap);
//        agentPort = "8077";
        varmap.put("agentport", agentPort);
        //get application
        String applicationid = getApplicationID(varmap);
        varmap.put("applicationid", applicationid);
        //get cloned specs
        List<ArtifactSpec> artifactSpecList = artifactSpecDAO.getArtifactDetailsForClone(accountid, destnodeid, destinationdepid);
        if (artifactSpecList != null && artifactSpecList.isEmpty()) {
            //clone specs
            String cloneupdateXml = integration.createCloneForProvisioning(varmap, provisioningDSC);
            //get cloned specs
            artifactSpecList = artifactSpecDAO.getArtifactDetailsForClone(accountid, destnodeid, destinationdepid);
        }
        //get COE plan to know existing task last sequence
        String planXML = getCOEPlan(varmap);
        //convert planxml in plan object
        Plan plan = convertToPlanObject(planXML);
        //frame provisioning task
        String inputxml = frameProvisionTaskXML(varmap, artifactSpecList, plan);
        //update plan
        String url = URLUtility.getApiURL() + SaasificationConsts.COE_PLAN_ADD_TASK_URL.replace("{planid}", provisioningDSC.getCoeplanid());

        String planInputXML = SaasificationConsts.COE_PLAN_TASK_UPDATE_REQ.replace("{taskxml}", inputxml);

        String method = RESTUtility.PUT;

        String callbackresponse = integration.restCallAPI(url, method, planInputXML);

        message = sASFUtil.createSuccessResponse("Provision task created Sucessfully");
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createProvisionDSC() Response::" + message);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createProvisionDSC() END");

        return message;
    }

    public String getDeployment(Map<String, String> varmap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeployment() start");
        String filter = "{fl:{fc1: { n : 'saasvarid', ty:'ST', op: 'EQ', vl : ['" + varmap.get("saasvariantid") + "'] } } }";
        filter = URLEncoder.encode(filter, "UTF-8");
        String url = URLUtility.getApiURL() + SaasificationConsts.AVMDEPLOY_GET_URL.replace("{account}", varmap.get("parentid")) + "&q=" + filter;
        String method = RESTUtility.GET;
        String inputxml = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
        if (StringUtility.isNotNullOrEmpty(callbackResponse)) {
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeployment()callbackresponse" + callbackResponse);
            String callbackid = XPathUtil.getXPathValue("//avmdeployment/@id", callbackResponse);
            String appverstionid = XPathUtil.getXPathValue("//avmdeployment/appversionid/text()", callbackResponse);
            varmap.put("appversionid", appverstionid);
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeployment()callbackid" + callbackid);
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeployment() END");
            return callbackid;
        } else {
            throw new ClientError("invalid depid");
        }
    }

    public String getNode(Map<String, String> varMap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getNode() start");
        String filter = "{fl:{fc1:{n:'fromresourceid',ty:'ST',op:'EQ',vl:['" + varMap.get("sourcenodeid") + "']},cond1:'AND',fc2:{n:'resourceid',ty:'ST',op:'EQ',vl:['" + varMap.get("depid") + "']}}}";
        filter = URLEncoder.encode(filter, "UTF-8");
        String url = URLUtility.getApiURL() + SaasificationConsts.AVMNODE_GET_URL.replace("{account}", varMap.get("parentid")) + "&q=" + filter;
        String method = RESTUtility.GET;
        String inputxml = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getNode()callbackresponse" + callbackResponse);
        String callbackid = XPathUtil.getXPathValue("//Node/@id", callbackResponse);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getNode()callbackid" + callbackid);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getNode() END");
        return callbackid;
    }

    public String getResourceGroup(Map<String, String> varMap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getResourceGroup() start");
        String url = URLUtility.getApiURL() + SaasificationConsts.RESOURCE_GROUP_GET_URL;
        url = url.replace("{depid}", varMap.get("depid")).replace("{nodeid}", varMap.get("zonenodeid"));
        String method = RESTUtility.GET;
        String inputxml = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getResourceGroup()callbackresponse" + callbackResponse);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getResourceGroup() END");
        return callbackResponse;
    }

    public String getResourceGroupDetails(Map<String, String> varMap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getResourceGroupDetails() start");
        String url = URLUtility.getApiURL() + SaasificationConsts.RESOURCE_GROUP_DETAILS_GET_URL;
        String filter = "{fl:{fc1:{n:'resourcetype',ty:'ST',op:'EQ',vl:['PAAS']},cond1:'AND',fc2:{n:'nodeId',ty:'ST',op:'EQ',vl:['" + varMap.get("zonenodeid") + "']}}}";
        filter = URLEncoder.encode(filter, "UTF-8");
        url = url + "q=" + filter;
        String method = RESTUtility.GET;
        String inputxml = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getResourceGroupDetails()callbackresponse" + callbackResponse);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getResourceGroupDetails() END");
        return callbackResponse;
    }

    public String createResourceGroup(Map<String, String> varMap, String response) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createResourceGroup() start");
        String url = URLUtility.getApiURL() + SaasificationConsts.RESOURCE_GROUP_POST_URL.replace("{account}", varMap.get("account"));
        String method = RESTUtility.POST;
        String callbackResponse = "";
        Document document;
        try {
            document = XMLUtility.getXMLDocument(response, false);
            NodeList nodelist = document.getElementsByTagName("resourcegroup");
            for (int i = 0; i < nodelist.getLength(); i++) {
                Node node = nodelist.item(i);
                String xml = XMLUtility.nodeToString(node);
                String type = XPathUtil.getXPathValue("//resourcegroup/type/text()", xml);
                if (!type.equalsIgnoreCase("PAAS")) {
                    String inputxml = frameResourceGroupInputXML(varMap, xml);
                    callbackResponse = integration.restCallAPI(url, method, inputxml);
                }
                loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createResourceGroup()callbackresponse" + callbackResponse);
                loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createResourceGroup() END");
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ProvisioningDSCManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ProvisioningDSCManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProvisioningDSCManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return callbackResponse;
    }

    public String createResourceGroupDetails(Map<String, String> varMap, String response) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createResourceGroupDetails() start");
        String url = URLUtility.getApiURL() + SaasificationConsts.RESOURCE_GROUP_DETAILS_POST_URL.replace("{account}", varMap.get("account") + "&resourceid=" + varMap.get("destinationdepid"));
        String method = RESTUtility.POST;
        String callbackResponse = "";
        Document document;
        try {
            document = XMLUtility.getXMLDocument(response, false);
            NodeList nodelist = document.getElementsByTagName("resourcedetail");
            for (int i = 0; i < nodelist.getLength(); i++) {
                Node node = nodelist.item(i);
                String xml = XMLUtility.nodeToString(node);
                String inputxml = frameResourceGroupDetailsInputXML(varMap, xml);
                callbackResponse = integration.restCallAPI(url, method, inputxml);
                loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createResourceGroupDetails()callbackresponse" + callbackResponse);
                loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()createResourceGroupDetails() END");
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ProvisioningDSCManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ProvisioningDSCManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProvisioningDSCManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return callbackResponse;
    }

    public String frameResourceGroupInputXML(Map<String, String> varMap, String response) {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceGroupInputXML() start");

        String resourcescaleid = XPathUtil.getXPathValue("//resourcegroup/resourcescaleid/text()", response);
        String cloudresourceid = XPathUtil.getXPathValue("//resourcegroup/cloudresourceid/text()", response);
        String cloudparentid = XPathUtil.getXPathValue("//resourcegroup/cloudparentid/text()", response);
        String isspec = XPathUtil.getXPathValue("//resourcegroup/isspec/text()", response);
        String type = XPathUtil.getXPathValue("//resourcegroup/type/text()", response);
        String status = XPathUtil.getXPathValue("//resourcegroup/status/text()", response);
        String inputxml = SaasificationConsts.RESOURCE_GROUP_REQ.replace("{depid}", varMap.get("destinationdepid"))
                .replace("{nodeid}", varMap.get("destnodeid")).replace("{scaleid}", resourcescaleid)
                .replace("{instanceid}", cloudresourceid).replace("{cloudpar}", cloudparentid)
                .replace("{isspec}", isspec).replace("{type}", type).replace("{status}", status);
        varMap.put("instanceid", cloudresourceid);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceGroupInputXML()inputxml::" + inputxml);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceGroupInputXML() END");
        return inputxml;

    }

    public String frameResourceGroupDetailsInputXML(Map<String, String> varMap, String response) {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceGroupDetailsInputXML() start");

        String cloudkeyid = XPathUtil.getXPathValue("//resourcedetail/cloudkeyid/text()", response);
        String resourcetype = XPathUtil.getXPathValue("//resourcedetail/resourcetype/text()", response);
        String resourceparentid = XPathUtil.getXPathValue("//resourcedetail/resourceparentid/text()", response);
        String cloudkeyname = XPathUtil.getXPathValue("//resourcedetail/cloudkeyname/text()", response);
        String resourcemeta = XPathUtil.getXPathValue("//resourcedetail/resourcemeta/text()", response);
        String cloudproperties = XPathUtil.getXPathValue("//resourcedetail/cloudproperties/text()", response);
        String isshared = XPathUtil.getXPathValue("//resourcedetail/isshared/text()", response);
        String isglobal = XPathUtil.getXPathValue("//resourcedetail/isglobal/text()", response);
        String status = XPathUtil.getXPathValue("//resourcedetail/status/text()", response);
        String inputxml = SaasificationConsts.RESOURCE_GROUP_DETAILS_REQ.replace("{depid}", varMap.get("destinationdepid"))
                .replace("{nodeid}", varMap.get("destnodeid"))
                .replace("{cloudkeyid}", cloudkeyid)
                .replace("{resourcetype}", resourcetype)
                .replace("{resourceparentid}", resourceparentid)
                .replace("{cloudkeyname}", cloudkeyname)
                .replace("{resourcemeta}", resourcemeta)
                .replace("{cloudproperties}", cloudproperties)
                .replace("{cloudproperties}", cloudproperties)
                .replace("{isshared}", isshared)
                .replace("{isglobal}", isglobal)
                .replace("{status}", status);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceGroupDetailsInputXML()inputxml::" + inputxml);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceGroupDetailsInputXML() END");
        return inputxml;

    }

    public String getRegisterAgentPort(Map<String, String> varMap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getRegisterAgentPort() start");
        String filter = "{fl:{fc1:{n:'nodeid',ty:'ST',op:'EQ',vl:['" + varMap.get("zonenodeid") + "']}}}";
        filter = URLEncoder.encode(filter, "UTF-8");
        String url = URLUtility.getAgentManagerURL() + SaasificationConsts.AGENT_GET_URL.replace("{account}", varMap.get("parentid")) + "&q=" + filter;
        String method = RESTUtility.GET;
        String inputxml = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
        String agentport = XPathUtil.getXPathValue("//agentport/text()", callbackResponse);

        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getRegisterAgentPort()callbackresponse" + callbackResponse);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getRegisterAgentPort()agentport" + agentport);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getRegisterAgentPort() END");
        return agentport;
    }

    private String getaccountxml(Map<String, String> varMap) throws ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getaccountxml() start");
        String accountxml = "";
        String url = URLUtility.getcommonApiURL() + SaasificationConsts.ACCOUNT_GET_URL;
        url = url.replace("{account}", varMap.get("account"));
        String method = RESTUtility.GET;
        accountxml = integration.restCallAPI(url, method, "");
        String tenantname = XPathUtil.getXPathValue("//account/name/text()", accountxml);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getaccountxml()accountxml::" + accountxml);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getaccountxml()tenantname::" + tenantname);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getaccountxml() END");
        return tenantname;
    }

    private String getApplicationID(Map<String, String> varMap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getApplicationID() start");
        String applicationxml = "";
        String qfilter = "{fl:{fc1: { n : 'id', ty:'IN', op: 'EQ', vl : ['" + Integer.parseInt(varMap.get("appversionid")) + "'] } } }";
        qfilter = URLEncoder.encode(qfilter, "UTF-8");
        String url = URLUtility.getApiURL() + SaasificationConsts.APPVERSION_GET_URL;
        url = url.replace("{account}", varMap.get("parentid"));
        url = url.replace("{query}", qfilter);
        String method = RESTUtility.GET;
        applicationxml = integration.restCallAPI(url, method, "");
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getApplicationID() appversionxml::" + applicationxml);
        String appid = XPathUtil.getXPathValue("//AppVersion/appid/text()", applicationxml);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getApplicationID() applicationid::" + appid);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getApplicationID() END");
        return appid;
    }

    private String frameProvisionTaskXML(Map<String, String> varMap, List<ArtifactSpec> specList, Plan plan) throws ServerError, ClientError, UnsupportedEncodingException {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameProvisionTaskXML() start");
        List<Task> taskList = plan.getTask();
        int n = taskList.size();
        StringBuilder builder = new StringBuilder();
        for (ArtifactSpec spec : specList) {
            String wspId = spec.getWspid();
            String wspName = getWspname(wspId, varMap);
            varMap.put("wspName", wspName);
            builder.append("<task type=\"PROVISION_TASK\" >")
                    .append("<resourceinfo><![CDATA[").append(frameResourceInfo(varMap, spec.getArtifactidentity())).append("]]></resourceinfo>")
                    .append("<seqid>").append(n + 1).append("</seqid>")
                    .append("<parentseqid>").append(n).append("</parentseqid>")
                    .append("<inputXml/>")
                    .append("</task>");
            n++;
        }
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameProvisionTaskXML() xml::" + builder.toString());
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameProvisionTaskXML() END");
        return builder.toString();
    }

    private String frameResourceInfo(Map<String, String> varMap, String artifactid) throws UnsupportedEncodingException, UnsupportedEncodingException, UnsupportedEncodingException, UnsupportedEncodingException, UnsupportedEncodingException, ClientError, ServerError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceInfo() start");
        StringBuilder builder = new StringBuilder();
        builder.append("<request>")
                .append("<applicationid>").append(varMap.get("applicationid")).append("</applicationid>")
                .append("<avmdepid>").append(varMap.get("destinationdepid")).append("</avmdepid>")
                .append("<zonedepid>").append(varMap.get("depid")).append("</zonedepid>")
                .append("<avmdepnodeid>").append(varMap.get("executionnode")).append("</avmdepnodeid>")
                .append("<agentport>").append(varMap.get("agentport")).append("</agentport>")
                .append("<tenantname>").append(varMap.get("tenantname")).append("</tenantname>")
                .append("<parentid>").append(varMap.get("parentid")).append("</parentid>")
                .append("<artifactid>").append(artifactid).append("</artifactid>")
                .append("<ispaas>").append(varMap.get("isspaas")).append("</ispaas>")
                .append("<datamaxnode>").append(varMap.get("destnodeid")).append("</datamaxnode>")
                .append("<cloudprovidername>").append(getCloudAccount(varMap)).append("</cloudprovidername>")
                .append("<fileguid>").append(wspkpartifactdao.getKpListWithversion(varMap.get("workloadversion"), varMap.get("wspName")).getGuid()).append("</fileguid>")
                .append("</request>");
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceInfo()requestXML" + builder.toString());
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()frameResourceInfo() END");
        return builder.toString();

    }

    private String getCOEPlan(Map<String, String> varMap) throws ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getCOEPlan() start");
        String url = URLUtility.getApiURL() + SaasificationConsts.COE_PLAN_ADD_TASK_URL.replace("{planid}", varMap.get("coeplanid"));
        String method = RESTUtility.GET;
        String inputXML = "";
        String callbackResponse = integration.restCallAPI(url, method, inputXML);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getCOEPlan()callbackResponse::" + callbackResponse);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getCOEPlan() END");
        return callbackResponse;
    }

    private Plan convertToPlanObject(String xml) throws ParserConfigurationException, IOException, SAXException {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()convertToPlanObject() start");
        Document document = XMLUtility.getXMLDocument(xml, false);
        NodeList nodelist = document.getElementsByTagName("plan");
        Node node = nodelist.item(0);
        xml = XMLUtility.nodeToString(node);
        Plan plan = (Plan) JaxBUtil.unmarshall(xml, Plan.class);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()convertToPlanObject() plan name::" + plan.getName());
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getCOEPlan() END");
        return plan;

    }

    private String getWspname(String id, Map<String, String> varMap) throws ServerError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getWspname() start");
        String wspname = integration.getWspName(id, varMap);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getWspname()wspname::" + wspname);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getWspname() END");

        return wspname;
    }

    public String cloneInstallAgent(Map<String, String> varMap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()cloneInstallAgent() start");
        String url = URLUtility.getAgentManagerURL() + SaasificationConsts.AGENT_CLONE_URL.replace("{account}", varMap.get("account"));
        url = url.replace("{source}", varMap.get("depid")).replace("{nodeid}", varMap.get("zonenodeid"));
        url = url.replace("{depid}", varMap.get("destinationdepid")).replace("{destnodeid}", varMap.get("destnodeid"));
        String method = RESTUtility.POST;
        String inputxml = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
//        String agentport = XPathUtil.getXPathValue("//agentport/text()", callbackResponse);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()cloneInstallAgent()callbackresponse" + callbackResponse);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()cloneInstallAgent() END");
        return callbackResponse;
    }

    public String getCloudAccount(Map<String, String> varmap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSC getCloudAccount()getDeployment() start");
        String filter = "{fl:{fc1: { n : 'id',ty:'ST', op: 'IN', vl : ['" + varmap.get("cloudproviderid") + "'] } } }";
        filter = URLEncoder.encode(filter, "UTF-8");
        String url = URLUtility.getApiURL() + SaasificationConsts.CLOUD_ACCOUNT_GET_URL.replace("{account}", varmap.get("parentid")) + "&q=" + filter;
        String method = RESTUtility.GET;
        String inputxml = "";
        String cloudprovidername = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
        if (StringUtility.isNotNullOrEmpty(callbackResponse)) {
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSC getCloudAccount callbackresponse" + callbackResponse);
            cloudprovidername = XPathUtil.getXPathValue("//provideraccount/@type", callbackResponse);
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeployment() cloudprovidername" + cloudprovidername);
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSC getCloudAccount()getDeployment() END");
            return cloudprovidername;
        } else {
            throw new ClientError("invalid depid");
        }
    }

    private String getDeploymentFromSaasBox(Map<String, String> varmap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeploymentFromSaasBox() start");
        String filter = "{fl:{fc1: { n : 'fromsaasbox', ty:'ST', op: 'EQ', vl : ['" + varmap.get("saasboxid") + "'] } } }";
        filter = URLEncoder.encode(filter, "UTF-8");
        String url = URLUtility.getApiURL() + SaasificationConsts.AVM_SAASBOX_GET_URL.replace("{account}", varmap.get("parentid")) + "&q=" + filter;
        String method = RESTUtility.GET;
        String inputxml = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
        if (StringUtility.isNotNullOrEmpty(callbackResponse)) {
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeploymentFromSaasBox()callbackresponse" + callbackResponse);
            String callbackid = XPathUtil.getXPathValue("//saasbox/resourceid/text()", callbackResponse);
            String templateaccountid = XPathUtil.getXPathValue("//saasbox/templateaccountid/text()", callbackResponse);
            String appverstionid = XPathUtil.getXPathValue("//saasbox/versionid/text()", callbackResponse);
            varmap.put("appversionid", appverstionid);
            varmap.put("templateaccountid", templateaccountid);
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeploymentFromSaasBox()callbackid" + callbackid);
            loggingUtility.logDebug(SaasificationConsts.MODULE + "ProvisioningDSCManagerImpl()getDeploymentFromSaasBox() END");
            return callbackid;
        } else {
            throw new ClientError("invalid depid");
        }
    }
}
