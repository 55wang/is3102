/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.cms;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.cms.CustomerCaseSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.Customer;
import entity.customer.CustomerCase;
import entity.customer.Issue;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerCaseManagedBean")
@ViewScoped
public class CustomerCaseManagedBean implements Serializable {
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    @EJB
    private AuditSessionBeanLocal auditSessionBean;
    
    private MainAccount mainAccount; 
    private List<AuditLog> auditLogs;
    private CustomerCase customerCase = new CustomerCase();
    private List<Issue> issues = new ArrayList<Issue> ();
    private Issue newIssue = new Issue();
    private Boolean issuePage = false;
    private int numOfIssues = 0;
    

    /**
     * Creates a new instance of CustomerCaseManagedBean
     */
    public CustomerCaseManagedBean() {
    }
    
    public void goToIssuePage (){
        issuePage = true;
    }
    
    public void addIssue (){
        issues.add(newIssue);
        newIssue.setCustomerCase(customerCase);
        newIssue = new Issue();
        issuePage = false;
        numOfIssues++;
    }
    
    public void saveCase(){
        customerCase.setIssues(issues);
        customerCase.setMainAccount(mainAccount);
        mainAccount.addCase(customerCase);
        customerCaseSessionBean.saveCase(customerCase);
    }
    
    public void removeIssue(String issueID){
        for(int i = 0; i < issues.size(); i++){
            if(issues.get(i).toString().equals(issueID))
                issues.remove(i);
        }
    }

    @PostConstruct
    public void setMainAccount() {
        this.mainAccount = customerCaseSessionBean.getMainAccountByUserID(SessionUtils.getUserName());
        this.auditLogs = auditSessionBean.getAuditLogByCustomerID(SessionUtils.getUserName());
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public CustomerCase getCustomerCase() {
        return customerCase;
    }

    public void setCustomerCase(CustomerCase customerCase) {
        this.customerCase = customerCase;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public Boolean getIssuePage() {
        return issuePage;
    }

    public void setIssuePage(Boolean issuePage) {
        this.issuePage = issuePage;
    }

    public Issue getNewIssue() {
        return newIssue;
    }

    public void setNewIssue(Issue newIssue) {
        this.newIssue = newIssue;
    }

    public int getNumOfIssues() {
        return numOfIssues;
    }

    public void setNumOfIssues(int numOfIssues) {
        this.numOfIssues = numOfIssues;
    }
    
}
