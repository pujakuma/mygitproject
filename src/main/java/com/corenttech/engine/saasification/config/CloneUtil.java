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
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.saasification.model.WSP;
import com.corenttech.engine.utility.DateUtility;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.RESTUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.URLUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.XMLUtility;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
 * @author volvo
 */
@Service("cloneUtil")
public class CloneUtil {

    @Autowired
    Integration integration;
    @Autowired
    SASFUtil sASFUtil;
    private LoggingUtility loggingUtility = LoggingUtility.getInstance(CloneUtil.class);

    public List<WSP> createWSPPojoList(String xml) throws ClientError {
        loggingUtility.logDebug(Constants.MODULE + "<<CloneUtil::createWSPPojoList()Start>>");
        loggingUtility.logDebug(Constants.MODULE + "<<CloneUtil::createWSPPojoList()XML>>" + xml);
        List<WSP> wspList = new ArrayList<>();
        try {
            Document document = XMLUtility.getXMLDocument(xml, false);
            NodeList nodelist = document.getElementsByTagName("wsp");
            for (int i = 0; i < nodelist.getLength(); i++) {
                WSP wsp = new WSP();
                Node node = nodelist.item(i);
                String nodeXml = XMLUtility.nodeToString(node);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element root = (Element) node;
                    String fromresourceid = XPathUtil.getXPathValue("//wsp/@id", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(fromresourceid)) {
                        wsp.setFromresourceid(fromresourceid);
                    }
                    String saasvariantid = XPathUtil.getXPathValue("//saasvariantid/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
                        wsp.setSaasvariantid(saasvariantid);
                    }
                    String nodeid = XPathUtil.getXPathValue("//nodeid/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(nodeid)) {
                        wsp.setNodeid(nodeid);
                    }
                    String tonodeid = XPathUtil.getXPathValue("//tonodeid/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(tonodeid)) {
                        wsp.setTonodeid(tonodeid);
                    }
                    String cloudresourceid = XPathUtil.getXPathValue("//cloudresourceid/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(cloudresourceid)) {
                        wsp.setCloudresourceid(cloudresourceid);
                    }
                    String workloadname = XPathUtil.getXPathValue("//workloadname/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(workloadname)) {
                        wsp.setWorkloadname(workloadname);
                    }
                    String workloadlocation = XPathUtil.getXPathValue("//workloadlocation/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(workloadlocation)) {
                        wsp.setWorkloadlocation(workloadlocation);//need to change pojo name of workloadlocation
                    }
                    String workloadversion = XPathUtil.getXPathValue("//workloadversion/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(workloadversion)) {
                        wsp.setWorkloadversion(workloadversion);
                    }
                    String workloadtype = XPathUtil.getXPathValue("//workloadtype/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(workloadtype)) {
                        wsp.setWorkloadtype(workloadtype);
                    }
                    String identity = XPathUtil.getXPathValue("//wsp/@id", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(identity)) {
                        wsp.setIdentity(identity);
                    }
                    String toidentity = XPathUtil.getXPathValue("//toidentity/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(toidentity)) {
                        wsp.setToidentity(toidentity);
                    }
                    String source = XPathUtil.getXPathValue("//source/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(source)) {
                        wsp.setSource(source);
                    }
                    String workloadrefid = XPathUtil.getXPathValue("//workloadrefid/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(workloadrefid)) {
                        wsp.setWorkloadrefid(workloadrefid);
                    }
                    String metadata = XPathUtil.getXPathValue("//metadata", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(metadata)) {
                        wsp.setMetadata(metadata);
                    }
                    String shareability = XPathUtil.getXPathValue("//shareability/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(shareability)) {
                        wsp.setShareability(shareability);
                    }
                    String effort = XPathUtil.getXPathValue("//effort", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(effort)) {
                        wsp.setEffort(effort);
                    }
                    String staaction = XPathUtil.getXPathValue("//staaction/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(staaction)) {
                        wsp.setStaaction(staaction);
                    }
                    String prop = XPathUtil.getXPathValue("//properties", nodeXml);
                    Node propertiesnode = root.getElementsByTagName("properties").item(0);
                    String properties = XMLUtility.nodeToString(propertiesnode);

                    if (StringUtility.isNotNullOrEmpty(properties) && (!properties.equalsIgnoreCase("<properties/>"))) {
                        wsp.setProperties(properties);
                    }
                    String wall = XPathUtil.getXPathValue("//firewall", nodeXml);
                    Node firewallsnode = root.getElementsByTagName("firewall").item(0);
                    String firewall = XMLUtility.nodeToString(firewallsnode);
                    if (StringUtility.isNotNullOrEmpty(firewall) && (!firewall.equalsIgnoreCase("<firewall/>"))) {
                        wsp.setFirewall(firewall);
                    }

                }
                wspList.add(wsp);
            }
        } catch (ParserConfigurationException | SAXException | IOException px) {
            loggingUtility.logDebug(px.getCause().toString());
            throw new ClientError("InvalidXML", SaasificationConsts.INVALID_XML);

        }
        loggingUtility.logDebug(Constants.MODULE + "<<CloneUtil::createWSPPojoList()END>>");

        return wspList;
    }

    public List<WSP> createDestWSPPojoList(String xml) throws ClientError {
        loggingUtility.logDebug(Constants.MODULE + "<<CloneUtil::createDestWSPPojoList()Start>>");
        loggingUtility.logDebug(Constants.MODULE + "<<CloneUtil::createDestWSPPojoList()XML>>" + xml);
        List<WSP> wspList = new ArrayList<>();
        try {
            Document document = XMLUtility.getXMLDocument(xml, false);
            NodeList nodelist = document.getElementsByTagName("wsp");
            for (int i = 0; i < nodelist.getLength(); i++) {
                WSP wsp = new WSP();
                Node node = nodelist.item(i);
                String nodeXml = XMLUtility.nodeToString(node);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element root = (Element) node;
                    String id = XPathUtil.getXPathValue("//wsp/@id", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(id)) {
                        wsp.setId(id);
                    }

                    String toidentity = XPathUtil.getXPathValue("//toidentity/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(toidentity)) {
                        wsp.setToidentity(toidentity);
                    }
                    String fromresourceid = XPathUtil.getXPathValue("//fromresourceid/text()", nodeXml);
                    if (StringUtility.isNotNullOrEmpty(fromresourceid)) {
                        wsp.setFromresourceid(fromresourceid);
                    }

                }
                wspList.add(wsp);
            }
        } catch (ParserConfigurationException | SAXException | IOException px) {
            loggingUtility.logDebug(px.getCause().toString());
            throw new ClientError("InvalidXML", SaasificationConsts.INVALID_XML);

        }

        loggingUtility.logDebug(Constants.MODULE + "<<CloneUtil::createDestWSPPojoList()END>>");

        return wspList;
    }

    public List<ArtifactSpec> createArtifactSpecPojo(Map<String, String> varmap) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpecUtil createArtifactSpecPojo() start --!>");
        String body = varmap.get("body");
        boolean iStemplateTenant = StringUtility.isNotNullOrEmpty(varmap.get("templateaccountid"));
        String accountid = varmap.get("account");
        String accountName = getAccountName(accountid);
        loggingUtility.logInfo("ArtifactSpecXMl xml :::: " + body);
        Document document = XMLUtility.getXMLDocument(body, false);
        NodeList nodelist = document.getElementsByTagName("artifactspec");
        List<ArtifactSpec> artSpecList = new ArrayList();
        for (int i = 0; i < nodelist.getLength(); i++) {
            ArtifactSpec artifactSpec = new ArtifactSpec();
            Node node = nodelist.item(i);
            String nodexml = XMLUtility.nodeToString(node);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element root = (Element) node;
                artifactSpec.setAccountid(accountid);
                String resourceid = XPathUtil.getXPathValue("//resourceid/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(resourceid)) {
                    artifactSpec.setResourceid(resourceid);
                }
                String saasvariantid = XPathUtil.getXPathValue("//saasvariantid/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
                    artifactSpec.setSaasvariantid(saasvariantid);
                }
                String wspid = XPathUtil.getXPathValue("//artifactspec/@wspid", nodexml);
//                wspid = varmap.get(wspid);
                if (StringUtility.isNotNullOrEmpty(wspid)) {
                    artifactSpec.setWspid(wspid);
                }
                String nodeid = XPathUtil.getXPathValue("//nodeid/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(nodeid)) {
                    artifactSpec.setNodeid(nodeid);
                }
                String sourceartificatidentity = XPathUtil.getXPathValue("//source_artificat_identity/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(sourceartificatidentity)) {
                    artifactSpec.setSourceartificatidentity(sourceartificatidentity);
                }
                String location = XPathUtil.getXPathValue("//location/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(location)) {
                    artifactSpec.setLocation(location);
                }
                String artifactidentitytype = XPathUtil.getXPathValue("//artifactidentitytype/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(artifactidentitytype)) {
                    artifactSpec.setArtifactidentitytype(artifactidentitytype);
                }
                String name = XPathUtil.getXPathValue("//name/text()", nodexml);
                loggingUtility.logDebug("<!--ArtifactSpecUtil createArtifactSpecPojo() name --!>" + name);
                if (StringUtility.isNotNullOrEmpty(name) && !artifactidentitytype.equalsIgnoreCase(SaasificationConsts.DB)) {
                    artifactSpec.setName(name);
                } else if (artifactidentitytype.equalsIgnoreCase(SaasificationConsts.DB) && SaasificationConsts.PROVISION.equalsIgnoreCase(varmap.get("action"))) {
                    if (StringUtility.isNotNullOrEmpty(accountName)) {
                        name = (accountName.length() > 10 ? accountName.substring(0, 10) : accountName) + "_" + sASFUtil.getGUID(5);
                    } else if (iStemplateTenant && accountid.equalsIgnoreCase(varmap.get("templateaccountid"))) {
                        name = varmap.get("account");
                    } else {
                        name = sASFUtil.getGUID();
                    }
                    artifactSpec.setName(name);
                    loggingUtility.logDebug("<!--ArtifactSpecUtil createArtifactSpecPojo() dbname --!>" + name);
                } else if (artifactidentitytype.equalsIgnoreCase(SaasificationConsts.DB) && !SaasificationConsts.PROVISION.equalsIgnoreCase(varmap.get("action"))) {
                    artifactSpec.setName(name);
                    loggingUtility.logDebug("<!--ArtifactSpecUtil createArtifactSpecPojo() dbname --!>" + name);
                } else {
                    throw new ClientError("name is invalid");
                }

                String artifactidentity = XPathUtil.getXPathValue("//artifactspec/@id", nodexml);
                if (SaasificationConsts.PROVISION.equalsIgnoreCase(varmap.get("action")) && SaasificationConsts.DB.equalsIgnoreCase(artifactidentitytype)) {
                    artifactidentity = sASFUtil.getGUID();
                    artifactSpec.setArtifactidentity(artifactidentity);
                } else if (StringUtility.isNotNullOrEmpty(artifactidentity)) {
                    artifactSpec.setArtifactidentity(artifactidentity);
                }

                String dependswspid = XPathUtil.getXPathValue("//depends_wspid/text()", nodexml);

                if (StringUtility.isNotNullOrEmpty(dependswspid)) {
                    artifactSpec.setDependsWspid(dependswspid);
                }
                String connectivity_artifact_idendity = XPathUtil.getXPathValue("//connectivity_artifact_idendity/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(connectivity_artifact_idendity)) {
                    artifactSpec.setConnectivityArtifactIdendity(connectivity_artifact_idendity);
                }
                String iscommon = XPathUtil.getXPathValue("//iscommon/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(iscommon)) {
                    artifactSpec.setIscommon(iscommon);
                }
                String action = XPathUtil.getXPathValue("//action/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(action)) {
                    artifactSpec.setIscommon(action);
                }
                String fromresourceid = XPathUtil.getXPathValue("//artifactspec/@id", nodexml);

                if (StringUtility.isNotNullOrEmpty(fromresourceid)) {
                    artifactSpec.setFromresourceid(fromresourceid);
                    artifactSpec.setSourceartificatidentity(fromresourceid);
                }
                String metadata = XPathUtil.getXPathValue("//metadata", nodexml);
                if (StringUtility.isNotNullOrEmpty(metadata)) {
                    artifactSpec.setMetadata(metadata);
                }
                Node propertiesnode = root.getElementsByTagName("properties").item(0);
                String properties = XMLUtility.nodeToString(propertiesnode);
                if (StringUtility.isNotNullOrEmpty(properties)) {
                    artifactSpec.setProperties(properties);
                }
                Node firewallsnode = root.getElementsByTagName("firewall").item(0);
                String firewall = XMLUtility.nodeToString(firewallsnode);
                if (StringUtility.isNotNullOrEmpty(firewall)) {
                    artifactSpec.setFirewall(firewall);
                }
                String deepscan = XPathUtil.getXPathValue("//deepscan", nodexml);
                if (StringUtility.isNotNullOrEmpty(deepscan)) {
                    artifactSpec.setDeepscan(deepscan);
                }
                String pluginfileid = XPathUtil.getXPathValue("//pluginfileid", nodexml);
                if (StringUtility.isNotNullOrEmpty(pluginfileid)) {
                    artifactSpec.setPluginfileid(pluginfileid);
                }
                String fromversion = XPathUtil.getXPathValue("//fromversion", nodexml);
                if (StringUtility.isNotNullOrEmpty(fromversion)) {
                    artifactSpec.setFromversion(fromversion);
                }
                String toversion = XPathUtil.getXPathValue("//toversion", nodexml);
                if (StringUtility.isNotNullOrEmpty(toversion)) {
                    artifactSpec.setToversion(toversion);
                }
                String fileguid = XPathUtil.getXPathValue("//fileguid", nodexml);
                if (StringUtility.isNotNullOrEmpty(fileguid)) {
                    artifactSpec.setFileguid(fileguid);
                }
                artifactSpec.setCreateDate(DateUtility.getCurrentDate());
            }
            artSpecList.add(artifactSpec);
        }
        return artSpecList;
    }

    public List<ArtifactSpec> createDestArtSpecPojo(String xml) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpecUtil createDestArtSpecPojo() start --!>");
        loggingUtility.logInfo("ArtifactSpecXMl xml :::: " + xml);
        Document document = XMLUtility.getXMLDocument(xml, false);
        NodeList nodelist = document.getElementsByTagName("artifactspec");
        List<ArtifactSpec> artSpecList = new ArrayList();
        for (int i = 0; i < nodelist.getLength(); i++) {
            ArtifactSpec artifactSpec = new ArtifactSpec();
            Node node = nodelist.item(i);
            String nodexml = XMLUtility.nodeToString(node);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element root = (Element) node;
                String id = XPathUtil.getXPathValue("//artifactspec/@id", nodexml);
                if (StringUtility.isNotNullOrEmpty(id)) {
                    artifactSpec.setId(id);
                }
                String sourceartificatidentity = XPathUtil.getXPathValue("//source_artificat_identity/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(sourceartificatidentity)) {
                    artifactSpec.setSourceartificatidentity(sourceartificatidentity);
                }
                String connectivity_artifact_idendity = XPathUtil.getXPathValue("//connectivity_artifact_idendity/text()", nodexml);
                if (StringUtility.isNotNullOrEmpty(connectivity_artifact_idendity)) {
                    artifactSpec.setConnectivityArtifactIdendity(connectivity_artifact_idendity);
                }
            }
            artSpecList.add(artifactSpec);
        }
        return artSpecList;
    }

    private String getAccountName(String accountid) throws ClientError {
        loggingUtility.logMethodEntry();
        String url = URLUtility.getcommonApiURL() + SaasificationConsts.ACCOUNT_GET_URL;
        url = url.replace("{account}", accountid);
        String method = RESTUtility.GET;
        loggingUtility.logDebug("url " + url);
        String accountxml = integration.restCallAPI(url, method, "");
        loggingUtility.logDebug("accountxml " + accountxml);
        String tenantname = XPathUtil.getXPathValue("//account/name/text()", accountxml);
        loggingUtility.logDebug("tenantname " + tenantname);
        loggingUtility.logMethodExit();
        return tenantname;
    }
}
