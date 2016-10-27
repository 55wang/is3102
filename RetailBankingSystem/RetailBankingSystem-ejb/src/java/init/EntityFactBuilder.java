/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.fact.FactSessionBeanLocal;
import entity.customer.MainAccount;
import entity.fact.customer.SinglePortfolioFactTable;
import entity.wealth.Portfolio;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author wang
 */
@Stateless
@LocalBean
public class EntityFactBuilder {

    @EJB
    private FactSessionBeanLocal factSessionBean;

    private String currentDate;
    private String monthStartDate;
    private String monthEndDate;

    public void initSinglePortfolioFact(MainAccount demoMainAccount, Portfolio demoPortfolio, String stockName) {

        initDate();
        String quandl = "https://www.quandl.com/api/v3/datasets/WIKI/"+stockName+".json?column_index=4&start_date=" + monthStartDate + "&transform=diff&api_key=wh4e1aGKQwZyE4RXWP7s";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(quandl);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        System.out.println(jsonString);

        JsonObject dataset = (JsonObject) jsonString.getJsonObject("dataset");
        JsonArray data = (JsonArray) dataset.getJsonArray("data");

        for (int i = 0; i < data.size(); i++) {
            JsonArray innerData = (JsonArray) data.getJsonArray(i);
            System.out.println(innerData.get(0) + " " + innerData.get(1));

            SinglePortfolioFactTable spf = new SinglePortfolioFactTable();
            System.out.println(innerData.get(0).toString());
            Date date;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(innerData.get(0).toString().substring(1, innerData.get(0).toString().length()-1));
                System.out.println(date);
                spf.setCreationDate(date);
            } catch (Exception ex) {
                
                System.out.println(ex);
            }
            
            
            spf.setCustomer(demoMainAccount.getCustomer());
            spf.setPortfolio(demoPortfolio);
            
            Double perc = Double.parseDouble(innerData.get(1).toString()) / 100;
            Double actualChanges = demoPortfolio.getTotalCurrentValue() * perc;
            
            spf.setTotalCurrentValue(demoPortfolio.getTotalCurrentValue() + actualChanges);
            spf.setTotalBuyingValue(demoPortfolio.getTotalBuyingValue());
            factSessionBean.createSinglePortfolioFactTable(spf);

        }

    }

    public void initDate() {
        Date current = new Date();
        setCurrentDate(new SimpleDateFormat("yyyy-MM-dd").format(current));

        Calendar cStart = Calendar.getInstance();   // this takes current date
        cStart.set(Calendar.DAY_OF_MONTH, 1);
        cStart.set(Calendar.MONTH, 8);
        setMonthStartDate(new SimpleDateFormat("yyyy-MM-dd").format(cStart.getTime()));

        Calendar cEnd = Calendar.getInstance();   // this takes current date
        cEnd.set(Calendar.DAY_OF_MONTH, cEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMonthEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cEnd.getTime()));
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getMonthStartDate() {
        return monthStartDate;
    }

    public void setMonthStartDate(String monthStartDate) {
        this.monthStartDate = monthStartDate;
    }

    public String getMonthEndDate() {
        return monthEndDate;
    }

    public void setMonthEndDate(String monthEndDate) {
        this.monthEndDate = monthEndDate;
    }
}
