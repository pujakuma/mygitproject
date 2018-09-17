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
import com.corenttech.engine.saasification.model.WSP;
import com.corenttech.engine.saasification.model.KbArtifacts;
import com.corenttech.engine.utility.DateUtility;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.XMLUtility;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author puja
 */
@Service("wSPUtil")
public class WSPUtil {

    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient Integration integration;
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WSPUtil.class);

    public WSP createWspPojo(Map<String, String> varMap, WSP wsp) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--WSPUtil createWspPojo() start --!>");

        String body = varMap.get("body");
        String accountid = varMap.get("account");
        loggingUtility.logInfo("Wsp xml :::: " + body);
        String id = SaasificationConsts.WSP + sASFUtil.getGUID();
        wsp.setId(id);
        Document document = XMLUtility.getXMLDocument(body, false);
        NodeList nodelist = document.getElementsByTagName("wsp");
        Node node = nodelist.item(0);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element root = (Element) node;
            wsp.setAccountid(accountid);
            String resourceid = XPathUtil.getXPathValue("//resourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(resourceid)) {
                wsp.setResourceid(resourceid);
            }
            String saasvariantid = XPathUtil.getXPathValue("//saasvariantid/text()", body);
            if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
                wsp.setSaasvariantid(saasvariantid);
            }
            String nodeid = XPathUtil.getXPathValue("//nodeid/text()", body);
            if (StringUtility.isNotNullOrEmpty(nodeid)) {
                wsp.setNodeid(nodeid);
            }
            String tonodeid = XPathUtil.getXPathValue("//tonodeid/text()", body);
            if (StringUtility.isNotNullOrEmpty(tonodeid)) {
                wsp.setTonodeid(tonodeid);
            }
            String cloudresourceid = XPathUtil.getXPathValue("//cloudresourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(cloudresourceid)) {
                wsp.setCloudresourceid(cloudresourceid);
            }
            String workloadname = XPathUtil.getXPathValue("//workloadname/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadname)) {
                wsp.setWorkloadname(workloadname);
            }
            String workloadversion = XPathUtil.getXPathValue("//workloadversion/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadversion)) {
                wsp.setWorkloadversion(workloadversion);
            }
            String workloadtype = XPathUtil.getXPathValue("//workloadtype/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadtype)) {
                wsp.setWorkloadtype(workloadtype);
            }
            String workloadlocation = XPathUtil.getXPathValue("//workloadlocation/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadlocation)) {
                wsp.setWorkloadlocation(workloadlocation);
            }

            String identity = XPathUtil.getXPathValue("//identity/text()", body);
            if (StringUtility.isNullOrEmpty(identity)) {
                identity = SaasificationConsts.IDENTITY + sASFUtil.getGUID();
            }
            wsp.setIdentity(identity);
            String toidentity = XPathUtil.getXPathValue("//toidentity/text()", body);
            if (StringUtility.isNotNullOrEmpty(toidentity)) {
                wsp.setToidentity(toidentity);
            }
            String source = XPathUtil.getXPathValue("//workloadsource/text()", body);
            if (StringUtility.isNotNullOrEmpty(source)) {
                wsp.setSource(source);
            }
            String workloadrefid = XPathUtil.getXPathValue("//workloadrefid/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadrefid)) {
                wsp.setWorkloadrefid(workloadrefid);
            }
            String fromresourceid = XPathUtil.getXPathValue("//fromresourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(fromresourceid)) {
                wsp.setFromresourceid(fromresourceid);
            }

            String shareability = XPathUtil.getXPathValue("//shareability/text()", body);
            if (StringUtility.isNotNullOrEmpty(shareability)) {
                wsp.setShareability(shareability);
            }
            String action = XPathUtil.getXPathValue("//action/text()", body);
            if (StringUtility.isNotNullOrEmpty(action)) {
                wsp.setAction(action);
            }
            String effort = XPathUtil.getXPathValue("//effort", body);
            if (StringUtility.isNotNullOrEmpty(effort)) {
                wsp.setEffort(effort);
            }
            String staaction = XPathUtil.getXPathValue("//staaction/text()", body);

            if (StringUtility.isNotNullOrEmpty(staaction)) {
                wsp.setStaaction(staaction);
            }

            String status = XPathUtil.getXPathValue("//status/text()", body);

            if (StringUtility.isNotNullOrEmpty(status)) {
                wsp.setStatus(status);
            } else {
                wsp.setStatus("active");
            }

            String prop = XPathUtil.getXPathValue("//properties", body);
            Node propertiesnode = root.getElementsByTagName("properties").item(0);
            String properties = XMLUtility.nodeToString(propertiesnode);

            if (StringUtility.isNotNullOrEmpty(properties)) {
                wsp.setProperties(properties);
            }
            String wall = XPathUtil.getXPathValue("//firewall", body);
            Node firewallsnode = root.getElementsByTagName("firewall").item(0);
            String firewall = XMLUtility.nodeToString(firewallsnode);
            if (StringUtility.isNotNullOrEmpty(firewall)) {
                wsp.setFirewall(firewall);
            }
            String metadata = XPathUtil.getXPathValue("//metadata", body);
            if (StringUtility.isNotNullOrEmpty(metadata)) {
                wsp.setMetadata(metadata);
            }
            if (StringUtility.isNullOrEmpty(wsp.getMessage())) {
                wsp.setMessage("The wsp is active");
            }
            wsp.setCreatedDate(DateUtility.getCurrentDate());
        }

        return wsp;
    }

    public String frameWspResponseXml(WSP wsp, String ip, String clone) throws ClientError, ServerError {
        StringBuilder builder = new StringBuilder();
        builder.append("<wsp id=\"").append(wsp.getIdentity()).append("\">");
        if (SaasificationConsts.TRUE.equalsIgnoreCase(clone)) {
            builder.append("<localid>").append(wsp.getAccountid()).append("</localid>");
        }
        builder.append("<accountid>").append(wsp.getAccountid()).append("</accountid>");
        builder.append("<workloadname>").append(wsp.getWorkloadname()).append("</workloadname>");
        String workloadversion = wsp.getWorkloadversion();
        if (StringUtility.isNotNullOrEmpty(workloadversion)) {
            builder.append("<workloadversion>").append(workloadversion).append("</workloadversion>");
        } else {
            builder.append("<workloadversion/>");
        }
        builder.append("<resourceid>").append(wsp.getResourceid()).append("</resourceid>");
        String saasvariantid = wsp.getSaasvariantid();
        if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
            builder.append("<saasvariantid>").append(saasvariantid).append("</saasvariantid>");
        } else {
            builder.append("<cloudresourceid/>");
        }
        builder.append("<nodeid>").append(wsp.getNodeid()).append("</nodeid>");
        String workloadip = ip.equalsIgnoreCase(SaasificationConsts.TRUE) && wsp.getResourceid().startsWith(SaasificationConsts.DEPLOYMENT) ? getWorkloadIp(wsp.getResourceid(), wsp.getNodeid()) : "";
        if (StringUtility.isNotNullOrEmpty(workloadip)) {
            builder.append("<workloadip>").append(workloadip).append("</workloadip>");
        }
        String tonodeid = wsp.getTonodeid();
        if (StringUtility.isNotNullOrEmpty(tonodeid)) {
            builder.append("<tonodeid>").append(tonodeid).append("</tonodeid>");
        } else {
            builder.append("<tonodeid/>");
        }
        String cloudresourceid = wsp.getCloudresourceid();
        if (StringUtility.isNotNullOrEmpty(cloudresourceid)) {
            builder.append("<cloudresourceid>").append(cloudresourceid).append("</cloudresourceid>");
        } else {
            builder.append("<cloudresourceid/>");
        }
        builder.append("<workloadtype>").append(wsp.getWorkloadtype()).append("</workloadtype>");
        String source = wsp.getSource();
        if (StringUtility.isNotNullOrEmpty(source)) {
            builder.append("<workloadsource>").append(source).append("</workloadsource>");
        } else {
            builder.append("<workloadsource/>");
        }
        builder.append("<workloadlocation isencode=\"true\">").append(wsp.getWorkloadlocation()).append("</workloadlocation>");
        String identity = wsp.getIdentity();
        if (StringUtility.isNotNullOrEmpty(identity)) {
            builder.append("<identity isencode=\"true\">").append(identity).append("</identity>");
        } else {
            builder.append("<identity/>");
        }

        String toidentity = wsp.getToidentity();
        if (StringUtility.isNotNullOrEmpty(toidentity)) {
            builder.append("<toidentity>").append(toidentity).append("</toidentity>");
        } else {
            builder.append("<toidentity/>");
        }
        String workloadrefid = wsp.getWorkloadrefid();
        if (StringUtility.isNotNullOrEmpty(workloadrefid)) {
            builder.append("<workloadrefid>").append(workloadrefid).append("</workloadrefid>");
        } else {
            builder.append("<workloadrefid/>");
        }
        String fromresourceid = wsp.getFromresourceid();
        if (StringUtility.isNotNullOrEmpty(fromresourceid)) {
            builder.append("<fromresourceid>").append(fromresourceid).append("</fromresourceid>");
        } else {
            builder.append("<fromresourceid/>");
        }
        builder.append("<shareability>").append(wsp.getShareability()).append("</shareability>");
        builder.append("<action>").append(wsp.getAction()).append("</action>");

        String staaction = wsp.getStaaction();
        if (StringUtility.isNotNullOrEmpty(staaction)) {
            builder.append("<staaction>").append(staaction).append("</staaction>");
        } else {
            builder.append("<staaction/>");
        }

        String message = wsp.getMessage();
        if (StringUtility.isNotNullOrEmpty(message)) {
            builder.append("<message>").append(message).append("</message>");
        } else {
            builder.append("<message/>");
        }

        builder.append("<status>").append(wsp.getStatus()).append("</status>");

        String properties = wsp.getProperties();
        if (StringUtility.isNotNullOrEmpty(properties)) {
            builder.append(properties);
        } else {
            builder.append("<properties/>");
        }
        String firewall = wsp.getFirewall();
        if (StringUtility.isNotNullOrEmpty(firewall)) {
            builder.append(firewall);
        } else {
            builder.append("<firewall/>");
        }
        String effort = wsp.getEffort();
        if (StringUtility.isNotNullOrEmpty(effort)) {
            builder.append("<effort>").append(effort).append("</effort>");
        } else {
            builder.append("<effort/>");
        }
        String metadata = wsp.getMetadata();
        if (StringUtility.isNotNullOrEmpty(metadata)) {
            builder.append("<metadata>").append(metadata).append("</metadata>");
        } else {
            builder.append("<metadata/>");
        }

        builder.append("</wsp>");

        return builder.toString();
    }

    public String getWorkloadIp(String resourceid, String nodeid) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "<<WSPUtil()getWorkloadIp() Start>>");
        String type = SaasificationConsts.INSTANCE;
        String paas = integration.getNode(nodeid);
        String ip = paas.equalsIgnoreCase("yes") ? integration.getResourceDetailsForPaas(resourceid, nodeid) : integration.getresourceGroup(resourceid, type, nodeid);
        loggingUtility.logDebug(Constants.MODULE + "<<WSPUtil() getWorkloadIp() ip>>" + ip);
        loggingUtility.logDebug(Constants.MODULE + "<<WSPUtil() getWorkloadIp() END>>");
        return ip;
    }

    public WSP updateWspPojo(Map<String, String> varMap, WSP wsp) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--WSPUtil createWspPojo() start --!>");

        String body = varMap.get("body");
        String accountid = varMap.get("account");
        loggingUtility.logInfo("Wsp xml :::: " + body);
        String id = SaasificationConsts.WSP + sASFUtil.getGUID();
        wsp.setId(id);
        Document document = XMLUtility.getXMLDocument(body, false);
        NodeList nodelist = document.getElementsByTagName("wsp");
        Node node = nodelist.item(0);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element root = (Element) node;
            wsp.setAccountid(accountid);
            String resourceid = XPathUtil.getXPathValue("//resourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(resourceid)) {
                wsp.setResourceid(resourceid);
            }
            String saasvariantid = XPathUtil.getXPathValue("//saasvariantid/text()", body);
            if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
                wsp.setSaasvariantid(saasvariantid);
            }
            String nodeid = XPathUtil.getXPathValue("//nodeid/text()", body);
            if (StringUtility.isNotNullOrEmpty(nodeid)) {
                wsp.setNodeid(nodeid);
            }
            String tonodeid = XPathUtil.getXPathValue("//tonodeid/text()", body);
            if (StringUtility.isNotNullOrEmpty(tonodeid)) {
                wsp.setTonodeid(tonodeid);
            }
            String cloudresourceid = XPathUtil.getXPathValue("//cloudresourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(cloudresourceid)) {
                wsp.setCloudresourceid(cloudresourceid);
            }
            String workloadname = XPathUtil.getXPathValue("//workloadname/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadname)) {
                wsp.setWorkloadname(workloadname);
            }
            String workloadversion = XPathUtil.getXPathValue("//workloadversion/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadversion)) {
                wsp.setWorkloadversion(workloadversion);
            }
            String workloadtype = XPathUtil.getXPathValue("//workloadtype/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadtype)) {
                wsp.setWorkloadtype(workloadtype);
            }
            String workloadlocation = XPathUtil.getXPathValue("//workloadlocation/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadlocation)) {
                wsp.setWorkloadlocation(workloadlocation);
            }

            String identity = XPathUtil.getXPathValue("//identity/text()", body);
            if (StringUtility.isNullOrEmpty(identity)) {
                wsp.setIdentity(identity);
            }

            String toidentity = XPathUtil.getXPathValue("//toidentity/text()", body);
            if (StringUtility.isNotNullOrEmpty(toidentity)) {
                wsp.setToidentity(toidentity);
            }
            String source = XPathUtil.getXPathValue("//workloadsource/text()", body);
            if (StringUtility.isNotNullOrEmpty(source)) {
                wsp.setSource(source);
            }
            String workloadrefid = XPathUtil.getXPathValue("//workloadrefid/text()", body);
            if (StringUtility.isNotNullOrEmpty(workloadrefid)) {
                wsp.setWorkloadrefid(workloadrefid);
            }
            String fromresourceid = XPathUtil.getXPathValue("//fromresourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(fromresourceid)) {
                wsp.setFromresourceid(fromresourceid);
            }

            String shareability = XPathUtil.getXPathValue("//shareability/text()", body);
            if (StringUtility.isNotNullOrEmpty(shareability)) {
                wsp.setShareability(shareability);
            }
            String action = XPathUtil.getXPathValue("//action/text()", body);
            if (StringUtility.isNotNullOrEmpty(action)) {
                wsp.setAction(action);
            }
            String effort = XPathUtil.getXPathValue("//effort", body);
            if (StringUtility.isNotNullOrEmpty(effort)) {
                wsp.setEffort(effort);
            }
            String staaction = XPathUtil.getXPathValue("//staaction/text()", body);

            if (StringUtility.isNotNullOrEmpty(staaction)) {
                wsp.setStaaction(staaction);
            }

            String status = XPathUtil.getXPathValue("//status/text()", body);

            if (StringUtility.isNotNullOrEmpty(status)) {
                wsp.setStatus(status);
            } else {
                wsp.setStatus("active");
            }

            String prop = XPathUtil.getXPathValue("//properties", body);
            Node propertiesnode = root.getElementsByTagName("properties").item(0);
            String properties = XMLUtility.nodeToString(propertiesnode);

            if (StringUtility.isNotNullOrEmpty(properties)) {
                wsp.setProperties(properties);
            }
            String wall = XPathUtil.getXPathValue("//firewall", body);
            Node firewallsnode = root.getElementsByTagName("firewall").item(0);
            String firewall = XMLUtility.nodeToString(firewallsnode);
            if (StringUtility.isNotNullOrEmpty(firewall)) {
                wsp.setFirewall(firewall);
            }
            String metadata = XPathUtil.getXPathValue("//metadata", body);
            if (StringUtility.isNotNullOrEmpty(metadata)) {
                wsp.setMetadata(metadata);
            }
            if (StringUtility.isNullOrEmpty(wsp.getMessage())) {
                wsp.setMessage("The wsp is active");
            }
            wsp.setCreatedDate(DateUtility.getCurrentDate());
        }

        return wsp;
    }

    public String frameWspkpResponseXml(KbArtifacts kbArtifacts) throws ClientError, ServerError {
        StringBuilder builder = new StringBuilder();
        builder.append("<wspkp>");
        builder.append("<id>").append(kbArtifacts.getId()).append("</id>");
        builder.append("<guid>").append(kbArtifacts.getGuid()).append("</guid>");
        builder.append("<workloadname>").append(kbArtifacts.getWorkloadname()).append("</workloadname>");
        builder.append("<version>").append(kbArtifacts.getVersion()).append("</version>");
        builder.append("<os>").append(kbArtifacts.getOs()).append("</os>");
        builder.append("</wspkp>");
        return builder.toString();
    }
}
