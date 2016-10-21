/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wang
 */
@Named(value = "analyticalManagedBean")
@ViewScoped
public class AnalyticalCrmManagedBean implements Serializable {

    /**
     * Creates a new instance of AnalyticalManagedBean
     */
    public AnalyticalCrmManagedBean() {
    }

    public void retrieveMarketBasketAnalysis() {
        //return DTO
    }

}
