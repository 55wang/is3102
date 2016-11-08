/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.meps;

import ejb.session.bean.MEPSSessionBean;
import java.io.Serializable;
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

    @EJB
    private MEPSSessionBean mepsBean;
    
    /**
     * Creates a new instance of testJSONManagedBean
     */
    public testJSONManagedBean() {
    }
    
    public void testClick() {
        System.out.println("GG");
        mepsBean.testMBS();
    }
    
}
