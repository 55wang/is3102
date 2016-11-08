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
            @FormParam("citiToBankCode") String citiToBankCode,
            @FormParam("citiToBankName") String citiToBankName,
            @FormParam("citiFromBankCode") String citiFromBankCode,
            @FormParam("citiFromBankName") String citiFromBankName,
            @FormParam("citiSettlementAmount") String citiSettlementAmount,
            @FormParam("ocbcToBankCode") String ocbcToBankCode,
            @FormParam("ocbcToBankName") String ocbcToBankName,
            @FormParam("ocbcFromBankCode") String ocbcFromBankCode,
            @FormParam("ocbcFromBankName") String ocbcFromBankName,
            @FormParam("ocbcSettlementAmount") String ocbcSettlementAmount
    ) {
        System.out.println(".");
        System.out.println("[MEPS]:");
        System.out.println("Received Net Settlement from SACH:");
        if (new BigDecimal(citiSettlementAmount).compareTo(BigDecimal.ZERO) == -1) {
            System.out.println(".       " + citiToBankCode + " " + citiToBankName + " to " + citiFromBankCode + " " + citiFromBankName + ": " + new BigDecimal(citiSettlementAmount).setScale(4).toString());
        } else if (new BigDecimal(citiSettlementAmount).compareTo(BigDecimal.ZERO) == 1) {
            System.out.println(".       " + citiFromBankCode + " " + citiFromBankName + " to " + citiToBankCode + " " + citiToBankName + ": " + new BigDecimal(citiSettlementAmount).setScale(4).toString());
        } else {
        }

        if (new BigDecimal(ocbcSettlementAmount).compareTo(BigDecimal.ZERO) == -1) {
            System.out.println(".       " + ocbcToBankCode + " " + ocbcToBankName + " to " + ocbcFromBankCode + " " + ocbcFromBankName + ": " + new BigDecimal(ocbcSettlementAmount).setScale(4).toString());
        } else if (new BigDecimal(ocbcSettlementAmount).compareTo(BigDecimal.ZERO) == 1) {
            System.out.println(".       " + ocbcFromBankCode + " " + ocbcFromBankName + " to " + ocbcToBankCode + " " + ocbcToBankName + ": " + new BigDecimal(ocbcSettlementAmount).setScale(4).toString());
        } else {
        }

        System.out.println("Received POST http meps_settlement");
        System.out.println(citiFromBankCode);
        List<SettlementAccount> bankAccounts = mepsBean.retrieveThreeSettlementAccounts(citiFromBankCode, citiToBankCode, ocbcToBankCode);
        System.out.println("Current Bank Account Balance:");
        for (SettlementAccount s : bankAccounts) {
            System.out.println(".       " + s.getBankCode() + " " + s.getName() + ": " + s.getAmount().setScale(4).toString());
        }

        System.out.println(".");
        System.out.println("Broadcasting net settlement ...");
        mepsBean.sendMBSNetSettlement(citiToBankCode, citiToBankName, citiSettlementAmount, ocbcToBankCode, ocbcToBankName, ocbcSettlementAmount);

        System.out.println(".");
        System.out.println("Updating Bank Accounts ...");
        List<SettlementAccount> updatedankAccounts = mepsBean.updateSettlementAccountsBalance(citiFromBankCode, citiToBankCode, citiSettlementAmount, ocbcFromBankCode, ocbcToBankCode, ocbcSettlementAmount);

        System.out.println("Bank Accounts Balance Updated:");
        for (SettlementAccount s : updatedankAccounts) {
            System.out.println(".       " + s.getBankCode() + " " + s.getName() + ": " + s.getAmount().setScale(4).toString());
        }

        System.out.println("Sending back meps_settlement response");
        MessageDTO err = new MessageDTO();
        err.setCode(0);
        err.setMessage("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }

}
