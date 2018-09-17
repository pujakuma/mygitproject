/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.WSPUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.WSP;
import com.corenttech.engine.saasification.model.KbArtifacts;
import com.corenttech.engine.saasification.model.KbArtifacts;
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
import com.corenttech.engine.saasification.dao.WSPDAO;
import com.corenttech.engine.saasification.dao.WSPKPARTIFACTDAO;

/**
 *
 * @author puja
 */
@Service("wSPManagerImpl")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class WSPManagerImpl implements WSPManager {

    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WSPManagerImpl.class);
    LoggingUtility log = LoggingUtility.getInstance(WSPManagerImpl.class);
    @Autowired
    private transient WSPDAO wspdao;
    @Autowired
    private transient WSPKPARTIFACTDAO wspkpartifactdao;
    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient WSPUtil wSPUtil;

    @Override
    public WSP createWsp(WSP wsp) throws ClientError, ServerError {
        loggingUtility.logDebug(Constants.MODULE + "<!--WSPManagerImpl createWsp() start --!>");
        if (null != wsp) {
            try {
                wspdao.create(wsp);
            } catch (ServerError ex) {
                log.logException(ex);
                log.logError("Exception in createWsp - WSPManagerImpl" + ex.getMessage() + ex.getCause().toString());
                throw new ServerError(ex.getCause().toString(), SaasificationConsts.DB_ERROR);
            }
        } else {
            throw new ClientError("Could Not Create wsp ", SaasificationConsts.UNABLETO_CREATE);
        }
        loggingUtility.logDebug(Constants.MODULE + "<!--WSPManagerImpl createWsp() end --!>");;
        return wsp;
    }

    @Override
    public WSP updateWsp(String accountId, String id, WSP wsp) throws ServerError, ClientError {
        List<WSP> wsps = wspdao.getWspList(id, accountId);
        WSP wsp1 = new WSP();

        wsp1 = wsps.get(0);
        if (wsp1 != null) {

            if (StringUtility.isNotNullOrEmpty(wsp.getResourceid())) {
                wsp1.setResourceid(wsp.getResourceid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getSaasvariantid())) {
                wsp1.setSaasvariantid(wsp.getSaasvariantid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getNodeid())) {
                wsp1.setNodeid(wsp.getNodeid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getTonodeid())) {
                wsp1.setTonodeid(wsp.getTonodeid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getCloudresourceid())) {
                wsp1.setCloudresourceid(wsp.getCloudresourceid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadname())) {
                wsp1.setWorkloadname(wsp.getWorkloadname());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadversion())) {
                wsp1.setWorkloadversion(wsp.getWorkloadversion());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadtype())) {
                wsp1.setWorkloadtype(wsp.getWorkloadtype());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadlocation())) {
                wsp1.setWorkloadlocation(wsp.getWorkloadlocation());
            }
            /*if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadip())) {
             wsp1.setWorkloadip(wsp.getWorkloadip());
             }*/
            if (StringUtility.isNotNullOrEmpty(wsp.getIdentity())) {
                wsp1.setIdentity(wsp.getIdentity());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getToidentity())) {
                wsp1.setToidentity(wsp.getToidentity());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getSource())) {
                wsp1.setSource(wsp.getSource());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadrefid())) {
                wsp1.setWorkloadrefid(wsp.getWorkloadrefid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getFromresourceid())) {
                wsp1.setFromresourceid(wsp.getWorkloadrefid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getMetadata())) {
                wsp1.setMetadata(wsp.getMetadata());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getShareability())) {
                wsp1.setShareability(wsp.getShareability());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getStatus())) {
                wsp1.setStatus(wsp.getStatus());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getStaaction())) {
                wsp1.setStaaction(wsp.getStaaction());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getAction())) {
                wsp1.setAction(wsp.getAction());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getEffort())) {
                wsp1.setEffort(wsp.getEffort());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getMessage())) {
                wsp1.setMessage(wsp.getMessage());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getProperties())) {
                wsp1.setProperties(wsp.getProperties());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getFirewall())) {
                wsp1.setFirewall(wsp.getFirewall());
            }
            wsp.setUpdatedDate(new Date());
            wspdao.update(wsp1);
            return wsp1;
        } else {
            throw new ClientError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, true));
        }


    }

    @Override
    public String getWsp(Map<String, String> varmap) throws ClientError {
        loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getWorkloadIdentity() start");
        try {
            StringBuilder builder = new StringBuilder();
            String getWorkload = "";
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
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getWorkloadIdentity() accountId " + accountId);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getWorkloadIdentity() id " + id);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getWorkloadIdentity() limit " + limit);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getWorkloadIdentity() offset " + offset);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getWorkloadIdentity() order " + order);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getWorkloadIdentity() filter " + filter);
            List<WSP> wsps = wspdao.getWspList(accountId, limit, offset, order, filter, varmap, id);
            if (!wsps.isEmpty()) {
                builder.append(Constants.XML_HEADER);
                builder.append(Constants.GET_SUCCESS);
//                builder.append(Constants.SUCCESS_START_200);
                builder.append("<wsps totalrecords=\"").append(wsps.size()).append("\" limit=\"").append(limit).append("\" offset=\"").append(offset).append("\">");
                for (WSP wsp : wsps) {
                    builder.append(wSPUtil.frameWspResponseXml(wsp, SaasificationConsts.FALSE, varmap.get("clone")));
                }
                builder.append("</wsps>");
                builder.append(Constants.END_RESPONSE);
                getWorkload = builder.toString();
                return getWorkload;
            } else {
                return sASFUtil.getNoRecoredFrame("wsps");
            }
        } catch (ClientError ex) {
            throw new ClientError(ex.getMessage(), ex.getCode());
        } catch (ServerError | NumberFormatException ex) {
            loggingUtility.logError("Exception in getwsp ::::" + ex.getMessage());
            loggingUtility.logException(ex);
            throw new ClientError(sASFUtil.getErrorMessage(SaasificationConsts.UNABLETO_GETTHERECORD, true));
        }
    }

    @Override
    public String getkp(Map<String, String> varmap) throws ClientError {
        loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getWorkloadIdentity() start");
        try {
            StringBuilder builder = new StringBuilder();
            String getWorkload = "";
            int limit = 0;
            int offset = 0;
            String filter = "";
            String order = "";
            String id = "";
            String guid = "";
            String os = "";
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
            if (varmap.containsKey("id") && !varmap.get("id").isEmpty()) {
                id = URLDecoder.decode(varmap.get("id"));
            }
            if (varmap.containsKey("guid") && !varmap.get("guid").isEmpty()) {
                guid = URLDecoder.decode(varmap.get("id"));
            }
            if (varmap.containsKey("os") && !varmap.get("os").isEmpty()) {
                os = URLDecoder.decode(varmap.get("id"));
            }
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getwspkps() accountId " + guid);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getwspkps() id " + id);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getwspkps() limit " + limit);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getwspkps() offset " + offset);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getwspkps() order " + order);
            loggingUtility.logDebug(Constants.MODULE + "WorkloadIdentityManagerImpl getwspkps() filter " + filter);
            List<KbArtifacts> kbArtifactses = wspkpartifactdao.getKpList(id, guid, os, limit, offset, order, filter);
            if (!kbArtifactses.isEmpty()) {
                builder.append(Constants.XML_HEADER);
                builder.append(Constants.GET_SUCCESS);
//                builder.append(Constants.SUCCESS_START_200);
                builder.append("<wspkps totalrecords=\"").append(kbArtifactses.size()).append("\" limit=\"").append(limit).append("\" offset=\"").append(offset).append("\">");
                for (KbArtifacts wspkpartifact : kbArtifactses) {
                    builder.append(wSPUtil.frameWspkpResponseXml(wspkpartifact));
                }
                builder.append("</wspkps>");
                builder.append(Constants.END_RESPONSE);
                getWorkload = builder.toString();
                return getWorkload;
            } else {
                return sASFUtil.getNoRecoredFrame("wspkps");
            }
        } catch (ClientError ex) {
            throw new ClientError(ex.getMessage(), ex.getCode());
        } catch (ServerError | NumberFormatException ex) {
            loggingUtility.logError("Exception in getwspkps ::::" + ex.getMessage());
            loggingUtility.logException(ex);
            throw new ClientError(sASFUtil.getErrorMessage(SaasificationConsts.UNABLETO_GETTHERECORD, true));
        }
    }

    @Override
    public WSP updateCloneArtifact(String accountid, String id, String resourceid, WSP wsp) throws ClientError, ServerError {
        List<WSP> wsps = wspdao.getWspDetailsWithResource(resourceid, id, accountid);
        WSP wsp1 = new WSP();

        wsp1 = wsps.get(0);
        if (wsp1 != null) {

            if (StringUtility.isNotNullOrEmpty(wsp.getResourceid())) {
                wsp1.setResourceid(wsp.getResourceid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getSaasvariantid())) {
                wsp1.setSaasvariantid(wsp.getSaasvariantid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getNodeid())) {
                wsp1.setNodeid(wsp.getNodeid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getTonodeid())) {
                wsp1.setTonodeid(wsp.getTonodeid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getCloudresourceid())) {
                wsp1.setCloudresourceid(wsp.getCloudresourceid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadname())) {
                wsp1.setWorkloadname(wsp.getWorkloadname());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadversion())) {
                wsp1.setWorkloadversion(wsp.getWorkloadversion());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadtype())) {
                wsp1.setWorkloadtype(wsp.getWorkloadtype());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadlocation())) {
                wsp1.setWorkloadlocation(wsp.getWorkloadlocation());
            }
            /*if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadip())) {
             wsp1.setWorkloadip(wsp.getWorkloadip());
             }*/
            if (StringUtility.isNotNullOrEmpty(wsp.getIdentity())) {
                wsp1.setIdentity(wsp.getIdentity());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getToidentity())) {
                wsp1.setToidentity(wsp.getToidentity());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getSource())) {
                wsp1.setSource(wsp.getSource());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getWorkloadrefid())) {
                wsp1.setWorkloadrefid(wsp.getWorkloadrefid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getFromresourceid())) {
                wsp1.setFromresourceid(wsp.getWorkloadrefid());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getMetadata())) {
                wsp1.setMetadata(wsp.getMetadata());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getShareability())) {
                wsp1.setShareability(wsp.getShareability());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getStatus())) {
                wsp1.setStatus(wsp.getStatus());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getStaaction())) {
                wsp1.setStaaction(wsp.getStaaction());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getAction())) {
                wsp1.setAction(wsp.getAction());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getEffort())) {
                wsp1.setEffort(wsp.getEffort());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getMessage())) {
                wsp1.setMessage(wsp.getMessage());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getProperties())) {
                wsp1.setProperties(wsp.getProperties());
            }
            if (StringUtility.isNotNullOrEmpty(wsp.getFirewall())) {
                wsp1.setFirewall(wsp.getFirewall());
            }
            wsp.setUpdatedDate(new Date());
            wspdao.update(wsp1);
            return wsp1;
        } else {
            throw new ClientError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, true));
        }
    }

    @Override
    public WSP deleteWsp(String accountId, String id) throws ServerError, ClientError {
        loggingUtility.logDebug("Delete ArtifactSpec is Start");
        List<WSP> list;
        WSP wsp = new WSP();
        list = wspdao.getWspList(id, accountId);
        for (WSP spec : list) {
            spec.setStatus("trash");
            spec.setMessage("ArtifactSpec is deleted successfully");
            //spec.setDeleteddate(DateUtility.getCurrentDate());
            wsp = wspdao.update(spec);
        }
        loggingUtility.logDebug("Delete ArtifactSpec is End");
        return wsp;
    }
}
