/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.cms;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import entity.customer.Customer;
import java.io.Serializable;
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
    private Customer customer; 
    
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
    
    public Boolean save(){
        
        Boolean result = customerProfileSessionBean.saveProfile(customer);
        RedirectUtils.redirect("view_profile.xhtml");
        return result;
    }

    public Customer getCustomer() {
        return customer ;
    }
    
    @PostConstruct
    public void setCustomer() {
        this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
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

   
       
    
}
