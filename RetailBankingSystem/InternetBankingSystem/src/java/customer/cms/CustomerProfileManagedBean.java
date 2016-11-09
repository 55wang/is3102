/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.cms;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.customer.Customer;
import entity.common.AuditLog;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.Income;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils.Occupation;
import util.exception.cms.CustomerNotExistException;
import util.exception.cms.UpdateCustomerException;
import utils.JSUtils;
import utils.MessageUtils;
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
    private UtilsSessionBeanLocal utilsSessionBean;
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    private AuditSessionBeanLocal auditSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailBean;

    private Customer customer;
    private List<AuditLog> auditLogs;
    private Boolean editingPage = false;
    private Boolean profileEdited = false;
    private List<String> occupationOptions = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> incomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);
    private String selectedOccupation;
    private String selectedIncome;
    private Integer age;
    /**
     * Creates a new instance of CustomerInformationManagedBean
     */
    public CustomerProfileManagedBean() {}
    
    @PostConstruct
    public void setCustomer() {
        
        
        try {
            this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        } catch (CustomerNotExistException e) {
            System.out.println("CustomerNotExistException CustomerProfileManagedBean.java");
        }
        
        this.auditLogs = auditSessionBean.getAuditLogByCustomerID(SessionUtils.getUserName());
        try{
            selectedIncome = customer.getIncome().toString();
            selectedOccupation = customer.getOccupation().toString();
            age=DateUtils.calculateAge(customer.getBirthDay());
        }catch(Exception ex){
            selectedIncome = "";
            selectedOccupation = "";
        }
    }

    public void goToEditPage() {
        editingPage = true;
    }

    public void goToConfirmPage() {
        editingPage = false;
        profileEdited = true;
    }

    public void save() {
        customer.setIncome(Income.getEnum(selectedIncome));
        customer.setOccupation(Occupation.getEnum(selectedOccupation));
        if (utilsSessionBean.checkUpdatedEmailIsUnique(customer) == false) {
            MessageUtils.displayInfo("Email is registered!");

        } else if (utilsSessionBean.checkUpdatedPhoneIsUnique(customer) == false) {
            MessageUtils.displayInfo("Phone is registered!");

        } else {
            try {
                customerProfileSessionBean.updateCustomer(customer);
                MessageUtils.displayInfo("Profile successfully updated!");
                emailBean.sendUpdatedProfile(customer.getEmail());
                RedirectUtils.redirect("view_profile.xhtml");
            } catch (UpdateCustomerException e) {
                MessageUtils.displayInfo("Update is unsuccessful, please check your input.");
            }
        }

    }
    
    //for loan
    public void checkAge(){
        if (getAge() < 21) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_AGE);
        } else {
            JSUtils.callJSMethod("PF('myWizard').next();");
        }  
    }
    

    public Customer getCustomer() {
        return customer;
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

    /**
     * @return the selectedOccupation
     */
    public String getSelectedOccupation() {
        return selectedOccupation;
    }

    /**
     * @param selectedOccupation the selectedOccupation to set
     */
    public void setSelectedOccupation(String selectedOccupation) {
        this.selectedOccupation = selectedOccupation;
    }

    /**
     * @return the selectedIncome
     */
    public String getSelectedIncome() {
        return selectedIncome;
    }

    /**
     * @param selectedIncome the selectedIncome to set
     */
    public void setSelectedIncome(String selectedIncome) {
        this.selectedIncome = selectedIncome;
    }

    /**
     * @return the occupationOptions
     */
    public List<String> getOccupationOptions() {
        return occupationOptions;
    }

    /**
     * @param occupationOptions the occupationOptions to set
     */
    public void setOccupationOptions(List<String> occupationOptions) {
        this.occupationOptions = occupationOptions;
    }

    /**
     * @return the incomeOptions
     */
    public List<String> getIncomeOptions() {
        return incomeOptions;
    }

    /**
     * @param incomeOptions the incomeOptions to set
     */
    public void setIncomeOptions(List<String> incomeOptions) {
        this.incomeOptions = incomeOptions;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
    
    
}
