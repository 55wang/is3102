/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.fact;

import entity.fact.bank.BankFactTable;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface BankFactTableSessionBeanLocal {

    public List<BankFactTable> getListBankFactTables();
    public BankFactTable createBankFactTable(BankFactTable r);
    public BankFactTable updateBankFactTable(BankFactTable r);
    public BankFactTable getBankFactTableById(Long Id);
    public BankFactTable getBankFactTableByCreationDate(Date date);

}
