/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardProductSessionBeanLocal;
import ejb.session.card.CardTransactionSessionBean;
import ejb.session.card.CardTransactionSessionBeanLocal;
import ejb.session.card.CreditCardOrderSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.order.CreditCardOrder;
import entity.card.product.CreditCardProduct;
import entity.card.product.PromoCode;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import entity.customer.MainAccount;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import server.utilities.EnumUtils;
import server.utilities.PincodeGenerationUtils;

/**
 *
 * @author wang
 */
@LocalBean
@Singleton
public class EntityCreditCardOrderBuilder {

    @EJB
    CardProductSessionBeanLocal cardProductSessionBean;
    @EJB
    CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    CreditCardOrderSessionBeanLocal creditCardOrderSessionBean;
    @EJB
    CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    private CardTransactionSessionBeanLocal cardTransactionSessionBean;

    public void initCreditCardOrder(MainAccount demoMainAccount, RewardCardProduct demoRewardCardProduct, PromoProduct demoPromoProduct) {
        //create an active cca and its cco
        CreditCardAccount cca = new CreditCardAccount();
        cca.setCreditCardProduct(demoRewardCardProduct);
        cca.setNameOnCard(demoMainAccount.getCustomer().getFullName());
        cca.setCreditCardNum("4545454545454545");
        cca.setOutstandingAmount(0);
        cca.setMerlionPoints(100000);
        cca.setCardStatus(EnumUtils.CardAccountStatus.ACTIVE);
        cca.setMainAccount(demoMainAccount);
        cca = cardAcctSessionBean.createCardAccount(cca);

        cca.deductPoints(demoPromoProduct.getPoints());
        PromoCode pc = new PromoCode();
        pc.setProduct(demoPromoProduct);
        pc.setPromotionCode(PincodeGenerationUtils.generateRandom(false, 8));
        pc.setCreditCardAccount(cca);
        cca.addPromoCode(pc);
        utilsBean.merge(cca);

        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
        cardTransaction.setAmount(500);
        cardTransaction.setIsCredit(true);
        cardTransaction.setTransactionCode("MST");
        cardTransaction.setTransactionDescription("AMAZON SERVICE USD378.50");
        cardTransaction.setCreditCardAccount(cca);
        cardTransactionSessionBean.createCardAccountTransaction(cca, cardTransaction);

        cardTransaction = new CardTransaction();
        cardTransaction.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
        cardTransaction.setAmount(200);
        cardTransaction.setIsCredit(true);
        cardTransaction.setTransactionCode("MST");
        cardTransaction.setTransactionDescription("Apple SERVICE USD168.50");
        cardTransaction.setCreditCardAccount(cca);
        cardTransactionSessionBean.createCardAccountTransaction(cca, cardTransaction);

        cardTransaction = new CardTransaction();
        cardTransaction.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
        cardTransaction.setAmount(100);
        cardTransaction.setIsCredit(true);
        cardTransaction.setTransactionCode("MST");
        cardTransaction.setTransactionDescription("Microsoft SERVICE USD78.50");
        cardTransaction.setCreditCardAccount(cca);
        cardTransactionSessionBean.createCardAccountTransaction(cca, cardTransaction);

        CreditCardOrder cco = new CreditCardOrder();
        cco.setApplicationStatus(EnumUtils.ApplicationStatus.APPROVED);
        cco.setCreditCardAccount(cca);
        cco.setMainAccount(demoMainAccount);
        creditCardOrderSessionBean.createCardOrder(cco);
        cca.setCreditCardOrder(cco);
        cardAcctSessionBean.updateCreditCardAccount(cca);

        //create a pending cca and its cco
        CreditCardAccount cca2 = new CreditCardAccount();
        cca2.setCreditCardProduct(demoRewardCardProduct);
        cca2.setNameOnCard(demoMainAccount.getCustomer().getFullName());
        cca2.setCreditCardNum("5273076135089505");
        cca2.setOutstandingAmount(0);
        cca2.setMerlionPoints(100000);
        cca2.setCardStatus(EnumUtils.CardAccountStatus.NEW);
        cca2.setMainAccount(demoMainAccount);
        cardAcctSessionBean.createCardAccount(cca2);

        CreditCardOrder cco2 = new CreditCardOrder();
        cco2.setApplicationStatus(EnumUtils.ApplicationStatus.NEW);
        cco2.setCreditCardAccount(cca2);
        cco2.setMainAccount(demoMainAccount);
        creditCardOrderSessionBean.createCardOrder(cco2);
        cca2.setCreditCardOrder(cco2);
        cardAcctSessionBean.updateCreditCardAccount(cca2);
    }
}
