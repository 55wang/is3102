/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.fact;

import entity.customer.Customer;
import entity.fact.customer.FinancialInstrumentFactTable;
import entity.fact.customer.SinglePortfolioFactTable;
import entity.wealth.Portfolio;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.Query;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Local
public interface PortfolioFactSessionBeanLocal {

    public FinancialInstrumentFactTable createFinancialInstrumentFactTable(FinancialInstrumentFactTable fif);
    public List<SinglePortfolioFactTable> getListPortfoliosFtByCustomerIdPortfolioId(String custId, Long portId);
    public SinglePortfolioFactTable getLatestPortfolioFtByCustomerIdPortfolioId(String custId, Long portId);
    public SinglePortfolioFactTable createSinglePortfolioFactTable(SinglePortfolioFactTable spf);
    public List<SinglePortfolioFactTable> getListPortfoliosFtByCustomerId(String custId);
    public SinglePortfolioFactTable updateSinglePortfolioFactTable(SinglePortfolioFactTable spf);
    public SinglePortfolioFactTable getSinglePortfolioFactTable(Date date, String custId, Long portId);
    public List<FinancialInstrumentFactTable> getListFinancialInstrumentFactTableByETFName(String etf);
    public List<Date> getDistinctDateFromFIFactTable();
    public Double getFinancialInstrumentValueChangeByCreationDateAndName(Date date, EnumUtils.FinancialInstrumentClass name);
}
