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
@Path("meps_settlement")
public class MEPSSettlementService {

    @EJB
    private MEPSSessionBean mepsBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response netSettlement(
            TransactionSummaryDTO transactionSummary
    ) {

        String citiToBankCode = transactionSummary.getCitiToBankCode();
        String citiToBankName = transactionSummary.getCitiToBankName();
        String citiFromBankCode = transactionSummary.getCitiFromBankCode();
        String citiFromBankName = transactionSummary.getCitiFromBankName();
        String citiSettlementAmount = transactionSummary.getCitiSettlementAmount();
        String ocbcToBankCode = transactionSummary.getOcbcToBankCode();
        String ocbcToBankName = transactionSummary.getOcbcToBankName();
        String ocbcFromBankCode = transactionSummary.getOcbcFromBankCode();
        String ocbcFromBankName = transactionSummary.getOcbcFromBankName();
        String ocbcSettlementAmount = transactionSummary.getOcbcSettlementAmount();
        String sentDate = transactionSummary.getDate();
        List<String> mbsTransactions = new ArrayList<String>();
        for (TransactionDTO dto:transactionSummary.getTransactionSummary() ){
            mbsTransactions.add(dto.getReferenceNumber());
        }

        System.out.println(".");
        System.out.println("[MEPS]:");
        System.out.println("Received Net Settlement from SACH:");
        if (new BigDecimal(citiSettlementAmount).compareTo(BigDecimal.ZERO) == -1) {
            System.out.println(".       " + citiToBankCode + " " + citiToBankName + " to " + citiFromBankCode + " " + citiFromBankName + ": " + new BigDecimal(citiSettlementAmount).toString());
        } else if (new BigDecimal(citiSettlementAmount).compareTo(BigDecimal.ZERO) == 1) {
            System.out.println(".       " + citiFromBankCode + " " + citiFromBankName + " to " + citiToBankCode + " " + citiToBankName + ": " + new BigDecimal(citiSettlementAmount).toString());
        } else {
        }

        if (new BigDecimal(ocbcSettlementAmount).compareTo(BigDecimal.ZERO) == -1) {
            System.out.println(".       " + ocbcToBankCode + " " + ocbcToBankName + " to " + ocbcFromBankCode + " " + ocbcFromBankName + ": " + new BigDecimal(ocbcSettlementAmount).toString());
        } else if (new BigDecimal(ocbcSettlementAmount).compareTo(BigDecimal.ZERO) == 1) {
            System.out.println(".       " + ocbcFromBankCode + " " + ocbcFromBankName + " to " + ocbcToBankCode + " " + ocbcToBankName + ": " + new BigDecimal(ocbcSettlementAmount).toString());
        } else {
        }

        System.out.println("Received POST http meps_settlement");
        List<SettlementAccount> bankAccounts = mepsBean.retrieveThreeSettlementAccounts(citiFromBankCode, citiToBankCode, ocbcToBankCode);
        System.out.println("Current Bank Account Balance:");
        for (SettlementAccount s : bankAccounts) {
            System.out.println(".       " + s.getBankCode() + " " + s.getName() + ": " + s.getAmount().toString());
        }

        System.out.println(".");
        System.out.println("Broadcasting net settlement ...");
        mepsBean.sendMBSNetSettlement(transactionSummary);
        System.out.println(".");
        System.out.println("Updating Bank Accounts ...");
        List<SettlementAccount> updatedankAccounts = mepsBean.updateSettlementAccountsBalance(citiFromBankCode, citiToBankCode, citiSettlementAmount, ocbcFromBankCode, ocbcToBankCode, ocbcSettlementAmount);

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
