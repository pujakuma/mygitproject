/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.integration;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.saasification.model.ProvisioningDSC;
import com.corenttech.engine.saasification.model.WSP;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Shagul Hameed S
 */
@Service("saasIntegration")
public interface Integration {

    public String restCallAPI(String resourceUrl, String method, String body) throws ClientError;

    public String restCallAPIWithSession(String resourceUrl, String method, String sessionId, String body) throws ClientError;

    public String restCallAPIWithTime(String resourceUrl, String method, String body) throws ClientError;

    public String getAgenttask(Map<String, String> varmap) throws ClientError;

    public String updateArtifact(Map<String, String> varmap) throws ClientError;
    
    public String getArtifact(Map<String, String> varmap) throws ClientError;
    
    public String createCloneForProvisioning(Map<String, String> varmap, ProvisioningDSC provisioningDSC) throws ClientError, ServerError;

    public String getClonePlan(String accountid, String deploymentid) throws ClientError, ServerError, Exception;

    public String updateClonePlan(Map<String, String> varmap) throws ClientError;

    public String getWspForArtifact(Map<String, String> varmap) throws ClientError, ServerError;

    public String wspSpecDetails(String parentId, String resourceid, String nodeid) throws ClientError, ServerError;

    public void createWSPSpec(Map<String, String> varMap, WSP wsp) throws ClientError, ServerError;

    public void updateWSPSpec(WSP wsp, String id, Map<String, String> varMap) throws ClientError, ServerError;

    public String aritifactSpecDetails(String parentId, String resourceid, String nodeid, Map<String, String> varMap) throws ClientError, ServerError;

    public void createArtifactSpec(Map<String, String> varMap, ArtifactSpec artifactSpec) throws ClientError, ServerError;

    public String nodeDetails(String filter, String account) throws ClientError, ServerError;

    public String getresourceGroup(String resourceid, String type, String nodeid) throws ClientError;

    public void updateArtifactSpec(Map<String, String> varMap, ArtifactSpec artifactSpec, String id) throws ClientError, ServerError;

    public String getWspName(String id, Map<String, String> varMap) throws ServerError;

    public String getShareabilityServiceUrl() throws ServerError, ClientError;

    public String getResourceDetailsForPaas(String resourceid, String nodeid) throws ClientError, ServerError;

    public String getNode(String id) throws ClientError, ServerError;
}
