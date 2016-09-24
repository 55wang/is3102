/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.request;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import report.ReportGenerationBean;

/**
 *
 * @author litong
 */
@Named(value = "requestManagedBean")
@ViewScoped
public class RequestManagedBean implements Serializable{

    /**
     * Creates a new instance of RequestManagedBean
     */
    public RequestManagedBean() {
    }
    
    public Boolean requestEStatement(){
        try{
            ReportGenerationBean bean = new ReportGenerationBean();
            bean.generateTestReport();
            return true;
        }
        catch(Exception ex){
            System.out.println("RequestManagedBean.requestEStatement: " + ex.toString());
            return false;
        }
    }
    
}
