/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.crm;

import entity.customer.Customer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author wang
 */
@Entity
public class CustomerGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique=true, nullable = false)
    private String groupName;
    private Long depositRecency = 1L;
    private Long depositFrequency =1L;
    private Long depositMonetary =1L;
    
    private Long cardRecency =1L;
    private Long cardFrequency =1L;
    private Long cardMonetary =1L;
    
    private String hashTag;
    private Double actualIncome = 0.0;
    
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Customer> customer = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof CustomerGroup)) {
            return false;
        }
        CustomerGroup other = (CustomerGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.crm.CustomerGroup[ id=" + id + " ]";
    }

    public List<Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(List<Customer> customer) {
        this.customer = customer;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getDepositRecency() {
        return depositRecency;
    }

    public void setDepositRecency(Long depositRecency) {
        this.depositRecency = depositRecency;
    }

    public Long getDepositFrequency() {
        return depositFrequency;
    }

    public void setDepositFrequency(Long depositFrequency) {
        this.depositFrequency = depositFrequency;
    }

    public Long getDepositMonetary() {
        return depositMonetary;
    }

    public void setDepositMonetary(Long depositMonetary) {
        this.depositMonetary = depositMonetary;
    }

    public Long getCardRecency() {
        return cardRecency;
    }

    public void setCardRecency(Long cardRecency) {
        this.cardRecency = cardRecency;
    }

    public Long getCardFrequency() {
        return cardFrequency;
    }

    public void setCardFrequency(Long cardFrequency) {
        this.cardFrequency = cardFrequency;
    }

    public Long getCardMonetary() {
        return cardMonetary;
    }

    public void setCardMonetary(Long cardMonetary) {
        this.cardMonetary = cardMonetary;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public Double getActualIncome() {
        return actualIncome;
    }

    public void setActualIncome(Double actualIncome) {
        this.actualIncome = actualIncome;
    }

}
