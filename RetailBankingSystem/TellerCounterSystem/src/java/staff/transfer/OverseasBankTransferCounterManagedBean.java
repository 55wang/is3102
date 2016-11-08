/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.counter.TellerCounterSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.BankEntity;
import entity.bill.Payee;
import entity.common.TransferRecord;
import entity.counter.TellerCounter;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
@Named(value = "overseasBankTransferCounterManagedBean")
@ViewScoped
public class OverseasBankTransferCounterManagedBean implements Serializable {

    @EJB
    private TellerCounterSessionBeanLocal counterBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;
    @EJB
    private BillSessionBeanLocal billBean;
    
    private String fromAccountNumber;
    private String toAccountNumber;
    private String customerIC;
    private String payeeId;
    private Payee payee = new Payee();
    private String purpose;
    private BigDecimal amount;
    private MainAccount mainAccount;
    private List<DepositAccount> depositAccounts = new ArrayList<>();
    private List<Payee> payees = new ArrayList<>();
    private List<String> purposeOptions = CommonUtils.getEnumList(EnumUtils.TransferPurpose.class);

    
    public OverseasBankTransferCounterManagedBean() {
    }
    
    public void retrieveMainAccount() {
        
        try {
            mainAccount = loginSessionBean.getMainAccountByUserIC(getCustomerIC());
            setPayees(transferBean.getPayeeFromUserIdWithType(mainAccount.getId(), EnumUtils.PayeeType.OVERSEAS));
            depositAccounts = mainAccount.getBankAcounts();
            payeeId = "New Receipiant";
        } catch (Exception e) {
            mainAccount = null;
            MessageUtils.displayError("Customer Main Account Not Found!");
        }
        
    }
    
    public void changePayee() {
        if (getPayeeId().equals("New Receipiant")) {
            setPayee(new Payee());
        } else {
            setPayee(transferBean.getPayeeById(Long.parseLong(payeeId)));
        }
    }
    
    public void transfer() {
        
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAccountNumber);
        if (fromAccount != null && fromAccount.getBalance().compareTo(amount) < 0) {
            MessageUtils.displayError(ConstantUtils.NOT_ENOUGH_BALANCE);
            return;
        }
        
        if (getPayeeId().equals("New Receipiant")) {
            getPayee().setType(EnumUtils.PayeeType.OVERSEAS);
            getPayee().setFromName(mainAccount.getCustomer().getFullName());
            payee.setMainAccount(mainAccount);
            Payee result = transferBean.createPayee(getPayee());
            if (result != null) {
                transferClearing();
                MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
            } else {
                MessageUtils.displayError(ConstantUtils.TRANSFER_FAILED);
            }
        } else {
            payee = transferBean.getPayeeById(Long.parseLong(payeeId));
            transferClearing();
            MessageUtils.displayInfo(ConstantUtils.TRANSFER_SUCCESS);
        }
    }
    
    private void transferClearing() {
        DepositAccount da = depositBean.getAccountFromId(fromAccountNumber);
        System.out.println("----------------SWIFT transfer messaging----------------");
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
        tr.setType(EnumUtils.PayeeType.OVERSEAS);
        tr.setActionType(EnumUtils.TransactionType.TRANSFER);
        webserviceBean.transferSWIFT(tr);
        da.removeBalance(getAmount());
        depositBean.updateAccount(da);
    }
    

    /**
     * @return the fromAccountNumber
     */
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    /**
     * @param fromAccountNumber the fromAccountNumber to set
     */
    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    /**
     * @return the toAccountNumber
     */
    public String getToAccountNumber() {
        return toAccountNumber;
    }

    /**
     * @param toAccountNumber the toAccountNumber to set
     */
    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    /**
     * @return the customerIC
     */
    public String getCustomerIC() {
        return customerIC;
    }

    /**
     * @param customerIC the customerIC to set
     */
    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
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
     * @return the mainAccount
     */
    public MainAccount getMainAccount() {
        return mainAccount;
    }

    /**
     * @param mainAccount the mainAccount to set
     */
    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    /**
     * @return the depositAccounts
     */
    public List<DepositAccount> getDepositAccounts() {
        return depositAccounts;
    }

    /**
     * @param depositAccounts the depositAccounts to set
     */
    public void setDepositAccounts(List<DepositAccount> depositAccounts) {
        this.depositAccounts = depositAccounts;
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
