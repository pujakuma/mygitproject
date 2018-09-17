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
import com.corenttech.engine.saasification.config.Constants;
import com.corenttech.engine.saasification.config.ArtifactSpecUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.corenttech.engine.saasification.manager.ArtifactSpecManager;
import java.util.List;

/**
 *
 * @author puja
 */
@Controller
public class ArtifactSpecController {

    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient ArtifactSpecUtil artifactSpecUtil;
    @Autowired
    private transient ArtifactSpecManager artifactSpecManager;
    LoggingUtility log = LoggingUtility.getInstance(ArtifactSpecController.class);
    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(ArtifactSpecController.class);

    public ResponseHolder createArtifactspec(Map<String, String> varmap, ResponseHolder response) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpecController createArtifactspec() start --!>");
        String MESSAGE = "";

        loggingUtility.logDebug(Constants.MODULE + "varmap : " + varmap);
        try {
            String accountid = varmap.get("account");
            String body = varmap.get("body");
            if (StringUtility.isNullOrEmpty(accountid)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_ACCOUNT, true);
                throw new VerifyError(MESSAGE);
            }
            if (StringUtility.isNullOrEmpty(body)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_XML, true);
                throw new VerifyError(MESSAGE);
            }
            List<ArtifactSpec> artifactSpec = artifactSpecUtil.createArtifactSpecPojo(varmap);
            artifactSpec = artifactSpecManager.createArtifactSpec(artifactSpec);
            MESSAGE = sASFUtil.createSuccessResponse(artifactSpec);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ServerError e) {
            MESSAGE = sASFUtil.parseServerException(e);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        } catch (ClientError ce) {
            MESSAGE = sASFUtil.parseClientException(ce);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        }
        log.logDebug("createArtifactspec Response" + response.getResponseXml());
        log.logDebug("createArtifactspec getstatus code " + response.getStatusCode());
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpecController createArtifactspec() END --!>");
        return response;
    }

    public ResponseHolder updateArtifactSpec(Map<String, String> varMap, ResponseHolder response) {
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpecController updateArtifactSpec() START --!>");
        String MESSAGE = "";
        String accountid = varMap.get("account");
        String id = varMap.get("artifactidentity");
        String body = varMap.get("body");
        loggingUtility.logDebug(Constants.MODULE + "<!------ Update ArtifactSpec ------!>");
        loggingUtility.logDebug(Constants.MODULE + "id      : " + id);
        loggingUtility.logDebug(Constants.MODULE + "AccountId : " + accountid);
        loggingUtility.logDebug(Constants.MODULE + "<!------ Update ArtifactSpec ------!>");
        try {
            if (StringUtility.isNullOrEmpty(id)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_ARTIFACTID, true);
                throw new VerifyError(MESSAGE);
            }
            if (StringUtility.isNullOrEmpty(body)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_XML, true);
                throw new VerifyError(MESSAGE);
            }
            ArtifactSpec artifactSpec = new ArtifactSpec();
            artifactSpec.setId(id);

            artifactSpec = artifactSpecUtil.updateArtifactSpecPojo(varMap, artifactSpec);
            if (artifactSpec != null) {
                loggingUtility.logInfo("<----ArtifactSpecController----->" + "<-----id--->" + artifactSpec.getId());
            } else {
                loggingUtility.logInfo("<-----ArtifactSpecController---->" + "<-----ArtifactSpec---->" + artifactSpec);
            }

            artifactSpec = artifactSpecManager.updateArtifactSpec(accountid, id, artifactSpec,varMap);

            if (artifactSpec != null) {
                loggingUtility.logInfo(Constants.MODULE + "<<----Id---->>" + artifactSpec.getId());
            } else {
                loggingUtility.logInfo(Constants.MODULE + "<----ArtifactSpec--->" + artifactSpec);
            }
            if (artifactSpec != null) {
                MESSAGE = sASFUtil.updateSuccessResponse();
                response.setStatusCode(HttpStatus.OK.value());
            }
            loggingUtility.logInfo(Constants.MODULE + "<---ArtifactSpecController--->" + MESSAGE);

        } catch (com.corenttech.engine.core.exception.ServerError serverError) {
            loggingUtility.logException(serverError);
            MESSAGE = sASFUtil.parseServerException(serverError);
        } catch (ClientError clientError) {
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
            MESSAGE = sASFUtil.parseClientException(clientError);

        } catch (Exception ex) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            loggingUtility.logDebug("<<Exeption in ArtifactSpec update>>" + ex.getCause());
            MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.COULDNOT_UPDATE, true);
        } catch (VerifyError e) {
            response.setResponseXml(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[updateArtifactSpec] : " + MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!--ArtifactSpecController updateArtifactSpec() End --!>");
        response.setResponseXml(MESSAGE);
        return response;
    }

    public ResponseHolder getArtifactSpec(Map<String, String> varmap, ResponseHolder response) throws Exception {
        String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "<!------ getArtifactSpec start ------!>");
        loggingUtility.logDebug(Constants.MODULE + "varMap      : " + varmap);
        String agtent = varmap.get("agent");
        try {
            if (Boolean.valueOf(agtent)) {
                MESSAGE = artifactSpecManager.getArtifactForAgent(varmap);
            } else {
                MESSAGE = artifactSpecManager.getArtifactSpec(varmap);
            }

            if (StringUtility.isNullOrEmpty(MESSAGE)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, true);
                throw new VerifyError(MESSAGE);
            }
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ClientError ex) {
            MESSAGE = sASFUtil.parseClientException(ex);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (ServerError e) {
            MESSAGE = sASFUtil.parseServerException(e);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (VerifyError error) {
            log.logDebug("VerifyError Exception in getArtifactSpec::: " + error.getMessage());
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getStatusCode()] : " + response.getStatusCode());
        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getResponseXml()] : " + response.getResponseXml());
        response.setResponseXml(MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!------ getArtifactSpec End ------!>");
        return response;
    }

    public ResponseHolder deleteArtifactSpec(String accountId, String id, ResponseHolder response) {
        String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "<!------ deleteArtifactSpec start ------!>");
        loggingUtility.logDebug(Constants.MODULE + "accountId      : " + accountId);
        loggingUtility.logDebug(Constants.MODULE + "artfactidentity      : " + id);
        try {
            ArtifactSpec artifactSpec = new ArtifactSpec();
            artifactSpec = artifactSpecManager.deleteArtifact(accountId, id);
            if (artifactSpec != null) {
                MESSAGE = sASFUtil.deleteSuccessResponse();
                response.setStatusCode(HttpStatus.OK.value());
                response.setResponseXml(MESSAGE);
            }
            if (StringUtility.isNullOrEmpty(MESSAGE)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.NO_RECORD_FOUND, true);
                throw new VerifyError(MESSAGE);
            }
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ClientError ex) {
            MESSAGE = sASFUtil.parseClientException(ex);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (ServerError e) {
            MESSAGE = sASFUtil.parseServerException(e);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (VerifyError error) {
            log.logDebug("VerifyError Exception in deleteArtifactSpec::: " + error.getMessage());
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getStatusCode()] : " + response.getStatusCode());
        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getResponseXml()] : " + response.getResponseXml());
        response.setResponseXml(MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!------ deleteArtifactSpec End ------!>");
        return response;
    }
}
