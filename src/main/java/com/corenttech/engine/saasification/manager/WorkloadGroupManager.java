/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.saasification.model.WorkloadGroup;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author optima
 */
@Service("workloadGroupManager")
public interface WorkloadGroupManager {

    public String createWorkloadGroup(List<WorkloadGroup> workloadlist)throws ServerError,ClientError;
    
    public String getWorkloadGroup(Map<String, String> varMap) throws ClientError,ServerError;
    
    public void updateWorkloadGroup(Map<String, String> workloadMap, WorkloadGroup group) throws ServerError, ClientError;

    public void deleteWorkloadGroup(String workloadId)throws ServerError, ClientError;


}
