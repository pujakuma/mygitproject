/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.WSP;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author puja
 */
@Service("wspManager")
public interface WSPManager {

    public WSP createWsp(WSP wsp) throws ClientError, ServerError;

    public WSP updateWsp(String accountId, String id, WSP wsp) throws ServerError, ClientError;

    public String getWsp(Map<String, String> varMap) throws ClientError, ServerError;

    public String getkp(Map<String, String> varMap) throws ClientError, ServerError;

    public WSP updateCloneArtifact(String accountid, String id, String resourceid, WSP wsp) throws ClientError, ServerError;

    public WSP deleteWsp(String accountId, String id) throws ServerError, ClientError;
}
