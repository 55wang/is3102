/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.VisaCardTransaction;
import entity.VisaSettlement;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface CardTransactionSessionBeanLocal {

    public void sendSuccessAuthorization(String transactionAmount, String creditCardNumber, String referenceNum, String requestBankCode);

    public List<VisaCardTransaction> getListVisaCardTransactions();

    public List<Long> getListVisaCardTransactionIdsByStatus(Boolean status);

    public Double calculateNetSettlement();

    public VisaCardTransaction updateVisaCardTransactionSettledStatusById(Long Id);

    public VisaSettlement persistSettlement(VisaSettlement vs);

    public void sendMEPSNetSettlement();

}
