/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.summary;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.common.TransactionRecord;
import entity.dams.account.DepositAccount;
import entity.loan.LoanAccount;
import entity.loan.LoanRepaymentRecord;
import java.math.RoundingMode;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import server.utilities.DateUtils;

/**
 *
 * @author leiyang
 */
@Path("mobile_account_history")
public class MobileAccountHistoryService {

    @Context
    private UriInfo context;

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getAccountHistory(
            @FormParam("accountNumber") String accountNumber,
            @FormParam("accountType") String accountType
    ) {
        System.out.println("Received accountNumber:" + accountNumber);
        System.out.println("Received accountType:" + accountType);
        System.out.println("Received POST http with url: /mobile_account_history");
        String jsonString;

        JSONArray ja = new JSONArray();

        if (accountType.equals("LOAN")) {
            LoanAccount la = null;
            try {
                la = loanAccountBean.getLoanAccountByAccountNumber(accountNumber);
            } catch (Exception e) {
                System.out.println("No account retreived");
            }

            if (la != null) {
                for (LoanRepaymentRecord r : la.getLoanRepaymentRecords()) {
                    TransferHistoryDTO dto = new TransferHistoryDTO();
                    dto.setTransferAmount(r.getPaymentAmount().toString());
                    dto.setTransferCredit("DEBIT");
                    dto.setTransferType(r.getType().toString());
                    dto.setTransferDate(DateUtils.readableDate(r.getTransactionDate()));
                    JSONObject jo = new JSONObject(dto);
                    ja.put(jo);
                }
            }
        } else {
            List<TransactionRecord> records = depositBean.transactionRecordFromAccountNumber(accountNumber);
            for (TransactionRecord r : records) {
                TransferHistoryDTO dto = new TransferHistoryDTO();
                dto.setTransferAmount(r.getAmount().toString());
                dto.setTransferCredit(r.getCredit() == null || r.getCredit() ? "CREDIT" : "DEBIT");
                dto.setTransferType(r.getActionType().toString());
                dto.setTransferDate(DateUtils.readableDate(r.getCreationDate()));
                JSONObject jo = new JSONObject(dto);
                ja.put(jo);
            }
        }

        JSONObject mainObj = new JSONObject();
        mainObj.put("history", ja);
        jsonString = mainObj.toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }

}
