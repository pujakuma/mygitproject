/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.config;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.constants.SaasificationConsts;
import com.corenttech.engine.saasification.exception.SaasificationErrorCodeList;
import com.corenttech.engine.saasification.integration.Integration;
import com.corenttech.engine.saasification.model.ArtifactSpec;
import com.corenttech.engine.saasification.model.ProvisioningDSC;
import com.corenttech.engine.saasification.model.WSP;
import com.corenttech.engine.utility.LoggingUtility;
import com.corenttech.engine.utility.RESTUtility;
import com.corenttech.engine.utility.StringUtility;
import com.corenttech.engine.utility.URLUtility;
import com.corenttech.engine.utility.XPathUtil;
import com.corenttech.utility.parsers.JSONUtility;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author puja
 */
@Service("sASFUtil")
public class SASFUtil {

    @Autowired
    Integration integrationObj;
    LoggingUtility log = LoggingUtility.getInstance(SASFUtil.class);

    public String getGUID() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    public String getGUID(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public String createSuccessResponse(List<ArtifactSpec> artifactSpec) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append("<id>").append(artifactSpec.size() > 0 ? "Artifacts Created Successfully" : artifactSpec.get(0).getArtifactidentity()).append("</id>");
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

    public String createSuccessResponse(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append("<id>").append(id).append("</id>");
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

    public String parseServerException(ServerError e) {
        String message = e.getMessage();
        //String message = e.getCause().toString();
        Throwable inner = null;
        Throwable root = e;
        if ((inner = root.getCause()) != null) {
            message += " " + inner.getMessage();
        }
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append("500").append("\">").append(message).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String parseClientException(ClientError ce) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append(ce.getCode()).append("\">").append(ce.getMessage()).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String updateSuccessResponse() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.SUCCESS_START_201);
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String getNoRecoredFrame(String nounValue) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.XML_HEADER);
        builder.append(Constants.SUCCESS_START_200);
        builder.append("<").append(nounValue).append(" totalrecords=\"0").append("\"").append(" limit=\"0").append("\"").append(" offset=\"0").append("\"").append("/>");
        builder.append(Constants.END_RESPONSE);
        return builder.toString();
    }

    public String parseException(Exception e, String errorCode) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append(errorCode).append("\">").append(e.getMessage()).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String parseException(ServerError serverError) {
        String message = serverError.getMessage();
        Throwable inner = null;
        Throwable root = serverError;
        if ((inner = root.getCause()) != null) {
            message += " " + inner.getMessage();

        }
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append(serverError.getCode()).append("\">").append(message).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String createSuccessResponse(WSP workloadIdentity) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.XML_HEADER);
        sb.append(Constants.SUCCESS_START_200);
        sb.append("<id>").append(workloadIdentity.getIdentity()).append("</id>");
        sb.append(Constants.END_RESPONSE);
        return sb.toString();
    }

    public String getErrorMessage(String errorCode, Boolean isExceptionXml) {
        StringBuilder sBuilder = new StringBuilder();
        if (isExceptionXml) {
            sBuilder.append(Constants.XML_HEADER);
            sBuilder.append(Constants.ERROR_START_500);
            sBuilder.append("<error httpstatuscode=\"").append(errorCode).append("\">").append(SaasificationErrorCodeList.getErrorResponse(errorCode)).append("</error>");
            sBuilder.append(Constants.END_RESPONSE);
            return sBuilder.toString();
        } else {
            return SaasificationErrorCodeList.getErrorResponse(errorCode);
        }
    }

    public String parseException(Exception exception) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.ERROR_START_500);
        sBuilder.append("<error httpstatuscode=\"").append("500").append("\">").append(exception.getMessage()).append("</error>");
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();

    }

    public Order getOrder(String jsonString) {
        if (!StringUtility.isNullOrEmpty(jsonString)) {
            JSONObject obj = JSONUtility.getFirstJSONObject(jsonString, false);
            JSONObject flObj = (JSONObject) obj.get("fl");
            if (!flObj.isEmpty()) {
                for (Iterator iter = flObj.keys(); iter.hasNext();) {
                    String key = (String) iter.next();
                    if (key.startsWith("fc") || key.startsWith("fl")) {
                        JSONObject subObj = (JSONObject) flObj.get(key);
                        String fieldName = (String) subObj.get("n");
                        log.logDebug(Constants.MODULE + "<< fieldName >>" + fieldName);
                        String condition = (String) subObj.get("op");
                        log.logDebug(Constants.MODULE + "<< condition >>" + condition);
                        if (condition.equalsIgnoreCase("asc")) {
                            return Order.asc(fieldName);
                        } else if (condition.equalsIgnoreCase("desc")) {
                            return Order.desc(fieldName);
                        }
                    }
                }
            }
        }
        return null;
    }

    public String createSuccessResponse(ProvisioningDSC provisioningDSC) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String updateSuccessTaskUpdate() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.SUCCESS_START_201);
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String encode(String message) throws UnsupportedEncodingException {
        log.logDebug("SASFUTIL encode() START");
        String encode = "";
//        String encode = Base64.getEncoder().encodeToString(message.getBytes());
        log.logDebug("SASFUTIL encode() value" + encode);
        log.logDebug("SASFUTIL encode() END");
        return encode;

    }

    public String decode(String message) throws UnsupportedEncodingException {
        log.logDebug("SASFUTIL decode() START");
        String decode = URLDecoder.decode(message, "UTF-8");
        log.logDebug("SASFUTIL decode() value" + decode);
        log.logDebug("SASFUTIL decode() END");
        return decode;
    }

    public String deleteSuccessResponse() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(Constants.XML_HEADER);
        sBuilder.append(Constants.SUCCESS_START_201);
        sBuilder.append(Constants.END_RESPONSE);
        return sBuilder.toString();
    }

    public String getAccountId(Map<String, String> varmap) throws ClientError {
        String url = URLUtility.getcommonApiURL() + SaasificationConsts.ACCOUNT_GET_WITHOUT_ID_URL + "?q=" + URLEncoder.encode("{\"name\":\"" + varmap.get("accountname") + "\"" + ",\"condition\":\"eq\"," + "\"parentguid\":\"" + varmap.get("parentid") + "\"}");
        log.logDebug("url ::: " + url);
        String responseXml = integrationObj.restCallAPI(url, RESTUtility.GET, "");
        log.logDebug("responseXml ::: " + responseXml);
        String accountId = XPathUtil.getXPathValue("//account/id/text()", responseXml);
        log.logDebug("accountId ::: " + accountId);
        return StringUtility.isNotNullOrEmpty(accountId) ? accountId : null;
    }
}
