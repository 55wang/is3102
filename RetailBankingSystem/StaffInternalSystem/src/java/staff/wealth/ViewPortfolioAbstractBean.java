/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.wealth;

import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 *
 * @author wang
 */
public abstract class ViewPortfolioAbstractBean implements Serializable {
    
    public BigDecimal getTotalDepositAmount(MainAccount main) {
        List<DepositAccount> das = main.getBankAcounts();
        BigDecimal totalBalance = new BigDecimal(0);
        for (DepositAccount da : das) {
            totalBalance = totalBalance.add(da.getBalance());
        }
        return totalBalance;
    }

    public Double getTotalPortfolioCurrentValue(MainAccount main) { //all the portfolio
        List<Portfolio> ps = main.getWealthManagementSubscriber().getPortfolios();
        Double totalCurrentPortfoliosValue = 0.0;
        for (Portfolio p : ps) {
            totalCurrentPortfoliosValue += p.getTotalCurrentValue();
        }
        return totalCurrentPortfoliosValue;
    }

    public Double getTotalDebtAmount(MainAccount main) {
        //List<LoanAccount> das = main.getLoanAccount();
        Double totalBalance = 1000.0;
        //to be continue with loan management system
        return totalBalance;
    }

    public BigDecimal getCurrentRatio(MainAccount main) {
        return getTotalDepositAmount(main).divide(new BigDecimal(getTotalDebtAmount(main)), 2, RoundingMode.HALF_UP);
    }

    public Double getTotalPortfolioBuyingValue(MainAccount main) {
        List<Portfolio> ports = main.getWealthManagementSubscriber().getPortfolios();
        Double totalValue = 0.0;
        for (Portfolio port : ports) {
            totalValue += port.getTotalBuyingValue();
        }
        return totalValue;
    }

    public BigDecimal getTotalAsset(MainAccount main) {
        return getTotalDepositAmount(main).add(new BigDecimal(getTotalDebtAmount(main)));
    }

    public BigDecimal getInvestToAssets(MainAccount main) {
        return new BigDecimal(getTotalPortfolioCurrentValue(main)).divide(getTotalAsset(main), 2, RoundingMode.HALF_UP);
    }

    public Double getPortfolioPercentageChange(MainAccount main) {
        return getTotalPortfolioCurrentValue(main)/getTotalPortfolioBuyingValue(main);
    }

}
