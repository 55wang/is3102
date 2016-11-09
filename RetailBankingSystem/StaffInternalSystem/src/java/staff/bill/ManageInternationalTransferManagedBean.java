/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bill;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.common.TransferRecord;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import util.exception.dams.UpdateDepositAccountException;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "manageInternationalTransferManagedBean")
@ViewScoped
public class ManageInternationalTransferManagedBean implements Serializable {

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private TransferSessionBeanLocal transferBean;

    private Date fromDate = DateUtils.getBeginOfDay();
    private Date toDate = DateUtils.getEndOfDay();
    private List<TransferRecord> transferRecords = new ArrayList<>();

    public ManageInternationalTransferManagedBean() {
    }

    @PostConstruct
    public void init() {
        transferRecords = transferBean.getAllTransactionRecordStartDateEndDateByType(fromDate, toDate, EnumUtils.PayeeType.OVERSEAS);
    }

    public void search() {
        fromDate = DateUtils.setTimeToBeginningOfDay(fromDate);
        toDate = DateUtils.setTimeToEndofDay(toDate);
        transferRecords = transferBean.getAllTransactionRecordStartDateEndDateByType(fromDate, toDate, EnumUtils.PayeeType.OVERSEAS);
    }

    public void freezeAccount(TransferRecord tr) {
        try {
            DepositAccount da = tr.getFromAccount();
            da.setStatus(EnumUtils.StatusType.FREEZE);
            depositBean.updateAccount(da);
            MessageUtils.displayInfo("Account Freezed");
        } catch (UpdateDepositAccountException e) {
            MessageUtils.displayInfo("Account Not Freezed..");
        }
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the transferRecords
     */
    public List<TransferRecord> getTransferRecords() {
        return transferRecords;
    }

    /**
     * @param transferRecords the transferRecords to set
     */
    public void setTransferRecords(List<TransferRecord> transferRecords) {
        this.transferRecords = transferRecords;
    }

}
