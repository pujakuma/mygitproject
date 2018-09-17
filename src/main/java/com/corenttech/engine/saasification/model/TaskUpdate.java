/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.model;

/**
 *
 * @author puja
 */
public class TaskUpdate {
    private String status;
    private String message;
    private String artifactid;
    private String resourceid;
    private String toversionid;

    public String getArtifactid() {
        return artifactid;
    }

    public void setArtifactid(String artifactid) {
        this.artifactid = artifactid;
    }
 
    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getToversionid() {
        return toversionid;
    }

    public void setToversionid(String toversionid) {
        this.toversionid = toversionid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
