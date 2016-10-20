/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.settlement;

import ejb.session.bean.MEPSSessionBean;
import entity.SettlementAccount;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;

/**
 *
 * @author leiyang
 */
@Path("meps_settlement_agency")
public class MEPSAgencySettlementService {
    @EJB
    private MEPSSessionBean mepsBean;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("toBankCode") String toBankCode,
            @FormParam("agencyCode") String agencyCode,
            @FormParam("netSettlementAmount") String netSettlementAmount,
            @FormParam("referenceNumber") String referenceNumber
    ) {
        System.out.println("Received fromBankCode:" + fromBankCode);
        System.out.println("Received toBankCode:" + toBankCode);
        System.out.println("Received agencyCode:" + agencyCode);
        System.out.println("Received referenceNumber:" + referenceNumber);
        System.out.println("Received netSettlementAmount:" + netSettlementAmount);
        System.out.println("Received POST http meps_settlement");

        SettlementAccount fromAccount = mepsBean.find(fromBankCode);
        SettlementAccount agencyAccount = mepsBean.find(agencyCode);
        
        fromAccount.removeBalance(new BigDecimal(netSettlementAmount));
        agencyAccount.addBalance(new BigDecimal(netSettlementAmount));
        
        mepsBean.merge(fromAccount);
        mepsBean.merge(agencyAccount);
        
        // inform
        if (agencyCode.equals("000")) {
            System.out.println("Net Settlement Amount");
            mepsBean.informSACHSettlement(fromBankCode, toBankCode, netSettlementAmount);
        } else if (agencyCode.equals("111")) {
            System.out.println("Net Settlement Amount");
            mepsBean.informFASTSettlement(fromBankCode, toBankCode, netSettlementAmount, referenceNumber);
        }
        
        System.out.println("Sending back meps_settlement response");
        MessageDTO err = new MessageDTO();
        err.setCode(0);
        err.setMessage("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }
}
