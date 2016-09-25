/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import java.awt.Toolkit;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author litong
 */
@Stateless
@LocalBean
public class ReportGenerationBean implements Serializable {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ReportGenerationBean() {
    }

    public boolean generateTestReport() throws JRException, IOException, SQLException, ClassNotFoundException {
//        String filePath = "/Users/litong/Documents/IS3102/is3102/RetailBankingSystem/InternetBankingSystem/src/java/report/testReport.jrxml";

        //Load Driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
        }
        
        System.out.println("DBAAAAA");
        Connection a;
        a = DriverManager.getConnection("jdbc:mysql://localhost:3306/RetailBankingSystem?zeroDateTimeBehavior=convertToNull", "root", "password");
        
        System.out.println("DB");
        String relativePath = new File("").getAbsolutePath();
        System.out.println("DB2222");
        JasperDesign jd = JRXmlLoader.load("/Users/litong/Documents/IS3102/is3102/RetailBankingSystem/InternetBankingSystem/src/java/report/report.jrxml");
        JasperReport jr = JasperCompileManager.compileReport("/Users/litong/Documents/IS3102/is3102/RetailBankingSystem/InternetBankingSystem/src/java/report/report.jrxml");
        System.out.println("Path");
        
        JasperPrint jp = JasperFillManager.fillReport(jr, new HashMap(), a);
           System.out.println("Path2222");
        //JasperViewer.viewReport(jp);
        System.out.println("View");
//        JasperExportManager.exportReportToPdfFile(jp, "/Users/litong/Downloads/testPDF.pdf");
        System.out.println(relativePath);
        JasperExportManager.exportReportToPdfFile(jp, "/Users/litong/Documents/IS3102/is3102/RetailBankingSystem/InternetBankingSystem/web/request/estatement.pdf");
       
// JasperExportManager.exportReportToPdfFile(jp, "http:///localhost:8181/InternetBankingSystem/src/java/report/testPDF.pdf");
               System.out.println("Done!");
        return true;
    }

    public static void main(String args[]) throws Exception {
        ReportGenerationBean bean = new ReportGenerationBean();
        bean.generateTestReport();
    }
}
