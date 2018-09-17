/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.saasification.model.WorkloadGroup;
import com.corenttech.engine.core.DAO;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saran Kumar
 */
@Service("workloadGroupDAO")
public interface WorkloadGroupDAO extends DAO<WorkloadGroup> {

    public List<WorkloadGroup> getWorkloadGroupList(String accountGuid, int limit, int offset, String order, String filter, Map<String, String> varMap, String resourceId) throws ServerError, ClientError;
    
    public List<WorkloadGroup> getWorkloadGroupList(Map<String,String> workloadMap);
    
    public WorkloadGroup getWorkloadGroupWithId(String workloadId);
}
