/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.common.BillTransferRecord;
import entity.common.TransferRecord;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author leiyang
 */
@Named(value = "transferDemoManagedBean")
@ViewScoped
public class TransferDemoManagedBean implements Serializable {
    
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;
    /**
     * Creates a new instance of TransferDemoManagedBean
     */
    public TransferDemoManagedBean() {
    }
    
    public void generateTransfer() {
        System.out.println("Generating IBG transfer");
        TransferRecord tr = new TransferRecord();
        tr.setAccountNumber("123456789");
        tr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        tr.setAmount(new BigDecimal(2000));
        tr.setToBankCode("002");
        tr.setToBranchCode("010");
        tr.setName("Wang Zhe");
        tr.setMyInitial("Ly");
        tr.setFromName("Lei Yang");
        webserviceBean.transferClearingSACH(tr);
    }
    
    public void generateBillTransfer() {
        System.out.println("Generating IBG transfer");
        BillTransferRecord btr = new BillTransferRecord();
        btr.setBillReferenceNumber("12345678");
        btr.setOrganizationName("Singtel");
        btr.setPartnerBankCode("002");
        btr.setSettled(false);
        btr.setShortCode("C123");
        btr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        webserviceBean.billingClearingSACH(btr);
    }
    
    public void fastTransfer() {
        System.out.println("Generating FAST transfer");
        TransferRecord tr = new TransferRecord();
        tr.setAccountNumber("123456789");
        tr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        tr.setAmount(new BigDecimal(2000));
        tr.setToBankCode("002");
        tr.setToBranchCode("010");
        tr.setName("Wang Zhe");
        tr.setMyInitial("Ly");
        tr.setFromName("Lei Yang");
        webserviceBean.transferClearingFAST(tr);
    }
    
}
