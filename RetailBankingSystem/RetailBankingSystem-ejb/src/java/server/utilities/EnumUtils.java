/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leiyang
 */
// REMARK: To get the list of enum values, just use e.g. CreditType.values()
public class EnumUtils {

    // LY: depends on the depth, we can further categorize them
    // e.g. Cashback on Purchases or Payments
    // discounted partner merchants
    public enum CreditType {

        MILE("MILE"),
        REWARD("REWARD"),
        CASHBACK("CASHBACK");
        private String value;

        CreditType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CreditType getEnum(String value) {
            for (CreditType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // LY: Can this conbimed with Status?
    public enum CardAccountStatus {

        PENDING("PENDING"),
        ACTIVE("ACTIVE"),
        FREEZE("FREEZE"),
        CLOSED("CLOSED");
        
        private String value;

        CardAccountStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CardAccountStatus getEnum(String value) {
            for (CardAccountStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum StatusType {

        ACTIVE("ACTIVE"),
        PENDING("PENDING"),
        FREEZE("FREEZE"),
        CLOSED("CLOSED");

        private String value;

        StatusType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static StatusType getEnum(String value) {
            for (StatusType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum ApplicationStatus {

        PENDING("PENDING"),
        EDITABLE("EDITABLE"),
        REJECT("REJECT"),
        APPROVED("APPROVED"),
        CANCELLED("CANCELLED");
        
        private String value;

        ApplicationStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static ApplicationStatus getEnum(String value) {
            for (ApplicationStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // Customer
    public enum Position {

        SENIOR_MANAGEMENT("SENIOR_MANAGEMENT"),
        PROFESSIONAL("PROFESSIONAL"),
        MANAGERIAL("MANAGERIAL"),
        EXECUTIVE("EXECUTIVE"),
        SALES("SALES"),
        OTHERS("OTHERS"),
        DIRECTOR("DIRECTOR"),
        SUPERVISOR("SUPERVISOR"),
        TEACHER_LECTURER("TEACHER_LECTURER"),
        DILPOMAT("DILPOMAT");
        
        private String value;

        Position(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Position getEnum(String value) {
            for (Position v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Industry {

        BUILDING_CONSTRUCTION("BUILDING_CONSTRUCTION"),
        BANKING_FINANCE("BANKING_FINANCE"),
        IT_TELCO("IT_TELCO"),
        GOVERNMENT("GOVERNMENT"),
        MANUFACTURING("MANUFACTURING"),
        OTHERS("OTHERS"),
        SHIPPING_TRANSPORT("SHIPPING_TRANSPORT"),
        ENTERTAINMENT("ENTERTAINMENT"),
        HOTEL_RESTAURANT("HOTEL_RESTAURANT"),
        RETAIL("RETAIL"),
        TRAVEL_RELATED("TRAVEL_RELATED");
        
        private String value;

        Industry(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Industry getEnum(String value) {
            for (Industry v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum EmploymentStatus {

        VARIABLE_COMMISSION("VARIABLE_COMMISSION"),
        EMPLOYEE("EMPLOYEE"),
        SELF_EMPLOYED("SELF_EMPLOYED"),
        OTHERS("OTHERS");
        
        private String value;

        EmploymentStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static EmploymentStatus getEnum(String value) {
            for (EmploymentStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }

    }

    public enum EduLevel {

        UNIVERSITY_GRAD("UNIVERSITY_GRAD"),
        DIPLOMA_HOLDER("DIPLOMA_HOLDER"),
        TECHNICAL("TECHNICAL"),
        A_LEVEL("A_LEVEL"),
        SECONDARY("SECONDARY"),
        PRIMARY("PRIMARY"),
        OTHERS("OTHERS");
        
        private String value;

        EduLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static EduLevel getEnum(String value) {
            for (EduLevel v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum ResidentialStatus {

        CONDO_APART("CONDO_APART"),
        HDB("HDB"),
        LANDED("LANDED"),
        OTHERS("OTHERS");
        
        private String value;

        ResidentialStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static ResidentialStatus getEnum(String value) {
            for (ResidentialStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum ResidentialType {

        EMPLOYER("EMPLOYER"),
        MORTGAGED("MORTGAGED"),
        OTHERS("OTHERS"),
        PARENTS("PARENTS"),
        RENTED("RENTED"),
        SELF_OWNED("SELF_OWNED");
        
        private String value;

        ResidentialType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static ResidentialType getEnum(String value) {
            for (ResidentialType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Salutation {

        DR("DR"),
        MDM("MDM"),
        MR("MR"),
        MRS("MRS"),
        MS("MS");
        
        private String value;

        Salutation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Salutation getEnum(String value) {
            for (Salutation v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum IdentityType {

        NRIC("NRIC"),
        PASSPORT("PASSPORT");
        
        private String value;

        IdentityType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static IdentityType getEnum(String value) {
            for (IdentityType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Nationality {

        SINGAPOREAN("SINGAPOREAN"),
        PR("PR"),
        E_PASS("E_PASS"),
        S_PASS("S_PASS");
        
        private String value;

        Nationality(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Nationality getEnum(String value) {
            for (Nationality v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Gender {

        MALE("MALE"),
        FEMALE("FEMALE");

        private String value;

        Gender(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Gender getEnum(String value) {
            for (Gender v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum MartialStatus {

        SINGLE("SINGLE"),
        MARRIED("MARRIED"),
        DIVORCED("DIVORCED"),
        OTHERS("OTHERS");
        
        private String value;

        MartialStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static MartialStatus getEnum(String value) {
            for (MartialStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // Transaction
    public enum TransactionType {

        INITIAL("INITIAL DEPOSIT"),
        DEPOSIT("DEPOSIT"),
        WITHDRAW("WITHDRAW"),
        CHEQUE("CHEQUE"),
        TRANSFER("TRANSFER"),
        LOCALTRANSFER("LOCAL TRANSFER"),
        INTERBANKTRANSFER("INTER BANK TRANSFER"),
        OVERSEASTRANSFER("OVERSEAS TRANSFER"),
        BILL("BILL"),
        CCSPENDING("CCSPENDING"),
        INVEST("INVEST"),
        SALARY("SALARY");
        
        private String value;

        TransactionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static TransactionType getEnum(String value) {
            for (TransactionType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // STAFF
    public enum Permission {

        SUPERUSER("SUPERUSER"),
        CUSTOMER("CUSTOMER"),
        DEPOSIT("DEPOSIT"),
        CARD("CARD"),
        LOAN("LOAN"),
        BILL("BILL"),
        WEALTH("WEALTH"),
        PORTFOLIO("PORTFOLIO"),
        ANALYTICS("ANALYTICS");
        
        private String value;

        Permission(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Permission getEnum(String value) {
            for (Permission v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum UserRole {

        GENERAL_TELLER("General Teller"),
        CUSTOMER_SERVICE("Customer Service Representative"),
        LOAN_OFFICIER("Loan Officer"),
        FINANCIAL_OFFICER("Financial Officer"),
        FINANCIAL_ANALYST("Financial Analyst"),
        PRODUCT_MANAGER("Product Manager"),
        SUPER_ADMIN("Super Admin");
        
        private String value;

        UserRole(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static UserRole getEnum(String value) {
            for (UserRole v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // Interests
    public enum InterestType {

        NORMAL("NORMAL"),
        RANGE("RANGE"),
        TIMERANGE("TIMERANGE"),
        CONDITION("CONDITION");
                        
        private String value;

        InterestType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static InterestType getEnum(String value) {
            for (InterestType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum InterestConditionType {

        BILL("BILL"),
        CCSPENDING("CCSPENDING"),
        SALARY("SALARY"),
        INVEST("INVEST"),
        INCREASE("INCREASE"),
        NOWITHDRAW("NOWITHDRAW");
        
        private String value;

        InterestConditionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static InterestConditionType getEnum(String value) {
            for (InterestConditionType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // Deposit Account
    public enum DepositAccountType {

        CUSTOM("CUSTOM"),
        CURRENT("CURRENT"),
        SAVING("SAVING"),
        FIXED("FIXED"),
        MOBILE("MOBILE");
        
        private String value;

        DepositAccountType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static DepositAccountType getEnum(String value) {
            for (DepositAccountType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum StatementType {

        E_STATEMENT("E_STATEMENT"),
        PRINTED("PRINTED");
        
        private String value;

        StatementType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static StatementType getEnum(String value) {
            for (StatementType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum CaseStatus {

        ONHOLD("ONHOLD"),
        ONGOING("ONGOING"),
        RESOLVED("RESOLVED"),
        CANCELLED("CANCELLED");
        
        private String value;

        CaseStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CaseStatus getEnum(String value) {
            for (CaseStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }
    
    public enum IssueField {

        PROFILE("PROFILE"),
        ACCOUNT("ACCOUNT"),
        DEPOSIT("DEPOSIT"),
        CARD("CARD"),
        LOAN("LOAN"),
        INVESTMENT("INVESTMENT");
        
        
        private String value;

        IssueField(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static IssueField getEnum(String value) {
            for (IssueField v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
