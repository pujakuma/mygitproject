/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.config;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.integration.Integration;
import com.corenttech.engine.saasification.model.Saasify;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.XMLUtility;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Shagul Hameed S
 */
@Service("saaSifyUtil")
public class SaaSifyUtil {

    static LoggingUtility log = LoggingUtility.getInstance(SaaSifyUtil.class);

    @Autowired
    Integration saasIntegration;

    public String parseException(Exception e, String errorCode) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append(errorCode).append("\">").append(e.getMessage()).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String getGUID() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public Saasify createSaaSifypojo(Map<String, String> saasifyMap) throws ClientError, ParserConfigurationException, SAXException, IOException {
        Saasify saasify = new Saasify();
        String accountId = saasifyMap.get("account");
        String body = saasifyMap.get("body");
        log.logDebug(" SaaSifypojo Entry body =" + body);
        Document document = XMLUtility.getXMLDocument(body, false);
        if (null != document) {
            NodeList nodeList = document.getElementsByTagName("saasify");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node saasifyElement = nodeList.item(0);
                String id = "SFY-" + getGUID();
                saasify.setId(id);
                if (StringUtility.isNotNullOrEmpty(accountId)) {
                    saasify.setAccountId(accountId);
                }
                String nodeid = XMLUtility.getValueOfSubTag(saasifyElement, "nodeid");
                if (StringUtility.isNotNullOrEmpty(nodeid)) {
                    saasify.setNodeId(nodeid);
                }
                String appId = XMLUtility.getValueOfSubTag(saasifyElement, "applicationid");
                if (StringUtility.isNotNullOrEmpty(appId)) {
                    saasify.setApplicationId(appId);
                }
                String deploymentid = XMLUtility.getValueOfSubTag(saasifyElement, "deploymentid");
                if (StringUtility.isNotNullOrEmpty(deploymentid)) {
                    saasify.setDeploymentId(deploymentid);
                }
                NodeList nodeList1 = document.getElementsByTagName("callback");
                {
                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        Node callBackElement = nodeList1.item(0);
                        String callbackurl = XMLUtility.getValueOfSubTag(callBackElement, "url");
                        if (StringUtility.isNotNullOrEmpty(callbackurl)) {
                            saasify.setCallBackUrl(callbackurl);
                        }
                        String callbackmethod = XMLUtility.getValueOfSubTag(callBackElement, "method");
                        if (StringUtility.isNotNullOrEmpty(callbackmethod)) {
                            saasify.setCallBackMethod(callbackmethod);
                        }
                        String callbackinput = XMLUtility.getValueOfSubTag(callBackElement, "input");
                        if (StringUtility.isNotNullOrEmpty(callbackinput)) {
                            saasify.setCallBackInput(callbackinput);
                        }
                    }
                }
                String iscallback = XMLUtility.getValueOfSubTag(saasifyElement, "iscallback");
                if (StringUtility.isNotNullOrEmpty(iscallback) && iscallback.equalsIgnoreCase("true")) {
                    saasify.setIsCallBack(Saasify.IsCallBack.TRUE);
                } else {
                    saasify.setIsCallBack(Saasify.IsCallBack.FALSE);
                }
                String type = XMLUtility.getValueOfSubTag(saasifyElement, "type");
                if (StringUtility.isNotNullOrEmpty(type)) {
                    saasify.setType(type);
                }

                String meta = XMLUtility.getValueOfSubTag(saasifyElement, "meta");
                if (StringUtility.isNotNullOrEmpty(meta)) {
                    saasify.setMeta(meta);
                }
            }
        } else {
            throw new ClientError("Invalid Request body", "SFY1000010");
        }
        return saasify;
    }

    public String parseClientException(ClientError ce) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append(ce.getCode()).append("\">").append(ce.getMessage()).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String createSuccessResponse(Saasify Saasify) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append("<id>").append(Saasify.getId()).append("</id>");
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

    public String parseServerException(ServerError serverError) {
        String message = serverError.getMessage();
        Throwable inner = null;
        Throwable root = serverError;
        if ((inner = root.getCause()) != null) {
            message += " " + inner.getMessage();
        }
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append("500").append("\">").append(message).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public void frameSaasifyPojo(Saasify saasify, StringBuilder sb) {
        log.logDebug("<----- frameSaasifyPojo method start -----> ");
        if (null != saasify) {
            sb.append("<saasify id=\"").append(saasify.getId()).append("\">");
            sb.append("<account>").append(saasify.getAccountId()).append("</account>");
            sb.append("<applicationid>").append(saasify.getApplicationId()).append("</applicationid>");
            sb.append("<deploymentid>").append(saasify.getDeploymentId()).append("</deploymentid>");
            sb.append("<nodeid>").append(saasify.getNodeId()).append("</nodeid>");
            sb.append("<meta>").append(saasify.getMeta()).append("</meta>");
            sb.append("<status>").append(saasify.getStatus()).append("</status>");
            sb.append("<callbackurl>").append(saasify.getCallBackUrl()).append("</callbackurl>");
            sb.append("<callbackmethod>").append(saasify.getCallBackMethod()).append("</callbackmethod>");
            sb.append("<callbackinput>").append(saasify.getCallBackInput()).append("</callbackinput>");
            sb.append("<iscallback>").append(saasify.getIsCallBack()).append("</iscallback>");
            sb.append("<message>").append(saasify.getMessage()).append("</message>");
            sb.append("<createddate>").append(saasify.getCreatedDate()).append("</createddate>");
            sb.append("<updateddate>").append(saasify.getUpdatedDate()).append("</updateddate>");
            sb.append("</saasify>");
        }
        log.logDebug("<----- frameSaasifyPojo method end -----> ");
    }

    public String updateSuccessResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_201);
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

    public void parseInputXml(Map<String, String> saasifyMap) throws ClientError, ParserConfigurationException, SAXException, IOException {
        String body = saasifyMap.get("body");
        log.logDebug("Parse InputXml Entry body =" + body);
        Document document = XMLUtility.getXMLDocument(body, false);
        if (null != document) {
            NodeList nodeList = document.getElementsByTagName("task");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node saasifyElement = nodeList.item(0);
                String status = XMLUtility.getValueOfSubTag(saasifyElement, "status");
                if (StringUtility.isNotNullOrEmpty(status)) {
                    saasifyMap.put("status", status);
                }
                String message = XMLUtility.getValueOfSubTag(saasifyElement, "description");
                if (StringUtility.isNotNullOrEmpty(message)) {
                    saasifyMap.put("message", message);
                }
            }
        } else {
            throw new ClientError("Invalid Request body", "SFY1000012");
        }
    }

//    public void amTaskCreate(Saasify SaasifyPojo) throws ClientError {
//        log.logDebug(":::: amTaskCreate method stated:::: ");
//        String response = null;
//        String inputXml = null;
//        try {
//            String saasApiUrl = URLUtility.getApiURL().endsWith("/") ? URLUtility.getApiURL() : URLUtility.getApiURL() + "/";
//            String url = saasApiUrl + Constants.RESOURCEGROUP_URL;
//            String filter = "{fl:{fc1: { n : 'resourceId', ty:'ST', op: 'EQ', vl : ['" + SaasifyPojo.getDeploymentId()
//                    + "']},cond1:'AND',fc2:{n:'nodeId',op:'EQ',ty:'ST',vl:['" + SaasifyPojo.getNodeId() + "']}}}";
//            log.logDebug(":::: filter Query :::: " + filter);
//            filter = URLEncoder.encode(filter);
//            String resourceUrl = url + "?q=" + filter;
//            log.logDebug(":::: resource group url :::: " + resourceUrl);
//            response = saasIntegration.restCallAPI(resourceUrl, "GET", null);
//            log.logDebug(":::: resource_group-GET response :::: " + response);
//            String depInsId = SaaSifyUtil.parseResouceXML(response);
//            log.logDebug(":::: Instance id :::: " + depInsId);
//            String callBackUrl = URLUtility.getApiURL() + "/saasification/saasify/" + SaasifyPojo.getId() + "?account=" + SaasifyPojo.getAccountId();
//            inputXml = SaaSifyUtil.frameTaskRequest(SaasifyPojo, depInsId, callBackUrl);
//            log.logDebug(":::: AM_Task input xml :::: " + inputXml);
//            String TaskUrl = saasApiUrl + Constants.AMTASK_URL + "?accountid=" + SaasifyPojo.getAccountId() + "&applicationid=" + SaasifyPojo.getApplicationId() + "&avmdepid=" + SaasifyPojo.getDeploymentId();
//            log.logDebug(":::: AM_Task url :::: " + TaskUrl);
//            response = saasIntegration.restCallAPI(TaskUrl, "POST", inputXml);
//            String amTaskId = XPathUtil.getXPathValue("//response/id/text()", response);
//            log.logDebug(":::: AM_Task response id :::: " + TaskUrl);
//            SaasifyPojo.setResponse(amTaskId);
//            log.logDebug(":::: amTaskCreate method end:::: ");
//        } catch (ClientError ex) {
//            throw new ClientError(ex.getCode(), ex.getMessage());
//        } catch (Exception ex) {
//            log.logError("Exception in AmTaskCreate " + ex.getMessage());
//            log.logException(ex);
//            throw new ClientError(ex.getMessage(), "SFY1000013");
//        }
//    }

    private static String parseResouceXML(String response) throws ClientError {
        Document document = null;
        String instName = null;
        try {
            document = XMLUtility.getXMLDocument(response, false);
            if (null != document) {
                NodeList nodeList = document.getElementsByTagName("resourcegroups");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node resourceElement = nodeList.item(0);
                    instName = XMLUtility.getValueOfSubTag(resourceElement, "cloudresourceid");
                }
                if (StringUtility.isNotNullOrEmpty(instName)) {
                    return instName;
                }
            } else {
                throw new ClientError("Invalid instance id", "SFY1000013");
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            log.logError("Exception in parseResouceXML " + ex.getMessage());
            log.logException(ex);
            throw new ClientError(ex.getMessage(), "SFY1000013");
        }
        return null;
    }

    private static String frameTaskRequest(Saasify SaasifyPojo, String depInsId, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("<task>").append("<nodeid>").append(SaasifyPojo.getNodeId()).append("</nodeid>");
        sb.append("<instanceid>").append(depInsId).append("</instanceid>");
        sb.append("<executionid>").append("</executionid>");
        sb.append("<priority>").append("false").append("</priority>");
        sb.append("<agentinputxml>").append("<![CDATA[").append(SaasifyPojo.getMeta()).append("]]>").append("</agentinputxml>");
        sb.append("<callback>").append("<url>").append(url).append("</url>");
        sb.append("<method>").append("PUT").append("</method>");
        sb.append("<inputxml>").append("<![CDATA[").append("<task></task>").append("]]>").append("</inputxml>");
        sb.append("</callback>");
        sb.append("</task>");
        return sb.toString();
    }

    public void updateCallBackUrl(Saasify saasify) throws ClientError {
        log.logDebug("updateCallBackUrl method start");
        String callBackUrl = saasify.getCallBackUrl();
        log.logDebug(":::: callback url :::: " + callBackUrl);
        String method = saasify.getCallBackMethod();
        log.logDebug(":::: callback method :::: " + method);
        String bodyXml = saasify.getCallBackInput();
        log.logDebug(":::: callback bodyXml :::: " + bodyXml);
        String response = saasIntegration.restCallAPI(callBackUrl, method, bodyXml);
        log.logDebug(":::: callback response :::: " + response);
        log.logDebug("updateCallBackUrl method end");
    }

    public String updateSuccessResponse(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append(message);
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

    public String frameWorkloadReport(String saasReportXml, String resourceType) throws ParserConfigurationException, SAXException, IOException {
        StringBuilder sb = new StringBuilder();
        Document xmlDocument = XMLUtility.getXMLDocument(saasReportXml, false);
        NodeList list = resourceType.equalsIgnoreCase(SaasificationConsts.WORKLOADREPORT) ? xmlDocument.getElementsByTagName("topology") : xmlDocument.getElementsByTagName("topologies");
        Node item = list.item(0);
        String analyzedSaasReport = XMLUtility.nodeToString(item);
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append("<workloadgroups>");
        sb.append("<workloadgroup>");
        sb.append("<resourceid>").append(XPathUtil.getXPathValue("//topology/@id", saasReportXml)).append("</resourceid>");
        sb.append("<resourcetype>").append(resourceType.toUpperCase()).append("</resourcetype>");
        sb.append("<metadata>");
        sb.append("<![CDATA[").append(analyzedSaasReport).append("]]>");
        sb.append("</metadata>");
        sb.append("</workloadgroup>");
        sb.append("</workloadgroups>");
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

}
