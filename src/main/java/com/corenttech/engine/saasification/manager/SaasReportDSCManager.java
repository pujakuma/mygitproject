/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import org.springframework.stereotype.Service;

/**
 *
 * @author Shagul Hameed S
 */
@Service("saasReportDSCManager")
public interface SaasReportDSCManager {

    String getSaaSReport(String topologyNodeInput,String resource_type) throws ClientError, ServerError;

}
