/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.fact;

import entity.fact.SinglePortfolioFactTable;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Local
public interface FactSessionBeanLocal {

    public List<SinglePortfolioFactTable> getListPortfoliosFtByCustomerIdPortfolioId(Long custId, Long portId);
    public SinglePortfolioFactTable getLatestPortfolioFtByCustomerIdPortfolioId(Long custId, Long portId);

}
