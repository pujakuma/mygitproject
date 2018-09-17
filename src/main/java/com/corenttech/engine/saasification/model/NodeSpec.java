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
 * @author fiesta
 */
@Entity
@Table(name = "node_spec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NodeSpec.findAll", query = "SELECT n FROM NodeSpec n")
    , @NamedQuery(name = "NodeSpec.findById", query = "SELECT n FROM NodeSpec n WHERE n.id = :id")
    , @NamedQuery(name = "NodeSpec.findByResourceid", query = "SELECT n FROM NodeSpec n WHERE n.resourceid = :resourceid")
    , @NamedQuery(name = "NodeSpec.findBySaasvariantid", query = "SELECT n FROM NodeSpec n WHERE n.saasvariantid = :saasvariantid")
    , @NamedQuery(name = "NodeSpec.findByNodeid", query = "SELECT n FROM NodeSpec n WHERE n.nodeid = :nodeid")
    , @NamedQuery(name = "NodeSpec.findByIssharable", query = "SELECT n FROM NodeSpec n WHERE n.issharable = :issharable")
    , @NamedQuery(name = "NodeSpec.findByStatus", query = "SELECT n FROM NodeSpec n WHERE n.status = :status")
    , @NamedQuery(name = "NodeSpec.findByMessage", query = "SELECT n FROM NodeSpec n WHERE n.message = :message")
    , @NamedQuery(name = "NodeSpec.findByDensity", query = "SELECT n FROM NodeSpec n WHERE n.density = :density")
    , @NamedQuery(name = "NodeSpec.findByCreatedate", query = "SELECT n FROM NodeSpec n WHERE n.createdate = :createdate")
    , @NamedQuery(name = "NodeSpec.findByUpdateddate", query = "SELECT n FROM NodeSpec n WHERE n.updateddate = :updateddate")
    , @NamedQuery(name = "NodeSpec.findByDeleteddate", query = "SELECT n FROM NodeSpec n WHERE n.deleteddate = :deleteddate")
    , @NamedQuery(name = "NodeSpec.findByAccountid", query = "SELECT n FROM NodeSpec n WHERE n.accountid = :accountid")})
public class NodeSpec implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "resourceid")
    private String resourceid;
    @Basic(optional = false)
    @Column(name = "saasvariantid")
    private String saasvariantid;
    @Basic(optional = false)
    @Column(name = "nodeid")
    private String nodeid;
    @Basic(optional = false)
    @Column(name = "issharable")
    private String issharable;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "message")
    private String message;
    @Lob
    @Column(name = "properties")
    private String properties;
    @Lob
    @Column(name = "connection")
    private String connection;
    @Lob
    @Column(name = "firewall")
    private String firewall;
    @Lob
    @Column(name = "device")
    private String device;
    @Lob
    @Column(name = "metadata")
    private String metadata;
    @Basic(optional = false)
    @Column(name = "density")
    private String density;
    @Basic(optional = false)
    @Column(name = "createdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @Column(name = "updateddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateddate;
    @Column(name = "deleteddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteddate;
    @Column(name = "accountid")
    private String accountid;

    public NodeSpec() {
    }

    public NodeSpec(String id) {
        this.id = id;
    }

    public NodeSpec(String id, String resourceid, String saasvariantid, String nodeid, String issharable, String status, String density, Date createdate) {
        this.id = id;
        this.resourceid = resourceid;
        this.saasvariantid = saasvariantid;
        this.nodeid = nodeid;
        this.issharable = issharable;
        this.status = status;
        this.density = density;
        this.createdate = createdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIssharable() {
        return issharable;
    }

    public void setIssharable(String issharable) {
        this.issharable = issharable;
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

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getFirewall() {
        return firewall;
    }

    public void setFirewall(String firewall) {
        this.firewall = firewall;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(Date updateddate) {
        this.updateddate = updateddate;
    }

    public Date getDeleteddate() {
        return deleteddate;
    }

    public void setDeleteddate(Date deleteddate) {
        this.deleteddate = deleteddate;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
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
        if (!(object instanceof NodeSpec)) {
            return false;
        }
        NodeSpec other = (NodeSpec) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.corenttech.engine.saasification.model.NodeSpec[ id=" + id + " ]";
    }
    
}
