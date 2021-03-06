/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.bill;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.bill.BillingOrg;
import entity.bill.Organization;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import util.exception.common.MainAccountNotExistException;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "manageBillOrgsManagedBean")
@ViewScoped
public class ManageBillOrgsManagedBean implements Serializable {
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private BillSessionBeanLocal billBean;
    @EJB
    private OTPSessionBeanLocal otpBean;
    
    private String selectedBillId;
    private String referenceNumber;
    private BillingOrg billingOrg = new BillingOrg();
    private MainAccount ma;
    private List<Organization> billOrgsOptions;
    private List<BillingOrg> addedBillOrgs;
    
    private String inputTokenString;
    
    public ManageBillOrgsManagedBean() {}
    
    @PostConstruct
    public void init() {
        setBillOrgsOptions(billBean.getActiveListOrganization());
        try{
            ma = mainAccountSessionBean.getMainAccountByUserId(SessionUtils.getUserName());
        }catch(MainAccountNotExistException ex){
            System.out.println("init.MainAccountNotExistException");
        }
        addedBillOrgs = billBean.getBillingOrgMainAccountId(ma.getId());
    }
    
    public void addBillOrg() {
        
        if (!checkOptAndProceed()) {
            return;
        }
        
        Organization o = billBean.getOrganizationById(Long.parseLong(selectedBillId));
        System.out.println(o);
        billingOrg.setOrganization(o);
        billingOrg.setBillReference(referenceNumber);
        billingOrg.setMainAccount(ma);
        BillingOrg result = billBean.createBillingOrganization(billingOrg);
        if (result != null) {
            JSUtils.callJSMethod("PF('myWizard').next()");
            addedBillOrgs.add(result);
            MessageUtils.displayInfo(ConstantUtils.BILL_ORG_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.BILL_ORG_FAILED);
        }
    }
    
    public String getOrgName(String selectedBillId) {
        Organization o = billBean.getOrganizationById(Long.parseLong(selectedBillId));
        return o.getName();
    }
    
    public void removeBillOrg(BillingOrg bo) {
        String result = billBean.deleteBillingOrganizationById(bo.getId());
        if (result.equals("SUCCESS")) {
            getAddedBillOrgs().remove(bo);
            MessageUtils.displayInfo(ConstantUtils.BILL_ORG_DELETE_SUCCESS);
        } else {
            MessageUtils.displayError(ConstantUtils.BILL_ORG_DELETE_FAILED);
        }
    }
    
    public void sendOpt() {
        System.out.println("sendOTP clicked, sending otp to: " + ma.getCustomer().getPhone());
        JSUtils.callJSMethod("PF('myWizard').next()");
        otpBean.generateOTP(ma.getCustomer().getPhone());
    }
    
    private Boolean checkOptAndProceed() {
        if (inputTokenString == null || inputTokenString.isEmpty()) {
            MessageUtils.displayError("Please enter one time password!");
            return false;
        }
        if (!otpBean.isOTPExpiredByPhoneNumber(inputTokenString, ma.getCustomer().getPhone())) {
            if (otpBean.checkOTPValidByPhoneNumber(inputTokenString, ma.getCustomer().getPhone())) {
                return true;
            } else {
                MessageUtils.displayError("One Time Password Not Match!");
                return false;
            }
        } else {
            MessageUtils.displayError("One Time Password Expired!");
            return false;
        }
    }

    /**
     * @return the selectedBillId
     */
    public String getSelectedBillId() {
        return selectedBillId;
    }

    /**
     * @param selectedBillId the selectedBillId to set
     */
    public void setSelectedBillId(String selectedBillId) {
        this.selectedBillId = selectedBillId;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the billingOrg
     */
    public BillingOrg getBillingOrg() {
        return billingOrg;
    }

    /**
     * @param billingOrg the billingOrg to set
     */
    public void setBillingOrg(BillingOrg billingOrg) {
        this.billingOrg = billingOrg;
    }

    /**
     * @return the billOrgsOptions
     */
    public List<Organization> getBillOrgsOptions() {
        return billOrgsOptions;
    }

    /**
     * @param billOrgsOptions the billOrgsOptions to set
     */
    public void setBillOrgsOptions(List<Organization> billOrgsOptions) {
        this.billOrgsOptions = billOrgsOptions;
    }

    /**
     * @return the addedBillOrgs
     */
    public List<BillingOrg> getAddedBillOrgs() {
        return addedBillOrgs;
    }

    /**
     * @param addedBillOrgs the addedBillOrgs to set
     */
    public void setAddedBillOrgs(List<BillingOrg> addedBillOrgs) {
        this.addedBillOrgs = addedBillOrgs;
    }
    
    /**
     * @return the inputTokenString
     */
    public String getInputTokenString() {
        return inputTokenString;
    }

    /**
     * @param inputTokenString the inputTokenString to set
     */
    public void setInputTokenString(String inputTokenString) {
        this.inputTokenString = inputTokenString;
    }
}
