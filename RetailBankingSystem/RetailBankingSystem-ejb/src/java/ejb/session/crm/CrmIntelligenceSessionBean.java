/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import ejb.session.bi.BizIntelligenceSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.customer.Customer;
import entity.dams.account.DepositAccount;
import entity.loan.LoanAccount;
import entity.wealth.InvestmentPlan;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils.CardAccountStatus;
import server.utilities.EnumUtils.InvestmentPlanStatus;
import server.utilities.EnumUtils.LoanAccountStatus;
import server.utilities.EnumUtils.StatusType;

/**
 *
 * @author wang
 */
@Stateless
public class CrmIntelligenceSessionBean implements CrmIntelligenceSessionBeanLocal {
    @EJB
    private BizIntelligenceSessionBeanLocal bizIntelligenceSessionBean;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long getDepositChurnCustomer(Date startDate, Date endDate){
        System.out.println("---------getDepositChurnCustomer---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT d FROM DepositAccount d WHERE d.mainAccount = m)");
        List<Customer> customers = q.getResultList();
        Long counter = 0L;
        System.out.println("Number of Customer Who has Deposit Account: " + customers.size());
        for(int i = 0; i < customers.size(); i++){
            List<DepositAccount> das = customers.get(i).getMainAccount().getBankAcounts();
            System.out.println("Customer " + i + " Deposit Account Number " + das.size());
            Boolean flag = true;
            for(int j = 0; j < das.size(); j++){
                if(!(das.get(j).getStatus() == StatusType.CLOSED && das.get(j).getCloseDate().after(startDate) && das.get(j).getCloseDate().before(endDate))){
                    flag = false;
                }
            }
            if(flag) counter++;
        }
        return counter;
    }
    
    @Override
    public Long getCardChurnCustomer(Date startDate, Date endDate){
        System.out.println("---------getCardChurnCustomer---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT cca FROM CreditCardAccount cca WHERE cca.mainAccount = m)");
        List<Customer> customers = q.getResultList();
        Long counter = 0L;
        System.out.println("Number of Customer Who has Credit Card Account: " + customers.size());
        for(int i = 0; i < customers.size(); i++){
            List<CreditCardAccount> ccas = customers.get(i).getMainAccount().getCreditCardAccounts();
            System.out.println("Customer " + i + " Credit Card Account Number " + ccas.size());
            Boolean flag = true;
            for(int j = 0; j < ccas.size(); j++){
                if(!(ccas.get(j).getCardStatus() == CardAccountStatus.CLOSED && ccas.get(j).getCloseDate().after(startDate) && ccas.get(j).getCloseDate().before(endDate))){
                    flag = false;
                }
            }
            if(flag) counter++;
        }
        return counter;
    }
    
    @Override
    public Long getLoanChurnCustomer(Date startDate, Date endDate){
        System.out.println("---------getLoanChurnCustomer---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT la FROM LoanAccount la WHERE la.mainAccount = m)");
        List<Customer> customers = q.getResultList();
        Long counter = 0L;
        System.out.println("Number of Customer Who has Loan Account: " + customers.size());
        for(int i = 0; i < customers.size(); i++){
            List<LoanAccount> las = customers.get(i).getMainAccount().getLoanAccounts();
            System.out.println("Customer " + i + " Loan Account Number " + las.size());
            Boolean flag = true;
            for(int j = 0; j < las.size(); j++){
                if(!(las.get(j).getLoanAccountStatus() == LoanAccountStatus.CLOSED && las.get(j).getCloseDate().after(startDate) && las.get(j).getCloseDate().before(endDate))){
                    flag = false;
                }
            }
            if(flag) counter++;
        }
        return counter;
    }
    
    @Override
    public Long getWealthChurnCustomer(Date startDate, Date endDate){
        System.out.println("---------getWealthChurnCustomer---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT ip FROM InvestmentPlan ip WHERE ip.wealthManagementSubscriber.mainAccount = m)");
        List<Customer> customers = q.getResultList();
        Long counter = 0L;
        System.out.println("Number of Customer Who has Investment Plan: " + customers.size());
        for(int i = 0; i < customers.size(); i++){
            List<InvestmentPlan> ips = customers.get(i).getMainAccount().getWealthManagementSubscriber().getInvestmentPlans();
            System.out.println("Customer " + i + " Investment Plan Number " + ips.size());
            Boolean flag = true;
            for(int j = 0; j < ips.size(); j++){
                if(!(ips.get(j).getStatus()== InvestmentPlanStatus.TERMINATED && ips.get(j).getSoldDate().after(startDate) && ips.get(j).getSoldDate().before(endDate))){
                    flag = false;
                }
            }
            if(flag) counter++;
        }
        return counter;
    }

    @Override
    public Double getCustomerAvgDepositSavingAmount(Date endDate) {
        System.out.println("---------getCustomerAvgDepositSavingAmount---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT d FROM DepositAccount d WHERE d.mainAccount = m AND d.creationDate <= :endDate)");
        q.setParameter("endDate", endDate);
        List<Customer> customers = q.getResultList();
        System.out.println("Number of Customer Who has Deposit Account: " + customers.size());
        Double totalDeposit = bizIntelligenceSessionBean.getBankTotalDepositAmount(endDate);
        System.out.println("Total Deposit Amount: " + totalDeposit);
        if(customers.isEmpty())
            return 0.0;
        else
            return totalDeposit/customers.size();
    }

    @Override
    public Double getCustomerAvgLoanAmount(Date endDate) {
        System.out.println("---------getCustomerAvgLoanAmount---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT la FROM LoanAccount la WHERE la.mainAccount = m AND la.creationDate <= :endDate)");
        q.setParameter("endDate", endDate);
        List<Customer> customers = q.getResultList();
        System.out.println("Number of Customer Who has Loan Account: " + customers.size());
        Double totalLoan = bizIntelligenceSessionBean.getBankTotalLoanAmount(endDate);
        System.out.println("Total Loan Amount: " + totalLoan);
        if(customers.isEmpty())
            return 0.0;
        else
            return totalLoan/customers.size();
    }

    @Override
    public Long getTotalCustomerAgeGroup(Integer startAge, Integer endAge) {
        Query q = em.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.age BETWEEN :inStartAge AND :inEndAge");
        q.setParameter("inStartAge", startAge);
        q.setParameter("inEndAge", endAge);
        return (Long) q.getSingleResult();
    }

    @Override
    public Long getNewCustomerAgeGroup(Integer startAge, Integer endAge, Date startDate, Date endDate) {
        Query q = em.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.age BETWEEN :inStartAge AND :inEndAge");
        q.setParameter("inStartAge", startAge);
        q.setParameter("inEndAge", endAge);
        return (Long) q.getSingleResult();
    }

}
