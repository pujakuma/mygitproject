/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.ArtifactSpecUtil;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.WSPUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.corenttech.engine.saasification.dao.ArtifactSpecDAO;
import com.corenttech.engine.saasification.dao.WSPDAO;
import com.corenttech.engine.saasification.integration.Integration;
import com.corenttech.engine.saasification.model.WSP;
import com.corenttech.engine.utility.RESTUtility;
import com.corenttech.engine.utility.URLUtility;
import com.corenttech.engine.utility.XPathUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author puja
 */
@Service("artifactSpecManagerImpl")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class ArtifactSpecManagerImpl implements ArtifactSpecManager {

    @Autowired
    private transient WSPDAO wspdao;
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(ArtifactSpecManagerImpl.class);
    LoggingUtility log = LoggingUtility.getInstance(ArtifactSpecManagerImpl.class);
    @Autowired
    private transient ArtifactSpecDAO artifactSpecDAO;
    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient ArtifactSpecUtil artifactSpecUtil;
    @Autowired
    private transient WSPUtil wSPUtil;
    @Autowired
    private transient Integration integration;

    @Override
    public List<ArtifactSpec> createArtifactSpec(List<ArtifactSpec> artifactSpec) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpec createArtifactSpec() start --!>");
        if (null != artifactSpec) {
            try {
                for (ArtifactSpec artifactSpec1 : artifactSpec) {
                    artifactSpecDAO.create(artifactSpec1);
                }
            } catch (ServerError ex) {
                log.logException(ex);
                log.logError("Exception in createArtifactSpec - ManagerImpl" + ex.getMessage() + ex.getCause().toString());
                throw new ServerError(ex.getCause().toString(), SaasificationConsts.DB_ERROR);
            }
        } else {
            throw new ClientError("Could Not Create ArtifactSpec ", SaasificationConsts.UNABLETO_CREATE);
        }
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpec createArtifactSpec() end --!>");
        return artifactSpec;
    }

    @Override
    public ArtifactSpec updateArtifactSpec(String accountid, String id, ArtifactSpec artifactSpec, Map<String, String> varMap) throws ClientError, ServerError {
        List<ArtifactSpec> artifactSpecList = artifactSpecDAO.getArtifactSpecList(id, accountid, varMap);
        ArtifactSpec oldspec = new ArtifactSpec();

        oldspec = artifactSpecList.get(0);
        if (oldspec != null) {

            if (StringUtility.isNotNullOrEmpty(artifactSpec.getResourceid())) {
                oldspec.setResourceid(artifactSpec.getResourceid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getSaasvariantid())) {
                oldspec.setSaasvariantid(artifactSpec.getSaasvariantid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getNodeid())) {
                oldspec.setNodeid(artifactSpec.getNodeid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getWspid())) {
                oldspec.setWspid(artifactSpec.getWspid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getSourceartificatidentity())) {
                oldspec.setSourceartificatidentity(artifactSpec.getSourceartificatidentity());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getLocation())) {
                oldspec.setLocation(artifactSpec.getLocation());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getName())) {
                oldspec.setName(artifactSpec.getName());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getArtifactidentity())) {
                oldspec.setArtifactidentity(artifactSpec.getArtifactidentity());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getArtifactidentitytype())) {
                oldspec.setArtifactidentitytype(artifactSpec.getArtifactidentitytype());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getDependsWspid())) {
                oldspec.setDependsWspid(artifactSpec.getDependsWspid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getConnectivityArtifactIdendity())) {
                oldspec.setConnectivityArtifactIdendity(artifactSpec.getConnectivityArtifactIdendity());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getIscommon())) {
                oldspec.setIscommon(artifactSpec.getIscommon());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getFromresourceid())) {
                oldspec.setFromresourceid(artifactSpec.getFromresourceid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getMetadata())) {
                oldspec.setMetadata(artifactSpec.getMetadata());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getStatus())) {
                oldspec.setStatus(artifactSpec.getStatus());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getAction())) {
                oldspec.setAction(artifactSpec.getAction());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getMessage())) {
                oldspec.setMessage(artifactSpec.getMessage());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getProperties())) {
                oldspec.setProperties(artifactSpec.getProperties());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getFirewall())) {
                oldspec.setFirewall(artifactSpec.getFirewall());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getDeepscan())) {
                oldspec.setDeepscan(artifactSpec.getDeepscan());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getPluginfileid())) {
                oldspec.setPluginfileid(artifactSpec.getPluginfileid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getFromversion())) {
                oldspec.setFromversion(artifactSpec.getFromversion());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getToversion())) {
                oldspec.setToversion(artifactSpec.getToversion());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getFileguid())) {
                oldspec.setFileguid(artifactSpec.getFileguid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getSaasfileguid())) {
                oldspec.setSaasfileguid(artifactSpec.getSaasfileguid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getTaskid())) {
                oldspec.setTaskid(artifactSpec.getTaskid());
            }
            artifactSpec.setUpdateDate(new Date());
            artifactSpecDAO.update(oldspec);
            return oldspec;
        } else {
            throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
        }
    }

    @Override
    public String getArtifactSpec(Map<String, String> varmap) throws ServerError, ClientError {
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() start");
        try {
            StringBuilder builder = new StringBuilder();
            String getSpec = "";
            String templateAccount = "";
            int limit = 0;
            int offset = 0;
            String filter = "";
            String order = "";
            String accountId = varmap.get("account");
            String id = varmap.get("id");
            if (varmap.containsKey("limit") && varmap.containsKey("offset")) {
                limit = Integer.parseInt(varmap.get("limit").equalsIgnoreCase("") ? "10" : varmap.get("limit"));
                offset = Integer.parseInt(varmap.get("offset").equalsIgnoreCase("") ? "0" : varmap.get("offset"));
            }
            if (varmap.containsKey("order") && !varmap.get("offset").isEmpty()) {
                order = URLDecoder.decode(varmap.get("order"));
            }
            if (varmap.containsKey("q") && !varmap.get("q").isEmpty()) {
                filter = URLDecoder.decode(varmap.get("q"));
            }
            if (StringUtility.isNotNullOrEmpty(varmap.get("deploymentid")) && StringUtility.isNotNullOrEmpty(varmap.get("parentid"))) {
                templateAccount = artifactSpecUtil.getAvmSaaSBoxTemplateId(varmap);
                varmap.put("templateid", templateAccount);
            }
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() accountId " + accountId);
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() id " + id);
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() limit " + limit);
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() offset " + offset);
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() order " + order);
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() filter " + filter);
            List<ArtifactSpec> artifactSpecsList = artifactSpecDAO.getArtifactSpecList(templateAccount, accountId, limit, offset, order, filter, varmap, id);
            String parentid = varmap.get("parentid");
            if (!artifactSpecsList.isEmpty()) {
                builder.append(Constants.XML_HEADER);
                builder.append(Constants.GET_SUCCESS);
                builder.append("<artifactspecs totalrecords=\"").append(artifactSpecsList.size()).append("\" limit=\"").append(limit).append("\" offset=\"").append(offset).append("\">");
                Set<String> wspset = new HashSet<>();
                List<String> dependartifact = new ArrayList<>();
                List<String> source_artificat_identity = new ArrayList<>();
                String showdependency = varmap.get("showdependency");
                String provisioningSpec = varmap.get("provisioningSpec");
                for (ArtifactSpec artifactSpecDetails : artifactSpecsList) {

                    builder.append(artifactSpecUtil.frameArtifactSpecResponseXml(artifactSpecDetails));
                    wspset.add(artifactSpecDetails.getWspid());
                    if (StringUtility.isNotNullOrEmpty(artifactSpecDetails.getConnectivityArtifactIdendity()) && (Boolean.valueOf(showdependency) || Boolean.valueOf(provisioningSpec))) {
                        dependartifact = Arrays.asList(artifactSpecDetails.getConnectivityArtifactIdendity().split(","));
                    }

                    if (Boolean.valueOf(showdependency)) {
                        builder.append("<dependencyartifacts>");
                        String dependartifactIdDetails = getdDependsArtifact(varmap, dependartifact);
                        builder.append(dependartifactIdDetails);
                        builder.append("</dependencyartifacts>");
                    }

                    if (Boolean.valueOf(provisioningSpec)) {
                        builder.append("<provisionspec>");
                        source_artificat_identity = Arrays.asList(artifactSpecDetails.getSourceartificatidentity());
                        builder.append(getdSourceArtifact(varmap, source_artificat_identity, parentid));
                        builder.append(getdConectivityArtifact(dependartifact, accountId));
                        builder.append("</provisionspec>");
                    }
                }

                builder.append("</artifactspecs>");
                String showwsp = varmap.get("showwsp");
                if (Boolean.valueOf(showwsp)) {
                    builder.append("<wsps>");
                    String wspdetails = getWspForArtifact(varmap, wspset);
                    builder.append(wspdetails);
                    builder.append("</wsps>");
                }
                builder.append(Constants.END_RESPONSE);
                getSpec = builder.toString();
                loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() END");
                return getSpec;
            } else {
                return sASFUtil.getNoRecoredFrame("artifactspecs");
            }
        } catch (ClientError ex) {
            throw new ClientError(ex.getMessage(), ex.getCode());
        } catch (ServerError | NumberFormatException ex) {
            loggingUtility.logError("Exception in getArtifactSpec ::::" + ex.getMessage());
            loggingUtility.logException(ex);
            throw new ClientError(ex.getMessage(), sASFUtil.getErrorMessage(SaasificationConsts.UNABLETO_GETTHERECORD, true));
        }

    }

    public String getWspForArtifact(Map<String, String> varmap, Set<String> wspset) throws ServerError, VerifyError, ClientError {
        StringBuilder builder = new StringBuilder();
        String account = varmap.get("account");

        Iterator<String> it = wspset.iterator();
        while (it.hasNext()) {
            String next = (it.next());
            List<WSP> wspDetails = wspdao.getWspList(next, account);

            if (!wspDetails.isEmpty()) {
                for (WSP wspxml : wspDetails) {
                    builder.append(wSPUtil.frameWspResponseXml(wspxml, SaasificationConsts.TRUE, SaasificationConsts.FALSE));
                }
            }
        }
        return builder.toString();
    }

    public String getdDependsArtifact(Map<String, String> varmap, List<String> dependartifact) throws ServerError, VerifyError, ClientError {
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getdDependsArtifact() start");
        StringBuilder builder = new StringBuilder();
        Set<String> wspsetFordepends = new HashSet<>();

        for (String dependartifactid : dependartifact) {
            List<ArtifactSpec> artifactSpecsList = artifactSpecDAO.getArtifactSpecList(dependartifactid, varmap.get("account"));
            // List<String> target = new ArrayList<>();
            List<String> source = new ArrayList<>();
            if (!artifactSpecsList.isEmpty()) {
                for (ArtifactSpec artifactSpec : artifactSpecsList) {
                    builder.append("<dependencyartifact>");
                    builder.append(artifactSpecUtil.frameArtifactSpecResponseXml(artifactSpec));
                    source = Arrays.asList(artifactSpec.getSourceartificatidentity());
                    // target = Arrays.asList(artifactSpec.getConnectivityArtifactIdendity().split(","));
                    builder.append(getdSourceArtifact(varmap, source, varmap.get("parentid")));
                    // builder.append(getdConectivityArtifact(target));
                    builder.append("</dependencyartifact>");

                    wspsetFordepends.add(artifactSpec.getWspid());

                }
                builder.append("<wsps>");
                builder.append(getWspForArtifact(varmap, wspsetFordepends));
                builder.append("</wsps>");
            }
        }
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getdDependsArtifact() end");
        return builder.toString();
    }

    private String getdSourceArtifact(Map<String, String> varmap, List<String> source_artificat_identity, String parentId) throws ServerError {
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getdSourceArtifact() start");
        StringBuilder builder = new StringBuilder();
        String templateId = varmap.get("templateid");
        String type = "";
        if ((StringUtility.isNotNullOrEmpty(templateId) && varmap.get("account").equalsIgnoreCase(templateId)) || StringUtility.isNullOrEmpty(templateId)) {
            type = "sascan";
        } else {
            type = "provisioning";
        }
        for (String sourceartifactid : source_artificat_identity) {
            List<ArtifactSpec> artifactSpecsList = artifactSpecDAO.getSourceArtifactSpecList(sourceartifactid, parentId, type);
            if (!artifactSpecsList.isEmpty()) {
                for (ArtifactSpec artifactSpec : artifactSpecsList) {
                    builder.append("<artifactspec id=\"").append(artifactSpec.getArtifactidentity()).append("\" wspid=\"").append(artifactSpec.getWspid()).append("\" artifacttype=\"source").append("\">");
                    builder.append(artifactSpecUtil.frameArtifactXml(artifactSpec));
                }
            }
        }
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getdSourceArtifact() end");
        return builder.toString();
    }

    private String getdConectivityArtifact(List<String> dependartifact, String accountid) throws ServerError {
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getdConectivityArtifact() start");
        StringBuilder builder = new StringBuilder();
        for (String conectivityArtifactid : dependartifact) {
            List<ArtifactSpec> artifactSpecsList = artifactSpecDAO.getArtifactSpecList(conectivityArtifactid, accountid);
            if (!artifactSpecsList.isEmpty()) {
                for (ArtifactSpec artifactSpec : artifactSpecsList) {
                    builder.append("<artifactspec id=\"").append(artifactSpec.getArtifactidentity()).append("\" wspid=\"").append(artifactSpec.getWspid()).append("\" artifacttype=\"target").append("\">");
                    builder.append(artifactSpecUtil.frameArtifactXml(artifactSpec));
                }
            }

        }
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getdConectivityArtifact() end");
        return builder.toString();

    }

    @Override
    public ArtifactSpec updateCloneArtifact(String accountid, String id, String resourceid, ArtifactSpec artifactSpec) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl updateCloneArtifact() start");
        List<ArtifactSpec> artifactSpecList = artifactSpecDAO.getArtifactDetailsWithResource(resourceid, id, accountid);
        ArtifactSpec oldspec = new ArtifactSpec();

        oldspec = artifactSpecList.get(0);
        if (oldspec != null) {

            if (StringUtility.isNotNullOrEmpty(artifactSpec.getResourceid())) {
                oldspec.setResourceid(artifactSpec.getResourceid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getSaasvariantid())) {
                oldspec.setSaasvariantid(artifactSpec.getSaasvariantid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getNodeid())) {
                oldspec.setNodeid(artifactSpec.getNodeid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getWspid())) {
                oldspec.setWspid(artifactSpec.getWspid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getSourceartificatidentity())) {
                oldspec.setSourceartificatidentity(artifactSpec.getSourceartificatidentity());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getLocation())) {
                oldspec.setLocation(artifactSpec.getLocation());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getName())) {
                oldspec.setName(artifactSpec.getName());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getArtifactidentity())) {
                oldspec.setArtifactidentity(artifactSpec.getArtifactidentity());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getArtifactidentitytype())) {
                oldspec.setArtifactidentitytype(artifactSpec.getArtifactidentitytype());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getDependsWspid())) {
                oldspec.setDependsWspid(artifactSpec.getDependsWspid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getConnectivityArtifactIdendity())) {
                oldspec.setConnectivityArtifactIdendity(artifactSpec.getConnectivityArtifactIdendity());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getIscommon())) {
                oldspec.setIscommon(artifactSpec.getIscommon());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getFromresourceid())) {
                oldspec.setFromresourceid(artifactSpec.getFromresourceid());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getMetadata())) {
                oldspec.setMetadata(artifactSpec.getMetadata());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getStatus())) {
                oldspec.setStatus(artifactSpec.getStatus());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getAction())) {
                oldspec.setAction(artifactSpec.getAction());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getMessage())) {
                oldspec.setMessage(artifactSpec.getMessage());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getProperties())) {
                oldspec.setProperties(artifactSpec.getProperties());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getFirewall())) {
                oldspec.setFirewall(artifactSpec.getFirewall());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getDeepscan())) {
                oldspec.setDeepscan(artifactSpec.getDeepscan());
            }
            if (StringUtility.isNotNullOrEmpty(artifactSpec.getPluginfileid())) {
                oldspec.setPluginfileid(artifactSpec.getPluginfileid());
            }
            artifactSpec.setUpdateDate(new Date());
            artifactSpecDAO.update(oldspec);
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl updateCloneArtifact() end");
            return oldspec;
        } else {
            throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
        }
    }

    @Override
    public String getArtifactForAgent(Map<String, String> varmap) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactForAgent() start");
        try {
            StringBuilder builder = new StringBuilder();
            String getSpec = "";
            String filter = "";
            String accountId = varmap.get("account");
            String id = varmap.get("id");

            if (varmap.containsKey("q") && !varmap.get("q").isEmpty()) {
                filter = URLDecoder.decode(varmap.get("q"));
            }
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactForAgent() accountId " + accountId);
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactForAgent() id " + id);
            loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactForAgent() filter " + filter);
            List<ArtifactSpec> artifactSpecsList = artifactSpecDAO.getArtifactSpecListAgent(accountId, filter, varmap, id);
            if (!artifactSpecsList.isEmpty()) {
                ArtifactSpec artifactSpec = artifactSpecsList.get(0);
                varmap.put("nodeid", artifactSpec.getNodeid());
                builder.append(Constants.XML_HEADER);
                builder.append("<input>");
                builder.append("<agentinfo>");
                builder.append("<ip>").append("localhost").append("</ip>");
                builder.append("<port>").append(getRegisterAgentPort(varmap)).append("</port>");
                builder.append("</agentinfo>");
                builder.append("<generalinfo>");
                builder.append("<zoneid>").append(accountId).append("</zoneid>");
                builder.append("</generalinfo>");
                builder.append("<artifactspecs>");
                builder.append(artifactSpecUtil.frameArtifactSpecResponseXml(artifactSpec));
                builder.append("</artifactspecs>");
                builder.append("</input>");
                getSpec = builder.toString();
                loggingUtility.logDebug(Constants.MODULE + "ArtifactSpecManagerImpl getArtifactSpec() END");
                return getSpec;
            } else {
                return sASFUtil.getNoRecoredFrame("artifactspecs");
            }
        } catch (ClientError ex) {
            throw new ClientError(ex.getMessage(), ex.getCode());
        } catch (ServerError | NumberFormatException ex) {
            loggingUtility.logError("Exception in getArtifactForAgent ::::" + ex.getMessage());
            loggingUtility.logException(ex);
            throw new ClientError(ex.getMessage(), sASFUtil.getErrorMessage(SaasificationConsts.UNABLETO_GETTHERECORD, true));
        }

    }

    public String getRegisterAgentPort(Map<String, String> varMap) throws UnsupportedEncodingException, ClientError {
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ArtifactSpecManagerImpl()getRegisterAgentPort() start");
        String filter = "{fl:{fc1:{n:'nodeid',ty:'ST',op:'EQ',vl:['" + varMap.get("nodeid") + "']}}}";
        filter = URLEncoder.encode(filter, "UTF-8");
        String url = URLUtility.getAgentManagerURL() + SaasificationConsts.AGENT_GET_URL.replace("{account}", varMap.get("account")) + "&q=" + filter;
        String method = RESTUtility.GET;
        String inputxml = "";
        String callbackResponse = integration.restCallAPI(url, method, inputxml);
        String agentport = XPathUtil.getXPathValue("//agentport/text()", callbackResponse);

        loggingUtility.logDebug(SaasificationConsts.MODULE + "ArtifactSpecManagerImpl()getRegisterAgentPort()callbackresponse" + callbackResponse);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ArtifactSpecManagerImpl()getRegisterAgentPort()agentport" + agentport);
        loggingUtility.logDebug(SaasificationConsts.MODULE + "ArtifactSpecManagerImpl()getRegisterAgentPort() END");
        return agentport;
    }

    @Override
    public ArtifactSpec deleteArtifact(String accountId, String id) throws ServerError, ClientError {
        loggingUtility.logDebug("Delete ArtifactSpec is Start");
        List<ArtifactSpec> artifactList;
        ArtifactSpec artifactSpec = new ArtifactSpec();
        artifactList = artifactSpecDAO.getArtifactSpecList(id, accountId);
        for (ArtifactSpec spec : artifactList) {
            spec.setStatus("trash");
            spec.setMessage("ArtifactSpec is deleted successfully");
            //spec.setDeleteddate(DateUtility.getCurrentDate());
            artifactSpec = artifactSpecDAO.update(spec);
        }
        loggingUtility.logDebug("Delete ArtifactSpec is End");
        return artifactSpec;
    }
}
