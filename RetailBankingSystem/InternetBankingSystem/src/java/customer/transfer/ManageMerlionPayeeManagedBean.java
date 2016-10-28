/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import entity.bill.Payee;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "manageMerlionPayeeManagedBean")
@ViewScoped
public class ManageMerlionPayeeManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private OTPSessionBeanLocal otpBean;
    
    private Payee payee;
    private MainAccount ma;
    private List<Payee> payees = new ArrayList<>();
    
    private String inputTokenString;
    
    /**
     * Creates a new instance of ManageMerlionPayeeManagedBean
     */
    public ManageMerlionPayeeManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        setPayees(ma.getPayees());
        setPayee(new Payee());
        getPayee().setFromName(ma.getCustomer().getFullName());
        payee.setMainAccount(ma);
        getPayee().setType(EnumUtils.PayeeType.MERLION);
    }
    
    public void addPayee() {
        
        if (!checkOptAndProceed()) {
            return;
        }
        
        try {
            DepositAccount da = depositBean.getAccountFromId(getPayee().getAccountNumber());
            if (da == null) {
                MessageUtils.displayError(ConstantUtils.TRANSFER_ACCOUNT_NOT_FOUND);
                return;
            }
        } catch (Exception e) {
            MessageUtils.displayError(ConstantUtils.TRANSFER_ACCOUNT_NOT_FOUND);
            return;
        }
        //TODO: need another authentication
        Payee result = transferBean.createPayee(getPayee());
        if (result != null) {
            getPayees().add(result);
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.PAYEE_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.PAYEE_FAILED);
        }
    }
    
    public void removePayee(Payee p) {
        String result = transferBean.deletePayeeById(p.getId());
        if (result.equals("SUCCESS")) {
            payees.remove(p);
            MessageUtils.displayInfo(ConstantUtils.PAYEE_DELETE_SUCCESS);
        } else {
            MessageUtils.displayError(ConstantUtils.PAYEE_DELETE_FAILED);
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

    // Getters and Setters
    /**
     * @return the payee
     */
    public Payee getPayee() {
        return payee;
    }

    /**
     * @param payee the payee to set
     */
    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    /**
     * @return the payees
     */
    public List<Payee> getPayees() {
        return payees;
    }

    /**
     * @param payees the payees to set
     */
    public void setPayees(List<Payee> payees) {
        this.payees = payees;
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
