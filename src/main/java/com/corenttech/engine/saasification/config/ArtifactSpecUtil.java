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
import com.corenttech.engine.utility.DateUtility;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.RESTUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.URLUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.XMLUtility;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
@Service("artifactSpecUtil")
public class ArtifactSpecUtil {

    @Autowired
    Integration integrationImpl;
    @Autowired
    private transient SASFUtil sASFUtil;
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(ArtifactSpecUtil.class);

    public List<ArtifactSpec> createArtifactSpecPojo(Map<String, String> varmap) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpecUtil createArtifactSpecPojo() start --!>");
        List<ArtifactSpec> artifactspc = new ArrayList<>();
        String body = varmap.get("body");
        String accountid = varmap.get("account");
        loggingUtility.logInfo("ArtifactSpecXMl xml :::: " + body);
        Document artsdocument = XMLUtility.getXMLDocument(body, false);
        NodeList artfactsnode = artsdocument.getElementsByTagName(body);
        for (int i = 0; i < artfactsnode.getLength(); i++) {
            ArtifactSpec artifactSpec = new ArtifactSpec();
            Node artifactnode = artfactsnode.item(i);
            String id = SaasificationConsts.ARTIFACTSPEC + sASFUtil.getGUID();
            artifactSpec.setId(id);
            Document document = XMLUtility.getXMLDocument(artifactnode.toString(), false);
            NodeList nodelist = document.getElementsByTagName("artifactspec");
            Node node = nodelist.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element root = (Element) node;
                artifactSpec.setAccountid(accountid);
                String resourceid = XPathUtil.getXPathValue("//resourceid/text()", body);
                if (StringUtility.isNotNullOrEmpty(resourceid)) {
                    artifactSpec.setResourceid(resourceid);
                }
                String saasvariantid = XPathUtil.getXPathValue("//saasvariantid/text()", body);
                if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
                    artifactSpec.setSaasvariantid(saasvariantid);
                }
                String nodeid = XPathUtil.getXPathValue("//nodeid/text()", body);
                if (StringUtility.isNotNullOrEmpty(nodeid)) {
                    artifactSpec.setNodeid(nodeid);
                }
                String wspid = XPathUtil.getXPathValue("//wspid/text()", body);
                if (StringUtility.isNotNullOrEmpty(wspid)) {
                    artifactSpec.setWspid(wspid);
                }
                String sourceartificatidentity = XPathUtil.getXPathValue("//source_artificat_identity/text()", body);
                if (StringUtility.isNotNullOrEmpty(sourceartificatidentity)) {
                    artifactSpec.setSourceartificatidentity(sourceartificatidentity);
                }
                String location = XPathUtil.getXPathValue("//location/text()", body);
                if (StringUtility.isNotNullOrEmpty(location)) {
                    artifactSpec.setLocation(location);
                }
                String artifactidentitytype = XPathUtil.getXPathValue("//artifactidentitytype/text()", body);
                if (StringUtility.isNotNullOrEmpty(artifactidentitytype)) {
                    artifactSpec.setArtifactidentitytype(artifactidentitytype);
                }
                String name = XPathUtil.getXPathValue("//name/text()", body);
                loggingUtility.logDebug("<!--ArtifactSpecUtil createArtifactSpecPojo() name --!>" + name);
                if (StringUtility.isNotNullOrEmpty(name)) {
                    artifactSpec.setName(name);
                }
                String artifactidentity = XPathUtil.getXPathValue("//artifactidentity/text()", body);
                if (StringUtility.isNullOrEmpty(artifactidentity)) {
                    artifactidentity = SaasificationConsts.ARTIFACTIDENTITY + sASFUtil.getGUID();
                }
                artifactSpec.setArtifactidentity(artifactidentity);

                String dependswspid = XPathUtil.getXPathValue("//depends_wspid/text()", body);
                if (StringUtility.isNotNullOrEmpty(dependswspid)) {
                    artifactSpec.setDependsWspid(dependswspid);
                }
                String connectivity_artifact_idendity = XPathUtil.getXPathValue("//connectivity_artifact_idendity/text()", body);
                if (StringUtility.isNotNullOrEmpty(connectivity_artifact_idendity)) {
                    artifactSpec.setConnectivityArtifactIdendity(connectivity_artifact_idendity);
                }
                String iscommon = XPathUtil.getXPathValue("//iscommon/text()", body);
                if (StringUtility.isNotNullOrEmpty(iscommon)) {
                    artifactSpec.setIscommon(iscommon);
                }
                String fromresourceid = XPathUtil.getXPathValue("//fromresourceid/text()", body);
                if (StringUtility.isNotNullOrEmpty(fromresourceid)) {
                    artifactSpec.setFromresourceid(fromresourceid);
                }
                String fromversion = XPathUtil.getXPathValue("//fromversion/text()", body);
                if (StringUtility.isNotNullOrEmpty(fromversion)) {
                    artifactSpec.setFromversion(fromversion);
                }
                String toversion = XPathUtil.getXPathValue("//toversion/text()", body);
                if (StringUtility.isNotNullOrEmpty(toversion)) {
                    artifactSpec.setToversion(toversion);
                }
                String fileguid = XPathUtil.getXPathValue("//fileguid/text()", body);
                if (StringUtility.isNotNullOrEmpty(fileguid)) {
                    artifactSpec.setFileguid(fileguid);
                }
                String metadata = XPathUtil.getXPathValue("//metadata", body);
                if (StringUtility.isNotNullOrEmpty(metadata)) {
                    artifactSpec.setMetadata(metadata);
                }
                String action = XPathUtil.getXPathValue("//action/text()", body);
                if (StringUtility.isNotNullOrEmpty(action)) {
                    artifactSpec.setAction(action);
                }
                String status = XPathUtil.getXPathValue("//status/text()", body);
                if (StringUtility.isNotNullOrEmpty(status)) {
                    artifactSpec.setStatus(status);
                } else {
                    artifactSpec.setStatus("active");
                }
                Node propertiesnode = root.getElementsByTagName("properties").item(0);
                String properties = XMLUtility.nodeToString(propertiesnode);
                if (StringUtility.isNotNullOrEmpty(properties)) {
                    artifactSpec.setProperties(properties);
                }

                String wall = XPathUtil.getXPathValue("//firewall", body);
                Node firewallsnode = root.getElementsByTagName("firewall").item(0);
                String firewall = XMLUtility.nodeToString(firewallsnode);
                if (StringUtility.isNotNullOrEmpty(firewall)) {
                    artifactSpec.setFirewall(firewall);
                }
                String deepscan = XPathUtil.getXPathValue("//deepscan", body);
                if (StringUtility.isNotNullOrEmpty(deepscan)) {
                    artifactSpec.setDeepscan(deepscan);
                }
                String pluginfileid = XPathUtil.getXPathValue("//pluginfileid", body);
                if (StringUtility.isNotNullOrEmpty(pluginfileid)) {
                    artifactSpec.setPluginfileid(pluginfileid);
                }
                if (StringUtility.isNullOrEmpty(artifactSpec.getMessage())) {
                    artifactSpec.setMessage("The workloadidentity is active");
                }
                artifactSpec.setCreateDate(DateUtility.getCurrentDate());
            }
            artifactspc.add(artifactSpec);
        }
        return artifactspc;
    }

    public String frameArtifactSpecResponseXml(ArtifactSpec artifactSpecDetails) {
        StringBuilder builder = new StringBuilder();
        builder.append("<artifactspec id=\"").append(artifactSpecDetails.getArtifactidentity()).append("\" wspid=\"").append(artifactSpecDetails.getWspid()).append("\">");
        builder.append("<accountid>").append(artifactSpecDetails.getAccountid()).append("</accountid>");
        builder.append("<name>").append(artifactSpecDetails.getName()).append("</name>");
        builder.append("<resourceid>").append(artifactSpecDetails.getResourceid()).append("</resourceid>");
        builder.append("<saasvariantid>").append(artifactSpecDetails.getSaasvariantid()).append("</saasvariantid>");
        builder.append("<nodeid>").append(artifactSpecDetails.getNodeid()).append("</nodeid>");
        builder.append("<artifactidentitytype>").append(artifactSpecDetails.getArtifactidentitytype()).append("</artifactidentitytype>");
        builder.append("<location isencode=\"true\">").append(artifactSpecDetails.getLocation()).append("</location>");
        String source_artificat_identity = artifactSpecDetails.getSourceartificatidentity();
        if (StringUtility.isNotNullOrEmpty(source_artificat_identity)) {
            builder.append("<source_artificat_identity>").append(source_artificat_identity).append("</source_artificat_identity>");
        } else {
            builder.append("<source_artificat_identity/>");
        }

        String connectivity_artifact_idendity = artifactSpecDetails.getConnectivityArtifactIdendity();
        if (StringUtility.isNotNullOrEmpty(connectivity_artifact_idendity)) {
            builder.append("<connectivity_artifact_idendity>").append(connectivity_artifact_idendity).append("</connectivity_artifact_idendity>");
        } else {
            builder.append("<connectivity_artifact_idendity/>");
        }
        String dependsWspid = artifactSpecDetails.getDependsWspid();
        if (StringUtility.isNotNullOrEmpty(dependsWspid)) {
            builder.append("<depends_wspid>").append(dependsWspid).append("</depends_wspid>");
        } else {
            builder.append("<depends_wspid/>");
        }
        builder.append("<iscommon>").append(artifactSpecDetails.getIscommon()).append("</iscommon>");
        builder.append("<fromresourceid>").append(artifactSpecDetails.getFromresourceid()).append("</fromresourceid>");
        builder.append("<fromversion>").append(artifactSpecDetails.getFromversion()).append("</fromversion>");
        builder.append("<toversion>").append(artifactSpecDetails.getToversion()).append("</toversion>");
        builder.append("<fileguid>").append(artifactSpecDetails.getFileguid()).append("</fileguid>");
        builder.append("<saasfileguid>").append(artifactSpecDetails.getSaasfileguid()).append("</saasfileguid>");
        builder.append("<taskid>").append(artifactSpecDetails.getTaskid()).append("</taskid>");
        builder.append("<action>").append(artifactSpecDetails.getAction()).append("</action>");
        builder.append("<status>").append(artifactSpecDetails.getStatus()).append("</status>");
        String message = artifactSpecDetails.getMessage();
        if (StringUtility.isNotNullOrEmpty(message)) {
            builder.append("<message>").append(message).append("</message>");
        } else {
            builder.append("<message/>");
        }
        String properties = artifactSpecDetails.getProperties();
        if (StringUtility.isNotNullOrEmpty(properties)) {
            builder.append(properties);
        } else {
            builder.append("<properties/>");
        }
        String firewall = artifactSpecDetails.getFirewall();
        if (StringUtility.isNotNullOrEmpty(firewall)) {
            builder.append(firewall);
        } else {
            builder.append("<firewall/>");
        }

        String metadata = artifactSpecDetails.getMetadata();
        if (StringUtility.isNotNullOrEmpty(metadata)) {
            builder.append("<metadata>").append(metadata).append("</metadata>");
        } else {
            builder.append("<metadata/>");
        }
        String deepscan = artifactSpecDetails.getDeepscan();
        if (StringUtility.isNotNullOrEmpty(deepscan)) {
            builder.append("<deepscan>").append(deepscan).append("</deepscan>");
        } else {
            builder.append("<deepscan/>");
        }
        String pluginfileid = artifactSpecDetails.getPluginfileid();
        if (StringUtility.isNotNullOrEmpty(pluginfileid)) {
            builder.append("<pluginfileid>").append(pluginfileid).append("</pluginfileid>");
        } else {
            builder.append("<pluginfileid/>");
        }

        builder.append("</artifactspec>");

        return builder.toString();
    }

    public String frameArtifactXml(ArtifactSpec artifactSpec) {
        StringBuilder builder = new StringBuilder();
        builder.append("<accountid>").append(artifactSpec.getAccountid()).append("</accountid>");
        builder.append("<resourceid>").append(artifactSpec.getResourceid()).append("</resourceid>");
        builder.append("<saasvariantid>").append(artifactSpec.getSaasvariantid()).append("</saasvariantid>");
        builder.append("<nodeid>").append(artifactSpec.getNodeid()).append("</nodeid>");
        String source_artificat_identity = artifactSpec.getSourceartificatidentity();
        if (StringUtility.isNotNullOrEmpty(source_artificat_identity)) {
            builder.append("<source_artificat_identity>").append(source_artificat_identity).append("</source_artificat_identity>");
        } else {
            builder.append("<source_artificat_identity/>");
        }
        builder.append("<location>").append(artifactSpec.getLocation()).append("</location>");
        builder.append("<name>").append(artifactSpec.getName()).append("</name>");
        builder.append("<artifactidentity>").append(artifactSpec.getArtifactidentity()).append("</artifactidentity>");
        builder.append("<artifactidentitytype>").append(artifactSpec.getArtifactidentitytype()).append("</artifactidentitytype>");

        String dependsWspid = artifactSpec.getDependsWspid();
        if (StringUtility.isNotNullOrEmpty(dependsWspid)) {
            builder.append("<depends_wspid>").append(dependsWspid).append("</depends_wspid>");
        } else {
            builder.append("<depends_wspid/>");
        }

        String connectivity_artifact_idendity = artifactSpec.getConnectivityArtifactIdendity();
        if (StringUtility.isNotNullOrEmpty(connectivity_artifact_idendity)) {
            builder.append("<connectivity_artifact_idendity>").append(connectivity_artifact_idendity).append("</connectivity_artifact_idendity>");
        } else {
            builder.append("<connectivity_artifact_idendity/>");
        }

        builder.append("<iscommon>").append(artifactSpec.getIscommon()).append("</iscommon>");
        builder.append("<fromresourceid>").append(artifactSpec.getFromresourceid()).append("</fromresourceid>");
        builder.append("<action>").append(artifactSpec.getAction()).append("</action>");
        builder.append("<status>").append(artifactSpec.getStatus()).append("</status>");

        String message = artifactSpec.getMessage();
        if (StringUtility.isNotNullOrEmpty(message)) {
            builder.append("<message>").append(message).append("</message>");
        } else {
            builder.append("<message/>");
        }

        String properties = artifactSpec.getProperties();
        if (StringUtility.isNotNullOrEmpty(properties)) {
            builder.append(properties);
        } else {
            builder.append("<properties/>");
        }
        String firewall = artifactSpec.getFirewall();
        if (StringUtility.isNotNullOrEmpty(firewall)) {
            builder.append(firewall);
        } else {
            builder.append("<firewall/>");
        }

        String metadata = artifactSpec.getMetadata();
        if (StringUtility.isNotNullOrEmpty(metadata)) {
            builder.append("<metadata>").append(metadata).append("</metadata>");
        } else {
            builder.append("<metadata/>");
        }
        String deepscan = artifactSpec.getDeepscan();
        if (StringUtility.isNotNullOrEmpty(deepscan)) {
            builder.append("<deepscan>").append(deepscan).append("</deepscan>");
        } else {
            builder.append("<deepscan/>");
        }
        String pluginfileid = artifactSpec.getPluginfileid();
        if (StringUtility.isNotNullOrEmpty(pluginfileid)) {
            builder.append("<pluginfileid>").append(pluginfileid).append("</pluginfileid>");
        } else {
            builder.append("<pluginfileid/>");
        }

        builder.append("</artifactspec>");

        return builder.toString();
    }

    public ArtifactSpec updateArtifactSpecPojo(Map<String, String> varmap, ArtifactSpec artifactSpec) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpecUtil createArtifactSpecPojo() start --!>");
        String body = varmap.get("body");
        String accountid = varmap.get("account");
        loggingUtility.logInfo("ArtifactSpecXMl xml :::: " + body);
        String id = "ASC-" + sASFUtil.getGUID();
        artifactSpec.setId(id);
        Document document = XMLUtility.getXMLDocument(body, false);
        NodeList nodelist = document.getElementsByTagName("artifactspec");
        Node node = nodelist.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element root = (Element) node;
            artifactSpec.setAccountid(accountid);
            String resourceid = XPathUtil.getXPathValue("//resourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(resourceid)) {
                artifactSpec.setResourceid(resourceid);
            }
            String saasvariantid = XPathUtil.getXPathValue("//saasvariantid/text()", body);
            if (StringUtility.isNotNullOrEmpty(saasvariantid)) {
                artifactSpec.setSaasvariantid(saasvariantid);
            }
            String nodeid = XPathUtil.getXPathValue("//nodeid/text()", body);
            if (StringUtility.isNotNullOrEmpty(nodeid)) {
                artifactSpec.setNodeid(nodeid);
            }
            String wspid = XPathUtil.getXPathValue("//wspid/text()", body);
            if (StringUtility.isNotNullOrEmpty(wspid)) {
                artifactSpec.setWspid(wspid);
            }
            String sourceartificatidentity = XPathUtil.getXPathValue("//source_artificat_identity/text()", body);
            if (StringUtility.isNotNullOrEmpty(sourceartificatidentity)) {
                artifactSpec.setSourceartificatidentity(sourceartificatidentity);
            }
            String location = XPathUtil.getXPathValue("//location/text()", body);
            if (StringUtility.isNotNullOrEmpty(location)) {
                String path = URLEncoder.encode(location);
                artifactSpec.setLocation(path);
            }
            String artifactidentitytype = XPathUtil.getXPathValue("//artifactidentitytype/text()", body);
            if (StringUtility.isNotNullOrEmpty(artifactidentitytype)) {
                artifactSpec.setArtifactidentitytype(artifactidentitytype);
            }
            String name = XPathUtil.getXPathValue("//name/text()", body);
            loggingUtility.logDebug("<!--ArtifactSpecUtil createArtifactSpecPojo() name --!>" + name);
            if (StringUtility.isNotNullOrEmpty(name)) {
                artifactSpec.setName(name);
            }

            String artifactidentity = XPathUtil.getXPathValue("//artifactidentity/text()", body);
            if (StringUtility.isNotNullOrEmpty(artifactidentity)) {
                artifactSpec.setArtifactidentity(artifactidentity);
            }

            String dependswspid = XPathUtil.getXPathValue("//depends_wspid/text()", body);
            if (StringUtility.isNotNullOrEmpty(dependswspid)) {
                artifactSpec.setDependsWspid(dependswspid);
            }
            String connectivity_artifact_idendity = XPathUtil.getXPathValue("//connectivity_artifact_idendity/text()", body);
            if (StringUtility.isNotNullOrEmpty(connectivity_artifact_idendity)) {
                artifactSpec.setConnectivityArtifactIdendity(connectivity_artifact_idendity);
            }
            String iscommon = XPathUtil.getXPathValue("//iscommon/text()", body);
            if (StringUtility.isNotNullOrEmpty(iscommon)) {
                artifactSpec.setIscommon(iscommon);
            }
            String fromresourceid = XPathUtil.getXPathValue("//fromresourceid/text()", body);
            if (StringUtility.isNotNullOrEmpty(fromresourceid)) {
                artifactSpec.setFromresourceid(fromresourceid);
            }
            String fromversion = XPathUtil.getXPathValue("//fromversion/text()", body);
            if (StringUtility.isNotNullOrEmpty(fromversion)) {
                artifactSpec.setFromversion(fromversion);
            }
            String toversion = XPathUtil.getXPathValue("//toversion/text()", body);
            if (StringUtility.isNotNullOrEmpty(toversion)) {
                artifactSpec.setToversion(toversion);
            }
            String fileguid = XPathUtil.getXPathValue("//fileguid/text()", body);
            if (StringUtility.isNotNullOrEmpty(fileguid)) {
                artifactSpec.setFileguid(fileguid);
            }
            String metadata = XPathUtil.getXPathValue("//metadata", body);
            if (StringUtility.isNotNullOrEmpty(metadata)) {
                artifactSpec.setMetadata(metadata);
            }
            String action = XPathUtil.getXPathValue("//action/text()", body);
            if (StringUtility.isNotNullOrEmpty(action)) {
                artifactSpec.setAction(action);
            }
            String status = XPathUtil.getXPathValue("//status/text()", body);
            if (StringUtility.isNotNullOrEmpty(status)) {
                artifactSpec.setStatus(status);
            }
            String prop = XPathUtil.getXPathValue("//properties", body);
            Node propertiesnode = root.getElementsByTagName("properties").item(0);
            String properties = XMLUtility.nodeToString(propertiesnode);
            if (StringUtility.isNotNullOrEmpty(properties)) {
                artifactSpec.setProperties(properties);
            }

            String wall = XPathUtil.getXPathValue("//firewall", body);
            Node firewallsnode = root.getElementsByTagName("firewall").item(0);
            String firewall = XMLUtility.nodeToString(firewallsnode);
            if (StringUtility.isNotNullOrEmpty(firewall)) {
                artifactSpec.setFirewall(firewall);
            }
            String deepscan = XPathUtil.getXPathValue("//deepscan", body);
            if (StringUtility.isNotNullOrEmpty(deepscan)) {
                artifactSpec.setDeepscan(deepscan);
            }
            String pluginfileid = XPathUtil.getXPathValue("//pluginfileid", body);
            if (StringUtility.isNotNullOrEmpty(pluginfileid)) {
                artifactSpec.setPluginfileid(pluginfileid);
            }
            String message = XPathUtil.getXPathValue("//message/text()", body);
            if (StringUtility.isNullOrEmpty(message)) {
                artifactSpec.setMessage("The workloadidentity is active");
            } else {
                artifactSpec.setMessage(message);
            }
            artifactSpec.setUpdateDate(DateUtility.getCurrentDate());

        }
        return artifactSpec;
    }

    public String getAvmSaaSBoxTemplateId(Map<String, String> varmap) throws ClientError {
        RESTUtility rESTUtility = new RESTUtility();
        String url = URLUtility.getApiURL() + SaasificationConsts.AVM_SAASBOX_GET_URL.replace("{account}", varmap.get("parentid")) + "&q=" + URLEncoder.encode("{fl:{fc1:{n:'resourceid',ty:'ST',op:'EQ',vl:['" + varmap.get("deploymentid") + "']}}}");
        String responseXml = integrationImpl.restCallAPI(url, RESTUtility.GET, "");
        String templateAccountId = XPathUtil.getXPathValue("//templateaccountid/text()", responseXml);
        return StringUtility.isNotNullOrEmpty(templateAccountId) ? templateAccountId : null;
    }
}
