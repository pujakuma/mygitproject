/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.NodeSpecUtil;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.NodeSpec;
import com.corenttech.engine.utility.DateUtility;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.corenttech.engine.saasification.dao.NodeSpecDAO;
import java.util.Date;

/**
 *
 * @author Naveen Kumar S
 */
@Service("nodeSpecManagerImpl")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class NodeSpecManagerImpl implements NodeSpecManager {

    LoggingUtility loggingUtility = LoggingUtility.getInstance(NodeSpecManagerImpl.class);
    @Autowired
    private transient NodeSpecDAO nodeSpecDAO;
    @Autowired
    private transient NodeSpecUtil nodeSpecUtil;
    @Autowired
    private transient SASFUtil sASFUtil;

    @Override
    public NodeSpec createNodeSpec(NodeSpec nodeSpec, String account) throws ClientError, ServerError {
        String nodeSpecId = null;
        NodeSpec nodeSpecRecord = new NodeSpec();
        try {
            do {
                nodeSpecId = "NSC-" + sASFUtil.getGUID();
                nodeSpecRecord = getNodeSpecRecord(nodeSpecId);
            } while (null != nodeSpecRecord);
            nodeSpec.setId(nodeSpecId);
            nodeSpec.setCreatedate(new Date());
            nodeSpec.setStatus(StringUtility.isNullOrEmpty(nodeSpec.getStatus()) ? "Active" : nodeSpec.getStatus());
            nodeSpec.setAccountid(account);
            nodeSpecDAO.create(nodeSpec);
            loggingUtility.logDebug("NodeSpec Record id ::: " + nodeSpec.getId());
            return nodeSpec;
        } catch (ServerError e) {
            e.printStackTrace(System.err);
            loggingUtility.logException(e);
            throw new ServerError(e);
        }
    }

    @Override
    public String getNodeSpecXml(Map<String, String> nodeSpecAttributes) throws ClientError, ServerError {
        loggingUtility.logDebug("Node Spec id ::: " + nodeSpecAttributes.get("id"));
        loggingUtility.logDebug("account id ::: " + nodeSpecAttributes.get("account"));
        loggingUtility.logDebug("Filter id ::: " + nodeSpecAttributes.get("q"));
        String nodeSpecResponseXml = null;
        List<NodeSpec> nodeSpecList = nodeSpecDAO.getNodeSpecList(nodeSpecAttributes);
        if (null != nodeSpecList && !nodeSpecList.isEmpty()) {
            nodeSpecResponseXml = nodeSpecUtil.getNodeSpecXml(nodeSpecList, nodeSpecAttributes);
        }
        return nodeSpecResponseXml;
    }

    @Override
    public void updateNodeSpec(NodeSpec nodeSpec, NodeSpec existNodeSpec) throws ClientError, ServerError {
        loggingUtility.logDebug("update nodespec's exist record ::: ");
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getDensity())) {
            existNodeSpec.setDensity(nodeSpec.getDensity());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getProperties())) {
            existNodeSpec.setProperties(nodeSpec.getProperties());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getConnection())) {
            existNodeSpec.setConnection(nodeSpec.getConnection());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getDevice())) {
            existNodeSpec.setDevice(nodeSpec.getDevice());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getFirewall())) {
            existNodeSpec.setFirewall(nodeSpec.getFirewall());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getMetadata())) {
            existNodeSpec.setMetadata(nodeSpec.getMetadata());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getIssharable())) {
            existNodeSpec.setIssharable(nodeSpec.getIssharable());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getNodeid())) {
            existNodeSpec.setNodeid(nodeSpec.getNodeid());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getResourceid())) {
            existNodeSpec.setResourceid(nodeSpec.getResourceid());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getSaasvariantid())) {
            existNodeSpec.setSaasvariantid(nodeSpec.getSaasvariantid());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getStatus())) {
            existNodeSpec.setStatus(nodeSpec.getStatus());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getMessage())) {
            existNodeSpec.setMessage(nodeSpec.getMessage());
        }
        if (StringUtility.isNotNullOrEmpty(nodeSpec.getMetadata())) {
            existNodeSpec.setMetadata(nodeSpec.getMetadata());
        }
        existNodeSpec.setUpdateddate(new Date());
        nodeSpecDAO.update(existNodeSpec);
        loggingUtility.logDebug("updated nodespec's id ::: " + existNodeSpec.getId());
    }

    @Override
    public boolean deleteNodeSpec(String nodeSpecId) throws ClientError, ServerError, VerifyError {
        NodeSpec nodeSpec = nodeSpecDAO.getNodeSpecRecord(nodeSpecId);
        if (null != nodeSpec) {
            nodeSpec.setStatus("Trash");
            nodeSpec.setDeleteddate(DateUtility.getCurrentDate());
            nodeSpecDAO.update(nodeSpec);
            return true;
        } else {
            throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
        }

    }

    @Override
    public NodeSpec getNodeSpecRecord(String nodeSpecId) throws ServerError {
        NodeSpec nodeSpec = new NodeSpec();
        nodeSpec = nodeSpecDAO.getNodeSpecRecord(nodeSpecId);
        if (null != nodeSpec) {
            return nodeSpec;
        } else {
            return null;
        }
    }
}
