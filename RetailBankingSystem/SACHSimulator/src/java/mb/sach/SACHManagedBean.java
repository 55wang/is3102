/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.sach;

import ejb.session.bean.SACHSessionBean;
import entity.BillTransfer;
import entity.PaymentTransfer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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
    private SACHSessionBean sachBean;
    
    public SACHManagedBean() {
    }
    
    public void sendMBSNetSettlement() {
        BigDecimal netSettlementAmount = BigDecimal.ZERO;
        List<PaymentTransfer> paymentTransfers = sachBean.findAllPaymentTransfer();
        for (PaymentTransfer pt : paymentTransfers) {
            netSettlementAmount = netSettlementAmount.add(pt.getAmount());
        }
        List<BillTransfer> billTransfers = sachBean.findAllBillTransfer();
        for (BillTransfer bt : billTransfers) {
            netSettlementAmount = netSettlementAmount.add(bt.getAmount());
        }
        
        sachBean.sendMBSNetSettlement(netSettlementAmount.toString());
    }
}
