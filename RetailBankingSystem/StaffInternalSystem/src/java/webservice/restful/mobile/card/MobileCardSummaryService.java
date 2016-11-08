/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardTransactionSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.MobileAccount;
import entity.loan.LoanAccount;
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
import org.primefaces.json.JSONObject;
import server.utilities.DateUtils;
import webservice.restful.mobile.ErrorDTO;
import webservice.restful.mobile.summary.AccountDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_card_summary")
public class MobileCardSummaryService {
    
    @Context
    private UriInfo context;

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;
    @EJB
    private CardAcctSessionBeanLocal cardBean;
    @EJB
    private CardTransactionSessionBeanLocal transactionBean;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getAccountSummary(
            @FormParam("username") String username
    ) {
        System.out.println("Received username:" + username);
        System.out.println("Received POST http with url: /mobile_account_summary");
        String jsonString;
        MainAccount ma;
        try {
            ma = loginBean.getMainAccountByUserID(username);
        } catch (Exception e) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("No user");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }

        if (ma != null) {
            CardSummaryDTO cardSummaryDTO = new CardSummaryDTO();
            
            MobileAccount mobileAccount = mobileBean.getMobileAccountByUserId(ma.getUserID());
            // request to set up mobile password
            AccountDTO mobileAccountDTO = new AccountDTO();
            mobileAccountDTO.setAccountBalance(mobileAccount.getBalance().setScale(2, RoundingMode.UP).toString());
            mobileAccountDTO.setAccountName("Mobile Account");
            mobileAccountDTO.setAccountNumber(mobileAccount.getAccountNumber());
            mobileAccountDTO.setAccountType("MOBILE");
            cardSummaryDTO.setMobileAccount(mobileAccountDTO);
            
            List<CreditCardAccount> ccAccounts = cardBean.getAllActiveCreditCardAccountsByMainId(ma.getId());
            for (CreditCardAccount c : ccAccounts) {
                CardDTO dto = new CardDTO();
                dto.setCcNumber(c.getCreditCardNum());
                dto.setCcType("VISA");
                dto.setOutstandingAmount(c.getOutstandingAmount().toString());
                dto.setNameOnCard(c.getNameOnCard());
                dto.setExpiryDate(DateUtils.getMonthNumber(c.getValidDate()) + "/" + DateUtils.getYearNumber(c.getValidDate()));
                CardTransaction latestTransaction = null;
                try {
                    latestTransaction = transactionBean.getLatestCardTransactionByCcaId(c.getId());
                } catch (Exception e) {
                    System.out.println("No transaction record for credit card");
                }
                if (latestTransaction == null) {
                    dto.setTransferAccount("");
                    dto.setTransferAmount("");
                    dto.setTransferType("");
                    dto.setTransferDate("");
                } else {
                    dto.setTransferAccount(c.getPartialHiddenAccountNumber());
                    dto.setTransferAmount(latestTransaction.getAmount().toString());
                    dto.setTransferType(latestTransaction.getTransactionCode());
                    dto.setTransferDate(DateUtils.readableDate(latestTransaction.getCreateDate()));
                }
                cardSummaryDTO.getCreditCards().add(dto);
            }
            
            jsonString = new JSONObject(cardSummaryDTO).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        } else {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("No such user found!");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }
}
