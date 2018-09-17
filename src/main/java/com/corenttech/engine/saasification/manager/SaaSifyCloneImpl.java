/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.CloneUtil;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.exception.SaasificationErrorCodeList;
import com.corenttech.engine.saasification.integration.Integration;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.saasification.model.WSP;
import com.corenttech.engine.utility.DateUtility;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.RESTUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.URLUtility;
import com.corenttech.engine.utility.XPathUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.StringJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author volvo
 */
@Service("saasifyCloneImpl")
public class SaaSifyCloneImpl implements SaaSifyClone {

    private LoggingUtility loggingUtility = LoggingUtility.getInstance(SaaSifyCloneImpl.class);
    @Autowired
    Integration integration;
    @Autowired
    CloneUtil cloneUtil;
    @Autowired
    SASFUtil sASFUtil;

    @Override
    public String cloneSpec(Map<String, String> varMap) throws ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--SaaSifyCloneImpl  cloneSpec start --!>");
        //clone the wsp
        clonewsp(varMap);
        //clone the artifactspec
        cloneArtifactSpec(varMap);

        return sASFUtil.createSuccessResponse(SaasificationConsts.UPDATESUCCESS);
    }

    public void clonewsp(Map<String, String> varMap) throws ClientError, ServerError, UnsupportedEncodingException {
        String artifactspec = varMap.get("cloneartifactspec");
        if (StringUtility.isNotNullOrEmpty(artifactspec) && artifactspec.equalsIgnoreCase("false")) {
            loggingUtility.logDebug(Constants.MODULE + "<!--SaaSifyCloneImpl  cloneSpec for WSP start --!>");
            //Get source wsp xml
            if (StringUtility.isNotNullOrEmpty(varMap.get("templateaccountid")) && !varMap.get("account").equalsIgnoreCase(varMap.get("templateaccountid"))) {
                varMap.put("parentid", varMap.get("templateaccountid"));
            }
            String wspSourceXML = integration.wspSpecDetails(varMap.get("parentid"), varMap.get("sourceid"), varMap.get("sourcenodeid"));
            String totalrecord = XPathUtil.getXPathValue("//wsps/@totalrecords", wspSourceXML);
            //Convert sourceXML into object
            if (StringUtility.isNotNullOrEmpty(totalrecord) && !totalrecord.equalsIgnoreCase("0")) {
                List<WSP> wspList = convertPojo(wspSourceXML);
                //Create WSP for destination
                creatWsp(varMap, wspList);
                //Get Destionation wsp xml to get toidentity for update   
                String wspDestinationXML = integration.wspSpecDetails(varMap.get("account"), varMap.get("destinationid"), varMap.get("destinationnodeid"));
                //Convert destionation xml into List with required identity
                List<WSP> wspDesList = convertDestPojo(wspDestinationXML);
                //update wsp spec with destination to identity
                updateWSPspecs(wspDesList, varMap);
            }

            loggingUtility.logDebug(Constants.MODULE + "<!--SaaSifyCloneImpl  cloneSpec for WSP END --!>");
        }

    }

    public void cloneArtifactSpec(Map<String, String> varMap) throws ClientError, ServerError, Exception {
        String wsp = varMap.get("clonewsp");
        if (StringUtility.isNotNullOrEmpty(wsp) && wsp.equalsIgnoreCase("false")) {
            loggingUtility.logDebug(Constants.MODULE + "<!--SaaSifyCloneImpl  cloneSpec for ArtifactSpec start --!>");
            //Get source artifactspec xml
            String artSpecSourceXML = integration.aritifactSpecDetails(varMap.get("parentid"), varMap.get("sourceid"), varMap.get("sourcenodeid"), varMap);
            //Convert sourceXML into object
            String totalrecord = XPathUtil.getXPathValue("//artifactspecs/@totalrecords", artSpecSourceXML);
            if (StringUtility.isNotNullOrEmpty(totalrecord) && !totalrecord.equalsIgnoreCase("0")) {
                varMap.put("body", artSpecSourceXML);
                List<ArtifactSpec> artSpecList = convertArtSpecPojo(varMap);
                //Get Destionation wsp xml to get toidentity for update   
                String wspDestinationXML = integration.wspSpecDetails(varMap.get("account"), varMap.get("destinationid"), varMap.get("destinationnodeid"));
                //Convert destionation xml into List for artifact spec wsp get
                List<WSP> wspDesList = convertDestPojo(wspDestinationXML);
                for (WSP wsps : wspDesList) {
                    varMap.put(wsps.getFromresourceid(), wsps.getId());
                }
                //Create WSP for ArtifactSpec
                creatArtSpec(varMap, artSpecList);
                //Get Destination artifactspec xml
                String artDestXML = integration.aritifactSpecDetails(varMap.get("account"), varMap.get("destinationid"), varMap.get("destinationnodeid"), varMap);
                //Convert destionation xml into List with required identity
                List<ArtifactSpec> desSpecList = convertDestArtSpecPojo(artDestXML);
                //update Artifactspecs identity
                updateArtifactSpecs(desSpecList, varMap);
            }
        }

    }

    public List<WSP> convertPojo(String wspSourceXML) throws ClientError {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertPojo Start>>");
        List<WSP> wspList = new ArrayList<>();
        String totalrecord = XPathUtil.getXPathValue("//response/wsps/@totalrecords", wspSourceXML);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertPojo totalrecords::" + totalrecord);
        if (StringUtility.isNotNullOrEmpty(totalrecord) & !totalrecord.equalsIgnoreCase("0")) {
            wspList = cloneUtil.createWSPPojoList(wspSourceXML);
        } else {
            throw new ClientError(SaasificationErrorCodeList.getErrorResponse(SaasificationConsts.INVALID_WSPID), SaasificationConsts.INVALID_WSPID);
        }
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertPojo END>>");
        return wspList;
    }

    public void creatWsp(Map<String, String> varMap, List<WSP> wspList) throws ClientError, ServerError, UnsupportedEncodingException {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl create wsp Start>>");
        String nodeid = "";
        for (WSP wsp : wspList) {
            String resourceid = varMap.get("destinationid");
            if (!resourceid.startsWith(SaasificationConsts.DEPLOYMENT)) {
                resourceid = getTopologyId(resourceid, varMap.get("account"));
                wsp.setNodeid(getfromNode(wsp.getNodeid(), varMap.get("account")));
                wsp.setTonodeid(getfromNode(wsp.getTonodeid(), varMap.get("account")));
            }
            //get destination node
            if (StringUtility.isNotNullOrEmpty(varMap.get("templateaccountid")) && !varMap.get("account").equalsIgnoreCase(varMap.get("templateaccountid"))) {
                String fromResourceId = getfromNode(wsp.getNodeid(), "");
                nodeid = getNode(resourceid, fromResourceId, varMap.get("account"));
            } else {
                nodeid = getNode(resourceid, wsp.getNodeid(), varMap.get("account"));
            }
            //Get Destination node value for to node
            String tonode = getNode(resourceid, wsp.getTonodeid(), varMap.get("account"));
            //GET workload ip if  wsp destination is deployment
            //String workloadip = resourceid.startsWith(SaasificationConsts.DEPLOYMENT) ? getWorkloadIp(varMap) : "";
            //loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl create wsp workloadip>>" + workloadip);
            String id = SaasificationConsts.WSP + sASFUtil.getGUID();
            String action = varMap.get("action");
            wsp.setId(id);
            wsp.setAccountid(varMap.get("account"));
            wsp.setResourceid(varMap.get("destinationid"));
            wsp.setNodeid(nodeid);
            wsp.setTonodeid(tonode);
            //wsp.setWorkloadip(workloadip);
            wsp.setStatus(Constants.INPROGRESS);
            wsp.setMessage(Constants.INPROGRESS);
            if (StringUtility.isNotNullOrEmpty(action)) {
                wsp.setAction(action);
            }
            wsp.setCreatedDate(DateUtility.getCurrentDate());
            integration.createWSPSpec(varMap, wsp);
            loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl create wsp END>>");
        }
    }

    public String getNode(String resourceid, String fromResourceid, String accountid) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getNode Start>>");
        String filter = "{fl:{fc1:{n:'fromresourceid',ty:'ST',op:'EQ',vl:['" + fromResourceid + "']},cond1:'AND',fc2:{n:'resourceid',ty:'ST',op:'EQ',vl:['" + resourceid + "']}}}";
        String nodexml = integration.nodeDetails(filter, accountid);
        String nodeid = XPathUtil.getXPathValue("//Node/@id", nodexml);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getNode nodeid>>" + nodeid);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getNode END>>");
        return nodeid;
    }

    /* public String getWorkloadIp(Map<String, String> varMap) throws ClientError, ServerError {
     loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl()getWorkloadIp() Start>>");
     String type = SaasificationConsts.INSTANCE;
     String ip = integration.getresourceGroup(varMap.get("destinationid"), type, varMap.get("destinationnodeid"));
     loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() getWorkloadIp() ip>>" + ip);
     loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() getWorkloadIp() END>>");
     return ip;
     }*/
    public List<WSP> convertDestPojo(String wspDestXML) throws ClientError {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() convertDestPojo() Start>>");
        List<WSP> wspList = new ArrayList<>();
        String totalrecord = XPathUtil.getXPathValue("//response/wsps/@totalrecords", wspDestXML);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() convertDestPojo()totalrecords::" + totalrecord);
        if (StringUtility.isNotNullOrEmpty(totalrecord) & !totalrecord.equalsIgnoreCase("0")) {
            wspList = cloneUtil.createDestWSPPojoList(wspDestXML);
        } else {
            throw new ClientError(SaasificationErrorCodeList.getErrorResponse(SaasificationConsts.INVALID_WSPID), SaasificationConsts.INVALID_WSPID);
        }
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() convertDestPojo()END>>");
        return wspList;
    }

    public void updateWSPspecs(List<WSP> wspDesList, Map<String, String> varMap) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() updateWSPspecs() Start>>");
        List<String> toIdentityList = new ArrayList<>();
        String wspIdentity = "";
        for (WSP desWSP : wspDesList) {
            if (StringUtility.isNotNullOrEmpty(desWSP.getToidentity())) {
                toIdentityList = Arrays.asList(desWSP.getToidentity().split(","));
                //to convert source toidentityid to destination identityid and update wsp toidentity
                updateWSPToIdentity(toIdentityList, varMap, desWSP.getId());
            }
        }
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() updateWSPspecs() END>>");
    }

    public void updateWSPToIdentity(List<String> identityList, Map<String, String> varMap, String id) throws ClientError, ServerError {
        loggingUtility.logMethodEntry();
        WSP wspNew = new WSP();
        //to convert source toidentityid to destination identityid
        StringJoiner identity = new StringJoiner(",");
        for (String wspid : identityList) {
            identity.add(varMap.get(wspid));
        }
        wspNew.setToidentity(identity.toString());
        integration.updateWSPSpec(wspNew, id, varMap);
        loggingUtility.logMethodExit();

    }

    public List<ArtifactSpec> convertArtSpecPojo(Map<String, String> varMap) throws ClientError, ServerError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertArtSpecPojo Start>>");
        List<ArtifactSpec> specList = new ArrayList<>();
        String totalrecord = XPathUtil.getXPathValue("//response/artifactspecs/@totalrecords", varMap.get("body"));
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertArtSpecPojo totalrecords::" + totalrecord);
        if (StringUtility.isNotNullOrEmpty(totalrecord) & !totalrecord.equalsIgnoreCase("0")) {
            specList = cloneUtil.createArtifactSpecPojo(varMap);
        } else {
            throw new ClientError(SaasificationErrorCodeList.getErrorResponse(SaasificationConsts.INVALID_ARTIFACTID), SaasificationConsts.INVALID_ARTIFACTID);
        }
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertPojo END>>");
        return specList;
    }

    public List<ArtifactSpec> convertDestArtSpecPojo(String ArtSourceXML) throws ClientError, ServerError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertDestArtSpecPojo Start>>");
        List<ArtifactSpec> specList = new ArrayList<>();
        String totalrecord = XPathUtil.getXPathValue("//response/artifactspecs/@totalrecords", ArtSourceXML);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertDestArtSpecPojo totalrecords::" + totalrecord);
        if (StringUtility.isNotNullOrEmpty(totalrecord) & !totalrecord.equalsIgnoreCase("0")) {
            specList = cloneUtil.createDestArtSpecPojo(ArtSourceXML);
        } else {
            throw new ClientError(SaasificationErrorCodeList.getErrorResponse(SaasificationConsts.INVALID_WSPID), SaasificationConsts.INVALID_WSPID);
        }
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl convertPojo END>>");
        return specList;
    }

    public void creatArtSpec(Map<String, String> varMap, List<ArtifactSpec> specList) throws ClientError, ServerError, UnsupportedEncodingException {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl creatArtSpec() Start>>");
        String action = varMap.get("action");
        String nodeid = "";
        for (ArtifactSpec artifactSpec : specList) {
            String resourceid = varMap.get("destinationid");
            if (!resourceid.startsWith(SaasificationConsts.DEPLOYMENT)) {
                resourceid = getTopologyId(resourceid, varMap.get("account"));
                artifactSpec.setNodeid(getfromNode(artifactSpec.getNodeid(), varMap.get("account")));
                artifactSpec.setSaasvariantid(varMap.get("destinationid"));
            }
            //get destination node
            if (StringUtility.isNotNullOrEmpty(varMap.get("templateaccountid")) && !varMap.get("account").equalsIgnoreCase(varMap.get("templateaccountid"))) {
                String fromResourceId = getfromNode(artifactSpec.getNodeid(), "");
                nodeid = getNode(resourceid, fromResourceId, varMap.get("account"));
            } else {
                nodeid = getNode(resourceid, artifactSpec.getNodeid(), varMap.get("account"));
            }
            String id = SaasificationConsts.ARTIFACTSPEC + sASFUtil.getGUID();
            artifactSpec.setId(id);
            artifactSpec.setAccountid(varMap.get("account"));
            artifactSpec.setResourceid(varMap.get("destinationid"));
            artifactSpec.setNodeid(nodeid);
            if (StringUtility.isNotNullOrEmpty(action)) {
                artifactSpec.setAction(action);
            }

            artifactSpec.setStatus(StringUtility.isNotNullOrEmpty(varMap.get("status")) ? varMap.get("status") : getCloneStatus(varMap));
            artifactSpec.setMessage(getCloneMessage(varMap));
            String wspid = artifactSpec.getWspid();
            String dependwspid = artifactSpec.getDependsWspid();
            artifactSpec.setWspid(varMap.get(wspid));
            artifactSpec.setDependsWspid(varMap.get(dependwspid));
            integration.createArtifactSpec(varMap, artifactSpec);
        }

        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl creatArtSpec() END>>");
    }

    public void updateArtifactSpecs(List<ArtifactSpec> specDesList, Map<String, String> varMap) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() updateArtifactSpecs() Start>>");
        List<String> dependsArtifacSpec = new ArrayList<>();
        for (ArtifactSpec desSpec : specDesList) {
            if (StringUtility.isNotNullOrEmpty(desSpec.getConnectivityArtifactIdendity())) {
                dependsArtifacSpec = Arrays.asList(desSpec.getConnectivityArtifactIdendity().split(","));
                updateArtifactSpec(dependsArtifacSpec, desSpec.getSourceartificatidentity(), desSpec.getId(), varMap);
            }
        }
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() updateArtifactSpecs() END>>");
    }

    public void updateArtifactSpec(List<String> dependsArtifactSpec, String sourceIdentity, String id, Map<String, String> varMap) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() updateArtifactSpec() Start>>");
        varMap.put("resourceid",varMap.get("destinationid"));
        ArtifactSpec updateSpec = new ArtifactSpec();
        StringJoiner dependsJoiner = new StringJoiner(",");
        for (String dependsArt : dependsArtifactSpec) {
            dependsJoiner.add(varMap.get(dependsArt));
        }
        updateSpec.setSourceartificatidentity(sourceIdentity);
        updateSpec.setConnectivityArtifactIdendity(dependsJoiner.toString());
        integration.updateArtifactSpec(varMap, updateSpec, id);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() updateArtifactSpec() END>>");
    }

    public String getTopologyId(String Saasvariantid, String accountid) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() getTopologyId() Start>>");
        String filter = "{fl:{fc1:{n:'saasvariantid',ty:'ST',op:'EQ',vl:['" + Saasvariantid + "']}}}";
        filter = URLEncoder.encode(filter, "UTF-8");
        String url = URLUtility.getApiURL() + SaasificationConsts.AVM_TOPOLOGY_URL.replace("{account}", accountid) + "&q=" + filter;
        String method = RESTUtility.GET;
        String inputxml = "";
        String callbackresponse = integration.restCallAPI(url, method, inputxml);
        String topologyid = XPathUtil.getXPathValue("//topologyid/text()", callbackresponse);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() getTopologyId() topologyid::" + topologyid);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl() getTopologyId() END>>");
        return topologyid;
    }

    public String getfromNode(String fromResourceid, String accountid) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getNode Start>>");
        String filter = "{fl:{fc1:{n:'id',ty:'ST',op:'EQ',vl:['" + fromResourceid + "']}}}";
        String nodexml = integration.nodeDetails(filter, accountid);
        String nodeid = XPathUtil.getXPathValue("//Node/@id", nodexml);
        String fromnode = XPathUtil.getXPathValue("//Node/fromResourceId/text()", nodexml);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getNode fromnodeid>>" + fromnode);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getNode END>>");
        return fromnode;
    }

    public String getCloneStatus(Map<String, String> varMap) {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getCloneStatus Start>>");
        String status = SaasificationConsts.PROVISION.equalsIgnoreCase(varMap.get("action")) ? Constants.INPROGRESS : SaasificationConsts.COMPLETED;
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getCloneStatus status::" + status);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getCloneStatus END>>");
        return status;

    }

    public String getCloneMessage(Map<String, String> varMap) {
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getCloneMessage Start>>");
        String message = varMap.get("action") + SaasificationConsts.COMPLETED;
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getCloneMessage Message::" + message);
        loggingUtility.logDebug(Constants.MODULE + "<<SaaSifyCloneImpl getCloneMessage END>>");
        return message;

    }
}
