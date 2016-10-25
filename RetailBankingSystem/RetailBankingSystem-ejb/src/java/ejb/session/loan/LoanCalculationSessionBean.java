/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import javax.ejb.Stateless;

/**
 *
 * @author leiyang
 */
@Stateless
public class LoanCalculationSessionBean implements LoanCalculationSessionBeanLocal {

    public final static Integer MAX_LOAN_AGE = 65;
    public final static Integer MIN_LOAN_AGE = 21;
    public final static Integer MIDDLE_HDB_LOAN_AGE = 40;
    public final static Integer MIDDLE_PP_LOAN_AGE = 35;
    public final static Integer MAX_HDB_LOAN_TENURE = 25;
    public final static Integer MAX_PP_LOAN_TENURE = 30;
    public final static Double MSR = 0.3;
    public final static Double TDSR = 0.6;
    public final static Double MEDIUM_INTEREST = 0.035;

    @Override
    public Double calculateMonthlyInstallment(Double monthlyLoanInterest, Integer tenure, Double loanAmt) {
        return loanAmt * monthlyLoanInterest / (1 - Math.pow((1 + monthlyLoanInterest), -tenure));
    }

    @Override
    public Integer calculateMaxHDBTenure(Integer age, Double monthlyIncome) {
        if (monthlyIncome < 1500) {
            return 0;
        } else {

            if (age < MIN_LOAN_AGE) {
                return 0;
            } else if (age > MAX_LOAN_AGE) {
                return 0;
            } else if (age >= MIN_LOAN_AGE && age <= MIDDLE_HDB_LOAN_AGE) {
                return MAX_HDB_LOAN_TENURE;
            } else {
                return MAX_HDB_LOAN_TENURE - (age - MIDDLE_HDB_LOAN_AGE);
            }
        }
    }

    @Override // checked
    public Integer calculateMaxPPTenure(Integer age, Double monthlyIncome) {
        if (monthlyIncome < 1500) {
            return 0;
        } else {
            if (age < MIN_LOAN_AGE) {
                return 0;
            } else if (age > MAX_LOAN_AGE) {
                return 0;
            } else if (age >= MIN_LOAN_AGE && age <= MIDDLE_PP_LOAN_AGE) {
                return MAX_PP_LOAN_TENURE;
            } else {
                return MAX_PP_LOAN_TENURE - (age - MIDDLE_PP_LOAN_AGE);
            }
        }
    }

    @Override
    public Double calculateMaxHDBLoanAmt(Double monthlyIncome, Integer age, Double otherHDBLoan, Double otherLoan) {
        Integer tenure = this.calculateMaxHDBTenure(age, monthlyIncome);
        Double maxHDBMonthlyPayment = MSR * monthlyIncome;
        Double maxTotalMonthlyPayment = TDSR * monthlyIncome;
        Double monthlyInstallment;
        Double maxHDBLoanAmt;
        if (monthlyIncome < 1500) {
            return 0.0;
        } else {
            if (maxHDBMonthlyPayment <= otherHDBLoan || maxTotalMonthlyPayment <= (otherHDBLoan + otherLoan)) {
                return 0.0;
            } else {
                monthlyInstallment = maxHDBMonthlyPayment - otherHDBLoan;
                maxHDBLoanAmt = monthlyInstallment * MEDIUM_INTEREST / (1 - Math.pow((1 + MEDIUM_INTEREST), -tenure));
                return maxHDBLoanAmt;
            }
        }
    }

    @Override
    public Double calculateMaxPPLoanAmt(Double monthlyIncome, Integer age, Double otherHomeLoan, Double otherLoan) {
        Integer tenure = this.calculateMaxPPTenure(age, monthlyIncome);
        Double maxTotalMonthlyPayment = TDSR * monthlyIncome;
        Double monthlyInstallment;
        Double maxPPLoanAmt;
        if (monthlyIncome < 1500) {
            return 0.0;
        } else {
            if (maxTotalMonthlyPayment <= (otherHomeLoan + otherLoan)) {
                return 0.0;
            } else {
                monthlyInstallment = maxTotalMonthlyPayment - otherHomeLoan - otherLoan;
                maxPPLoanAmt = monthlyInstallment * (1 - Math.pow((1 + MEDIUM_INTEREST), -tenure)) / MEDIUM_INTEREST;
                return maxPPLoanAmt;
            }
        }
    }

    @Override
    public Double calculateCarLoanAmt(Integer tenure, Double monthlyInterest, Double monthlyIncome, Integer age, Double otherLoan) {
        Double maxTotalMonthlyPayment = TDSR * monthlyIncome;
        Double monthlyInstallment;
        Double maxCarLoanAmt;
        if (monthlyIncome < 2000) {
            return 0.0;
        } else {
            if (maxTotalMonthlyPayment <= otherLoan) {
                return 0.0;
            } else {
                monthlyInstallment = maxTotalMonthlyPayment - otherLoan;
                maxCarLoanAmt = monthlyInstallment * (1 - Math.pow((1 + monthlyInterest), -tenure)) / monthlyInterest;
                return maxCarLoanAmt;
            }

        }
    }

    @Override
    public Double calculateMaxHDBMonthlyInstalment(Double monthlyIncome, Double otherLoan) {
        Double maxHDBMonthlyPayment1 = MSR * monthlyIncome;
        Double maxTotalMonthlyPayment = TDSR * monthlyIncome;
        Double maxHDBMonthlyPayment2 = maxTotalMonthlyPayment - otherLoan;
        if (maxHDBMonthlyPayment2 <= 0) {
            return 0.0;
        }
        Double maxMonthlyPayment;
        if (maxHDBMonthlyPayment1 <= maxHDBMonthlyPayment2) {
            maxMonthlyPayment = maxHDBMonthlyPayment1;
        } else {
            maxMonthlyPayment = maxHDBMonthlyPayment2;
        }
        return maxMonthlyPayment;
    }

    @Override
    public Double calculateMaxPPMonthlyInstalment(Double monthlyIncome, Double otherLoan) {
        Double maxMonthlyPayment = TDSR * monthlyIncome;
        Double monthlyInstalment = maxMonthlyPayment - otherLoan;
        if (monthlyInstalment <= 0.0) {
            return 0.0;
        }

        return monthlyInstalment;
    }

    @Override
    public Double calculateMaxHomeLoanAmt(Double monthlyInstalment, Integer tenure) {
        Double maxHDBLoanAmt = monthlyInstalment * (1 - Math.pow((1 + MEDIUM_INTEREST), -tenure * 12)) / MEDIUM_INTEREST;
        return maxHDBLoanAmt;

    }

    @Override
    public Double homeMarketValue(Double loanAmt, Integer numberOfHousingLoan) {
        Double LTV;
        Double homeMarketValue = 0.0;
        if (numberOfHousingLoan == 0) {
            LTV = 0.8;
            homeMarketValue = loanAmt / LTV;

        } else if (numberOfHousingLoan == 1) {
            LTV = 0.5;
            homeMarketValue = loanAmt / LTV;

        } else if (numberOfHousingLoan >= 2) {
            LTV = 0.4;
            homeMarketValue = loanAmt / LTV;

        }
        return homeMarketValue;
    }

    @Override
    public Double calculateMaxCarLoanAmt(Double openMarketValue) {
        Double maxCarLoanAmt = 0.0;
        if (openMarketValue > 20000.0) {
            maxCarLoanAmt = 0.6 * openMarketValue;
        } else if (openMarketValue <= 20000.0) {
            maxCarLoanAmt = 0.7 * openMarketValue;
        }
        return maxCarLoanAmt;
    }

    @Override
    public Double calculateCarMonthlyInstalment(Double annualInterest, Integer tenure, Double loanAmt) {
        Double interest = loanAmt * annualInterest * tenure;
        Double monthlyInstalment = (interest + loanAmt) / tenure / 12;
        return monthlyInstalment;
    }

    @Override
    public Double calculateMaxPersonalLoanAmt(Double monthlyIncome, Double otherMonthlyCommitment) {
        if (monthlyIncome - otherMonthlyCommitment < 0) {
            return 0.0;
        }
        return 24 * (monthlyIncome - otherMonthlyCommitment);
    }
    
    @Override
    public Double calculatePersonalMonthlyInstalment(Double annualInterest, Integer tenure, Double loanAmt) {
        Double interest = loanAmt * annualInterest * tenure;
        Double monthlyInstalment = (interest + loanAmt) / tenure / 12;
        return monthlyInstalment;
    }
}
