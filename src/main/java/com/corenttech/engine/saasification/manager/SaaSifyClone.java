/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.manager;

import com.corenttech.engine.core.exception.ClientError;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author volvo
 */
@Service("saasifyClone")
public interface SaaSifyClone {
    
    public String cloneSpec(Map<String,String> varMap)throws ClientError, Exception;
    
    
}
