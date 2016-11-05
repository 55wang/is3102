/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.staff.StaffAccount;
import entity.wealth.FinancialInstrument;
import entity.wealth.Portfolio;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sound.sampled.Port;
import server.utilities.ConstantUtils;

/**
 *
 * @author leiyang
 */
@Singleton
@LocalBean
@Startup
public class EntityBuilderBean {

    // builders
    @EJB
    private EntityCustomerBuilder entityCustomerBuilder;
    @EJB
    private EntityCreditCardOrderBuilder entityCreditCardOrderBuilder;
    @EJB
    private EntityCreditCardProductBuilder entityCreditCardProductBuilder;
    @EJB
    private EntityPromoProductBuilder entityPromoProductBuilder;
    @EJB
    private EntityBillOrganizationBuilder entityBillOrgBuilder;
    @EJB
    private EntityStaffBuilder entityStaffBuilder;
    @EJB
    private EntityDAMSBuilder entityDAMSBuilder;
    @EJB
    private EntityLoanBuilder entityLoanBuilder;
    @EJB
    private EntityCaseBuilder entityCaseBuilder;
    @EJB
    private EntityPayLahBuilder entityPayLahBuilder;
    @EJB
    private EntityWealthBuilder entityWealthBuilder;
    @EJB
    private EntityFactBuilder entityFactBuilder;
    
    // session beans
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;

    private RewardCardProduct demoRewardCardProduct;
    private PromoProduct demoPromoProduct;
    private MainAccount demoMainAccount;
    private Portfolio demoPortfolio;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        System.out.println("EntityInitilzationBean @PostConstruct");
        if (needInit()) {
            buildEntities();
        } else {
            
        }
    }

    // Use Super Admin Account as a flag
    private Boolean needInit() {
        StaffAccount sa = staffAccountSessionBean.getAccountByUsername(ConstantUtils.SUPER_ADMIN_USERNAME);
        return sa == null;
    }

    private void buildEntities() {
        entityStaffBuilder.initStaffAndRoles();
        demoMainAccount = entityCustomerBuilder.initCustomer();
        CustomerDepositAccount demoDepositAccount = entityDAMSBuilder.initDAMS();
        entityLoanBuilder.initLoanAccount(demoDepositAccount);
        
        demoPromoProduct = entityPromoProductBuilder.initPromoProduct(demoPromoProduct);
        demoRewardCardProduct = entityCreditCardProductBuilder.initCreditCardProduct(demoPromoProduct);
        
        entityCreditCardOrderBuilder.initCreditCardOrder(demoRewardCardProduct, demoPromoProduct);
        entityCaseBuilder.initCase();
        entityBillOrgBuilder.initBillOrganization();
        entityPayLahBuilder.initPayLahDemoData();
        
//        List<FinancialInstrument> allFinancialInstruments = entityWealthBuilder.allFinancialInstrument();
//        demoPortfolio = entityWealthBuilder.initWealth(allFinancialInstruments);
//        entityFactBuilder.initSinglePortfolioFact(demoMainAccount, demoPortfolio);
//        demoPortfolio = entityWealthBuilder.initPortfolioFactTable2(demoMainAccount, allFinancialInstruments);
//        entityFactBuilder.initSinglePortfolioFact(demoMainAccount, demoPortfolio);

    }
}
