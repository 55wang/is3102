/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import entity.common.TransferRecord;
import entity.customer.CustomerCase;
import entity.customer.Issue;
import entity.customer.MainAccount;
import entity.customer.WealthManagementSubscriber;
import entity.dams.account.Cheque;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.DepositAccount;
import entity.dams.account.DepositAccountProduct;
import entity.dams.account.FixedDepositAccountProduct;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
import entity.dams.rules.RangeInterest;
import entity.dams.rules.TimeRangeInterest;
import entity.staff.Announcement;
import entity.staff.Role;
import entity.staff.StaffAccount;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.PayeeType;

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

    // session beans
    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;

    private RewardCardProduct demoRewardCardProduct;
    private PromoProduct demoPromoProduct;

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
        entityCustomerBuilder.initCustomer();
        CustomerDepositAccount demoDepositAccount = entityDAMSBuilder.initDAMS();
        entityLoanBuilder.initLoanAccount(demoDepositAccount);
        demoPromoProduct = entityPromoProductBuilder.initPromoProduct(demoPromoProduct);
        demoRewardCardProduct = entityCreditCardProductBuilder.initCreditCardProduct(demoPromoProduct);
        entityCaseBuilder.initCase();
        entityCreditCardOrderBuilder.initCreditCardOrder(demoRewardCardProduct, demoPromoProduct);
        entityBillOrgBuilder.initBillOrganization();
        entityPayLahBuilder.initPayLahDemoData();
//        entityWealthBuilder.initWealth();
    }
}
