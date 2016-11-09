/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.loan.LoanProductSessionBeanLocal;
import entity.loan.LoanInterestCollection;
import entity.loan.LoanProduct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createLoanProductManagedBean")
@ViewScoped
public class CreateLoanProductManagedBean implements Serializable {

    @EJB
    private LoanProductSessionBeanLocal loanProductBean;

    private LoanProduct newLoanProduct;

    private String selectedProductType = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL.toString();
    private Long selectedInterestCollectionId;

    private List<LoanProduct> addedLoanProducts = new ArrayList<>();
    // available interest
    private List<LoanInterestCollection> personalInterests = new ArrayList<>();
    private List<LoanInterestCollection> carInterests = new ArrayList<>();
    private List<LoanInterestCollection> hdbInterests = new ArrayList<>();
    private List<LoanInterestCollection> ppInterest = new ArrayList<>();
    // loan type
    private final String PERSONAL_LOAN_TYPE = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL.toString();
    private final String CAR_LOAN_TYPE = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR.toString();
    private final String HDB_LOAN_TYPE = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB.toString();
    private final String PP_LOAN_TYPE = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PRIVATE_HOUSE.toString();

    /**
     * Creates a new instance of CreateLoanProductManagedBean
     */
    public CreateLoanProductManagedBean() {
    }

    @PostConstruct
    public void init() {
        setNewLoanProduct(new LoanProduct());
        addedLoanProducts = loanProductBean.getAllLoanProduct();
        setPersonalInterests(loanProductBean.getAllPersonalLoanInterestCollection());
        setCarInterests(loanProductBean.getAllCarLoanInterestCollection());
        setHdbInterests(loanProductBean.getAllHDBLoanInterestCollection());
        setPpInterest(loanProductBean.getAllPPLoanInterestCollection());
    }

    public void createLoanProduct() {
        LoanInterestCollection lic=new LoanInterestCollection();
        lic=loanProductBean.getInterestCollectionById(selectedInterestCollectionId);
        newLoanProduct.setProductType(EnumUtils.LoanProductType.getEnum(selectedProductType));
        newLoanProduct.setLoanInterestCollection(lic);
        

        LoanProduct result = loanProductBean.createLoanProduct(newLoanProduct);
        if (result != null) {
            addedLoanProducts.add(result);
            lic.setLoanProduct(newLoanProduct);
            loanProductBean.updateInterestCollection(lic);
            newLoanProduct = new LoanProduct();
            MessageUtils.displayInfo("Loan Product Created");
        } else {
            MessageUtils.displayError("This Loan Product already Exists");
        }
    }

    /**
     * @return the PERSONAL_LOAN_TYPE
     */
    public String getPERSONAL_LOAN_TYPE() {
        return PERSONAL_LOAN_TYPE;
    }

    /**
     * @return the CAR_LOAN_TYPE
     */
    public String getCAR_LOAN_TYPE() {
        return CAR_LOAN_TYPE;
    }

    /**
     * @return the HDB_LOAN_TYPE
     */
    public String getHDB_LOAN_TYPE() {
        return HDB_LOAN_TYPE;
    }

    /**
     * @return the PP_LOAN_TYPE
     */
    public String getPP_LOAN_TYPE() {
        return PP_LOAN_TYPE;
    }

    /**
     * @return the selectedProductType
     */
    public String getSelectedProductType() {
        return selectedProductType;
    }

    /**
     * @param selectedProductType the selectedProductType to set
     */
    public void setSelectedProductType(String selectedProductType) {
        this.selectedProductType = selectedProductType;
    }

    /**
     * @return the hdbInterests
     */
    public List<LoanInterestCollection> getHdbInterests() {
        return hdbInterests;
    }

    /**
     * @param hdbInterests the hdbInterests to set
     */
    public void setHdbInterests(List<LoanInterestCollection> hdbInterests) {
        this.hdbInterests = hdbInterests;
    }

    /**
     * @return the ppInterest
     */
    public List<LoanInterestCollection> getPpInterest() {
        return ppInterest;
    }

    /**
     * @param ppInterest the ppInterest to set
     */
    public void setPpInterest(List<LoanInterestCollection> ppInterest) {
        this.ppInterest = ppInterest;
    }

    /**
     * @return the personalInterests
     */
    public List<LoanInterestCollection> getPersonalInterests() {
        return personalInterests;
    }

    /**
     * @param personalInterests the personalInterests to set
     */
    public void setPersonalInterests(List<LoanInterestCollection> personalInterests) {
        this.personalInterests = personalInterests;
    }

    /**
     * @return the carInterests
     */
    public List<LoanInterestCollection> getCarInterests() {
        return carInterests;
    }

    /**
     * @param carInterests the carInterests to set
     */
    public void setCarInterests(List<LoanInterestCollection> carInterests) {
        this.carInterests = carInterests;
    }

    /**
     * @return the newLoanProduct
     */
    public LoanProduct getNewLoanProduct() {
        return newLoanProduct;
    }

    /**
     * @param newLoanProduct the newLoanProduct to set
     */
    public void setNewLoanProduct(LoanProduct newLoanProduct) {
        this.newLoanProduct = newLoanProduct;
    }

    /**
     * @return the selectedInterestCollectionId
     */
    public Long getSelectedInterestCollectionId() {
        return selectedInterestCollectionId;
    }

    /**
     * @param selectedInterestCollectionId the selectedInterestCollectionId to
     * set
     */
    public void setSelectedInterestCollectionId(Long selectedInterestCollectionId) {
        this.selectedInterestCollectionId = selectedInterestCollectionId;
    }

    /**
     * @return the addedLoanProducts
     */
    public List<LoanProduct> getAddedLoanProducts() {
        return addedLoanProducts;
    }

    /**
     * @param addedLoanProducts the addedLoanProducts to set
     */
    public void setAddedLoanProducts(List<LoanProduct> addedLoanProducts) {
        this.addedLoanProducts = addedLoanProducts;
    }
}
