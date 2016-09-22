/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author litong
 */
@Stateless
@LocalBean
public class ReportGenerationManagedBean implements Serializable {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ReportGenerationManagedBean() {
    }
    
    public void generateTestReport() throws JRException, IOException {
        String filePath = new File("").getAbsolutePath();
        filePath += "/src/java/report/testReport.jrxml";
        System.out.println(filePath);
        // Compile jrxml file.
        JasperReport jasperReport = JasperCompileManager
                .compileReport(filePath);

          // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();

        // DataSource
        // This is simple example, no database.
        // then using empty datasource.
        JRDataSource dataSource = new JREmptyDataSource();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                parameters, dataSource);

        // Make sure the output directory exists.

        filePath = "/Users/litong/NetBeansProjects/test/test-ejb/StyledTextReportFFFF.pdf";
        System.out.println(filePath);
        // Export to PDF.
        JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
        System.out.println("Done!");
    }
    
    public static void main(String args[]) throws Exception{
        ReportGenerationManagedBean bean = new ReportGenerationManagedBean();
        bean.generateTestReport();
    }
}

