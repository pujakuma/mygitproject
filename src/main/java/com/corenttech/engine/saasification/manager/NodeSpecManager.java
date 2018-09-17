/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.NodeSpec;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Naveen Kumar S
 */
@Service("nodeSpecManager")
public interface NodeSpecManager {

    public NodeSpec createNodeSpec(NodeSpec nodeSpec, String account) throws ClientError, ServerError;

    public String getNodeSpecXml(Map<String, String> searchattributes) throws ClientError, ServerError;

    public void updateNodeSpec(NodeSpec nodeSpec, NodeSpec oldObjectt) throws ClientError, ServerError;

    public boolean deleteNodeSpec(String nodeSpecId) throws ClientError, ServerError, VerifyError;

    public NodeSpec getNodeSpecRecord(String nodeSpecId) throws ServerError;
}
