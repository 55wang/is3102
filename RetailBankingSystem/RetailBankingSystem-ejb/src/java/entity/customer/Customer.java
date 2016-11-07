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
import server.utilities.EnumUtils.PortfolioStatus;
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
    private Integer age;
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
    private Double actualIncome = 0.0;
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

    private Long depositRecency = 0L;
    private Long depositFrequency = 0L;
    private Long depositMonetary = 0L;

    private Long cardRecency = 0L;
    private Long cardFrequency = 0L;
    private Long cardMonetary = 0L;
            
    private String hashTag = "";

    // mapping
    @OneToOne(cascade = {CascadeType.MERGE})
    private MainAccount mainAccount;
    @ManyToOne
    private CustomerGroup customerGroup;

    public Long calcAge() {
        return ((new Date().getTime() - getBirthDay().getTime()) / (24 * 60 * 60 * 1000)) / 365;
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
        try {
            List<LoanAccount> las = getMainAccount().getLoanAccounts();

            totalMortgageMonthlyInstallment = 0.0;
            for (LoanAccount la : las) {
                if (la.getLoanProduct().getProductType().equals(LoanProductType.LOAN_PRODUCT_TYPE_HDB) || la.getLoanProduct().getProductType().equals(LoanProductType.LOAN_PRODUCT_TYPE_HDB)) {
                    totalMortgageMonthlyInstallment += la.getMonthlyInstallment();
                }
            }
            return round(totalMortgageMonthlyInstallment, 1);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalMonthlyInstallment() {
        try {
            List<LoanAccount> las = getMainAccount().getLoanAccounts();

            totalMonthlyInstallment = 0.0;
            for (LoanAccount la : las) {
                totalMonthlyInstallment += la.getMonthlyInstallment();
            }
            return round(totalMonthlyInstallment, 1);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalAsset() {
        try {
            totalAsset = getMainAccount().getWealthManagementSubscriber().getTotalPortfolioValue()
                    + getTotalDepositAmount().doubleValue()
                    + getTotalDebtAmount();
            return totalAsset;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getSavingToIncome() {
        try {
            savingToIncome = getSavingPerMonth() / getIncome().getAvgValue();
            return savingToIncome;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getDebtToIncome() {
        try {
            debtToIncome = getTotalMonthlyInstallment() / getIncome().getAvgValue();
            return debtToIncome;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getHousingCostRatio() {
        try {
            housingCostRatio = getTotalMortgageMonthlyInstallment() / getIncome().getAvgValue();
            return housingCostRatio;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getDebtRatio() {
        try {
            debtRatio = getTotalDebtAmount() / getTotalAsset();
            return debtRatio;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getNetWorth() {
        try {
            netWorth = getTotalAsset() - getTotalDebtAmount();
            return netWorth;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public BigDecimal getTotalDepositAmount() {
        try {
            List<DepositAccount> das = getMainAccount().getBankAcounts();
            totalDepositAmount = new BigDecimal(0);
            for (DepositAccount da : das) {
                totalDepositAmount = totalDepositAmount.add(da.getBalance());
            }
            return totalDepositAmount;
        } catch (Exception ex) {
            return new BigDecimal(0);
        }
    }

    public Double getTotalDebtAmount() {
        try {
            totalDebtAmount = getTotalLoanAmount() + getTotalCreditAmount();
            return totalDebtAmount;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalLoanAmount() {
        try {
            List<LoanAccount> las = getMainAccount().getLoanAccounts();

            totalLoanAmount = 0.0;
            for (LoanAccount la : las) {
                totalLoanAmount += la.getOutstandingPrincipal();
            }
            return totalLoanAmount;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalCreditAmount() {
        try {
            List<CreditCardAccount> ccas = getMainAccount().getCreditCardAccounts();

            totalCreditAmount = 0.0;
            for (CreditCardAccount cca : ccas) {
                totalCreditAmount += cca.getOutstandingAmount();
            }
            return totalCreditAmount;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalPortfolioCurrentValue() { //all the portfolio
        try {
            List<Portfolio> ps = getMainAccount().getWealthManagementSubscriber().getPortfolios();
            totalPortfolioCurrentValue = 0.0;
            for (Portfolio p : ps) {
                if (p.getStatus() == PortfolioStatus.BOUGHT) {
                    totalPortfolioCurrentValue += p.getTotalCurrentValue();
                }
            }

            System.out.println("getTOtalportfoliovalue: " + totalPortfolioCurrentValue);
            return totalPortfolioCurrentValue;
        } catch (Exception ex) {
            System.out.println("getTOtalportfoliovalue: " + ex);
            return 0.0;
        }
    }

    public Double getTotalPortfolioBuyingValue() {
        try {
            List<Portfolio> ports = getMainAccount().getWealthManagementSubscriber().getPortfolios();
            totalPortfolioBuyingValue = 0.0;
            for (Portfolio port : ports) {
                if (port.getStatus() == PortfolioStatus.BOUGHT) {
                    totalPortfolioBuyingValue += port.getTotalBuyingValue();
                }
            }
            return totalPortfolioBuyingValue;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getPortfolioPercentageChange() {
        try {
            portfolioPercentageChange = getTotalPortfolioCurrentValue() / getTotalPortfolioBuyingValue() * 100;
            return portfolioPercentageChange;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getFinancialHealthScore() {
        try {
            financialHealthScore = 85.0;
            if (getSavingToIncome() >= 10 && getSavingToIncome() <= 20) {
                financialHealthScore += 5;
            } else if (getSavingToIncome() < 10) {
                //too little saving
                financialHealthScore -= 20;
            } else if (getSavingToIncome() > 20) {
                //too much saving
                financialHealthScore -= 5;
            }

            if (getHousingCostRatio() >= 28) {
                financialHealthScore -= 20;
            } else {
                financialHealthScore += 5;
            }

            if (getDebtRatio() >= 36) {
                financialHealthScore -= 20;
            } else {
                financialHealthScore += 5;
            }

            if (getAge() <= 22 && getNetWorth() > 0.0) {
                financialHealthScore += 5;
            } else if (getAge() <= 25 && getNetWorth() > 50000.0) {
                financialHealthScore += 10;
            } else if (getAge() <= 30 && getNetWorth() > 150000.0) {
                financialHealthScore += 10;
            } else if (getAge() <= 35 && getNetWorth() > 250000.0) {
                financialHealthScore += 10;
            } else if (getAge() <= 40 && getNetWorth() > 400000.0) {
                financialHealthScore += 15;
            } else if (getAge() <= 45 && getNetWorth() > 600000.0) {
                financialHealthScore += 15;
            } else if (getAge() <= 50 && getNetWorth() > 850000.0) {
                financialHealthScore += 15;
            } else if (getAge() <= 55 && getNetWorth() > 1000000.0) {
                financialHealthScore += 15;
            } else if (getAge() <= 60 && getNetWorth() > 1500000.0) {
                financialHealthScore += 15;
            } else if (getAge() >= 60 && getNetWorth() > 2000000.0) {
                financialHealthScore += 15;
            } else {
                financialHealthScore -= 20;
            }

            return financialHealthScore;
        } catch (Exception ex) {
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
        try {
            if (getFinancialHealthScore() >= 80) {
                financialHealthScoreLevel = EnumUtils.FinancialHealthLevel.VERYHEALTHY.getValue();
            } else if (getFinancialHealthScore() >= 60 && getFinancialHealthScore() < 80) {
                financialHealthScoreLevel = EnumUtils.FinancialHealthLevel.HEALTHY.getValue();
            } else if (getFinancialHealthScore() >= 40 && getFinancialHealthScore() < 60) {
                financialHealthScoreLevel = EnumUtils.FinancialHealthLevel.UNHEALTHY.getValue();
            } else {
                financialHealthScoreLevel = EnumUtils.FinancialHealthLevel.VERYUNHEALTHY.getValue();
            }
            return financialHealthScoreLevel;
        } catch (Exception ex) {
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

    public Long getDepositRecency() {
        return depositRecency;
    }

    public void setDepositRecency(Long depositRecency) {
        this.depositRecency = depositRecency;
    }

    public Long getDepositFrequency() {
        return depositFrequency;
    }

    public void setDepositFrequency(Long depositFrequency) {
        this.depositFrequency = depositFrequency;
    }

    public Long getDepositMonetary() {
        return depositMonetary;
    }

    public void setDepositMonetary(Long depositMonetary) {
        this.depositMonetary = depositMonetary;
    }

    public Long getCardRecency() {
        return cardRecency;
    }

    public void setCardRecency(Long cardRecency) {
        this.cardRecency = cardRecency;
    }

    public Long getCardFrequency() {
        return cardFrequency;
    }

    public void setCardFrequency(Long cardFrequency) {
        this.cardFrequency = cardFrequency;
    }

    public Long getCardMonetary() {
        return cardMonetary;
    }

    public void setCardMonetary(Long cardMonetary) {
        this.cardMonetary = cardMonetary;
    }
    //1
    public Long getDepositRFMScore() {
        Long recency = depositRecency * 100;
        Long frequency = depositFrequency * 10;
        Long monetary = depositMonetary * 1;
        return recency + frequency + monetary;
    }
    //2
    public Long getCardRFMScore() {
        Long recency = cardRecency * 100;
        Long frequency = cardFrequency * 10;
        Long monetary = cardMonetary * 1;
        return recency + frequency + monetary;
    }
    //3
    public Long getDepositRMFScore() {
        Long recency = depositRecency * 100;
        Long frequency = depositFrequency * 1;
        Long monetary = depositMonetary * 10;
        return recency + frequency + monetary;
    }
    //4
    public Long getCardRMFScore() {
        Long recency = cardRecency * 100;
        Long frequency = cardFrequency * 1;
        Long monetary = cardMonetary * 10;
        return recency + frequency + monetary;
    }
    //5
    public Long getDepositFRMScore() {
        Long recency = depositRecency * 10;
        Long frequency = depositFrequency * 100;
        Long monetary = depositMonetary * 1;
        return recency + frequency + monetary;
    }
    //6
    public Long getCardFRMScore() {
        Long recency = cardRecency * 10;
        Long frequency = cardFrequency * 100;
        Long monetary = cardMonetary * 1;
        return recency + frequency + monetary;
    }
    //7
    public Long getDepositFMRScore() {
        Long recency = depositRecency * 1;
        Long frequency = depositFrequency * 100;
        Long monetary = depositMonetary * 10;
        return recency + frequency + monetary;
    }
    //8
    public Long getCardFMRScore() {
        Long recency = depositRecency * 1;
        Long frequency = depositFrequency * 100;
        Long monetary = depositMonetary * 10;
        return recency + frequency + monetary;
    }
    //9
    public Long getDepositMRFScore() {
        Long recency = depositRecency * 10;
        Long frequency = depositFrequency * 1;
        Long monetary = depositMonetary * 100;
        return recency + frequency + monetary;
    }
    //10
    public Long getCardMRFScore() {
        Long recency = depositRecency * 10;
        Long frequency = depositFrequency * 1;
        Long monetary = depositMonetary * 100;
        return recency + frequency + monetary;
    }
    //11
    public Long getDepositMFRScore() {
        Long recency = depositRecency * 1;
        Long frequency = depositFrequency * 10;
        Long monetary = depositMonetary * 100;
        return recency + frequency + monetary;
    }
    //12
    public Long getCardMFRScore() {
        Long recency = depositRecency * 1;
        Long frequency = depositFrequency * 10;
        Long monetary = depositMonetary * 100;
        return recency + frequency + monetary;
    }

    public Integer getAge() {
        System.out.println("age: " + age);
        System.out.println(calcAge());
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag += hashTag;
    }

}
