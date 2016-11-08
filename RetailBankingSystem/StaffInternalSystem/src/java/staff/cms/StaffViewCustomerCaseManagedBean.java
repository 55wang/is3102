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
import server.utilities.ConstantUtils;
import util.exception.cms.CustomerCaseNotFoundException;
import util.exception.cms.UpdateCaseException;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "staffViewCustomerCaseManagedBean")
@ViewScoped
public class StaffViewCustomerCaseManagedBean implements Serializable {

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

    @PostConstruct
    public void setCases() {
        this.cases = customerCaseSessionBean.getAllCaseUnderCertainStaff(SessionUtils.getStaff());
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter view_customer_information.xhtml");
        a.setFunctionName("StaffViewCustomerCaseManagedBean @PostConstruct setCases()");
        a.setFunctionInput("Getting all customer cases");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void search() {
        if (searchText.isEmpty()) {
            cases = customerCaseSessionBean.getAllCaseUnderCertainStaff(SessionUtils.getStaff());
        } else {

            try {
                CustomerCase tempCase = customerCaseSessionBean.getCaseById(searchText);
                cases = new ArrayList<>();
                cases.add(tempCase);
            } catch (CustomerCaseNotFoundException e) {
                System.out.println("CustomerCaseNotFoundException thrown at StaffViewCustomerCaseManagedBean");
                cases = new ArrayList<>();
                MessageUtils.displayError("No results found");
            }

        }
    }

    public void start(CustomerCase cc) {
        try {

            cc.setCaseStatus(EnumUtils.CaseStatus.ONGOING);
            if (cc.getIsChargeBackCase()) {
                if (cardOperatorSimulator()) {
                    cc.setCardOperatorResponse(CardOperatorChargebackStatus.APPROVE);
                } else {
                    cc.setCardOperatorResponse(CardOperatorChargebackStatus.REJECT);
                }
            }

            customerCaseSessionBean.updateCase(cc);
            emailServiceSessionBean.sendCaseStatusChangeToCustomer(cc.getMainAccount().getCustomer().getEmail(), cc);
            MessageUtils.displayInfo("Case information is updated!");

        } catch (UpdateCaseException e) {
            System.out.println("UpdateCaseException thrown at StaffViewCustomerCaseManagedBean.java");
            MessageUtils.displayError("Case not found!");
        }
    }

    public void update(CustomerCase cc) {
        try {

            CustomerCase result = null;

            result = customerCaseSessionBean.updateCase(cc);

            emailServiceSessionBean.sendCaseStatusChangeToCustomer(cc.getMainAccount().getCustomer().getEmail(), cc);
            MessageUtils.displayInfo("Case information is updated!");

        } catch (UpdateCaseException e) {
            System.out.println("UpdateCaseException thrown at StaffViewCustomerCaseManagedBean.java");
            MessageUtils.displayError("Case not found!");
        }
    }

    public void transfer(StaffAccount staff) {
        try {

            CustomerCase result = null;

            transferedCase.setStaffAccount(staff);

            result = customerCaseSessionBean.updateCase(transferedCase);
            transferedCase = new CustomerCase();
            RedirectUtils.redirect(ConstantUtils.STAFF_CMS_STAFF_VIEW_CASE);

        } catch (UpdateCaseException e) {
            System.out.println("UpdateCaseException thrown at StaffViewCustomerCaseManagedBean.java");
            MessageUtils.displayError("Transfer fail!");
        }
    }

    public Boolean cardOperatorSimulator() {
        return Math.random() < 0.5;
    }

    public void approveChargeBack(CustomerCase chargeBackCase) {
        try {

            CardTransaction originalTransaction = chargeBackCase.getChargebackTransaction();
            CardTransaction reverseTransaction = new CardTransaction();
            reverseTransaction.setAmount(originalTransaction.getAmount());
            if (originalTransaction.isIsCredit()) {
                reverseTransaction.setIsCredit(false);
            } else {
                reverseTransaction.setIsCredit(true);
            }
            reverseTransaction.setCreateDate(new Date());
            reverseTransaction.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
            reverseTransaction.setCreditCardAccount(originalTransaction.getCreditCardAccount());
            reverseTransaction.setTransactionCode(originalTransaction.getTransactionCode());
            reverseTransaction.setTransactionDescription("This is a reversed transaction. Original transaction is "
                    + originalTransaction.getTransactionDescription());

            Boolean result = cardTransactionSessionBean.createCardTransaction(reverseTransaction);
            if (result) {
                chargeBackCase.setCaseStatus(CaseStatus.RESOLVED);
                chargeBackCase.setReverseTransaction(reverseTransaction);

                customerCaseSessionBean.updateCase(chargeBackCase);

                emailServiceSessionBean.sendchargeBackGmailForSuccessfulCustomer(chargeBackCase.getMainAccount().getCustomer().getEmail(), chargeBackCase.getId());
                MessageUtils.displayInfo("The chargeback is approved");
            } else {
                MessageUtils.displayError("Error");
            }

        } catch (UpdateCaseException e) {
            System.out.println("UpdateCaseException thrown at StaffViewCustomerCaseManagedBean.java");
            MessageUtils.displayError("Error");
        }
    }

    public void cancelCase(CustomerCase chargeBackCase) {
        try {

            chargeBackCase.setCaseStatus(CaseStatus.CANCELLED);

            CustomerCase result = null;

            customerCaseSessionBean.updateCase(chargeBackCase);

            if (chargeBackCase.getIsChargeBackCase()) {
                emailServiceSessionBean.sendchargeBackGmailForRejectedCustomer(chargeBackCase.getMainAccount().getCustomer().getEmail(), chargeBackCase.getId());
            } else {
                emailServiceSessionBean.sendCaseStatusChangeToCustomer(chargeBackCase.getMainAccount().getCustomer().getEmail(), chargeBackCase);
            }

            MessageUtils.displayInfo("The case is cancelled");

        } catch (UpdateCaseException e) {
            System.out.println("UpdateCaseException thrown at StaffViewCustomerCaseManagedBean.java");
            MessageUtils.displayError("Error");
        }
    }

    public void resolveCase(CustomerCase chargeBackCase) {
        try {

            chargeBackCase.setCaseStatus(CaseStatus.RESOLVED);

            CustomerCase result = null;

            customerCaseSessionBean.updateCase(chargeBackCase);

            emailServiceSessionBean.sendCaseStatusChangeToCustomer(chargeBackCase.getMainAccount().getCustomer().getEmail(), chargeBackCase);
            MessageUtils.displayInfo("The case is resolved");

        } catch (UpdateCaseException e) {
            System.out.println("UpdateCaseException thrown at StaffViewCustomerCaseManagedBean.java");
            MessageUtils.displayError("Error");
        }
    }

    public void redirectToTransferPage(CustomerCase cc) {
        this.transferedCase = cc;
        RedirectUtils.redirect(ConstantUtils.STAFF_CMS_STAFF_TRANSFER_CASE);
    }

    public Boolean isChargeBack(String selectedField) {
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
