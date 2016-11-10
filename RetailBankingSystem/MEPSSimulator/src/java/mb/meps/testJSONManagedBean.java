/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.meps;

import ejb.session.bean.MEPSSessionBean;
import init.BankAccountBuilder;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "testJSONManagedBean")
@ViewScoped
public class testJSONManagedBean implements Serializable {

    private String name = "MEPS Simulator";
    
    @EJB
    private MEPSSessionBean mepsBean;
    @EJB
    private BankAccountBuilder builderBean;
    
    /**
     * Creates a new instance of testJSONManagedBean
     */
    public testJSONManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        builderBean.init();
    }
    
    public void testClick() {
        System.out.println("GG");
        mepsBean.testMBS();
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
