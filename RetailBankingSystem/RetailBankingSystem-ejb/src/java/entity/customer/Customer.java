/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.customer;

import entity.card.account.CreditCardAccount;
import entity.crm.CustomerGroup;
import entity.dams.account.DepositAccount;
import entity.loan.LoanAccount;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.OneToOne;
import static server.utilities.CommonUtils.round;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.Education;
import server.utilities.EnumUtils.EmploymentStatus;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.IdentityType;
import server.utilities.EnumUtils.Income;
import server.utilities.EnumUtils.Industry;
import server.utilities.EnumUtils.LoanProductType;
import server.utilities.EnumUtils.MaritalStatus;
import server.utilities.EnumUtils.Nationality;
import server.utilities.EnumUtils.ResidentialStatus;
import server.utilities.EnumUtils.ResidentialType;

@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //personal info
    private IdentityType identityType;
    @Column(unique = true)
    private String identityNumber;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDay;
    private String firstname;
    private String lastname;
    private String fullName;
    private String phone;
    @Column(unique = true)
    private String email;

    // general info
    private Nationality nationality;
    private MaritalStatus maritalStatus;
    private String address;
    private String postalCode;
    private Industry industry;
    private Education education;
    private ResidentialStatus residentialStatus;
    private ResidentialType residentialType;
    private EmploymentStatus employmentStatus;
    private Income income;
    private Double actualIncome;
    private Double savingPerMonth;
    private Gender gender;

    // credit
    private Double creditScore;
    private String BureaCreditScore;
    
    // portfolioInfomation
    private Double totalMortgageMonthlyInstallment;
    private Double totalMonthlyInstallment;
    private Double totalAsset;
    private Double savingToIncome;
    private Double debtToIncome;
    private Double housingCostRatio;
    private Double debtRatio;
    private Double netWorth;
    private BigDecimal totalDepositAmount;
    private Double totalDebtAmount;
    private Double totalLoanAmount;
    private Double totalCreditAmount;
    private Double totalPortfolioCurrentValue;
    private Double totalPortfolioBuyingValue;
    private Double portfolioPercentageChange;
    private Double financialHealthScore;
    private String financialHealthScoreLevel;
    
    // mapping
    @OneToOne(cascade = {CascadeType.MERGE})
    private MainAccount mainAccount;
    @ManyToOne
    private CustomerGroup customerGroup;

    public Integer getAge() {
        Integer age = (int) (System.currentTimeMillis() - getBirthDay().getTime()) / (24 * 60 * 60 * 1000);
        return age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + id + " ]";
    }
    
    public Double getTotalMortgageMonthlyInstallment() {
        try{
            List<LoanAccount> las = getMainAccount().getLoanAccounts();

            Double totalBalance = 0.0;
            for (LoanAccount la : las) {
                if (la.getLoanProduct().getProductType().equals(LoanProductType.LOAN_PRODUCT_TYPE_HDB) || la.getLoanProduct().getProductType().equals(LoanProductType.LOAN_PRODUCT_TYPE_HDB)) {
                    totalBalance += la.getMonthlyInstallment();
                }
            }
            return round(totalBalance, 1);
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getTotalMonthlyInstallment() {
        try{
            List<LoanAccount> las = getMainAccount().getLoanAccounts();

            Double totalBalance = 0.0;
            for (LoanAccount la : las) {
                totalBalance += la.getMonthlyInstallment();
            }
            return round(totalBalance, 1);
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getTotalAsset() {
        try{
            return getMainAccount().getWealthManagementSubscriber().getTotalPortfolioValue()
                    + getTotalDepositAmount().doubleValue()
                    + getTotalDebtAmount();
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getSavingToIncome() {
        try{
            return getSavingPerMonth() / getIncome().getAvgValue();
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getDebtToIncome() {
        try{
            return getTotalMonthlyInstallment()/getIncome().getAvgValue();
        }
        catch(Exception ex){
            return 0.0;
        }
    }

    public Double getHousingCostRatio() {
        try{
            return getTotalMortgageMonthlyInstallment() / getIncome().getAvgValue();
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getDebtRatio() {
        try{
            return getTotalDebtAmount() / getTotalAsset();
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getNetWorth() {
        try{
            return getTotalAsset() - getTotalDebtAmount();
        }catch(Exception ex){
            return 0.0;
        }
    }
    
    public BigDecimal getTotalDepositAmount() {
        try{
            List<DepositAccount> das = getMainAccount().getBankAcounts();
            BigDecimal totalBalance = new BigDecimal(0);
            for (DepositAccount da : das) {
                totalBalance = totalBalance.add(da.getBalance());
            }
            return totalBalance;
        }catch(Exception ex){
            return new BigDecimal(0);
        }
    }

    public Double getTotalDebtAmount() {
        try{
            return getTotalLoanAmount() + getTotalCreditAmount();
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getTotalLoanAmount() {
        try{
            List<LoanAccount> las = getMainAccount().getLoanAccounts();

            Double totalBalance = 0.0;
            for (LoanAccount la : las) {
                totalBalance += la.getOutstandingPrincipal();
            }
            return totalBalance;
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getTotalCreditAmount() {
        try{
            List<CreditCardAccount> ccas = getMainAccount().getCreditCardAccounts();

            Double totalBalance = 0.0;
            for (CreditCardAccount cca : ccas) {
                totalBalance += cca.getOutstandingAmount();
            }
            return totalBalance;
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getTotalPortfolioCurrentValue() { //all the portfolio
        try{
            List<Portfolio> ps = getMainAccount().getWealthManagementSubscriber().getPortfolios();
            Double totalCurrentPortfoliosValue = 0.0;
            for (Portfolio p : ps) {
                totalCurrentPortfoliosValue += p.getTotalCurrentValue();
            }
            return totalCurrentPortfoliosValue;
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getTotalPortfolioBuyingValue() {
        try{
            List<Portfolio> ports = getMainAccount().getWealthManagementSubscriber().getPortfolios();
            Double totalValue = 0.0;
            for (Portfolio port : ports) {
                totalValue += port.getTotalBuyingValue();
            }
            return totalValue;
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getPortfolioPercentageChange() {
        try{
            return getTotalPortfolioCurrentValue() / getTotalPortfolioBuyingValue() * 100;
        }catch(Exception ex){
            return 0.0;
        }
    }

    public Double getFinancialHealthScore() {
        try{
            Double score = 100.0;
            if (getSavingToIncome() >= 10 && getSavingToIncome() <= 20) {
                score += 5;
            } else if (getSavingToIncome() < 10) {
                //too little saving
                score -= 20;
            } else if (getSavingToIncome() > 20) {
                //too much saving
                score -= 5;
            }

            if (getHousingCostRatio() >= 28) {
                score -= 20;
            } else {
                score += 5;
            }

            if (getDebtRatio() >= 36) {
                score -= 20;
            } else {
                score += 5;
            }

            if (getAge() <= 22 && getNetWorth() > 0.0) {
                score += 5;
            } else if (getAge() <= 25 && getNetWorth() > 50000.0) {
                score += 20;
            } else if (getAge() <= 30 && getNetWorth() > 150000.0) {
                score += 20;
            } else if (getAge() <= 35 && getNetWorth() > 250000.0) {
                score += 20;
            } else if (getAge() <= 40 && getNetWorth() > 400000.0) {
                score += 20;
            } else if (getAge() <= 45 && getNetWorth() > 600000.0) {
                score += 20;
            } else if (getAge() <= 50 && getNetWorth() > 850000.0) {
                score += 20;
            } else if (getAge() <= 55 && getNetWorth() > 1000000.0) {
                score += 20;
            } else if (getAge() <= 60 && getNetWorth() > 1500000.0) {
                score += 20;
            } else if (getAge() >= 60 && getNetWorth() > 2000000.0) {
                score += 20;
            } else {
                score -= 20;
            }

            return score;
        }catch(Exception ex){
            return 0.0;
        }
        /*
         http://www.thefrugaltoad.com/personalfinance/personal-financial-ratios-everyone-should-know
        
         Savings to Income (S to I) Savings Ratio: Should be between 10% to 20%.
        
         Debt to Income (D to I) Consumer Debt Ratio: Should not exceed 20%.
         Calculated by dividing total monthly loan payments by monthly income.
        
         Housing Cost Ratio:  Should not exceed 28% of gross income.  
         = Total of monthly mortgage payment (principal + interest) / the gross monthly income
        
         Total Debt Ratio: Should not exceed 36% of gross income.  
         = total Debt / Total Asset
         
        
         networth = asset - liabilities
         http://www.financialsamurai.com/the-average-net-worth-for-the-above-average-person/
         */
    }

    public void setTotalMortgageMonthlyInstallment() {
        this.totalMortgageMonthlyInstallment = getTotalMortgageMonthlyInstallment();
    }

    public void setTotalMonthlyInstallment() {
        this.totalMonthlyInstallment = getTotalMonthlyInstallment();
    }

    public void setTotalAsset() {
        this.totalAsset = getTotalAsset();
    }

    public void setSavingToIncome() {
        this.savingToIncome = getSavingToIncome();
    }

    public void setDebtToIncome() {
        this.debtToIncome = getDebtToIncome();
    }

    public void setHousingCostRatio() {
        this.housingCostRatio = getHousingCostRatio();
    }

    public void setDebtRatio() {
        this.debtRatio = getDebtRatio();
    }

    public void setNetWorth() {
        this.netWorth = getNetWorth();
    }

    public void setTotalDepositAmount() {
        this.totalDepositAmount = getTotalDepositAmount();
    }

    public void setTotalDebtAmount() {
        this.totalDebtAmount = getTotalDebtAmount();
    }

    public void setTotalLoanAmount() {
        this.totalLoanAmount = getTotalLoanAmount();
    }

    public void setTotalCreditAmount() {
        this.totalCreditAmount = getTotalCreditAmount();
    }

    public void setTotalPortfolioCurrentValue() {
        this.totalPortfolioCurrentValue = getTotalPortfolioCurrentValue();
    }

    public void setTotalPortfolioBuyingValue() {
        this.totalPortfolioBuyingValue = getTotalPortfolioBuyingValue();
    }

    public void setPortfolioPercentageChange() {
        this.portfolioPercentageChange = getPortfolioPercentageChange();
    }

    public void setFinancialHealthScore() {
        this.financialHealthScore = getFinancialHealthScore();
    }

    public void setFinancialHealthScoreLevel() {
        this.financialHealthScoreLevel = getFinancialHealthScoreLevel();
    }

    public String getFinancialHealthScoreLevel() {
        try{
            if (getFinancialHealthScore() >= 80) {
                return EnumUtils.FinancialHealthLevel.VERYHEALTHY.getValue();
            } else if (getFinancialHealthScore() >= 60 && getFinancialHealthScore() < 80) {
                return EnumUtils.FinancialHealthLevel.HEALTHY.getValue();
            } else if (getFinancialHealthScore() >= 40 && getFinancialHealthScore() < 60) {
                return EnumUtils.FinancialHealthLevel.UNHEALTHY.getValue();
            } else {
                return EnumUtils.FinancialHealthLevel.VERYUNHEALTHY.getValue();
            }
        }catch(Exception ex){
            return "Not Applicable";
        }
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public Double getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Double creditScore) {
        this.creditScore = creditScore;
    }

    /**
     * @return the maritalStatus
     */
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @param maritalStatus the maritalStatus to set
     */
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * @return the nationality
     */
    public Nationality getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the education
     */
    public Education getEducation() {
        return education;
    }

    /**
     * @param education the education to set
     */
    public void setEducation(Education education) {
        this.education = education;
    }

    /**
     * @return the income
     */
    public Income getIncome() {
        return income;
    }

    /**
     * @param income the income to set
     */
    public void setIncome(Income income) {
        this.income = income;
    }

    /**
     * @return the birthDay
     */
    public Date getBirthDay() {
        return birthDay;
    }

    /**
     * @param birthDay the birthDay to set
     */
    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public ResidentialStatus getResidentialStatus() {
        return residentialStatus;
    }

    public void setResidentialStatus(ResidentialStatus residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public ResidentialType getResidentialType() {
        return residentialType;
    }

    public void setResidentialType(ResidentialType residentialType) {
        this.residentialType = residentialType;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getBureaCreditScore() {
        return BureaCreditScore;
    }

    public void setBureaCreditScore(String BureaCreditScore) {
        this.BureaCreditScore = BureaCreditScore;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public CustomerGroup getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroup = customerGroup;
    }

    /**
     * @return the actualIncome
     */
    public Double getActualIncome() {
        return actualIncome;
    }

    /**
     * @param actualIncome the actualIncome to set
     */
    public void setActualIncome(Double actualIncome) {
        this.actualIncome = actualIncome;
    }
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Customer() {
    }

    public Double getSavingPerMonth() {
        return savingPerMonth;
    }

    public void setSavingPerMonth(Double savingPerMonth) {
        this.savingPerMonth = savingPerMonth;
    }

}
