/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.dams;

import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.crm.MarketingCampaignSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.DepositProductSessionBeanLocal;
import entity.crm.CustomerGroup;
import entity.crm.MarketingCampaign;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccountProduct;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;
import server.utilities.ConstantUtils;
import server.utilities.CommonUtils;
import server.utilities.EnumUtils.DepositAccountType;
import utils.MessageUtils;

/**
 *
 * @author litong
 */
@Named(value = "existingCustomerApplicationManagedBean")
@ViewScoped

public class ExistingCustomerApplicationManagedBean implements Serializable {

    @EJB
    private DepositProductSessionBeanLocal productBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositAccountBean;
    @EJB
    private DepositProductSessionBeanLocal depositProductBean;
    @EJB
    private MarketingCampaignSessionBeanLocal marketingCampaignSessionBean;

    private String selectedMC;
    private MarketingCampaign marketingCampaign;

    private Customer customer = new Customer();
    private String initialDepositAccount;
    private List<DepositAccountProduct> currentDepositProducts = new ArrayList<>();
    private List<DepositAccountProduct> customDepositProducts = new ArrayList<>();
    private List<String> productOptions = new ArrayList<>();
    // TODO: For resend Email Button
    private Boolean emailSuccessFlag = true;

    /**
     * Creates a new instance of customerApplicationManagedBean
     */
    public ExistingCustomerApplicationManagedBean() {
    }

    @PostConstruct
    public void init() {
        System.out.println("CustomerApplicationManagedBean @PostContruct");
        currentDepositProducts = productBean.getAllPresentCurrentDepositProducts();
        customDepositProducts = productBean.getAllPresentCustomDepositProducts();
        initProductOptions();
    }

    public void initParameter() {
        System.out.println("MarketingCampaign id is: " + getSelectedMC());
        marketingCampaign = marketingCampaignSessionBean.getMarketingCampaign(Long.parseLong(getSelectedMC()));
    }

    // public functions
    public void save(Customer thisCustomer) {
        try {
            System.out.print(thisCustomer.getLastname());

            MainAccount mainAccount = thisCustomer.getMainAccount();

            CustomerDepositAccount depostiAccount = new CustomerDepositAccount();
            depostiAccount.setMainAccount(mainAccount);
            depostiAccount.setType(getType(initialDepositAccount));
            depostiAccount.setProduct(depositProductBean.getDepositProductByName(initialDepositAccount));

            depositAccountBean.createAccount(depostiAccount);

            //update response if he is in the marketing campaign
            for (CustomerGroup cg : thisCustomer.getCustomerGroups()) {
                for (MarketingCampaign mc : cg.getMarketingCampaigns()) {
                    if (mc.equals(marketingCampaign)) {
                        marketingCampaignSessionBean.addResponseCount(mc);
                        System.out.println("marketing response count added");
                    }
                }
            }

            MessageUtils.displayInfo("Your deposit account application is successful!");
        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }

    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    // private functions
    private void initProductOptions() {
        for (DepositAccountProduct p : currentDepositProducts) {
            getProductOptions().add(p.getName());
        }
        for (DepositAccountProduct p : customDepositProducts) {
            getProductOptions().add(p.getName());
        }
    }

    private DepositAccountType getType(String name) {
        for (DepositAccountProduct p : currentDepositProducts) {
            if (name.equals(p.getName())) {
                return DepositAccountType.CURRENT;
            }
        }
        for (DepositAccountProduct p : customDepositProducts) {
            if (name.equals(p.getName())) {
                return DepositAccountType.CUSTOM;
            }
        }
        return DepositAccountType.CURRENT;
    }

    // Getter and Setter
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the initialDepositAccount
     */
    public String getInitialDepositAccount() {
        return initialDepositAccount;
    }

    /**
     * @param initialDepositAccount the initialDepositAccount to set
     */
    public void setInitialDepositAccount(String initialDepositAccount) {
        this.initialDepositAccount = initialDepositAccount;
    }

    /**
     * @return the emailSuccessFlag
     */
    public Boolean getEmailSuccessFlag() {
        return emailSuccessFlag;
    }

    /**
     * @param emailSuccessFlag the emailSuccessFlag to set
     */
    public void setEmailSuccessFlag(Boolean emailSuccessFlag) {
        this.emailSuccessFlag = emailSuccessFlag;
    }

    /**
     * @return the productOptions
     */
    public List<String> getProductOptions() {
        return productOptions;
    }

    /**
     * @param productOptions the productOptions to set
     */
    public void setProductOptions(List<String> productOptions) {
        this.productOptions = productOptions;
    }

    public String getSelectedMC() {
        return selectedMC;
    }

    public void setSelectedMC(String selectedMC) {
        this.selectedMC = selectedMC;
    }

    public MarketingCampaign getMarketingCampaign() {
        return marketingCampaign;
    }

    public void setMarketingCampaign(MarketingCampaign marketingCampaign) {
        this.marketingCampaign = marketingCampaign;
    }

}
