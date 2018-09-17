/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DAO;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.WSP;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author puja
 */
@Service("wspdao")
public interface WSPDAO extends DAO<WSP> {
   
    public List<WSP> getWspList(String id,String accountid)throws ServerError, VerifyError;
    
    public List<WSP> getWspList(String accountId, int limit, int offset, String order, String filter, Map<String, String> varmap, String id)throws ServerError,ClientError;

    public List<WSP> getWspDetailsWithResource( String resourceid,String id, String accountid) throws ServerError;
}
