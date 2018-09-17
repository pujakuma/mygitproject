/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corenttech.engine.saasification.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Saravanan J
 */
    @Entity
@Table(name = "kb_artifacts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KbArtifacts.findAll", query = "SELECT k FROM KbArtifacts k"),
    @NamedQuery(name = "KbArtifacts.findById", query = "SELECT k FROM KbArtifacts k WHERE k.id = :id"),
    @NamedQuery(name = "KbArtifacts.findByGuid", query = "SELECT k FROM KbArtifacts k WHERE k.guid = :guid"),
    @NamedQuery(name = "KbArtifacts.findByWorkloadname", query = "SELECT k FROM KbArtifacts k WHERE k.workloadname = :workloadname"),
    @NamedQuery(name = "KbArtifacts.findByVersion", query = "SELECT k FROM KbArtifacts k WHERE k.version = :version"),
    @NamedQuery(name = "KbArtifacts.findByOs", query = "SELECT k FROM KbArtifacts k WHERE k.os = :os"),
    @NamedQuery(name = "KbArtifacts.findByCreateddate", query = "SELECT k FROM KbArtifacts k WHERE k.createddate = :createddate"),
    @NamedQuery(name = "KbArtifacts.findByModifieddate", query = "SELECT k FROM KbArtifacts k WHERE k.modifieddate = :modifieddate")})
public class KbArtifacts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "GUID")
    private String guid;
    @Basic(optional = false)
    @Column(name = "WORKLOADNAME")
    private String workloadname;
    @Column(name = "VERSION")
    private String version;
    @Column(name = "OS")
    private String os;
    @Basic(optional = false)
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "MODIFIEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifieddate;

    public KbArtifacts() {
    }

    public KbArtifacts(Integer id) {
        this.id = id;
    }

    public KbArtifacts(Integer id, String guid, String workloadname, Date createddate) {
        this.id = id;
        this.guid = guid;
        this.workloadname = workloadname;
        this.createddate = createddate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getWorkloadname() {
        return workloadname;
    }

    public void setWorkloadname(String workloadname) {
        this.workloadname = workloadname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public Date getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(Date modifieddate) {
        this.modifieddate = modifieddate;
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
        if (!(object instanceof KbArtifacts)) {
            return false;
        }
        KbArtifacts other = (KbArtifacts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.corenttech.engine.saasification.model.KbArtifacts[ id=" + id + " ]";
    }
    
}
