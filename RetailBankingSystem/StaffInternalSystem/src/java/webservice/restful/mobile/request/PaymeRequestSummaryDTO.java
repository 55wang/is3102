/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.request;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leiyang
 */
public class PaymeRequestSummaryDTO {
    
    private List<PaymeRequestDTO> receivedRequests = new ArrayList<>();
    private List<PaymeRequestDTO> sentRequests = new ArrayList<>();

    /**
     * @return the receivedRequests
     */
    public List<PaymeRequestDTO> getReceivedRequests() {
        return receivedRequests;
    }

    /**
     * @param receivedRequests the receivedRequests to set
     */
    public void setReceivedRequests(List<PaymeRequestDTO> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    /**
     * @return the sentRequests
     */
    public List<PaymeRequestDTO> getSentRequests() {
        return sentRequests;
    }

    /**
     * @param sentRequests the sentRequests to set
     */
    public void setSentRequests(List<PaymeRequestDTO> sentRequests) {
        this.sentRequests = sentRequests;
    }
            
}
