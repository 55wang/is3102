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
public class ConstantUtils {
    public static final String ipAddress = "127.0.0.1"; //192.168.10.101
    public static final String DELIMITER = "``";
    
    public static final Integer ACCOUNT_NUBMER_LENGTH = 11;
    public static final Integer REFERENCE_NUBMER_LENGTH = 13;
    public static final Integer LOAN_ACCOUNT_NUBMER_LENGTH = 12;
    
    // Entity Table Name
    public static final String TRANSACTION_ENTITY = "TransactionRecord";
    public static final String SERVICE_CHARGE_ENTITY = "ServiceCharge";
    
    // DEMO
    // DAMS
    public static final String DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME = "Merlion Account";
    public static final String DEMO_SAVING1_DEPOSIT_PRODUCT_NAME = "Monthly Savings Account";
    public static final String DEMO_SAVING2_DEPOSIT_PRODUCT_NAME = "Bonus+Savings";
    public static final String DEMO_CURRENT_DEPOSIT_PRODUCT_NAME = "MBS Current Account";
    public static final String DEMO_FIXED_DEPOSIT_PRODUCT_NAME = "Time Deposit";
    // LOAN
    // Personal loan
    public static final String DEMO_PERSONAL_LOAN_PRODUCT_NAME_12 = "MBS Personal Loan 0-12 Month";
    public static final String DEMO_PERSONAL_LOAN_PRODUCT_NAME_24 = "MBS Personal Loan 0-24 Month";
    public static final String DEMO_PERSONAL_LOAN_PRODUCT_NAME_36 = "MBS Personal Loan 0-36 Month";
    public static final String DEMO_PERSONAL_LOAN_INTEREST_NAME_12 = "MBS Personal Loan Interest 1 year";
    public static final String DEMO_PERSONAL_LOAN_INTEREST_NAME_24 = "MBS Personal Loan Interest 2 years";
    public static final String DEMO_PERSONAL_LOAN_INTEREST_NAME_36 = "MBS Personal Loan Interest 3 years";
    // Car loan
    public static final String DEMO_CAR_LOAN_PRODUCT_NAME = "MBS Car Loan";
    public static final String DEMO_CAR_LOAN_INTEREST_NAME = "MBS Car Loan Interest";
    // HDB Fixed
    public static final String DEMO_HDB_FIXED_LOAN_PRODUCT_NAME = "MBS HDB Loan with fixed interest";
    public static final String DEMO_HDB_FIXED_INTEREST_NAME = "MBS HDB Loan fixed Interest";
    // HDB SIBOR
    public static final String DEMO_HDB_SIBOR_LOAN_PRODUCT_NAME = "MBS HDB Loan with SIBOR interest";
    public static final String DEMO_HDB_SIBOR_INTEREST_NAME = "MBS HDB Loan SIBOR Interest";
    
    public static final String DEMO_LOAN_COMMON_INTEREST_NAME = "SIBOR Interest";
    
    public static final String DEMO_MAIN_ACCOUNT_USER_ID_0 = "c7654321";
    public static final String DEMO_MAIN_ACCOUNT_USER_ID_1 = "c1234567";
    public static final String DEMO_MAIN_ACCOUNT_USER_ID_2 = "c0000002";
    public static final String DEMO_MAIN_ACCOUNT_USER_ID_3 = "c0000003";
    public static final String DEMO_MAIN_ACCOUNT_USER_ID_4 = "c0000004";
    public static final String DEMO_MAIN_ACCOUNT_USER_ID_5 = "c0000005";
    
    public static final String SUPER_ADMIN_USERNAME = "adminadmin";
    public static final String SUPER_ADMIN_PASSWORD = HashPwdUtils.hashPwd("password");
    public static final String CUSTOMER_SERVICE_USERNAME = "customer_service";
    public static final String FINANCIAL_ANALYST_USERNAME = "financial_analyst";
    public static final String FINANCIAL_OFFICER_USERNAME = "financial_officer";
    public static final String GENERAL_TELLER_USERNAME = "general_teller";
    public static final String LOAN_OFFICIER_USERNAME = "loan_officer";
    public static final String PRODUCT_MANAGER_USERNAME = "product_manager";
    public static final String RELATIONSHIP_MANAGER_USERNAME = "relationship_manager";
    public static final String STAFF_DEMO_PASSWORD = HashPwdUtils.hashPwd("password");
    
    // Redirect path
    // staff card
    public static final String STAFF_CARD_CARD_CREATE_PRODUCT = "/StaffInternalSystem/card/card_create_product.xhtml";
    public static final String STAFF_CARD_STAFF_VIEW_CARD = "/StaffInternalSystem/card/card-view-product.xhtml";
    // staff cms
    public static final String STAFF_CMS_STAFF_VIEW_CASE = "/StaffInternalSystem/cms/staff_view_case.xhtml";
    public static final String STAFF_CMS_STAFF_TRANSFER_CASE = "/StaffInternalSystem/cms/staff_transfer_case.xhtml";
    public static final String STAFF_CMS_STAFF_EDIT_CUSTOMER = "/StaffInternalSystem/cms/staff_edit_customer.xhtml";
    // counter cms
    public static final String COUNTER_CMS_STAFF_VIEW_CASE = "/TellerCounterSystem/cms/staff_view_case.xhtml";
    public static final String COUNTER_CMS_STAFF_EDIT_CUSTOMER = "/TellerCounterSystem/cms/staff_edit_customer.xhtml";
    
    // staff marketing campaign
    public static final String STAFF_MC_VIEW_MARKETING_CAMPAIGNS = "/StaffInternalSystem/crm/view_marketing_campaigns.xhtml";
    public static final String STAFF_MC_CREATE_MARKETING_CAMPAIGNS = "/StaffInternalSystem/crm/create_marketing_campaign.xhtml";
    
    
    // Error Message
    // General
    public static final String SOMETHING_WENT_WRONG = "Something went wrong.";
    public static final String NOT_ENOUGH_BALANCE = "Not enough balance.";
    public static final String NOT_ENOUGH_AGE = "Your age must be above 21 years old to apply this loan.";
    public static final String NOT_ENOUGH_INCOME_2000 = "Your monthly income must be above $2000 to apply this loan.";
    public static final String NOT_ENOUGH_INCOME_1500 = "Your monthly income must be above $1500 to apply this loan.";
    public static final String NOT_ENOUGH_LOAN_LIMIT = "Your loan request amount has exceed maximum loan amount.";
    public static final String LoanToValue_NOT_RIGHT="Your Loan-To-Value ratio is exceeded. Please reduce loan amount or increase housing value.";
    public static final String EXCEED_MAX_HDB_TENURE="The max tenure is 25 years";
    public static final String EXCEED_MAX_PP_TENURE="The max tenure is 30 years";
    public static final String EXCEED_MAX_TENURE="Exceed max tenure!";
    
    // Password
    public static final String OLD_PASSWORD_NOTMTACH = "You have entered a wrong password! ";
    public static final String PASSWORD_CHANGE_SUCCESS = "Successful! You have reset your password. ";
    // Transfer
    public static final String TRANSFER_SUCCESS = "Transfer Successed!";
    public static final String TRANSFER_FAILED = "Transfer Failed! Not enough balance in your deposit account!";
    public static final String TRANSFER_FAILED2 = "Your payment is greater than your outstanding loan amount!";
    public static final String TRANSFER_FAILED3 = "Please go to lum Sum Payment if your repayment is greater then 10,000";
    public static final String EXCEED_TRANSFER_LIMIT = "Transfer Failed! Check your transfer limit!";
    public static final String TRANSFER_ACCOUNT_NOT_FOUND = "Transfer Failed! Check your whether account number is correct!";
    public static final String UPDATE_TRANSFER_LIMIT_SUCCESS = "Transfer Limits updated!";
    public static final String UPDATE_TRANSFER_LIMIT_FAIL = "Transfer Limits update failed!";
    // Payee
    public static final String PAYEE_SUCCESS = "Add new Payee Successed!";
    public static final String PAYEE_DELETE_SUCCESS = "Delete Payee Successed!";
    public static final String PAYEE_FAILED = "Add new Payee Failed! Check Payee Account Number!";
    public static final String PAYEE_DELETE_FAILED = "Delete Payee Failed!";
    public static final String PAYEE_ACCOUNT_NOT_FOUND = "Add new Payee Failed! Check your payee's account number is correct!";
    // bill org
    public static final String BILL_ORG_SUCCESS = "Add new billing organization Successed!";
    public static final String BILL_ORG_DELETE_SUCCESS = "Delete billing organization Successed!";
    public static final String BILL_ORG_DELETE_FAILED = "Delete billing organization Failed!";
    public static final String BILL_ORG_FAILED = "Add new billing organization Failed! Check reference number!";
    // giro
    public static final String GIRO_SUCCESS = "Add new GIRO Successed!";
    public static final String GIRO_DELETE_SUCCESS = "Delete GIRO arrangement Successed!";
    public static final String GIRO_DELETE_FAILED = "Delete GIRO arrangement Failed!";
    public static final String GIRO_FAILED = "Add new GIRO arrangement Failed! Check reference number!";
    public static final String GIRO_LIMIT_SUCCESS = "GIRO limit changed Successed!";
    public static final String GIRO_LIMIT_FAIL = "GIRO limit changed Failed!";
    // cc 
    public static final String CC_NUMBER_INVALID = "Credit Card Number is not valid!";
}
