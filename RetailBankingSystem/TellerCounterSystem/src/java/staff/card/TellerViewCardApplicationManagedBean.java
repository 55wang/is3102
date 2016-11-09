/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CreditCardOrderSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.card.order.CreditCardOrder;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import util.exception.common.MainAccountNotExistException;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "tellerViewCardApplicationManagedBean")
@ViewScoped
public class TellerViewCardApplicationManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private CreditCardOrderSessionBeanLocal creditCardOrderSessionBean;

    private String customerIC;
    private MainAccount mainAccount;
    private List<CreditCardOrder> creditCardOrders;

    public TellerViewCardApplicationManagedBean() {
    }

    public void retrieveMainAccount() {

        try {
            mainAccount = loginBean.getMainAccountByUserIC(getCustomerIC());
            creditCardOrders = creditCardOrderSessionBean.getListCreditCardOrdersByMainId(mainAccount.getId());
            System.out.println("success");
        } catch (Exception e) {
            mainAccount = null;
            MessageUtils.displayError("Customer Main Account Not Found!");
        }
    }

    public String getCustomerIC() {
        return customerIC;
    }

    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public List<CreditCardOrder> getCreditCardOrders() {
        return creditCardOrders;
    }

    public void setCreditCardOrders(List<CreditCardOrder> creditCardOrders) {
        this.creditCardOrders = creditCardOrders;
    }

    public void cancel(CreditCardOrder cco) {
        CreditCardOrder result = creditCardOrderSessionBean.updateCreditCardOrderStatus(cco, EnumUtils.CardApplicationStatus.CANCELLED);
        if (result != null) {
            creditCardOrders.remove(cco);
            MessageUtils.displayInfo("Cancel successfully");
        } else {
            MessageUtils.displayError("Error");
        }
    }

    public void showAllApplication() {
        this.creditCardOrders = creditCardOrderSessionBean.getListCreditCardOrders();
    }

    public List<CreditCardOrder> getApplications() {
        return creditCardOrders;
    }

}
