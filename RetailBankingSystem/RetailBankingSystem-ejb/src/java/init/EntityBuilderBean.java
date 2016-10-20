/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanCalculationSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import ejb.session.loan.LoanProductSessionBeanLocal;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import entity.customer.WealthManagementSubscriber;
import entity.staff.StaffAccount;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
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
        entityDAMSBuilder.initDAMS();
        demoPromoProduct = entityPromoProductBuilder.initPromoProduct(demoPromoProduct);
        demoRewardCardProduct = entityCreditCardProductBuilder.initCreditCardProduct(demoPromoProduct);
        entityCaseBuilder.initCase();
        entityCreditCardOrderBuilder.initCreditCardOrder(demoRewardCardProduct, demoPromoProduct);
        entityBillOrgBuilder.initBillOrganization();
        entityPayLahBuilder.initPayLahDemoData();
        entityWealthBuilder.initWealth();
    }

}
