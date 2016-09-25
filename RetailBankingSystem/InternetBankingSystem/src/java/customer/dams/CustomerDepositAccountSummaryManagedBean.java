/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.dams;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.StatusType;
import utils.JSUtils;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "customerDepositAccountSummaryManagedBean")
@ViewScoped
public class CustomerDepositAccountSummaryManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    private final StatusType ACCOUNT_ACTIVE = StatusType.ACTIVE;
    
    private List<DepositAccount> accounts = new ArrayList<>();
    /**
     * Creates a new instance of CustomerDepositAccountSummaryManagedBean
     */
    public CustomerDepositAccountSummaryManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        setAccounts(depositBean.getAllCustomerAccounts(Long.parseLong(SessionUtils.getUserId())));
    }
    
    public void confirm() {
        JSUtils.callJSMethod("PF('close_account').show()");
    }
    
    public void closeAccount(DepositAccount da) {
        System.out.println("Closing account");
        if (da.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            da.setStatus(EnumUtils.StatusType.FREEZE);
            DepositAccount result = depositBean.updateAccount(da);
            if (result != null) {
                MessageUtils.displayInfo("Your account has been closed!");
            } else {
                MessageUtils.displayInfo("Your account close failed! Please contact our staffs!");
            }
        } else {
            MessageUtils.displayError("Make sure your account balance is zero before closure!");
        }
    }
    
    public void viewTransaction(DepositAccount da) {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("accountId", da.getAccountNumber());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("deposit_account_transaction.xhtml" + params);
    }

    /**
     * @return the accounts
     */
    public List<DepositAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<DepositAccount> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return the ACCOUNT_ACTIVE
     */
    public StatusType getACCOUNT_ACTIVE() {
        return ACCOUNT_ACTIVE;
    }
    
}

