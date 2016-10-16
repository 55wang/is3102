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
    private WealthManagementSubscriber demoWealthSubscriber;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        System.out.println("EntityInitilzationBean @PostConstruct");
        if (needInit()) {
            buildEntities();
        } else {
//            String queryString = "SELECT t FROM TransferRecord t WHERE (";
//            queryString += " t.fromAccount.accountNumber = 13059510076";
//            queryString += ")";
//            Query q = em.createQuery(queryString + " AND t.type =:inType  AND t.creationDate BETWEEN :startDate AND :endDate");
//            Date startDate = DateUtils.getBeginOfDay();
//            Date endDate = DateUtils.getEndOfDay();
//            q.setParameter("startDate", startDate);
//            q.setParameter("endDate", endDate);
//            q.setParameter("inType", PayeeType.LOCAL);
//            List<TransferRecord> records = q.getResultList();
//            BigDecimal totalAmount = BigDecimal.ZERO;
//            System.out.println("Totoal records found:" + records.size());
//            for (TransferRecord t : records) {
//                System.out.println("TransferRecord Found:" + t.getAmount());
//                totalAmount = totalAmount.add(t.getAmount());
//            }
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
        entityDAMSBuilder.initDAMS();
        demoPromoProduct = entityPromoProductBuilder.initPromoProduct(demoPromoProduct);
        demoRewardCardProduct = entityCreditCardProductBuilder.initCreditCardProduct(demoPromoProduct);
        entityCaseBuilder.initCase();
        entityCreditCardOrderBuilder.initCreditCardOrder(demoRewardCardProduct, demoPromoProduct);
        entityBillOrgBuilder.initBillOrganization();
        entityPayLahBuilder.initPayLahDemoData();
//        entityWealthBuilder.initWealth();
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
