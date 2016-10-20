/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.bill;

import entity.dams.account.DepositAccount;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author leiyang
 */
@Entity
public class GiroArrangement extends BillingOrg {
    
    private Double billLimit;// for giro
    
    @OneToOne
    private DepositAccount depositAccount;

    /**
     * @return the billLimit
     */
    public Double getBillLimit() {
        return billLimit;
    }

    /**
     * @param billLimit the billLimit to set
     */
    public void setBillLimit(Double billLimit) {
        this.billLimit = billLimit;
    }

    /**
     * @return the depositAccount
     */
    public DepositAccount getDepositAccount() {
        return depositAccount;
    }

    /**
     * @param depositAccount the depositAccount to set
     */
    public void setDepositAccount(DepositAccount depositAccount) {
        this.depositAccount = depositAccount;
    }
}
