/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdb;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "fastManagedBean")
@ViewScoped
public class FastManagedBean implements Serializable {

    private String name = "FAST Simmulator";
    /**
     * Creates a new instance of FastManagedBean
     */
    public FastManagedBean() {
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
