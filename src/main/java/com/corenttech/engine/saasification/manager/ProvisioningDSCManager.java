/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.ProvisioningDSC;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author veyron
 */
@Service("provisioningDSCManager")
public interface ProvisioningDSCManager {

    public String createProvisionDSC(Map<String, String> varmap,ProvisioningDSC provisioningDSC)throws ClientError,ServerError,Exception;
    
}
