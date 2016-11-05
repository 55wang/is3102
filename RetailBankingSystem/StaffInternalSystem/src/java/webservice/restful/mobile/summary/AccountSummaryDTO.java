/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.summary;

import webservice.restful.mobile.transfer.PayeeDTO;
import java.util.ArrayList;
import java.util.List;
import webservice.restful.mobile.card.CardDTO;
import webservice.restful.mobile.transfer.BillOrgDTO;

/**
 *
 * @author leiyang
 */
public class AccountSummaryDTO {
    
    private List<AccountDTO> depositAccounts = new ArrayList<>();
    private List<AccountDTO> fixedDepositAccounts = new ArrayList<>();
    private List<AccountDTO> loanAccounts = new ArrayList<>();
    private List<PayeeDTO> merlionPayee = new ArrayList<>();
    private List<PayeeDTO> otherBankPayee = new ArrayList<>();
    private List<CardDTO> merlionCCBill = new ArrayList<>();
    private List<BillOrgDTO> otherCCBill = new ArrayList<>();
    private List<BillOrgDTO> otherBill = new ArrayList<>();
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

    /**
     * @return the merlionPayee
     */
    public List<PayeeDTO> getMerlionPayee() {
        return merlionPayee;
    }

    /**
     * @param merlionPayee the merlionPayee to set
     */
    public void setMerlionPayee(List<PayeeDTO> merlionPayee) {
        this.merlionPayee = merlionPayee;
    }

    /**
     * @return the otherBankPayee
     */
    public List<PayeeDTO> getOtherBankPayee() {
        return otherBankPayee;
    }

    /**
     * @param otherBankPayee the otherBankPayee to set
     */
    public void setOtherBankPayee(List<PayeeDTO> otherBankPayee) {
        this.otherBankPayee = otherBankPayee;
    }

    /**
     * @return the merlionCCBill
     */
    public List<CardDTO> getMerlionCCBill() {
        return merlionCCBill;
    }

    /**
     * @param merlionCCBill the merlionCCBill to set
     */
    public void setMerlionCCBill(List<CardDTO> merlionCCBill) {
        this.merlionCCBill = merlionCCBill;
    }

    /**
     * @return the otherCCBill
     */
    public List<BillOrgDTO> getOtherCCBill() {
        return otherCCBill;
    }

    /**
     * @param otherCCBill the otherCCBill to set
     */
    public void setOtherCCBill(List<BillOrgDTO> otherCCBill) {
        this.otherCCBill = otherCCBill;
    }

    /**
     * @return the otherBill
     */
    public List<BillOrgDTO> getOtherBill() {
        return otherBill;
    }

    /**
     * @param otherBill the otherBill to set
     */
    public void setOtherBill(List<BillOrgDTO> otherBill) {
        this.otherBill = otherBill;
    }
}
