/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.InvestmentPlan;
import entity.wealth.Portfolio;
import java.util.List;
import javax.ejb.Local;
import server.utilities.EnumUtils;



@Local
public interface PortfolioSessionBeanLocal {
    public List<Portfolio> getListPortfolios();
    public Portfolio getPortfolioById(Long Id);
    public Portfolio createPortfolio(Portfolio p);
    public Portfolio updatePortfolio(Portfolio p);
}
