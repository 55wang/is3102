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
import java.util.ArrayList;
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
        System.out.println("");
        System.out.println("---------getDepositChurnCustomer---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT d FROM DepositAccount d WHERE d.mainAccount = m AND d.status =:closedStatus)");
        q.setParameter("closedStatus", StatusType.CLOSED);
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
        System.out.println("");
        System.out.println("---------getCardChurnCustomer---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT cca FROM CreditCardAccount cca WHERE cca.mainAccount = m AND cca.CardStatus =:closedStatus)");
        q.setParameter("closedStatus", CardAccountStatus.CLOSED);
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
        System.out.println("");
        System.out.println("---------getLoanChurnCustomer---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT la FROM LoanAccount la WHERE la.mainAccount = m AND la.loanAccountStatus =:closedStatus)");
        q.setParameter("closedStatus", LoanAccountStatus.CLOSED);
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
        System.out.println("");
        System.out.println("---------getWealthChurnCustomer---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.mainAccount = m AND EXISTS (SELECT ip FROM InvestmentPlan ip WHERE ip.wealthManagementSubscriber.mainAccount = m AND ip.status =:terminatedStatus)");
        q.setParameter("terminatedStatus", InvestmentPlanStatus.TERMINATED);
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
        System.out.println("");
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
        System.out.println("");
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
        System.out.println("");
        System.out.println("---------getNewCustomerAgeGroup"+" age group("+startAge + " and "+ endAge + ")---------");
        Query q = em.createQuery("SELECT c FROM Customer c, MainAccount m WHERE c.age BETWEEN :inStartAge AND :inEndAge AND c.mainAccount = m AND (EXISTS (SELECT la FROM LoanAccount la WHERE la.mainAccount = m AND la.creationDate BETWEEN :startDate AND :endDate) OR EXISTS (SELECT da FROM DepositAccount da WHERE da.mainAccount = m AND da.creationDate BETWEEN :startDate AND :endDate) OR EXISTS (SELECT cca FROM CreditCardAccount cca WHERE cca.mainAccount = m AND cca.creationDate BETWEEN :startDate AND :endDate) OR EXISTS (SELECT ip FROM InvestmentPlan ip WHERE ip.wealthManagementSubscriber.mainAccount = m AND ip.creationDate BETWEEN :startDate AND :endDate))");
        q.setParameter("inStartAge", startAge);
        q.setParameter("inEndAge", endAge);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        List<Customer> customers = q.getResultList();
        System.out.println("Number of Customer Who Has Subscriber at least 1 Service: " + customers.size());
        Long counter = 0L;
        for(int i=0; i < customers.size();i++){
            System.out.println("");
            System.out.println("**Customer #" + i+"**");
            List<DepositAccount> das = customers.get(i).getMainAccount().getBankAcounts();
            System.out.println("DepositAccount number: " + das.size());
            List<LoanAccount> las = customers.get(i).getMainAccount().getLoanAccounts();
            System.out.println("LoanAccount number: " + las.size());
            List<CreditCardAccount> ccas = customers.get(i).getMainAccount().getCreditCardAccounts();
            List<InvestmentPlan> ips = new ArrayList<InvestmentPlan>();
            try{
                System.out.println("CreditCardAccount number: " + ccas.size());
                ips = customers.get(i).getMainAccount().getWealthManagementSubscriber().getInvestmentPlans();
                System.out.println("InvestmentPlan number: " + ips.size());
            }catch(Exception ex){
                System.out.println("InvestmentPlan number: 0");
            }
            
            if(das.size()==1 && las.isEmpty() && ccas.isEmpty() && ips.isEmpty())
                counter++;
            else if(das.isEmpty() && las.size()==1 && ccas.isEmpty() && ips.isEmpty())
                counter++;
            else if(das.isEmpty() && las.isEmpty() && ccas.size()==1 && ips.isEmpty())
                counter++;
            else if(das.isEmpty() && las.isEmpty() && ccas.isEmpty() && ips.size()==1)
                counter++;
        }
        return (Long) counter;
    }

}
