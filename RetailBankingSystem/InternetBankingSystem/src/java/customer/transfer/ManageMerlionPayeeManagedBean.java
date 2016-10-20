/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
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
    
    private Payee payee;
    private List<Payee> payees = new ArrayList<>();
    
    /**
     * Creates a new instance of ManageMerlionPayeeManagedBean
     */
    public ManageMerlionPayeeManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        MainAccount ma = loginBean.getMainAccountByUserID(SessionUtils.getUserName());
        setPayees(ma.getPayees());
        setPayee(new Payee());
        getPayee().setMainAccount(ma);
        getPayee().setFromName(ma.getCustomer().getFullName());
        getPayee().setType(EnumUtils.PayeeType.MERLION);
    }
    
    public void addPayee() {
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
}
