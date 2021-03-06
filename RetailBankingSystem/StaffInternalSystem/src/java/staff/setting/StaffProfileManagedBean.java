/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.setting;

import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.StaffAccount;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.Gender;
import server.utilities.EnumUtils.Nationality;
import server.utilities.CommonUtils;
import util.exception.common.UpdateStaffAccountException;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffProfileManagedBean")
@ViewScoped
public class StaffProfileManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private StaffAccount staff = SessionUtils.getStaff();
    private Boolean editingPage = false;
    private Boolean profileEdited = false;
    private String selectedGender;
    private String selectedNationality;
    private List<String> genderOptions = CommonUtils.getEnumList(Gender.class);
    private List<String> nationalityOptions = CommonUtils.getEnumList(Nationality.class);

    /**
     * Creates a new instance of StaffProfileManagedBean
     */
    public StaffProfileManagedBean() {
    }

    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter StaffProfileManagedBean");
        a.setFunctionName("StaffProfileManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all StaffProfileManagedBean");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);

        selectedGender = staff.getGender() == null ? null : staff.getGender().toString();
        selectedNationality = staff.getNationality() == null ? null : staff.getNationality().toString();
    }

    public void goToEditPage() {
        editingPage = true;
    }

    public void goToConfirmPage() {
        editingPage = false;
        profileEdited = true;
        staff.setGender(Gender.getEnum(selectedGender));
        staff.setNationality(Nationality.getEnum(selectedNationality));
    }

    public void save() {
        try {
            staff.setGender(Gender.getEnum(selectedGender));
            staff.setNationality(Nationality.getEnum(selectedNationality));
            StaffAccount result = staffBean.updateAccount(staff);
            SessionUtils.setStaffAccount(result);
            selectedGender = null;
            selectedNationality = null;
            RedirectUtils.redirect("staff_view_profile.xhtml");
        } catch (UpdateStaffAccountException e) {
            System.out.println("UpdateStaffAccountException StaffProfileManagedBean save()");
        }

    }

    /**
     * @return the staff
     */
    public StaffAccount getStaff() {
        return staff;
    }

    /**
     * @param staff the staff to set
     */
    public void setStaff(StaffAccount staff) {
        this.staff = staff;
    }

    /**
     * @return the editingPage
     */
    public Boolean getEditingPage() {
        return editingPage;
    }

    /**
     * @param editingPage the editingPage to set
     */
    public void setEditingPage(Boolean editingPage) {
        this.editingPage = editingPage;
    }

    /**
     * @return the profileEdited
     */
    public Boolean getProfileEdited() {
        return profileEdited;
    }

    /**
     * @param profileEdited the profileEdited to set
     */
    public void setProfileEdited(Boolean profileEdited) {
        this.profileEdited = profileEdited;
    }

    /**
     * @return the selectedGender
     */
    public String getSelectedGender() {
        return selectedGender;
    }

    /**
     * @param selectedGender the selectedGender to set
     */
    public void setSelectedGender(String selectedGender) {
        this.selectedGender = selectedGender;
    }

    /**
     * @return the selectedNationality
     */
    public String getSelectedNationality() {
        return selectedNationality;
    }

    /**
     * @param selectedNationality the selectedNationality to set
     */
    public void setSelectedNationality(String selectedNationality) {
        this.selectedNationality = selectedNationality;
    }

    /**
     * @return the genderOptions
     */
    public List<String> getGenderOptions() {
        return genderOptions;
    }

    /**
     * @param genderOptions the genderOptions to set
     */
    public void setGenderOptions(List<String> genderOptions) {
        this.genderOptions = genderOptions;
    }

    /**
     * @return the nationalityOptions
     */
    public List<String> getNationalityOptions() {
        return nationalityOptions;
    }

    /**
     * @param nationalityOptions the nationalityOptions to set
     */
    public void setNationalityOptions(List<String> nationalityOptions) {
        this.nationalityOptions = nationalityOptions;
    }
}
