/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.request;

import ejb.session.dams.CurrentAccountChequeSessionBeanLocal;
import entity.dams.account.Cheque;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "customerViewChequeStatusManagedBean")
@ViewScoped
public class CustomerViewChequeStatusManagedBean implements Serializable {

    @EJB
    private CurrentAccountChequeSessionBeanLocal chequeBean;
    
    private List<Cheque> cheques;
    /**
     * Creates a new instance of CustomerViewChequeStatusManagedBean
     */
    public CustomerViewChequeStatusManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        setCheques(chequeBean.getChequeByMainAccountId(SessionUtils.getUserId()));
        System.out.println("Cheque length:" + cheques.size());
    }

    /**
     * @return the cheques
     */
    public List<Cheque> getCheques() {
        return cheques;
    }

    /**
     * @param cheques the cheques to set
     */
    public void setCheques(List<Cheque> cheques) {
        this.cheques = cheques;
    }
}
