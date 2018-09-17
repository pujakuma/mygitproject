/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author veyron
 */
@Entity
@Table(name = "artifact_spec")
public class ArtifactSpec implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "accountid")
    private String accountid;
    @Basic(optional = false)
    @Column(name = "resourceid")
    private String resourceid;
    //@Basic(optional = false)
    @Column(name = "saasvariantid")
    private String saasvariantid;
    @Basic(optional = false)
    @Column(name = "nodeid")
    private String nodeid;
    @Basic(optional = false)
    @Column(name = "wspid")
    private String wspid;
    @Column(name = "source_artificat_identity")
    private String sourceartificatidentity;
//    @Basic(optional = false)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "artifactidentity")
    private String artifactidentity;
    @Basic(optional = false)
    @Column(name = "artifactidentitytype")
    private String artifactidentitytype;
    @Column(name = "depends_wspid")
    private String dependsWspid;
    @Column(name = "connectivity_artifact_idendity")
    private String connectivityArtifactIdendity;
    @Basic(optional = false)
    @Column(name = "iscommon")
    private String iscommon;
    @Column(name = "fromresourceid")
    private String fromresourceid;
    @Column(name = "fromversion")
    private String fromversion;
    @Column(name = "toversion")
    private String toversion;
    @Column(name = "fileguid")
    private String fileguid;
    @Column(name = "saasfileguid")
    private String saasfileguid;
    @Column(name = "taskid")
    private String taskid;
    @Basic(optional = false)
    @Column(name = "action")
    private String action;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "message")
    private String message;
    @Lob
    @Column(name = "properties")
    private String properties;
    @Lob
    @Column(name = "metadata")
    private String metadata;
    @Lob
    @Column(name = "firewall")
    private String firewall;
    @Lob
    @Column(name = "deepscan")
    private String deepscan;
    @Column(name = "pluginfileid")
    private String pluginfileid;
    @Basic(optional = false)
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "delete_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;

    public ArtifactSpec() {
    }

    public ArtifactSpec(String id) {
        this.id = id;
    }

    public ArtifactSpec(String id, String accountid, String resourceid, String saasvariantid, String nodeid, String wspid, String location, String artifactidentity, String artifactidentitytype, String iscommon, String action, String status, Date createDate) {
        this.id = id;
        this.accountid = accountid;
        this.resourceid = resourceid;
        this.saasvariantid = saasvariantid;
        this.nodeid = nodeid;
        this.wspid = wspid;
        this.location = location;
        this.artifactidentity = artifactidentity;
        this.artifactidentitytype = artifactidentitytype;
        this.iscommon = iscommon;
        this.action = action;
        this.status = status;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromversion() {
        return fromversion;
    }

    public String getToversion() {
        return toversion;
    }

    public void setToversion(String toversion) {
        this.toversion = toversion;
    }

    public void setFromversion(String fromversion) {
        this.fromversion = fromversion;
    }

    public String getFileguid() {
        return fileguid;
    }

    public void setFileguid(String fileguid) {
        this.fileguid = fileguid;
    }

    public String getSaasfileguid() {
        return saasfileguid;
    }

    public void setSaasfileguid(String saasfileguid) {
        this.saasfileguid = saasfileguid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getSaasvariantid() {
        return saasvariantid;
    }

    public void setSaasvariantid(String saasvariantid) {
        this.saasvariantid = saasvariantid;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getWspid() {
        return wspid;
    }

    public void setWspid(String wspid) {
        this.wspid = wspid;
    }

    public String getArtifactidentity() {
        return artifactidentity;
    }

    public void setArtifactidentity(String artifactidentity) {
        this.artifactidentity = artifactidentity;
    }

    public String getArtifactidentitytype() {
        return artifactidentitytype;
    }

    public void setArtifactidentitytype(String artifactidentitytype) {
        this.artifactidentitytype = artifactidentitytype;
    }

    public String getDependsWspid() {
        return dependsWspid;
    }

    public void setDependsWspid(String dependsWspid) {
        this.dependsWspid = dependsWspid;
    }

    public String getConnectivityArtifactIdendity() {
        return connectivityArtifactIdendity;
    }

    public void setConnectivityArtifactIdendity(String connectivityArtifactIdendity) {
        this.connectivityArtifactIdendity = connectivityArtifactIdendity;
    }

    public String getIscommon() {
        return iscommon;
    }

    public void setIscommon(String iscommon) {
        this.iscommon = iscommon;
    }

    public String getFromresourceid() {
        return fromresourceid;
    }

    public void setFromresourceid(String fromresourceid) {
        this.fromresourceid = fromresourceid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getFirewall() {
        return firewall;
    }

    public void setFirewall(String firewall) {
        this.firewall = firewall;
    }

    public String getDeepscan() {
        return deepscan;
    }

    public void setDeepscan(String deepscan) {
        this.deepscan = deepscan;
    }

    public String getPluginfileid() {
        return pluginfileid;
    }

    public void setPluginfileid(String pluginfileid) {
        this.pluginfileid = pluginfileid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getSourceartificatidentity() {
        return sourceartificatidentity;
    }

    public void setSourceartificatidentity(String sourceartificatidentity) {
        this.sourceartificatidentity = sourceartificatidentity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof ArtifactSpec)) {
            return false;
        }
        ArtifactSpec other = (ArtifactSpec) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.corenttech.engine.saasification.model.ArtifactSpec[ id=" + id + " ]";
    }
}
