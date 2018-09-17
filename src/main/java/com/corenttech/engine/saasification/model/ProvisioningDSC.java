/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.model;

/**
 *
 * @author veyron
 */
public class ProvisioningDSC {

    private static final long serialVersionUID = 1L;
    private String saasvariantid;
    private String sourcenodeid;
    private String destinationdepid;
    private String destnodeid;
    private String action;
    private String parentid;
    private String coeplanid;
    private String ispaas;
    private String cloudproviderid;
    private String saasboxid;
    private String executionnode;

    public String getExecutionnode() {
        return executionnode;
    }

    public void setExecutionnode(String executionnode) {
        this.executionnode = executionnode;
    }

    public String getCloudproviderid() {
        return cloudproviderid;
    }

    public void setCloudproviderid(String cloudproviderid) {
        this.cloudproviderid = cloudproviderid;
    }

    public String getIspaas() {
        return ispaas;
    }

    public void setIspaas(String ispaas) {
        this.ispaas = ispaas;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSaasvariantid() {
        return saasvariantid;
    }

    public void setSaasvariantid(String saasvariantid) {
        this.saasvariantid = saasvariantid;
    }

    public String getSourcenodeid() {
        return sourcenodeid;
    }

    public void setSourcenodeid(String sourcenodeid) {
        this.sourcenodeid = sourcenodeid;
    }

    public String getDestinationdepid() {
        return destinationdepid;
    }

    public void setDestinationdepid(String destinationdepid) {
        this.destinationdepid = destinationdepid;
    }

    public String getDestnodeid() {
        return destnodeid;
    }

    public void setDestnodeidd(String destnodeid) {
        this.destnodeid = destnodeid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getCoeplanid() {
        return coeplanid;
    }

    public void setCoeplanid(String coeplanid) {
        this.coeplanid = coeplanid;
    }

    public String getSaasboxid() {
        return saasboxid;
    }

    public void setSaasboxid(String saasboxid) {
        this.saasboxid = saasboxid;
    }
    
}
