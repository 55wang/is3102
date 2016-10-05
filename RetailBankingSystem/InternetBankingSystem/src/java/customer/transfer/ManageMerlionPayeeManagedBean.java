/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.transfer.TransferSessionBeanLocal;
import entity.bill.Payee;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
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
    
    private Payee payee;
    
    /**
     * Creates a new instance of ManageMerlionPayeeManagedBean
     */
    public ManageMerlionPayeeManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        MainAccount ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        setPayee(new Payee());
        getPayee().setMainAccount(ma);
        getPayee().setFromName(ma.getCustomer().getFullName());
    }
    
    public void transfer() {
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
            init();
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.PAYEE_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.PAYEE_FAILED);
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
}
