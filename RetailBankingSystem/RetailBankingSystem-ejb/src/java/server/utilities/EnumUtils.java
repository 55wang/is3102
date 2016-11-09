/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

/**
 *
 * @author leiyang
 */
// REMARK: To get the list of enum values, just use e.g. CreditType.values()
public class EnumUtils {

    public enum AdsType {

        DEPOSIT("deposit"),
        CARD("card");

        private String value;

        AdsType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static AdsType getEnum(String value) {
            for (AdsType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum RFMLevel {

        LOW("LOW"),
        MEDIUM("MEDIUM"),
        HIGH("HIGH");

        private String value;

        RFMLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static RFMLevel getEnum(String value) {
            for (RFMLevel v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Month {

        JANUARY("JANUARY"),
        FEBRUARY("FEBRUARY"),
        MARCH("MARCH"),
        APRIL("APRIL"),
        MAY("MAY"),
        JUNE("JUNE"),
        JULY("JULY"),
        AUGUST("AUGUST"),
        SEPTEMBER("SEPTEMBER"),
        OCTOBER("OCTOBER"),
        NOVEMBER("NOVEMBER"),
        DECEMBER("DECEMBER");

        private String value;

        Month(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Month getEnum(String value) {
            for (Month v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum FinancialHealthLevel {

        VERYUNHEALTHY("VERY UNHEALTHY"),
        UNHEALTHY("UNHEALTHY"),
        HEALTHY("HEALTHY"),
        VERYHEALTHY("VERY HEALTHY");

        private String value;

        FinancialHealthLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static FinancialHealthLevel getEnum(String value) {
            for (FinancialHealthLevel v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum TypeMarketingCampaign {

        ADSBANNER("ADSBANNERCAMPAIGN"),
        EMAILCAMPAIGN("EMAILCAMPAIGN");
        private String value;

        TypeMarketingCampaign(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static TypeMarketingCampaign getEnum(String value) {
            for (TypeMarketingCampaign v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum CardTransactionPaymentStatus {

        UNPAID("UNPAID"),
        PAID("PAID");
        private String value;

        CardTransactionPaymentStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CardTransactionPaymentStatus getEnum(String value) {
            for (CardTransactionPaymentStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum CardTransactionStatus {

        PENDINGTRANSACTION("PENDINGSETTLEMENT"),
        SETTLEDTRANSACTION("SETTLEDTRANSACTION");
        private String value;

        CardTransactionStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CardTransactionStatus getEnum(String value) {
            for (CardTransactionStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum CardNetwork {

        VISA("VISA"),
        MasterCard("MASTERCARD");
        private String value;

        CardNetwork(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CardNetwork getEnum(String value) {
            for (CardNetwork v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

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
    //for card account only
    public enum CardAccountStatus {

        PENDING("PENDING"),
        APPROVED("APPROVED"),
        ISSUED("ISSUED"),
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

    //main account
    public enum StatusType {

        NEW("NEW"),
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

    //for card application
    public enum ApplicationStatus {

        NEW("NEW"),
        PENDING("PENDING"),
        APPROVED("APPROVED"),
        REJECT("REJECT"),
        EDITABLE("EDITABLE"),
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

    public enum CardApplicationStatus {

        PENDING("PENDING"),
        APPROVED("APPROVED"),
        ISSUED("ISSUED"),
        REJECTED("REJECTED"),
        CANCELLED("CANCELLED");

        private String value;

        CardApplicationStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CardApplicationStatus getEnum(String value) {
            for (CardApplicationStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum PromoType {

        DISCOUNT("DISCOUNT"),
        VOUCHER("VOUCHER");

        private String value;

        PromoType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static PromoType getEnum(String value) {
            for (PromoType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

//    // Customer
//    public enum Position {
//
//        SENIOR_MANAGEMENT("SENIOR_MANAGEMENT"),
//        PROFESSIONAL("PROFESSIONAL"),
//        MANAGERIAL("MANAGERIAL"),
//        EXECUTIVE("EXECUTIVE"),
//        SALES("SALES"),
//        OTHERS("OTHERS"),
//        DIRECTOR("DIRECTOR"),
//        SUPERVISOR("SUPERVISOR"),
//        TEACHER_LECTURER("TEACHER_LECTURER"),
//        DILPOMAT("DILPOMAT");
//
//        private String value;
//
//        Position(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        @Override
//        public String toString() {
//            return this.getValue();
//        }
//
//        public static Position getEnum(String value) {
//            for (Position v : values()) {
//                if (v.getValue().equalsIgnoreCase(value)) {
//                    return v;
//                }
//            }
//            throw new IllegalArgumentException();
//        }
//    }
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
        PASSPORT("Passport");

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

    public enum Citizenship {

        SINGAPOREAN("Singaporean"),
        SINGAPORE_PR("Singapore PR"),
        FOREIGNER("Foreigner");

        private String value;

        Citizenship(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Citizenship getEnum(String value) {
            for (Citizenship v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Nationality {

        SINGAPORE("Singapore"),
        INDONESIA("Indonesia"),
        CHINA("China"),
        MALAYSIA("Malaysia"),
        AUSTRALIA("Australia"),
        INDIA("India"),
        JAPAN("Japan"),
        PHILIPPINES("Philippines"),
        CHINA_HONG_KONG("China Hong Kong"),
        SOUTH_KOREA("South Korea"),
        THAILAND("Thailand"),
        UNITED_STATES("United States"),
        UNITED_KINGDOM("United Kingdom"),
        VIETNAM("Vietnam"),
        GERMANY("Germany");

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

    public enum Occupation {

        SELF_EMPLOYED("Self employed"),
        FREELANCER("Freelancer"),
        SENIOR_MANAGEMENT("Senior Management"),
        DIRECTOR("Director"),
        EXECUTIVE("Executive"),
        TEACHER("Teacher"),
        SALES("Sales"),
        SUPERVISOR("Supervisor"),
        MANAGERIAL("Managerial"),
        DIPLOMAT("Diplomat"),
        OTHERS("Others");

        private String value;

        Occupation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Occupation getEnum(String value) {
            for (Occupation v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Education {

        POSTGRAD("PostGrad"),
        UNIVERSITY("University"),
        DIPLOMA("Diploma"),
        A_LEVEL("A_level"),
        SECONDARY("Secondary"),
        TECHNICAL("Technical"),
        OTHERS("Others");

        private String value;

        Education(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Education getEnum(String value) {
            for (Education v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Income {

        BELOW_2000("< S$2000", 2000.0),
        FROM_2000_TO_4000("S$2000 to 4000", 3000.0),
        FROM_4000_TO_6000("S$4000 to 6000", 5000.0),
        FROM_6000_TO_8000("S$6000 to 8000", 7000.0),
        FROM_8000_TO_10000("S$8000 to 10000", 9000.0),
        OVER_10000("> S$10000", 10000.0);

        private final String value;
        private final Double avgValue;

        Income(String value, Double avgValue) {
            this.value = value;
            this.avgValue = avgValue;
        }

        public String getValue() {
            return value;
        }

        public static Income getEnumFromNumber(Double income) {
            if (income < 2000) {
                return Income.BELOW_2000;
            } else if (income >= 2000 & income < 4000) {
                return Income.FROM_2000_TO_4000;
            } else if (income >= 4000 & income < 6000) {
                return Income.FROM_4000_TO_6000;
            } else if (income >= 6000 & income < 8000) {
                return Income.FROM_6000_TO_8000;
            } else if (income >= 8000 & income < 10000) {
                return Income.FROM_8000_TO_10000;
            } else {
                return Income.OVER_10000;
            }
        }

        public Double getAvgValue() {
            return avgValue;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static Income getEnum(String value) {
            for (Income v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Gender {

        MALE("Male"),
        FEMALE("Female");

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

    public enum MaritalStatus {

        SINGLE("Single"),
        MARRIED("Married"),
        DIVORCED("Divorced"),
        OTHERS("Others");

        private String value;

        MaritalStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static MaritalStatus getEnum(String value) {
            for (MaritalStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // Transaction
    public enum TransactionType {

        INITIAL("Initial Deposit"),
        DEPOSIT("Deposit"),
        WITHDRAW("Withdraw"),
        CHEQUE("Cheque"),
        INTEREST("Interest"),
        TRANSFER("Transfer"),
        TOPUP("Top up"),
        LOCALTRANSFER("Local Transfer"),
        INTERBANKTRANSFER("Inter Bank Transfer"),
        OVERSEASTRANSFER("Overseas transfer"),
        BILL("Bill"),
        CCSPENDING("Credit Card Spending"),
        INVEST("Invest"),
        SALARY("Salary");

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
        SUPER_ADMIN("Super Admin"),
        RELATIONSHIP_MANAGER("Relationship Manager");

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

    public enum InterestTimeRange {

        // in month
        T1_T2("1-2 months"),
        T3_T5("3-5 months"),
        T6("6 mth"),
        T7_T8("7-8 months"),
        T9_T11("9-11 months"),
        T12_T15("12-15 months"),
        T18("18 mth"),
        T24("24 mth"),
        T36("18 mth");

        private String value;

        InterestTimeRange(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static InterestTimeRange getEnum(String value) {
            for (InterestTimeRange v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum InterestAmountRange {

        M5_M20("$5,000 - $20,000"),
        M20_M50("$20,000 - $50,000"),
        M50_M99("$50,000 - $99,999"),
        M100_M249("$100,000 - $249,999"),
        M250_M499("$250,000 - $499,999"),
        M500_M999("$500,000 - $999,999");

        private String value;

        InterestAmountRange(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static InterestAmountRange getEnum(String value) {
            for (InterestAmountRange v : values()) {
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

    public enum ChequeStatus {

        UNTOUCHED("UNTOUCHED"),
        RECEIVED("RECEIVED"),
        PROCESSING("PROCESSING"),
        TRANSFERED("TRANSFERED"),
        LOST("LOST");

        private String value;

        ChequeStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static ChequeStatus getEnum(String value) {
            for (ChequeStatus v : values()) {
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
        INVESTMENT("INVESTMENT"),
        CHARGEBACK("CHARGEBACK");

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

    public enum CardOperatorChargebackStatus {

        PENDING("PENDING"),
        APPROVE("APPROVE"),
        REJECT("REJECT");
        private String value;

        CardOperatorChargebackStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CardOperatorChargebackStatus getEnum(String value) {
            for (CardOperatorChargebackStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // bill
    public enum BillType {

        CLUBS("Country Clubs/ Recreational Clubs"),
        EDU("Educational Institution"),
        COUNCILS("Town Councils"),
        CARD("Credit Cards"),
        UTIL("Telecommunications and Utilities"),
        GOV_AGEN("Government Agencies"),
        NON_GOV_AGEN("Other Agencies"),
        INSUR("Insurance Companies"),
        SECUR("Brokerage/Securities Firms");
        private String value;

        BillType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static BillType getEnum(String value) {
            for (BillType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // transfer
    public enum PayeeType {

        MERLION("MERLION"),
        LOCAL("LOCAL"),
        OVERSEAS("OVERSEAS");

        private String value;

        PayeeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static PayeeType getEnum(String value) {
            for (PayeeType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum TransferPurpose {

        // Personal Expenses
        CC_PAY("Credit Card Payment"),
        CARPARK_CHRAGES("Carpark Charges"),
        CABLE_TV_BILL("Cable TV Bill"),
        DC_PAY("Debit Card Payment"),
        DENTAL("Dental Services"),
        HOSPITAL("Hospital Care"),
        MEDICAL("Medical Services"),
        FEES_N_CHARGERS("Payment Of Fees & Charges"),
        RENT("Rent"),
        TELCO("Telco Bill"),
        TELEPHONE("Telephone Bill"),
        TOWN_COUNCIL("Town Council Service Charges"),
        TRANSPORT("Transport"),
        UTILITIES("Utilities"),
        // Business Expenses
        BUSINESS_EXPENSE("Business Expenses"),
        COLLECTION_PAYMENT("Collection Payment"),
        CASH_DISBURSEMENT("Cash Disbursement"),
        INSTAL_HIRE_PUR_AGREE("Instalment Hire Purchase Agreement"),
        INTRA_COM_PAY("Intra Company Payment"),
        INVOICE_PAY("Invoice Payment"),
        PURCHASE_SOG("Purchase Sale of Goods"),
        SUPPLIER_PAYMENT("Supplier Payment"),
        TRADE_SERVICES("Trade Services"),
        TREASURY_PAYMENT("Treasury Payment"),
        // Donation to Charity
        CHARITY_PAYMENT("Charity Payment"),
        // Education Expenses
        EDUCATION("Education"),
        STUDY("Study"),
        // Foreign Worker Levy
        FOREIGN_WORKER_LEVY("Foreign Worker Levy"),
        // Investments and Insurance
        GOV_INSUR("Government Insurance"),
        INSUR_PREMIUM("Insurance Premium"),
        INVEST_N_SECUR("Investment & Securities"),
        // Loan Repayment
        LOAN("Loan"),
        // Salary / Commission
        BONUS_PAYMENT("Bonus Payment"),
        COMMISSION("Commission"),
        SALARY_PAYMENT("Salary Payment"),
        // Tax Payments
        GOODS_N_SERVICES_TAX("Goods & Services Tax"),
        NET_INCOME_TAX("Net Income Tax"),
        PROPERTY_TAX("Property Tax"),
        ROAD_TAX("Road Tax"),
        TAX_PAYMENT("Tax Payment"),
        // Others
        DIVIDEND("Dividend"),
        INTEREST("Interest"),
        REBATE("Rebate"),
        REFUND("Refund"),
        WITHHOLDING("Withholding"),
        OTHERS("Others");

        private String value;

        TransferPurpose(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static TransferPurpose getEnum(String value) {
            for (TransferPurpose v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum RiskToleranceLevel {

        LOW_RISK_TOLERANCE("Low risk tolerance"),
        BELOW_AVERAGE_RISK_TOLERANCE("Below-average risk tolerance"),
        AVERAGE_RISK_TOLERANCE("Average/moderate risk tolerance"),
        ABOVE_AVERAGE_RISK_TOLERANCE("Above-average risk tolerance"),
        HIGH_RISK_ROLERANCE("High risk tolerance");

        private String value;

        RiskToleranceLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static RiskToleranceLevel getEnum(String value) {
            for (RiskToleranceLevel v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum InvestmentRiskLevel {

        LOW_RISK("Low risk"),
        BELOW_AVERAGE_RISK("Below-average risk"),
        AVERAGE_RISK("Average/moderate risk"),
        ABOVE_AVERAGE_RISK("Above-average risk"),
        HIGH_RISK("High risk");

        private String value;

        InvestmentRiskLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static InvestmentRiskLevel getEnum(String value) {
            for (InvestmentRiskLevel v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum InvestmentPlanStatus {

        PENDING("PENDING"),
        CANCELLED("CANCELLED"),
        ONGOING("ONGOING"),
        WAITING("WAITING APPROVAL"),
        APPROVAL("APPROVAL"),
        EXECUTED("EXECUTED"),
        TERMINATED("TERMINATED");
        ;

        private String value;

        InvestmentPlanStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static InvestmentPlanStatus getEnum(String value) {
            for (InvestmentPlanStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum InvestmentPlanSatisfactionLevel {

        VERY_SATISFIED("Very Satisfied"),
        SATISFIED("Satisfied"),
        OK("OK"),
        DISATISFIED("Dissatisfied"),
        VERY_DISATISFIED("Very Disatisfied");

        private String value;

        InvestmentPlanSatisfactionLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static InvestmentPlanSatisfactionLevel getEnum(String value) {
            for (InvestmentPlanSatisfactionLevel v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum FinancialInstrumentClass {

        US_STOCKS("US STOCKS"),
        FOREIGN_DEVELOPED_STOCKS("FOREIGN DEVELOPED STOCKS"),
        EMERGING_MARKET_STOCKS("EMERGING MARKET STOCKS"),
        DIVIDEND_GROWTH_STOCKS("DIVIDEND GROWTH STOCKS"),
        US_GOVERNMENT_BONDS("US GOVERNMENT BONDS"),
        CORPORATE_BONDS("CORPORATE BONDS"),
        EMERGING_MARKET_BONDS("EMERGING MARKET BONDS"),
        MUNICIPAL_BONDS("MUNICIPAL BONDS"),
        TREASURY_INFLATION_PROTECTED_SECURITIES("TREASURY INFLATION-PROTECTED SECURITIES"),
        REAL_ESTATE("REAL ESTATE"),
        NATURAL_RESOURCES("NATURAL RESOURCES");

        private String value;

        FinancialInstrumentClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static FinancialInstrumentClass getEnum(String value) {
            for (FinancialInstrumentClass v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum IntraBankTransferLimit {

        _25000("25000.00"),
        _23500("23500.00"),
        _22000("22000.00"),
        _20000("20000.00"),
        _15000("15000.00"),
        _14000("14000.00"),
        _12500("12500.00"),
        _11000("11000.00"),
        _10000("10000.00"),
        _9000("9000.00"),
        _7500("7500.00"),
        _5000("5000.00"),
        _4000("4000.00"),
        _3000("3000.00"),
        _2000("2000.00"),
        _1000("1000.00"),
        _500("500.00");

        private String value;

        IntraBankTransferLimit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static IntraBankTransferLimit getEnum(String value) {
            for (IntraBankTransferLimit v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum InterBankTransferLimit {

        _50000("50000.00"),
        _40000("40000.00"),
        _15000("15000.00"),
        _10000("10000.00"),
        _7500("7500.00"),
        _5000("5000.00"),
        _4000("4000.00"),
        _3000("3000.00"),
        _2000("2000.00"),
        _1000("1000.00"),
        _500("500.00");

        private String value;

        InterBankTransferLimit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static InterBankTransferLimit getEnum(String value) {
            for (InterBankTransferLimit v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum OverseasBankTransferLimit {

        _200000("200000.00"),
        _150000("150000.00"),
        _100000("190000.00"),
        _50000("50000.00"),
        _40000("40000.00"),
        _15000("15000.00"),
        _10000("10000.00"),
        _7500("7500.00"),
        _5000("5000.00"),
        _4000("4000.00"),
        _3000("3000.00"),
        _2000("2000.00"),
        _1000("1000.00"),
        _500("500.00");

        private String value;

        OverseasBankTransferLimit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static OverseasBankTransferLimit getEnum(String value) {
            for (OverseasBankTransferLimit v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    // loan
    public enum LoanAccountStatus {

        NEW("NEW"),
        INPROGRESS("INPROGRESS"),
        PENDING("PENDING"),
        APPROVED("APPROVED"),
        REJECTED("REJECTED"),
        CLOSED("CLOSED"),
        SUSPENDED("SUSPENDED");

        private String value;

        LoanAccountStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static LoanAccountStatus getEnum(String value) {
            for (LoanAccountStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum LoanProductType {

        LOAN_PRODUCT_TYPE_PRIVATE_HOUSE("MBS Private House Loan"),
        LOAN_PRODUCT_TYPE_HDB("MBS HDB Loan"),
        LOAN_PRODUCT_TYPE_CAR("MBS Car Loan"),
        LOAN_PRODUCT_TYPE_PERSONAL("MBS Personal Loan"),
        SUSPENDED("SUSPENDED");

        private String value;

        LoanProductType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static LoanProductType getEnum(String value) {
            for (LoanProductType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum LoanRepaymentType {

        LOAN_REPAYMENT("Loan Repayment"),
        LOAN_REPAYMENT_LESS("Loan Repayment Less"),
        LOAN_REPAYMENT_MORE("Loan Repayment More"),
        LOAN_LUMSUM_PAY("Loan Lumsum Repayment"),
        LOAN_CHANGE_PERIOD("Loan Change Period");

        private String value;

        LoanRepaymentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static LoanRepaymentType getEnum(String value) {
            for (LoanRepaymentType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum PortfolioStatus {

        PENDING("PENDING"),
        BOUGHT("BOUGHT"),
        SOLD("SOLD");

        private String value;

        PortfolioStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static PortfolioStatus getEnum(String value) {
            for (PortfolioStatus v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum CreditCardType {

        REWARD("REWARD"),
        MILE("MILE"),
        CASHBACK("CASHBACK");

        private String value;

        CreditCardType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static CreditCardType getEnum(String value) {
            for (CreditCardType v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
