/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.card.account.CreditCardOrder;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import server.utilities.EnumUtils.*;
import server.utilities.CommonHelper;
import server.utilities.GenerateAccountAndCCNumber;

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
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;

    /**
     * Creates a new instance of CardApplicationManagedBean
     */
    public CardApplicationManagedBean() {
    }
    
    public String processChargeBack(String notification, String creditCard, String status) {
        //reverse transaction amount
        

        //send notification to specific customer based on their creditcard number
        
        
        //update chargeback status at the case management
        
        return null;
    }

    //get all applications
    public List<CreditCardOrder> displayCreditCardOrders() {
        return cardAcctSessionBean.showAllCreditCardOrder();
    }

    public String writeHardwareCard(String cardNumber) {

        return null;
    }

    //D.4.1.3
    //create card account
    public CreditCardAccount issueCreditCard(String creditCardOrder) {
        CreditCardOrder cco = cardAcctSessionBean.getCardOrderFromId(Long.parseLong(creditCardOrder));

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
        cca.setCvv(Integer.parseInt(CommonHelper.generateRandom(true, 3)));
        cal.set(Calendar.YEAR, 2);
        cca.setValidDate(cal.getTime());

        //new main account and customer
        Customer c = new Customer();
        c.setAddress(cco.getAddress());
        c.setBirthDay(cco.getBirthDay());
        c.setCitizenship(cco.getCitizenship());
        c.setCreditScore(cco.getCreditScore());
        c.setEducation(cco.getEduLevel());
        c.setEmail(cco.getEmail());
        c.setFirstname(cco.getFirstName());
        c.setGender(cco.getGender());
        c.setIdentityNumber(cco.getIdentityNumber());
        c.setIdentityType(cco.getIdentityType());
        c.setIncome(cco.getIncome());
        c.setLastname(cco.getLastName());
        c.setMaritalStatus(cco.getMaritalStatus());
        c.setNationality(cco.getNationality());
        c.setOccupation(cco.getOccupation());
        c.setPhone(cco.getPhone());
        c.setPostalCode(cco.getPostalCode());

        MainAccount ma = new MainAccount();
        ma.setUserID("CUST" + CommonHelper.generateRandom(true, 7));
        ma.setPassword(CommonHelper.generateRandom(false, 8));
        ma.setStatus(StatusType.PENDING);
        ma.setCustomer(c);
        List al = new ArrayList<>();
        al.add(cca);
        ma.setCreditCardAccounts(al);
        c.setMainAccount(ma);
        newCustomerSessionBean.createCustomer(c);
        //email to the customer about new account
        emailServiceSessionBean.sendActivationGmailForCustomer(cco.getEmail(), ma.getPassword());

        //send a notification to staff to write card info to mifare card
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(STAFF_NOTIFY_CHANNEL, "New Card Printing Order" + cca.getCreditCardNum());

        return cca;
    }

    //D.4.1.2 verify card application + additional info
    //editable
    public String needAdditionalInfo(String creditCardOrder, String msg) {
        try {

            CreditCardOrder cco = cardAcctSessionBean.getCardOrderFromId(Long.parseLong(creditCardOrder));
            cardAcctSessionBean.updateCardOrderStatus(cco, ApplicationStatus.PENDING);

            //email the customer
            String email = cco.getEmail();
            emailServiceSessionBean.sendRequireAdditionalInfo(email, msg);

        } catch (Exception ex) {
            System.out.println("CardApplicationManagedBean.needAdditionalInfo ERROR");
            return "FAIL";
        }
        return "SUCCESS";
    }

    //pending, reject,
    public String updateApplicationStatus(String creditCardOrder, ApplicationStatus status) {
        CreditCardOrder cco = cardAcctSessionBean.getCardOrderFromId(Long.parseLong(creditCardOrder));

        String result = cardAcctSessionBean.updateCardOrderStatus(cco, status);

        if (status.equals(ApplicationStatus.REJECT)) {
            //email the customer about the result
            String msg = "We are sorry that your application is not successful.";
            emailServiceSessionBean.sendRequireAdditionalInfo(cco.getEmail(), msg);
        }

        return result;
    }

}
