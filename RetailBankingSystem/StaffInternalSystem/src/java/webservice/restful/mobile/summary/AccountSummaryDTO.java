/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.summary;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leiyang
 */
public class AccountSummaryDTO {
    
    private List<AccountDTO> depositAccounts = new ArrayList<>();
    private List<AccountDTO> fixedDepositAccounts = new ArrayList<>();
    private List<AccountDTO> loanAccounts = new ArrayList<>();
    private AccountDTO mobileAccount;

    /**
     * @return the depositAccounts
     */
    public List<AccountDTO> getDepositAccounts() {
        return depositAccounts;
    }

    /**
     * @param depositAccounts the depositAccounts to set
     */
    public void setDepositAccounts(List<AccountDTO> depositAccounts) {
        this.depositAccounts = depositAccounts;
    }

    /**
     * @return the loanAccounts
     */
    public List<AccountDTO> getLoanAccounts() {
        return loanAccounts;
    }

    /**
     * @param loanAccounts the loanAccounts to set
     */
    public void setLoanAccounts(List<AccountDTO> loanAccounts) {
        this.loanAccounts = loanAccounts;
    }

    /**
     * @return the mobileAccount
     */
    public AccountDTO getMobileAccount() {
        return mobileAccount;
    }

    /**
     * @param mobileAccount the mobileAccount to set
     */
    public void setMobileAccount(AccountDTO mobileAccount) {
        this.mobileAccount = mobileAccount;
    }

    /**
     * @return the fixedDepositAccounts
     */
    public List<AccountDTO> getFixedDepositAccounts() {
        return fixedDepositAccounts;
    }

    /**
     * @param fixedDepositAccounts the fixedDepositAccounts to set
     */
    public void setFixedDepositAccounts(List<AccountDTO> fixedDepositAccounts) {
        this.fixedDepositAccounts = fixedDepositAccounts;
    }
}
