/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.bill.TransactionSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.BankEntity;
import entity.bill.Payee;
import entity.common.TransferRecord;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "internationalBankTransferManagedBean")
@ViewScoped
public class InternationalBankTransferManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private TransactionSessionBeanLocal transactionBean;
    @EJB
    private BillSessionBeanLocal billBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;
    
    private String payeeId;
    private String fromAccountNo;
    private String purpose;
    private BigDecimal amount;
    private Payee payee = new Payee();
    private MainAccount ma;
    private List<Payee> payees = new ArrayList<>();
    private List<DepositAccount> accounts = new ArrayList<>();
    private List<String> purposeOptions = CommonUtils.getEnumList(EnumUtils.TransferPurpose.class);
    
    public InternationalBankTransferManagedBean() {
    }
    
    
    @PostConstruct
    public void init() {
        setMa(loginBean.getMainAccountByUserID(SessionUtils.getUserName()));
        setAccounts(getMa().getBankAcounts());
        setPayees(transferBean.getPayeeFromUserIdWithType(getMa().getId(), EnumUtils.PayeeType.OVERSEAS));
        setPayeeId("New Receipiant");
    }
    
    public void changePayee() {
        if (getPayeeId().equals("New Receipiant")) {
            setPayee(new Payee());
        } else {
            setPayee(transferBean.getPayeeById(Long.parseLong(getPayeeId())));
        }
    }
    
    public void transfer() {
        if (getPayeeId().equals("New Receipiant")) {
            getPayee().setMainAccount(getMa());
            getPayee().setType(EnumUtils.PayeeType.OVERSEAS);
            getPayee().setFromName(getMa().getCustomer().getFullName());
            Payee result = transferBean.createPayee(getPayee());
            if (result != null) {
                transferClearing();
                JSUtils.callJSMethod("PF('myWizard').next()");
                MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
            } else {
                JSUtils.callJSMethod("PF('myWizard').back()");
                MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
            }
        } else {
            transferClearing();
            JSUtils.callJSMethod("PF('myWizard').next()");
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        }
    }
    
    public String getBankName(String bankCode) {
        System.out.println(bankCode);
        if (bankCode == null || bankCode.equals("")) {
            return "No bank selected";
        }
        BankEntity b = billBean.getBankEntityByCode(bankCode);
        return b.getName();
    }
    
    private void transferClearing() {
        DepositAccount da = depositBean.getAccountFromId(getFromAccountNo());
        System.out.println("SWIFT transfer messaging");
        TransferRecord tr = new TransferRecord();
        tr.setAccountNumber(getPayee().getAccountNumber());
        tr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        tr.setAmount(getAmount());
        tr.setSwiftCode(payee.getSwiftCode());
        tr.setToBankCode(getPayee().getBankCode());
        tr.setToBranchCode("010");// dummy
        tr.setToBankAddress(getPayee().getBankAddress());
        tr.setName(getPayee().getName());
        tr.setMyInitial(getPayee().getMyInitial());
        tr.setFromName(getPayee().getFromName());
        tr.setPurpose(EnumUtils.TransferPurpose.getEnum(getPurpose()));
        tr.setFromAccount(da);
        webserviceBean.transferSWIFT(tr);
        da.removeBalance(getAmount());
        depositBean.updateAccount(da);
        transactionBean.createTransferRecord(tr);
    }

    /**
     * @return the payeeId
     */
    public String getPayeeId() {
        return payeeId;
    }

    /**
     * @param payeeId the payeeId to set
     */
    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    /**
     * @return the fromAccountNo
     */
    public String getFromAccountNo() {
        return fromAccountNo;
    }

    /**
     * @param fromAccountNo the fromAccountNo to set
     */
    public void setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

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
     * @return the ma
     */
    public MainAccount getMa() {
        return ma;
    }

    /**
     * @param ma the ma to set
     */
    public void setMa(MainAccount ma) {
        this.ma = ma;
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
     * @return the purposeOptions
     */
    public List<String> getPurposeOptions() {
        return purposeOptions;
    }

    /**
     * @param purposeOptions the purposeOptions to set
     */
    public void setPurposeOptions(List<String> purposeOptions) {
        this.purposeOptions = purposeOptions;
    }
}
