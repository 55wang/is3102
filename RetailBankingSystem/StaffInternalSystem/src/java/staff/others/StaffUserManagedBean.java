/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.others;

import entity.Role.Permission;
import entity.StaffAccount;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import utils.LoggingUtil;
import utils.SessionUtils;
import utils.UserUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffUserManagedBean")
@SessionScoped
public class StaffUserManagedBean implements Serializable {

    private Permission PERMISSION_SUPERUSER = Permission.SUPERUSER;
    private Permission PERMISSION_CUSTOMER = Permission.CUSTOMER;
    private Permission PERMISSION_DEPOSIT = Permission.DEPOSIT;
    private Permission PERMISSION_CARD = Permission.CARD;
    private Permission PERMISSION_LOAN = Permission.LOAN;
    private Permission PERMISSION_BILL = Permission.BILL;
    private Permission PERMISSION_WEALTH = Permission.WEALTH;
    private Permission PERMISSION_PORTFOLIO = Permission.PORTFOLIO;
    private Permission PERMISSION_ANALYTICS = Permission.ANALYTICS;

    @PostConstruct
    public void init() {
        LoggingUtil.StaffMessageLog(StaffUserManagedBean.class, "StaffUserManagedBean @PostConstruct init");
    }
    @PreDestroy
    public void deinit() {
        LoggingUtil.StaffMessageLog(StaffUserManagedBean.class, "StaffUserManagedBean @PostConstruct deinit");
    }
    /**
     * Creates a new instance of StaffUserManagedBean
     */
    public StaffUserManagedBean() {}
    
    public String getUserName() {
        return SessionUtils.getStaffUsername();
    }
    
    public StaffAccount getStaff() {
        return SessionUtils.getStaff();
    }
    
    public Boolean isUserInRole(Permission p) {
        return UserUtils.isUserInRole(p);
    }
    
    public Boolean isUserInRoles(List<Permission> roles) {
        return UserUtils.isUserInRoles(roles);
    }

    /**
     * @return the PERMISSION_SUPERUSER
     */
    public Permission getPERMISSION_SUPERUSER() {
        return PERMISSION_SUPERUSER;
    }

    /**
     * @param PERMISSION_SUPERUSER the PERMISSION_SUPERUSER to set
     */
    public void setPERMISSION_SUPERUSER(Permission PERMISSION_SUPERUSER) {
        this.PERMISSION_SUPERUSER = PERMISSION_SUPERUSER;
    }

    /**
     * @return the PERMISSION_CUSTOMER
     */
    public Permission getPERMISSION_CUSTOMER() {
        return PERMISSION_CUSTOMER;
    }

    /**
     * @param PERMISSION_CUSTOMER the PERMISSION_CUSTOMER to set
     */
    public void setPERMISSION_CUSTOMER(Permission PERMISSION_CUSTOMER) {
        this.PERMISSION_CUSTOMER = PERMISSION_CUSTOMER;
    }

    /**
     * @return the PERMISSION_DEPOSIT
     */
    public Permission getPERMISSION_DEPOSIT() {
        return PERMISSION_DEPOSIT;
    }

    /**
     * @param PERMISSION_DEPOSIT the PERMISSION_DEPOSIT to set
     */
    public void setPERMISSION_DEPOSIT(Permission PERMISSION_DEPOSIT) {
        this.PERMISSION_DEPOSIT = PERMISSION_DEPOSIT;
    }

    /**
     * @return the PERMISSION_CARD
     */
    public Permission getPERMISSION_CARD() {
        return PERMISSION_CARD;
    }

    /**
     * @param PERMISSION_CARD the PERMISSION_CARD to set
     */
    public void setPERMISSION_CARD(Permission PERMISSION_CARD) {
        this.PERMISSION_CARD = PERMISSION_CARD;
    }

    /**
     * @return the PERMISSION_LOAN
     */
    public Permission getPERMISSION_LOAN() {
        return PERMISSION_LOAN;
    }

    /**
     * @param PERMISSION_LOAN the PERMISSION_LOAN to set
     */
    public void setPERMISSION_LOAN(Permission PERMISSION_LOAN) {
        this.PERMISSION_LOAN = PERMISSION_LOAN;
    }

    /**
     * @return the PERMISSION_BILL
     */
    public Permission getPERMISSION_BILL() {
        return PERMISSION_BILL;
    }

    /**
     * @param PERMISSION_BILL the PERMISSION_BILL to set
     */
    public void setPERMISSION_BILL(Permission PERMISSION_BILL) {
        this.PERMISSION_BILL = PERMISSION_BILL;
    }

    /**
     * @return the PERMISSION_WEALTH
     */
    public Permission getPERMISSION_WEALTH() {
        return PERMISSION_WEALTH;
    }

    /**
     * @param PERMISSION_WEALTH the PERMISSION_WEALTH to set
     */
    public void setPERMISSION_WEALTH(Permission PERMISSION_WEALTH) {
        this.PERMISSION_WEALTH = PERMISSION_WEALTH;
    }

    /**
     * @return the PERMISSION_PORTFOLIO
     */
    public Permission getPERMISSION_PORTFOLIO() {
        return PERMISSION_PORTFOLIO;
    }

    /**
     * @param PERMISSION_PORTFOLIO the PERMISSION_PORTFOLIO to set
     */
    public void setPERMISSION_PORTFOLIO(Permission PERMISSION_PORTFOLIO) {
        this.PERMISSION_PORTFOLIO = PERMISSION_PORTFOLIO;
    }

    /**
     * @return the PERMISSION_ANALYTICS
     */
    public Permission getPERMISSION_ANALYTICS() {
        return PERMISSION_ANALYTICS;
    }

    /**
     * @param PERMISSION_ANALYTICS the PERMISSION_ANALYTICS to set
     */
    public void setPERMISSION_ANALYTICS(Permission PERMISSION_ANALYTICS) {
        this.PERMISSION_ANALYTICS = PERMISSION_ANALYTICS;
    }
}
