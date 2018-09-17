/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.TaskUpdate;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author veyron
 */
@Service("taskUpdateManager")
public interface TaskUpdateManager {

  
    public String updateTaskUpdate(Map<String, String> varmap)throws ClientError,ServerError,Exception;
    
    public String upgradeTaskUpdate(Map<String, String> varmap,TaskUpdate taskUpdate)throws ClientError,ServerError,Exception;
    
    
}
