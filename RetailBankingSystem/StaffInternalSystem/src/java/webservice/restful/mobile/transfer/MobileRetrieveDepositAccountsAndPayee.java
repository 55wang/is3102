/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.bill.BillingOrg;
import entity.bill.Payee;
import entity.card.account.CreditCardAccount;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.MobileAccount;
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
import server.utilities.EnumUtils;
import webservice.restful.mobile.ErrorDTO;
import webservice.restful.mobile.card.CardDTO;
import webservice.restful.mobile.summary.AccountDTO;
import webservice.restful.mobile.summary.AccountSummaryDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_deposit_accounts_and_payee")
public class MobileRetrieveDepositAccountsAndPayee {
    
    @Context
    private UriInfo context;

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;
    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CardAcctSessionBeanLocal cardBean;
    @EJB
    private BillSessionBeanLocal billBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getDepositAccountAndPayee(
            @FormParam("username") String username
    ) {
        System.out.println("Received username:" + username);
        System.out.println("Received POST http with url: /mobile_deposit_accounts_and_payee");
        String jsonString;
        MainAccount ma;
        try {
            ma = mainAccountSessionBean.getMainAccountByUserId(username);
        } catch (Exception e) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("No user");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }

        if (ma != null) {
            AccountSummaryDTO accountSummaryDTO = new AccountSummaryDTO();

            List<CustomerDepositAccount> savingsAccounts = depositBean.getAllNonFixedCustomerAccounts(ma.getId());
            for (CustomerDepositAccount a : savingsAccounts) {
                AccountDTO dto = new AccountDTO();
                dto.setAccountBalance(a.getBalance().toString());
                dto.setAccountName(a.getProduct().getName());
                dto.setAccountNumber(a.getAccountNumber());
                dto.setAccountType("SAVING");
                accountSummaryDTO.getDepositAccounts().add(dto);
            }

            MobileAccount mobileAccount = mobileBean.getMobileAccountByUserId(username);
            AccountDTO dto = new AccountDTO();
            dto.setAccountBalance(mobileAccount.getBalance().toString());
            dto.setAccountName("Mobile Account");
            dto.setAccountNumber(mobileAccount.getAccountNumber());
            dto.setAccountType("MOBILE");
            accountSummaryDTO.getDepositAccounts().add(dto);
            
            List<Payee> merlionPayees = transferBean.getPayeeFromUserIdWithType(ma.getId(), EnumUtils.PayeeType.MERLION);
            List<Payee> otherBankPayees = transferBean.getPayeeFromUserIdWithType(ma.getId(), EnumUtils.PayeeType.LOCAL);
            
            for (Payee p : merlionPayees) {
                PayeeDTO d = new PayeeDTO();
                d.setPayeeAccountName(p.getName());
                d.setPayeeAccountNumber(p.getAccountNumber());
                d.setPayeeId(p.getId().toString());
                d.setPayeeType("MERLION");
                accountSummaryDTO.getMerlionPayee().add(d);
            }
            
            for (Payee p : otherBankPayees) {
                PayeeDTO d = new PayeeDTO();
                d.setPayeeAccountName(p.getName());
                d.setPayeeAccountNumber(p.getAccountNumber());
                d.setPayeeId(p.getId().toString());
                d.setPayeeType("OTHER");
                accountSummaryDTO.getOtherBankPayee().add(d);
            }
            
            // own cc card
            List<CreditCardAccount> ccAccounts = cardBean.getAllActiveCreditCardAccountsByMainId(ma.getId());
            for (CreditCardAccount c : ccAccounts) {
                CardDTO cardDto = new CardDTO();
                cardDto.setCcNumber(c.getCreditCardNum());
                cardDto.setCcType("VISA");
                cardDto.setOutstandingAmount(c.getOutstandingAmount().toString());
                cardDto.setNameOnCard(c.getNameOnCard());
                cardDto.setExpiryDate(DateUtils.getMonthNumber(c.getValidDate()) + "/" + DateUtils.getYearNumber(c.getValidDate()));
                accountSummaryDTO.getMerlionCCBill().add(cardDto);
            }
            
            List<BillingOrg> otherCCBills = billBean.getCreditCardBillingMainAccountId(ma.getId());
            for (BillingOrg b : otherCCBills) {
                BillOrgDTO billDto = new BillOrgDTO();
                billDto.setBillId(b.getId().toString());
                billDto.setBillType("CCBILL");
                billDto.setBillNumber(b.getBillReference());
                billDto.setBillName(b.getOrganization().getName());
                accountSummaryDTO.getOtherCCBill().add(billDto);
            }
            
            List<BillingOrg> otherBills = billBean.getBillingOrgMainAccountId(ma.getId());
            for (BillingOrg b : otherBills) {
                BillOrgDTO billDto = new BillOrgDTO();
                billDto.setBillId(b.getId().toString());
                billDto.setBillType("BILL");
                billDto.setBillNumber(b.getBillReference());
                billDto.setBillName(b.getOrganization().getName());
                accountSummaryDTO.getOtherBill().add(billDto);
            }

            jsonString = new JSONObject(accountSummaryDTO).toString();
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
