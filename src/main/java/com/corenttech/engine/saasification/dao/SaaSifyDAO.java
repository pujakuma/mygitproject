/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DAO;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.Saasify;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Shagul Hameed S
 */
@Repository("saaSifyDAO")
public interface SaaSifyDAO extends DAO<Saasify> {

    public List<Saasify> getSaasifyList(Map<String, String> saasifyMap) throws ServerError;

    public Saasify getSaasifyRecord(Map<String, String> saasifyMap) throws ServerError;

}
