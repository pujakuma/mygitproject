/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fiesta
 */
@Entity
@Table(name = "saasify")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Saasify.findAll", query = "SELECT s FROM Saasify s")
    , @NamedQuery(name = "Saasify.findById", query = "SELECT s FROM Saasify s WHERE s.id = :id")
    , @NamedQuery(name = "Saasify.findByAccountId", query = "SELECT s FROM Saasify s WHERE s.accountId = :accountId")
    , @NamedQuery(name = "Saasify.findByApplicationId", query = "SELECT s FROM Saasify s WHERE s.applicationId = :applicationId")
    , @NamedQuery(name = "Saasify.findByDeploymentId", query = "SELECT s FROM Saasify s WHERE s.deploymentId = :deploymentId")
    , @NamedQuery(name = "Saasify.findByNodeId", query = "SELECT s FROM Saasify s WHERE s.nodeId = :nodeId")
    , @NamedQuery(name = "Saasify.findByType", query = "SELECT s FROM Saasify s WHERE s.type = :type")
    , @NamedQuery(name = "Saasify.findByStatus", query = "SELECT s FROM Saasify s WHERE s.status = :status")
    , @NamedQuery(name = "Saasify.findByResponse", query = "SELECT s FROM Saasify s WHERE s.response = :response")
    , @NamedQuery(name = "Saasify.findByCallBackUrl", query = "SELECT s FROM Saasify s WHERE s.callBackUrl = :callBackUrl")
    , @NamedQuery(name = "Saasify.findByCallBackMethod", query = "SELECT s FROM Saasify s WHERE s.callBackMethod = :callBackMethod")
    , @NamedQuery(name = "Saasify.findByCallBackInput", query = "SELECT s FROM Saasify s WHERE s.callBackInput = :callBackInput")
    , @NamedQuery(name = "Saasify.findByIsCallBack", query = "SELECT s FROM Saasify s WHERE s.isCallBack = :isCallBack")
    , @NamedQuery(name = "Saasify.findByCreatedDate", query = "SELECT s FROM Saasify s WHERE s.createdDate = :createdDate")
    , @NamedQuery(name = "Saasify.findByUpdatedDate", query = "SELECT s FROM Saasify s WHERE s.updatedDate = :updatedDate")
    , @NamedQuery(name = "Saasify.findByDeletedDate", query = "SELECT s FROM Saasify s WHERE s.deletedDate = :deletedDate")})
public class Saasify implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Column(name = "ACCOUNT_ID")
    private String accountId;
    @Column(name = "APPLICATION_ID")
    private String applicationId;
    @Column(name = "DEPLOYMENT_ID")
    private String deploymentId;
    @Column(name = "NODE_ID")
    private String nodeId;
    @Column(name = "TYPE")
    private String type;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;
    @Column(name = "RESPONSE")
    private String response;
    @Column(name = "CALL_BACK_URL")
    private String callBackUrl;
    @Column(name = "CALL_BACK_METHOD")
    private String callBackMethod;
    @Column(name = "CALL_BACK_INPUT")
    private String callBackInput;
    @Enumerated(EnumType.STRING)
    @Column(name = "IS_CALL_BACK")
    private IsCallBack isCallBack;
    @Lob
    @Column(name = "MESSAGE")
    private String message;
    @Lob
    @Column(name = "META")
    private String meta;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Column(name = "DELETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;

    public Saasify() {
    }

    public enum Status {
        ACTIVE, INACTIVE, TRASH, INPROGRESS, SUCCESS, FAILED;
    }

    public enum IsCallBack {
        TRUE, FALSE;
    }

    public Saasify(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getCallBackMethod() {
        return callBackMethod;
    }

    public void setCallBackMethod(String callBackMethod) {
        this.callBackMethod = callBackMethod;
    }

    public String getCallBackInput() {
        return callBackInput;
    }

    public void setCallBackInput(String callBackInput) {
        this.callBackInput = callBackInput;
    }

    public IsCallBack getIsCallBack() {
        return isCallBack;
    }

    public void setIsCallBack(IsCallBack isCallBack) {
        this.isCallBack = isCallBack;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Saasify)) {
            return false;
        }
        Saasify other = (Saasify) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.corenttech.engine.saasification.model.Saasify[ id=" + id + " ]";
    }

}
