/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bill;

import ejb.session.bill.BillSessionBeanLocal;
import entity.bill.BankEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "managedBankManagedBean")
@ViewScoped
public class ManagedBankManagedBean implements Serializable {

    // ejbs
    @EJB
    private BillSessionBeanLocal billBean;
    
    // attributes
    private BankEntity bankEntity;
    private List<BankEntity> banks = new ArrayList<>();
    
    public ManagedBankManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        banks = billBean.getActiveListBankEntities();
        bankEntity = new BankEntity();
    }
    
    // public functions
    public void create() {
        bankEntity.setStatus(EnumUtils.StatusType.ACTIVE);
        BankEntity result = billBean.createBankEntity(bankEntity);
        if (result != null) {
            MessageUtils.displayInfo("New Bank Entity created!");
            banks.add(result);
            bankEntity = new BankEntity();
        } else {
            MessageUtils.displayError("Fail to create new bank entity");
        }
    }

    /**
     * @return the bankEntity
     */
    public BankEntity getBankEntity() {
        return bankEntity;
    }

    /**
     * @param bankEntity the bankEntity to set
     */
    public void setBankEntity(BankEntity bankEntity) {
        this.bankEntity = bankEntity;
    }

    /**
     * @return the banks
     */
    public List<BankEntity> getBanks() {
        return banks;
    }

    /**
     * @param banks the banks to set
     */
    public void setBanks(List<BankEntity> banks) {
        this.banks = banks;
    }
    
}
