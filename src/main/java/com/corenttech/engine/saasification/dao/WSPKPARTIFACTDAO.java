/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.dao;

import com.corenttech.engine.core.DAO;
import com.corenttech.engine.core.exception.ClientError;
import com.corenttech.engine.core.exception.ServerError;
import com.corenttech.engine.saasification.model.KbArtifacts;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saravanan J
 */
@Service("wSPKPARTIFACTDAO")
public interface WSPKPARTIFACTDAO extends DAO<KbArtifacts> {

    public List<KbArtifacts> getKpList(String id, String guid, String os, int limit, int offset, String order, String filter) throws ServerError, ClientError;

    public KbArtifacts getKpListWithversion(String versionid, String name) throws ServerError, ClientError;
}
