/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DAO;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.utility.queryfilter.FilterQuery;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author puja
 */
@Repository("artifactSpecDAO")
public interface ArtifactSpecDAO extends DAO<ArtifactSpec>{

    public List<ArtifactSpec> getArtifactSpecList(String id, String accountid) throws VerifyError,ServerError;
    
    public List<ArtifactSpec> getArtifactSpecList(String id, String accountid,Map<String,String>varMap) throws VerifyError,ServerError;
    
    public List<ArtifactSpec> getArtifactSpecList(String templateAccount, String accountId, int limit, int offset, String order, String filter, Map<String, String> varmap, String id)throws  ServerError,ClientError;

    public FilterQuery getQueryCondition(String colum, String value) throws ServerError;
    
    public List<ArtifactSpec> getArtifactDetailsForClone(String accountid, String destinationid,String destinationnodeid) throws ServerError,VerifyError;

    public List<ArtifactSpec> getArtifactDetailsWithResource(String resourceid, String id, String accountid) throws ServerError;

    public List<ArtifactSpec> getSourceArtifactSpecList(String id, String accountid, String action) throws VerifyError, ServerError;

    public List<ArtifactSpec> getArtifactSpecListAgent(String accountId, String filter, Map<String, String> varmap, String id) throws ClientError, ServerError;
}
