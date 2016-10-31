/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.FinancialInstrument;
import entity.wealth.FinancialInstrumentAndWeight;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import server.utilities.EnumUtils.FinancialInstrumentClass;

/**
 *
 * @author VIN-S
 */
@Local
public interface FinancialInstrumentSessionBeanLocal {
    public FinancialInstrument createFinancialInstrument(FinancialInstrument fi);
    public FinancialInstrument getFinancialInstrumentByName(FinancialInstrumentClass name);
    public FinancialInstrument updateFinancialInstrument(FinancialInstrument fi);
    public List<FinancialInstrument> getAllFinancialInstruments();
    public FinancialInstrumentAndWeight updateFinancialInstrumentAndWeight(FinancialInstrumentAndWeight fiw);
}
