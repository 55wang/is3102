/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.card.product.MileCardProduct;
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
    @EJB
    private EntityTellerCounterBuilder entityTellerCounterBuilder;
    @EJB
    private EntityCRMBuilder entityCRMBuilder;
    
    // session beans
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;

    private RewardCardProduct demoRewardCardProduct;
    private MileCardProduct demoMileCardProduct;

    private PromoProduct demoPromoProduct;
    private PromoProduct demoPromoProduct2;

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
        StaffAccount sa = null;
        
        try {
            sa = staffAccountSessionBean.getAdminStaff();
        } catch (Exception e) {
            System.out.println("Super admin not found, init all data...");
        }
         
        return sa == null;
    }

    private void buildEntities() {
        entityStaffBuilder.initStaffAndRoles();
        demoMainAccount = entityCustomerBuilder.initCustomer();
        CustomerDepositAccount demoDepositAccount = entityDAMSBuilder.initDAMS();
        entityLoanBuilder.initLoanAccount(demoDepositAccount);

        demoPromoProduct = entityPromoProductBuilder.initPromoProduct(demoPromoProduct);
        demoRewardCardProduct = entityCreditCardProductBuilder.initCreditCardProduct();
        demoMileCardProduct = entityCreditCardProductBuilder.initMileCreditCardProduct();

        entityCreditCardOrderBuilder.initCreditCardOrder(demoRewardCardProduct, demoMileCardProduct, demoPromoProduct);
        entityCaseBuilder.initCase();
        entityBillOrgBuilder.initBillOrganization();
        entityPayLahBuilder.initPayLahDemoData();
        entityTellerCounterBuilder.init();

        //wealth

//        List<FinancialInstrument> allFinancialInstruments = entityWealthBuilder.allFinancialInstrument();
//        demoPortfolio = entityWealthBuilder.initWealth(allFinancialInstruments);
//        entityFactBuilder.initSinglePortfolioFact(demoMainAccount, demoPortfolio);
//        demoPortfolio = entityWealthBuilder.initPortfolioFactTable2(demoMainAccount, allFinancialInstruments);
//        entityFactBuilder.initSinglePortfolioFact(demoMainAccount, demoPortfolio);
//        entityFactBuilder.initBankFact();
//        entityCRMBuilder.initCustomerRFM();
    }
}
