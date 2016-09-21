/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.NewCardProductSessionBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wang
 */
@Named(value = "cardProductManagedBean")
@ViewScoped
public class CardProductManagedBean implements Serializable{

    @EJB
    private NewCardProductSessionBeanLocal newProductSessionBeanLocal;
    /**
     * Creates a new instance of CardProductManagedBean
     */
    public CardProductManagedBean() {
    }
    
    
    
}
