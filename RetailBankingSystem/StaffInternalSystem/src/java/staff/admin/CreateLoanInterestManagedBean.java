/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.loan.LoanProductSessionBeanLocal;
import entity.dto.LoanRangeInterestDTO;
import entity.loan.LoanInterest;
import entity.loan.LoanInterestCollection;
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
@Named(value = "createLoanInterestManagedBean")
@ViewScoped
public class CreateLoanInterestManagedBean implements Serializable {

    @EJB
    private LoanProductSessionBeanLocal loanProductBean;

    private String selectedInterestType = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL.toString();
    private String loanName;
    private Double loanRate;

    // added interests
    private List<LoanInterest> addedLoanInterests = new ArrayList<>();
    // collection dtos
    private List<LoanRangeInterestDTO> collectionDTOs = new ArrayList<>();
    // loan type
    private final String PERSONAL_LOAN_TYPE = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PERSONAL.toString();
    private final String CAR_LOAN_TYPE = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_CAR.toString();
    private final String HDB_LOAN_TYPE = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_HDB.toString();
    private final String PP_LOAN_TYPE = EnumUtils.LoanProductType.LOAN_PRODUCT_TYPE_PRIVATE_HOUSE.toString();
    // external interest type
    private final String NO_EXTERNAL_INTEREST = "NO_EXTERNAL_INTEREST";
    private final String FHR18_INTEREST = "FHR18_INTEREST";
    private final String SIBOR_INTEREST = "SIBOR_INTEREST";

    /**
     * Creates a new instance of CreateLoanInterestManagedBean
     */
    public CreateLoanInterestManagedBean() {
    }

    @PostConstruct
    public void init() {
        addedLoanInterests = loanProductBean.getAllLoanInterest();
        initCollection();
    }

    public void createLoanInterest() {
        if (selectedInterestType.equals(PERSONAL_LOAN_TYPE) || selectedInterestType.equals(CAR_LOAN_TYPE)) {
            LoanInterest newLoanInterest = new LoanInterest();
            newLoanInterest.setEndMonth(-1);
            newLoanInterest.setStartMonth(0);
            newLoanInterest.setName(loanName);
            newLoanInterest.setInterestRate(loanRate);
            if (loanProductBean.createLoanInterest(newLoanInterest) != null) {
                addedLoanInterests.add(newLoanInterest);
                MessageUtils.displayInfo("Loan Interest Created");
            } else {
                MessageUtils.displayError("This Loan Interest already Exists");
            }
        } else {
            List<LoanInterest> tobeSaved = new ArrayList<>();

            for (LoanRangeInterestDTO dto : collectionDTOs) {
                LoanInterest newLoanInterest = new LoanInterest();
                newLoanInterest.setEndMonth(dto.getEndMonth());
                newLoanInterest.setStartMonth(dto.getStartMonth());
                newLoanInterest.setName(loanName + dto.getStartMonth() + "-" + dto.getEndMonth());
                newLoanInterest.setInterestRate(dto.getInterestRate());
                if (dto.getExternalInteret().equals(FHR18_INTEREST)) {
                    newLoanInterest.setFhr18(Boolean.TRUE);
                } else if (dto.getExternalInteret().equals(SIBOR_INTEREST)) {
                    newLoanInterest.setLoanExternalInterest(loanProductBean.getSIBORInterest());
                }
                if (loanProductBean.createLoanInterest(newLoanInterest) != null) {
                    addedLoanInterests.add(newLoanInterest);
                    tobeSaved.add(newLoanInterest);
                }
            }

            LoanInterestCollection newLoanCollection = new LoanInterestCollection();
            newLoanCollection.setName(loanName);
            newLoanCollection.setLoanInterests(tobeSaved);
            
            if (loanProductBean.createInterestCollection(newLoanCollection) != null) {
                MessageUtils.displayInfo("Loan Interest Collection Created");
            } else {
                MessageUtils.displayError("This Loan Interest already Exists");
            }
        }
        loanName = "";
        loanRate = 0.0;
    }

    private void initCollection() {
        LoanRangeInterestDTO i1 = new LoanRangeInterestDTO();
        i1.setStartMonth(0);
        i1.setEndMonth(12);
        i1.setInterestRate(0.018);
        i1.setExternalInteret("NO_EXTERNAL_INTEREST");
        getCollectionDTOs().add(i1);

        LoanRangeInterestDTO i2 = new LoanRangeInterestDTO();
        i2.setStartMonth(13);
        i2.setEndMonth(24);
        i2.setInterestRate(0.018);
        i2.setExternalInteret("NO_EXTERNAL_INTEREST");
        getCollectionDTOs().add(i2);

        LoanRangeInterestDTO i3 = new LoanRangeInterestDTO();
        i3.setStartMonth(25);
        i3.setEndMonth(36);
        i3.setInterestRate(0.018);
        i3.setExternalInteret("NO_EXTERNAL_INTEREST");
        getCollectionDTOs().add(i3);

        LoanRangeInterestDTO i4 = new LoanRangeInterestDTO();
        i4.setStartMonth(37);
        i4.setEndMonth(-1);
        i4.setInterestRate(0.018);
        i4.setExternalInteret("NO_EXTERNAL_INTEREST");
        getCollectionDTOs().add(i4);
    }

    /**
     * @return the NO_EXTERNAL_INTEREST
     */
    public String getNO_EXTERNAL_INTEREST() {
        return NO_EXTERNAL_INTEREST;
    }

    /**
     * @return the FHR18_INTEREST
     */
    public String getFHR18_INTEREST() {
        return FHR18_INTEREST;
    }

    /**
     * @return the SIBOR_INTEREST
     */
    public String getSIBOR_INTEREST() {
        return SIBOR_INTEREST;
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
     * @return the selectedInterestType
     */
    public String getSelectedInterestType() {
        return selectedInterestType;
    }

    /**
     * @param selectedInterestType the selectedInterestType to set
     */
    public void setSelectedInterestType(String selectedInterestType) {
        this.selectedInterestType = selectedInterestType;
    }

    /**
     * @return the addedLoanInterests
     */
    public List<LoanInterest> getAddedLoanInterests() {
        return addedLoanInterests;
    }

    /**
     * @param addedLoanInterests the addedLoanInterests to set
     */
    public void setAddedLoanInterests(List<LoanInterest> addedLoanInterests) {
        this.addedLoanInterests = addedLoanInterests;
    }

    /**
     * @return the collectionDTOs
     */
    public List<LoanRangeInterestDTO> getCollectionDTOs() {
        return collectionDTOs;
    }

    /**
     * @param collectionDTOs the collectionDTOs to set
     */
    public void setCollectionDTOs(List<LoanRangeInterestDTO> collectionDTOs) {
        this.collectionDTOs = collectionDTOs;
    }

    /**
     * @return the loanName
     */
    public String getLoanName() {
        return loanName;
    }

    /**
     * @param loanName the loanName to set
     */
    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    /**
     * @return the loanRate
     */
    public Double getLoanRate() {
        return loanRate;
    }

    /**
     * @param loanRate the loanRate to set
     */
    public void setLoanRate(Double loanRate) {
        this.loanRate = loanRate;
    }

}
