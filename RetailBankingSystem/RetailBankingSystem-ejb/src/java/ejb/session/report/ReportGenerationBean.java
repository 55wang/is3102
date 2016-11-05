/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.report;

import ejb.session.bill.TransferSessionBeanLocal;
import entity.common.TransactionRecord;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import server.utilities.DateUtils;

/**
 *
 * @author litong
 */
@Stateless
@LocalBean
public class ReportGenerationBean implements ReportGenerationBeanLocal  {

    @EJB
    private TransferSessionBeanLocal transferBean;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public ReportGenerationBean() {
    }

    @Override
    public boolean generateMonthlyDepositAccountTransactionReport(String accountNumber, Date startDate, Date endDate) {
//        String filePath = "/Users/litong/Documents/IS3102/is3102/RetailBankingSystem/InternetBankingSystem/src/java/report/testReport.jrxml";

        //Load Driver
        String systemUser = System.getProperty("user.name");
        String prependingPath = "";
        if (systemUser.equals("wang")) {
            prependingPath = "/Users/wang/NEW_IS3102/";
        } else if (systemUser.equals("litong")) {
            prependingPath = "/Users/litong/Documents/IS3102/";
        } else if (systemUser.equals("leiyang")) {
            prependingPath = "/Users/leiyang/Desktop/IS3102/workspace/";
        } else if (systemUser.equals("syx")) {

        } else if (systemUser.equals("xiaqing")) {

        } else if (systemUser.equals("yifan")) {

        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
        }
        List<TransactionRecord> rawList = transferBean.getTransactionRecordByAccountNumberStartDateEndDate(accountNumber, startDate, endDate);
        ArrayList<TransactionDTO> dataList = getTransactionDTOList(rawList);
        System.out.println(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);

        Map parameters = new HashMap();
        try {
            JasperReport jr = JasperCompileManager.compileReport(prependingPath+"is3102/RetailBankingSystem/RetailBankingSystem-ejb/src/java/ejb/session/report/report.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, beanColDataSource);
            JasperExportManager.exportReportToPdfFile(jp, prependingPath+"is3102/RetailBankingSystem/InternetBankingSystem/web/personal_request/estatement_"+accountNumber+ DateUtils.getYearNumber(endDate)+ "_" + DateUtils.getMonthNumber(startDate) + "_" + DateUtils.getMonthNumber(endDate) +".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private ArrayList<TransactionDTO> getTransactionDTOList(List<TransactionRecord> transactions) {
        ArrayList<TransactionDTO> transactionList = new ArrayList<>();
        for (TransactionRecord t : transactions) {
            TransactionDTO dto = new TransactionDTO();
            dto.setFromAccount(t.getFromAccount().getAccountNumber());
            dto.setAction(t.getActionType().toString());
            dto.setAmount(t.getAmount().setScale(2, RoundingMode.UP).toString());
            dto.setCreateDate(DateUtils.readableDate(t.getCreationDate()));
            dto.setType(t.getCredit() ? "Credit" : "Debit");
            transactionList.add(dto);
        }
        return transactionList;
    }
}
