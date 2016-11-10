/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CreditCardOrderSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.card.account.DebitCardAccount;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.smartcardio.CardChannel;
import static nfcCardDevice.NfcDevice.initializeDevice;
import static nfcCardDevice.NfcDevice.readCard;
import static nfcCardDevice.NfcDevice.writeCard;
import server.utilities.EnumUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "cardIssueManagedBean")
@Dependent
public class CardIssueManagedBean {

    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    CreditCardOrderSessionBeanLocal creditOrderSessionBean;

    private List<CreditCardAccount> ccas;
    private List<DebitCardAccount> dcas;

    /**
     * Creates a new instance of CardIssueManagedBean
     */
    public CardIssueManagedBean() {
    }

    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter issue_card.xhtml");
        a.setFunctionName("CardIssueManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all card applications");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);

        setCcas(cardAcctSessionBean.getListCreditCardAccountsByCardStatusAndAppStatus(EnumUtils.CardAccountStatus.APPROVED,
                EnumUtils.CardApplicationStatus.APPROVED));
        setDcas(cardAcctSessionBean.getListDebitCardAccountsByStatus(EnumUtils.CardAccountStatus.APPROVED));
    }

    public void issueCard(CreditCardAccount cca) {
        // TODO: write to card
        String ccNum = cca.getCreditCardNum();
        System.out.println("*** Start nfc device ***");
        boolean writeStatus = false;

        try {

            CardChannel channel = initializeDevice();
            writeCard(channel, ccNum); //32 digit
            if (readCard(channel).equals(ccNum)) {
                System.out.println("read card to confirm success");
                writeStatus = true;
            } else {
                writeStatus = false;
            }
            writeStatus = true;

        } catch (Exception ex) {
            System.out.println("error" + ex);
            writeStatus = false;
        }

        if (writeStatus) {
            cca.getCreditCardOrder().setApplicationStatus(EnumUtils.CardApplicationStatus.ISSUED);
            creditOrderSessionBean.updateCreditCardOrder(cca.getCreditCardOrder());
            cca.setCardStatus(EnumUtils.CardAccountStatus.ISSUED);
            CreditCardAccount result = cardAcctSessionBean.updateCreditCardAccount(cca);
            if (result == null) {
                MessageUtils.displayError("Something went wrong!");
            } else {
                MessageUtils.displayInfo("Credit Card Issued!");
            }
        }
        //if success then continue

    }

    public void issueDebitCard(DebitCardAccount dca) {
        // TODO: write to card
        String ccNum = dca.getCreditCardNum();
        System.out.println("*** Start nfc device ***");
        boolean writeStatus = false;

        try {
            
             CardChannel channel = initializeDevice();
             writeCard(channel, ccNum); //32 digit
             if (readCard(channel).equals(ccNum)) {
             System.out.println("read card to confirm success");
             writeStatus = true;
             } else {
             writeStatus = false;
             }
             
//            writeStatus = true;

        } catch (Exception ex) {
            System.out.println("error" + ex);
            writeStatus = false;
        }

        if (writeStatus) {
            dca.setCardStatus(EnumUtils.CardAccountStatus.ISSUED);
            DebitCardAccount result = cardAcctSessionBean.updateDebitAccount(dca);
            if (result == null) {
                MessageUtils.displayError("Something went wrong!");
            } else {
                MessageUtils.displayInfo("Debit Card Issued!");
            }
        }
        //if success then continue

    }

    /**
     * @return the ccas
     */
    public List<CreditCardAccount> getCcas() {
        return ccas;
    }

    /**
     * @param ccas the ccas to set
     */
    public void setCcas(List<CreditCardAccount> ccas) {
        this.ccas = ccas;
    }

    /**
     * @return the dcas
     */
    public List<DebitCardAccount> getDcas() {
        return dcas;
    }

    /**
     * @param dcas the dcas to set
     */
    public void setDcas(List<DebitCardAccount> dcas) {
        this.dcas = dcas;
    }
}
