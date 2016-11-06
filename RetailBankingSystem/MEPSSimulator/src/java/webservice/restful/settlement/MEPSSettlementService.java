/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.settlement;

import ejb.session.bean.MEPSSessionBean;
import entity.SettlementAccount;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
@Path("meps_settlement")
public class MEPSSettlementService {

    @EJB
    private MEPSSessionBean mepsBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("mbsCode") String mbsCode,
            @FormParam("mbsSettlementAmount") String mbsSettlementAmount,
            @FormParam("mbsName") String mbsName,
            @FormParam("citiCode") String citiCode,
            @FormParam("citiSettlementAmount") String citiSettlementAmount,
            @FormParam("citiName") String citiName,
            @FormParam("ocbcCode") String ocbcCode,
            @FormParam("ocbcSettlementAmount") String ocbcSettlementAmount,
            @FormParam("ocbcName") String ocbcName
    ) {
        System.out.println(".");
        System.out.println("[MEPS]:");
        System.out.println("Received Net Settlement from SACH:");
        System.out.println(".       " + mbsCode + " " + mbsName + ": " + mbsSettlementAmount);
        System.out.println(".       " + citiCode + " " + citiName + ": " + citiSettlementAmount);
        System.out.println(".       " + ocbcCode + " " + ocbcName + ": " + ocbcSettlementAmount);
        System.out.println("Received POST http meps_settlement");

        List<SettlementAccount> bankAccounts = mepsBean.retrieveThreeSettlementAccounts(mbsCode, citiCode, ocbcCode);
        System.out.println("Current Bank Account Balance:");
        System.out.println(".       " + bankAccounts.get(0).getBankCode() + " " + bankAccounts.get(0).getName() + ": " + bankAccounts.get(0).getAmount().setScale(4).toString());
        System.out.println(".       " + bankAccounts.get(1).getBankCode() + " " + bankAccounts.get(1).getName() + ": " + bankAccounts.get(1).getAmount().setScale(4).toString());
        System.out.println(".       " + bankAccounts.get(2).getBankCode() + " " + bankAccounts.get(2).getName() + ": " + bankAccounts.get(2).getAmount().setScale(4).toString());

        System.out.println(".");
        System.out.println("Broadcasting net settlement ...");
        mepsBean.sendMBSNetSettlement(mbsSettlementAmount);

        System.out.println(".");
        System.out.println("Updating Bank Accounts ...");
        List<SettlementAccount> updatedankAccounts = mepsBean.updateSettlementAccountsBalance(mbsCode, mbsSettlementAmount, citiCode, citiSettlementAmount, ocbcCode, ocbcSettlementAmount);

        System.out.println("Bank Accounts Balance Updated:");
        System.out.println(".       " + updatedankAccounts.get(0).getBankCode() + " " + updatedankAccounts.get(0).getName() + ": " + updatedankAccounts.get(0).getAmount().setScale(4).toString());
        System.out.println(".       " + updatedankAccounts.get(1).getBankCode() + " " + updatedankAccounts.get(1).getName() + ": " + updatedankAccounts.get(1).getAmount().setScale(4).toString());
        System.out.println(".       " + updatedankAccounts.get(2).getBankCode() + " " + updatedankAccounts.get(2).getName() + ": " + updatedankAccounts.get(2).getAmount().setScale(4).toString());

        System.out.println("Sending back meps_settlement response");
        MessageDTO err = new MessageDTO();
        err.setCode(0);
        err.setMessage("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }

}
