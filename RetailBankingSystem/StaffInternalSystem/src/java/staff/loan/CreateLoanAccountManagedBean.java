/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.DepositProductSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import ejb.session.loan.LoanPaymentSessionBeanLocal;
import ejb.session.loan.LoanProductSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanApplication;
import entity.loan.LoanProduct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import server.utilities.PincodeGenerationUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createLoanAccountManagedBean")
@ViewScoped
public class CreateLoanAccountManagedBean implements Serializable {
    
    @EJB
    private LoanProductSessionBeanLocal loanProductBean;
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;
    @EJB
    private LoanPaymentSessionBeanLocal loanPaymentSessionBean;
    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB 
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerBean;
    @EJB
    private DepositProductSessionBeanLocal depositProductBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositAccountBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountBean;
    
    private String applicationId;

    private String mainAccountId;
    private Long selectedLoanProductId;
    private Date paymentStartDate;
    private Double principalAmount;
    private LoanApplication currentApplication;
    
    private List<LoanProduct> loanProducts = new ArrayList<>();
    /**
     * Creates a new instance of CreateLoanAccountManagedBean
     */
    public CreateLoanAccountManagedBean() {
    }
    
    public void retrieveLoanApplication() {
        currentApplication = loanAccountBean.getLoanApplicationById(Long.parseLong(applicationId));
    }
    
    @PostConstruct
    public void init() {
        setLoanProducts(loanProductBean.getAllLoanProduct());
    }
    
    public void creatLoanAccount() {
        createOhterAccounts(currentApplication);
        MainAccount ma = loginBean.getMainAccountByUserID(getMainAccountId());
        CustomerDepositAccount cda = depositAccountBean.getDaytoDayAccountByMainAccount(ma);
        LoanAccount la = new LoanAccount();
        // mapping
        la.setMainAccount(ma);
        la.setDepositAccount(cda);
        la.setLoanOfficer(SessionUtils.getStaff());
        la.setLoanProduct(loanProductBean.getLoanProductById(selectedLoanProductId));
        // info
        la.setPaymentStartDate(getPaymentStartDate());
        la.setPaymentDate(DateUtils.getDayNumber(getPaymentStartDate()));
        la.setMaturityDate(DateUtils.addYearsToDate(getPaymentStartDate(), la.getLoanProduct().getTenure()));
        la.setTenure(la.getLoanProduct().getTenure());
        la.setPrincipal(getPrincipalAmount());
        la.setOutstandingPrincipal(principalAmount);
        la.setMonthlyInstallment(loanPaymentSessionBean.calculateMonthlyInstallment(la));
        la.setLoanAccountStatus(EnumUtils.LoanAccountStatus.PENDING);
        
        LoanAccount result = loanAccountBean.createLoanAccount(la);
        if (result != null) {
            ma.addLoanAccount(la);
            mainAccountBean.updateMainAccount(ma);
            MessageUtils.displayInfo("Loan Account Created!");
        } else {
            MessageUtils.displayError("Loan Account Not Created!");
        }
    }
    
    private void createOhterAccounts(LoanApplication la) {
        MainAccount ma = null;
        try {
            ma = loginBean.getMainAccountByUserIC(la.getIdNumber());
        } catch (Exception e) {
            System.out.println("Main Account not found, creating new..");
        }
        
        if (ma == null) {
            // new customer
            MainAccount mainAccount = new MainAccount();
            mainAccount.setStatus(EnumUtils.StatusType.PENDING);
            mainAccount.setUserID(generateUserID(EnumUtils.IdentityType.NRIC, la.getIdNumber()));
            String randomPwd = PincodeGenerationUtils.generatePwd();
            mainAccount.setPassword(randomPwd);
            mainAccount = mainAccountSessionBean.createMainAccount(mainAccount);
            mainAccountId = mainAccount.getUserID();

            Customer customer = new Customer();
            customer.setIncome(EnumUtils.Income.getEnumFromNumber(la.getIncome()));
            customer.setActualIncome(la.getIncome());
            customer.setMainAccount(mainAccount);
            newCustomerBean.createCustomer(customer);

            CustomerDepositAccount depostiAccount = new CustomerDepositAccount();
            depostiAccount.setMainAccount(mainAccount);
            depostiAccount.setType(EnumUtils.DepositAccountType.CURRENT);
            depostiAccount.setProduct(depositProductBean.getDepositProductByName(ConstantUtils.DEMO_CURRENT_DEPOSIT_PRODUCT_NAME));
            depositAccountBean.createAccount(depostiAccount);
            
            la.setMainAccount(mainAccount);
            la.setStatus(EnumUtils.LoanAccountStatus.PENDING);
            loanAccountBean.updateLoanApplication(la);
            
            mainAccount.setCustomer(customer);
            mainAccountBean.updateMainAccount(ma);
        } else {
            mainAccountId = ma.getUserID();
            
            la.setMainAccount(ma);
            la.setStatus(EnumUtils.LoanAccountStatus.PENDING);
            loanAccountBean.updateLoanApplication(la);
            
            Customer customer = ma.getCustomer();
            customer.setIncome(EnumUtils.Income.getEnumFromNumber(la.getIncome()));
            customer.setActualIncome(la.getIncome());
            newCustomerBean.updateCustomer(customer);
            
            depositAccountBean.getDaytoDayAccountByMainAccount(ma);
            
            ma.setCustomer(customer);
            mainAccountBean.updateMainAccount(ma);
        }
    }
    
    private String generateUserID(EnumUtils.IdentityType identityType, String identityNum) {

        if (identityType.equals(EnumUtils.IdentityType.NRIC)) {
            return "c" + identityNum.substring(1, identityNum.length() - 1);
        } else {
            return "error";
        }
    }

    /**
     * @return the loanProducts
     */
    public List<LoanProduct> getLoanProducts() {
        return loanProducts;
    }

    /**
     * @param loanProducts the loanProducts to set
     */
    public void setLoanProducts(List<LoanProduct> loanProducts) {
        this.loanProducts = loanProducts;
    }

    /**
     * @return the selectedLoanProductId
     */
    public Long getSelectedLoanProductId() {
        return selectedLoanProductId;
    }

    /**
     * @param selectedLoanProductId the selectedLoanProductId to set
     */
    public void setSelectedLoanProductId(Long selectedLoanProductId) {
        this.selectedLoanProductId = selectedLoanProductId;
    }

    /**
     * @return the paymentStartDate
     */
    public Date getPaymentStartDate() {
        return paymentStartDate;
    }

    /**
     * @param paymentStartDate the paymentStartDate to set
     */
    public void setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    /**
     * @return the principalAmount
     */
    public Double getPrincipalAmount() {
        return principalAmount;
    }

    /**
     * @param principalAmount the principalAmount to set
     */
    public void setPrincipalAmount(Double principalAmount) {
        this.principalAmount = principalAmount;
    }

    /**
     * @return the mainAccountId
     */
    public String getMainAccountId() {
        return mainAccountId;
    }

    /**
     * @param mainAccountId the mainAccountId to set
     */
    public void setMainAccountId(String mainAccountId) {
        this.mainAccountId = mainAccountId;
    }

    /**
     * @return the applicationId
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @return the currentApplication
     */
    public LoanApplication getCurrentApplication() {
        return currentApplication;
    }

    /**
     * @param currentApplication the currentApplication to set
     */
    public void setCurrentApplication(LoanApplication currentApplication) {
        this.currentApplication = currentApplication;
    }

}
