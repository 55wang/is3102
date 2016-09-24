/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.dams;

import ejb.session.dams.CustomerDepositSessionBean;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
    
    public void viewTransaction(DepositAccount da) {
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("accountId", da.getId().toString());
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
}
