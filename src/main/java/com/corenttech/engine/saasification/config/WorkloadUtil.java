/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.config;

import com.corenttech.engine.saasification.dao.WorkloadGroupDAO;
import com.corenttech.engine.saasification.model.WorkloadGroup;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import static com.corenttech.engine.saasification.config.SaaSifyUtil.log;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.XMLUtility;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author
 * optima
 */
@Service("workloadUtil")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class WorkloadUtil {

    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WorkloadUtil.class);
    @Autowired
    WorkloadGroupDAO workloadGroupDAO;

    public List<WorkloadGroup> createWorkloadgroupPojo(Map<String, String> varMap) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--WorkloadUtil creatAvmWorkloadgroupPojo() start --!>");
        String accountid = varMap.get("account");
        String appVersionId = varMap.get("appversionid");
        String resourceId = varMap.get("resourceid");
        String body = varMap.get("body");
        WorkloadGroup workloadGroup = null;
        List<WorkloadGroup> workloadGroups = new ArrayList<>();
        loggingUtility.logDebug(Constants.MODULE + "<!--WorkloadUtil creatWorkloadgroupPojo() body " + body);
        if (StringUtility.isNotNullOrEmpty(body)) {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            Document document;
            builder = builderFactory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(body)));
            NodeList nodeList = document.getElementsByTagName("workloadgroup");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                workloadGroup = new WorkloadGroup();
                String input = XMLUtility.nodeToString(nodeList.item(i));

                workloadGroup.setAccountId(accountid);
                String name = XPathUtil.getXPathValue("//name/text()", input);
                if (StringUtility.isNotNullOrEmpty(name)) {
                    workloadGroup.setName(name);
                }
                String nodeid = XPathUtil.getXPathValue("//nodeid/text()", input);
                if(StringUtility.isNullOrEmpty(nodeid)){
                    nodeid = XPathUtil.getXPathValue("//nodeid/@id()", input);
                }
                if (StringUtility.isNotNullOrEmpty(nodeid)) {
                    workloadGroup.setNodeId(nodeid);
                }
                String resId = XPathUtil.getXPathValue("//resourceid/text()", input);
                if (StringUtility.isNotNullOrEmpty(resId)) {
                    workloadGroup.setResourceId(resId);
                }else{
                    workloadGroup.setResourceId(resourceId);
                }
                String sharable = XPathUtil.getXPathValue("//issharable/text()", input);
                if (StringUtility.isNotNullOrEmpty(sharable)) {
                    workloadGroup.setIssharable(sharable);
                } else {
                    workloadGroup.setIssharable("false");
                }
                String type = XPathUtil.getXPathValue("//type/text()", input);
                if (StringUtility.isNotNullOrEmpty(type)) {
                    workloadGroup.setType(type);
                }
                String version = XPathUtil.getXPathValue("//version/text()", input);
                if (StringUtility.isNotNullOrEmpty(version)) {
                    workloadGroup.setVersion(version);
                }
                String appVersion = XPathUtil.getXPathValue("//appversion/text()", input);
                if (StringUtility.isNotNullOrEmpty(appVersion) && !appVersion.equals("null")) {
                    workloadGroup.setAppversion(appVersion);
                }else{
                    workloadGroup.setAppversion(appVersionId);
                }
                workloadGroup.setResourcetype(XPathUtil.getXPathValue("//resourcetype/text()", input));
                String status = XPathUtil.getXPathValue("//status/text()", input);
                if (StringUtility.isNotNullOrEmpty(status)) {
                    workloadGroup.setStatus(status);
                } else {
                    workloadGroup.setStatus(Constants.COMPLETE);
                }
//                workloadGroup.setStatus(XPathUtil.getXPathValue("//status/text()",input));
                String metaData = XMLUtility.nodeToString(document.getElementsByTagName("bundles").item(i)).trim();
                if (StringUtility.isNullOrEmpty(metaData)) {
                    metaData = XPathUtil.getXPathValue("//metadata", input);
                }
                if (StringUtility.isNotNullOrEmpty(metaData)) {
                    workloadGroup.setMetaData(metaData);
                }

                String prop = XPathUtil.getXPathValue("//properties", body);
                Node propertiesnode = element.getElementsByTagName("properties").item(0);
                String properties = XMLUtility.nodeToString(propertiesnode);
                if (StringUtility.isNotNullOrEmpty(properties)) {
                    log.logDebug(" properties ::: " + properties);
                    workloadGroup.setProperties(properties);
                }
                String conn = XPathUtil.getXPathValue("//connection", body);
                Node conneNode = element.getElementsByTagName("connection").item(0);
                String connection = XMLUtility.nodeToString(conneNode);
                if (StringUtility.isNotNullOrEmpty(connection)) {
                    log.logDebug(" connection ::: " + connection);
                    workloadGroup.setConnections(connection);
                }
                String fire = XPathUtil.getXPathValue("//firewall", body);
                Node fireWallNode = element.getElementsByTagName("firewall").item(0);
                String firewall = XMLUtility.nodeToString(fireWallNode);
                if (StringUtility.isNotNullOrEmpty(firewall)) {
                    log.logDebug(" firewall ::: " + firewall);
                    workloadGroup.setFirewall(firewall);
                }
                String devicex = XPathUtil.getXPathValue("//device", body);
                Node deviceNode = element.getElementsByTagName("device").item(0);
                String device = XMLUtility.nodeToString(deviceNode);
                if (StringUtility.isNotNullOrEmpty(device)) {
                    log.logDebug(" device ::: " + device);
                    workloadGroup.setDevices(device);
                }
                workloadGroup.setCreatedDate(new Date());
                workloadGroup.setUpdatedDate(null);
                //workloadGroupDAO.create(workloadGroup);
//                workloadGroupDAO.create(workloadGroup);
                workloadGroups.add(workloadGroup);
            }
            /* if (null != workloadGroups) {
             if(nodeList.getLength()>1){
             MESSAGE = createSuccessResponse(workloadGroup);
             }else
             {
             MESSAGE=createSingleSuccessResponse(workloadGroup);
             }
             } else {
             throw new ClientError("Could Not Create Workload Group", "SAAS0000050");
             }*/
        } else {
            throw new ClientError("Input XMl is Null or Empty", SaasificationConsts.INVALID_XML);
        }
        return workloadGroups;
    }

//    public void UpdateWorkloadGroupEntity(WorkloadGroup workloadGroup, String xml,String resourceId) throws ServerError, ClientError {
//        Document document;
//        try {
//            loggingUtility.logInfo("UpdateWorkloadGroupEntity entry" + xml);
//            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder;
//            builder = builderFactory.newDocumentBuilder();
//            document = builder.parse(new InputSource(new StringReader(xml)));
//            NodeList nodeList = document.getElementsByTagName("workloadgroup");
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                Node node = nodeList.item(i);
//                String input = XMLUtility.nodeToString(nodeList.item(i));
//                String name = XPathUtil.getXPathValue("//name/text()", input);
//                if (StringUtility.isNotNullOrEmpty(name)) {
//                    workloadGroup.setName(name);
//                }
//                String nodeid = XPathUtil.getXPathValue("//nodeid/text()", input);
//                if(StringUtility.isNullOrEmpty(nodeid)){
//                    nodeid = XPathUtil.getXPathValue("//nodeid/@id()", input);
//                }
//                if (StringUtility.isNotNullOrEmpty(nodeid)) {
//                    workloadGroup.setNodeId(nodeid);
//                }
//                String resId = XPathUtil.getXPathValue("//resourceid/text()", input);
//                if (StringUtility.isNotNullOrEmpty(resId)) {
//                    workloadGroup.setResourceId(resId);
//                }else{
//                    workloadGroup.setResourceId(resourceId);
//                }
//                String sharable = XPathUtil.getXPathValue("//issharable/text()", input);
//                if (StringUtility.isNotNullOrEmpty(sharable)) {
//                    workloadGroup.setIssharable(sharable);
//                } else {
//                    workloadGroup.setIssharable("false");
//                }
//                String type = XPathUtil.getXPathValue("//type/text()", input);
//                if (StringUtility.isNotNullOrEmpty(type)) {
//                    workloadGroup.setType(type);
//                }
//                String version = XPathUtil.getXPathValue("//version/text()", input);
//                if (StringUtility.isNotNullOrEmpty(version)) {
//                    workloadGroup.setVersion(version);
//                }
//                String appVersion = XPathUtil.getXPathValue("//appversion/text()", input);
//                if (StringUtility.isNotNullOrEmpty(appVersion) && !appVersion.equals("null")) {
//                    workloadGroup.setAppversion(appVersion);
//                }
//                workloadGroup.setResourcetype(XPathUtil.getXPathValue("//resourcetype/text()", input));
//                String status = XPathUtil.getXPathValue("//status/text()", input);
//                if (StringUtility.isNotNullOrEmpty(status)) {
//                    workloadGroup.setStatus(status);
//                }
//                String metaData = XMLUtility.nodeToString(document.getElementsByTagName("bundles").item(i)).trim();
//                if (StringUtility.isNullOrEmpty(metaData)) {
//                    metaData = XPathUtil.getXPathValue("//metadata", input);
//                }
//                if (StringUtility.isNotNullOrEmpty(metaData)) {
//                    workloadGroup.setMetaData(metaData);
//                }
//
//                String prop = XPathUtil.getXPathValue("//properties", xml);
//                if (StringUtility.isNotNullOrEmpty(prop)) {
//                    log.logDebug(" properties ::: " + prop);
//                    workloadGroup.setProperties(prop);
//                }
//                String conn = XPathUtil.getXPathValue("//connection", xml);
//                if (StringUtility.isNotNullOrEmpty(conn)) {
//                    log.logDebug(" connection ::: " + conn);
//                    workloadGroup.setConnections(conn);
//                }
//                String fire = XPathUtil.getXPathValue("//firewall", xml);
//                if (StringUtility.isNotNullOrEmpty(fire)) {
//                    log.logDebug(" firewall ::: " + fire);
//                    workloadGroup.setFirewall(fire);
//                }
//                String devicex = XPathUtil.getXPathValue("//device", xml);
//                if (StringUtility.isNotNullOrEmpty(devicex)) {
//                    log.logDebug(" device ::: " + devicex);
//                    workloadGroup.setDevices(devicex);
//                }
//                workloadGroup.setUpdatedDate(new Date());
//            }
//
//        } catch (Exception ex) {
//            loggingUtility.logException(ex);
//            throw new ClientError("Input Xml is Invalid", "SAAS000005");
//        }
//    }
    
    /*public String parseServerException(ServerError serverError) {
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

     public String parseException(ServerError serverError) {
     String message = serverError.getMessage();
     Throwable inner = null;
     Throwable root = serverError;
     if ((inner = root.getCause()) != null) {
     message += " " + inner.getMessage();

     }
     StringBuilder sBuilder = new StringBuilder();
     sBuilder.append(Constants.XML_HEADER);
     sBuilder.append(Constants.ERROR_START_500);
     sBuilder.append("<error httpstatuscode=\"").append(serverError.getCode()).append("\">").append(message).append("</error>");
     sBuilder.append(Constants.END_RESPONSE);
     return sBuilder.toString();
     }

     public String parseClientException(ClientError clientError) {
     StringBuilder sBuilder = new StringBuilder();
     sBuilder.append(Constants.XML_HEADER);
     sBuilder.append(Constants.ERROR_START_500);
     sBuilder.append("<error httpstatuscode=\"").append(clientError.getCode()).append("\">").append(clientError.getMessage()).append("</error>");
     sBuilder.append(Constants.END_RESPONSE);
     return sBuilder.toString();
     }*/

    public String createSuccessResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append("<id>").append("Workload Group Created Successfully").append("</id>");
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

    public String createSingleSuccessResponse(WorkloadGroup workloadGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append("<id>").append(workloadGroup.getId()).append("</id>");
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }
    /* public String parseException(Exception exception, String errorCode) {
     StringBuilder sBuilder = new StringBuilder();
     sBuilder.append(Constants.XML_HEADER);
     sBuilder.append(Constants.ERROR_START_500);
     sBuilder.append("<error httpstatuscode=\"").append(errorCode).append("\">").append(exception.getMessage()).append("</error>");
     sBuilder.append(Constants.END_RESPONSE);
     return sBuilder.toString();
     }*/
    /*public String getNoRecoredFrame(String nounValue) {
     StringBuilder builder = new StringBuilder();
     builder.append(Constants.XML_HEADER);
     builder.append(Constants.SUCCESS_START_200);
     builder.append("<").append(nounValue).append(" totalrecords=\"0").append("\"").append(" limit=\"0").append("\"").append(" offset=\"0").append("\"").append("/>");
     builder.append(Constants.END_RESPONSE);
     return builder.toString();
     } 
     public String updateSuccessResponse() {
     StringBuilder sBuilder = new StringBuilder();
     sBuilder.append(Constants.XML_HEADER);
     sBuilder.append(Constants.SUCCESS_START_201);
     sBuilder.append(Constants.END_RESPONSE);
     return sBuilder.toString();
     }*/
}
