/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CreditCardOrderSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.card.order.CreditCardOrder;
import entity.common.AuditLog;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import server.utilities.EnumUtils.*;
import server.utilities.GenerateAccountAndCCNumber;
import server.utilities.PincodeGenerationUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "cardApplicationManagedBean")
@ViewScoped
public class CardApplicationManagedBean implements Serializable {

    private final static String STAFF_NOTIFY_CHANNEL = "/staff_notify";
    private final static String CUSTOMER_NOTIFY_CHANNEL = "/customer_notify";
    private final static String ROLE_NOTIFY_CHANNEL = "/role_notification/";

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private CreditCardOrderSessionBeanLocal creditCardOrderSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    /**
     * Creates a new instance of CardApplicationManagedBean
     */
    public CardApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter apply_card.xhtml");
        a.setFunctionName("CardApplicationManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all card applications");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    //get all applications
    public List<CreditCardOrder> displayCreditCardOrders() {
        return creditCardOrderSessionBean.getListCreditCardOrders();
    }

    //D.4.1.3
    //create card account
    public CreditCardAccount issueCreditCard(String creditCardOrder) {
        CreditCardOrder cco = creditCardOrderSessionBean.getCreditCardOrderById(Long.parseLong(creditCardOrder));

        updateApplicationStatus(creditCardOrder, ApplicationStatus.APPROVED);
        //generate card number
        String cardNumber = GenerateAccountAndCCNumber.generateMasterCardNumber();

        //create card account;
        CreditCardAccount cca = new CreditCardAccount();
        cca.setCreditCardNum(cardNumber);
        cca.setCardStatus(CardAccountStatus.PENDING);
        Calendar cal = Calendar.getInstance();
        cca.setCreationDate(cal.getTime());

//        CreditCardProduct cp = null;
//        if (cco.getCreditType().getValue().equals("MILE")) {
//            cp = new MileCardProduct();
//        } else if (cco.getCreditType().getValue().equals("REWARD")) {
//            cp = new RewardCardProduct();
//        } else if (cco.getCreditType().getValue().equals("CASHBACK")) {
//            cp = new CashBackCardProduct();
//        }
//        cca.setCreditCardProduct(cp);
       
        cco.getMainAccount().setPassword(PincodeGenerationUtils.generatePwd());
        
        //email to the customer about new account
        emailServiceSessionBean.sendActivationGmailForCustomer(cco.getMainAccount().getCustomer().getEmail(), cco.getMainAccount().getPassword());

        //send a notification to staff to write card info to mifare card
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(STAFF_NOTIFY_CHANNEL, "New Card Printing Order" + cca.getCreditCardNum());

        return cca;
    }

    //D.4.1.2 verify card application + additional info
    //editable
    public String needAdditionalInfo(String creditCardOrder, String msg) {
        try {

            CreditCardOrder cco = creditCardOrderSessionBean.getCreditCardOrderById(Long.parseLong(creditCardOrder));
            creditCardOrderSessionBean.updateCreditCardOrderStatus(cco, ApplicationStatus.PENDING);

            //email the customer
            String email = cco.getMainAccount().getCustomer().getEmail();
            emailServiceSessionBean.sendRequireAdditionalInfo(email, msg);

        } catch (Exception ex) {
            System.out.println("CardApplicationManagedBean.needAdditionalInfo ERROR");
            return "FAIL";
        }
        return "SUCCESS";
    }

    //pending, reject,
    public CreditCardOrder updateApplicationStatus(String creditCardOrder, ApplicationStatus status) {
        CreditCardOrder cco = creditCardOrderSessionBean.getCreditCardOrderById(Long.parseLong(creditCardOrder));

        CreditCardOrder result = creditCardOrderSessionBean.updateCreditCardOrderStatus(cco, status);

        if (status.equals(ApplicationStatus.REJECT)) {
            //email the customer about the result
            String msg = "We are sorry that your application is not successful.";
            emailServiceSessionBean.sendRequireAdditionalInfo(cco.getMainAccount().getCustomer().getEmail(), msg);
        }

        return result;
    }

}
