/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.sach;

import ejb.session.bean.SACHSessionBean;
import entity.BillTransfer;
import entity.PaymentTransfer;
import init.SachBankAccountBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "sachManagedBean")
@ViewScoped
public class SACHManagedBean implements Serializable {
    
    @EJB
    private SachBankAccountBuilder builderBean;

    private String referenceNumber;
    private BigDecimal amount;
    private String toBankCode = "001";
    private String fromBankCode = "005";
    private String accountNumber;
    private String toName;
    private String fromName;
    private String myInitial;

    private String ccNumber;
    private BigDecimal ccAmount;
    private String partnerBankCode = "001";
    private String fromCCBankCode = "005";
    private String organizationName = "Merlion Bank";

    private String referenceNumber1;
    private String referenceNumber2;

    private String billReferenceNumber;
    private String shortCode;
    private BigDecimal billAmount;

    @EJB
    private SACHSessionBean sachBean;

    public SACHManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        builderBean.init();
    }

    public void sendMBSNetSettlement() {
        // REMARK: MBS only transfer to SACH, dont care about other things

        System.out.println("----------------Settlement Processing----------------");
        BigDecimal netSettlementAmount = BigDecimal.ZERO;
        List<PaymentTransfer> paymentTransfers = sachBean.findAllPaymentTransferFromBankCode("001");
        for (PaymentTransfer pt : paymentTransfers) {
            netSettlementAmount = netSettlementAmount.add(pt.getAmount());
        }
        List<BillTransfer> billTransfers = sachBean.findAllBillTransferFromBankCode("001");
        for (BillTransfer bt : billTransfers) {
            netSettlementAmount = netSettlementAmount.add(bt.getAmount());
        }

        paymentTransfers = sachBean.findAllPaymentTransferToBankCode("001");
        for (PaymentTransfer pt : paymentTransfers) {
            netSettlementAmount = netSettlementAmount.subtract(pt.getAmount());
        }
        billTransfers = sachBean.findAllBillTransferToBankCode("001");
        for (BillTransfer bt : billTransfers) {
            netSettlementAmount = netSettlementAmount.subtract(bt.getAmount());
        }

        if (netSettlementAmount.compareTo(BigDecimal.ZERO) > 0) {
            sachBean.sendMBSNetSettlement(netSettlementAmount.toString());
        } else {
            System.out.println("No need to ask MBS for settlement, ask other banks to pay MBS");
        }
    }

    public void sendMEPSNetSettlement() {
        sachBean.sendMEPSNetSettlement();
    }

    public void sendMBSTransfer() {
        System.out.println("----------------Fund Transfer to MBS----------------");
        PaymentTransfer pt = new PaymentTransfer();
        pt.setReferenceNumber(getReferenceNumber());
        pt.setAmount(getAmount());
        pt.setToBankCode(getToBankCode());
        pt.setFromBankCode(getFromBankCode());
        pt.setAccountNumber(getAccountNumber());
        pt.setToName(getToName());
        pt.setFromName(getFromName());
        pt.setMyInitial(getMyInitial());
        pt.setSettled(false);

        sachBean.sendMBSPaymentTransfer(pt);
    }

    public void sendMBSCCPayment() {
        System.out.println("----------------Bill Transfer to MBS----------------");
        BillTransfer bt = new BillTransfer();
        bt.setReferenceNumber(referenceNumber2);
        bt.setBillReferenceNumber(ccNumber);
        bt.setAmount(ccAmount);
        bt.setPartnerBankCode(partnerBankCode);
        bt.setFromBankCode(fromCCBankCode);
        bt.setOrganizationName(organizationName);
        bt.setSettled(false);
        System.out.println(bt);
        sachBean.sendMBSCCPaymentSettlement(bt);
    }

    public void sendMBSGiroRequest() {
        System.out.println("----------------GIRO request----------------");
        BillTransfer bt = new BillTransfer();
        bt.setReferenceNumber(referenceNumber1);
        bt.setAmount(billAmount);
        bt.setBillReferenceNumber(billReferenceNumber);
        bt.setShortCode(shortCode);
        bt.setSettled(false);

        sachBean.sendMBSGiroRequest(bt);
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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
     * @return the toBankCode
     */
    public String getToBankCode() {
        return toBankCode;
    }

    /**
     * @param toBankCode the toBankCode to set
     */
    public void setToBankCode(String toBankCode) {
        this.toBankCode = toBankCode;
    }

    /**
     * @return the fromBankCode
     */
    public String getFromBankCode() {
        return fromBankCode;
    }

    /**
     * @param fromBankCode the fromBankCode to set
     */
    public void setFromBankCode(String fromBankCode) {
        this.fromBankCode = fromBankCode;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the toName
     */
    public String getToName() {
        return toName;
    }

    /**
     * @param toName the toName to set
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * @return the fromName
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * @param fromName the fromName to set
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     * @return the myInitial
     */
    public String getMyInitial() {
        return myInitial;
    }

    /**
     * @param myInitial the myInitial to set
     */
    public void setMyInitial(String myInitial) {
        this.myInitial = myInitial;
    }

    /**
     * @return the ccNumber
     */
    public String getCcNumber() {
        return ccNumber;
    }

    /**
     * @param ccNumber the ccNumber to set
     */
    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    /**
     * @return the ccAmount
     */
    public BigDecimal getCcAmount() {
        return ccAmount;
    }

    /**
     * @param ccAmount the ccAmount to set
     */
    public void setCcAmount(BigDecimal ccAmount) {
        this.ccAmount = ccAmount;
    }

    /**
     * @return the partnerBankCode
     */
    public String getPartnerBankCode() {
        return partnerBankCode;
    }

    /**
     * @param partnerBankCode the partnerBankCode to set
     */
    public void setPartnerBankCode(String partnerBankCode) {
        this.partnerBankCode = partnerBankCode;
    }

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the billReferenceNumber
     */
    public String getBillReferenceNumber() {
        return billReferenceNumber;
    }

    /**
     * @param billReferenceNumber the billReferenceNumber to set
     */
    public void setBillReferenceNumber(String billReferenceNumber) {
        this.billReferenceNumber = billReferenceNumber;
    }

    /**
     * @return the referenceNumber1
     */
    public String getReferenceNumber1() {
        return referenceNumber1;
    }

    /**
     * @param referenceNumber1 the referenceNumber1 to set
     */
    public void setReferenceNumber1(String referenceNumber1) {
        this.referenceNumber1 = referenceNumber1;
    }

    /**
     * @return the shortCode
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * @param shortCode the shortCode to set
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * @return the billAmount
     */
    public BigDecimal getBillAmount() {
        return billAmount;
    }

    /**
     * @param billAmount the billAmount to set
     */
    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    /**
     * @return the fromCCBankCode
     */
    public String getFromCCBankCode() {
        return fromCCBankCode;
    }

    /**
     * @param fromCCBankCode the fromCCBankCode to set
     */
    public void setFromCCBankCode(String fromCCBankCode) {
        this.fromCCBankCode = fromCCBankCode;
    }

    /**
     * @return the referenceNumber2
     */
    public String getReferenceNumber2() {
        return referenceNumber2;
    }

    /**
     * @param referenceNumber2 the referenceNumber2 to set
     */
    public void setReferenceNumber2(String referenceNumber2) {
        this.referenceNumber2 = referenceNumber2;
    }
}
