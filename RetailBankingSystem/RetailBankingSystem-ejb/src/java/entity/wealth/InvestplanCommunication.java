/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.wealth;

import entity.customer.WealthManagementSubscriber;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author VIN-S
 */
@Entity
public class InvestplanCommunication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate = new Date();
    
    // mapping
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "investplanCommunication")
    private List<InvestplanMessage> messages = new ArrayList<InvestplanMessage>();
    @ManyToOne(cascade = {CascadeType.MERGE})
    private StaffAccount rm;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private WealthManagementSubscriber wms;
    @OneToOne(cascade = {CascadeType.MERGE}, optional = false)
    private InvestmentPlan ip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void addMessage(InvestplanMessage m) {
        messages.add(m);
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
        if (!(object instanceof InvestplanCommunication)) {
            return false;
        }
        InvestplanCommunication other = (InvestplanCommunication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.wealth.InvestplanCommunication[ id=" + id + " ]";
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<InvestplanMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<InvestplanMessage> messages) {
        this.messages = messages;
    }

    public StaffAccount getRm() {
        return rm;
    }

    public void setRm(StaffAccount rm) {
        this.rm = rm;
    }

    public WealthManagementSubscriber getWms() {
        return wms;
    }

    public void setWms(WealthManagementSubscriber wms) {
        this.wms = wms;
    }

    public InvestmentPlan getIp() {
        return ip;
    }

    public void setIp(InvestmentPlan ip) {
        this.ip = ip;
    }
    
}
