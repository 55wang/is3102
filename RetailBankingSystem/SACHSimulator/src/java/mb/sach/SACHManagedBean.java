/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.sach;

import ejb.session.bean.SACHSessionBean;
import entity.PaymentTransfer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author leiyang
 */
@Named(value = "sachManagedBean")
@ViewScoped
public class SACHManagedBean implements Serializable {
    
    private final String MBS_NET_SETTLEMENT_PATH = "https://localhost:8181/StaffInternalSystem/rest/net_settlement";
    
    @EJB
    private SACHSessionBean sachBean;
    
    public SACHManagedBean() {
    }
    
    public void sendMBSNetSettlement() {
        BigDecimal netSettlementAmount = BigDecimal.ZERO;
        List<PaymentTransfer> results = sachBean.findAll();
        for (PaymentTransfer pt : results) {
            netSettlementAmount = netSettlementAmount.add(pt.getAmount());
        }
        
        // send to mbs
        Form form = new Form(); //bank info
        form.param("netSettlementAmount", netSettlementAmount.toString());

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MBS_NET_SETTLEMENT_PATH);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        
        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {
            System.out.println("Request received");
        } else {
            System.out.println("FAIL");
        }
    }
}
