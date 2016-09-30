/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.cms;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
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
import server.utilities.EnumUtils.Occupation;
import utils.CommonUtils;
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

    private Customer customer;
    private List<AuditLog> auditLogs;
    private Boolean editingPage = false;
    private Boolean profileEdited = false;
    private List<String> occupationOptions = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> incomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);
    private String selectedOccupation;
    private String selectedIncome;

    /**
     * Creates a new instance of CustomerInformationManagedBean
     */
    public CustomerProfileManagedBean() {

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
        if (utilsSessionBean.checkUpdatedEmailIsUnique(customer) == false) {
            MessageUtils.displayInfo("Email is registered!");

        } else if (utilsSessionBean.checkUpdatedPhoneIsUnique(customer) == false) {
            MessageUtils.displayInfo("Phone is registered!");

        } else {
            Customer result = customerProfileSessionBean.saveProfile(customer);
            if (result != null) {
                MessageUtils.displayInfo("Profile successfully updated!");
                RedirectUtils.redirect("view_profile.xhtml");
            } else {
                MessageUtils.displayInfo("Update is unsuccessful, please check your input.");
            }
        }

    }
    

    public Customer getCustomer() {
        return customer;
    }

    @PostConstruct
    public void setCustomer() {
        this.customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        this.auditLogs = auditSessionBean.getAuditLogByCustomerID(SessionUtils.getUserName());
        selectedIncome = customer.getIncome().toString();
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

}
