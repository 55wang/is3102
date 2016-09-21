/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.cms;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.customer.Customer;
import entity.common.AuditLog;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "customerProfileManagedBean")
@ViewScoped
public class CustomerProfileManagedBean implements Serializable {
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    private AuditSessionBeanLocal auditSessionBean;
    
    private Customer customer; 
    private List<AuditLog> auditLogs;
    private Boolean editingPage = false;
    private Boolean profileEdited = false;

    
    /**
     * Creates a new instance of CustomerInformationManagedBean
     */
    public CustomerProfileManagedBean() {
       
    }
    
    public void goToEditPage (){
        editingPage = true;
    }
    
    public void goToConfirmPage(){
        editingPage = false;
        profileEdited = true;
    }
    
    public void save(){
        Customer result = customerProfileSessionBean.saveProfile(customer);
        if (result != null) {
            RedirectUtils.redirect("view_profile.xhtml");
        }
    }

    public Customer getCustomer() {
        return customer ;
    }
    
    @PostConstruct
    public void setCustomer() {
        this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        this.auditLogs = auditSessionBean.getAuditLogByCustomerID(SessionUtils.getUserName());
    }

    /**
     * @return the editingPage
     */
    public Boolean getEditingPage() {
        return editingPage;
    }

    /**
     * @param editingPage the editingPage to set
     */
    public void setEditingPage(Boolean editingPage) {
        this.editingPage = editingPage;
    }

    /**
     * @return the profileEdited
     */
    public Boolean getProfileEdited() {
        return profileEdited;
    }

    /**
     * @param profileEdited the profileEdited to set
     */
    public void setProfileEdited(Boolean profileEdited) {
        this.profileEdited = profileEdited;
    }  

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
    
}
