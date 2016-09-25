/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.cms.CustomerCaseSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.customer.CustomerCase;
import entity.customer.Issue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.CardTransactionStatus;
import server.utilities.EnumUtils.IssueField;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "staffCreateCustomerCaseManagedBean")
@ViewScoped
public class StaffCreateCustomerCaseManagedBean implements Serializable{
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    
    private String issueField = "CHARGEBACK";
    private String selectChargeBack = "CHARGEBACK";
    private CustomerCase newCase = new CustomerCase();
    private List<Issue> issues = new ArrayList<Issue> ();
    private Issue newIssue = new Issue();
    private String chargebackTransactionID;
    
    
    /**
     * Creates a new instance of StaffCreateCustomerCaseManagedBean
     */
    public StaffCreateCustomerCaseManagedBean() {
    }

    public String getIssueField() {
        return issueField;
    }

    public void setIssueField(String issueField) {
        this.issueField = issueField;
    }

    public String getSelectChargeBack() {
        return selectChargeBack;
    }

    public void setSelectChargeBack(String selectChargeBack) {
        this.selectChargeBack = selectChargeBack;
    }

    public CustomerCase getNewCase() {
        return newCase;
    }

    public void setNewCase(CustomerCase newCase) {
        this.newCase = newCase;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }
    
    public void addIssue(){
        newIssue.setField(IssueField.CHARGEBACK);
        this.issues.add(newIssue);
        newIssue = new Issue();
        newIssue.setField(IssueField.CHARGEBACK);   
    }
    
    public void createCase(){
        newCase.setIssues(issues);
        for (Issue sue : issues) {
            sue.setCustomerCase(newCase);
        }
        if(issueField.equals(selectChargeBack)){
            newCase.setIsChargeBackCase(Boolean.TRUE);
            if(newCase.getIsChargeBackCase()) newCase.setCardOperatorResponse(EnumUtils.cardOperatorChargebackStatus.PENDING);
            System.out.println("chargebackTransactionID: " + chargebackTransactionID);
            CardTransaction ct = cardAcctSessionBean.getSpecificCaedTransactionFromId(Long.parseLong(chargebackTransactionID));
            if(ct == null || ct.getCardTransactionStatus().equals(CardTransactionStatus.CANCELLED))
                MessageUtils.displayError("Transaction not found");
            else{
                newCase.setMainAccount(ct.getCreditCardAccount().getMainAccount());
                newCase.setCreateDate(new Date());
                newCase.setChargebackTransaction(ct);
                customerCaseSessionBean.saveCase(newCase);
                RedirectUtils.redirect("staff-view-case.xhtml");
            }
        }
    }

    public String getChargebackTransactionID() {
        return chargebackTransactionID;
    }

    public void setChargebackTransactionID(String chargebackTransactionID) {
        this.chargebackTransactionID = chargebackTransactionID;
    }

    public Issue getNewIssue() {
        return newIssue;
    }

    public void setNewIssue(Issue newIssue) {
        this.newIssue = newIssue;
    }
}