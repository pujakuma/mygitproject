/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author puja
 */
@Service("artifactSpecManager")
public interface ArtifactSpecManager {

    public List<ArtifactSpec> createArtifactSpec(List<ArtifactSpec> artifactSpecs) throws ClientError, ServerError;

    public ArtifactSpec updateArtifactSpec(String accountid, String id, ArtifactSpec artifactSpec,Map<String,String> varMap)throws ClientError,ServerError;

    public String getArtifactSpec(Map<String, String> varmap) throws ServerError, ClientError;

    public ArtifactSpec updateCloneArtifact(String accountid, String id, String resourceid, ArtifactSpec artifactSpec) throws ClientError, ServerError;

    public ArtifactSpec deleteArtifact(String accountId, String id) throws ServerError,ClientError;

    public String getArtifactForAgent(Map<String, String> varmap)throws ServerError, ClientError, Exception;

   
}
