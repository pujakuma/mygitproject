/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.Saasify;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Shagul Hameed S
 */
@Service("saaSifyManager")
public interface SaaSifyManager {

    public void createSaasify(Saasify SaasifyPojo) throws ClientError, ServerError;

    public String getSaaSifyListxml(Map<String, String> saasifyMap) throws ClientError, ServerError;

    public void updateSaasifyRecord(Map<String, String> saasifyMap, Saasify saasify) throws ClientError, ServerError;

    public void deleteSaasifyRecord(Map<String, String> saasifyRecords) throws ClientError, ServerError;

}
