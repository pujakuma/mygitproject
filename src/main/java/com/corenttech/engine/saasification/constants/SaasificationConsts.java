/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.constants;

/**
 *
 * @author shagul
 */
public class SaasificationConsts {

    public enum URLs {

        SHARABILITY_WORKLOAD_URL("/shareability?type=workloadanalyze"),
        SHARABILITY_TOPOLOGY_URL("/shareability?type=suggestedtopology"),
        CEAPI_SESSIONS("v2/admin/sessions"),
        ROOT_CONFIG_URL("/v2/config/attributes?account={account}");
        private final String url;

        private URLs(String pURL) {
            this.url = pURL;
        }

        public String getUrl() {
            return url;
        }
    }
    /**
     * Constants to define report request type
     */
    public static final String WORKLOADREPORT = "workloadreport";
    public static final String TOPOLOGYREPORT = "topologyreport";
    /**
     * Constants to define tags
     */
    public static final String ACCOUNT_TAG = "{account}";
    /**
     * Constants to define account hierarchy
     */
    public static final String SUPER_ROOT = "superroot";
    /**
     * Constants to define Status
     */
    public static final String TRASH = "Trash";
    /**
     * Constants to define Module name
     *
     */
    public static final String MODULE = "SSF";
    /**
     * Constants to define error codes
     *
     */
    public static final String INVALID_ACCOUNT = "SAAS001";
    public static final String INVALID_XML = "SAAS002";
    public static final String INVALID_INPUT = "SAAS003";
    public static final String INVALID_NODESPECID = "SAAS004";
    public static final String NO_RECORD_FOUND = "SAAS005";
    public static final String INVALID_WSPID = "SAAS006";
    public static final String COULDNOT_UPDATE = "SAAS007";
    public static final String DB_ERROR = "SAAS008";
    public static final String INVALID_ARTIFACTID = "SAAS009";
    public static final String UNABLETO_GETTHERECORD = "SAAS010";
    public static final String UNABLETO_CREATE = "SAAS011";
    public static final String INVALID_TAG = "SAAS012";
    public static final String INVALID_TASKID = "SAAS013";
    /**
     * Constants to define rest error codes
     *
     */
    public static final String SERVER_ERROR = "500";
    /**
     * Constants to define variable used for validation
     *
     */
    public static final String DB = "database";
    public static final String DEPLOYMENT = "DEP";
    public static final String INSTANCE = "instance";
    public static final String PROVISION = "provisioning";
    public static final String FALSE = "false";
    public static final String TRUE = "true";
    public static final String COMPLETED = "completed";
    /**
     * Constants to define variable used for id generation
     *
     */
    public static final String WSP = "WSP-";
    public static final String IDENTITY = "IDY-";
    public static final String ARTIFACTSPEC = "ASC-";
    public static final String ARTIFACTIDENTITY = "AIDY-";
    public static final String UPDATESUCCESS = "Record updated sucessfully";
    public static final String UPDATEARTIFACTXML = "<artifactspec><status>{status}</status><message><![CDATA[{message}]]></message></artifactspec>";
    public static final String UPDATECLONEPLANXML = "<task><status>{status}</status><description><![CDATA[{description}]]></description></task>";
    public static final String CREATEAVMCLONEPLANXML = "<coeclone><name>{name}</name><resourceid>{resourceid}</resourceid><resourcemoreinfo>{resourcemoreinfo}</resourcemoreinfo><deploymentplanname>{deploymentplanname}</deploymentplanname></coeclone>";
    //constant url
    public static final String AVMDEPLOY_GET_URL = "/avm/avmdeploy?account={account}";
    public static final String AVMNODE_GET_URL = "/avm/node?account={account}";
    public static final String AVM_SAASBOX_GET_URL = "/avm/saasbox?account={account}";
    public static final String RESOURCE_GROUP_GET_URL = "/cloud/resourcegroup?depid={depid}&nodeid={nodeid}";
    public static final String RESOURCE_GROUP_DETAILS_GET_URL = "/cloud/resourcedetails?";
    public static final String RESOURCE_GROUP_POST_URL = "/cloud/resourcegroup?account={account}";
    public static final String RESOURCE_GROUP_DETAILS_POST_URL = "/cloud/paas/resource?account={account}";
    public static final String AGENT_GET_URL = "/agent/installagent?account={account}";
    public static final String AGENT_CLONE_URL = "/agent/installagentclone?accountid={account}&sourcedepid={source}&sourcenodeid={nodeid}&destinationdepid={depid}&destinationnodeid={destnodeid}";
    public static final String ACCOUNT_GET_URL = "/v2/admin/accounts?account={account}";
    public static final String ACCOUNT_GET_WITHOUT_ID_URL = "/v2/admin/accounts";
    public static final String APPVERSION_GET_URL = "/avm/appversion?account={account}&q={query}";
    public static final String COE_PLAN_ADD_TASK_URL = "/coe/plan?planId={planid}";
    public static final String AVM_TOPOLOGY_URL = "/avm/template?account={account}";
    //constant xml
    public static final String RESOURCE_GROUP_REQ = "<resourcegroup><resourceid>{depid}</resourceid><nodeid>{nodeid}</nodeid><resourcescaleid>{scaleid}</resourcescaleid><cloudresourceid>{instanceid}</cloudresourceid><cloudparentid>{cloudpar}</cloudparentid><isspec>{isspec}</isspec><type>{type}</type><status>{status}</status></resourcegroup>";
    public static final String RESOURCE_GROUP_DETAILS_REQ = "<resourcedetails><resourcedetail><resourceid>{depid}</resourceid><nodeid>{nodeid}</nodeid><cloudkeyid>{cloudkeyid}</cloudkeyid><resourcetype>{resourcetype}</resourcetype><resourceparentid>{resourceparentid}</resourceparentid><cloudkeyname>{cloudkeyname}</cloudkeyname><resourcemeta><![CDATA[{resourcemeta}]]></resourcemeta><cloudproperties>{cloudproperties}</cloudproperties><isshared>{isshared}</isshared>\n" +
" <isglobal>{isglobal}</isglobal><status>{status}</status></resourcedetail></resourcedetails>";
    public static final String COE_PLAN_TASK_UPDATE_REQ = "<plan action=\"updatePlan\" execute=\"false\">{taskxml}</plan>";
    public static final String CLOUD_ACCOUNT_GET_URL = "/cloud/provideraccount?account={account}";
    /*
     Others
     */
    public static final String SESSION_INPUT = "<request><apikey>{apikey}</apikey></request>";
}
