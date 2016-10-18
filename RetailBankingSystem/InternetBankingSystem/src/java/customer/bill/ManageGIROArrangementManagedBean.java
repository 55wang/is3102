/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.bill;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.bill.GiroArrangement;
import entity.bill.Organization;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import utils.JSUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "manageGIROArrangementManagedBean")
@ViewScoped
public class ManageGIROArrangementManagedBean implements Serializable {
    
    @EJB
    private BillSessionBeanLocal billBean;
    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    private String selectedBillId;
    private String referenceNumber;
    private String fromAccountNo;
    private Double billLimit;
    private GiroArrangement giroArr = new GiroArrangement();
    private MainAccount ma;
    private List<Organization> billOrgsOptions;
    private List<DepositAccount> accounts = new ArrayList<>();
    private List<GiroArrangement> addedGiroArrs;
    
    public ManageGIROArrangementManagedBean() {}
    
    @PostConstruct
    public void init() {
        setBillOrgsOptions(billBean.getActiveListOrganization());
        setMa(loginBean.getMainAccountByUserID(SessionUtils.getUserName()));
        setAddedGiroArrs(billBean.getGiroArrsByMainAccountId(getMa().getId()));
        setAccounts(ma.getBankAcounts());
    }
    
    public void addGIROArrangement() {
        DepositAccount da = depositBean.getAccountFromId(getFromAccountNo());
        Organization o = billBean.getOrganizationById(Long.parseLong(selectedBillId));
        giroArr.setOrganization(o);
        giroArr.setBillReference(referenceNumber);
        giroArr.setMainAccount(getMa());
        giroArr.setDepositAccount(da);
        giroArr.setBillLimit(billLimit);
        GiroArrangement result = billBean.createGiroArr(giroArr);
        if (result != null) {
            JSUtils.callJSMethod("PF('myWizard').next()");
            addedGiroArrs.add(result);
            MessageUtils.displayInfo(ConstantUtils.GIRO_SUCCESS);
        } else {
            JSUtils.callJSMethod("PF('myWizard').back()");
            MessageUtils.displayError(ConstantUtils.GIRO_FAILED);
        }
    }
    
    public String getOrgName(String selectedBillId) {
        Organization o = billBean.getOrganizationById(Long.parseLong(selectedBillId));
        return o.getName();
    }
    
    public void removeGIROArrangement(GiroArrangement g) {
        String result = billBean.deleteGiroArrById(g.getId());
        if (result.equals("SUCCESS")) {
            addedGiroArrs.remove(g);
            MessageUtils.displayInfo(ConstantUtils.GIRO_DELETE_SUCCESS);
        } else {
            MessageUtils.displayError(ConstantUtils.GIRO_DELETE_FAILED);
        }
    }
    
    public void onCellEdit(GiroArrangement g) {
        GiroArrangement result = billBean.updateGiroArr(g);
         
        if (result != null) {
            MessageUtils.displayInfo(ConstantUtils.GIRO_LIMIT_SUCCESS);
        } else {
            MessageUtils.displayInfo(ConstantUtils.GIRO_LIMIT_FAIL);
        }
    }

    // TODO: Change limit
    
    /**
     * @return the selectedBillId
     */
    public String getSelectedBillId() {
        return selectedBillId;
    }

    /**
     * @param selectedBillId the selectedBillId to set
     */
    public void setSelectedBillId(String selectedBillId) {
        this.selectedBillId = selectedBillId;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the fromAccountNo
     */
    public String getFromAccountNo() {
        return fromAccountNo;
    }

    /**
     * @param fromAccountNo the fromAccountNo to set
     */
    public void setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    /**
     * @return the ma
     */
    public MainAccount getMa() {
        return ma;
    }

    /**
     * @param ma the ma to set
     */
    public void setMa(MainAccount ma) {
        this.ma = ma;
    }

    /**
     * @return the billOrgsOptions
     */
    public List<Organization> getBillOrgsOptions() {
        return billOrgsOptions;
    }

    /**
     * @param billOrgsOptions the billOrgsOptions to set
     */
    public void setBillOrgsOptions(List<Organization> billOrgsOptions) {
        this.billOrgsOptions = billOrgsOptions;
    }

    /**
     * @return the giroArr
     */
    public GiroArrangement getGiroArr() {
        return giroArr;
    }

    /**
     * @param giroArr the giroArr to set
     */
    public void setGiroArr(GiroArrangement giroArr) {
        this.giroArr = giroArr;
    }

    /**
     * @return the addedGiroArrs
     */
    public List<GiroArrangement> getAddedGiroArrs() {
        return addedGiroArrs;
    }

    /**
     * @param addedGiroArrs the addedGiroArrs to set
     */
    public void setAddedGiroArrs(List<GiroArrangement> addedGiroArrs) {
        this.addedGiroArrs = addedGiroArrs;
    }

    /**
     * @return the accounts
     */
    public List<DepositAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<DepositAccount> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return the billLimit
     */
    public Double getBillLimit() {
        return billLimit;
    }

    /**
     * @param billLimit the billLimit to set
     */
    public void setBillLimit(Double billLimit) {
        this.billLimit = billLimit;
    }
}
