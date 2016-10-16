/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.common.TransactionRecord;
import entity.common.TransferRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.ConstantUtils;

/**
 *
 * @author leiyang
 */
@Stateless
public class TransactionSessionBean implements TransactionSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public TransferRecord createTransferRecord(TransferRecord tr) {
        em.persist(tr);
        return tr;
    }
    
    @Override
    public List<TransactionRecord> getTransactionRecordByAccountNumberStartDateEndDate(String accountNumber, Date sinceDate, Date toDate) {
        java.sql.Date startDate = new java.sql.Date(sinceDate.getTime());
        java.sql.Date endDate = new java.sql.Date(toDate.getTime());
        Query q = em.createQuery(
                "SELECT t FROM " + ConstantUtils.TRANSACTION_ENTITY + " t WHERE "
                + "t.fromAccount.accountNumber = :accountNumber AND "
                + "t.creationDate BETWEEN :startDate AND :endDate"
        );
        q.setParameter("accountNumber", accountNumber);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }
}
