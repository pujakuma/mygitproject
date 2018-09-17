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
import com.corenttech.engine.saasification.config.WSPUtil;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.model.WSP;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.StringUtility;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.corenttech.engine.saasification.manager.WSPManager;

/**
 *
 * @author puja
 */
@Service("wSPController")
public class WSPController {

    @Autowired
    private transient WSPUtil wSPUtil;
    @Autowired
    private transient SASFUtil sASFUtil;
    @Autowired
    private transient WSPManager wspManager;


    private com.corenttech.engine.utility.LoggingUtility loggingUtility = LoggingUtility.getInstance(WSPController.class);
    LoggingUtility log = LoggingUtility.getInstance(WSPController.class);

    public ResponseHolder createWsp(Map<String, String> varMap, ResponseHolder response) throws ServerError, ClientError, Exception {
        loggingUtility.logDebug(Constants.MODULE + "<!--WSPController createWsp() start --!>");
        String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "varmap : " + varMap);
        try {
            String accountid = varMap.get("account");
            String body = varMap.get("body");
            if (StringUtility.isNullOrEmpty(accountid)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_ACCOUNT, true);
                throw new VerifyError(MESSAGE);
            }
            if (StringUtility.isNullOrEmpty(body)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_XML, true);
                throw new VerifyError(MESSAGE);
            }
            WSP wsp = new WSP();
            wsp = wSPUtil.createWspPojo(varMap, wsp);
            wsp = wspManager.createWsp(wsp);
            MESSAGE = sASFUtil.createSuccessResponse(wsp);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.OK.value());
        } catch (ServerError serverError) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            MESSAGE = sASFUtil.parseServerException(serverError);
            loggingUtility.logException(serverError);
        } catch (ClientError ce) {
            MESSAGE = sASFUtil.parseClientException(ce);
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
        } catch (VerifyError e) {
            response.setResponseXml(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        catch (Exception e) {
            MESSAGE = sASFUtil.parseException(new ServerError(e.getMessage(),SaasificationConsts.INVALID_TAG));
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            loggingUtility.logException(e);
        }
        log.logDebug("createWsp Response" + response.getResponseXml());
        log.logDebug("createWsp getstatus code " + response.getStatusCode());
        log.logDebug("<----- WSPController createWsp controller end ----->");
        response.setResponseXml(MESSAGE);
        return response;
    }

    public ResponseHolder updateWsp(Map<String, String> varMap, ResponseHolder response) {
        String MESSAGE = "";
        String accountid = varMap.get("account");
        String id = varMap.get("identity");
        String body = varMap.get("body");
        loggingUtility.logDebug(Constants.MODULE + "<!------ updateWsp ------!>");
        loggingUtility.logDebug(Constants.MODULE + "id      : " + id);
        loggingUtility.logDebug(Constants.MODULE + "AccountId : " + accountid);
        loggingUtility.logDebug(Constants.MODULE + "<!------ updateWsp ------!>");
        try {
            if (StringUtility.isNullOrEmpty(id)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_WSPID, true);
                throw new VerifyError(MESSAGE);
            }
            if (StringUtility.isNullOrEmpty(body)) {
                MESSAGE = sASFUtil.getErrorMessage(SaasificationConsts.INVALID_XML, true);
                throw new VerifyError(MESSAGE);
            }
            WSP wsp = new WSP();
            wsp.setId(id);

            wsp = wSPUtil.updateWspPojo(varMap, wsp);
            if (wsp != null) {
                loggingUtility.logInfo("<----WSPController----->" + "<-----id--->" + wsp.getId());
            } else {
                loggingUtility.logInfo("<-----WSPController---->" + "<-----wsp---->" + wsp);
            }

            wsp = wspManager.updateWsp(accountid, id, wsp);

            if (wsp != null) {
                loggingUtility.logInfo(Constants.MODULE + "<<----Id---->>" + wsp.getId());
            } else {
                loggingUtility.logInfo(Constants.MODULE + "<----wsp--->" + wsp);
            }
            if (wsp != null) {
                MESSAGE = sASFUtil.updateSuccessResponse();
                response.setStatusCode(HttpStatus.OK.value());
            }
            loggingUtility.logInfo(Constants.MODULE + "<---WSPController--->" + MESSAGE);

        } catch (com.corenttech.engine.core.exception.ServerError serverError) {
            loggingUtility.logException(serverError);
            MESSAGE = sASFUtil.parseServerException(serverError);
        } catch (ClientError clientError) {
            response.setStatusCode(HttpStatus.Series.CLIENT_ERROR.value());
            MESSAGE = sASFUtil.parseClientException(clientError);

        } catch (Exception ex) {
            response.setStatusCode(HttpStatus.Series.SERVER_ERROR.value());
            loggingUtility.logDebug("<<Exeption in wsp update>>"+ex.getCause());
            MESSAGE = sASFUtil.getErrorMessage( SaasificationConsts.COULDNOT_UPDATE,true);
        } catch (VerifyError e) {
            response.setResponseXml(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[updateWsp] : " + MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!--WSPController updateWsp() End --!>");
        response.setResponseXml(MESSAGE);
        return response;

    }

    public ResponseHolder getWsp(Map<String, String> varMap, ResponseHolder response) {

        String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "<!------ getWsp start ------!>");
        loggingUtility.logDebug(Constants.MODULE + "varMap      : " + varMap);
        try {
            MESSAGE = wspManager.getWsp(varMap);

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
            log.logDebug("VerifyError Exception in getWsp::: " + error.getMessage());
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getStatusCode()] : " + response.getStatusCode());
        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getResponseXml()] : " + response.getResponseXml());
        response.setResponseXml(MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!------ getWsp End ------!>");
        return response;
    }
        public ResponseHolder getkp(Map<String, String> varMap, ResponseHolder response) {

        String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "<!------ getkp start ------!>");
        loggingUtility.logDebug(Constants.MODULE + "varMap      : " + varMap);
        try {
            MESSAGE = wspManager.getkp(varMap);

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
            log.logDebug("VerifyError Exception in getkp::: " + error.getMessage());
            response.setResponseXml(MESSAGE);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }

        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getStatusCode()] : " + response.getStatusCode());
        loggingUtility.logDebug(Constants.MODULE + "FINAL RESPONSE[response.getResponseXml()] : " + response.getResponseXml());
        response.setResponseXml(MESSAGE);
        loggingUtility.logDebug(Constants.MODULE + "<!------ getWsp End ------!>");
        return response;
    }

    public ResponseHolder deleteWsp(String id, ResponseHolder response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ResponseHolder deleteWsp(String accountId, String id, ResponseHolder response) {
       String MESSAGE = "";
        loggingUtility.logDebug(Constants.MODULE + "<!------ deleteArtifactSpec start ------!>");
        loggingUtility.logDebug(Constants.MODULE + "accountId      : " + accountId);
        loggingUtility.logDebug(Constants.MODULE + "artfactidentity      : " + id);
        try {
            WSP wsp=new WSP();
            wsp =wspManager.deleteWsp(accountId,id);
            if (wsp != null) {
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
