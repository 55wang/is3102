/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.bill;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import server.utilities.EnumUtils.BillType;
import server.utilities.EnumUtils.StatusType;

/**
 *
 * @author leiyang
 */
@Entity
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate = new Date();
    
    // info
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String shortCode;
    private String partnerBankCode;
    private StatusType status = StatusType.ACTIVE;
    private BillType type;// if bill type is credit card, then this is credit card bill
    // mapping
    
    // function
    
    // toString and equal
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
        hash = 13 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    @Override    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Organization other = (Organization) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Organization{" + "id=" + id + ", createDate=" + createDate + ", name=" + name + '}';
    }
    
    
    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the status
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusType status) {
        this.status = status;
    }

    /**
     * @return the shortCode
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * @param shortCode the shortCode to set
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * @return the type
     */
    public BillType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(BillType type) {
        this.type = type;
    }

    /**
     * @return the partnerBankCode
     */
    public String getPartnerBankCode() {
        return partnerBankCode;
    }

    /**
     * @param partnerBankCode the partnerBankCode to set
     */
    public void setPartnerBankCode(String partnerBankCode) {
        this.partnerBankCode = partnerBankCode;
    }
}
