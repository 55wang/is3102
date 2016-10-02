/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.card.CardTransactionSessionBeanLocal;
import ejb.session.cms.CustomerCaseSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.common.AuditLog;
import entity.customer.CustomerCase;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.CaseStatus;
import server.utilities.EnumUtils.CardOperatorChargebackStatus;
import server.utilities.CommonUtils;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "staffViewCustomerCaseManagedBean")
@ViewScoped
public class StaffViewCustomerCaseManagedBean implements Serializable{
    @EJB
    private CardTransactionSessionBeanLocal cardTransactionSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    
    private List<CustomerCase> cases;
    private String searchText;
    private String searchStaff;
    private List<StaffAccount> staffs = new ArrayList<>();
    private static CustomerCase transferedCase = new CustomerCase();
    
    private List<String> caseStatusList = CommonUtils.getEnumList(EnumUtils.CaseStatus.class);
    private List<String> issueFieldList = CommonUtils.getEnumList(EnumUtils.IssueField.class);
    
    /**
     * Creates a new instance of StaffCutomerCaseManagedBean
     */
    public StaffViewCustomerCaseManagedBean() {
    }
    
    public void search() {
        if (searchText.isEmpty()){
            cases = customerCaseSessionBean.getAllCaseUnderCertainStaff(SessionUtils.getStaff());
        }else{
            CustomerCase tempCase = customerCaseSessionBean.searchCaseByID(searchText);
            if(tempCase == null){
                cases = new ArrayList<CustomerCase>(); 
                MessageUtils.displayInfo("No results found");
            }else{      
                cases = new ArrayList<CustomerCase>();
                cases.add(tempCase);
            }
        }
    }
    
    public void start(CustomerCase cc) {
        cc.setCaseStatus(EnumUtils.CaseStatus.ONGOING);
        if(cc.getIsChargeBackCase()){ 
            if(cardOperatorSimulator())
                cc.setCardOperatorResponse(CardOperatorChargebackStatus.APPROVE);
            else
                cc.setCardOperatorResponse(CardOperatorChargebackStatus.REJECT);
        }
        Boolean result = customerCaseSessionBean.updateCase(cc);
        if (result == false) {
            MessageUtils.displayError("Case not found!");
        } else {
            emailServiceSessionBean.sendCaseStatusChangeToCustomer(cc.getMainAccount().getCustomer().getEmail(), cc);
            MessageUtils.displayInfo("Case information is updated!");
        }
    }
    
    public void update(CustomerCase cc) {
        Boolean result = customerCaseSessionBean.updateCase(cc);
        if (result == false) {
            MessageUtils.displayError("Case not found!");
        } else {
            emailServiceSessionBean.sendCaseStatusChangeToCustomer(cc.getMainAccount().getCustomer().getEmail(), cc);
            MessageUtils.displayInfo("Case information is updated!");
        }
    }
    
    public void transfer(StaffAccount staff) {
        transferedCase.setStaffAccount(staff);
        Boolean result = customerCaseSessionBean.updateCase(transferedCase);
        if (result == false) {
            MessageUtils.displayError("Transfer fail!");
        } else {
            transferedCase = new CustomerCase();
            RedirectUtils.redirect("staff-view-case.xhtml");
        }
    }
    
    public Boolean cardOperatorSimulator(){
        return Math.random() < 0.5;
    }
    
    public void approveChargeBack(CustomerCase chargeBackCase){
        CardTransaction originalTransaction = chargeBackCase.getChargebackTransaction();
        CardTransaction reverseTransaction = new CardTransaction();
        reverseTransaction.setAmount(originalTransaction.getAmount());
        if(originalTransaction.isIsCredit())
            reverseTransaction.setIsCredit(false);
        else
            reverseTransaction.setIsCredit(true);
        reverseTransaction.setCreateDate(new Date());
        reverseTransaction.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
        reverseTransaction.setCreditCardAccount(originalTransaction.getCreditCardAccount());
        reverseTransaction.setTransactionCode(originalTransaction.getTransactionCode());
        reverseTransaction.setTransactionDescription("This is a reversed transaction. Original transaction is " 
                + originalTransaction.getTransactionDescription());
     
        Boolean result = cardTransactionSessionBean.createCardTransaction(reverseTransaction);
        if(result){
            chargeBackCase.setCaseStatus(CaseStatus.RESOLVED);
            chargeBackCase.setReverseTransaction(reverseTransaction);
            customerCaseSessionBean.updateCase(chargeBackCase);
            emailServiceSessionBean.sendchargeBackGmailForSuccessfulCustomer(chargeBackCase.getMainAccount().getCustomer().getEmail(), chargeBackCase.getId());
            MessageUtils.displayInfo("The chargeback is approved");
        }else{
            MessageUtils.displayError("Error");
        }
    }
    
    public void cancelCase(CustomerCase chargeBackCase){
        chargeBackCase.setCaseStatus(CaseStatus.CANCELLED);
        Boolean result =  customerCaseSessionBean.updateCase(chargeBackCase);
        if(result){
            if(chargeBackCase.getIsChargeBackCase()){
            emailServiceSessionBean.sendchargeBackGmailForRejectedCustomer(chargeBackCase.getMainAccount().getCustomer().getEmail(), chargeBackCase.getId());
            } else
            emailServiceSessionBean.sendCaseStatusChangeToCustomer(chargeBackCase.getMainAccount().getCustomer().getEmail(), chargeBackCase);
            
            MessageUtils.displayInfo("The case is cancelled");
        }else{
            MessageUtils.displayError("Error");
        }
    }
    
    public void resolveCase(CustomerCase chargeBackCase){
        chargeBackCase.setCaseStatus(CaseStatus.RESOLVED);
        Boolean result =  customerCaseSessionBean.updateCase(chargeBackCase);
        if(result){
            emailServiceSessionBean.sendCaseStatusChangeToCustomer(chargeBackCase.getMainAccount().getCustomer().getEmail(), chargeBackCase);
            MessageUtils.displayInfo("The case is resolved");
        }else{
            MessageUtils.displayError("Error");
        }
    }
    
    public void redirectToTransferPage(CustomerCase cc){
        this.transferedCase = cc;
        RedirectUtils.redirect("staff-transfer-case.xhtml");
    }
    
    public Boolean isChargeBack(String selectedField){
        return selectedField.equals("CHARGEBACK");
    }
    
    public void searchStaff() {
        staffs = staffBean.searchStaffByUsernameOrName(searchStaff);
        removeSelf();
    }
    
    public void showAllStaff() {
        setStaffs(staffBean.getAllStaffs());
        removeSelf();
    }
    
    @PostConstruct
    public void setCases() {
        this.cases = customerCaseSessionBean.getAllCaseUnderCertainStaff(SessionUtils.getStaff());
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter view_customer_information.xhtml");
        a.setFunctionName("StaffViewCustomerCaseManagedBean @PostConstruct setCases()");
        a.setInput("Getting all customer cases");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    
    public List<CustomerCase> getCases() {
        return cases;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<String> getCaseStatusList() {
        return caseStatusList;
    }

    public void setCaseStatusList(List<String> caseStatusList) {
        this.caseStatusList = caseStatusList;
    }

    public List<String> getIssueFieldList() {
        return issueFieldList;
    }

    public void setIssueFieldList(List<String> issueFieldList) {
        this.issueFieldList = issueFieldList;
    }

    public List<StaffAccount> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<StaffAccount> staffs) {
        this.staffs = staffs;
    }
    
    public void removeSelf() {
        List<StaffAccount> result = new ArrayList<>();
        StaffAccount self = SessionUtils.getStaff();
        for (StaffAccount sa : staffs) {
            if (!sa.equals(self)) {
                result.add(sa);
            }
        }
        staffs = result;
    }

    public CustomerCase getTransferedCase() {
        return transferedCase;
    }

    public void setTransferedCase(CustomerCase transferedCase) {
        this.transferedCase = transferedCase;
    }

    public String getSearchStaff() {
        return searchStaff;
    }

    public void setSearchStaff(String searchStaff) {
        this.searchStaff = searchStaff;
    }
}
