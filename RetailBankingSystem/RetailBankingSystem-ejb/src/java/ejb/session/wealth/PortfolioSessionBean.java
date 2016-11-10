/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.Portfolio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils.PortfolioStatus;

/**
 *
 * @author wang
 */
@Stateless
public class PortfolioSessionBean implements PortfolioSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    //call R times series holt winters model
    @Override
    public List<Double> getHoltWinterModel(double[] inputData) {
        System.out.println("getHotWinterModel called " + inputData.toString());
        RConnection connection = null;
        double[] result = new double[5];
        try {
            connection = new RConnection("localhost", 6311);

            String prependingPath = CommonUtils.getPrependFolderName();
            connection.assign("inputTSData", inputData);
            connection.eval("source('" + prependingPath + "ConstructPortfolioModel.R')");
            result = connection.eval("getReturnTimeSeries(inputTSData)").asDoubles();
            System.out.println("result: " + Arrays.toString(result));

        } catch (RserveException | REXPMismatchException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            connection.close();
        }

        List<Double> resultList = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            resultList.add(result[i]);
        }
        System.out.println(resultList.toString());
        return resultList;
    }

    @Override
    public List<Portfolio> getListPortfolios() {
        Query q = em.createQuery("SELECT p FROM Portfolio p");
        return q.getResultList();
    }

    @Override
    public List<Portfolio> getListPortfoliosByCustomerId(String Id) {
        Query q = em.createQuery("Select p from Portfolio p where p.wealthManagementSubscriber.mainAccount.customer.id=:inId AND p.status =:status");
        q.setParameter("inId", Id);
        q.setParameter("status", PortfolioStatus.BOUGHT);
        return q.getResultList();
    }

    @Override
    public Portfolio getPortfolioById(Long Id) {
        return em.find(Portfolio.class, Id);
    }

    @Override
    public Portfolio createPortfolio(Portfolio p) {
        em.persist(p);
        return p;
    }

    @Override
    public Portfolio updatePortfolio(Portfolio p) {
        em.merge(p);
        return p;
    }

}
