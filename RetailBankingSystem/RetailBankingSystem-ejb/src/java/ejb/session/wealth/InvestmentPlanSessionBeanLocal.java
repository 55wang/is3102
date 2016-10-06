/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.InvestmentPlan;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface InvestmentPlanSessionBeanLocal {
    public InvestmentPlan createInvestmentPlan(InvestmentPlan ip);
    public InvestmentPlan getInvestmentPlanById(Long id);
    public InvestmentPlan updateInvestmentPlan(InvestmentPlan ip);
}
