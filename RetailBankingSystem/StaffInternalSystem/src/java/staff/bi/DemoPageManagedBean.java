/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bi;

import ejb.session.fact.BankFactTableSessionBeanLocal;
import entity.fact.bank.BankFactTable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Named(value = "demoPageManagedBean")
@ViewScoped
public class DemoPageManagedBean implements Serializable {

    @EJB
    private BankFactTableSessionBeanLocal bankFactTableSessionBean;

    public DemoPageManagedBean() {
    }

    public void insertBankFactTable() {
        BankFactTable bft = new BankFactTable();
        Calendar cal = Calendar.getInstance();
        Date predefined = cal.getTime();
        bft.setCreationDate(predefined);
        bft.setMonthOfDate(EnumUtils.Month.NOVEMBER);
        bft.setYearOfDate(cal.get(Calendar.YEAR));

        //hardcode, need to continue to retrieve live data
        bft.setTotalDepositAcct(100);
        bft.setTotalActiveDepositAcct(70);
        bft.setNewDepositAcct(20);
        bft.setTotalDepositAmount(new BigDecimal(10000));
        bft.setAvgDepositInterestRate(8.0);
        bft.setTotalDepositInterestAmount(800.0);

        bft.setTotalLoanAcct(50);
        bft.setTotalActiveLoanAcct(20);
        bft.setNewLoanAcct(5);
        bft.setTotalLoanAmount(100000.0);
        bft.setAvgLoanInterestRate(7.0);
        bft.setTotalLoanInterestAmount(7000.0);
        bft.setNumOfDefaultLoanAccount(3);

        bft.setTotalCardAcct(99);
        bft.setTotalActiveCardAcct(22);
        bft.setNewCardAcct(11);
        bft.setTotalCardAmount(1200000.0);
        bft.setTotalOutstandingAmount(3000000.0);
        bft.setNumOfBadCardAccount(33);

        bft.setTotalExecutedPortfolio(55);
        bft.setNewExecutedPortfolio(9);
        bft.setTotalPortfolioAmount(3000000000.0);
        bft.setTotalPortfolioProfitAmount(33333.3);
        bankFactTableSessionBean.createBankFactTable(bft);
    }

}
