/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.controller;

import com.corenttech.engine.annotation.support.ResponseHolder;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.config.SASFUtil;
import com.corenttech.engine.saasification.config.NodeSpecUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.manager.NodeSpecManager;
import com.corenttech.engine.saasification.model.NodeSpec;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;
import com.corenttech.engine.saasification.dao.NodeSpecDAO;

/**
 *
 * @author Naveen Kumar S
 */
@Controller
public class NodeSpecController {

    private final com.corenttech.engine.utility.LoggingUtility log = LoggingUtility.getInstance(NodeSpecController.class);
    @Autowired
    private transient NodeSpecUtil nodeSpecUtil;
    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient NodeSpecManager nodeSpecManager;
    @Autowired
    private transient NodeSpecDAO nodeSpecDAO;

    public ResponseHolder createNodeSpec(String accountId, String specInput, ResponseHolder response) {
        log.logDebug("Body ::: " + specInput);
        log.logDebug("accountID ::: " + accountId);
        String message = "";
        try {
            if (StringUtility.isNullOrEmpty(accountId)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_ACCOUNT, true);
                throw new VerifyError(message);
            }
            if (StringUtility.isNullOrEmpty(specInput)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_XML, true);
                throw new VerifyError(message);
            }
            NodeSpec nodeSpec = nodeSpecUtil.createNodeSpecPojo(specInput);
            nodeSpec = nodeSpecManager.createNodeSpec(nodeSpec, accountId);
            if (null != nodeSpec) {
                message = nodeSpecUtil.createSuccessResponse(nodeSpec.getId());
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_INPUT, true);
                throw new VerifyError(message);
            }
            response.setResponseXml(message);
        } catch (ServerError e) {
            log.logDebug("ServerError Exception in createNodeSpec ::: " + e.getMessage());
            e.printStackTrace(System.err);
            message = sASFUtil.parseServerException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        } catch (ClientError e) {
            log.logDebug("ClientError Exception in createNodeSpec ::: " + e.getMessage());
            e.printStackTrace(System.err);
            message = sASFUtil.parseClientException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            log.logDebug("Exception in createNodeSpec ::: " + e.getMessage());
            e.printStackTrace(System.err);
            message = sASFUtil.parseException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        } catch (VerifyError error) {
            log.logDebug("VerifyError Exception in createNodeSpec ::: " + error.getMessage());
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        log.logDebug("Response code ::: " + response.getStatusCode() + "   Response xml ::: " + response.getResponseXml());
        log.logDebug("NodeSpec creation method finished ");
        return response;
    }

    public ResponseHolder updateNodeSpec(String nodeSpecId, String accountId, String body, ResponseHolder response) {
        String message = "";
        log.logDebug("node spec id ::: " + nodeSpecId);
        log.logDebug("account id ::: " + accountId);
        log.logDebug("input xml ::: " + body);
        try {
            if (StringUtility.isNullOrEmpty(nodeSpecId)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_NODESPECID, Boolean.TRUE);
                throw new VerifyError(message);
            }
            if (StringUtility.isNullOrEmpty(body)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_XML, Boolean.TRUE);
                throw new VerifyError(message);
            }
            NodeSpec nodeSpec = nodeSpecUtil.createNodeSpecPojo(body);
            NodeSpec existNodeSpec = nodeSpecDAO.getNodeSpecRecord(nodeSpecId);
            if (null != existNodeSpec) {
                nodeSpecManager.updateNodeSpec(nodeSpec, existNodeSpec);
            } else {
                throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, Boolean.TRUE));
            }
            message = sASFUtil.updateSuccessResponse();
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ServerError e) {
            log.logDebug("ServerError Exception in updateNodeSpec ::: " + e.getMessage());
            message = sASFUtil.parseServerException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        } catch (ClientError ce) {
            log.logDebug("ClientError Exception in updateNodeSpec ::: " + ce.getMessage());
            message = sASFUtil.parseClientException(ce);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (VerifyError error) {
            log.logDebug("VerifyError Exception in updateNodeSpec ::: " + error.getMessage());
            response.setResponseXml(error.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        } catch (IOException | ParserConfigurationException | SAXException | NullPointerException e) {
            log.logDebug("Exception in updateNodeSpec ::: " + e.getMessage());
            message = sASFUtil.parseException(e);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        }
        log.logDebug("updateNodeSpec response ::: " + response.getResponseXml());
        return response;
    }

    public ResponseHolder getNodeSpec(Map<String, String> searchattribute, ResponseHolder response) {
        String message = "";
        try {
            log.logDebug("Input param in getNodeSpec ::: " + searchattribute);
            message = nodeSpecManager.getNodeSpecXml(searchattribute);
            if (StringUtility.isNullOrEmpty(message)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, true);
                throw new VerifyError(message);
            }
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ClientError ex) {
            message = sASFUtil.parseClientException(ex);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (ServerError ex) {
            message = sASFUtil.parseServerException(ex);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (VerifyError error) {
            log.logDebug("VerifyError Exception in getNodeSpec ::: " + error.getMessage());
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        log.logDebug("getNodeSpec End response ::: " + response.getResponseXml());
        return response;
    }

    public ResponseHolder deleteNodeSpec(String nodeSpecId, ResponseHolder response) {
        String message = "";
        try {
            log.logDebug("Node spec id to delete ::: " + nodeSpecId);
            if (StringUtility.isNullOrEmpty(nodeSpecId)) {
                message = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_NODESPECID, true);
                throw new VerifyError(message);
            }
            boolean flag = nodeSpecManager.deleteNodeSpec(nodeSpecId);
            if (flag) {
                message = sASFUtil.updateSuccessResponse();
                response.setResponseXml(message);
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                throw new VerifyError(sASFUtil.getErrorMessage(SaasificationConsts.INVALID_INPUT, true));
            }
        } catch (ClientError ex) {
            message = sASFUtil.parseClientException(ex);
            ex.printStackTrace(System.err);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (ServerError ex) {
            message = sASFUtil.parseServerException(ex);
            ex.printStackTrace(System.err);
            response.setResponseXml(message);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (VerifyError ex) {
            log.logDebug("VerifyError Exception in deleteNodeSpec ::: " + ex.getMessage());
            ex.printStackTrace(System.err);
            response.setResponseXml(ex.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        log.logDebug("deleteNodeSpec response ::: " + response.getResponseXml());
        return response;
    }
}
