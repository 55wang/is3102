/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardNetworkSystem;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author wang
 */
@Named(value = "simulatedCallManagedBean")
@ViewScoped
public class SimulatedCallManagedBean {

    public SimulatedCallManagedBean() {
    }
    
    public void initiating() {
     Client client = ClientBuilder.newClient();
     
     
     
    }
    
}
