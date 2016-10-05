/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.report;

import ejb.session.bill.TransactionSessionBeanLocal;
import entity.common.TransactionRecord;
import java.awt.Toolkit;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author litong
 */
@Stateless
@LocalBean
public class ReportGenerationBean implements ReportGenerationBeanLocal  {

    @EJB
    private TransactionSessionBeanLocal transactionBean;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public ReportGenerationBean() {
    }

    @Override
    public boolean generateMonthlyDepositAccountTransactionReport(String accountNumber, Date startDate, Date endDate) throws JRException, IOException, SQLException, ClassNotFoundException {
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

//        Connection a = DriverManager.getConnection("jdbc:mysql://localhost:3306/RetailBankingSystem?zeroDateTimeBehavior=convertToNull", "root", "password");

//        String sourceFileName = prependingPath + "is3102/RetailBankingSystem/InternetBankingSystem/web/request/estatement.pdf";
        List<TransactionRecord> rawList = transactionBean.getTransactionRecordByAccountNumberStartDateEndDate(accountNumber, startDate, endDate);
        ArrayList<TransactionDTO> dataList = getTransactionDTOList(rawList);
        System.out.println(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);

        Map parameters = new HashMap();
        try {
//            JasperFillManager.fillReportToFile(sourceFileName, parameters, beanColDataSource);
            JasperReport jr = JasperCompileManager.compileReport(prependingPath+"is3102/RetailBankingSystem/RetailBankingSystem-ejb/src/java/ejb/session/report/report.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, beanColDataSource);
            JasperExportManager.exportReportToPdfFile(jp, prependingPath+"is3102/RetailBankingSystem/InternetBankingSystem/web/personal_request/estatement.pdf");
        } catch (JRException e) {
            e.printStackTrace();
        }

//        JasperReport jr = JasperCompileManager.compileReport(prependingPath+"is3102/RetailBankingSystem/InternetBankingSystem/src/java/report/report.jrxml");
//        JasperPrint jp = JasperFillManager.fillReport(jr, new HashMap(), a);
//        JasperExportManager.exportReportToPdfFile(jp, prependingPath+"is3102/RetailBankingSystem/InternetBankingSystem/web/request/estatement.pdf");
        return true;
    }

    private ArrayList<TransactionDTO> getTransactionDTOList(List<TransactionRecord> transactions) {
        ArrayList<TransactionDTO> transactionList = new ArrayList<>();
        for (TransactionRecord t : transactions) {
            TransactionDTO dto = new TransactionDTO();
            dto.setFromAccount(t.getFromAccount().getAccountNumber());
            dto.setAction(t.getActionType().toString());
            dto.setAmount(t.getAmount().setScale(2, RoundingMode.UP).toString());
            dto.setCreateDate(t.getCreationDate().toString());
            dto.setType(t.getCredit() ? "Credit" : "Debit");
            transactionList.add(dto);
        }
        return transactionList;
    }
}
