/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import entity.wealth.PortfolioModel;
import entity.wealth.InvestmentPlan;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface InvestmentPlanSessionBeanLocal {
    public List<PortfolioModel> getTestR();
    public InvestmentPlan createInvestmentPlan(InvestmentPlan ip);
    public InvestmentPlan getInvestmentPlanById(Long id);
    public InvestmentPlan updateInvestmentPlan(InvestmentPlan ip);
    public List<InvestmentPlan> getInvestmentPlanByRM(StaffAccount sa);
    public List<InvestmentPlan> getInvestmentPlanByMainAccount(MainAccount ma);
}
