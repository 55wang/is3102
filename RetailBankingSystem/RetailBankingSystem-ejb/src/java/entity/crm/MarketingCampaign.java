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
    private String landingPageUrl;
    private Integer numOfResponse;
    private Integer numOfTargetResponse;
    
    private EnumUtils.RFMLevel depositRecencyLevel;
    private EnumUtils.RFMLevel depositFrequencyLevel;
    private EnumUtils.RFMLevel depositMonetaryLevel;
    private EnumUtils.RFMLevel depositOverallRFMLevel;
    
    private EnumUtils.RFMLevel cardRecencyLevel;
    private EnumUtils.RFMLevel cardFrequencyLevel;
    private EnumUtils.RFMLevel cardMonetaryLevel;
    private EnumUtils.RFMLevel cardOverallRFMLevel;
    
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

    public String getLandingPageUrl() {
        return landingPageUrl;
    }

    public void setLandingPageUrl(String landingPageUrl) {
        this.landingPageUrl = landingPageUrl;
    }

    public Integer getNumOfResponse() {
        return numOfResponse;
    }

    public void setNumOfResponse(Integer numOfResponse) {
        this.numOfResponse = numOfResponse;
    }

    public Integer getNumOfTargetResponse() {
        return numOfTargetResponse;
    }

    public void setNumOfTargetResponse(Integer numOfTargetResponse) {
        this.numOfTargetResponse = numOfTargetResponse;
    }

    public StaffAccount getStaffAccount() {
        return staffAccount;
    }

    public void setStaffAccount(StaffAccount staffAccount) {
        this.staffAccount = staffAccount;
    }

    public EnumUtils.RFMLevel getDepositRecencyLevel() {
        return depositRecencyLevel;
    }

    public void setDepositRecencyLevel(EnumUtils.RFMLevel depositRecencyLevel) {
        this.depositRecencyLevel = depositRecencyLevel;
    }

    public EnumUtils.RFMLevel getDepositFrequencyLevel() {
        return depositFrequencyLevel;
    }

    public void setDepositFrequencyLevel(EnumUtils.RFMLevel depositFrequencyLevel) {
        this.depositFrequencyLevel = depositFrequencyLevel;
    }

    public EnumUtils.RFMLevel getDepositMonetaryLevel() {
        return depositMonetaryLevel;
    }

    public void setDepositMonetaryLevel(EnumUtils.RFMLevel depositMonetaryLevel) {
        this.depositMonetaryLevel = depositMonetaryLevel;
    }

    public EnumUtils.RFMLevel getDepositOverallRFMLevel() {
        return depositOverallRFMLevel;
    }

    public void setDepositOverallRFMLevel(EnumUtils.RFMLevel depositOverallRFMLevel) {
        this.depositOverallRFMLevel = depositOverallRFMLevel;
    }

    public EnumUtils.RFMLevel getCardRecencyLevel() {
        return cardRecencyLevel;
    }

    public void setCardRecencyLevel(EnumUtils.RFMLevel cardRecencyLevel) {
        this.cardRecencyLevel = cardRecencyLevel;
    }

    public EnumUtils.RFMLevel getCardFrequencyLevel() {
        return cardFrequencyLevel;
    }

    public void setCardFrequencyLevel(EnumUtils.RFMLevel cardFrequencyLevel) {
        this.cardFrequencyLevel = cardFrequencyLevel;
    }

    public EnumUtils.RFMLevel getCardMonetaryLevel() {
        return cardMonetaryLevel;
    }

    public void setCardMonetaryLevel(EnumUtils.RFMLevel cardMonetaryLevel) {
        this.cardMonetaryLevel = cardMonetaryLevel;
    }

    public EnumUtils.RFMLevel getCardOverallRFMLevel() {
        return cardOverallRFMLevel;
    }

    public void setCardOverallRFMLevel(EnumUtils.RFMLevel cardOverallRFMLevel) {
        this.cardOverallRFMLevel = cardOverallRFMLevel;
    }

    public CustomerGroup getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroup = customerGroup;
    }

}
