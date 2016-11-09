/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.OTPSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.customer.TransferLimits;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import util.exception.common.MainAccountNotExistException;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "manageTransferLimitsManagedBean")
@ViewScoped
public class ManageTransferLimitsManagedBean implements Serializable {

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private OTPSessionBeanLocal otpBean;
    
    private TransferLimits transferLimits;
    private String newIntraBankLimit;
    private String newInterBankLimit;
    private String newOverseasBankLimit;
    
    private String inputTokenString;
    
    private MainAccount ma;
    
    private List<String> intraBankLimitOptions = CommonUtils.getEnumList(EnumUtils.IntraBankTransferLimit.class);
    private List<String> interBankLimitOptions = CommonUtils.getEnumList(EnumUtils.InterBankTransferLimit.class);
    private List<String> overseasBankLimitOptions = CommonUtils.getEnumList(EnumUtils.OverseasBankTransferLimit.class);
    
    /**
     * Creates a new instance of ManageTransferLimitsManagedBean
     */
    public ManageTransferLimitsManagedBean() {}
    
    @PostConstruct
    public void init() {
        System.out.println("manageTransferLimitsManagedBean @PostConstruct");
        try{
            ma = mainAccountSessionBean.getMainAccountByUserId(SessionUtils.getUserName());
        }catch(MainAccountNotExistException ex){
            System.out.println("init.MainAccountNotExistException");
        }
        transferLimits = ma.getTransferLimits();
        newIntraBankLimit = transferLimits.getDailyIntraBankLimit().toString();
        newInterBankLimit = transferLimits.getDailyInterBankLimit().toString();
        newOverseasBankLimit = transferLimits.getDailyOverseasBankLimit().toString();
        System.out.println("@PostConstruct finished init");
    }
    
    public void checkOptAndProceed() {
        if (inputTokenString == null || inputTokenString.isEmpty()) {
            MessageUtils.displayError("Please enter one time password!");
            return;
        }
        if (!otpBean.isOTPExpiredByPhoneNumber(inputTokenString, ma.getCustomer().getPhone())) {
            if (otpBean.checkOTPValidByPhoneNumber(inputTokenString, ma.getCustomer().getPhone())) {
                changeLimits();
            } else {
                MessageUtils.displayError("One Time Password Not Match!");
            }
        } else {
            MessageUtils.displayError("One Time Password Expired!");
        }
    }
    
    public void changeLimits() {
        System.out.println("changeLimits clicked");
        // check if otp valid
        transferLimits.setDailyIntraBankLimit(EnumUtils.IntraBankTransferLimit.getEnum(newIntraBankLimit));
        transferLimits.setDailyInterBankLimit(EnumUtils.InterBankTransferLimit.getEnum(newInterBankLimit));
        transferLimits.setDailyOverseasBankLimit(EnumUtils.OverseasBankTransferLimit.getEnum(newOverseasBankLimit));
        TransferLimits result = transferBean.updateTransferLimits(transferLimits);
        if (result != null ) {
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.UPDATE_TRANSFER_LIMIT_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.UPDATE_TRANSFER_LIMIT_FAIL);
        }
    }
    
    public void sendOpt() {
        System.out.println("sendOTP clicked, sending otp to: " + ma.getCustomer().getPhone());
        JSUtils.callJSMethod("PF('myWizard').next()");
        otpBean.generateOTP(ma.getCustomer().getPhone());
    }

    /**
     * @return the transferLimits
     */
    public TransferLimits getTransferLimits() {
        return transferLimits;
    }

    /**
     * @param transferLimits the transferLimits to set
     */
    public void setTransferLimits(TransferLimits transferLimits) {
        this.transferLimits = transferLimits;
    }

    /**
     * @return the newIntraBankLimit
     */
    public String getNewIntraBankLimit() {
        return newIntraBankLimit;
    }

    /**
     * @param newIntraBankLimit the newIntraBankLimit to set
     */
    public void setNewIntraBankLimit(String newIntraBankLimit) {
        this.newIntraBankLimit = newIntraBankLimit;
    }

    /**
     * @return the newInterBankLimit
     */
    public String getNewInterBankLimit() {
        return newInterBankLimit;
    }

    /**
     * @param newInterBankLimit the newInterBankLimit to set
     */
    public void setNewInterBankLimit(String newInterBankLimit) {
        this.newInterBankLimit = newInterBankLimit;
    }

    /**
     * @return the newOverseasBankLimit
     */
    public String getNewOverseasBankLimit() {
        return newOverseasBankLimit;
    }

    /**
     * @param newOverseasBankLimit the newOverseasBankLimit to set
     */
    public void setNewOverseasBankLimit(String newOverseasBankLimit) {
        this.newOverseasBankLimit = newOverseasBankLimit;
    }

    /**
     * @return the intraBankLimitOptions
     */
    public List<String> getIntraBankLimitOptions() {
        return intraBankLimitOptions;
    }

    /**
     * @param intraBankLimitOptions the intraBankLimitOptions to set
     */
    public void setIntraBankLimitOptions(List<String> intraBankLimitOptions) {
        this.intraBankLimitOptions = intraBankLimitOptions;
    }

    /**
     * @return the interBankLimitOptions
     */
    public List<String> getInterBankLimitOptions() {
        return interBankLimitOptions;
    }

    /**
     * @param interBankLimitOptions the interBankLimitOptions to set
     */
    public void setInterBankLimitOptions(List<String> interBankLimitOptions) {
        this.interBankLimitOptions = interBankLimitOptions;
    }

    /**
     * @return the overseasBankLimitOptions
     */
    public List<String> getOverseasBankLimitOptions() {
        return overseasBankLimitOptions;
    }

    /**
     * @param overseasBankLimitOptions the overseasBankLimitOptions to set
     */
    public void setOverseasBankLimitOptions(List<String> overseasBankLimitOptions) {
        this.overseasBankLimitOptions = overseasBankLimitOptions;
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
