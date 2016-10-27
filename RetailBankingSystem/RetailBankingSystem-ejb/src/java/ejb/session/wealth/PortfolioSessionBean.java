/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.fact.customer.SinglePortfolioFactTable;
import entity.wealth.MovingAverage;
import entity.wealth.Portfolio;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Stateless
public class PortfolioSessionBean implements PortfolioSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    public static void main(String[] args) {
        Queue<Double> q = new LinkedList();
        
    }
    
    public List<Double> calcMovingAverageValue(Queue<Double> q, int windowSize) {
//        int n = 4;
        List<Double> result = new ArrayList<>();
        Double[] a = new Double[windowSize];
        for (int i=0; i<windowSize; i++) {
            a[i] = 0.0;
        }
        
        Double sum = 0.0;
        for (int i = 1; !q.isEmpty(); i++) {
            sum -= a[i % windowSize];
            a[i % windowSize] = q.poll();
            sum += a[i % windowSize];
            if (i >= windowSize) {
                result.add(sum/windowSize);
            }
        }
        return result;
    }
    
    //must be sorted the input spf, with ascending date order
    @Override
    public void calcMovingAverage(List<SinglePortfolioFactTable> spf) {
        //cut the input
        //calculate the value
        
        //finally store as hashmap
        List<Double> q = new ArrayList<>();
        for (int i = 0; i < spf.size(); i++) {
            Double value = spf.get(i).getTotalCurrentValue();

            if (q.size() == 4) {
                q.remove(0);
                q.add(value);

                Double total = 0.0;
                for (int j = 0; j < q.size(); j++) {
                    total += q.get(j);
                }

                Double avg = total / 4;
                System.out.println("Average: " + avg);
                //persist movingAverage
                MovingAverage movingAvg;
                try {
                    movingAvg = em.find(MovingAverage.class, spf.get(i).getCreationDate());
                } catch (Exception ex) {
                    movingAvg = new MovingAverage();
                    em.persist(movingAvg);
                }

                movingAvg.setCreationDate(spf.get(i).getCreationDate());
                movingAvg.setAvgValue(avg);
                movingAvg.setPortfolio(spf.get(i).getPortfolio());
                em.merge(movingAvg);

            }
        }
    }

    @Override
    public List<Portfolio> getListPortfolios() {
        Query q = em.createQuery("SELECT p FROM Portfolio p");
        return q.getResultList();
    }

    @Override
    public List<Portfolio> getListPortfoliosByCustomerId(Long Id) {
        Query q = em.createQuery("Select p from Portfolio p where p.wealthManagementSubscriber.mainAccount.customer.id=:inId");
        q.setParameter("inId", Id);
        return q.getResultList();
    }

    @Override
    public List<Portfolio> getListPortfoliosByCustomerName(String searchText) {
        Query q = em.createQuery("Select p from Portfolio p where UPPER(p.wealthManagementSubscriber.mainAccount.customer.fullName) like :inSearchText");
        q.setParameter("inSearchText", "%" + searchText.toUpperCase() + "%");
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
