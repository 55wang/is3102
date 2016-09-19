/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class CustomAccount extends DepositAccount {
    
    private String name;
    private Boolean isMaster = Boolean.FALSE;

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

    /**
     * @return the isMaster
     */
    public Boolean getIsMaster() {
        return isMaster;
    }

    /**
     * @param isMaster the isMaster to set
     */
    public void setIsMaster(Boolean isMaster) {
        this.isMaster = isMaster;
    }
}
