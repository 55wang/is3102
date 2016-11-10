/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardProductSessionBeanLocal;
import ejb.session.card.CardTransactionSessionBeanLocal;
import ejb.session.card.CreditCardOrderSessionBeanLocal;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.order.CreditCardOrder;
import entity.card.product.MileCardProduct;
import entity.card.product.PromoCode;
import entity.card.product.PromoProduct;
import entity.card.product.RewardCardProduct;
import entity.customer.MainAccount;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import server.utilities.PincodeGenerationUtils;
import util.exception.common.MainAccountNotExistException;

/**
 *
 * @author wang
 */
@LocalBean
@Stateless
public class EntityCreditCardOrderBuilder {

    @EJB
    private CreditCardOrderSessionBeanLocal creditCardOrderSessionBean;
    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    private CardTransactionSessionBeanLocal cardTransactionSessionBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;

    public void initCreditCardOrder(RewardCardProduct demoRewardCardProduct, MileCardProduct demoMileCardProduct, PromoProduct demoPromoProduct) {

        MainAccount demoMainAccount = null;
        try {
            demoMainAccount = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_1);
        } catch (MainAccountNotExistException ex) {
            System.out.println("EntityCreditCardOrderBuilder.initCreditCardOrder.MainAccountNotExistException");
        }
        //create an active cca and its cco
        CreditCardAccount cca = new CreditCardAccount();
        cca.setCreditCardProduct(demoRewardCardProduct);
        cca.setNameOnCard(demoMainAccount.getCustomer().getFullName());
        cca.setCreditLimit(2000.0);
        cca.setCreditCardNum("5544498059996726");
        cca.setCvv("123");
        cca.setOutstandingAmount(1000.0);
        cca.setMerlionPoints(100000.0);
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
        cardTransaction.setAmount(500.0);
        cardTransaction.setIsCredit(true);
        cardTransaction.setTransactionCode("MST");
        cardTransaction.setTransactionDescription("AMAZON SERVICE USD378.50");
        cardTransaction.setCreditCardAccount(cca);
        cardTransactionSessionBean.createCardAccountTransaction(cca, cardTransaction);

        cardTransaction = new CardTransaction();
        cardTransaction.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
        cardTransaction.setAmount(200.0);
        cardTransaction.setIsCredit(true);
        cardTransaction.setTransactionCode("MST");
        cardTransaction.setTransactionDescription("Apple SERVICE USD168.50");
        cardTransaction.setCreditCardAccount(cca);
        cardTransactionSessionBean.createCardAccountTransaction(cca, cardTransaction);

        cardTransaction = new CardTransaction();
        cardTransaction.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
        cardTransaction.setAmount(11000.0);
        cardTransaction.setIsCredit(true);
        cardTransaction.setTransactionCode("MST");
        cardTransaction.setTransactionDescription("Microsoft SERVICE USD7800.50");
        cardTransaction.setCreditCardAccount(cca);
        cardTransactionSessionBean.createCardAccountTransaction(cca, cardTransaction);

        CreditCardOrder cco = new CreditCardOrder();
        cco.setApplicationStatus(EnumUtils.CardApplicationStatus.APPROVED);
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
        cca2.setCvv("123");
        cca2.setCreditLimit(20000.0);
        cca2.setOutstandingAmount(0.0);
        cca2.setMerlionPoints(100000.0);
        cca2.setCardStatus(EnumUtils.CardAccountStatus.PENDING);
        cca2.setMainAccount(demoMainAccount);
        cardAcctSessionBean.createCardAccount(cca2);

        CreditCardOrder cco2 = new CreditCardOrder();
        cco2.setApplicationStatus(EnumUtils.CardApplicationStatus.PENDING);
        cco2.setCreditCardAccount(cca2);
        cco2.setMainAccount(demoMainAccount);
        creditCardOrderSessionBean.createCardOrder(cco2);
        cca2.setCreditCardOrder(cco2);
        cardAcctSessionBean.updateCreditCardAccount(cca2);

        CreditCardAccount cca3 = new CreditCardAccount();
        cca3.setCreditCardProduct(demoMileCardProduct);
        cca3.setNameOnCard(demoMainAccount.getCustomer().getFullName());
        cca3.setCreditCardNum("5556336827217675");
        cca3.setCvv("123");
        cca3.setOutstandingAmount(10000.0);
        cca3.setCreditLimit(10000.0);
        cca3.setMerlionMiles(80000.0);
        cca3.setCardStatus(EnumUtils.CardAccountStatus.ACTIVE);
        cca3.setMainAccount(demoMainAccount);
        cca3 = cardAcctSessionBean.createCardAccount(cca3);

        CreditCardOrder cco3 = new CreditCardOrder();
        cco3.setApplicationStatus(EnumUtils.CardApplicationStatus.PENDING);
        cco3.setCreditCardAccount(cca3);
        cco3.setMainAccount(demoMainAccount);
        creditCardOrderSessionBean.createCardOrder(cco3);
        cca3.setCreditCardOrder(cco3);
        cardAcctSessionBean.updateCreditCardAccount(cca3);

        CreditCardAccount cca4 = new CreditCardAccount();
        cca4.setCreditCardProduct(demoMileCardProduct);
        cca4.setNameOnCard(demoMainAccount.getCustomer().getFullName());
        cca4.setCreditCardNum("5374943811149259");
        cca4.setCvv("123");
        cca4.setOutstandingAmount(-500.0);
        cca4.setMerlionMiles(8000.0);
        cca4.setCardStatus(EnumUtils.CardAccountStatus.ACTIVE);
        cca4.setMainAccount(demoMainAccount);
        cca4 = cardAcctSessionBean.createCardAccount(cca4);

        CreditCardOrder cco4 = new CreditCardOrder();
        cco4.setApplicationStatus(EnumUtils.CardApplicationStatus.APPROVED);
        cco4.setCreditCardAccount(cca4);
        cco4.setMainAccount(demoMainAccount);
        creditCardOrderSessionBean.createCardOrder(cco4);
        cca4.setCreditCardOrder(cco4);
        cardAcctSessionBean.updateCreditCardAccount(cca4);

    }
}
