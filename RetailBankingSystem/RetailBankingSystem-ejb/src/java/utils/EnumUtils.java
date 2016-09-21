/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

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

        MILE {
                    @Override
                    public String toString() {
                        return "MILE";
                    }
                },
        REWARD {
                    @Override
                    public String toString() {
                        return "REWARD";
                    }
                },
        CASHBACK {
                    @Override
                    public String toString() {
                        return "CASHBACK";
                    }
                }
    }

    // LY: Can this conbimed with Status?
    public enum CardAccountStatus {

        PENDING {
                    @Override
                    public String toString() {
                        return "PENDING";
                    }
                },
        ACTIVE {
                    @Override
                    public String toString() {
                        return "ACTIVE";
                    }
                },
        FREEZE {
                    @Override
                    public String toString() {
                        return "FREEZE";
                    }
                },
        CLOSED {
                    @Override
                    public String toString() {
                        return "CLOSED";
                    }
                }
    }

    public enum StatusType {

        ACTIVE {
                    public String toString() {
                        return "ACTIVE";
                    }
                },
        PENDING {
                    public String toString() {
                        return "PENDING";
                    }
                },
        FREEZE {
                    public String toString() {
                        return "FREEZE";
                    }
                },
        CLOSED {
                    public String toString() {
                        return "CLOSED";
                    }
                }
    }

    public enum ApplicationStatus {

        PENDING {
                    @Override
                    public String toString() {
                        return "PENDING";
                    }
                },
        EDITABLE {
                    @Override
                    public String toString() {
                        return "EDITABLE";
                    }
                },
        REJECT {
                    @Override
                    public String toString() {
                        return "REJECT";
                    }
                },
        APPROVED {
                    @Override
                    public String toString() {
                        return "APPROVED";
                    }
                },
        CANCELLED {
                    @Override
                    public String toString() {
                        return "CANCELLED";
                    }
                }
    }

    // Customer
    public enum Position {

        SENIOR_MANAGEMENT {
                    @Override
                    public String toString() {
                        return "SENIOR_MANAGEMENT";
                    }
                },
        PROFESSIONAL {
                    @Override
                    public String toString() {
                        return "PROFESSIONAL";
                    }
                },
        MANAGERIAL {
                    @Override
                    public String toString() {
                        return "MANAGERIAL";
                    }
                },
        EXECUTIVE {
                    @Override
                    public String toString() {
                        return "EXECUTIVE";
                    }
                },
        SALES {
                    @Override
                    public String toString() {
                        return "SALES";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                },
        DIRECTOR {
                    @Override
                    public String toString() {
                        return "DIRECTOR";
                    }
                },
        SUPERVISOR {
                    @Override
                    public String toString() {
                        return "SUPERVISOR";
                    }
                },
        TEACHER_LECTURER {
                    @Override
                    public String toString() {
                        return "TEACHER_LECTURER";
                    }
                },
        DILPOMAT {
                    @Override
                    public String toString() {
                        return "DILPOMAT";
                    }
                }
    }

    public enum Industry {

        BUILDING_CONSTRUCTION {
                    @Override
                    public String toString() {
                        return "BUILDING_CONSTRUCTION";
                    }
                },
        BANKING_FINANCE {
                    @Override
                    public String toString() {
                        return "BANKING_FINANCE";
                    }
                },
        IT_TELCO {
                    @Override
                    public String toString() {
                        return "IT_TELCO";
                    }
                },
        GOVERNMENT {
                    @Override
                    public String toString() {
                        return "GOVERNMENT";
                    }
                },
        MANUFACTURING {
                    @Override
                    public String toString() {
                        return "MANUFACTURING";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                },
        SHIPPING_TRANSPORT {
                    @Override
                    public String toString() {
                        return "SHIPPING_TRANSPORT";
                    }
                },
        ENTERTAINMENT {
                    @Override
                    public String toString() {
                        return "ENTERTAINMENT";
                    }
                },
        HOTEL_RESTAURANT {
                    @Override
                    public String toString() {
                        return "HOTEL_RESTAURANT";
                    }
                },
        RETAIL {
                    @Override
                    public String toString() {
                        return "RETAIL";
                    }
                },
        TRAVEL_RELATED {
                    @Override
                    public String toString() {
                        return "TRAVEL_RELATED";
                    }
                }
    }

    public enum EmploymentStatus {

        VARIABLE_COMMISSION {
                    @Override
                    public String toString() {
                        return "VARIABLE_COMMISSION";
                    }
                },
        EMPLOYEE {
                    @Override
                    public String toString() {
                        return "EMPLOYEE";
                    }
                },
        SELF_EMPLOYED {
                    @Override
                    public String toString() {
                        return "SELF_EMPLOYED";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                }

    }

    public enum EduLevel {

        UNIVERSITY_GRAD {
                    @Override
                    public String toString() {
                        return "UNIVERSITY_GRAD";
                    }
                },
        DIPLOMA_HOLDER {
                    @Override
                    public String toString() {
                        return "DIPLOMA_HOLDER";
                    }
                },
        TECHNICAL {
                    @Override
                    public String toString() {
                        return "TECHNICAL";
                    }
                },
        A_LEVEL {
                    @Override
                    public String toString() {
                        return "A_LEVEL";
                    }
                },
        SECONDARY {
                    @Override
                    public String toString() {
                        return "SECONDARY";
                    }
                },
        PRIMARY {
                    @Override
                    public String toString() {
                        return "PRIMARY";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                }
    }

    public enum ResidentialStatus {

        CONDO_APART {
                    @Override
                    public String toString() {
                        return "CONDO_APART";
                    }
                },
        HDB {
                    @Override
                    public String toString() {
                        return "HDB";
                    }
                },
        LANDED {
                    @Override
                    public String toString() {
                        return "LANDED";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                }
    }

    public enum ResidentialType {

        EMPLOYER {
                    @Override
                    public String toString() {
                        return "EMPLOYER";
                    }
                },
        MORTGAGED {
                    @Override
                    public String toString() {
                        return "MORTGAGED";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                },
        PARENTS {
                    @Override
                    public String toString() {
                        return "PARENTS";
                    }
                },
        RENTED {
                    @Override
                    public String toString() {
                        return "RENTED";
                    }
                },
        SELF_OWNED {
                    @Override
                    public String toString() {
                        return "SELF_OWNED";
                    }
                }
    }

    public enum Salutation {

        DR {
                    @Override
                    public String toString() {
                        return "DR";
                    }
                },
        MDM {
                    @Override
                    public String toString() {
                        return "MDM";
                    }
                },
        MR {
                    @Override
                    public String toString() {
                        return "MR";
                    }
                },
        MRS {
                    @Override
                    public String toString() {
                        return "MRS";
                    }
                },
        MS {
                    @Override
                    public String toString() {
                        return "MS";
                    }
                }
    }

    public enum IdentityType {

        NRIC {
                    @Override
                    public String toString() {
                        return "NRIC";
                    }
                },
        PASSPORT {
                    @Override
                    public String toString() {
                        return "PASSPORT";
                    }
                }
    }

    public enum Nationality {

        SINGAPOREAN {
                    @Override
                    public String toString() {
                        return "SINGAPOREAN";
                    }
                },
        PR {
                    @Override
                    public String toString() {
                        return "PR";
                    }
                },
        E_PASS {
                    @Override
                    public String toString() {
                        return "E_PASS";
                    }
                },
        S_PASS {
                    @Override
                    public String toString() {
                        return "S_PASS";
                    }
                }// TODO: Fill up the list
    }

    public enum Gender {

        MALE {
                    @Override
                    public String toString() {
                        return "MALE";
                    }
                },
        FEMALE {
                    @Override
                    public String toString() {
                        return "FEMALE";
                    }
                }
    }

    public enum MartialStatus {

        SINGLE {
                    @Override
                    public String toString() {
                        return "SINGLE";
                    }
                },
        MARRIED {
                    @Override
                    public String toString() {
                        return "MARRIED";
                    }
                },
        DIVORCED {
                    @Override
                    public String toString() {
                        return "DIVORCED";
                    }
                },
        OTHERS {
                    @Override
                    public String toString() {
                        return "OTHERS";
                    }
                }
    }

    // Transaction
    public enum TransactionType {

        INITIAL {
                    @Override
                    public String toString() {
                        return "INITIAL DEPOSIT";
                    }
                },
        DEPOSIT {
                    @Override
                    public String toString() {
                        return "DEPOSIT";
                    }
                },
        WITHDRAW {
                    @Override
                    public String toString() {
                        return "WITHDRAW";
                    }
                },
        CHEQUE {
                    @Override
                    public String toString() {
                        return "CHEQUE";
                    }
                },
        TRANSFER {
                    @Override
                    public String toString() {
                        return "TRANSFER";
                    }
                },
        LOCALTRANSFER {
                    @Override
                    public String toString() {
                        return "LOCAL TRANSFER";
                    }
                },
        INTERBANKTRANSFER {
                    @Override
                    public String toString() {
                        return "INTER BANK TRANSFER";
                    }
                },
        OVERSEASTRANSFER {
                    @Override
                    public String toString() {
                        return "OVERSEAS TRANSFER";
                    }
                },
        BILL {
                    @Override
                    public String toString() {
                        return "BILL";
                    }
                },
        CCSPENDING {
                    @Override
                    public String toString() {
                        return "CCSPENDING";
                    }
                },
        INVEST {
                    @Override
                    public String toString() {
                        return "INVEST";
                    }
                },
        SALARY {
                    @Override
                    public String toString() {
                        return "SALARY";
                    }
                }
    }

    // STAFF
    public enum Permission {

        SUPERUSER {
                    public String toString() {
                        return "SUPERUSER";
                    }
                },
        CUSTOMER {
                    public String toString() {
                        return "CUSTOMER";
                    }
                },
        DEPOSIT {
                    public String toString() {
                        return "DEPOSIT";
                    }
                },
        CARD {
                    public String toString() {
                        return "CARD";
                    }
                },
        LOAN {
                    public String toString() {
                        return "LOAN";
                    }
                },
        BILL {
                    public String toString() {
                        return "BILL";
                    }
                },
        WEALTH {
                    public String toString() {
                        return "WEALTH";
                    }
                },
        PORTFOLIO {
                    public String toString() {
                        return "PORTFOLIO";
                    }
                },
        ANALYTICS {
                    public String toString() {
                        return "ANALYTICS";
                    }
                }
    }
    
    public enum UserRole {
        SUPER_ADMIN {
            @Override
            public String toString() {
                return "SUPER_ADMIN";
            }
        }
    }

    // Interests
    public enum InterestType {

        NORMAL {
                    @Override
                    public String toString() {
                        return "NORMAL";
                    }
                },
        RANGE {
                    @Override
                    public String toString() {
                        return "RANGE";
                    }
                },
        TIMERANGE {
                    @Override
                    public String toString() {
                        return "TIMERANGE";
                    }
                },
        CONDITION {
                    @Override
                    public String toString() {
                        return "CONDITION";
                    }
                }
    }

    public enum InterestConditionType {

        BILL {
                    @Override
                    public String toString() {
                        return "BILL";
                    }
                },
        CCSPENDING {
                    @Override
                    public String toString() {
                        return "CCSPENDING";
                    }
                },
        SALARY {
                    @Override
                    public String toString() {
                        return "SALARY";
                    }
                },
        INVEST {
                    @Override
                    public String toString() {
                        return "INVEST";
                    }
                },
        INCREASE {
                    @Override
                    public String toString() {
                        return "INCREASE";
                    }
                },
        NOWITHDRAW {
                    @Override
                    public String toString() {
                        return "NOWITHDRAW";
                    }
                }
    }

    // Deposit Account
    public enum DepositAccountType {

        CUSTOM {
                    @Override
                    public String toString() {
                        return "CUSTOM";
                    }
                },
        CURRENT {
                    @Override
                    public String toString() {
                        return "CURRENT";
                    }
                },
        SAVING {
                    @Override
                    public String toString() {
                        return "SAVING";
                    }
                },
        FIXED {
                    @Override
                    public String toString() {
                        return "FIXED";
                    }
                },
        MOBILE {
                    @Override
                    public String toString() {
                        return "MOBILE";
                    }
                },
    }

    public enum StatementType {

        E_STATEMENT {
                    @Override
                    public String toString() {
                        return "E_STATEMENT";
                    }
                },
        PRINTED {
                    @Override
                    public String toString() {
                        return "PRINTED";
                    }
                }
    }
}
