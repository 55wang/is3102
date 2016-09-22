/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.CommonUtils;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "formDemoManagedBean")
@ViewScoped
public class formDemoManagedBean implements Serializable {

    private String selectedEnum;
    private List<String> selectEnums = CommonUtils.getEnumList(EnumUtils.Gender.class);
    /**
     * Creates a new instance of formDemoManagedBean
     */
    public formDemoManagedBean() {
    }

    /**
     * @return the selectedEnum
     */
    public String getSelectedEnum() {
        return selectedEnum;
    }

    /**
     * @param selectedEnum the selectedEnum to set
     */
    public void setSelectedEnum(String selectedEnum) {
        this.selectedEnum = selectedEnum;
    }

    /**
     * @return the selectEnums
     */
    public List<String> getSelectEnums() {
        return selectEnums;
    }

    /**
     * @param selectEnums the selectEnums to set
     */
    public void setSelectEnums(List<String> selectEnums) {
        this.selectEnums = selectEnums;
    }
}
