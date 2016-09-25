/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.request;

import java.awt.Toolkit;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import report.ReportGenerationBean;
import utils.RedirectUtils;

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
            
//            System.setProperty("java.awt.headless", "true"); 
//            Toolkit tk = Toolkit.getDefaultToolkit();
// 
           ReportGenerationBean bean = new ReportGenerationBean();
//            System.out.println("11");
            
           boolean bl = bean.generateTestReport();
           RedirectUtils.redirect("view_estatement.xhtml");
//           System.out.println(bl);
//            System.out.println("22");
            
            return true;
        }
        catch(Exception ex){
  //          System.out.println(ex.printStackTrace());
            System.out.println("RequestManagedBean.requestEStatement: " + ex.toString());
            return false;
        }
    }
    
//    public static void main(String args[]) {
//        RequestManagedBean bean = new RequestManagedBean();
//          bean.requestEStatement();
//    }
//    
}
