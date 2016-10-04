/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bill;

import ejb.session.bill.BillSessionBeanLocal;
import entity.bill.Organization;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import server.utilities.CommonUtils;
import server.utilities.EnumUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "managedOrgManagedBean")
@ViewScoped
public class ManagedOrgManagedBean implements Serializable {

    // ejbs
    @EJB
    private BillSessionBeanLocal billBean;
    
    // attributes
    private String selectedType;
    private Organization organzation;
    private List<String> typeOptions = CommonUtils.getEnumList(EnumUtils.BillType.class);
    private List<Organization> organizations = new ArrayList<>();
    
    // constructor
    public ManagedOrgManagedBean() {}
    
    @PostConstruct
    public void init() {
        setOrganizations(billBean.getActiveListOrganization());
        setOrganzation(new Organization());
    }
    
    // public functions
    public void create() {
        organzation.setType(EnumUtils.BillType.getEnum(selectedType));
        Organization result = billBean.createOrganization(getOrganzation());
        if (result != null) {
            MessageUtils.displayInfo("New Organization created!");
            setOrganzation(new Organization());
            organizations.add(result);
        } else {
            MessageUtils.displayError("Fail to create new organization");
        }
    }
    
    // Getter and Setters
    /**
     * @return the organzation
     */
    public Organization getOrganzation() {
        return organzation;
    }

    /**
     * @param organzation the organzation to set
     */
    public void setOrganzation(Organization organzation) {
        this.organzation = organzation;
    }

    /**
     * @return the organizations
     */
    public List<Organization> getOrganizations() {
        return organizations;
    }

    /**
     * @param organizations the organizations to set
     */
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    /**
     * @return the selectedType
     */
    public String getSelectedType() {
        return selectedType;
    }

    /**
     * @param selectedType the selectedType to set
     */
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    /**
     * @return the typeOptions
     */
    public List<String> getTypeOptions() {
        return typeOptions;
    }

    /**
     * @param typeOptions the typeOptions to set
     */
    public void setTypeOptions(List<String> typeOptions) {
        this.typeOptions = typeOptions;
    }
}
