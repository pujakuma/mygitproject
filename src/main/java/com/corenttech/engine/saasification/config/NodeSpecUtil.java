/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.config;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.NodeSpec;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.tags.tagexception.ErrorCodes;
import com.corenttech.utility.parsers.XMLUtility;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Naveen Kumar S
 */
@Service("nodeSpecUtil")
public class NodeSpecUtil {

    @Autowired
    private transient SASFUtil sASFUtil;
    LoggingUtility log = LoggingUtility.getInstance(NodeSpecUtil.class);

    public NodeSpec createNodeSpecPojo(String body) throws ParserConfigurationException, SAXException, IOException, ServerError {
        NodeSpec nodeSpec = new NodeSpec();
        Document document = XMLUtility.getXMLDocument(body, false);
        NodeList nodeSpecXmlList = document.getElementsByTagName("nodespec");
        for (int i = 0; i < nodeSpecXmlList.getLength(); i++) {
            Node nodeSpecXml = nodeSpecXmlList.item(i);
            Element root = (Element) nodeSpecXml;
            String resourceid = XMLUtility.getValueOfSubTag(nodeSpecXml, "resourceid");
            if (StringUtility.isNotNullOrEmpty(resourceid)) {
                log.logDebug(" resourceid ::: " + resourceid);
                nodeSpec.setResourceid(resourceid);
            }

            String saasvariantid = XMLUtility.getValueOfSubTag(nodeSpecXml, "saasvariantid");
            if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
                log.logDebug(" saasvariantid ::: " + saasvariantid);
                nodeSpec.setSaasvariantid(saasvariantid);
            }
            String nodeid = XMLUtility.getValueOfSubTag(nodeSpecXml, "nodeid");
            if (StringUtility.isNotNullOrEmpty(nodeid)) {
                log.logDebug(" nodeid ::: " + nodeid);
                nodeSpec.setNodeid(nodeid);
            }
            String prop = XPathUtil.getXPathValue("//properties", body);
            Node propertiesnode=root.getElementsByTagName("properties").item(0);
            String properties=XMLUtility.nodeToString(propertiesnode);
            if (StringUtility.isNotNullOrEmpty(properties)) {
                log.logDebug(" properties ::: " + properties);
                nodeSpec.setProperties(properties);
            }
            String conn = XPathUtil.getXPathValue("//connection", body);
            Node conneNode=root.getElementsByTagName("connection").item(0);
            String connection=XMLUtility.nodeToString(conneNode);
            if (StringUtility.isNotNullOrEmpty(connection)) {
                log.logDebug(" connection ::: " + connection);
                nodeSpec.setConnection(connection);
            }
            String fire = XPathUtil.getXPathValue("//firewall", body);
            Node fireWallNode=root.getElementsByTagName("firewall").item(0);
            String firewall=XMLUtility.nodeToString(fireWallNode);
            if (StringUtility.isNotNullOrEmpty(firewall)) {
                log.logDebug(" firewall ::: " + firewall);
                nodeSpec.setFirewall(firewall);
            }
            String devicex = XPathUtil.getXPathValue("//device", body);
            Node deviceNode=root.getElementsByTagName("device").item(0);
            String device=XMLUtility.nodeToString(deviceNode);
            if (StringUtility.isNotNullOrEmpty(device)) {
                log.logDebug(" device ::: " + device);
                nodeSpec.setDevice(device);
            }
            String metadata = XPathUtil.getXPathValue("//metadata", body);
            if (StringUtility.isNotNullOrEmpty(metadata)) {
                log.logDebug(" metadata ::: " + metadata);
                nodeSpec.setMetadata(metadata);
            }

            String status = XMLUtility.getValueOfSubTag(nodeSpecXml, "status");
            if (StringUtility.isNotNullOrEmpty(status)) {
                log.logDebug(" status ::: " + status);
                nodeSpec.setStatus(status);
            }
            String issharable = XMLUtility.getValueOfSubTag(nodeSpecXml, "issharable");
            if (StringUtility.isNotNullOrEmpty(issharable)) {
                log.logDebug(" issharable ::: " + issharable);
                nodeSpec.setIssharable(issharable);
            } else {
                nodeSpec.setIssharable("false");
                log.logDebug(" issharable ::: false");
            }
            String density = XMLUtility.getValueOfSubTag(nodeSpecXml, "density");
            if (StringUtility.isNotNullOrEmpty(density)) {
                log.logDebug(" density ::: " + density);
                nodeSpec.setDensity(density);
            } else {
                log.logDebug(" density ::: -1");
                nodeSpec.setDensity("-1");
            }
        }
        return nodeSpec;
    }

    
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

    public String createSuccessResponse(String nodeSpecID) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append("<nodespec>");
        sb.append("<id>").append(nodeSpecID).append("</id>");
        sb.append("</nodespec>");
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

    /*public String parseException(Exception exception) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append("500").append("\">").append(exception.getMessage()).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();

    }*/

    

    public String frameNodeSpecResponseXml(NodeSpec nodeSpec) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<nodespec id=\"" + nodeSpec.getId() + "\">");
        stringBuilder.append("<accountid>").append(nodeSpec.getAccountid()).append("</accountid>");
        stringBuilder.append("<resourceid>").append(nodeSpec.getResourceid()).append("</resourceid>");
        stringBuilder.append("<saasvariantid>").append(nodeSpec.getSaasvariantid()).append("</saasvariantid>");
        stringBuilder.append("<nodeid>").append(nodeSpec.getNodeid()).append("</nodeid>");
        stringBuilder.append("<issharable>").append(nodeSpec.getIssharable()).append("</issharable>");
        stringBuilder.append("<density>").append(nodeSpec.getDensity()).append("</density>");
        stringBuilder.append("<status>").append(nodeSpec.getStatus()).append("</status>");
        stringBuilder.append("<message>").append(nodeSpec.getMessage()).append("</message>");
        String properties=nodeSpec.getProperties();
                        if (StringUtility.isNotNullOrEmpty(properties)) {
                            stringBuilder.append(properties);
                        }else{
                        stringBuilder.append("<properties/>");
                        }
        String firewall=nodeSpec.getFirewall();
                        if (StringUtility.isNotNullOrEmpty(firewall)) {
                            stringBuilder.append(firewall);
                        }else{
                        stringBuilder.append("<firewall/>");
                        }
        String connection=nodeSpec.getConnection();
                        if (StringUtility.isNotNullOrEmpty(connection)) {
                            stringBuilder.append(connection);
                        }else{
                        stringBuilder.append("<connection/>");
                        }
         String device=nodeSpec.getDevice();
                        if (StringUtility.isNotNullOrEmpty(device)) {
                            stringBuilder.append(device);
                        }else{
                        stringBuilder.append("<device/>");
                        }
       
         String metadata = nodeSpec.getMetadata();
                        if (StringUtility.isNotNullOrEmpty(metadata)) {
                            stringBuilder.append("<metadata>").append(metadata).append("</metadata>");
                        } else {
                            stringBuilder.append("<metadata/>");
                        }
        stringBuilder.append("<createdate>").append(nodeSpec.getCreatedate()).append("</createdate>");
        Date updateddate = (null != nodeSpec.getUpdateddate()) ? nodeSpec.getUpdateddate() : null;
        stringBuilder.append("<updateddate>").append(updateddate).append("</updateddate>");
        Date deleteddate = (null != nodeSpec.getDeleteddate()) ? nodeSpec.getDeleteddate() : null;
        stringBuilder.append("<deleteddate>").append(deleteddate).append("</deleteddate>");
        stringBuilder.append("</nodespec>");
        return stringBuilder.toString();
    }

    public String setLimit(String limit) throws ClientError {
        if (!StringUtility.isNullOrEmpty(limit)) {
            if (!StringUtility.isInteger(limit)) {
                throw new ClientError(ErrorCodes.getErrorMsg(ErrorCodes.ERR_INVALID_LIMIT), ErrorCodes.ERR_INVALID_LIMIT);
            }
        } else {
            limit = "10";
        }
        return limit;
    }

    public String setOffset(String offset) throws ClientError {
        if (!StringUtility.isNullOrEmpty(offset)) {
            if (!StringUtility.isInteger(offset)) {
                throw new ClientError(ErrorCodes.getErrorMsg(ErrorCodes.ERR_INVALID_OFFSET), ErrorCodes.ERR_INVALID_OFFSET);
            }
        } else {
            offset = "0";
        }
        return offset;
    }

    public String getNodeSpecXml(List<NodeSpec> nodeSpecList, Map<String, String> nodeSpecAttributes) throws ClientError {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (null != nodeSpecList) {
                stringBuilder.append(Constants.XML_HEADER);
                stringBuilder.append(Constants.GET_SUCCESS);
                int _limit = StringUtility.convertStrToInt(setLimit(nodeSpecAttributes.get("limit")));
                int _offset = StringUtility.convertStrToInt(setOffset(nodeSpecAttributes.get("offset")));
                stringBuilder.append("<nodespecs totalrecords=\"").append(nodeSpecAttributes.get("size")).append("\" limit=\"").append(_limit).append("\" offset=\"").append(_offset).append("\">");
                for (NodeSpec nodeSpec : nodeSpecList) {
                    stringBuilder.append(frameNodeSpecResponseXml(nodeSpec));
                }
                stringBuilder.append("</nodespecs>");
                stringBuilder.append(Constants.END_RESPONSE);
            } else {
                stringBuilder.append(sASFUtil.getNoRecoredFrame("nodespecs"));
            }
        } catch (ClientError ex) {
            log.logError("Exception in getNodeSpecXml ::::" + ex.getMessage());
            ex.printStackTrace(System.err);
            throw new ClientError(ex);
        }
        return stringBuilder.toString();
    }

    
}