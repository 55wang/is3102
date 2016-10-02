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
    public static final String DELIMITER = "``";
    
    public static final Integer ACCOUNT_NUBMER_LENGTH = 11;
    public static final Integer LOAN_ACCOUNT_NUBMER_LENGTH = 12;
    // Entity Table Name
    public static final String TRANSACTION_ENTITY = "TransactionRecord";
    public static final String SERVICE_CHARGE_ENTITY = "ServiceCharge";
    
    // DEMO
    public static final String DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME = "Merlion Account";
    public static final String DEMO_SAVING1_DEPOSIT_PRODUCT_NAME = "Monthly Savings Account";
    public static final String DEMO_SAVING2_DEPOSIT_PRODUCT_NAME = "Bonus+Savings";
    public static final String DEMO_CURRENT_DEPOSIT_PRODUCT_NAME = "MBS Current Account";
    public static final String DEMO_FIXED_DEPOSIT_PRODUCT_NAME = "Time Deposit";
    
    public static final String DEMO_MAIN_ACCOUNT_USER_ID = "c1234567";
    public static final String SUPER_ADMIN_USERNAME = "adminadmin";
    public static final String SUPER_ADMIN_PASSWORD = HashPwdUtils.hashPwd("password");
    public static final String CUSTOMER_SERVICE_USERNAME = "customer_service";
    public static final String FINANCIAL_ANALYST_USERNAME = "financial_analyst";
    public static final String FINANCIAL_OFFICER_USERNAME = "financial_officer";
    public static final String GENERAL_TELLER_USERNAME = "general_teller";
    public static final String LOAN_OFFICIER_USERNAME = "loan_officer";
    public static final String PRODUCT_MANAGER_USERNAME = "product_manager";
    public static final String STAFF_DEMO_PASSWORD = HashPwdUtils.hashPwd("password");
    
    // General
    public static final String SOMETHING_WENT_WRONG = "Something went wrong.";
    // Password
    public static final String OLD_PASSWORD_NOTMTACH = "You have entered a wrong password! ";
    public static final String PASSWORD_CHANGE_SUCCESS = "Successful! You have reset your password. ";
}
