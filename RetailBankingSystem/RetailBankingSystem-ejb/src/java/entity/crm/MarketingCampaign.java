/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.crm;

import entity.customer.Customer;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.TypeMarketingCampaign;

/**
 *
 * @author wang
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MarketingCampaign implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameCampaign;
    private TypeMarketingCampaign TypeCampaign; //email or ads
    private String description;
    private String landingPageName;
    
    private Long numOfResponse = 0L;
    private Long numOfTargetResponse = 0L;
    private Long clickCount = 0L; //when ads banner or email URL is clicked 
    
    @ManyToOne
    private StaffAccount staffAccount;
    @OneToOne
    private CustomerGroup customerGroup;

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
        if (!(object instanceof MarketingCampaign)) {
            return false;
        }
        MarketingCampaign other = (MarketingCampaign) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.crm.MarketingCampaign[ id=" + id + " ]";
    }

    public String getNameCampaign() {
        return nameCampaign;
    }

    public void setNameCampaign(String nameCampaign) {
        this.nameCampaign = nameCampaign;
    }

    public TypeMarketingCampaign getTypeCampaign() {
        return TypeCampaign;
    }

    public void setTypeCampaign(TypeMarketingCampaign TypeCampaign) {
        this.TypeCampaign = TypeCampaign;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StaffAccount getStaffAccount() {
        return staffAccount;
    }

    public void setStaffAccount(StaffAccount staffAccount) {
        this.staffAccount = staffAccount;
    }

    public CustomerGroup getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroup = customerGroup;
    }

    public String getLandingPageName() {
        return landingPageName;
    }

    public void setLandingPageName(String landingPageName) {
        this.landingPageName = landingPageName;
    }

    public Long getNumOfResponse() {
        return numOfResponse;
    }

    public void setNumOfResponse(Long numOfResponse) {
        this.numOfResponse = numOfResponse;
    }

    public Long getNumOfTargetResponse() {
        return numOfTargetResponse;
    }

    public void setNumOfTargetResponse(Long numOfTargetResponse) {
        this.numOfTargetResponse = numOfTargetResponse;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

}
