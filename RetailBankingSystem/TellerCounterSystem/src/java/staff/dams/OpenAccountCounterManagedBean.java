/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.dams;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.counter.TellerCounterSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.DepositProductSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.counter.TellerCounter;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.DepositProduct;
import entity.dams.account.FixedDepositAccountProduct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.validation.constraints.NotNull;
import server.utilities.CommonUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.DepositAccountType;
import server.utilities.PincodeGenerationUtils;
import util.exception.common.DuplicateMainAccountExistException;
import util.exception.common.UpdateMainAccountException;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "openAccountCounterManagedBean")
@ViewScoped
public class OpenAccountCounterManagedBean implements Serializable {

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private InterestSessionBeanLocal interestBean;
    @EJB
    private DepositProductSessionBeanLocal productBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private TellerCounterSessionBeanLocal counterBean;

    private MainAccount mainAccount;
    private Customer customer;
    private String customerIC;
    private Boolean isNewCustomer = false;

    private Integer years;
    private BigDecimal initialDeposit;
    @NotNull(message = "Deposit account may not be null")
    private String selectedProduct;
    private List<DepositProduct> avaiableProducts = new ArrayList<>();
    @NotNull(message = "Identity type is required.")
    private String selectedIdentityType;
    private String selectedNationality;
    private String selectedGender;
    private String selectedOccupation;
    private String selectedIncome;
    private List<String> identityTypeOptions = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> nationalityOptions = CommonUtils.getEnumList(EnumUtils.Nationality.class);
    private List<String> genderOptions = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private List<String> occupationOptions = CommonUtils.getEnumList(EnumUtils.EmploymentStatus.class);
    private List<String> incomeOptions = CommonUtils.getEnumList(EnumUtils.Income.class);

    private Date currentDate = new Date();

    public OpenAccountCounterManagedBean() {
        System.out.println("OpenAccountManagedBean() Created!!");
    }

    @PostConstruct
    public void init() {
        avaiableProducts = productBean.getAllPresentProducts();

        AuditLog a = new AuditLog();
        a.setActivityLog("Counter user enter openAccountCounterManagedBean");
        a.setFunctionName("openAccountCounterManagedBean @PostConstruct init()");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void retrieveMainAccount() {

        try {
            mainAccount = loginSessionBean.getMainAccountByUserIC(getCustomerIC());
        } catch (Exception e) {
            mainAccount = null;
        }

        if (mainAccount != null) {
            customer = mainAccount.getCustomer();
            isNewCustomer = false;
            MessageUtils.displayInfo("Customer Main Account Retrieved!");
            selectedIdentityType = customer.getIdentityType().toString();
            selectedNationality = customer.getNationality().toString();
            selectedGender = customer.getGender().toString();
            selectedOccupation = customer.getEmploymentStatus().toString();
            selectedIncome = customer.getIncome().toString();
        } else {
            MessageUtils.displayInfo("New Main Account!");
            isNewCustomer = true;
            mainAccount = new MainAccount();
            customer = new Customer();
        }
    }

    public Boolean isFixedDeposit() {
        if (selectedProduct == null) {
            return false;
        }
        DepositProduct dp = productBean.getDepositProductByName(selectedProduct);
        if (dp.getType() == DepositAccountType.FIXED) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean initialDepositWithinRange() {
        DepositProduct dp = productBean.getDepositProductByName(selectedProduct);
        if (dp.getType() == DepositAccountType.FIXED) {
            FixedDepositAccountProduct fdp = new FixedDepositAccountProduct();
            if (dp instanceof FixedDepositAccountProduct) {
                fdp = (FixedDepositAccountProduct) dp;
            }
            return initialDeposit.compareTo(fdp.getMinAmount()) >= 0 && initialDeposit.compareTo(fdp.getMaxAmount()) < 0;
        } else {
            return initialDeposit.compareTo(new BigDecimal(999999)) < 0;
        }
    }

    public void openAccount() {

        try {
            if (!updateCounter(true, initialDeposit)) {
                return;
            }

            if (!initialDepositWithinRange()) {
                MessageUtils.displayError("Not Within Initial Deposit Range!");
            }

            DepositProduct dp = productBean.getDepositProductByName(selectedProduct);

            if (isNewCustomer) {
                mainAccount.setStatus(EnumUtils.StatusType.PENDING);
                mainAccount.setUserID(generateUserID(EnumUtils.IdentityType.NRIC, getCustomer().getIdentityNumber()));
                String randomPwd = PincodeGenerationUtils.generatePwd();
                mainAccount.setPassword(randomPwd);
                mainAccount = mainAccountSessionBean.createMainAccount(mainAccount);
                customer.setIncome(EnumUtils.Income.getEnum(selectedIncome));
                customer.setNationality(EnumUtils.Nationality.getEnum(selectedNationality));
                customer.setGender(EnumUtils.Gender.getEnum(selectedGender));
                customer.setNationality(EnumUtils.Nationality.getEnum(selectedNationality));
                customer.setEmploymentStatus(EnumUtils.EmploymentStatus.getEnum(selectedOccupation));
                customer.setMainAccount(mainAccount);
                customer = newCustomerSessionBean.createCustomer(getCustomer());
                mainAccount.setCustomer(customer);
                mainAccount = mainAccountSessionBean.updateMainAccount(mainAccount);
                emailServiceSessionBean.sendActivationGmailForCustomer(getCustomer().getEmail(), randomPwd);
            } else {
                customer.setIncome(EnumUtils.Income.getEnum(selectedIncome));
                customer.setNationality(EnumUtils.Nationality.getEnum(selectedNationality));
                customer.setGender(EnumUtils.Gender.getEnum(selectedGender));
                customer.setNationality(EnumUtils.Nationality.getEnum(selectedNationality));
                customer.setEmploymentStatus(EnumUtils.EmploymentStatus.getEnum(selectedOccupation));
                newCustomerSessionBean.updateCustomer(customer);
            }

            DepositProduct product = productBean.getDepositProductByName(selectedProduct);

            if (product.getType() == DepositAccountType.FIXED) {
                CustomerFixedDepositAccount fda = new CustomerFixedDepositAccount();
                fda.setMainAccount(mainAccount);
                fda.setType(DepositAccountType.FIXED);
                fda.setBalance(initialDeposit);
                fda.setInterestRules(interestBean.getFixedDepositAccountDefaultInterests());
                fda.setProduct(product);
                fda.setMaturityDate(DateUtils.addYearsToDate(currentDate, years));
                depositBean.createAccount(fda);
            } else {
                CustomerDepositAccount da = new CustomerDepositAccount();
                da.setMainAccount(mainAccount);
                da.setType(product.getType());
                da.setBalance(initialDeposit);
                da.setProduct(product);
                depositBean.createAccount(da);
            }

            MessageUtils.displayInfo("Deposit Account Created!");
        } catch (DuplicateMainAccountExistException | UpdateMainAccountException e) {
            System.out.println("DuplicateMainAccountExistException or UpdateMainAccountException thrown at OpenAccountCounterManagedBean.java openAccount()");
            MessageUtils.displayError("Error opening account!");
        }

    }

    public Boolean updateCounter(Boolean isAdd, BigDecimal amount) {
        TellerCounter tc = SessionUtils.getTellerCounter();
        tc = counterBean.getTellerCounterById(tc.getId());
        if (isAdd) {
            tc.addCash(amount);
        } else {
            if (tc.hasEnoughCash(amount)) {
                tc.removeCash(amount);
            } else {
                MessageUtils.displayError("Not Enough Cash in Counter!");
                return false;
            }
        }

        tc = counterBean.updateTellerCounter(tc);
        SessionUtils.setTellerCounter(tc);

        return true;
    }

    private String generateUserID(EnumUtils.IdentityType identityType, String identityNum) {

        if (identityType.equals(EnumUtils.IdentityType.NRIC)) {
            return "c" + identityNum.substring(1, identityNum.length() - 1);
        } else {
            return "error";
        }
    }

    /**
     * @return the mainAccount
     */
    public MainAccount getMainAccount() {
        return mainAccount;
    }

    /**
     * @param mainAccount the mainAccount to set
     */
    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    /**
     * @return the customerIC
     */
    public String getCustomerIC() {
        return customerIC;
    }

    /**
     * @param customerIC the customerIC to set
     */
    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }

    /**
     * @return the identityTypeOptions
     */
    public List<String> getIdentityTypeOptions() {
        return identityTypeOptions;
    }

    /**
     * @param identityTypeOptions the identityTypeOptions to set
     */
    public void setIdentityTypeOptions(List<String> identityTypeOptions) {
        this.identityTypeOptions = identityTypeOptions;
    }

    /**
     * @return the nationalityOptions
     */
    public List<String> getNationalityOptions() {
        return nationalityOptions;
    }

    /**
     * @param nationalityOptions the nationalityOptions to set
     */
    public void setNationalityOptions(List<String> nationalityOptions) {
        this.nationalityOptions = nationalityOptions;
    }

    /**
     * @return the genderOptions
     */
    public List<String> getGenderOptions() {
        return genderOptions;
    }

    /**
     * @param genderOptions the genderOptions to set
     */
    public void setGenderOptions(List<String> genderOptions) {
        this.genderOptions = genderOptions;
    }

    /**
     * @return the occupationOptions
     */
    public List<String> getOccupationOptions() {
        return occupationOptions;
    }

    /**
     * @param occupationOptions the occupationOptions to set
     */
    public void setOccupationOptions(List<String> occupationOptions) {
        this.occupationOptions = occupationOptions;
    }

    /**
     * @return the incomeOptions
     */
    public List<String> getIncomeOptions() {
        return incomeOptions;
    }

    /**
     * @param incomeOptions the incomeOptions to set
     */
    public void setIncomeOptions(List<String> incomeOptions) {
        this.incomeOptions = incomeOptions;
    }

    /**
     * @return the selectedIdentityType
     */
    public String getSelectedIdentityType() {
        return selectedIdentityType;
    }

    /**
     * @param selectedIdentityType the selectedIdentityType to set
     */
    public void setSelectedIdentityType(String selectedIdentityType) {
        this.selectedIdentityType = selectedIdentityType;
    }

    /**
     * @return the selectedNationality
     */
    public String getSelectedNationality() {
        return selectedNationality;
    }

    /**
     * @param selectedNationality the selectedNationality to set
     */
    public void setSelectedNationality(String selectedNationality) {
        this.selectedNationality = selectedNationality;
    }

    /**
     * @return the selectedGender
     */
    public String getSelectedGender() {
        return selectedGender;
    }

    /**
     * @param selectedGender the selectedGender to set
     */
    public void setSelectedGender(String selectedGender) {
        this.selectedGender = selectedGender;
    }

    /**
     * @return the selectedOccupation
     */
    public String getSelectedOccupation() {
        return selectedOccupation;
    }

    /**
     * @param selectedOccupation the selectedOccupation to set
     */
    public void setSelectedOccupation(String selectedOccupation) {
        this.selectedOccupation = selectedOccupation;
    }

    /**
     * @return the selectedIncome
     */
    public String getSelectedIncome() {
        return selectedIncome;
    }

    /**
     * @param selectedIncome the selectedIncome to set
     */
    public void setSelectedIncome(String selectedIncome) {
        this.selectedIncome = selectedIncome;
    }

    /**
     * @return the selectedProduct
     */
    public String getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct the selectedProduct to set
     */
    public void setSelectedProduct(String selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    /**
     * @return the avaiableProducts
     */
    public List<DepositProduct> getAvaiableProducts() {
        return avaiableProducts;
    }

    /**
     * @param avaiableProducts the avaiableProducts to set
     */
    public void setAvaiableProducts(List<DepositProduct> avaiableProducts) {
        this.avaiableProducts = avaiableProducts;
    }

    /**
     * @return the currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return the years
     */
    public Integer getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(Integer years) {
        this.years = years;
    }

    /**
     * @return the isNewCustomer
     */
    public Boolean getIsNewCustomer() {
        return isNewCustomer;
    }

    /**
     * @param isNewCustomer the isNewCustomer to set
     */
    public void setIsNewCustomer(Boolean isNewCustomer) {
        this.isNewCustomer = isNewCustomer;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the initialDeposit
     */
    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    /**
     * @param initialDeposit the initialDeposit to set
     */
    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

}
