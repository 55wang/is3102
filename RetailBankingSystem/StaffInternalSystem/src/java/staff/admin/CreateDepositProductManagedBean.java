/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.dams.DepositProductSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.dams.account.DepositAccountProduct;
import entity.dams.account.DepositProduct;
import entity.dams.account.FixedDepositAccountProduct;
import entity.dams.rules.Interest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.DepositAccountType;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createDepositProductManagedBean")
@ViewScoped
public class CreateDepositProductManagedBean implements Serializable {
    
    @EJB
    private DepositProductSessionBeanLocal depositProductSessionBean;
    @EJB
    private InterestSessionBeanLocal interestSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    
    private String[] selectedInterests;
    private Map<String, String> selectInterests = new HashMap<>();
    private List<Interest> interests = new ArrayList<>();
    
    private DepositAccountProduct customProduct = new DepositAccountProduct();
    private DepositAccountProduct currentProduct = new DepositAccountProduct();
    private DepositAccountProduct savingProduct = new DepositAccountProduct();
    private FixedDepositAccountProduct fixedProduct = new FixedDepositAccountProduct();
    
    private List<DepositAccountProduct> customProducts = new ArrayList<>();
    private List<DepositAccountProduct> currentProducts = new ArrayList<>();
    private List<DepositAccountProduct> savingProducts = new ArrayList<>();
    private List<FixedDepositAccountProduct> fixedProducts = new ArrayList<>();
    
    private DepositAccountType productType;
    private final DepositAccountType PRODUCT_TYPE_CUSTOM = DepositAccountType.CUSTOM;
    private final DepositAccountType PRODUCT_TYPE_CURRENT = DepositAccountType.CURRENT;
    private final DepositAccountType PRODUCT_TYPE_SAVING = DepositAccountType.SAVING;
    private final DepositAccountType PRODUCT_TYPE_FIXED = DepositAccountType.FIXED;
    
    public CreateDepositProductManagedBean() {}
    
    @PostConstruct
    public void init() {
        initProductsType();
        productType = PRODUCT_TYPE_CUSTOM;
        System.out.println("CreateDepositProductManagedBean: @PostConstruct");
        List<DepositProduct> products = depositProductSessionBean.getAllPresentProducts();
        System.out.println("Retrieved products: " + products.toString());
        for (DepositProduct p : products) {
            if (p instanceof DepositAccountProduct) {
                if (p.getType().equals(PRODUCT_TYPE_CUSTOM)) {
                    customProducts.add((DepositAccountProduct)p);
                } else if (p.getType().equals(PRODUCT_TYPE_CURRENT)) {
                    currentProducts.add((DepositAccountProduct)p);
                } else if (p.getType().equals(PRODUCT_TYPE_SAVING)) {
                    savingProducts.add((DepositAccountProduct)p);
                }
            } else if (p instanceof FixedDepositAccountProduct) {
                if (p.getType().equals(PRODUCT_TYPE_FIXED)) {
                    fixedProducts.add((FixedDepositAccountProduct)p);
                }
            } else {
                System.out.println("Should never come here.");
            }
        }
        
        interests = interestSessionBean.showAllPresentInterests();
        for (Interest i : interests) {
            selectInterests.put(i.getName(), i.getName());
        }
        
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter create_deposit_product.xhtml");
        a.setFunctionName("CreateDepositProductManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all deposit products");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    
    private void initProductsType(){
        getCustomProduct().setType(PRODUCT_TYPE_CUSTOM);
        currentProduct.setType(PRODUCT_TYPE_CURRENT);
        savingProduct.setType(PRODUCT_TYPE_SAVING);
        getFixedProduct().setType(PRODUCT_TYPE_FIXED);
    }
    
    public void toggleProductStatus(DepositProduct dp) {
        dp.setIsHistory(!dp.getIsHistory());
        depositProductSessionBean.updateDepositProduct(dp);
    } 
    
    public void addProduct(ActionEvent event) {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user add product");
        a.setFunctionName("CreateDepositManagedBean addProduct()");
        
        
        
        if (productType.equals(PRODUCT_TYPE_CUSTOM)) {
            customProduct.setInterestRules(getSelectedInterestsList());
            DepositAccountProduct temp = (DepositAccountProduct)depositProductSessionBean.createDepositProduct(customProduct);
            a.setFunctionInput(temp.getName());
            if (temp != null) {
                customProducts.add(temp);
                customProduct = new DepositAccountProduct();
                a.setFunctionOutput("SUCCESS");
                MessageUtils.displayInfo("Custom Deposit Product Created");
            } else {
                a.setFunctionOutput("FAIL");
                MessageUtils.displayError("This Deposit Product Exists");
            }
        } else if (productType.equals(PRODUCT_TYPE_CURRENT)) {
            currentProduct.setInterestRules(getSelectedInterestsList());
            DepositAccountProduct temp = (DepositAccountProduct) depositProductSessionBean.createDepositProduct(currentProduct);
            a.setFunctionInput(temp.getName());
            if (temp != null) {
                currentProducts.add(temp);
                currentProduct = new DepositAccountProduct();
                a.setFunctionOutput("SUCCESS");
                MessageUtils.displayInfo("Current Deposit Product Created");
            } else {
                a.setFunctionOutput("FAIL");
                MessageUtils.displayError("This Deposit Product Exists");
            }
        } else if (productType.equals(PRODUCT_TYPE_SAVING)) {
            savingProduct.setInterestRules(getSelectedInterestsList());
            DepositAccountProduct temp = (DepositAccountProduct) depositProductSessionBean.createDepositProduct(savingProduct);
            a.setFunctionInput(temp.getName());
            if (temp != null) {
                savingProducts.add(temp);
                savingProduct = new DepositAccountProduct();
                a.setFunctionOutput("SUCCESS");
                MessageUtils.displayInfo("Saving Deposit Product Created");
            } else {
                a.setFunctionOutput("FAIL");
                MessageUtils.displayError("This Deposit Product Exists");
            }
        } else if (productType.equals(PRODUCT_TYPE_FIXED)) {
            a.setFunctionInput(fixedProduct.getName());
            if (depositProductSessionBean.createDepositProduct(fixedProduct) != null) {
                fixedProducts.add(fixedProduct);
                fixedProduct = new FixedDepositAccountProduct();
                a.setFunctionOutput("SUCCESS");
                MessageUtils.displayInfo("Fixed Deposit Product Created");
            } else {
                a.setFunctionOutput("FAIL");
                MessageUtils.displayError("This Deposit Product Exists");
            }
        } else {
            MessageUtils.displayError("There's some error when creating interest");
        }
        initProductsType();
    }
    
    private List<Interest> getSelectedInterestsList() {
        List<Interest> result = new ArrayList<>();
        for (int i = 0; i < selectedInterests.length; i++){
            for (Interest interest : interests) {
                if (interest.getName().equals(selectedInterests[i])) {
                    System.out.println("Adding Interest: " + interest.getName());
                    result.add(interest);
                }
            }
        }
        return result;
    }

    /**
     * @return the productType
     */
    public DepositAccountType getProductType() {
        return productType;
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(DepositAccountType productType) {
        this.productType = productType;
    }

    /**
     * @return the PRODUCT_TYPE_CUSTOM
     */
    public DepositAccountType getPRODUCT_TYPE_CUSTOM() {
        return PRODUCT_TYPE_CUSTOM;
    }

    /**
     * @return the PRODUCT_TYPE_CURRENT
     */
    public DepositAccountType getPRODUCT_TYPE_CURRENT() {
        return PRODUCT_TYPE_CURRENT;
    }

    /**
     * @return the PRODUCT_TYPE_SAVING
     */
    public DepositAccountType getPRODUCT_TYPE_SAVING() {
        return PRODUCT_TYPE_SAVING;
    }

    /**
     * @return the PRODUCT_TYPE_FIXED
     */
    public DepositAccountType getPRODUCT_TYPE_FIXED() {
        return PRODUCT_TYPE_FIXED;
    }

    /**
     * @return the customProduct
     */
    public DepositAccountProduct getCustomProduct() {
        return customProduct;
    }

    /**
     * @param customProduct the customProduct to set
     */
    public void setCustomProduct(DepositAccountProduct customProduct) {
        this.customProduct = customProduct;
    }

    /**
     * @return the currentProduct
     */
    public DepositAccountProduct getCurrentProduct() {
        return currentProduct;
    }

    /**
     * @param currentProduct the currentProduct to set
     */
    public void setCurrentProduct(DepositAccountProduct currentProduct) {
        this.currentProduct = currentProduct;
    }

    /**
     * @return the savingProduct
     */
    public DepositAccountProduct getSavingProduct() {
        return savingProduct;
    }

    /**
     * @param savingProduct the savingProduct to set
     */
    public void setSavingProduct(DepositAccountProduct savingProduct) {
        this.savingProduct = savingProduct;
    }

    /**
     * @return the fixedProduct
     */
    public FixedDepositAccountProduct getFixedProduct() {
        return fixedProduct;
    }

    /**
     * @param fixedProduct the fixedProduct to set
     */
    public void setFixedProduct(FixedDepositAccountProduct fixedProduct) {
        this.fixedProduct = fixedProduct;
    }

    /**
     * @return the customProducts
     */
    public List<DepositAccountProduct> getCustomProducts() {
        return customProducts;
    }

    /**
     * @param customProducts the customProducts to set
     */
    public void setCustomProducts(List<DepositAccountProduct> customProducts) {
        this.customProducts = customProducts;
    }

    /**
     * @return the currentProducts
     */
    public List<DepositAccountProduct> getCurrentProducts() {
        return currentProducts;
    }

    /**
     * @param currentProducts the currentProducts to set
     */
    public void setCurrentProducts(List<DepositAccountProduct> currentProducts) {
        this.currentProducts = currentProducts;
    }

    /**
     * @return the savingProducts
     */
    public List<DepositAccountProduct> getSavingProducts() {
        return savingProducts;
    }

    /**
     * @param savingProducts the savingProducts to set
     */
    public void setSavingProducts(List<DepositAccountProduct> savingProducts) {
        this.savingProducts = savingProducts;
    }

    /**
     * @return the fixedProducts
     */
    public List<FixedDepositAccountProduct> getFixedProducts() {
        return fixedProducts;
    }

    /**
     * @param fixedProducts the fixedProducts to set
     */
    public void setFixedProducts(List<FixedDepositAccountProduct> fixedProducts) {
        this.fixedProducts = fixedProducts;
    }

    /**
     * @return the selectedInterests
     */
    public String[] getSelectedInterests() {
        return selectedInterests;
    }

    /**
     * @param selectedInterests the selectedInterests to set
     */
    public void setSelectedInterests(String[] selectedInterests) {
        this.selectedInterests = selectedInterests;
    }

    /**
     * @return the selectInterests
     */
    public Map<String, String> getSelectInterests() {
        return selectInterests;
    }

    /**
     * @param selectInterests the selectInterests to set
     */
    public void setSelectInterests(Map<String, String> selectInterests) {
        this.selectInterests = selectInterests;
    }
}
