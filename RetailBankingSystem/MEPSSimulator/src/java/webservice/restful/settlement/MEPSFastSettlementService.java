/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.settlement;

import dto.TransactionDTO;
import dto.TransactionSummaryDTO;
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
@Path("meps_fast_settlement")
public class MEPSFastSettlementService {

    @EJB
    private MEPSSessionBean mepsBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("fromBankName") String fromBankName,
            @FormParam("toBankCode") String toBankCode,
            @FormParam("toBankName") String toBankName,
            @FormParam("amount") String netSettlementAmount,
            @FormParam("referenceNumber") String referenceNumber
    ) {

        List<String> mbsTransactions = new ArrayList<String>();
        mbsTransactions.add(referenceNumber);

        System.out.println(".");
        System.out.println("[MEPS]:");
        System.out.println("Received FAST Settlement from SACH:");
        System.out.println(".       " + fromBankCode + " " + fromBankName + " to " + toBankCode + " " + toBankName + ": " + new BigDecimal(netSettlementAmount));

        System.out.println("Received POST http meps_settlement");
        List<SettlementAccount> bankAccounts = mepsBean.retrieveThreeSettlementAccounts("001", "005", "013");
        System.out.println("Current Bank Account Balance:");
        for (SettlementAccount s : bankAccounts) {
            System.out.println(".       " + s.getBankCode() + " " + s.getName() + ": " + s.getAmount().toString());
        }

        System.out.println(".");
        System.out.println("Broadcasting net settlement ...");
        mepsBean.sendMBSFastSettlement(referenceNumber, toBankCode, toBankName, netSettlementAmount);
        System.out.println(".");
        System.out.println("Updating Bank Accounts ...");
        List<SettlementAccount> updatedankAccounts = mepsBean.updateSettlementAccountsBalanceByTransaction(fromBankCode, toBankCode, netSettlementAmount);

        System.out.println("Bank Accounts Balance Updated:");
        for (SettlementAccount s : updatedankAccounts) {
            System.out.println(".       " + s.getBankCode() + " " + s.getName() + ": " + s.getAmount().toString());
        }

        System.out.println("Sending back meps_settlement response");
        MessageDTO err = new MessageDTO();
        err.setCode(0);
        err.setMessage("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }

}
