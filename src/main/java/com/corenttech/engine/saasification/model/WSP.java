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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author veyron
 */
@Entity
@Table(name = "wsp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WSP.findAll", query = "SELECT w FROM WSP w"),
    @NamedQuery(name = "WSP.findById", query = "SELECT w FROM WSP w WHERE w.id = :id"),
    @NamedQuery(name = "WSP.findByAccountid", query = "SELECT w FROM WSP w WHERE w.accountid = :accountid"),
    @NamedQuery(name = "WSP.findByResourceid", query = "SELECT w FROM WSP w WHERE w.resourceid = :resourceid"),
    @NamedQuery(name = "WSP.findBySaasvariantid", query = "SELECT w FROM WSP w WHERE w.saasvariantid = :saasvariantid"),
    @NamedQuery(name = "WSP.findByNodeid", query = "SELECT w FROM WSP w WHERE w.nodeid = :nodeid"),
    @NamedQuery(name = "WSP.findByTonodeid", query = "SELECT w FROM WSP w WHERE w.tonodeid = :tonodeid"),
    @NamedQuery(name = "WSP.findByCloudresourceid", query = "SELECT w FROM WSP w WHERE w.cloudresourceid = :cloudresourceid"),
    @NamedQuery(name = "WSP.findByWorkloadname", query = "SELECT w FROM WSP w WHERE w.workloadname = :workloadname"),
    @NamedQuery(name = "WSP.findByWorkloadversion", query = "SELECT w FROM WSP w WHERE w.workloadversion = :workloadversion"),
    @NamedQuery(name = "WSP.findByWorkloadtype", query = "SELECT w FROM WSP w WHERE w.workloadtype = :workloadtype"),
    @NamedQuery(name = "WSP.findByIdentity", query = "SELECT w FROM WSP w WHERE w.identity = :identity"),
    @NamedQuery(name = "WSP.findByToidentity", query = "SELECT w FROM WSP w WHERE w.toidentity = :toidentity"),
    @NamedQuery(name = "WSP.findBySource", query = "SELECT w FROM WSP w WHERE w.source = :source"),
    @NamedQuery(name = "WSP.findByWorkloadrefid", query = "SELECT w FROM WSP w WHERE w.workloadrefid = :workloadrefid"),
    @NamedQuery(name = "WSP.findByFromresourceid", query = "SELECT w FROM WSP w WHERE w.fromresourceid = :fromresourceid"),
    @NamedQuery(name = "WSP.findByShareability", query = "SELECT w FROM WSP w WHERE w.shareability = :shareability"),
    @NamedQuery(name = "WSP.findByStatus", query = "SELECT w FROM WSP w WHERE w.status = :status"),
    @NamedQuery(name = "WSP.findByStaaction", query = "SELECT w FROM WSP w WHERE w.staaction = :staaction"),
    @NamedQuery(name = "WSP.findByAction", query = "SELECT w FROM WSP w WHERE w.action = :action"),
    @NamedQuery(name = "WSP.findByMessage", query = "SELECT w FROM WSP w WHERE w.message = :message"),
    @NamedQuery(name = "WSP.findByCreatedDate", query = "SELECT w FROM WSP w WHERE w.createdDate = :createdDate"),
    @NamedQuery(name = "WSP.findByDeletedDate", query = "SELECT w FROM WSP w WHERE w.deletedDate = :deletedDate"),
    @NamedQuery(name = "WSP.findByUpdatedDate", query = "SELECT w FROM WSP w WHERE w.updatedDate = :updatedDate")})
public class WSP implements Serializable {

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
    @Column(name = "saasvariantid")
    private String saasvariantid;
    @Basic(optional = false)
    @Column(name = "nodeid")
    private String nodeid;
    @Column(name = "tonodeid")
    private String tonodeid;
    @Column(name = "cloudresourceid")
    private String cloudresourceid;
    @Basic(optional = false)
    @Column(name = "workloadname")
    private String workloadname;
    @Column(name = "workloadversion")
    private String workloadversion;
    //@Basic(optional = false)
    @Column(name = "workloadtype")
    private String workloadtype;
    /*private String workloadip;

     public String getWorkloadip() {
     return workloadip;
     }

     public void setWorkloadip(String workloadip) {
     this.workloadip = workloadip;
     }*/
//    @Basic(optional = false)
    @Column(name = "workloadlocation")
    private String workloadlocation;
    @Column(name = "identity")
    private String identity;
    @Column(name = "toidentity")
    private String toidentity;
    @Column(name = "source")
    private String source;
    @Column(name = "workloadrefid")
    private String workloadrefid;
    @Column(name = "fromresourceid")
    private String fromresourceid;
    @Lob
    @Column(name = "metadata")
    private String metadata;
    @Basic(optional = false)
    @Column(name = "shareability")
    private String shareability;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "staaction")
    private String staaction;
    @Basic(optional = false)
    @Column(name = "action")
    private String action;
    @Lob
    @Column(name = "effort")
    private String effort;
    @Column(name = "message")
    private String message;
    @Lob
    @Column(name = "properties")
    private String properties;
    @Lob
    @Column(name = "firewall")
    private String firewall;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "deleted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public WSP() {
    }

    public WSP(String id) {
        this.id = id;
    }

    public WSP(String id, String accountid, String resourceid,String nodeid, String workloadname, String shareability, String status, String action, Date createdDate) {
        this.id = id;
        this.accountid = accountid;
        this.resourceid = resourceid;
        //this.saasvariantid = saasvariantid;
        this.nodeid = nodeid;
        this.workloadname = workloadname;
       // this.workloadtype = workloadtype;
        this.shareability = shareability;
        this.status = status;
        this.action = action;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTonodeid() {
        return tonodeid;
    }

    public void setTonodeid(String tonodeid) {
        this.tonodeid = tonodeid;
    }

    public String getCloudresourceid() {
        return cloudresourceid;
    }

    public void setCloudresourceid(String cloudresourceid) {
        this.cloudresourceid = cloudresourceid;
    }

    public String getWorkloadname() {
        return workloadname;
    }

    public void setWorkloadname(String workloadname) {
        this.workloadname = workloadname;
    }

    public String getWorkloadversion() {
        return workloadversion;
    }

    public void setWorkloadversion(String workloadversion) {
        this.workloadversion = workloadversion;
    }

    public String getWorkloadtype() {
        return workloadtype;
    }

    public void setWorkloadtype(String workloadtype) {
        this.workloadtype = workloadtype;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getToidentity() {
        return toidentity;
    }

    public void setToidentity(String toidentity) {
        this.toidentity = toidentity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWorkloadrefid() {
        return workloadrefid;
    }

    public void setWorkloadrefid(String workloadrefid) {
        this.workloadrefid = workloadrefid;
    }

    public String getFromresourceid() {
        return fromresourceid;
    }

    public void setFromresourceid(String fromresourceid) {
        this.fromresourceid = fromresourceid;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getShareability() {
        return shareability;
    }

    public void setShareability(String shareability) {
        this.shareability = shareability;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaaction() {
        return staaction;
    }

    public void setStaaction(String staaction) {
        this.staaction = staaction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
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

    public String getFirewall() {
        return firewall;
    }

    public void setFirewall(String firewall) {
        this.firewall = firewall;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getWorkloadlocation() {
        return workloadlocation;
    }

    public void setWorkloadlocation(String workloadlocation) {
        this.workloadlocation = workloadlocation;
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
        if (!(object instanceof WSP)) {
            return false;
        }
        WSP other = (WSP) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.corenttech.engine.saasification.model.WSP[ id=" + id + " ]";
    }
}
