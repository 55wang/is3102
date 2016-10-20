/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.webservice;

import entity.common.BillTransferRecord;
import entity.common.TransferRecord;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface WebserviceSessionBeanLocal {
    public void paySACHSettlement(String netSettlementAmount, String fromBankCode, String toBankCode, String agencyCode);
    public void payFASTSettlement(String netSettlementAmount, String fromBankCode, String toBankCode, String agencyCode, String referenceNumber);
    public void transferClearingSACH(TransferRecord tr);
    public void billingClearingSACH(BillTransferRecord btr);
    public void transferClearingFAST(TransferRecord tr);
    public void transferSWIFT(TransferRecord tr);
}
