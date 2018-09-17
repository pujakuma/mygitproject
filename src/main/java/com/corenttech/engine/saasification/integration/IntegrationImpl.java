/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.integration;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.dao.WSPDAO;
import com.corenttech.engine.saasification.manager.WSPManager;
import com.corenttech.engine.saasification.manager.ArtifactSpecManager;
import com.corenttech.engine.saasification.manager.NodeSpecManager;
import com.corenttech.engine.saasification.manager.WorkloadGroupManagerImpl;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.saasification.model.ProvisioningDSC;
import com.corenttech.engine.saasification.model.WSP;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.RESTUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.URLUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.PropertiesUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author puja
 */
@Service("saasIntegrationImpl")
public class IntegrationImpl implements Integration {

    @Autowired
    NodeSpecManager nodeSpecManager;
    @Autowired
    WSPManager wspManager;
    @Autowired
    WorkloadGroupManagerImpl workloadGroupManagerImpl;
    @Autowired
    SASFUtil sASFUtil;
    @Autowired
    WSPDAO wspdao;
    @Autowired
    ArtifactSpecManager artifactSpecManager;
    private final LoggingUtility log = LoggingUtility.getInstance(IntegrationImpl.class);
    String sharabilityServiceUrl = null;
    RESTUtility restUtility = new RESTUtility(60000);

    @Override
    public String restCallAPI(String resourceUrl, String method, String body) throws ClientError {
        log.logDebug(Constants.MODULE + "executeAPI[url]     : " + resourceUrl);
        log.logDebug(Constants.MODULE + "executeAPI[xml] : " + body);
        log.logDebug(Constants.MODULE + "executeAPI[method]  : " + method);
        String responsexml = null;
        BasicHeader[] headers = null;
        HttpResponse httpRes = null;
        try {
            httpRes = restUtility.request(true, method, resourceUrl, null, null, body, headers, null, false);
        } catch (IOException ex) {
            log.logDebug("Exception in Saasify restCallAPI ::::" + ex.getMessage());
            log.logError("Exception in Saasify restCallAPI ::::" + ex.getMessage());
            log.logException(ex);
            throw new ClientError(ex.getMessage(), "SFY1000014");
        }
        if (null != httpRes) {
            responsexml = restUtility.getResponseValue(httpRes);
        }
        return responsexml;
    }

    @Override
    public String restCallAPIWithSession(String resourceUrl, String method, String sessionId, String body) throws ClientError {
        log.logDebug(Constants.MODULE + "executeAPI[url]     : " + resourceUrl);
        log.logDebug(Constants.MODULE + "executeAPI[xml] : " + body);
        log.logDebug(Constants.MODULE + "executeAPI[method]  : " + method);
        String responsexml = null;
        RESTUtility restUtility = new RESTUtility(60000);
        BasicHeader[] headers = null;
        HttpResponse httpRes = null;
        try {
            httpRes = restUtility.request(true, method, resourceUrl, null, null, body, headers, null, false);
        } catch (IOException ex) {
            log.logDebug("Exception in Saasify restCallAPI ::::" + ex.getMessage());
            log.logError("Exception in Saasify restCallAPI ::::" + ex.getMessage());
            log.logException(ex);
            throw new ClientError(ex.getMessage(), "SFY1000014");
        }
        if (null != httpRes) {
            responsexml = restUtility.getResponseValue(httpRes);
        }
        return responsexml;
    }

    @Override
    public String restCallAPIWithTime(String resourceUrl, String method, String body) throws ClientError {
        log.logDebug(Constants.MODULE + "executeAPI[url]     : " + resourceUrl);
        log.logDebug(Constants.MODULE + "executeAPI[xml] : " + body);
        log.logDebug(Constants.MODULE + "executeAPI[method]  : " + method);
        String responsexml = null;
        RESTUtility restUtility = new RESTUtility(90000);
        BasicHeader[] headers = null;
        HttpResponse httpRes = null;
        try {
            httpRes = restUtility.request(true, method, resourceUrl, null, null, body, headers, null, false);
        } catch (IOException ex) {
            log.logDebug("Exception in Saasify restCallAPI ::::" + ex.getMessage());
            log.logError("Exception in Saasify restCallAPI ::::" + ex.getMessage());
            log.logException(ex);
            throw new ClientError(ex.getMessage(), "SFY1000014");
        }
        if (null != httpRes) {
            responsexml = restUtility.getResponseValue(httpRes);
        }
        return responsexml;
    }

    @Override
    public String getAgenttask(Map<String, String> varmap) throws ClientError {
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl getAgenttask() start --!>");
        String accountid = varmap.get("accountid");
        String taskid = varmap.get("taskid");
        String url = URLUtility.getAgentManagerURL();
        url = url + "/agentmanager/task?account=" + accountid + "";
        String q = "{fl:{fc1: { n :'id', ty:'ST', op: 'EQ', vl : ['" + taskid + "'] } } }";
        try {
            q = URLEncoder.encode(q, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(IntegrationImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        url = url + "&q=" + q + "";
        String method = RESTUtility.GET;

        String responsexml = restCallAPI(url, method, "");
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl getAgenttask() end --!>");
        return responsexml;
    }

    @Override
    public String updateArtifact(Map<String, String> varmap) throws ClientError {
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateArtifact() start --!>");
        String account = varmap.get("account");
        String artifactspecid = varmap.get("artifactid");
        String url = URLUtility.getApiURL();
        url = url + "/saasification/artifactspec/" + artifactspecid + "?account=" + account;
        String body = SaasificationConsts.UPDATEARTIFACTXML.replace("{status}", varmap.get("status")).replace("{message}", varmap.get("message"));
        String method = RESTUtility.PUT;
        String responsexml = restCallAPI(url, method, body);
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateArtifact() end --!>");
        return responsexml;
    }
    @Override
    public String getArtifact(Map<String, String> varmap) throws ClientError {
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl getArtifact() start --!>");
        String account = varmap.get("account");
        String artifactspecid = varmap.get("artifactid");
        String url = URLUtility.getApiURL();
        url = url + "/saasification/artifactspec/" + artifactspecid + "?account=" + account;
        String body = SaasificationConsts.UPDATEARTIFACTXML.replace("{status}", varmap.get("status")).replace("{message}", varmap.get("message"));
        String method = RESTUtility.GET;
        String responsexml = restCallAPI(url, method, body);
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl getArtifact() end --!>");
        return responsexml;
    }

    public String wspSpecDetails(String parentId, String resourceid, String nodeid) throws ClientError, ServerError {
        log.logDebug(Constants.MODULE + "<<IntegrationImpl wspSpecDetails Start >>");
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put("resourceid", resourceid);
        if (StringUtility.isNotNullOrEmpty(parentId)) {
            sourceMap.put("account", parentId);
        }
//        sourceMap.put("nodeid", nodeid);
        sourceMap.put("clone", "true");
        log.logDebug(Constants.MODULE + "<<IntegrationImpl workloadIdentityGet inputMap >>" + sourceMap);
        String identityResponse = wspManager.getWsp(sourceMap);
        log.logDebug(Constants.MODULE + "<<IntegrationImpl wspSpecDetails Response >>" + identityResponse);
        log.logDebug(Constants.MODULE + "<<IntegrationImpl wspSpecDetailst END >>");
        return identityResponse;
    }

    @Override
    public String updateClonePlan(Map<String, String> varmap) throws ClientError {
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateClonePlan() start --!>");
        //String accountid = varmap.get("accountid");
        String taskid = varmap.get("coetaskid");
        String coeplanid = varmap.get("coeplanid");
        String url = URLUtility.getApiURL();
        url = url + "/coe/plan?planId=" + coeplanid + "&action=callback&taskId=" + taskid;
        String body = SaasificationConsts.UPDATECLONEPLANXML.replace("{status}", varmap.get("status")).replace("{description}", varmap.get("message"));
        String method = RESTUtility.PUT;

        String responsexml = restCallAPIWithTime(url, method, body);
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateClonePlan() end --!>");
        return responsexml;

    }

    @Override
    public String createCloneForProvisioning(Map<String, String> varmap, ProvisioningDSC provisioningDSC) throws ClientError, ServerError {
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl createCloneForProvisioning() start --!>");
        boolean isTemplateTenant = false;
        isTemplateTenant = StringUtility.isNotNullOrEmpty(varmap.get("templateaccountid"));
        String account = varmap.get("account");
        String saasvariantid = provisioningDSC.getSaasvariantid();
        String sourcenodeid = provisioningDSC.getSourcenodeid();
        String destinationid = provisioningDSC.getDestinationdepid();
        String destinationnodeid = provisioningDSC.getDestnodeid();
        String templateaccountid = varmap.get("templateaccountid");
        String action = provisioningDSC.getAction();
        String url = URLUtility.getApiURL();
        if ((isTemplateTenant && account.equalsIgnoreCase(varmap.get("templateaccountid"))) || !isTemplateTenant) {
            url = url + "/saasification/saasifyclone?account=" + account + "&" + "sourceid=" + saasvariantid + "&" + "sourcenodeid=" + sourcenodeid + "&" + "destinationid=" + destinationid + "&" + "destinationnodeid=" + destinationnodeid + "&" + "templateaccountid=" + templateaccountid + "&" + "action=" + action;
        } else {
            url = url + "/saasification/saasifyclone?account=" + account + "&" + "sourceid=" + varmap.get("depid") + "&" + "sourcenodeid=" + varmap.get("zonenodeid") + "&" + "destinationid=" + destinationid + "&" + "destinationnodeid=" + destinationnodeid + "&" + "templateaccountid=" + templateaccountid + "&" + "action=" + action;
        }
        String method = RESTUtility.POST;
        String responsexml = restCallAPI(url, method, "");
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl createCloneForProvisioning() end --!>");
        return responsexml;
    }

    public String getWspForArtifact(Map<String, String> varmap) throws ClientError, ServerError {
        String wsp = wspManager.getWsp(varmap);
        return wsp;
    }

    @Override
    public String getClonePlan(String accountid, String saasvariantid) throws ClientError, ServerError, Exception {
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl getClonePlan() start --!>");

        String url = URLUtility.getApiURL();
        url = url + "/coe/plan?account=" + accountid;
        String q = "{fl:{fc1: { n :'resourceId', ty:'ST', op: 'EQ', vl : ['" + saasvariantid + "'] }, cond1: 'AND', fc2: { n: 'type', ty: 'ST', op: 'EQ', vl: [ 'MTABASE' ] } } }";
        try {
            q = URLEncoder.encode(q, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(IntegrationImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        url = url + "&q=" + q;
        String method = RESTUtility.GET;

        String responsexml = restCallAPI(url, method, "");
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl getClonePlan() end --!>");
        return responsexml;

    }

    @Override
    public void createWSPSpec(Map<String, String> varMap, WSP wsp) throws ClientError, ServerError {
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl createWSPSpec() start --!>");
        WSP wspnew = wspManager.createWsp(wsp);
        varMap.put(wspnew.getFromresourceid(), wspnew.getIdentity());
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl createWSPSpec() varMap --!>" + varMap);
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl createWSPSpec() END --!>");
    }

    @Override
    public void updateWSPSpec(WSP wsp, String id, Map<String, String> varMap) throws ClientError, ServerError {
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateWSPSpec() start --!>");
        String account = varMap.get("account");
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateWSPSpec() account --!>" + account);
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateWSPSpec() id to update --!>" + id);
        wsp = wspManager.updateWsp(account, id, wsp);
        String toidentity = wsp.getToidentity();
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateWSPSpec() toIdentity after update --!>" + toidentity);
        log.logDebug(Constants.MODULE + "<!--IntegrationImpl updateWSPSpec() END --!>");
    }

    @Override
    public String aritifactSpecDetails(String parentId, String resourceid, String nodeid, Map<String, String> varMap) throws ClientError, ServerError {
        log.logDebug(Constants.MODULE + "<<IntegrationImpl aritifactSpecDetails Start >>");
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put("resourceid", resourceid);
        if (StringUtility.isNotNullOrEmpty(parentId)) {
            sourceMap.put("account", parentId);
        }
        String toversionid = varMap.get("toversionid");
        if (StringUtility.isNotNullOrEmpty(toversionid)) {
            sourceMap.put("toversionid", toversionid);
        }
//        sourceMap.put("nodeid", nodeid);
        log.logDebug(Constants.MODULE + "<<IntegrationImpl aritifactSpecDetails inputMap >>" + sourceMap);
        String aritifactResponse = artifactSpecManager.getArtifactSpec(sourceMap);
        log.logDebug(Constants.MODULE + "<<IntegrationImpl aritifactSpecDetails Response >>" + aritifactResponse);
        log.logDebug(Constants.MODULE + "<<IntegrationImpl aritifactSpecDetails END >>");
        return aritifactResponse;
    }

    @Override
    public void createArtifactSpec(Map<String, String> varMap, ArtifactSpec artifactSpec) throws ClientError, ServerError {
        log.logDebug(Constants.MODULE + "<<IntegrationImpl createArtifactSpec Start >>");
        Map<String, String> sourceMap = new HashMap<>();
        ArtifactSpec artSpec = new ArtifactSpec();
        String accountId = varMap.get("account");
        log.logDebug(Constants.MODULE + "<<IntegrationImpl createArtifactSpec account >>" + accountId);
        List<ArtifactSpec> artifactSpecs = new ArrayList<>();
        artifactSpecs.add(artifactSpec);
        artSpec = artifactSpecManager.createArtifactSpec(artifactSpecs).get(0);
        log.logDebug(Constants.MODULE + "<<IntegrationImpl aritifactSpecId >>" + artSpec.getArtifactidentity());
        log.logDebug(Constants.MODULE + "<<IntegrationImpl fromSourceId >>" + artSpec.getFromresourceid());
        varMap.put(artSpec.getFromresourceid(), artSpec.getArtifactidentity());
        log.logDebug(Constants.MODULE + "<<IntegrationImpl aritifactSpecDetails END >>");
    }

    @Override
    public void updateArtifactSpec(Map<String, String> varMap, ArtifactSpec artifactSpec, String id) throws ClientError, ServerError {
        log.logDebug(Constants.MODULE + "<<IntegrationImpl createArtifactSpec Start >>");
        ArtifactSpec artSpec = new ArtifactSpec();
        String accountId = varMap.get("account");
        log.logDebug(Constants.MODULE + "<<IntegrationImpl createArtifactSpec account >>" + accountId);
        artSpec = artifactSpecManager.updateArtifactSpec(accountId, id, artifactSpec,varMap);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(IntegrationImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.logDebug(Constants.MODULE + "<<IntegrationImpl aritifactSpecUpdatedSource >>" + artSpec.getSourceartificatidentity());
        log.logDebug(Constants.MODULE + "<<IntegrationImpl artifactSpecupdatedDependency >>" + artSpec.getConnectivityArtifactIdendity());
        log.logDebug(Constants.MODULE + "<<IntegrationImpl aritifactSpecDetails END >>");
    }

    @Override
    public String nodeDetails(String filter, String account) throws ClientError, ServerError {
        log.logDebug(Constants.MODULE + "<<IntegrationImpl nodeDetails Start >>");
        log.logDebug(Constants.MODULE + "<<IntegrationImpl nodeDetails query >>" + filter);
        String q = "";
        try {
            q = URLEncoder.encode(filter, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(IntegrationImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String url = URLUtility.getApiURL() + "/avm/node?q=" + q;
        log.logDebug(Constants.MODULE + "<<IntegrationImpl nodeDetails url >>" + url);

        String nodexml = restCallAPI(url, RESTUtility.GET, "");
        log.logDebug(Constants.MODULE + "<<IntegrationImpl nodeDetails response >>" + nodexml);
        log.logDebug(Constants.MODULE + "<<IntegrationImpl nodeDetails END >>");
        return nodexml;
    }

    @Override
    public String getresourceGroup(String resourceid, String type, String nodeid) throws ClientError {
        log.logMethodEntry();
        String url = URLUtility.getApiURL() + "/cloud/resourcegroup";
        String q = "{fl:{fc1:{n:'resourceId',ty:'ST',op:'EQ',vl:['" + resourceid + "']},cond1:'AND',fc2:{n:'nodeId',ty:'ST',op:'EQ',vl:['" + nodeid + "']}}}";
        q = URLEncoder.encode(q);
        url = url + "?q=" + q + "&type=" + type;
        String resourcexml = restCallAPI(url, RESTUtility.GET, "");
        log.logDebug("getresourceGroup  resourcexml = " + resourcexml);
        String cloudresource = XPathUtil.getXPathValue("//cloudresource/text()", resourcexml);
        log.logDebug("getresourceGroup  cloudresource = " + cloudresource);
        String ip = XPathUtil.getXPathValue("//instance/properties/property[@name='publicip']/text()", cloudresource);
        log.logDebug("getresourceGroup  ip = " + ip);
        log.logMethodExit();
        return ip;
    }

    @Override
    public String getWspName(String id, Map<String, String> varMap) throws ServerError {
        String account = varMap.get("account");
        List<WSP> wsps = wspdao.getWspList(id, account);
        WSP wsp1 = new WSP();
        wsp1 = wsps.get(0);
        varMap.put("workloadversion", wsp1.getWorkloadversion());
        return wsp1.getWorkloadname();
    }

    public String getsessionId() throws ClientError {
        log.logMethodEntry();
        String sessionxml = "", sessionid = "";
        String url = URLUtility.getcommonApiURL() + "/" + SaasificationConsts.URLs.CEAPI_SESSIONS.getUrl();
        String inputxml = SaasificationConsts.SESSION_INPUT;
        String key = "f9865b17-b177-4fed-ac84-d4d684d5a57a";
        inputxml = inputxml.replace("{apikey}", key);
        sessionxml = restCallAPI(url, inputxml, "POST");
        sessionid = XPathUtil.getXPathValue("//sessionID/text()", sessionxml);
        log.logMethodExit();
        return sessionid;
    }

    public String getShareabilityServiceUrl() throws ServerError, ClientError {
        log.logMethodEntry();
        String sessionId = getsessionId();
        String commonApiUrl = URLUtility.getcommonApiURL() + SaasificationConsts.URLs.ROOT_CONFIG_URL.getUrl().replace(SaasificationConsts.ACCOUNT_TAG, SaasificationConsts.SUPER_ROOT);
        log.logDebug("sessionId ::: " + sessionId);
        log.logDebug("commonApiUrl ::: " + commonApiUrl);
        String resourcexml = restCallAPIWithSession(commonApiUrl, RESTUtility.GET, sessionId, "");
        if (StringUtility.isNotNullOrEmpty(resourcexml)) {
            log.logDebug("response of config GET ::: " + resourcexml);
            sharabilityServiceUrl = XPathUtil.getXPathValue("//response/SHAREABILITYSERVICE_URL/text()", resourcexml);
            log.logDebug("sharabilityServiceUrl ::: " + sharabilityServiceUrl);
            if (StringUtility.isNullOrEmpty(sharabilityServiceUrl)) {
                throw new ServerError("Sharability Service url is empty", resourcexml);
            }
        }
        log.logMethodExit();
        return StringUtility.isNotNullOrEmpty(sharabilityServiceUrl) ? sharabilityServiceUrl : null;
    }

    @Override
    public String getResourceDetailsForPaas(String resourceid, String nodeid) throws ClientError, ServerError {
        log.logMethodEntry();
        String url = URLUtility.getApiURL() + "/cloud/resourcedetails";
        String q = "{fl:{fc1:{n:'resourcetype',ty:'ST',op:'EQ',vl:['PAAS']},cond1:'AND',fc2:{n:'nodeId',ty:'ST',op:'EQ',vl:['" + nodeid + "']}}}";
        q = URLEncoder.encode(q);
        url = url + "?q=" + q;
        String resourcexml = restCallAPI(url, RESTUtility.GET, "");
        log.logDebug("getResourceDetails  resourcexml = " + resourcexml);
        String cloudresource = XPathUtil.getXPathValue("//resourcemeta/text()", resourcexml);
        log.logDebug("getResourceDetails  cloudresource = " + cloudresource);
        String ip = XPathUtil.getXPathValue("//cloudresourcedetails/properties/property[@name='endpoint']/text()", cloudresource);;
        log.logDebug("getResourceDetails  ip = " + ip);
        log.logMethodExit();
        return ip;
    }

    @Override
    public String getNode(String id) throws ClientError, ServerError {
        log.logMethodEntry();
        String url = URLUtility.getApiURL() + "/avm/node";
        String q = "{fl:{fc1:{n:'nodeid',ty:'ST',op:'EQ',vl:['" + id + "']}}}";
        q = URLEncoder.encode(q);
        url = url + "?q=" + q;
        String resourcexml = restCallAPI(url, RESTUtility.GET, "");
        log.logDebug("getNode  resourcexml = " + resourcexml);
        String Paas = XPathUtil.getXPathValue("//isPaas/text()", resourcexml);
        log.logDebug("getNode  isPaas = " + Paas);
        log.logMethodExit();
        return Paas;
    }
}
