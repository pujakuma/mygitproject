/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DAO;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.NodeSpec;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Naveen Kumar S
 */
@Repository("nodeSpecDAO")
public interface NodeSpecDAO extends DAO<NodeSpec> {

    public NodeSpec getNodeSpecRecord(String nodeSpecId) throws ServerError, VerifyError;

    public List<NodeSpec> getNodeSpecList(Map<String, String> searchattributes) throws ServerError;
}
