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
import ejb.session.report.ReportGenerationBean;
import ejb.session.report.ReportGenerationBeanLocal;
import javax.ejb.EJB;
import server.utilities.DateUtils;
import utils.RedirectUtils;

/**
 *
 * @author litong
 */
@Named(value = "requestManagedBean")
@ViewScoped
public class RequestManagedBean implements Serializable{

    @EJB
    private ReportGenerationBeanLocal reportBean;
    /**
     * Creates a new instance of RequestManagedBean
     */
    public RequestManagedBean() {
    }
    
    public Boolean requestEStatement(){
        try{
           reportBean.generateMonthlyDepositAccountTransactionReport("07671444540", DateUtils.getBeginOfMonth(), DateUtils.getEndOfMonth());
           RedirectUtils.redirect("view_estatement.xhtml");
            return true;
        }
        catch(Exception ex){
            System.out.println("RequestManagedBean.requestEStatement: " + ex.toString());
            return false;
        }
    }
}
